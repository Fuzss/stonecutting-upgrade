package fuzs.easystonecutters.client.gui.components;

import fuzs.easystonecutters.client.gui.screens.inventory.ModStonecutterScreen;
import fuzs.puzzleslib.api.client.gui.v2.tooltip.TooltipBuilder;
import fuzs.puzzleslib.api.client.gui.v2.tooltip.TooltipRenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.client.gui.screens.inventory.tooltip.MenuTooltipPositioner;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.inventory.StonecutterMenu;
import net.minecraft.world.item.ItemStack;

import java.util.function.Function;

public class RecipeImageButton extends ImageButton {
    private static ItemStack lastRecipeOutput = ItemStack.EMPTY;
    private final StonecutterMenu menu;
    private final int recipeIndex;
    private final ItemStack recipeOutput;

    public RecipeImageButton(StonecutterMenu menu, int posX, int posY, int recipeIndex, ItemStack recipeOutput) {
        super(posX,
                posY,
                ModStonecutterScreen.RECIPES_IMAGE_SIZE_WIDTH,
                ModStonecutterScreen.RECIPES_IMAGE_SIZE_HEIGHT,
                ModStonecutterScreen.RECIPE_SPRITES,
                Function.identity()::apply);
        this.menu = menu;
        this.recipeIndex = recipeIndex;
        this.recipeOutput = recipeOutput;
        TooltipBuilder.create(TooltipRenderHelper.getTooltipLines(recipeOutput))
                .setTooltipPositionerFactory((ClientTooltipPositioner clientTooltipPositioner, AbstractWidget abstractWidget) -> {
                    return clientTooltipPositioner instanceof MenuTooltipPositioner ?
                            DefaultTooltipPositioner.INSTANCE : clientTooltipPositioner;
                })
                .build(this);
        if (this.menu.getSelectedRecipeIndex() == recipeIndex || ItemStack.isSameItemSameComponents(recipeOutput,
                lastRecipeOutput)) {
            this.active = false;
            if (this.menu.getSelectedRecipeIndex() != recipeIndex) {
                // When the recipe input update, this runs immediately, before the server is notified and has a chance to refresh the recipes for the new input.
                // So this must run deferred, so the server has the correct recipes set up already which can then be selected.
                Minecraft.getInstance().schedule(() -> {
                    this.selectRecipe(recipeIndex, false);
                });
            }
        }
    }

    private boolean selectRecipe(int index, boolean playSound) {
        Minecraft minecraft = Minecraft.getInstance();
        if (this.menu.clickMenuButton(minecraft.player, index)) {
            minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, index);
            if (playSound) {
                minecraft.getSoundManager()
                        .play(SimpleSoundInstance.forUI(SoundEvents.UI_STONECUTTER_SELECT_RECIPE, 1.0F));
            }

            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onPress(InputWithModifiers input) {
        if (this.selectRecipe(this.recipeIndex, true)) {
            this.active = this.menu.getSelectedRecipeIndex() != this.recipeIndex;
            lastRecipeOutput = this.recipeOutput.copyWithCount(1);
        }
    }

    @Override
    public void renderContents(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.active = this.menu.getSelectedRecipeIndex() != this.recipeIndex;
        super.renderContents(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.renderFakeItem(this.recipeOutput, this.getX(), this.getY() + 1);
    }
}
