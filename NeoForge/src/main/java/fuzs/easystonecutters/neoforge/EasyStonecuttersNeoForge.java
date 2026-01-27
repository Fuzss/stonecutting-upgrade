package fuzs.easystonecutters.neoforge;

import fuzs.easystonecutters.EasyStonecutters;
import fuzs.easystonecutters.data.ModRecipeProvider;
import fuzs.easystonecutters.data.tags.ModItemTagsProvider;
import fuzs.easystonecutters.init.ModRegistry;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.puzzleslib.neoforge.api.data.v2.core.DataProviderHelper;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;

@Mod(EasyStonecutters.MOD_ID)
public class EasyStonecuttersNeoForge {

    public EasyStonecuttersNeoForge() {
        ModConstructor.construct(EasyStonecutters.MOD_ID, EasyStonecutters::new);
        registerEventHandlers(NeoForge.EVENT_BUS);
        DataProviderHelper.registerDataProviders(EasyStonecutters.MOD_ID,
                ModRegistry.REGISTRY_SET_BUILDER,
                ModItemTagsProvider::new,
                ModRecipeProvider::new);
    }

    private static void registerEventHandlers(IEventBus eventBus) {
        eventBus.addListener((final OnDatapackSyncEvent event) -> {
            event.sendRecipes(ModRegistry.HAMMERING_RECIPE_TYPE.value());
        });
    }
}
