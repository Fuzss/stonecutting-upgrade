package fuzs.easystonecutters.data;

import fuzs.easystonecutters.EasyStonecutters;
import fuzs.easystonecutters.init.ModRegistry;
import fuzs.easystonecutters.world.item.crafting.HammeringRecipe;
import fuzs.puzzleslib.api.data.v2.AbstractRecipeProvider;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.criterion.RecipeUnlockedTrigger;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.Arrays;

public class ModRecipeProvider extends AbstractRecipeProvider {

    public ModRecipeProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addRecipes(RecipeOutput recipeOutput) {
        this.shaped(RecipeCategory.TOOLS, ModRegistry.MASONRY_HAMMER_ITEM.value())
                .define('#', Items.STICK)
                .define('X', ItemTags.COPPER_TOOL_MATERIALS)
                .pattern(" X#")
                .pattern(" #X")
                .pattern("#  ")
                .unlockedBy(getHasName(ItemTags.COPPER_TOOL_MATERIALS), this.has(ItemTags.COPPER_TOOL_MATERIALS))
                .save(recipeOutput);
        this.hammering(Blocks.STONE,
                Blocks.COBBLESTONE,
                Blocks.STONE_BRICKS,
                Blocks.CRACKED_STONE_BRICKS,
                Blocks.CHISELED_STONE_BRICKS);
        this.hammering(Blocks.STONE_SLAB, Blocks.COBBLESTONE_SLAB, Blocks.STONE_BRICK_SLAB);
        this.hammering(Blocks.STONE_STAIRS, Blocks.COBBLESTONE_STAIRS, Blocks.STONE_BRICK_STAIRS);
        this.hammering(Blocks.COBBLESTONE_WALL, Blocks.STONE_BRICK_WALL);
        this.hammering(Blocks.GRANITE, Blocks.POLISHED_GRANITE);
        this.hammering(Blocks.GRANITE_SLAB, Blocks.POLISHED_GRANITE_SLAB);
        this.hammering(Blocks.GRANITE_STAIRS, Blocks.POLISHED_GRANITE_STAIRS);
        this.hammering(Blocks.DIORITE, Blocks.POLISHED_DIORITE);
        this.hammering(Blocks.DIORITE_SLAB, Blocks.POLISHED_DIORITE_SLAB);
        this.hammering(Blocks.DIORITE_STAIRS, Blocks.POLISHED_DIORITE_STAIRS);
        this.hammering(Blocks.ANDESITE, Blocks.POLISHED_ANDESITE);
        this.hammering(Blocks.ANDESITE_SLAB, Blocks.POLISHED_ANDESITE_SLAB);
        this.hammering(Blocks.ANDESITE_STAIRS, Blocks.POLISHED_ANDESITE_STAIRS);
        this.hammering(Blocks.DEEPSLATE,
                Blocks.COBBLED_DEEPSLATE,
                Blocks.CHISELED_DEEPSLATE,
                Blocks.POLISHED_DEEPSLATE,
                Blocks.DEEPSLATE_BRICKS,
                Blocks.CRACKED_DEEPSLATE_BRICKS,
                Blocks.DEEPSLATE_TILES,
                Blocks.CRACKED_DEEPSLATE_TILES);
        this.hammering(Blocks.COBBLED_DEEPSLATE_SLAB,
                Blocks.POLISHED_DEEPSLATE_SLAB,
                Blocks.DEEPSLATE_BRICK_SLAB,
                Blocks.DEEPSLATE_TILE_SLAB);
        this.hammering(Blocks.COBBLED_DEEPSLATE_STAIRS,
                Blocks.POLISHED_DEEPSLATE_STAIRS,
                Blocks.DEEPSLATE_BRICK_STAIRS,
                Blocks.DEEPSLATE_TILE_STAIRS);
        this.hammering(Blocks.COBBLED_DEEPSLATE_WALL,
                Blocks.POLISHED_DEEPSLATE_WALL,
                Blocks.DEEPSLATE_BRICK_WALL,
                Blocks.DEEPSLATE_TILE_WALL);
        this.hammering(Blocks.TUFF,
                Blocks.POLISHED_TUFF,
                Blocks.CHISELED_TUFF,
                Blocks.TUFF_BRICKS,
                Blocks.CHISELED_TUFF_BRICKS);
        this.hammering(Blocks.TUFF_SLAB, Blocks.POLISHED_TUFF_SLAB, Blocks.TUFF_BRICK_SLAB);
        this.hammering(Blocks.TUFF_STAIRS, Blocks.POLISHED_TUFF_STAIRS, Blocks.TUFF_BRICK_STAIRS);
        this.hammering(Blocks.TUFF_WALL, Blocks.POLISHED_TUFF_WALL, Blocks.TUFF_BRICK_WALL);
        this.hammering(Blocks.SANDSTONE, Blocks.SMOOTH_SANDSTONE, Blocks.CUT_SANDSTONE, Blocks.CHISELED_SANDSTONE);
        this.hammering(Blocks.SANDSTONE_SLAB, Blocks.SMOOTH_SANDSTONE_SLAB, Blocks.CUT_SANDSTONE_SLAB);
        this.hammering(Blocks.SANDSTONE_STAIRS, Blocks.SMOOTH_SANDSTONE_STAIRS);
        this.hammering(Blocks.RED_SANDSTONE,
                Blocks.SMOOTH_RED_SANDSTONE,
                Blocks.CUT_RED_SANDSTONE,
                Blocks.CHISELED_RED_SANDSTONE);
        this.hammering(Blocks.RED_SANDSTONE_SLAB, Blocks.SMOOTH_RED_SANDSTONE_SLAB, Blocks.CUT_RED_SANDSTONE_SLAB);
        this.hammering(Blocks.RED_SANDSTONE_STAIRS, Blocks.SMOOTH_RED_SANDSTONE_STAIRS);
        this.hammering(Blocks.BASALT, Blocks.SMOOTH_BASALT, Blocks.POLISHED_BASALT);
        this.hammering(Blocks.BLACKSTONE,
                Blocks.POLISHED_BLACKSTONE,
                Blocks.CHISELED_POLISHED_BLACKSTONE,
                Blocks.POLISHED_BLACKSTONE_BRICKS,
                Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS);
        this.hammering(Blocks.BLACKSTONE_SLAB, Blocks.POLISHED_BLACKSTONE_SLAB, Blocks.POLISHED_BLACKSTONE_BRICK_SLAB);
        this.hammering(Blocks.BLACKSTONE_STAIRS,
                Blocks.POLISHED_BLACKSTONE_STAIRS,
                Blocks.POLISHED_BLACKSTONE_BRICK_STAIRS);
        this.hammering(Blocks.BLACKSTONE_WALL, Blocks.POLISHED_BLACKSTONE_WALL, Blocks.POLISHED_BLACKSTONE_BRICK_WALL);
        this.hammering(Blocks.END_STONE, Blocks.END_STONE_BRICKS);
    }

    public final void hammering(Block... blocks) {
        HolderSet.Direct<Block> holderSet = HolderSet.direct(Arrays.stream(blocks)
                .map(BuiltInRegistries.BLOCK::wrapAsHolder)
                .toList());
        this.hammeringResultFromBase(RecipeCategory.MISC, holderSet);
    }

    public final void hammeringResultFromBase(RecipeCategory category, HolderSet<Block> blocks) {
        // TODO handle this with tags
        Holder<Block> input = blocks.get(0);
        String recipeName = getItemName(input.value()) + "_hammering";
        ResourceKey<Recipe<?>> resourceKey = ResourceKey.create(Registries.RECIPE, EasyStonecutters.id(recipeName));
        Advancement.Builder builder = this.output.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(resourceKey))
                .rewards(AdvancementRewards.Builder.recipe(resourceKey))
                .requirements(AdvancementRequirements.Strategy.OR);
        builder.addCriterion(getHasName(input.value()), this.has(input.value()));
        Recipe<?> recipe = new HammeringRecipe(blocks);
        this.output.accept(resourceKey,
                recipe,
                builder.build(resourceKey.identifier().withPrefix("recipes/" + category.getFolderName() + "/")));
    }
}
