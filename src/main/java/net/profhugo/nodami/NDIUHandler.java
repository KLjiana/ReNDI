package net.profhugo.nodami;

//import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

@Mod.EventBusSubscriber
public class NDIUHandler {

	@SubscribeEvent(priority = EventPriority.LOW)
	public static void onEntityHurt(LivingHurtEvent event) {
		if (!event.isCanceled()) {
			LivingEntity entity = event.getEntity();
			if (entity.level().isClientSide()) {
				return;
			}
			DamageSource source = event.getSource();
			Entity trueSource = source.getDirectEntity();
			ResourceLocation trueSourceloc = trueSource != null ? EntityType.getKey(trueSource.getType()) : null;
			if (NDIUConfig.CORE.excludePlayers && entity instanceof Player) {
				return;
			}

			if (NDIUConfig.CORE.excludeAllMobs && !(entity instanceof Player)) {
				return;
			}

			ResourceLocation loc = EntityType.getKey(entity.getType());
			if (NDIUConfig.EXCLUSIONS.dmgReceiveExcludedEntities.contains(loc.toString())) {
				return;
			}

			if (NDIUConfig.EXCLUSIONS.damageSrcWhitelist.contains(source.getMsgId())) {
				return;
			}

			if (trueSource != null) {
				if (NDIUConfig.EXCLUSIONS.attackExcludedEntities.contains(trueSourceloc.toString())) {
					return;
				}

			}
			entity.invulnerableTime = NDIUConfig.CORE.iFrameInterval;
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onPlayerAttack(AttackEntityEvent event) {
		if (!event.isCanceled()) {
			Player player = event.getEntity();
			if (player.level().isClientSide()) {
				return;
			}

			if (player instanceof FakePlayer) {
				return;
			}

			float str = player.getAttackStrengthScale(0);
			if (str <= NDIUConfig.THRESHOLDS.attackCancelThreshold) {
				event.setCanceled(true);
				return;
			}

			if (str <= NDIUConfig.THRESHOLDS.knockbackCancelThreshold) {
				Entity target = event.getTarget();
				// Don't worry, it's only magic
				if (target instanceof LivingEntity) {
					((LivingEntity)target).swinging = true;
				}

			}
		}
	}


	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onLivingKnockBack(LivingKnockBackEvent event) {
		if (!event.isCanceled()) {
			LivingEntity entity = event.getEntity();
			if (entity.swinging) {
				event.setCanceled(true);
				entity.swinging = false;
			}

		}

	}
}
