package fuzs.easystonecutters.neoforge.client;

import fuzs.easystonecutters.EasyStonecutters;
import fuzs.easystonecutters.client.EasyStonecuttersClient;
import fuzs.easystonecutters.client.handler.HighlightedBlocksHandler;
import fuzs.easystonecutters.client.util.OutlineShapeRenderer;
import fuzs.easystonecutters.data.client.ModLanguageProvider;
import fuzs.easystonecutters.data.client.ModModelProvider;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.neoforge.api.data.v2.core.DataProviderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ExtractLevelRenderStateEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.common.NeoForge;

@Mod(value = EasyStonecutters.MOD_ID, dist = Dist.CLIENT)
public class EasyStonecuttersNeoForgeClient {
    private static final ContextKey<VoxelShape> OUTLINE_SHAPE_DATA_KEY = new ContextKey<>(EasyStonecutters.id(
            "outline_shape"));

    public EasyStonecuttersNeoForgeClient() {
        ClientModConstructor.construct(EasyStonecutters.MOD_ID, EasyStonecuttersClient::new);
        registerEventHandlers(NeoForge.EVENT_BUS);
        DataProviderHelper.registerDataProviders(EasyStonecutters.MOD_ID,
                ModLanguageProvider::new,
                ModModelProvider::new);
    }

    private static void registerEventHandlers(IEventBus eventBus) {
        eventBus.addListener((final ExtractLevelRenderStateEvent event) -> {
            HighlightedBlocksHandler.pickHighlightedBlocks(event.getLevel(),
                    event.getCamera(),
                    Minecraft.getInstance().hitResult);
            if (event.getRenderState().blockOutlineRenderState != null) {
                VoxelShape voxelShape = HighlightedBlocksHandler.getHighlightedBlocks()
                        .getJoinedShape(event.getLevel());
                if (!voxelShape.isEmpty()) {
                    event.getRenderState().setRenderData(OUTLINE_SHAPE_DATA_KEY, voxelShape);
                }
            }
        });
        eventBus.addListener((final RenderLevelStageEvent.AfterEntities event) -> {
            VoxelShape voxelShape = event.getLevelRenderState()
                    .getRenderDataOrDefault(OUTLINE_SHAPE_DATA_KEY, Shapes.empty());
            if (!voxelShape.isEmpty()) {
                OutlineShapeRenderer.renderLines(event.getPoseStack(),
                        Minecraft.getInstance().renderBuffers().bufferSource(),
                        event.getLevelRenderState().cameraRenderState.pos,
                        voxelShape);
            }
        });
    }
}
