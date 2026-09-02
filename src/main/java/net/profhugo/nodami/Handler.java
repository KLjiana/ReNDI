package net.profhugo.nodami;

import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

import static net.profhugo.nodami.ReNDI.Config;

public class Handler {
    public static void sendDamageSourceToChat(LivingEntity entity, DamageSource source) {
        if (!Config.damageSourcesToChat || entity.getServer() == null) {
            return;
        }
        Component message = Component.literal("[ReNDI] DamageSource.msgId = " + source.getMsgId());
        entity.getServer().getPlayerList().getPlayers()
                .forEach(player -> player.sendSystemMessage(message));
    }
//
//
//	@SubscribeEvent(priority = EventPriority.LOWEST)
//	public static void onPlayerAttack(AttackEntityEvent event) {
//
//	}
//
//
//	@SubscribeEvent(priority = EventPriority.LOWEST)
//	public static void onLivingKnockBack(LivingKnockBackEvent event) {
//		if (!event.isCanceled()) {
//			LivingEntity entity = event.getEntity();
//			if (entity.swinging) {
//				event.setCanceled(true);
//				entity.swinging = false;
//			}
//
//		}
//
//	}
}
