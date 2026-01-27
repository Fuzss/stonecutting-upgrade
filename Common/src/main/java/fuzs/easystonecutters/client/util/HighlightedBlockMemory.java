package fuzs.easystonecutters.client.util;

import fuzs.easystonecutters.world.item.HammerItem;
import fuzs.easystonecutters.world.item.component.SelectionMode;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public record HighlightedBlockMemory(int interactionRange,
                                     SelectionMode selectionMode,
                                     BlockPos blockPos,
                                     Direction direction,
                                     BlockState blockState) {
    public static final HighlightedBlockMemory EMPTY = new HighlightedBlockMemory(-1,
            SelectionMode.FLAT,
            BlockPos.ZERO,
            Direction.NORTH,
            Blocks.AIR.defaultBlockState());
    private static final int MAX_NEIGHBOR_POSITIONS = 1024;

    public static HighlightedBlockMemory of(BlockGetter blockGetter, Player player, ItemStack itemStack, BlockHitResult hitResult) {
        int interactionRange = HammerItem.getInteractionRange(itemStack, player);
        SelectionMode selectionMode = HammerItem.getSelectionMode(itemStack);
        return new HighlightedBlockMemory(selectionMode.adjustInteractionRange(interactionRange),
                selectionMode,
                hitResult.getBlockPos(),
                hitResult.getDirection(),
                blockGetter.getBlockState(hitResult.getBlockPos()));
    }

    boolean stillValid(BlockGetter blockGetter, @Nullable HitResult hitResult, SelectionMode selectionMode, int interactionRange) {
        if (selectionMode.adjustInteractionRange(interactionRange) == this.interactionRange
                && selectionMode == this.selectionMode) {
            return this.stillValid(blockGetter, hitResult);
        } else {
            return false;
        }
    }

    boolean stillValid(BlockGetter blockGetter, @Nullable HitResult hitResult) {
        if (hitResult != null && hitResult.getType() == HitResult.Type.BLOCK) {
            BlockHitResult blockHitResult = (BlockHitResult) hitResult;
            if (!Objects.equals(this.blockPos, blockHitResult.getBlockPos())) {
                return false;
            } else if (this.direction != blockHitResult.getDirection()) {
                return false;
            } else {
                BlockState blockState = blockGetter.getBlockState(blockHitResult.getBlockPos());
                return Objects.equals(this.blockState, blockState);
            }
        } else {
            return false;
        }
    }

    List<BlockPos> getAllBlockPositions(BlockGetter blockGetter) {
        List<BlockPos> blockPositions = new ArrayList<>();
        BlockPos.breadthFirstTraversal(this.blockPos,
                this.interactionRange,
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

    boolean isSameBlock(BlockState blockState) {
        return this.blockState.is(blockState.getBlock());
    }

    boolean shouldDrawHighlight() {
        return this.interactionRange > 0;
    }
}
