package fuzs.easystonecutters.network.client;

import fuzs.easystonecutters.init.ModRegistry;
import fuzs.easystonecutters.util.MiniumStoneHelper;
import fuzs.easystonecutters.world.item.crafting.HammeringRecipe;
import fuzs.puzzleslib.api.network.v4.codec.ExtraStreamCodecs;
import fuzs.puzzleslib.api.network.v4.message.MessageListener;
import fuzs.puzzleslib.api.network.v4.message.play.ServerboundPlayMessage;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.List;

public record ServerboundUseHammerMessage(int selectedSlot,
                                          InteractionHand interactionHand,
                                          BlockPos blockPos,
                                          List<BlockPos> blockPositions,
                                          ResourceKey<Recipe<?>> recipe) implements ServerboundPlayMessage {
    public static final StreamCodec<ByteBuf, ServerboundUseHammerMessage> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.SHORT.map(Short::intValue, Integer::shortValue),
            ServerboundUseHammerMessage::selectedSlot,
            ExtraStreamCodecs.fromEnum(InteractionHand.class),
            ServerboundUseHammerMessage::interactionHand,
            BlockPos.STREAM_CODEC,
            ServerboundUseHammerMessage::blockPos,
            BlockPos.STREAM_CODEC.apply(ByteBufCodecs.list()),
            ServerboundUseHammerMessage::blockPositions,
            ResourceKey.streamCodec(Registries.RECIPE),
            ServerboundUseHammerMessage::recipe,
            ServerboundUseHammerMessage::new);

    @Override
    public MessageListener<Context> getListener() {
        return new MessageListener<Context>() {
            @Override
            public void accept(Context context) {
                context.packetListener()
                        .handleSetCarriedItem(new ServerboundSetCarriedItemPacket(ServerboundUseHammerMessage.this.selectedSlot));
                ItemStack itemInHand = context.player().getItemInHand(ServerboundUseHammerMessage.this.interactionHand);
                if (itemInHand.is(ModRegistry.MASONRY_HAMMER_ITEM.value())) {
                    HammeringRecipe recipe = context.level()
                            .recipeAccess()
                            .byKey(ServerboundUseHammerMessage.this.recipe)
                            .map(RecipeHolder::value)
                            .map(HammeringRecipe.class::cast)
                            .orElse(null);
                    if (recipe != null) {
                        MiniumStoneHelper.transmuteBlocks(context.level(),
                                context.player(),
                                itemInHand,
                                ServerboundUseHammerMessage.this.blockPos,
                                ServerboundUseHammerMessage.this.blockPositions,
                                recipe);
                        itemInHand.hurtAndBreak(1, context.player(), ServerboundUseHammerMessage.this.interactionHand);
                    }
                }
            }
        };
    }
}
