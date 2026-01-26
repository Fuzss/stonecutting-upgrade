package fuzs.easystonecutters.client.handler;

import fuzs.easystonecutters.client.util.ClientRecipeHelper;
import fuzs.easystonecutters.world.item.component.SelectionMode;
import fuzs.easystonecutters.world.item.crafting.TransmutationInWorldRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Util;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SelectableRecipe;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class BlockWalker {
    private static final List<BlockPos> NEIGHBOR_POSITIONS_CUBE = BlockPos.betweenClosedStream(-1, -1, -1, 1, 1, 1)
            .filter(Predicate.not(BlockPos.ZERO::equals))
            .map(BlockPos::immutable)
            .toList();
    private static final Function<Direction, List<BlockPos>> NEIGHBOR_POSITIONS_FLAT = Util.memoize((Direction direction) -> {
        return BlockPos.betweenClosedStream(-1, -1, -1, 1, 1, 1).filter((BlockPos blockPos) -> {
            return !Objects.equals(blockPos, BlockPos.ZERO)
                    && direction.getAxis().choose(blockPos.getX(), blockPos.getY(), blockPos.getZ()) == 0;
        }).map(BlockPos::immutable).toList();
    });
    private static final Function<Direction, List<BlockPos>> NEIGHBOR_POSITIONS_LINE = Util.memoize(direction -> List.of(
            BlockPos.ZERO.relative(direction.getOpposite())));
    private static final List<BlockPos> NEIGHBOR_POSITIONS_BUSH = BlockPos.betweenClosedStream(-2, -1, -2, 2, 1, 2)
            .filter((BlockPos blockPos) -> {
                return !Objects.equals(blockPos, BlockPos.ZERO) && (blockPos.getY() == 0
                        || blockPos.distManhattan(BlockPos.ZERO) == 1);
            })
            .map(BlockPos::immutable)
            .toList();

    private final SelectionMode selectionMode;
    private final BlockPos blockPos;
    private final Direction blockDirection;
    private final BlockState blockState;
    private final List<BlockPos> neighborPositions;
    private final int maxDepth;
    private int blockTicks;
    @Nullable
    private RecipeHolder<TransmutationInWorldRecipe> recipe;
    @Nullable
    private List<BlockPos> blocks;
    @Nullable
    private VoxelShape voxelShape;

    private BlockWalker(int maxDepth, SelectionMode selectionMode, BlockPos blockPos, Direction blockDirection, BlockState blockState) {
        this.maxDepth = selectionMode == SelectionMode.LINE ? maxDepth * 2 : maxDepth;
        this.selectionMode = selectionMode;
        this.blockPos = blockPos;
        this.blockDirection = blockDirection;
        this.blockState = blockState;
        this.neighborPositions =
                blockState.getBlock() instanceof BushBlock ? NEIGHBOR_POSITIONS_BUSH : switch (selectionMode) {
                    case CUBE -> NEIGHBOR_POSITIONS_CUBE;
                    case FLAT -> NEIGHBOR_POSITIONS_FLAT.apply(blockDirection);
                    case LINE -> NEIGHBOR_POSITIONS_LINE.apply(blockDirection);
                };
    }

    public static BlockWalker fromHitResult(int maxDepth, SelectionMode selectionMode, BlockHitResult hitResult, BlockGetter blockGetter) {
        return new BlockWalker(maxDepth,
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
                        && this.blockDirection.equals(blockHitResult.getDirection()) && this.blockState.equals(
                        blockGetter.getBlockState(blockHitResult.getBlockPos()));
            }
        }

        return false;
    }

    public void testAllBlocks(@Nullable HitResult hitResult, BlockGetter blockGetter) {
        if (this.stillValid(-1, null, hitResult, blockGetter)) {
            if (this.blocks != null && --this.blockTicks <= 0) {
                this.blockTicks = 20;
                if (this.blocks.stream().anyMatch(t -> !this.isSame(blockGetter, t))) {
                    this.recipe = null;
                    this.blocks = null;
                    this.voxelShape = null;
                }
            }
        }
    }

    public VoxelShape getJoinedShape(Level level) {
        if (this.voxelShape == null) {
            return this.voxelShape = this.createJoinedShape(level);
        } else {
            return this.voxelShape;
        }
    }

    private VoxelShape createJoinedShape(Level level) {
        VoxelShape voxelShape = Shapes.empty();
        for (BlockPos blockPos : this.getBlocks(level)) {
            voxelShape = Shapes.joinUnoptimized(voxelShape,
                    Shapes.create(new AABB(blockPos).inflate(0.005)),
                    BooleanOp.OR);
        }

        return voxelShape;
    }

    public List<BlockPos> getBlocks(Level level) {
        if (this.blocks == null) {
            if (this.findRecipes(ClientRecipeHelper.getRecipes())) {
                this.blocks = this.findBlocks(level);
            } else {
                this.blocks = List.of();
            }
        }

        return this.recipe != null ? this.blocks : List.of();
    }

    private boolean findRecipes(SelectableRecipe.SingleInputSet<TransmutationInWorldRecipe> transmutationInWorldRecipes) {
        MutableBoolean mutableBoolean = new MutableBoolean();
        List<RecipeHolder<TransmutationInWorldRecipe>> recipes = transmutationInWorldRecipes.entries()
                .stream()
                .map(SelectableRecipe.SingleInputEntry::recipe)
                .map(SelectableRecipe::recipe)
                .<RecipeHolder<TransmutationInWorldRecipe>>mapMulti(Optional::ifPresent)
                .toList();
        recipes.stream()
                .filter(recipe -> this.blockState.is(recipe.value().getBlockIngredient()))
                .findFirst()
                .ifPresent(recipe -> {
                    this.recipe = recipe;
                    mutableBoolean.setTrue();
                });
        return mutableBoolean.booleanValue();
    }

    private List<BlockPos> findBlocks(BlockGetter blockGetter) {
        List<BlockPos> blocks = new ArrayList<>();
        BlockPos.breadthFirstTraversal(this.blockPos,
                this.maxDepth,
                1024,
                (BlockPos pos, Consumer<BlockPos> blockPosConsumer) -> {
                    for (BlockPos side : this.neighborPositions) {
                        blockPosConsumer.accept(pos.offset(side));
                    }
                },
                (BlockPos blockPos) -> {
                    if (this.isSame(blockGetter, blockPos)) {
                        blocks.add(blockPos);
                    }
                    return BlockPos.TraversalNodeStatus.ACCEPT;
                });
        return blocks;
    }

    private boolean isSame(BlockGetter blockGetter, BlockPos blockPos) {
        return this.blockState.is(blockGetter.getBlockState(blockPos).getBlock());
    }

    public BlockPos getBlockPos() {
        return this.blockPos;
    }

    @Nullable
    public Block getResult() {
        return this.recipe != null ? this.recipe.value().getBlockResult() : null;
    }

    @Nullable
    public RecipeHolder<TransmutationInWorldRecipe> getRecipe() {
        return this.recipe;
    }
}
