package com.betterpathfinding.betterpathfinding.mixin;

import com.betterpathfinding.betterpathfinding.config.BetterPathfindingConfig;
import java.util.Map;
import java.util.WeakHashMap;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={TargetingConditions.class})
public abstract class TargetingConditionsMixin {
    @Unique
    private static final Map<LivingEntity, Long> betterpathfinding$lastSeenTime = new WeakHashMap<LivingEntity, Long>();
    @Unique
    private static final Map<LivingEntity, LivingEntity> betterpathfinding$lastSeenTarget = new WeakHashMap<LivingEntity, LivingEntity>();
    @Unique
    private static final long GRACE_PERIOD_TICKS = 40L;

    @Inject(method={"test"}, at={@At(value="RETURN")}, cancellable=true)
    private void betterpathfinding$targetGrace(LivingEntity shooter, LivingEntity target, CallbackInfoReturnable<Boolean> cir) {
        if (!((Boolean)BetterPathfindingConfig.ENABLED.get()).booleanValue() || !((Boolean)BetterPathfindingConfig.FIX_SIGHT_ON_TRANSPARENT.get()).booleanValue()) {
            return;
        }
        if (((Boolean)cir.getReturnValue()).booleanValue()) {
            betterpathfinding$lastSeenTime.put(shooter, target.level().getGameTime());
            betterpathfinding$lastSeenTarget.put(shooter, target);
            return;
        }
        if (target == null || !target.isAlive()) {
            return;
        }
        if (target.isSpectator()) {
            return;
        }
        if (!target.isAttackable()) {
            return;
        }
        if (shooter.distanceToSqr((Entity)target) > 4096.0) {
            return;
        }
        Long lastSeen = betterpathfinding$lastSeenTime.get(shooter);
        LivingEntity lastTarget = betterpathfinding$lastSeenTarget.get(shooter);
        if (lastSeen != null && target.level().getGameTime() - lastSeen < 40L && target.equals((Object)lastTarget)) {
            cir.setReturnValue((Object)true);
        }
    }
}
