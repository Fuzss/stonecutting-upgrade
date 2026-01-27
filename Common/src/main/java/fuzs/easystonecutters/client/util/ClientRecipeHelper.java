package fuzs.easystonecutters.client.util;

import fuzs.easystonecutters.init.ModRegistry;
import fuzs.easystonecutters.world.item.crafting.TransmutationInWorldRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeMap;
import net.minecraft.world.item.crafting.SelectableRecipe;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ClientRecipeHelper {
    private static SelectableRecipe.SingleInputSet<TransmutationInWorldRecipe> syncedRecipes = SelectableRecipe.SingleInputSet.empty();

    public static void setRecipeMap(RecipeMap recipeMap) {
        Objects.requireNonNull(recipeMap, "recipe map is null");
        if (recipeMap == RecipeMap.EMPTY) {
            syncedRecipes = SelectableRecipe.SingleInputSet.empty();
        } else {
            Collection<RecipeHolder<TransmutationInWorldRecipe>> recipeHolders = recipeMap.byType(ModRegistry.TRANSMUTATION_IN_WORLD_RECIPE_TYPE.value());
            List<SelectableRecipe.SingleInputEntry<TransmutationInWorldRecipe>> recipes = recipeHolders.stream()
                    .map((RecipeHolder<TransmutationInWorldRecipe> holder) -> new SelectableRecipe.SingleInputEntry<>(
                            holder.value().input(),
                            new SelectableRecipe<>(holder.value().resultDisplay(), Optional.of(holder))))
                    .toList();
            syncedRecipes = new SelectableRecipe.SingleInputSet<>(recipes);
        }
    }

    public static SelectableRecipe.SingleInputSet<TransmutationInWorldRecipe> getRecipes() {
        return syncedRecipes;
    }
}
