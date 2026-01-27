package fuzs.easystonecutters.fabric;

import fuzs.easystonecutters.EasyStonecutters;
import fuzs.easystonecutters.init.ModRegistry;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.recipe.v1.sync.RecipeSynchronization;

public class EasyStonecuttersFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        ModConstructor.construct(EasyStonecutters.MOD_ID, EasyStonecutters::new);
        RecipeSynchronization.synchronizeRecipeSerializer(ModRegistry.HAMMERING_RECIPE_SERIALIZER.value());
    }
}
