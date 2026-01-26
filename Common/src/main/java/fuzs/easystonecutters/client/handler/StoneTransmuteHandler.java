package fuzs.easystonecutters.client.handler;

import fuzs.easystonecutters.init.ModRegistry;
import fuzs.easystonecutters.network.client.ServerboundStoneTransmutationMessage;
import fuzs.easystonecutters.util.MiniumStoneHelper;
import fuzs.easystonecutters.world.item.crafting.TransmutationInWorldRecipe;
import fuzs.puzzleslib.api.event.v1.core.EventResultHolder;
import fuzs.puzzleslib.api.network.v4.MessageSender;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.BlockHitResult;

import java.util.List;

public class StoneTransmuteHandler {

    public static EventResultHolder<InteractionResult> onUseBlock(Player player, Level level, InteractionHand interactionHand, BlockHitResult hitResult) {
        ItemStack itemInHand = player.getItemInHand(interactionHand);
        if (itemInHand.is(ModRegistry.POCKET_STONECUTTER_ITEM.value()) && level.isClientSide()) {
            BlockWalker blockWalker = TransmutateShapeRenderingHandler.getBlockWalker();
            if (blockWalker != null) {
                boolean isReversed = player.isSecondaryUseActive();
                RecipeHolder<TransmutationInWorldRecipe> holder = blockWalker.getRecipe();
                if (holder != null) {
                    BlockPos blockPos = blockWalker.getBlockPos();
                    List<BlockPos> blocks = blockWalker.getBlocks(level);
                    TransmutationInWorldRecipe recipe = holder.value();
                    Block ingredient = isReversed ? recipe.getBlockResult() : recipe.getBlockIngredient();
                    Block result = isReversed ? recipe.getBlockIngredient() : recipe.getBlockResult();
                    MiniumStoneHelper.transmuteBlocks(blockPos, blocks, level, ingredient, result, itemInHand);
                    int selectedSlot = player.getInventory().getSelectedSlot();
                    MessageSender.broadcast(new ServerboundStoneTransmutationMessage(selectedSlot,
                            interactionHand,
                            blockPos,
                            blocks,
                            isReversed,
                            holder.id()));
                    TransmutateShapeRenderingHandler.clearBlockWalker();
//                    TransmutationResultGuiHandler.setBlockPopTime(5);
                    return EventResultHolder.interrupt(InteractionResult.SUCCESS);
                }
            }

            return EventResultHolder.interrupt(InteractionResult.PASS);
        }

        return EventResultHolder.pass();
    }
}
