package fuzs.easystonecutters.client.handler;

import fuzs.easystonecutters.client.PocketStonecutterCyclingProvider;
import fuzs.easystonecutters.world.item.crafting.TransmutationInWorldRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

import java.util.List;

public final class HighlightedBlocksHolder {
    private static final int DEFAULT_TICKS_SINCE_LAST_CHECK = 20;

    private final HighlightedBlockMemory blockMemory;
    private int ticksSinceLastCheck;
    @Nullable
    private List<BlockPos> cachedBlockPositions;
    @Nullable
    private VoxelShape cachedVoxelShape;

    public HighlightedBlocksHolder(HighlightedBlockMemory blockMemory) {
        this.blockMemory = blockMemory;
    }

    public void testAllBlocks(@Nullable HitResult hitResult, BlockGetter blockGetter) {
        if (this.blockMemory.stillValid(-1, null, hitResult, blockGetter)) {
            if (this.cachedBlockPositions != null && --this.ticksSinceLastCheck <= 0) {
                this.ticksSinceLastCheck = DEFAULT_TICKS_SINCE_LAST_CHECK;
                if (this.cachedBlockPositions.stream().anyMatch((BlockPos blockPos) -> {
                    return !this.blockMemory.isSameBlock(blockGetter.getBlockState(blockPos));
                })) {
                    this.cachedBlockPositions = null;
                    this.cachedVoxelShape = null;
                }
            }
        }
    }

    public VoxelShape getJoinedShape(BlockGetter blockGetter) {
        if (this.cachedVoxelShape == null) {
            return this.cachedVoxelShape = this.createJoinedShape(blockGetter);
        } else {
            return this.cachedVoxelShape;
        }
    }

    private VoxelShape createJoinedShape(BlockGetter blockGetter) {
        VoxelShape voxelShape = Shapes.empty();
        for (BlockPos blockPos : this.getBlocksForRecipe(blockGetter)) {
            voxelShape = Shapes.joinUnoptimized(voxelShape,
                    Shapes.create(new AABB(blockPos).inflate(0.005)),
                    BooleanOp.OR);
        }

        return voxelShape;
    }

    public List<BlockPos> getBlocksForRecipe(BlockGetter blockGetter) {
        boolean hasRecipe = PocketStonecutterCyclingProvider.getSelectedRecipeHolder() != null;
        if (this.cachedBlockPositions == null) {
            if (hasRecipe) {
                return this.cachedBlockPositions = this.blockMemory.getAllBlockPositions(blockGetter);
            } else {
                return this.cachedBlockPositions = List.of();
            }
        } else {
            return hasRecipe ? this.cachedBlockPositions : List.of();
        }
    }

    public BlockPos getBlockPos() {
        return this.blockMemory.blockPos();
    }

    @Nullable
    public Block getCraftingResult() {
        return PocketStonecutterCyclingProvider.getSelectedRecipeHolder() != null ?
                PocketStonecutterCyclingProvider.getSelectedRecipeHolder().value().getBlockResult() : null;
    }

    @Nullable
    public RecipeHolder<TransmutationInWorldRecipe> getRecipe() {
        return PocketStonecutterCyclingProvider.getSelectedRecipeHolder();
    }
}
