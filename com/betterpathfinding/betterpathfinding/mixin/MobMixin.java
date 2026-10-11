package com.betterpathfinding.betterpathfinding.mixin;

import com.betterpathfinding.betterpathfinding.config.BetterPathfindingConfig;
import java.util.List;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={Mob.class})
public abstract class MobMixin {
    @Shadow
    protected PathNavigation navigation;
    @Shadow
    protected MoveControl moveControl;
    @Unique
    private Vec3 betterpathfinding$lastCheckPos = Vec3.ZERO;
    @Unique
    private int betterpathfinding$stuckTicks = 0;
    @Unique
    private static final int STUCK_CHECK_TICKS = 60;
    @Unique
    private static final double CROWD_RADIUS = 0.7;
    @Unique
    private static final double CROWD_PUSH = 0.05;

    @Shadow
    public abstract LivingEntity getTarget();

    @Inject(method={"aiStep"}, at={@At(value="HEAD")})
    private void betterpathfinding$stuckCheck(CallbackInfo ci) {
        if (!((Boolean)BetterPathfindingConfig.ENABLED.get()).booleanValue() || !((Boolean)BetterPathfindingConfig.FIX_STUCK_DETECTION.get()).booleanValue()) {
            return;
        }
        Mob self = (Mob)this;
        if (self.level().isClientSide()) {
            return;
        }
        if (this.navigation == null || this.navigation.isDone()) {
            return;
        }
        if (self.tickCount % 20 != 0) {
            return;
        }
        Vec3 pos = self.position();
        if (this.betterpathfinding$lastCheckPos == Vec3.ZERO) {
            this.betterpathfinding$lastCheckPos = pos;
            return;
        }
        double moved = pos.distanceToSqr(this.betterpathfinding$lastCheckPos);
        this.betterpathfinding$stuckTicks = moved < 0.02 ? (this.betterpathfinding$stuckTicks += 20) : 0;
        if (this.betterpathfinding$stuckTicks >= 60) {
            this.betterpathfinding$stuckTicks = 0;
            if (this.navigation.getPath() != null && !this.navigation.isDone()) {
                this.navigation.recomputePath();
            }
        }
        this.betterpathfinding$lastCheckPos = pos;
    }

    @Inject(method={"aiStep"}, at={@At(value="HEAD")})
    private void betterpathfinding$crowdSeparation(CallbackInfo ci) {
        if (!((Boolean)BetterPathfindingConfig.ENABLED.get()).booleanValue() || !((Boolean)BetterPathfindingConfig.STEERING_ENABLED.get()).booleanValue()) {
            return;
        }
        Mob self = (Mob)this;
        if (self.level().isClientSide()) {
            return;
        }
        if (self.tickCount % 5 != 0) {
            return;
        }
        if (!this.moveControl.hasWanted()) {
            return;
        }
        double r = 0.7 + (double)self.getBbWidth() * 0.5;
        AABB box = self.getBoundingBox().inflate(r, r * 0.5, r);
        List nearby = self.level().getEntities(EntityTypeTest.forClass(Mob.class), box, other -> other != self && other.getType() == self.getType());
        Vec3 push = Vec3.ZERO;
        for (Mob other2 : nearby) {
            Vec3 diff = self.position().subtract(other2.position());
            double dist = diff.length();
            if (!(dist > 0.01) || !(dist < r)) continue;
            push = push.add(diff.normalize().scale(0.05 * (1.0 - dist / r)));
        }
        if (push.lengthSqr() > 1.0E-4) {
            self.setDeltaMovement(self.getDeltaMovement().add(push.x, 0.0, push.z));
        }
    }

    @Inject(method={"aiStep"}, at={@At(value="TAIL")})
    private void betterpathfinding$pathRecovery(CallbackInfo ci) {
        if (!((Boolean)BetterPathfindingConfig.ENABLED.get()).booleanValue()) {
            return;
        }
        Mob self = (Mob)this;
        if (self.level().isClientSide()) {
            return;
        }
        if (this.navigation == null) {
            return;
        }
        LivingEntity target = this.getTarget();
        if (this.moveControl.hasWanted() && (this.navigation.getPath() == null || this.navigation.isDone()) && target != null) {
            this.navigation.moveTo(target.getX(), target.getY(), target.getZ(), 1.0);
        }
    }
}
