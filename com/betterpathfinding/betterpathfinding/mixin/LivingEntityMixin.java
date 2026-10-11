package com.betterpathfinding.betterpathfinding.mixin;

import com.betterpathfinding.betterpathfinding.config.BetterPathfindingConfig;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={LivingEntity.class})
public abstract class LivingEntityMixin {
    @Inject(method={"hasLineOfSight"}, at={@At(value="RETURN")}, cancellable=true)
    private void betterpathfinding$checkVisualLineOfSight(Entity target, CallbackInfoReturnable<Boolean> cir) {
        boolean canSeeVisual;
        if (!((Boolean)BetterPathfindingConfig.ENABLED.get()).booleanValue() || !((Boolean)BetterPathfindingConfig.FIX_SIGHT_ON_TRANSPARENT.get()).booleanValue()) {
            return;
        }
        if (((Boolean)cir.getReturnValue()).booleanValue()) {
            return;
        }
        LivingEntity self = (LivingEntity)this;
        Vec3 eyePos = new Vec3(self.getX(), self.getEyeY(), self.getZ());
        Vec3 targetPos = new Vec3(target.getX(), target.getEyeY(), target.getZ());
        if (targetPos.distanceToSqr(eyePos) > 16384.0) {
            return;
        }
        boolean bl = canSeeVisual = self.level().clip(new ClipContext(eyePos, targetPos, ClipContext.Block.VISUAL, ClipContext.Fluid.NONE, (Entity)self)).getType() == HitResult.Type.MISS;
        if (canSeeVisual) {
            cir.setReturnValue((Object)true);
        }
    }
}
