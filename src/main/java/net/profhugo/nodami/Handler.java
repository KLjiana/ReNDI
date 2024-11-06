package net.profhugo.nodami;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.event.entity.living.ArmorHurtEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingKnockBackEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;

@EventBusSubscriber
public class Handler {

	@SubscribeEvent(priority = EventPriority.LOW)
	public static void onEntityHurt(LivingIncomingDamageEvent event) {
		if (!event.isCanceled()) {
			LivingEntity entity = event.getEntity();
			if (entity.level().isClientSide()) {
				return;
			}
			DamageSource source = event.getSource();
			Entity trueSource = source.getDirectEntity();
			ResourceLocation trueSourceloc = trueSource != null ? EntityType.getKey(trueSource.getType()) : null;
			if (Config.CORE.excludePlayers && entity instanceof Player) {
				return;
			}

			if (Config.CORE.excludeAllMobs && !(entity instanceof Player)) {
				return;
			}

			ResourceLocation loc = EntityType.getKey(entity.getType());
			if (Config.EXCLUSIONS.dmgReceiveExcludedEntities.contains(loc.toString())) {
				return;
			}

			if (Config.EXCLUSIONS.damageSrcWhitelist.contains(source.getMsgId())) {
				return;
			}

			if (trueSource != null) {
				if (Config.EXCLUSIONS.attackExcludedEntities.contains(trueSourceloc.toString())) {
					return;
				}

			}
			entity.invulnerableTime = Config.CORE.iFrameInterval;
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
			if (str <= Config.THRESHOLDS.attackCancelThreshold) {
				event.setCanceled(true);
				return;
			}

			if (str <= Config.THRESHOLDS.knockbackCancelThreshold) {
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
