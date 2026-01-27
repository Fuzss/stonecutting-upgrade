package fuzs.easystonecutters.client.handler;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import fuzs.easystonecutters.client.renderer.rendertype.ModRenderTypes;
import fuzs.easystonecutters.init.ModRegistry;
import fuzs.easystonecutters.util.MiniumStoneHelper;
import fuzs.easystonecutters.world.item.MasonryHammerItem;
import fuzs.easystonecutters.world.item.component.SelectionMode;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.ShapeRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.util.ARGB;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class OutlineShapeRenderingHandler {
    @Nullable
    private static HighlightedBlocksHolder blockWalker;

    public static @Nullable VoxelShape getOutlineShape(ClientLevel clientLevel, BlockHitResult hitResult, Camera camera) {
        if (camera.entity() instanceof Player player) {
            InteractionHand interactionHand = MiniumStoneHelper.getHandHoldingItem(player,
                    ModRegistry.MASONRY_HAMMER_ITEM.value());
            if (interactionHand != null) {
                ItemStack itemInHand = player.getItemInHand(interactionHand);
                int charge = MasonryHammerItem.getCharge(itemInHand);
                SelectionMode selectionMode = MasonryHammerItem.getSelectionMode(itemInHand);
                HighlightedBlocksHolder blockWalker = OutlineShapeRenderingHandler.blockWalker;
                if (blockWalker == null || !blockWalker.stillValid(charge, selectionMode, hitResult, clientLevel)) {
                    OutlineShapeRenderingHandler.blockWalker = blockWalker = HighlightedBlocksHolder.fromHitResult(charge,
                            selectionMode,
                            hitResult,
                            clientLevel);
                }

                return blockWalker.getJoinedShape(clientLevel);
            }
        }

        return null;
    }

    public static void renderLines(PoseStack poseStack, MultiBufferSource.BufferSource bufferSource, Vec3 cameraPosition, VoxelShape voxelShape) {
        renderShape(poseStack,
                bufferSource,
                cameraPosition,
                voxelShape,
                ModRenderTypes.linesSeeThrough(),
                ARGB.white(0.65F));
        renderShape(poseStack, bufferSource, cameraPosition, voxelShape, RenderTypes.lines(), -1);
    }

    private static void renderShape(PoseStack poseStack, MultiBufferSource.BufferSource bufferSource, Vec3 cameraPosition, VoxelShape voxelShape, RenderType renderType, int color) {
        VertexConsumer vertexConsumer = bufferSource.getBuffer(renderType);
        float lineWidth = Minecraft.getInstance().getWindow().getAppropriateLineWidth();
        ShapeRenderer.renderShape(poseStack,
                vertexConsumer,
                voxelShape,
                -cameraPosition.x,
                -cameraPosition.y,
                -cameraPosition.z,
                color,
                lineWidth);
        bufferSource.endBatch(renderType);
    }

    public static void onEndClientTick(Minecraft minecraft) {
        if (blockWalker != null) {
            blockWalker.testAllBlocks(minecraft.hitResult, minecraft.level);
        }
    }

    @Nullable
    public static HighlightedBlocksHolder getBlockWalker() {
        return blockWalker;
    }

    public static void clearBlockWalker() {
        blockWalker = null;
    }
}
