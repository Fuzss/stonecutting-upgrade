package fuzs.easystonecutters.client.util;

import fuzs.easystonecutters.world.item.HammerItem;
import fuzs.easystonecutters.world.item.component.SelectionMode;
import fuzs.easystonecutters.world.item.crafting.HammeringRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.Util;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SelectableRecipe;
import net.minecraft.world.item.crafting.display.SlotDisplayContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class HighlightedBlocksHolder {
    public static final HighlightedBlocksHolder EMPTY = Util.make(new HighlightedBlocksHolder(HighlightedBlockMemory.EMPTY),
            (HighlightedBlocksHolder highlightedBlocks) -> {
                highlightedBlocks.ticksSinceLastCheck = -1;
                highlightedBlocks.cachedBlockPositions = Collections.emptyList();
                highlightedBlocks.cachedVoxelShape = Shapes.empty();
                highlightedBlocks.recipes = SelectableRecipe.SingleInputSet.empty();
            });
    private static final int DEFAULT_TICKS_SINCE_LAST_CHECK = 20;

    private final HighlightedBlockMemory blockMemory;
    private int ticksSinceLastCheck;
    @Nullable
    private List<BlockPos> cachedBlockPositions;
    @Nullable
    private VoxelShape cachedVoxelShape;
    private SelectableRecipe.@Nullable SingleInputSet<HammeringRecipe> recipes;
    private int selectedRecipe;

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

    public boolean stillValid(BlockGetter blockGetter, Player player, ItemStack itemStack, BlockHitResult hitResult) {
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
        if (this.blockMemory.shouldDrawHighlight()) {
            for (BlockPos blockPos : this.getBlockPositionsForRecipe(blockGetter)) {
                voxelShape = Shapes.joinUnoptimized(voxelShape,
                        Shapes.create(new AABB(blockPos).inflate(0.005)),
                        BooleanOp.OR);
            }
        }

        return voxelShape;
    }

    public List<BlockPos> getBlockPositionsForRecipe(BlockGetter blockGetter) {
        boolean hasRecipe = this.getRecipe() != null;
        if (this.cachedBlockPositions == null) {
            if (hasRecipe) {
                return this.cachedBlockPositions = this.blockMemory.getAllBlockPositions(blockGetter);
            } else {
                return this.cachedBlockPositions = Collections.emptyList();
            }
        } else {
            return hasRecipe ? this.cachedBlockPositions : Collections.emptyList();
        }
    }

    public BlockPos getBlockPos() {
        return this.blockMemory.blockPos();
    }

    public boolean updateSelectedRecipe(int indexOffset) {
        SelectableRecipe.SingleInputSet<HammeringRecipe> recipes = this.getRecipes();
        if (!recipes.isEmpty()) {
            this.selectedRecipe = Mth.positiveModulo(this.selectedRecipe + indexOffset, recipes.size());
            return true;
        } else {
            return false;
        }
    }

    public ItemStack getRecipeOutput(int indexOffset) {
        SelectableRecipe.SingleInputSet<HammeringRecipe> recipes = this.getRecipes();
        if (recipes.isEmpty()) {
            return ItemStack.EMPTY;
        } else {
            SelectableRecipe<HammeringRecipe> recipe = this.getRecipeByIndex(recipes,
                    this.selectedRecipe + indexOffset);
            ClientLevel clientLevel = Minecraft.getInstance().level;
            ContextMap contextMap = SlotDisplayContext.fromLevel(clientLevel);
            return recipe.optionDisplay().resolveForFirstStack(contextMap);
        }
    }

    public @Nullable RecipeHolder<HammeringRecipe> getRecipe() {
        SelectableRecipe.SingleInputSet<HammeringRecipe> recipes = this.getRecipes();
        return recipes.isEmpty() ? null : this.getRecipeByIndex(recipes, this.selectedRecipe).recipe().orElseThrow();
    }

    private SelectableRecipe.SingleInputSet<HammeringRecipe> getRecipes() {
        return Objects.requireNonNullElseGet(this.recipes, () -> {
            return this.recipes = ClientRecipeHelper.getRecipes()
                    .selectByInput(new ItemStack(this.blockMemory.blockState().getBlock()));
        });
    }

    private SelectableRecipe<HammeringRecipe> getRecipeByIndex(SelectableRecipe.SingleInputSet<HammeringRecipe> recipes, int recipeIndex) {
        return recipes.entries().get(Mth.positiveModulo(recipeIndex, recipes.size())).recipe();
    }
}
