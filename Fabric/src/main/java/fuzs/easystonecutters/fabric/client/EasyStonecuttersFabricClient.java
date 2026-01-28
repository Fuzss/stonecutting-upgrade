package fuzs.easystonecutters.fabric.client;

import fuzs.easystonecutters.EasyStonecutters;
import fuzs.easystonecutters.client.EasyStonecuttersClient;
import fuzs.easystonecutters.client.handler.HighlightedBlocksHandler;
import fuzs.easystonecutters.client.util.ClientRecipeHelper;
import fuzs.easystonecutters.client.util.OutlineShapeRenderer;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.fabric.api.client.event.v1.FabricClientPlayerEvents;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.recipe.v1.sync.ClientRecipeSynchronizedEvent;
import net.fabricmc.fabric.api.client.rendering.v1.RenderStateDataKey;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldExtractionContext;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
import net.fabricmc.fabric.api.recipe.v1.sync.SynchronizedRecipes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.Connection;
import net.minecraft.world.item.crafting.RecipeMap;
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
        ClientRecipeSynchronizedEvent.EVENT.register((Minecraft client, SynchronizedRecipes recipes) -> {
            ClientRecipeHelper.setRecipeMap(RecipeMap.create(recipes.recipes()));
        });
        FabricClientPlayerEvents.PLAYER_LEAVE.register((LocalPlayer player, MultiPlayerGameMode multiPlayerGameMode, Connection connection) -> {
            ClientRecipeHelper.setRecipeMap(RecipeMap.EMPTY);
        });
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
