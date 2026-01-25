package fuzs.easystonecutters.client.gui.screens.inventory;

import com.mojang.blaze3d.platform.InputConstants;
import fuzs.easystonecutters.EasyStonecutters;
import fuzs.easystonecutters.client.gui.components.AbstractMenuSelectionList;
import fuzs.easystonecutters.client.gui.components.RecipeImageButton;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.inventory.StonecutterScreen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.StonecutterMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.SelectableRecipe;
import net.minecraft.world.item.crafting.StonecutterRecipe;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplayContext;

import java.util.List;

public class ModStonecutterScreen extends StonecutterScreen {
    public static final Identifier TEXTURE_LOCATION = EasyStonecutters.id("textures/gui/container/stonecutter.png");
    public static final Identifier RECIPE_SELECTED_SPRITE = Identifier.withDefaultNamespace(
            "container/stonecutter/recipe_selected");
    public static final Identifier RECIPE_HIGHLIGHTED_SPRITE = Identifier.withDefaultNamespace(
            "container/stonecutter/recipe_highlighted");
    public static final Identifier RECIPE_SPRITE = Identifier.withDefaultNamespace("container/stonecutter/recipe");
    public static final WidgetSprites RECIPE_SPRITES = new WidgetSprites(RECIPE_SPRITE,
            RECIPE_SELECTED_SPRITE,
            RECIPE_HIGHLIGHTED_SPRITE);
    public static final int RECIPES_COLUMNS = 7;
    public static final int RECIPES_ROWS = 3;
    public static final int RECIPES_IMAGE_SIZE_WIDTH = 16;
    public static final int RECIPES_IMAGE_SIZE_HEIGHT = 18;

    private RecipeSelectionList scrollingList;
    private ItemStack recipeInput = ItemStack.EMPTY;

    public ModStonecutterScreen(StonecutterMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        menu.registerUpdateListener(this::containerChanged);
        this.setSlotPosition(StonecutterMenu.INPUT_SLOT, 13, 19);
        this.setSlotPosition(StonecutterMenu.RESULT_SLOT, 13, 49);
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
        int size = this.scrollingList.children().size();
        this.scrollingList.clearEntries();
        if (this.getMenu().hasInputItem()) {
            this.recipeInput = this.getMenu().getSlot(StonecutterMenu.INPUT_SLOT).getItem().copyWithCount(1);
            ContextMap contextMap = SlotDisplayContext.fromLevel(this.minecraft.level);
            List<SelectableRecipe.SingleInputEntry<StonecutterRecipe>> recipesForInput = this.getMenu()
                    .getVisibleRecipes()
                    .entries();
            for (int recipeIndex = 0; recipeIndex < recipesForInput.size(); recipeIndex++) {
                SlotDisplay slotDisplay = recipesForInput.get(recipeIndex).recipe().optionDisplay();
                ItemStack itemStack = slotDisplay.resolveForFirstStack(contextMap);
                this.scrollingList.addRecipe(this.getMenu(), recipeIndex, itemStack);
            }
        }

        if (!this.getMenu().hasInputItem() || size != this.scrollingList.children().size()) {
            this.scrollingList.setScrollAmount(0.0);
        }
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED,
                TEXTURE_LOCATION,
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
    public boolean keyPressed(KeyEvent event) {
        if (event.isSelection() && !this.recipeInput.isEmpty()) {
            boolean hasMovedItems = false;
            Slot inputSlot = this.getMenu().getSlot(StonecutterMenu.INPUT_SLOT);
            if (inputSlot.getItem().getCount() < inputSlot.getMaxStackSize(inputSlot.getItem())) {
                int size = this.getMenu().slots.size();
                for (int i = 0; i < size; i++) {
                    Slot slot = this.getMenu().getSlot(i);
                    if (slot.hasItem() && slot.container instanceof Inventory) {
                        if (ItemStack.isSameItemSameComponents(slot.getItem(), this.recipeInput)) {
                            hasMovedItems = true;
                            this.refillSlot(slot, inputSlot, event.hasShiftDown());
                            if (!event.hasShiftDown() || inputSlot.getItem().getCount() >= inputSlot.getMaxStackSize(
                                    inputSlot.getItem())) {
                                break;
                            }
                        }
                    }
                }
            }

            if (hasMovedItems) {
                AbstractWidget.playButtonClickSound(this.minecraft.getSoundManager());
                return true;
            }
        }

        return super.keyPressed(event);
    }

    private void refillSlot(Slot inventorySlot, Slot inputSlot, boolean maxAmount) {
        this.slotClicked(inventorySlot, inventorySlot.index, InputConstants.MOUSE_BUTTON_LEFT, ClickType.PICKUP);
        this.slotClicked(inputSlot,
                inputSlot.index,
                maxAmount ? InputConstants.MOUSE_BUTTON_LEFT : InputConstants.MOUSE_BUTTON_RIGHT,
                ClickType.PICKUP);
        this.slotClicked(inventorySlot, inventorySlot.index, InputConstants.MOUSE_BUTTON_LEFT, ClickType.PICKUP);
    }

    private class RecipeSelectionList extends AbstractMenuSelectionList<RecipeSelectionList.Entry> {

        public RecipeSelectionList(int posX, int posY) {
            super(ModStonecutterScreen.this.minecraft,
                    posX,
                    posY,
                    RECIPES_COLUMNS * RECIPES_IMAGE_SIZE_WIDTH,
                    RECIPES_ROWS * RECIPES_IMAGE_SIZE_HEIGHT,
                    RECIPES_IMAGE_SIZE_HEIGHT,
                    3);
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
                    this.getX() + columnIndex * ModStonecutterScreen.RECIPES_IMAGE_SIZE_WIDTH,
                    this.getY(),
                    recipeIndex,
                    itemStack));
        }

        private static class Entry extends AbstractMenuSelectionList.Entry<Entry> {

            @Override
            public <T extends AbstractWidget> T addRenderableWidget(T widget) {
                return super.addRenderableWidget(widget);
            }
        }
    }
}
