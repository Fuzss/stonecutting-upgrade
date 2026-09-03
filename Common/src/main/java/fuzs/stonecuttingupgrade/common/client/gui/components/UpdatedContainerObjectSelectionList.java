package fuzs.stonecuttingupgrade.common.client.gui.components;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;

public abstract class UpdatedContainerObjectSelectionList<E extends ContainerObjectSelectionList.Entry<E>> extends ContainerObjectSelectionList<E> {

    public UpdatedContainerObjectSelectionList(Minecraft minecraft, int width, int height, int y, int itemHeight) {
        super(minecraft, width, height, y, itemHeight);
    }

    @Override
    protected void renderDecorations(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        this.extractScrollbar(guiGraphics, mouseX, mouseY);
    }

    protected abstract void extractScrollbar(GuiGraphics guiGraphics, int mouseX, int mouseY);

    @Override
    protected final boolean scrollbarVisible() {
        return false;
    }

    @Override
    protected final int getMaxPosition() {
        return this.contentHeight();
    }

    protected int contentHeight() {
        return super.getMaxPosition() + 4;
    }

    @Override
    protected final int getScrollbarPosition() {
        return this.scrollBarX();
    }

    protected int scrollBarX() {
        return super.getScrollbarPosition();
    }

    public int scrollbarWidth() {
        return this.scrollerWidth();
    }

    public int scrollbarHeight() {
        return this.getHeight();
    }

    protected abstract int scrollerWidth();

    protected abstract int scrollerHeight();
}
