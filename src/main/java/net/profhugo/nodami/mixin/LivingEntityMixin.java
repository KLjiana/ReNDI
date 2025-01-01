package net.profhugo.nodami.mixin;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.profhugo.nodami.ReNDI.Config;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(method = "actuallyHurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;isInvulnerableTo(Lnet/minecraft/world/damagesource/DamageSource;)Z"))
    public void onEntityHurt(DamageSource source, float f, CallbackInfo ci){
        LivingEntity entity = (LivingEntity) (Object)this;
        if (entity.level().isClientSide()) {
            return;
        }
        Entity trueSource = source.getDirectEntity();
        ResourceLocation trueSourceloc = trueSource != null ? EntityType.getKey(trueSource.getType()) : null;
        if (Config.excludeAllMobs) {
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

    @Inject(method = "knockback", at = @At("HEAD"), cancellable = true)
    public void onLivingKnockBack(double pStrength, double pX, double pZ, CallbackInfo ci){
        LivingEntity entity = (LivingEntity) (Object)this;
        if (entity.swinging) {
            ci.cancel();
            entity.swinging = false;
        }
    }
}
