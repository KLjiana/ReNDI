package net.profhugo.nodami.mixin;

import net.fabricmc.fabric.api.entity.FakePlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.profhugo.nodami.ReNDI.Config;

@Mixin(Player.class)
public class PlayerMixin {
    @Inject(method = "attack", at = @At("HEAD"), cancellable = true)
    public void onPlayerAttack(Entity target, CallbackInfo ci){
        Player player = (Player) (Object) this;
        if (player.level().isClientSide()) {
            return;
        }

        if (player instanceof FakePlayer) {
            return;
        }

        float str = player.getAttackStrengthScale(0);
        if (str <= Config.attackCancelThreshold) {
            ci.cancel();
            return;
        }

        if (str <= Config.knockbackCancelThreshold) {
            // Don't worry, it's only magic
            if (target instanceof LivingEntity livingEntity) {
                livingEntity.swinging = true;
            }
        }
    }

    @Inject(method = "actuallyHurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;isInvulnerableTo(Lnet/minecraft/world/damagesource/DamageSource;)Z", shift = At.Shift.AFTER))
    public void onEntityHurt(DamageSource source, float f, CallbackInfo ci){
        LivingEntity entity = (LivingEntity) (Object)this;
        if (entity.level().isClientSide()) {
            return;
        }
        Entity trueSource = source.getDirectEntity();
        ResourceLocation trueSourceloc = trueSource != null ? EntityType.getKey(trueSource.getType()) : null;
        if (Config.excludePlayers) {
            return;
        }

        ResourceLocation loc = EntityType.getKey(entity.getType());
        if (Config.dmgReceiveExcludedEntities.contains(loc.toString())) {
            return;
        }

        if (Config.damageSrcWhitelist.contains(source.getMsgId())) {
            return;
        }

        if (trueSource != null) {
            if (Config.attackExcludedEntities.contains(trueSourceloc.toString())) {
                return;
            }

        }
        entity.invulnerableTime = Config.iFrameInterval;
    }
}
