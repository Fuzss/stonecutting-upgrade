package fuzs.easystonecutters.client.handler;

import fuzs.easystonecutters.client.util.HighlightedBlockMemory;
import fuzs.easystonecutters.client.util.HighlightedBlocksHolder;
import fuzs.easystonecutters.init.ModRegistry;
import fuzs.easystonecutters.network.client.ServerboundUseHammerMessage;
import fuzs.easystonecutters.util.MiniumStoneHelper;
import fuzs.easystonecutters.world.item.crafting.HammeringRecipe;
import fuzs.puzzleslib.api.event.v1.core.EventResultHolder;
import fuzs.puzzleslib.api.network.v4.MessageSender;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

import java.util.List;

public class HighlightedBlocksHandler {
    private static final InteractionHand[] HAND_VALUES = InteractionHand.values();

    private static HighlightedBlocksHolder highlightedBlocks = HighlightedBlocksHolder.EMPTY;

    private static ItemStack getHeldItemStack(Player player, Item item) {
        for (InteractionHand interactionHand : HAND_VALUES) {
            ItemStack itemInHand = player.getItemInHand(interactionHand);
            if (itemInHand.is(item)) {
                return itemInHand;
            }
        }

        return ItemStack.EMPTY;
    }

    public static HighlightedBlocksHolder getHighlightedBlocks() {
        return highlightedBlocks;
    }

    private static void setHighlightedBlocks(HighlightedBlocksHolder highlightedBlocks) {
        HighlightedBlocksHandler.highlightedBlocks = highlightedBlocks;
    }

    private static void resetHighlightedBlocks() {
        setHighlightedBlocks(HighlightedBlocksHolder.EMPTY);
    }

    public static void pickHighlightedBlocks(ClientLevel clientLevel, Camera camera, BlockHitResult hitResult) {
        if (camera.entity() instanceof Player player) {
            ItemStack itemStack = getHeldItemStack(player, ModRegistry.MASONRY_HAMMER_ITEM.value());
            if (!itemStack.isEmpty()) {
                HighlightedBlocksHolder highlightedBlocks = getHighlightedBlocks();
                if (highlightedBlocks.isEmpty() || !highlightedBlocks.stillValid(clientLevel,
                        player,
                        itemStack,
                        hitResult)) {
                    setHighlightedBlocks(new HighlightedBlocksHolder(HighlightedBlockMemory.of(clientLevel,
                            player,
                            itemStack,
                            hitResult)));
                }

                return;
            }
        }

        resetHighlightedBlocks();
    }

    public static void onEndClientTick(Minecraft minecraft) {
        if (minecraft.level != null) {
            getHighlightedBlocks().testAllBlocks(minecraft.level, minecraft.hitResult);
        }
    }

    public static EventResultHolder<InteractionResult> onUseBlock(Player player, Level level, InteractionHand interactionHand, BlockHitResult hitResult) {
        ItemStack itemInHand = player.getItemInHand(interactionHand);
        if (itemInHand.is(ModRegistry.MASONRY_HAMMER_ITEM.value()) && level.isClientSide()
                && !player.isSecondaryUseActive()) {
            HighlightedBlocksHolder highlightedBlocks = getHighlightedBlocks();
            RecipeHolder<HammeringRecipe> recipeHolder = highlightedBlocks.getRecipe();
            if (recipeHolder != null) {
                BlockPos blockPos = highlightedBlocks.getBlockPos();
                List<BlockPos> blockPositions = highlightedBlocks.getBlockPositionsForRecipe(level);
                MiniumStoneHelper.transmuteBlocks(level,
                        player,
                        itemInHand,
                        blockPos,
                        blockPositions,
                        recipeHolder.value());
                int selectedSlot = player.getInventory().getSelectedSlot();
                MessageSender.broadcast(new ServerboundUseHammerMessage(selectedSlot,
                        interactionHand,
                        blockPos,
                        blockPositions,
                        recipeHolder.id()));
                resetHighlightedBlocks();
                return EventResultHolder.interrupt(InteractionResult.SUCCESS);
            }

            return EventResultHolder.interrupt(InteractionResult.PASS);
        }

        return EventResultHolder.pass();
    }
}
