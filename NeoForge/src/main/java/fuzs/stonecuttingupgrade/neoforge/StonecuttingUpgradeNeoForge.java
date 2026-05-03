package fuzs.stonecuttingupgrade.neoforge;

import fuzs.puzzleslib.common.api.core.v1.ModConstructor;
import fuzs.stonecuttingupgrade.StonecuttingUpgrade;
import net.neoforged.fml.common.Mod;

@Mod(StonecuttingUpgrade.MOD_ID)
public class StonecuttingUpgradeNeoForge {

    public StonecuttingUpgradeNeoForge() {
        ModConstructor.construct(StonecuttingUpgrade.MOD_ID, StonecuttingUpgrade::new);
    }
}
