package fuzs.easystonecutters.world.item.component;

import fuzs.easystonecutters.world.item.HammerItem;
import io.netty.buffer.ByteBuf;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.util.Util;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;

public enum SelectionMode implements StringRepresentable {
    FLAT {
        @Override
        boolean isNeighborPosition(BlockPos blockPos, Direction direction) {
            return !Objects.equals(blockPos, BlockPos.ZERO)
                    && direction.getAxis().choose(blockPos.getX(), blockPos.getY(), blockPos.getZ()) == 0;
        }
    },
    CUBE {
        @Override
        boolean isNeighborPosition(BlockPos blockPos, Direction direction) {
            return !Objects.equals(blockPos, BlockPos.ZERO);
        }
    },
    LINE {
        @Override
        boolean isNeighborPosition(BlockPos blockPos, Direction direction) {
            return Objects.equals(blockPos, BlockPos.ZERO.relative(direction.getOpposite()));
        }

        @Override
        public int adjustInteractionRange(int interactionRange) {
            return super.adjustInteractionRange(interactionRange) * 2;
        }
    };

    public static final StringRepresentable.StringRepresentableCodec<SelectionMode> CODEC = StringRepresentable.fromEnum(
            SelectionMode::values);
    public static final IntFunction<SelectionMode> BY_ID = ByIdMap.continuous(SelectionMode::ordinal,
            values(),
            ByIdMap.OutOfBoundsStrategy.WRAP);
    public static final StreamCodec<ByteBuf, SelectionMode> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID,
            SelectionMode::ordinal);
    private static final List<BlockPos> BUSH_NEIGHBOR_POSITIONS = getNeighborPositions(2, 1, (BlockPos blockPos) -> {
        return !Objects.equals(blockPos, BlockPos.ZERO) && (blockPos.getY() == 0
                || blockPos.distManhattan(BlockPos.ZERO) == 1);
    });
    public static final SelectionMode DEFAULT_SELECTION_MODE = FLAT;

    private final Component component;
    private final Function<Direction, List<BlockPos>> neighborPositions;

    SelectionMode() {
        this.component = Component.translatable(HammerItem.createTranslationKey(this.getSerializedName()))
                .withStyle(ChatFormatting.GOLD);
        this.neighborPositions = Util.memoize((Direction direction) -> {
            return getNeighborPositions(1, 1, (BlockPos blockPos) -> {
                return this.isNeighborPosition(blockPos, direction);
            });
        });
    }

    private static List<BlockPos> getNeighborPositions(int horizontalDistance, int verticalDistance, Predicate<BlockPos> filter) {
        return BlockPos.betweenClosedStream(-horizontalDistance,
                -verticalDistance,
                -horizontalDistance,
                horizontalDistance,
                verticalDistance,
                horizontalDistance).filter(filter).map(BlockPos::immutable).toList();
    }

    public Component getComponent() {
        return this.component;
    }

    public List<BlockPos> selectNeighborPositions(BlockState blockState, Direction direction) {
        if (blockState.getBlock() instanceof BushBlock) {
            return BUSH_NEIGHBOR_POSITIONS;
        } else {
            return this.neighborPositions.apply(direction);
        }
    }

    public int adjustInteractionRange(int interactionRange) {
        return interactionRange;
    }

    abstract boolean isNeighborPosition(BlockPos blockPos, Direction direction);

    public SelectionMode cycle() {
        return BY_ID.apply(this.ordinal() + 1);
    }

    @Override
    public String getSerializedName() {
        return this.name().toLowerCase(Locale.ROOT);
    }
}
