package net.profhugo.nodami.regression;

import com.mojang.authlib.GameProfile;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;
import net.profhugo.nodami.Config;
import net.profhugo.nodami.Handler;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.lang.reflect.Field;

@Mod("rendi_tests")
@GameTestHolder("rendi")
@PrefixGameTestTemplate(false)
public class RegressionTests {
    private static class ChatPlayer extends FakePlayer {
        private final List<String> messages = new ArrayList<>();
        ChatPlayer(ServerLevel level) { super(level, new GameProfile(UUID.randomUUID(), "DebugTest")); }
        @Override public void sendSystemMessage(Component message) { messages.add(message.getString()); }
    }

    @GameTest(template = "empty")
    @SuppressWarnings("unchecked")
    public static void debugToggleShowsExcludedDamageWithoutChangingRules(GameTestHelper helper) throws ReflectiveOperationException {
        ChatPlayer first = new ChatPlayer(helper.getLevel());
        ChatPlayer second = new ChatPlayer(helper.getLevel());
        // getPlayers() exposes an unmodifiable view; test fixtures need the backing list.
        Field playerField = PlayerList.class.getDeclaredField("players");
        playerField.setAccessible(true);
        List<ServerPlayer> players = (List<ServerPlayer>) playerField.get(helper.getLevel().getServer().getPlayerList());
        boolean previous = Config.DEBUG.damageSourcesToChat;
        boolean hadLava = Config.EXCLUSIONS.damageSrcWhitelist.contains("lava");
        players.add(first);
        players.add(second);
        try {
            Config.EXCLUSIONS.damageSrcWhitelist.add("lava");
            var target = EntityType.ZOMBIE.create(helper.getLevel());
            target.invulnerableTime = 17;
            Config.DEBUG.damageSourcesToChat = false;
            Handler.onEntityHurt(new LivingHurtEvent(target, target.damageSources().lava(), 2));
            helper.assertTrue(first.messages.isEmpty() && second.messages.isEmpty(), "Disabled debug must be silent");
            Config.DEBUG.damageSourcesToChat = true;
            var event = new LivingHurtEvent(target, target.damageSources().lava(), 2);
            Handler.onEntityHurt(event);
            helper.assertTrue(first.messages.equals(List.of("[ReNDI] DamageSource.msgId = lava")), "Chat must show the actual message ID");
            helper.assertTrue(second.messages.equals(first.messages), "Each online player must receive one message");
            helper.assertTrue(target.invulnerableTime == 17 && event.getAmount() == 2 && !event.isCanceled(), "Debugging must preserve excluded damage behavior");
            Config.DEBUG.damageSourcesToChat = false;
            Handler.onEntityHurt(new LivingHurtEvent(target, target.damageSources().lava(), 2));
            helper.assertTrue(first.messages.size() == 1, "Disabling debug again must stop output");
        } finally {
            Config.DEBUG.damageSourcesToChat = previous;
            if (!hadLava) Config.EXCLUSIONS.damageSrcWhitelist.remove("lava");
            players.remove(first);
            players.remove(second);
        }
        helper.succeed();
    }
}
