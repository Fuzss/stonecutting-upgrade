package fuzs.easystonecutters.client.util;

import fuzs.easystonecutters.world.entity.attachment.SelectedHammeringBlocks;
import fuzs.easystonecutters.world.item.HammerItem;
import fuzs.easystonecutters.world.item.component.SelectionMode;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Util;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class HighlightedBlocksHolder {
    private static final int DEFAULT_TICKS_SINCE_LAST_CHECK = 20;
    public static final HighlightedBlocksHolder EMPTY = Util.make(new HighlightedBlocksHolder(HighlightedBlockMemory.EMPTY),
            (HighlightedBlocksHolder highlightedBlocks) -> {
                highlightedBlocks.ticksSinceLastCheck = -1;
                highlightedBlocks.cachedBlockPositions = Collections.emptyList();
                highlightedBlocks.cachedVoxelShape = Shapes.empty();
            });

    private final HighlightedBlockMemory blockMemory;
    private int ticksSinceLastCheck;
    @Nullable
    private List<BlockPos> cachedBlockPositions;
    @Nullable
    private VoxelShape cachedVoxelShape;

    public HighlightedBlocksHolder(HighlightedBlockMemory blockMemory) {
        this.blockMemory = blockMemory;
    }

    public boolean isEmpty() {
        return this.blockMemory == HighlightedBlockMemory.EMPTY;
    }

    public void testAllBlocks(BlockGetter blockGetter, @Nullable HitResult hitResult) {
        if (!this.isEmpty() && this.blockMemory.stillValid(blockGetter, hitResult)) {
            if (this.cachedBlockPositions != null && this.ticksSinceLastCheck != -1
                    && --this.ticksSinceLastCheck == 0) {
                this.ticksSinceLastCheck = DEFAULT_TICKS_SINCE_LAST_CHECK;
                if (!this.cachedBlockPositions.isEmpty() && this.cachedBlockPositions.stream()
                        .anyMatch((BlockPos blockPos) -> {
                            return !this.blockMemory.isSameBlock(blockGetter.getBlockState(blockPos));
                        })) {
                    this.cachedBlockPositions = null;
                    this.cachedVoxelShape = null;
                }
            }
        }
    }

    public boolean stillValid(BlockGetter blockGetter, Player player, ItemStack itemStack, @Nullable HitResult hitResult) {
        int interactionRange = HammerItem.getInteractionRange(itemStack, player);
        SelectionMode selectionMode = HammerItem.getSelectionMode(itemStack);
        return this.blockMemory.stillValid(blockGetter, hitResult, selectionMode, interactionRange);
    }

    public VoxelShape getJoinedShape(BlockGetter blockGetter) {
        return Objects.requireNonNullElseGet(this.cachedVoxelShape,
                () -> this.cachedVoxelShape = this.createJoinedShape(blockGetter));
    }

    private VoxelShape createJoinedShape(BlockGetter blockGetter) {
        VoxelShape voxelShape = Shapes.empty();
        for (BlockPos blockPos : this.getAllBlockPositions(blockGetter)) {
            voxelShape = Shapes.joinUnoptimized(voxelShape,
                    Shapes.create(new AABB(blockPos).inflate(0.005)),
                    BooleanOp.OR);
        }

        return voxelShape;
    }

    public List<BlockPos> getAllBlockPositions(BlockGetter blockGetter) {
        return Objects.requireNonNullElseGet(this.cachedBlockPositions,
                () -> this.cachedBlockPositions = this.blockMemory.getAllBlockPositions(blockGetter));
    }

    public SelectedHammeringBlocks pack(BlockGetter blockGetter) {
        return new SelectedHammeringBlocks(this.blockMemory.blockPos(), this.getAllBlockPositions(blockGetter));
    }
}
