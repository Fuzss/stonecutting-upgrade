package fuzs.stonecuttingupgrade.fabric.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import fuzs.stonecuttingupgrade.common.client.gui.screens.inventory.CustomStonecutterScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(AbstractContainerScreen.class)
abstract class AbstractContainerScreenFabricMixin<T extends AbstractContainerMenu> extends Screen {

    protected AbstractContainerScreenFabricMixin(Component title) {
        super(title);
    }

    @ModifyReturnValue(method = "keyPressed",
                       at = @At("RETURN"),
                       slice = @Slice(from = @At(value = "INVOKE",
                                                 target = "Lnet/minecraft/client/gui/screens/inventory/AbstractContainerScreen;checkHotbarKeyPressed(II)Z")))
    public boolean keyPressed(boolean keyPressed) {
        return !(AbstractContainerScreen.class.cast(this) instanceof CustomStonecutterScreen) && keyPressed;
    }
}
