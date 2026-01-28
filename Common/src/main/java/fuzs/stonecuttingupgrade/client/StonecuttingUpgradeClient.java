package fuzs.stonecuttingupgrade.client;

import fuzs.stonecuttingupgrade.client.gui.screens.inventory.ModStonecutterScreen;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.api.client.event.v1.gui.ScreenOpeningCallback;
import fuzs.puzzleslib.api.event.v1.core.EventResultHolder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.StonecutterScreen;
import org.jspecify.annotations.Nullable;

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
            return EventResultHolder.interrupt(new ModStonecutterScreen(screen.getMenu(),
                    screen.minecraft.player.getInventory(),
                    screen.getTitle()));
        } else {
            return EventResultHolder.pass();
        }
    }
}
