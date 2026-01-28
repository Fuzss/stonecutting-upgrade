package fuzs.easystonecutters.data;

import fuzs.easystonecutters.init.ModRegistry;
import fuzs.puzzleslib.api.data.v2.AbstractRecipeProvider;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

public class ModRecipeProvider extends AbstractRecipeProvider {

    public ModRecipeProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addRecipes(RecipeOutput recipeOutput) {
        this.hammer(recipeOutput, ModRegistry.WOODEN_HAMMER_ITEM.value(), ItemTags.WOODEN_TOOL_MATERIALS);
        this.hammer(recipeOutput, ModRegistry.COPPER_HAMMER_ITEM.value(), ItemTags.COPPER_TOOL_MATERIALS);
        this.hammer(recipeOutput, ModRegistry.STONE_HAMMER_ITEM.value(), ItemTags.STONE_TOOL_MATERIALS);
        this.hammer(recipeOutput, ModRegistry.GOLDEN_HAMMER_ITEM.value(), ItemTags.GOLD_TOOL_MATERIALS);
        this.hammer(recipeOutput, ModRegistry.IRON_HAMMER_ITEM.value(), ItemTags.IRON_TOOL_MATERIALS);
        this.hammer(recipeOutput, ModRegistry.DIAMOND_HAMMER_ITEM.value(), ItemTags.DIAMOND_TOOL_MATERIALS);
        this.netheriteSmithing(ModRegistry.DIAMOND_HAMMER_ITEM.value(),
                RecipeCategory.TOOLS,
                ModRegistry.NETHERITE_HAMMER_ITEM.value());
    }

    public final void hammer(RecipeOutput recipeOutput, ItemLike result, TagKey<Item> ingredient) {
        this.shaped(RecipeCategory.TOOLS, result)
                .define('#', Items.STICK)
                .define('X', ingredient)
                .pattern(" X#")
                .pattern(" #X")
                .pattern("#  ")
                .unlockedBy(getHasName(ingredient), this.has(ingredient))
                .save(recipeOutput);
    }
}
