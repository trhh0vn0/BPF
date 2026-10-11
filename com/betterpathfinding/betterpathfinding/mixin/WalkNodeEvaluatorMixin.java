package com.betterpathfinding.betterpathfinding.mixin;

import com.betterpathfinding.betterpathfinding.config.BetterPathfindingConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={WalkNodeEvaluator.class})
public abstract class WalkNodeEvaluatorMixin {
    @Inject(method={"getPathTypeFromState"}, at={@At(value="RETURN")}, cancellable=true)
    private static void betterpathfinding$adjustPathType(BlockGetter level, BlockPos pos, CallbackInfoReturnable<PathType> cir) {
        if (!((Boolean)BetterPathfindingConfig.ENABLED.get()).booleanValue()) {
            return;
        }
        PathType type = (PathType)cir.getReturnValue();
        if (type == PathType.FENCE) {
            cir.setReturnValue((Object)PathType.WALKABLE);
        } else if (type == PathType.DOOR_WOOD_CLOSED || type == PathType.TRAPDOOR) {
            cir.setReturnValue((Object)PathType.WALKABLE_DOOR);
        } else if (type == PathType.STICKY_HONEY) {
            cir.setReturnValue((Object)PathType.BREACH);
        }
    }

    @Inject(method={"getNeighbors([Lnet/minecraft/world/level/pathfinder/Node;Lnet/minecraft/world/level/pathfinder/Node;)I"}, at={@At(value="RETURN")})
    private void betterpathfinding$addFallCost(Node[] neighbors, Node parent, CallbackInfoReturnable<Integer> cir) {
        if (!((Boolean)BetterPathfindingConfig.ENABLED.get()).booleanValue() || !((Boolean)BetterPathfindingConfig.FIX_FALL_DAMAGE_PATH.get()).booleanValue()) {
            return;
        }
        int count = (Integer)cir.getReturnValue();
        for (int i = 0; i < count; ++i) {
            int drop;
            Node neighbor = neighbors[i];
            if (neighbor == null || (drop = parent.y - neighbor.y) <= 0) continue;
            neighbor.costMalus += (float)drop * 2.0f;
        }
    }
}
