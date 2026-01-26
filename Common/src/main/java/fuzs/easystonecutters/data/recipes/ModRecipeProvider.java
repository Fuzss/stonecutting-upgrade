package fuzs.easystonecutters.data.recipes;

import fuzs.easystonecutters.EasyStonecutters;
import fuzs.easystonecutters.world.item.crafting.TransmutationInWorldRecipe;
import fuzs.puzzleslib.api.data.v2.AbstractRecipeProvider;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class ModRecipeProvider extends AbstractRecipeProvider {

    public ModRecipeProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addRecipes(RecipeOutput recipeOutput) {
        this.inWorldReversible(recipeOutput,
                Blocks.STONE,
                Blocks.COBBLESTONE,
                Blocks.MOSSY_COBBLESTONE,
                Blocks.STONE_BRICKS,
                Blocks.MOSSY_STONE_BRICKS,
                Blocks.CRACKED_STONE_BRICKS,
                Blocks.CHISELED_STONE_BRICKS);
        this.inWorldReversible(recipeOutput,
                Blocks.STONE_SLAB,
                Blocks.COBBLESTONE_SLAB,
                Blocks.MOSSY_COBBLESTONE_SLAB,
                Blocks.STONE_BRICK_SLAB,
                Blocks.MOSSY_STONE_BRICK_SLAB);
        this.inWorldReversible(recipeOutput,
                Blocks.STONE_STAIRS,
                Blocks.COBBLESTONE_STAIRS,
                Blocks.MOSSY_COBBLESTONE_STAIRS,
                Blocks.STONE_BRICK_STAIRS,
                Blocks.MOSSY_STONE_BRICK_STAIRS);
        this.inWorldReversible(recipeOutput,
                Blocks.COBBLESTONE_WALL,
                Blocks.MOSSY_COBBLESTONE_WALL,
                Blocks.STONE_BRICK_WALL,
                Blocks.MOSSY_STONE_BRICK_WALL);
        this.inWorldReversible(recipeOutput, Blocks.GRANITE, Blocks.POLISHED_GRANITE);
        this.inWorldReversible(recipeOutput, Blocks.GRANITE_SLAB, Blocks.POLISHED_GRANITE_SLAB);
        this.inWorldReversible(recipeOutput, Blocks.GRANITE_STAIRS, Blocks.POLISHED_GRANITE_STAIRS);
        this.inWorldReversible(recipeOutput, Blocks.DIORITE, Blocks.POLISHED_DIORITE);
        this.inWorldReversible(recipeOutput, Blocks.DIORITE_SLAB, Blocks.POLISHED_DIORITE_SLAB);
        this.inWorldReversible(recipeOutput, Blocks.DIORITE_STAIRS, Blocks.POLISHED_DIORITE_STAIRS);
        this.inWorldReversible(recipeOutput, Blocks.ANDESITE, Blocks.POLISHED_ANDESITE);
        this.inWorldReversible(recipeOutput, Blocks.ANDESITE_SLAB, Blocks.POLISHED_ANDESITE_SLAB);
        this.inWorldReversible(recipeOutput, Blocks.ANDESITE_STAIRS, Blocks.POLISHED_ANDESITE_STAIRS);
        this.inWorldReversible(recipeOutput,
                Blocks.DEEPSLATE,
                Blocks.COBBLED_DEEPSLATE,
                Blocks.CHISELED_DEEPSLATE,
                Blocks.POLISHED_DEEPSLATE,
                Blocks.DEEPSLATE_BRICKS,
                Blocks.CRACKED_DEEPSLATE_BRICKS,
                Blocks.DEEPSLATE_TILES,
                Blocks.CRACKED_DEEPSLATE_TILES);
        this.inWorldReversible(recipeOutput,
                Blocks.COBBLED_DEEPSLATE_SLAB,
                Blocks.POLISHED_DEEPSLATE_SLAB,
                Blocks.DEEPSLATE_BRICK_SLAB,
                Blocks.DEEPSLATE_TILE_SLAB);
        this.inWorldReversible(recipeOutput,
                Blocks.COBBLED_DEEPSLATE_STAIRS,
                Blocks.POLISHED_DEEPSLATE_STAIRS,
                Blocks.DEEPSLATE_BRICK_STAIRS,
                Blocks.DEEPSLATE_TILE_STAIRS);
        this.inWorldReversible(recipeOutput,
                Blocks.COBBLED_DEEPSLATE_WALL,
                Blocks.POLISHED_DEEPSLATE_WALL,
                Blocks.DEEPSLATE_BRICK_WALL,
                Blocks.DEEPSLATE_TILE_WALL);
        this.inWorldReversible(recipeOutput,
                Blocks.TUFF,
                Blocks.POLISHED_TUFF,
                Blocks.CHISELED_TUFF,
                Blocks.TUFF_BRICKS,
                Blocks.CHISELED_TUFF_BRICKS);
        this.inWorldReversible(recipeOutput, Blocks.TUFF_SLAB, Blocks.POLISHED_TUFF_SLAB, Blocks.TUFF_BRICK_SLAB);
        this.inWorldReversible(recipeOutput, Blocks.TUFF_STAIRS, Blocks.POLISHED_TUFF_STAIRS, Blocks.TUFF_BRICK_STAIRS);
        this.inWorldReversible(recipeOutput, Blocks.TUFF_WALL, Blocks.POLISHED_TUFF_WALL, Blocks.TUFF_BRICK_WALL);
        this.inWorldReversible(recipeOutput,
                Blocks.SANDSTONE,
                Blocks.SMOOTH_SANDSTONE,
                Blocks.CUT_SANDSTONE,
                Blocks.CHISELED_SANDSTONE);
        this.inWorldReversible(recipeOutput,
                Blocks.SANDSTONE_SLAB,
                Blocks.SMOOTH_SANDSTONE_SLAB,
                Blocks.CUT_SANDSTONE_SLAB);
        this.inWorldReversible(recipeOutput, Blocks.SANDSTONE_STAIRS, Blocks.SMOOTH_SANDSTONE_STAIRS);
        this.inWorldReversible(recipeOutput,
                Blocks.RED_SANDSTONE,
                Blocks.SMOOTH_RED_SANDSTONE,
                Blocks.CUT_RED_SANDSTONE,
                Blocks.CHISELED_RED_SANDSTONE);
        this.inWorldReversible(recipeOutput,
                Blocks.RED_SANDSTONE_SLAB,
                Blocks.SMOOTH_RED_SANDSTONE_SLAB,
                Blocks.CUT_RED_SANDSTONE_SLAB);
        this.inWorldReversible(recipeOutput, Blocks.RED_SANDSTONE_STAIRS, Blocks.SMOOTH_RED_SANDSTONE_STAIRS);
        this.inWorldReversible(recipeOutput,
                Blocks.WHITE_TERRACOTTA,
                Blocks.ORANGE_TERRACOTTA,
                Blocks.MAGENTA_TERRACOTTA,
                Blocks.LIGHT_BLUE_TERRACOTTA,
                Blocks.YELLOW_TERRACOTTA,
                Blocks.LIME_TERRACOTTA,
                Blocks.PINK_TERRACOTTA,
                Blocks.GRAY_TERRACOTTA,
                Blocks.LIGHT_GRAY_TERRACOTTA,
                Blocks.CYAN_TERRACOTTA,
                Blocks.PURPLE_TERRACOTTA,
                Blocks.BLUE_TERRACOTTA,
                Blocks.BROWN_TERRACOTTA,
                Blocks.GREEN_TERRACOTTA,
                Blocks.RED_TERRACOTTA,
                Blocks.BLACK_TERRACOTTA);
        this.inWorldReversible(recipeOutput,
                Blocks.WHITE_GLAZED_TERRACOTTA,
                Blocks.ORANGE_GLAZED_TERRACOTTA,
                Blocks.MAGENTA_GLAZED_TERRACOTTA,
                Blocks.LIGHT_BLUE_GLAZED_TERRACOTTA,
                Blocks.YELLOW_GLAZED_TERRACOTTA,
                Blocks.LIME_GLAZED_TERRACOTTA,
                Blocks.PINK_GLAZED_TERRACOTTA,
                Blocks.GRAY_GLAZED_TERRACOTTA,
                Blocks.LIGHT_GRAY_GLAZED_TERRACOTTA,
                Blocks.CYAN_GLAZED_TERRACOTTA,
                Blocks.PURPLE_GLAZED_TERRACOTTA,
                Blocks.BLUE_GLAZED_TERRACOTTA,
                Blocks.BROWN_GLAZED_TERRACOTTA,
                Blocks.GREEN_GLAZED_TERRACOTTA,
                Blocks.RED_GLAZED_TERRACOTTA,
                Blocks.BLACK_GLAZED_TERRACOTTA);
        this.inWorldReversible(recipeOutput, Blocks.BASALT, Blocks.SMOOTH_BASALT, Blocks.POLISHED_BASALT);
        this.inWorldReversible(recipeOutput,
                Blocks.BLACKSTONE,
                Blocks.POLISHED_BLACKSTONE,
                Blocks.CHISELED_POLISHED_BLACKSTONE,
                Blocks.POLISHED_BLACKSTONE_BRICKS,
                Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS);
        this.inWorldReversible(recipeOutput,
                Blocks.BLACKSTONE_SLAB,
                Blocks.POLISHED_BLACKSTONE_SLAB,
                Blocks.POLISHED_BLACKSTONE_BRICK_SLAB);
        this.inWorldReversible(recipeOutput,
                Blocks.BLACKSTONE_STAIRS,
                Blocks.POLISHED_BLACKSTONE_STAIRS,
                Blocks.POLISHED_BLACKSTONE_BRICK_STAIRS);
        this.inWorldReversible(recipeOutput,
                Blocks.BLACKSTONE_WALL,
                Blocks.POLISHED_BLACKSTONE_WALL,
                Blocks.POLISHED_BLACKSTONE_BRICK_WALL);
        this.inWorldReversible(recipeOutput, Blocks.END_STONE, Blocks.END_STONE_BRICKS);
    }

    public final void inWorldReversible(RecipeOutput recipeOutput, Block... blocks) {
        for (Block result : blocks) {
            for (Block ingredient : blocks) {
                if (result != ingredient) {
                    this.hammeringResultFromBase(RecipeCategory.MISC, result, ingredient);
                }
            }
        }
    }

    public final void inWorldOneWay(RecipeOutput recipeOutput, Block ingredient, Block result) {
        TransmutationInWorldRecipeBuilder.oneWay(ingredient, result)
                .unlockedBy(getHasName(ingredient), this.has(ingredient))
                .save(recipeOutput, id(getConversionRecipeName(result, ingredient) + "_transmutation_in_world"));
    }

    public final void hammeringResultFromBase(RecipeCategory category, Block result, Block ingredient) {
        new SingleItemRecipeBuilder(category,
                TransmutationInWorldRecipe::new,
                Ingredient.of(ingredient),
                result,
                1).unlockedBy(getHasName(ingredient), this.has(ingredient))
                .save(this.output, getConversionRecipeName(result, ingredient) + "_hammering");
    }

    protected static ResourceKey<Recipe<?>> id(String path) {
        return ResourceKey.create(Registries.RECIPE, EasyStonecutters.id(path));
    }
}
