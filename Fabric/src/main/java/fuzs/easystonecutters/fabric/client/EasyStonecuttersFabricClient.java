package fuzs.easystonecutters.fabric.client;

import fuzs.easystonecutters.EasyStonecutters;
import fuzs.easystonecutters.client.EasyStonecuttersClient;
import fuzs.easystonecutters.client.handler.HighlightedBlocksHandler;
import fuzs.easystonecutters.client.util.OutlineShapeRenderer;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.RenderStateDataKey;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldExtractionContext;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class EasyStonecuttersFabricClient implements ClientModInitializer {
    private static final RenderStateDataKey<VoxelShape> OUTLINE_SHAPE_DATA_KEY = RenderStateDataKey.create(
            EasyStonecutters.id("outline_shape")::toString);

    @Override
    public void onInitializeClient() {
        ClientModConstructor.construct(EasyStonecutters.MOD_ID, EasyStonecuttersClient::new);
        registerEventHandlers();
    }

    private static void registerEventHandlers() {
        WorldRenderEvents.END_EXTRACTION.register((WorldExtractionContext context) -> {
            HighlightedBlocksHandler.pickHighlightedBlocks(context.world(),
                    context.camera(),
                    context.gameRenderer().getMinecraft().hitResult);
            if (context.worldState().blockOutlineRenderState != null) {
                VoxelShape voxelShape = HighlightedBlocksHandler.getHighlightedBlocks().getJoinedShape(context.world());
                if (!voxelShape.isEmpty()) {
                    context.worldState().setData(OUTLINE_SHAPE_DATA_KEY, voxelShape);
                }
            }
        });
        WorldRenderEvents.AFTER_ENTITIES.register((WorldRenderContext context) -> {
            VoxelShape voxelShape = context.worldState().getDataOrDefault(OUTLINE_SHAPE_DATA_KEY, Shapes.empty());
            if (!voxelShape.isEmpty()) {
                OutlineShapeRenderer.renderLines(context.matrices(),
                        Minecraft.getInstance().renderBuffers().bufferSource(),
                        context.worldState().cameraRenderState.pos,
                        voxelShape);
            }
        });
    }
}
