package fuzs.easystonecutters.client.util;

import fuzs.easystonecutters.init.ModRegistry;
import fuzs.easystonecutters.world.item.crafting.HammeringRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeMap;
import net.minecraft.world.item.crafting.SelectableRecipe;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ClientRecipeHelper {
    private static SelectableRecipe.SingleInputSet<HammeringRecipe> syncedRecipes = SelectableRecipe.SingleInputSet.empty();

    public static void setRecipeMap(RecipeMap recipeMap) {
        Objects.requireNonNull(recipeMap, "recipe map is null");
        if (recipeMap == RecipeMap.EMPTY) {
            syncedRecipes = SelectableRecipe.SingleInputSet.empty();
        } else {
            Collection<RecipeHolder<HammeringRecipe>> recipeHolders = recipeMap.byType(ModRegistry.HAMMERING_RECIPE_TYPE.value());
            List<SelectableRecipe.SingleInputEntry<HammeringRecipe>> recipes = recipeHolders.stream()
                    .map((RecipeHolder<HammeringRecipe> holder) -> new SelectableRecipe.SingleInputEntry<>(holder.value()
                            .ingredient(), new SelectableRecipe<>(holder.value().resultDisplay(), Optional.of(holder))))
                    .toList();
            syncedRecipes = new SelectableRecipe.SingleInputSet<>(recipes);
        }
    }

    public static SelectableRecipe.SingleInputSet<HammeringRecipe> getRecipes() {
        return syncedRecipes;
    }
}
