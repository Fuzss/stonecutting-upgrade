package fuzs.easystonecutters.client.handler;

import fuzs.easystonecutters.world.item.component.SelectionMode;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public record HighlightedBlockMemory(int maxDepth,
                                     SelectionMode selectionMode,
                                     BlockPos blockPos,
                                     Direction direction,
                                     BlockState blockState) {
    private static final int MAX_NEIGHBOR_POSITIONS = 1024;

    public static HighlightedBlockMemory of(int maxDepth, SelectionMode selectionMode, BlockHitResult hitResult, BlockGetter blockGetter) {
        return new HighlightedBlockMemory(selectionMode.adjustMaxDepth(maxDepth),
                selectionMode,
                hitResult.getBlockPos(),
                hitResult.getDirection(),
                blockGetter.getBlockState(hitResult.getBlockPos()));
    }

    public boolean stillValid(int maxDepth, @Nullable SelectionMode selectionMode, @Nullable HitResult hitResult, @Nullable BlockGetter blockGetter) {
        if ((maxDepth == -1 || maxDepth == this.maxDepth) && (selectionMode == null
                || selectionMode == this.selectionMode)) {
            if (hitResult != null && hitResult.getType() == HitResult.Type.BLOCK && blockGetter != null) {
                BlockHitResult blockHitResult = (BlockHitResult) hitResult;
                return this.blockPos.equals(blockHitResult.getBlockPos())
                        && this.direction == blockHitResult.getDirection()
                        && this.blockState.equals(blockGetter.getBlockState(blockHitResult.getBlockPos()));
            }
        }

        return false;
    }

    public List<BlockPos> getAllBlockPositions(BlockGetter blockGetter) {
        List<BlockPos> blockPositions = new ArrayList<>();
        BlockPos.breadthFirstTraversal(this.blockPos,
                this.maxDepth,
                MAX_NEIGHBOR_POSITIONS,
                (BlockPos blockPos, Consumer<BlockPos> blockPosConsumer) -> {
                    for (BlockPos neighborPosition : this.getNeighborPositions()) {
                        blockPosConsumer.accept(blockPos.offset(neighborPosition));
                    }
                },
                (BlockPos blockPos) -> {
                    if (this.isSameBlock(blockGetter.getBlockState(blockPos))) {
                        blockPositions.add(blockPos);
                    }

                    return BlockPos.TraversalNodeStatus.ACCEPT;
                });
        return blockPositions;
    }

    private List<BlockPos> getNeighborPositions() {
        return this.selectionMode.selectNeighborPositions(this.blockState, this.direction);
    }

    public boolean isSameBlock(BlockState blockState) {
        return this.blockState.is(blockState.getBlock());
    }
}
