package fuzs.easystonecutters.world.item.component;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.Block;

public record SelectedSingleItemRecipe(Holder<Block> recipeInput, ResourceKey<Recipe<?>> recipeId) {

}
