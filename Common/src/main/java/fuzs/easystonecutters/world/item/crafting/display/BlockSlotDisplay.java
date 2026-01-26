package fuzs.easystonecutters.world.item.crafting.display;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fuzs.easystonecutters.init.ModRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.item.crafting.display.DisplayContentsFactory;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.level.block.Block;

import java.util.stream.Stream;

/**
 * This is not so useful right now, as we still sync the full recipe holder to clients with a custom stream codec, since
 * we need the resource key from the holder.
 * <p>
 * Might become useful though when vanilla potentially removes the holder.
 */
public record BlockSlotDisplay(Block block) implements SlotDisplay {
    public static final MapCodec<BlockSlotDisplay> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                    BuiltInRegistries.BLOCK.byNameCodec().fieldOf("block").forGetter(BlockSlotDisplay::block))
            .apply(instance, BlockSlotDisplay::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, BlockSlotDisplay> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.registry(Registries.BLOCK),
            BlockSlotDisplay::block,
            BlockSlotDisplay::new);

    @Override
    public <T> Stream<T> resolve(ContextMap context, DisplayContentsFactory<T> output) {
        return output instanceof DisplayContentsFactory.ForStacks<T> forStacks ?
                Stream.of(forStacks.forStack(this.block.asItem())) : Stream.empty();
    }

    @Override
    public Type<? extends SlotDisplay> type() {
        return ModRegistry.BLOCK_SLOT_DISPLAY.value();
    }
}
