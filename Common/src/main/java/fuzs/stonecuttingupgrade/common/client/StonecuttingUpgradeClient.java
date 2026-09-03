package fuzs.stonecuttingupgrade.common.client;

import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.api.client.event.v2.gui.ScreenOpeningCallback;
import fuzs.puzzleslib.api.event.v1.core.EventResultHolder;
import fuzs.stonecuttingupgrade.common.client.gui.screens.inventory.CustomStonecutterScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.StonecutterScreen;
import org.jetbrains.annotations.Nullable;

public class StonecuttingUpgradeClient implements ClientModConstructor {

    @Override
    public void onConstructMod() {
        registerEventHandlers();
    }

    private static void registerEventHandlers() {
        ScreenOpeningCallback.EVENT.register(StonecuttingUpgradeClient::onScreenOpening);
    }

    private static EventResultHolder<Screen> onScreenOpening(@Nullable Screen oldScreen, @Nullable Screen newScreen) {
        if (newScreen instanceof StonecutterScreen screen) {
            return EventResultHolder.interrupt(new CustomStonecutterScreen(screen.getMenu(),
                    Minecraft.getInstance().player.getInventory(),
                    screen.getTitle()));
        } else {
            return EventResultHolder.pass();
        }
    }
}
