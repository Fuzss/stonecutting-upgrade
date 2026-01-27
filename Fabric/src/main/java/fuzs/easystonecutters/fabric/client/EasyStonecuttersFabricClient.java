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
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.state.BlockOutlineRenderState;
import net.minecraft.network.Connection;
import net.minecraft.world.item.crafting.RecipeMap;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

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
        WorldRenderEvents.AFTER_BLOCK_OUTLINE_EXTRACTION.register((WorldExtractionContext context, @Nullable HitResult hitResult) -> {
            BlockOutlineRenderState blockOutlineRenderState = context.worldState().blockOutlineRenderState;
            if (blockOutlineRenderState != null && hitResult != null && hitResult.getType() == HitResult.Type.BLOCK) {
                HighlightedBlocksHandler.pickHighlightedBlocks(context.world(),
                        context.camera(),
                        (BlockHitResult) hitResult);
                VoxelShape voxelShape = HighlightedBlocksHandler.getHighlightedBlocks().getJoinedShape(context.world());
                if (!voxelShape.isEmpty()) {
                    blockOutlineRenderState.setData(OUTLINE_SHAPE_DATA_KEY, voxelShape);
                }
            }
        });
        WorldRenderEvents.BEFORE_BLOCK_OUTLINE.register((WorldRenderContext context, BlockOutlineRenderState outlineRenderState) -> {
            VoxelShape voxelShape = outlineRenderState.getDataOrDefault(OUTLINE_SHAPE_DATA_KEY, Shapes.empty());
            if (!voxelShape.isEmpty()) {
                OutlineShapeRenderer.renderLines(context.matrices(),
                        (MultiBufferSource.BufferSource) context.consumers(),
                        context.worldState().cameraRenderState.pos,
                        voxelShape);
            }
            return true;
        });
    }
}
