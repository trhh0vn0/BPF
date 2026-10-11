package com.betterpathfinding.betterpathfinding.mixin;

import com.betterpathfinding.betterpathfinding.config.BetterPathfindingConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.level.pathfinder.Path;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={GroundPathNavigation.class})
public abstract class GroundPathNavigationMixin {
    @Unique
    private int betterpathfinding$failCount = 0;
    @Unique
    private long betterpathfinding$throttleUntil = 0L;
    @Unique
    private boolean betterpathfinding$wasThrottled = false;
    @Unique
    private static final int MAX_FAIL = 5;
    @Unique
    private static final long COOLDOWN_MS = 3000L;

    @Inject(method={"createPath(Lnet/minecraft/core/BlockPos;I)Lnet/minecraft/world/level/pathfinder/Path;"}, at={@At(value="HEAD")}, cancellable=true)
    private void betterpathfinding$throttle(BlockPos pos, int accuracy, CallbackInfoReturnable<Path> cir) {
        if (!((Boolean)BetterPathfindingConfig.ENABLED.get()).booleanValue() || !((Boolean)BetterPathfindingConfig.FREQUENCY_CONTROL_ENABLED.get()).booleanValue()) {
            return;
        }
        this.betterpathfinding$wasThrottled = false;
        long now = System.currentTimeMillis();
        if (this.betterpathfinding$throttleUntil > 0L && now >= this.betterpathfinding$throttleUntil) {
            this.betterpathfinding$failCount = 0;
            this.betterpathfinding$throttleUntil = 0L;
        }
        if (this.betterpathfinding$failCount >= 5) {
            this.betterpathfinding$throttleUntil = now + 3000L;
            this.betterpathfinding$wasThrottled = true;
            cir.setReturnValue(null);
        }
    }

    @Inject(method={"createPath(Lnet/minecraft/core/BlockPos;I)Lnet/minecraft/world/level/pathfinder/Path;"}, at={@At(value="RETURN")})
    private void betterpathfinding$track(BlockPos pos, int accuracy, CallbackInfoReturnable<Path> cir) {
        if (this.betterpathfinding$wasThrottled) {
            return;
        }
        if (cir.getReturnValue() == null) {
            ++this.betterpathfinding$failCount;
        } else {
            this.betterpathfinding$failCount = 0;
            this.betterpathfinding$throttleUntil = 0L;
        }
    }
}
