package fuzs.easystonecutters.world.entity.attachment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.Collections;
import java.util.List;

public record SelectedHammeringBlocks(BlockPos clickedPosition, List<BlockPos> blockPositions) {
    public static final Codec<SelectedHammeringBlocks> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    BlockPos.CODEC.fieldOf("hammerable_block").forGetter(SelectedHammeringBlocks::clickedPosition),
                    BlockPos.CODEC.listOf().fieldOf("selected_block").forGetter(SelectedHammeringBlocks::blockPositions))
            .apply(instance, SelectedHammeringBlocks::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, SelectedHammeringBlocks> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            SelectedHammeringBlocks::clickedPosition,
            BlockPos.STREAM_CODEC.apply(ByteBufCodecs.list()),
            SelectedHammeringBlocks::blockPositions,
            SelectedHammeringBlocks::new);
    public static final SelectedHammeringBlocks EMPTY = new SelectedHammeringBlocks(BlockPos.ZERO,
            Collections.emptyList());
}
