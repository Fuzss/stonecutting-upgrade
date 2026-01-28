package fuzs.easystonecutters.client;

import fuzs.easystonecutters.EasyStonecutters;
import fuzs.easystonecutters.client.gui.screens.inventory.ModStonecutterScreen;
import fuzs.easystonecutters.client.handler.HighlightedBlocksHandler;
import fuzs.easystonecutters.client.handler.SelectedItemInGuiHandler;
import fuzs.easystonecutters.client.renderer.rendertype.ModRenderTypes;
import fuzs.easystonecutters.init.ModRegistry;
import fuzs.easystonecutters.world.item.HammerItem;
import fuzs.easystonecutters.world.item.component.SelectionMode;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.api.client.core.v1.context.GuiLayersContext;
import fuzs.puzzleslib.api.client.core.v1.context.RenderPipelinesContext;
import fuzs.puzzleslib.api.client.event.v1.ClientTickEvents;
import fuzs.puzzleslib.api.client.event.v1.entity.player.ClientPlayerNetworkEvents;
import fuzs.puzzleslib.api.client.event.v1.gui.ScreenOpeningCallback;
import fuzs.puzzleslib.api.client.gui.v2.tooltip.ItemTooltipRegistry;
import fuzs.puzzleslib.api.event.v1.core.EventResultHolder;
import fuzs.puzzleslib.api.event.v1.entity.player.PlayerInteractEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.StonecutterScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.jspecify.annotations.Nullable;

import java.util.function.Consumer;

public class EasyStonecuttersClient implements ClientModConstructor {

    @Override
    public void onConstructMod() {
        registerEventHandlers();
    }

    private static void registerEventHandlers() {
        ScreenOpeningCallback.EVENT.register(EasyStonecuttersClient::onScreenOpening);
        ClientTickEvents.END.register(HighlightedBlocksHandler::onEndClientTick);
        PlayerInteractEvents.USE_BLOCK.register(HighlightedBlocksHandler::onUseBlock);
        ClientPlayerNetworkEvents.LEAVE.register(HighlightedBlocksHandler::onPlayerLeave);
        ClientTickEvents.END.register(SelectedItemInGuiHandler::onEndClientTick);
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
    public void onClientSetup() {
        ItemTooltipRegistry.ITEM.registerItemTooltip((ItemStack itemStack) -> itemStack.is(ModRegistry.HAMMERS_ITEM_TAG),
                EasyStonecuttersClient::appendHoverText);
    }

    private static void appendHoverText(ItemStack itemStack, Item.TooltipContext tooltipContext, TooltipFlag tooltipFlag, @Nullable Player player, Consumer<Component> tooltipLineConsumer) {
        if (EnchantmentHelper.has(itemStack,
                ModRegistry.CONTROLS_SELECTION_MODE_ENCHANTMENT_EFFECT_COMPONENT_TYPE.value())) {
            SelectionMode selectionMode = HammerItem.getSelectionMode(itemStack);
            tooltipLineConsumer.accept(Component.translatable(HammerItem.getCurrentSelectionTranslationKey(),
                    selectionMode.getComponent()).withStyle(ChatFormatting.GRAY));
        }
    }

    @Override
    public void onRegisterRenderPipelines(RenderPipelinesContext context) {
        context.registerRenderPipeline(ModRenderTypes.LINES_SEE_THROUGH_RENDER_PIPELINE);
    }

    @Override
    public void onRegisterGuiLayers(GuiLayersContext context) {
        context.registerGuiLayer(GuiLayersContext.HOTBAR,
                EasyStonecutters.id("selected_item"),
                SelectedItemInGuiHandler::onRenderGuiLayer);
    }
}
