package fuzs.easystonecutters.neoforge.client;

import com.mojang.blaze3d.vertex.PoseStack;
import fuzs.easystonecutters.EasyStonecutters;
import fuzs.easystonecutters.client.EasyStonecuttersClient;
import fuzs.easystonecutters.client.handler.OutlineShapeRenderingHandler;
import fuzs.easystonecutters.client.util.ClientRecipeHelper;
import fuzs.easystonecutters.data.client.ModLanguageProvider;
import fuzs.easystonecutters.data.client.ModModelProvider;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.neoforge.api.data.v2.core.DataProviderHelper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.state.BlockOutlineRenderState;
import net.minecraft.client.renderer.state.LevelRenderState;
import net.minecraft.world.item.crafting.RecipeMap;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.ExtractBlockOutlineRenderStateEvent;
import net.neoforged.neoforge.client.event.RecipesReceivedEvent;
import net.neoforged.neoforge.common.NeoForge;

@Mod(value = EasyStonecutters.MOD_ID, dist = Dist.CLIENT)
public class EasyStonecuttersNeoForgeClient {

    public EasyStonecuttersNeoForgeClient() {
        ClientModConstructor.construct(EasyStonecutters.MOD_ID, EasyStonecuttersClient::new);
        registerEventHandlers(NeoForge.EVENT_BUS);
        DataProviderHelper.registerDataProviders(EasyStonecutters.MOD_ID,
                ModLanguageProvider::new,
                ModModelProvider::new);
    }

    private static void registerEventHandlers(IEventBus eventBus) {
        eventBus.addListener((final RecipesReceivedEvent event) -> {
            ClientRecipeHelper.setRecipeMap(event.getRecipeMap());
        });
        eventBus.addListener((final ClientPlayerNetworkEvent.LoggingOut event) -> {
            ClientRecipeHelper.setRecipeMap(RecipeMap.EMPTY);
        });
        eventBus.addListener((final ExtractBlockOutlineRenderStateEvent event) -> {
            VoxelShape voxelShape = OutlineShapeRenderingHandler.getOutlineShape(event.getLevel(),
                    event.getHitResult(),
                    event.getCamera());
            if (voxelShape != null) {
                event.addCustomRenderer((BlockOutlineRenderState renderState, MultiBufferSource.BufferSource bufferSource, PoseStack poseStack, boolean isTranslucentPass, LevelRenderState levelRenderState) -> {
                    OutlineShapeRenderingHandler.renderLines(poseStack,
                            bufferSource,
                            levelRenderState.cameraRenderState.pos,
                            voxelShape);
                    return false;
                });
            }
        });
    }
}
