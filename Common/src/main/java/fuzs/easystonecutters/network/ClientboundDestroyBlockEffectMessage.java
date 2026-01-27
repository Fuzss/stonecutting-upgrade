package fuzs.easystonecutters.network;

import fuzs.puzzleslib.api.network.v4.codec.ExtraStreamCodecs;
import fuzs.puzzleslib.api.network.v4.message.MessageListener;
import fuzs.puzzleslib.api.network.v4.message.play.ClientboundPlayMessage;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.block.state.BlockState;

public record ClientboundDestroyBlockEffectMessage(BlockPos blockPos,
                                                   BlockState blockState) implements ClientboundPlayMessage {
    public static final StreamCodec<ByteBuf, ClientboundDestroyBlockEffectMessage> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            ClientboundDestroyBlockEffectMessage::blockPos,
            ExtraStreamCodecs.BLOCK_STATE,
            ClientboundDestroyBlockEffectMessage::blockState,
            ClientboundDestroyBlockEffectMessage::new);

    @Override
    public MessageListener<Context> getListener() {
        return new MessageListener<Context>() {
            @Override
            public void accept(Context context) {
                context.level()
                        .addDestroyBlockEffect(ClientboundDestroyBlockEffectMessage.this.blockPos,
                                ClientboundDestroyBlockEffectMessage.this.blockState);
            }
        };
    }
}
