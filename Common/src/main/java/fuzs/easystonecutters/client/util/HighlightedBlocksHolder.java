package fuzs.easystonecutters.client.util;

import fuzs.easystonecutters.world.item.HammerItem;
import fuzs.easystonecutters.world.item.component.SelectionMode;
import fuzs.easystonecutters.world.item.crafting.HammeringRecipe;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.Mth;
import net.minecraft.util.Util;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SelectableRecipe;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.joml.Vector2i;
import org.joml.Vector2ic;
import org.jspecify.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class HighlightedBlocksHolder {
    private static final Object2ObjectMap<Holder<Block>, Vector2i> SELECTED_RECIPE_INDICES = new Object2ObjectLinkedOpenHashMap<>();
    private static final int DEFAULT_TICKS_SINCE_LAST_CHECK = 20;
    public static final HighlightedBlocksHolder EMPTY = Util.make(new HighlightedBlocksHolder(HighlightedBlockMemory.EMPTY),
            (HighlightedBlocksHolder highlightedBlocks) -> {
                highlightedBlocks.ticksSinceLastCheck = -1;
                highlightedBlocks.cachedBlockPositions = Collections.emptyList();
                highlightedBlocks.cachedVoxelShape = Shapes.empty();
                highlightedBlocks.recipes = SelectableRecipe.SingleInputSet.empty();
            });

    private final HighlightedBlockMemory blockMemory;
    private final Vector2i selectedRecipe;
    private int ticksSinceLastCheck;
    @Nullable
    private List<BlockPos> cachedBlockPositions;
    @Nullable
    private VoxelShape cachedVoxelShape;
    private SelectableRecipe.@Nullable SingleInputSet<HammeringRecipe> recipes;

    public HighlightedBlocksHolder(HighlightedBlockMemory blockMemory) {
        this.blockMemory = blockMemory;
        Holder<Block> holder = blockMemory.blockState().getBlockHolder();
        this.selectedRecipe =
                SELECTED_RECIPE_INDICES.containsKey(holder) ? SELECTED_RECIPE_INDICES.get(holder) : new Vector2i();
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
        for (BlockPos blockPos : this.getBlockPositionsForRecipe(blockGetter)) {
            voxelShape = Shapes.joinUnoptimized(voxelShape,
                    Shapes.create(new AABB(blockPos).inflate(0.005)),
                    BooleanOp.OR);
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
            this.updateSelectedRecipe(recipes, this.selectedRecipe, indexOffset);
            return true;
        } else {
            return false;
        }
    }

    private void updateSelectedRecipe(SelectableRecipe.SingleInputSet<HammeringRecipe> recipes, Vector2i selectedRecipe, int indexOffset) {
        this.updateSelectedRecipe(recipes, selectedRecipe, indexOffset, selectedRecipe);
    }

    private void updateSelectedRecipe(SelectableRecipe.SingleInputSet<HammeringRecipe> recipes, Vector2ic selectedRecipe, int indexOffset, Vector2i outputVector) {
        if (indexOffset > 0) {
            while (indexOffset-- > 0) {
                selectedRecipe.add(0, 1, outputVector);
                SelectableRecipe.SingleInputEntry<HammeringRecipe> recipe = recipes.entries().get(outputVector.x());
                int size = recipe.recipe().recipe().orElseThrow().value().blocks().size();
                if (outputVector.y() >= size) {
                    outputVector.x = Mth.positiveModulo(outputVector.x() + 1, recipes.size());
                    outputVector.y = 0;
                }
            }
        } else if (indexOffset < 0) {
            while (indexOffset++ < 0) {
                if (selectedRecipe.y() == 0) {
                    outputVector.x = Mth.positiveModulo(outputVector.x() - 1, recipes.size());
                    SelectableRecipe.SingleInputEntry<HammeringRecipe> recipe = recipes.entries().get(outputVector.x());
                    int size = recipe.recipe().recipe().orElseThrow().value().blocks().size();
                    outputVector.y = size - 1;
                } else {
                    selectedRecipe.add(0, -1, outputVector);
                }
            }
        }
    }

    public ItemStack getRecipeOutput(int indexOffset) {
        SelectableRecipe.SingleInputSet<HammeringRecipe> recipes = this.getRecipes();
        if (recipes.isEmpty()) {
            return ItemStack.EMPTY;
        } else {
            Vector2i vector2i = new Vector2i();
            this.updateSelectedRecipe(recipes, this.selectedRecipe, indexOffset, vector2i);
            SelectableRecipe<HammeringRecipe> recipe = this.getRecipeByIndex(recipes, vector2i.x());
            Holder<Block> holder = recipe.recipe().orElseThrow().value().blocks().get(vector2i.y());
            return new ItemStack(holder.value());
        }
    }

    public @Nullable RecipeHolder<HammeringRecipe> getRecipe() {
        SelectableRecipe.SingleInputSet<HammeringRecipe> recipes = this.getRecipes();
        return recipes.isEmpty() ? null :
                this.getRecipeByIndex(recipes, this.selectedRecipe.x()).recipe().orElseThrow();
    }

    public int getRecipeIndex() {
        return this.selectedRecipe.y();
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
