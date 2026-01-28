package fuzs.easystonecutters.client.handler;

import fuzs.easystonecutters.init.ModRegistry;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class SelectedItemInGuiHandler {
    /**
     * @see Gui#HOTBAR_OFFHAND_LEFT_SPRITE
     */
    private static final Identifier HOTBAR_OFFHAND_LEFT_SPRITE = Identifier.withDefaultNamespace(
            "hud/hotbar_offhand_left");
    /**
     * @see Gui#HOTBAR_OFFHAND_RIGHT_SPRITE
     */
    private static final Identifier HOTBAR_OFFHAND_RIGHT_SPRITE = Identifier.withDefaultNamespace(
            "hud/hotbar_offhand_right");

    private static Holder<Block> lastSelectedItem = Blocks.AIR.builtInRegistryHolder();
    private static int itemStackPopTime;

    public static void onEndClientTick(Minecraft minecraft) {
        if (minecraft.player != null) {
            if (itemStackPopTime > 0) {
                itemStackPopTime--;
            }

            Holder<Block> holder = ModRegistry.SELECTED_BLOCK_ATTACHMENT_TYPE.getOrDefault(minecraft.player,
                    Blocks.AIR.builtInRegistryHolder());
            if (!lastSelectedItem.is(holder)) {
                lastSelectedItem = holder;
                itemStackPopTime = 5;
            }
        }
    }

    public static void onRenderGuiLayer(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null && !HighlightedBlocksHandler.getHeldItemStack(player, ModRegistry.HAMMERS_ITEM_TAG)
                .isEmpty()) {
            Holder<Block> holder = ModRegistry.SELECTED_BLOCK_ATTACHMENT_TYPE.getOrDefault(player,
                    Blocks.AIR.builtInRegistryHolder());
            ItemStack itemStack = new ItemStack(holder.value());
            itemStack.setPopTime(itemStackPopTime);
            if (!itemStack.isEmpty()) {
                guiGraphics.pose().pushMatrix();
                HumanoidArm humanoidArm = player.getMainArm().getOpposite();
                int screenWidth = guiGraphics.guiWidth();
                int screenHeight = guiGraphics.guiHeight();
                if (humanoidArm == HumanoidArm.LEFT) {
                    guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED,
                            HOTBAR_OFFHAND_LEFT_SPRITE,
                            screenWidth / 2 - 91 - 29 * 2,
                            screenHeight - 23,
                            29,
                            24);
                    renderFakeItem(guiGraphics,
                            screenWidth / 2 - 91 - 29 * 2 + 3,
                            screenHeight - 23 + 4,
                            deltaTracker,
                            itemStack);
                } else {
                    guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED,
                            HOTBAR_OFFHAND_RIGHT_SPRITE,
                            screenWidth / 2 + 91 + 29,
                            screenHeight - 23,
                            29,
                            24);
                    renderFakeItem(guiGraphics,
                            screenWidth / 2 + 91 + 29 + 10,
                            screenHeight - 23 + 4,
                            deltaTracker,
                            itemStack);
                }

                guiGraphics.pose().popMatrix();
            }
        }
    }

    /**
     * @see Gui#renderSlot(GuiGraphics, int, int, DeltaTracker, Player, ItemStack, int)
     */
    private static void renderFakeItem(GuiGraphics guiGraphics, int posX, int posY, DeltaTracker deltaTracker, ItemStack itemStack) {
        float partialTick = deltaTracker.getGameTimeDeltaPartialTick(false);
        float popTime = itemStack.getPopTime() - partialTick;
        if (popTime > 0.0F) {
            float popTimeScale = 1.0F + popTime / 5.0F;
            guiGraphics.pose().pushMatrix();
            guiGraphics.pose().translate(posX + 8, posY + 12);
            guiGraphics.pose().scale(1.0F / popTimeScale, (popTimeScale + 1.0F) / 2.0F);
            guiGraphics.pose().translate(-(posX + 8), -(posY + 12));
        }

        guiGraphics.renderFakeItem(itemStack, posX, posY, 0);
        if (popTime > 0.0F) {
            guiGraphics.pose().popMatrix();
        }
    }
}
