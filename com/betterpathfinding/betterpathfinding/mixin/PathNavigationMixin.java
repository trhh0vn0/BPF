package com.betterpathfinding.betterpathfinding.mixin;

import com.betterpathfinding.betterpathfinding.config.BetterPathfindingConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={PathNavigation.class})
public abstract class PathNavigationMixin {
    @Shadow
    @Final
    protected Mob mob;
    @Unique
    private BlockPos betterpathfinding$cacheTarget = BlockPos.ZERO;
    @Unique
    private BlockPos betterpathfinding$cacheStart = BlockPos.ZERO;
    @Unique
    private long betterpathfinding$cacheTime = 0L;
    @Unique
    private static final long CACHE_TTL = 20L;

    @Inject(method={"moveTo(DDDD)Z"}, at={@At(value="HEAD")}, cancellable=true)
    private void betterpathfinding$skipDuplicate(double x, double y, double z, double speed, CallbackInfoReturnable<Boolean> cir) {
        if (!((Boolean)BetterPathfindingConfig.ENABLED.get()).booleanValue() || !((Boolean)BetterPathfindingConfig.PATH_CACHE_ENABLED.get()).booleanValue()) {
            return;
        }
        BlockPos target = BlockPos.containing((double)x, (double)y, (double)z);
        BlockPos start = this.mob.blockPosition();
        long now = this.mob.level().getGameTime();
        if (start.equals((Object)this.betterpathfinding$cacheStart) && target.equals((Object)this.betterpathfinding$cacheTarget) && now - this.betterpathfinding$cacheTime < 20L) {
            cir.setReturnValue((Object)true);
        }
    }

    @Inject(method={"moveTo(DDDD)Z"}, at={@At(value="RETURN")})
    private void betterpathfinding$cacheStore(double x, double y, double z, double speed, CallbackInfoReturnable<Boolean> cir) {
        if (((Boolean)cir.getReturnValue()).booleanValue()) {
            this.betterpathfinding$cacheStart = this.mob.blockPosition();
            this.betterpathfinding$cacheTarget = BlockPos.containing((double)x, (double)y, (double)z);
            this.betterpathfinding$cacheTime = this.mob.level().getGameTime();
        }
    }
}
