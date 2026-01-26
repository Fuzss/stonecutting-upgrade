package fuzs.easystonecutters.client;

import fuzs.hotbarslotcycling.api.v1.client.ItemCyclingProvider;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Holder;
import net.minecraft.util.Mth;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.SelectableRecipe;
import net.minecraft.world.item.crafting.StonecutterRecipe;
import net.minecraft.world.item.crafting.display.SlotDisplayContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public record PocketStonecutterCyclingProvider(ItemStack itemInHand,
                                               InteractionHand interactionHand) implements ItemCyclingProvider {
    private static Holder<Block> recipeInput = Blocks.AIR.builtInRegistryHolder();
    private static SelectableRecipe.SingleInputSet<StonecutterRecipe> recipes = SelectableRecipe.SingleInputSet.empty();
    private static int recipeIndex;

    public static void onBeforeRenderGui(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        Minecraft minecraft = Minecraft.getInstance();
        Holder<Block> holder = getHighlightedBlock(minecraft.level, minecraft.hitResult);
        if (!recipeInput.is(holder)) {
            recipeInput = holder;
            recipes = minecraft.getConnection()
                    .recipes()
                    .stonecutterRecipes()
                    .selectByInput(new ItemStack(holder.value()));
        }
    }

    private static Holder<Block> getHighlightedBlock(Level level, HitResult hitResult) {
        if (hitResult.getType() == HitResult.Type.BLOCK) {
            return level.getBlockState(((BlockHitResult) hitResult).getBlockPos()).getBlockHolder();
        } else {
            return Blocks.AIR.builtInRegistryHolder();
        }
    }

    @Override
    public ItemStack getSelectedStack() {
        return !recipes.isEmpty() ? this.getRecipeOutput(recipeIndex) : ItemStack.EMPTY;
    }

    @Override
    public ItemStack getForwardStack() {
        return !recipes.isEmpty() ? this.getRecipeOutput(recipeIndex + 1) : ItemStack.EMPTY;
    }

    @Override
    public ItemStack getBackwardStack() {
        return !recipes.isEmpty() ? this.getRecipeOutput(recipeIndex - 1) : ItemStack.EMPTY;
    }

    private ItemStack getRecipeOutput(int recipeIndex) {
        ContextMap contextMap = SlotDisplayContext.fromLevel(Minecraft.getInstance().level);
        int wrappedRecipeIndex = Mth.positiveModulo(recipeIndex, recipes.size());
        return recipes.entries().get(wrappedRecipeIndex).recipe().optionDisplay().resolveForFirstStack(contextMap);
    }

    @Override
    public boolean cycleSlotForward() {
        if (!recipes.isEmpty()) {
            recipeIndex = Mth.positiveModulo(++recipeIndex, recipes.size());
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean cycleSlotBackward() {
        if (!recipes.isEmpty()) {
            recipeIndex = Mth.positiveModulo(--recipeIndex, recipes.size());
            return true;
        } else {
            return false;
        }
    }
}
