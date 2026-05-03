package fuzs.stonecuttingupgrade.fabric;

import fuzs.stonecuttingupgrade.common.StonecuttingUpgrade;
import fuzs.puzzleslib.common.api.core.v1.ModConstructor;
import net.fabricmc.api.ModInitializer;

public class StonecuttingUpgradeFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        ModConstructor.construct(StonecuttingUpgrade.MOD_ID, StonecuttingUpgrade::new);
    }
}
