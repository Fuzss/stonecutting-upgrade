package fuzs.easystonecutters.neoforge;

import fuzs.easystonecutters.EasyStonecutters;
import fuzs.easystonecutters.data.ModRecipeProvider;
import fuzs.easystonecutters.data.tags.ModItemTagsProvider;
import fuzs.easystonecutters.init.ModRegistry;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.puzzleslib.neoforge.api.data.v2.core.DataProviderHelper;
import net.neoforged.fml.common.Mod;

@Mod(EasyStonecutters.MOD_ID)
public class EasyStonecuttersNeoForge {

    public EasyStonecuttersNeoForge() {
        ModConstructor.construct(EasyStonecutters.MOD_ID, EasyStonecutters::new);
        DataProviderHelper.registerDataProviders(EasyStonecutters.MOD_ID,
                ModRegistry.REGISTRY_SET_BUILDER,
                ModItemTagsProvider::new,
                ModRecipeProvider::new);
    }
}
