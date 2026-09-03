package fuzs.stonecuttingupgrade.common.client.gui.components;

import fuzs.puzzleslib.api.client.gui.v2.ScreenHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import java.util.ArrayList;
import java.util.List;

/**
 * A selection list implementation that can be used as part of a screen anywhere, without having to cover the whole
 * screen width.
 * <p>
 * Also, the scroll bar is mostly handled separately, and is placed outside the bounds of the actual list.
 */
public abstract class AbstractMenuSelectionList<E extends ContainerObjectSelectionList.Entry<E>> extends UpdatedContainerObjectSelectionList<E> {
    public static final ResourceLocation SCROLLER_SPRITE = ResourceLocation.withDefaultNamespace(
            "container/creative_inventory/scroller");
    public static final ResourceLocation SCROLLER_DISABLED_SPRITE = ResourceLocation.withDefaultNamespace(
            "container/creative_inventory/scroller_disabled");

    public AbstractMenuSelectionList(Minecraft minecraft, int x, int y, int width, int height, int itemHeight) {
        super(minecraft, width, height, y, itemHeight);
        this.setX(x);
    }

    @Override
    public int addEntry(E entry) {
        return super.addEntry(entry);
    }

    @Override
    public E getEntryAtPosition(double mouseX, double mouseY) {
        // a trick to get around vanilla subtracting 4 without having to copy the whole method
        this.headerHeight -= 4;
        E entry = super.getEntryAtPosition(mouseX, mouseY);
        this.headerHeight += 4;
        return entry;
    }

    @Override
    public int getRowWidth() {
        return this.getWidth();
    }

    @Override
    protected void renderItem(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, int index, int left, int top, int width, int height) {
        // add back height subtracted by vanilla for item outline, which we have removed
        super.renderItem(guiGraphics, mouseX, mouseY, partialTick, index, left, top, width, height + 4);
    }

    @Override
    protected void extractScrollbar(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        ResourceLocation sprite = this.scrollable() ? SCROLLER_SPRITE : SCROLLER_DISABLED_SPRITE;
        int scrollerX = this.scrollBarX();
        int scrollerY = this.scrollable() ? this.scrollBarY() : this.getY();
        guiGraphics.blitSprite(
                sprite,
                scrollerX,
                scrollerY,
                this.scrollerWidth(),
                this.scrollerHeight());
    }

    @Override
    protected void renderDecorations(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        // TODO remove this
        int posX = this.getScrollbarPosition();
        double scrollAmount = this.getMaxScroll() > 0 ? this.getScrollAmount() / this.getMaxScroll() : 0;
        int posY = this.getY() + (int) (scrollAmount * (this.getHeight() - this.scrollerHeight()));
        ResourceLocation resourceLocation = this.getMaxScroll() > 0 ? SCROLLER_SPRITE : SCROLLER_DISABLED_SPRITE;
        guiGraphics.blitSprite(resourceLocation, posX, posY, this.scrollbarWidth(), this.scrollerHeight());
    }

    @Override
    protected void renderListSeparators(GuiGraphics guiGraphics) {
        // NO-OP
    }

    @Override
    protected void renderListBackground(GuiGraphics guiGraphics) {
        // NO-OP
    }

    @Override
    public int getMaxScroll() {
        return Math.max(0, this.getMaxPosition() - this.getHeight());
    }

    public int maxScrollAmount() {
        return this.getMaxScroll();
    }

    @Override
    protected void updateScrollingState(double mouseX, double mouseY, int button) {
        this.scrolling = this.isValidClickButton(button) && this.isOverScrollbar(mouseX, mouseY);
    }

    protected boolean scrollable() {
        return this.maxScrollAmount() > 0;
    }

    @Override
    protected int scrollerWidth() {
        return 12;
    }

    @Override
    protected int scrollerHeight() {
        return 15;
    }

    @Override
    public final double getScrollAmount() {
        return this.scrollAmount();
    }

    public double scrollAmount() {
        return super.getScrollAmount();
    }

    public int scrollBarY() {
        if (!this.scrollable() || this.maxScrollAmount() == 0) {
            return this.getY();
        } else {
            double scrollScale = this.scrollAmount() / this.maxScrollAmount();
            return this.getY() + Math.max(0, (int) (scrollScale * (this.getHeight() - this.scrollerHeight())));
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!this.isValidMouseClick(button)) {
            return false;
        }

        this.updateScrollingState(mouseX, mouseY, button);
        if (this.scrolling) {
            this.setMouseButtonScrollAmount(mouseX, mouseY, button);
            return true;
        } else {
            return super.mouseClicked(mouseX, mouseY, button);
        }
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (this.scrolling) {
            this.setMouseButtonScrollAmount(mouseX, mouseY, button);
            return true;
        } else {
            return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
        }
    }

    protected void setMouseButtonScrollAmount(double mouseX, double mouseY, int button) {
        double scrollOffs = (mouseY - this.getY() - this.scrollerHeight() / 2.0) / (this.getHeight() - this.scrollerHeight());
        this.setScrollAmount(Mth.clamp(scrollOffs, 0.0, 1.0) * this.getMaxScroll());
    }

//    @Override
//    public boolean isMouseOver(double mouseX, double mouseY) {
//        return super.isMouseOver(mouseX, mouseY) || this.isMouseOverScrollbar(mouseX, mouseY);
//    }
//
//    protected boolean isMouseOverScrollbar(double mouseX, double mouseY) {
//        return ScreenHelper.isHovering(this.getScrollbarPosition(),
//                this.getY(),
//                this.scrollbarWidth(),
//                this.getHeight(),
//                mouseX,
//                mouseY);
//    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return super.isMouseOver(mouseX, mouseY) || this.isOverScrollbar(mouseX, mouseY);
    }

    protected boolean isOverScrollbar(double mouseX, double mouseY) {
        return ScreenHelper.isHovering(this.scrollBarX(),
                this.getY(),
                this.scrollbarWidth(),
                this.scrollbarHeight(),
                mouseX,
                mouseY);
    }

    @Override
    protected void renderSelection(GuiGraphics guiGraphics, int top, int width, int height, int outerColor, int innerColor) {
        // NO-OP
    }

    @Override
    public int getRowLeft() {
        return this.getX();
    }

    @Override
    protected int getRowTop(int index) {
        return super.getRowTop(index) - 4;
    }

    public static class Entry<E extends Entry<E>> extends ContainerObjectSelectionList.Entry<E> {
        private final List<AbstractWidget> children = new ArrayList<>();

        public <T extends AbstractWidget> T addRenderableWidget(T widget) {
            this.children.add(widget);
            return widget;
        }

        @Override
        public void render(GuiGraphics guiGraphics, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean hovering, float partialTick) {
            for (AbstractWidget abstractWidget : this.children) {
                abstractWidget.setY(top);
                abstractWidget.render(guiGraphics, mouseX, mouseY, partialTick);
            }
        }

        @Override
        public List<? extends GuiEventListener> children() {
            return this.children;
        }

        @Override
        public List<? extends NarratableEntry> narratables() {
            return this.children;
        }
    }
}
