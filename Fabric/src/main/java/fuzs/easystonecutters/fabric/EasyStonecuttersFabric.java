package fuzs.easystonecutters.fabric;

import fuzs.easystonecutters.EasyStonecutters;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import net.fabricmc.api.ModInitializer;

public class EasyStonecuttersFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        ModConstructor.construct(EasyStonecutters.MOD_ID, EasyStonecutters::new);
    }
}
