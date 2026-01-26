package fuzs.easystonecutters.client.renderer.rendertype;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.DepthTestFunction;
import fuzs.easystonecutters.EasyStonecutters;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.rendertype.LayeringTransform;
import net.minecraft.client.renderer.rendertype.OutputTarget;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;

public final class ModRenderTypes {
    /**
     * Disable depth test for rendering through blocks.
     *
     * @see RenderPipelines#LINES
     */
    public static final RenderPipeline LINES_SEE_THROUGH_RENDER_PIPELINE = RenderPipeline.builder(RenderPipelines.LINES_SNIPPET)
            .withLocation(EasyStonecutters.id("pipeline/lines_see_through"))
            .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
            .build();
    /**
     * @see net.minecraft.client.renderer.rendertype.RenderTypes#LINES
     */
    private static final RenderType LINES_SEE_THROUGH = RenderType.create(EasyStonecutters.id("lines_see_through")
                    .toString(),
            RenderSetup.builder(LINES_SEE_THROUGH_RENDER_PIPELINE)
                    .setLayeringTransform(LayeringTransform.VIEW_OFFSET_Z_LAYERING)
                    .setOutputTarget(OutputTarget.ITEM_ENTITY_TARGET)
                    .createRenderSetup());

    public static RenderType linesSeeThrough() {
        return LINES_SEE_THROUGH;
    }
}
