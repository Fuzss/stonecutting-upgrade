package fuzs.easystonecutters.client;

import fuzs.easystonecutters.client.handler.HighlightedBlocksHandler;
import fuzs.hotbarslotcycling.api.v1.client.ItemCyclingProvider;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

public record HammerCyclingProvider(ItemStack itemInHand,
                                    InteractionHand interactionHand) implements ItemCyclingProvider {

    @Override
    public ItemStack getSelectedStack() {
        return HighlightedBlocksHandler.getHighlightedBlocks().getRecipeOutput(0);
    }

    @Override
    public ItemStack getForwardStack() {
        return HighlightedBlocksHandler.getHighlightedBlocks().getRecipeOutput(1);
    }

    @Override
    public ItemStack getBackwardStack() {
        return HighlightedBlocksHandler.getHighlightedBlocks().getRecipeOutput(-1);
    }

    @Override
    public boolean cycleSlotForward() {
        return HighlightedBlocksHandler.getHighlightedBlocks().updateSelectedRecipe(1);
    }

    @Override
    public boolean cycleSlotBackward() {
        return HighlightedBlocksHandler.getHighlightedBlocks().updateSelectedRecipe(-1);
    }
}
