package com.betterpathfinding.betterpathfinding.mixin;

import com.betterpathfinding.betterpathfinding.config.BetterPathfindingConfig;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.animal.Animal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={TemptGoal.class})
public abstract class TemptGoalMixin {
    @Shadow
    protected PathfinderMob mob;

    @Inject(method={"canUse"}, at={@At(value="RETURN")}, cancellable=true)
    private void betterpathfinding$onlyIfCanBreed(CallbackInfoReturnable<Boolean> cir) {
        Animal animal;
        PathfinderMob pathfinderMob;
        if (!((Boolean)BetterPathfindingConfig.ENABLED.get()).booleanValue() || !((Boolean)BetterPathfindingConfig.FIX_POST_FEED_PATH.get()).booleanValue()) {
            return;
        }
        if (((Boolean)cir.getReturnValue()).booleanValue() && (pathfinderMob = this.mob) instanceof Animal && !(animal = (Animal)pathfinderMob).isBaby() && (!animal.canFallInLove() || animal.getAge() != 0)) {
            cir.setReturnValue((Object)false);
        }
    }
}
