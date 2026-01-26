package fuzs.easystonecutters.client;

import fuzs.easystonecutters.client.gui.screens.inventory.ModStonecutterScreen;
import fuzs.easystonecutters.client.handler.StoneTransmuteHandler;
import fuzs.easystonecutters.client.handler.TransmutateShapeRenderingHandler;
import fuzs.easystonecutters.client.renderer.rendertype.ModRenderTypes;
import fuzs.easystonecutters.init.ModRegistry;
import fuzs.hotbarslotcycling.api.v1.client.SlotCyclingProvider;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.api.client.core.v1.context.RenderPipelinesContext;
import fuzs.puzzleslib.api.client.event.v1.ClientTickEvents;
import fuzs.puzzleslib.api.client.event.v1.gui.RenderGuiEvents;
import fuzs.puzzleslib.api.client.event.v1.gui.ScreenOpeningCallback;
import fuzs.puzzleslib.api.event.v1.core.EventResultHolder;
import fuzs.puzzleslib.api.event.v1.entity.player.PlayerInteractEvents;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.StonecutterScreen;
import org.jspecify.annotations.Nullable;

public class EasyStonecuttersClient implements ClientModConstructor {

    @Override
    public void onConstructMod() {
        registerEventHandlers();
    }

    private static void registerEventHandlers() {
        ScreenOpeningCallback.EVENT.register(EasyStonecuttersClient::onScreenOpening);
        RenderGuiEvents.BEFORE.register(PocketStonecutterCyclingProvider::onBeforeRenderGui);
        ClientTickEvents.END.register(TransmutateShapeRenderingHandler::onEndClientTick);
//        RenderLevelEvents.AFTER_ENTITIES.register(TransmutateShapeRenderingHandler::onRenderLevelAfterEntities);
        PlayerInteractEvents.USE_BLOCK.register(StoneTransmuteHandler::onUseBlock);
    }

    @Override
    public void onClientSetup() {
        SlotCyclingProvider.registerProvider(ModRegistry.POCKET_STONECUTTER_ITEM.value(),
                PocketStonecutterCyclingProvider::new);
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

    @Override
    public void onRegisterRenderPipelines(RenderPipelinesContext context) {
        context.registerRenderPipeline(ModRenderTypes.LINES_SEE_THROUGH_RENDER_PIPELINE);
    }
}
