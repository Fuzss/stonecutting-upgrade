package fuzs.easystonecutters.network.client;

import fuzs.easystonecutters.init.ModRegistry;
import fuzs.easystonecutters.world.entity.attachment.SelectedHammeringBlocks;
import fuzs.puzzleslib.api.network.v4.message.MessageListener;
import fuzs.puzzleslib.api.network.v4.message.play.ServerboundPlayMessage;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public record ServerboundSelectHammeringBlocksMessage(SelectedHammeringBlocks selectedHammeringBlocks) implements ServerboundPlayMessage {
    public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundSelectHammeringBlocksMessage> STREAM_CODEC = StreamCodec.composite(
            SelectedHammeringBlocks.STREAM_CODEC,
            ServerboundSelectHammeringBlocksMessage::selectedHammeringBlocks,
            ServerboundSelectHammeringBlocksMessage::new);

    @Override
    public MessageListener<Context> getListener() {
        return new MessageListener<Context>() {
            @Override
            public void accept(Context context) {
                ModRegistry.SELECTED_HAMMERING_BLOCKS_ATTACHMENT_TYPE.set(context.player(),
                        ServerboundSelectHammeringBlocksMessage.this.selectedHammeringBlocks());
            }
        };
    }
}
