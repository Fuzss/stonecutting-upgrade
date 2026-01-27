package fuzs.easystonecutters.network.client;

import fuzs.easystonecutters.init.ModRegistry;
import fuzs.easystonecutters.util.MiniumStoneHelper;
import fuzs.easystonecutters.world.item.crafting.TransmutationInWorldRecipe;
import fuzs.puzzleslib.api.item.v2.ItemHelper;
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
import net.minecraft.world.level.block.Block;

import java.util.List;

public record ServerboundStoneTransmutationMessage(int selectedItemStack,
                                                   InteractionHand interactionHand,
                                                   BlockPos blockPos,
                                                   List<BlockPos> blocks,
                                                   boolean reverse,
                                                   ResourceKey<Recipe<?>> recipe) implements ServerboundPlayMessage {
    public static final StreamCodec<ByteBuf, ServerboundStoneTransmutationMessage> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.SHORT.map(Short::intValue, Integer::shortValue),
            ServerboundStoneTransmutationMessage::selectedItemStack,
            ExtraStreamCodecs.fromEnum(InteractionHand.class),
            ServerboundStoneTransmutationMessage::interactionHand,
            BlockPos.STREAM_CODEC,
            ServerboundStoneTransmutationMessage::blockPos,
            BlockPos.STREAM_CODEC.apply(ByteBufCodecs.list()),
            ServerboundStoneTransmutationMessage::blocks,
            ByteBufCodecs.BOOL,
            ServerboundStoneTransmutationMessage::reverse,
            ResourceKey.streamCodec(Registries.RECIPE),
            ServerboundStoneTransmutationMessage::recipe,
            ServerboundStoneTransmutationMessage::new);

    @Override
    public MessageListener<Context> getListener() {
        return new MessageListener<Context>() {
            @Override
            public void accept(Context context) {
                context.packetListener()
                        .handleSetCarriedItem(new ServerboundSetCarriedItemPacket(ServerboundStoneTransmutationMessage.this.selectedItemStack));
                ItemStack itemInHand = context.player()
                        .getItemInHand(ServerboundStoneTransmutationMessage.this.interactionHand);
                if (itemInHand.is(ModRegistry.MASONRY_HAMMER_ITEM.value())) {
                    TransmutationInWorldRecipe recipe = context.level()
                            .recipeAccess()
                            .byKey(ServerboundStoneTransmutationMessage.this.recipe)
                            .map(RecipeHolder::value)
                            .map(TransmutationInWorldRecipe.class::cast)
                            .orElse(null);
                    if (recipe != null) {
                        Block ingredient = ServerboundStoneTransmutationMessage.this.reverse ? recipe.getBlockResult() :
                                recipe.getBlockIngredient();
                        Block result = ServerboundStoneTransmutationMessage.this.reverse ? recipe.getBlockIngredient() :
                                recipe.getBlockResult();
                        MiniumStoneHelper.transmuteBlocks(ServerboundStoneTransmutationMessage.this.blockPos,
                                ServerboundStoneTransmutationMessage.this.blocks,
                                context.level(),
                                ingredient,
                                result,
                                itemInHand);
                        ItemHelper.hurtAndBreak(itemInHand,
                                1,
                                context.player(),
                                ServerboundStoneTransmutationMessage.this.interactionHand);
                    }
                }
            }
        };
    }
}
