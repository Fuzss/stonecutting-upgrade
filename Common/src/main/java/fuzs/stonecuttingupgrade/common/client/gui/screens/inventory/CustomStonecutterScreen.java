package fuzs.stonecuttingupgrade.common.client.gui.screens.inventory;

import com.mojang.blaze3d.platform.InputConstants;
import fuzs.puzzleslib.api.client.gui.v2.screens.inventory.AbstractWidgetsContainerScreen;
import fuzs.puzzleslib.api.client.input.v1.KeyEvent;
import fuzs.stonecuttingupgrade.common.StonecuttingUpgrade;
import fuzs.stonecuttingupgrade.common.client.gui.components.AbstractMenuSelectionList;
import fuzs.stonecuttingupgrade.common.client.gui.components.RecipeImageButton;
import fuzs.stonecuttingupgrade.common.config.ClientConfig;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.StonecutterMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.StonecutterRecipe;

import java.util.List;

/**
 * @see net.minecraft.client.gui.screens.inventory.StonecutterScreen
 */
public class CustomStonecutterScreen extends AbstractWidgetsContainerScreen<StonecutterMenu> {
    public static final ResourceLocation TEXTURE_LOCATION = StonecuttingUpgrade.id(
            "textures/gui/container/stonecutter.png");
    public static final ResourceLocation RECIPE_SELECTED_SPRITE = ResourceLocation.withDefaultNamespace(
            "container/stonecutter/recipe_selected");
    public static final ResourceLocation RECIPE_HIGHLIGHTED_SPRITE = ResourceLocation.withDefaultNamespace(
            "container/stonecutter/recipe_highlighted");
    public static final ResourceLocation RECIPE_SPRITE = ResourceLocation.withDefaultNamespace(
            "container/stonecutter/recipe");
    public static final WidgetSprites RECIPE_SPRITES = new WidgetSprites(RECIPE_SPRITE,
            RECIPE_SELECTED_SPRITE,
            RECIPE_HIGHLIGHTED_SPRITE);
    public static final int RECIPES_COLUMNS = 7;
    public static final int RECIPES_ROWS = 3;
    public static final int RECIPES_IMAGE_SIZE_WIDTH = 16;
    public static final int RECIPES_IMAGE_SIZE_HEIGHT = 18;

    private static ItemStack recipeInput = ItemStack.EMPTY;
    private RecipeSelectionList scrollingList;

    public CustomStonecutterScreen(StonecutterMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        menu.registerUpdateListener(this::containerChanged);
        this.setSlotPosition(StonecutterMenu.INPUT_SLOT, 13, 19);
        this.setSlotPosition(StonecutterMenu.RESULT_SLOT, 13, 49);
        this.titleLabelY = 5;
    }

    private void setSlotPosition(int index, int x, int y) {
        Slot slot = this.getMenu().getSlot(index);
        slot.x = x;
        slot.y = y;
    }

    @Override
    protected void init() {
        super.init();
        this.scrollingList = new RecipeSelectionList(this.leftPos + 40, this.topPos + 15);
        this.addRenderableWidget(this.scrollingList);
        this.containerChanged();
    }

    private void containerChanged() {
        if (this.scrollingList != null) {
            int size = this.scrollingList.children().size();
            this.scrollingList.clearEntries();
            if (this.getMenu().hasInputItem()) {
                if (StonecuttingUpgrade.CONFIG.get(ClientConfig.class).rememberLastRecipe) {
                    ItemStack inputItemStack = this.getMenu().getSlot(StonecutterMenu.INPUT_SLOT).getItem();
                    if (!ItemStack.isSameItemSameComponents(inputItemStack, recipeInput)) {
                        recipeInput = inputItemStack.copyWithCount(1);
                        RecipeImageButton.clearLastRecipeOutput();
                    }
                } else {
                    RecipeImageButton.clearLastRecipeOutput();
                }

                List<RecipeHolder<StonecutterRecipe>> recipesForInput = this.menu.getRecipes();
                for (int recipeIndex = 0; recipeIndex < recipesForInput.size(); recipeIndex++) {
                    ItemStack itemStack = recipesForInput.get(recipeIndex)
                            .value()
                            .getResultItem(this.minecraft.level.registryAccess());
                    this.scrollingList.addRecipe(this.getMenu(), recipeIndex, itemStack);
                }
            }

            if (!this.getMenu().hasInputItem() || size != this.scrollingList.children().size()) {
                this.scrollingList.setScrollAmount(0.0);
            }
        }
    }

    @Override
    public void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.blit(TEXTURE_LOCATION,
                this.leftPos,
                this.topPos,
                0.0F,
                0.0F,
                this.imageWidth,
                this.imageHeight,
                256,
                256);
    }

    @Override
    public boolean keyPressed(int key, int scancode, int modifiers) {
        return this.keyPressed(new KeyEvent(key, scancode, modifiers));
    }

    public boolean keyPressed(KeyEvent keyEvent) {
        if (super.keyPressed(keyEvent.key(), keyEvent.scancode(), keyEvent.modifiers())) {
            return true;
        }

        if (!StonecuttingUpgrade.CONFIG.get(ClientConfig.class).quickMoveLastRecipeInput) {
            return false;
        }

        if (keyEvent.isSelection() && !recipeInput.isEmpty()) {
            boolean hasMovedItems = false;
            Slot inputSlot = this.getMenu().getSlot(StonecutterMenu.INPUT_SLOT);
            $1:
            if (inputSlot.getItem().getCount() < inputSlot.getMaxStackSize(inputSlot.getItem())) {
                boolean moveAllItems = StonecuttingUpgrade.CONFIG.get(ClientConfig.class).quickMoveAllItems.test(
                        keyEvent);
                if (ItemStack.isSameItemSameComponents(this.getMenu().getCarried(), recipeInput)) {
                    hasMovedItems = true;
                    this.refillSlotFromCarried(inputSlot, moveAllItems);
                    if (!moveAllItems
                            || inputSlot.getItem().getCount() >= inputSlot.getMaxStackSize(inputSlot.getItem())) {
                        break $1;
                    }
                }

                int size = this.getMenu().slots.size();
                for (int i = 0; i < size; i++) {
                    Slot slot = this.getMenu().getSlot(i);
                    if (slot.hasItem() && slot.container instanceof Inventory) {
                        if (ItemStack.isSameItemSameComponents(slot.getItem(), recipeInput)) {
                            hasMovedItems = true;
                            this.refillSlotFromInventory(slot, inputSlot, moveAllItems);
                            if (!moveAllItems || inputSlot.getItem().getCount()
                                    >= inputSlot.getMaxStackSize(inputSlot.getItem())) {
                                break;
                            }
                        }
                    }
                }
            }

            if (hasMovedItems) {
                SoundManager soundManager = this.minecraft.getSoundManager();
                soundManager.play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                return true;
            }
        }

        return false;
    }

    private void refillSlotFromCarried(Slot inputSlot, boolean moveAllItems) {
        this.slotClicked(inputSlot,
                inputSlot.index,
                moveAllItems ? InputConstants.MOUSE_BUTTON_LEFT : InputConstants.MOUSE_BUTTON_RIGHT,
                ClickType.PICKUP);
    }

    private void refillSlotFromInventory(Slot inventorySlot, Slot inputSlot, boolean moveAllItems) {
        this.slotClicked(inventorySlot, inventorySlot.index, InputConstants.MOUSE_BUTTON_LEFT, ClickType.PICKUP);
        this.slotClicked(inputSlot,
                inputSlot.index,
                moveAllItems ? InputConstants.MOUSE_BUTTON_LEFT : InputConstants.MOUSE_BUTTON_RIGHT,
                ClickType.PICKUP);
        this.slotClicked(inventorySlot, inventorySlot.index, InputConstants.MOUSE_BUTTON_LEFT, ClickType.PICKUP);
    }

    private class RecipeSelectionList extends AbstractMenuSelectionList<RecipeSelectionList.Entry> {

        public RecipeSelectionList(int posX, int posY) {
            super(CustomStonecutterScreen.this.minecraft,
                    posX,
                    posY,
                    RECIPES_COLUMNS * RECIPES_IMAGE_SIZE_WIDTH,
                    RECIPES_ROWS * RECIPES_IMAGE_SIZE_HEIGHT,
                    RECIPES_IMAGE_SIZE_HEIGHT);
        }

        @Override
        protected int scrollBarX() {
            return this.getRowRight() + 3;
        }

        public void addRecipe(StonecutterMenu menu, int recipeIndex, ItemStack itemStack) {
            int columnIndex = recipeIndex % RECIPES_COLUMNS;
            Entry entry;
            if (columnIndex == 0) {
                entry = new Entry();
                this.addEntry(entry);
            } else {
                entry = this.children().getLast();
            }

            entry.addRenderableWidget(new RecipeImageButton(menu,
                    this.getX() + columnIndex * CustomStonecutterScreen.RECIPES_IMAGE_SIZE_WIDTH,
                    this.getY(),
                    recipeIndex,
                    itemStack));
        }

        private static class Entry extends AbstractMenuSelectionList.Entry<Entry> {

        }
    }
}
