package fuzs.easystonecutters.client.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import fuzs.easystonecutters.client.renderer.rendertype.ModRenderTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.ShapeRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.util.ARGB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

public class OutlineShapeRenderer {

    public static void renderLines(PoseStack poseStack, MultiBufferSource.BufferSource bufferSource, Vec3 cameraPosition, VoxelShape voxelShape) {
        renderShape(poseStack,
                bufferSource,
                cameraPosition,
                voxelShape,
                ModRenderTypes.linesSeeThrough(),
                ARGB.white(0.35F));
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
}
