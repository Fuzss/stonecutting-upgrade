package fuzs.easystonecutters.network;

import fuzs.puzzleslib.api.network.v4.message.MessageListener;
import fuzs.puzzleslib.api.network.v4.message.play.ClientboundPlayMessage;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record ClientboundTransmutationParticleMessage(BlockPos blockPos,
                                                      boolean isSmoking) implements ClientboundPlayMessage {
    public static final StreamCodec<ByteBuf, ClientboundTransmutationParticleMessage> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            ClientboundTransmutationParticleMessage::blockPos,
            ByteBufCodecs.BOOL,
            ClientboundTransmutationParticleMessage::isSmoking,
            ClientboundTransmutationParticleMessage::new);

    @Override
    public MessageListener<Context> getListener() {
        return new MessageListener<Context>() {
            @Override
            public void accept(Context context) {
                BlockPos blockPos = ClientboundTransmutationParticleMessage.this.blockPos;
                if (ClientboundTransmutationParticleMessage.this.isSmoking) {
                    for (int i = 0; i < 8; ++i) {
                        context.level()
                                .addParticle(ParticleTypes.LARGE_SMOKE,
                                        blockPos.getX() + context.level().random.nextDouble(),
                                        blockPos.getY() + 1.2D,
                                        blockPos.getZ() + context.level().random.nextDouble(),
                                        0.0D,
                                        0.0D,
                                        0.0D);
                    }
                } else {
                    context.level()
                            .addParticle(ParticleTypes.EXPLOSION,
                                    blockPos.getX() + 0.5D,
                                    blockPos.getY() + 0.5D,
                                    blockPos.getZ() + 0.5D,
                                    0.0D,
                                    0.0D,
                                    0.0D);
                }
            }
        };
    }
}
