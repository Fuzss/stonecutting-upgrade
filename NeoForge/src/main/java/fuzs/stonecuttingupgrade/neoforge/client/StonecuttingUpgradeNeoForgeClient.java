package fuzs.stonecuttingupgrade.neoforge.client;

import fuzs.stonecuttingupgrade.StonecuttingUpgrade;
import fuzs.stonecuttingupgrade.client.StonecuttingUpgradeClient;
import fuzs.puzzleslib.common.api.client.core.v1.ClientModConstructor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;

@Mod(value = StonecuttingUpgrade.MOD_ID, dist = Dist.CLIENT)
public class StonecuttingUpgradeNeoForgeClient {

    public StonecuttingUpgradeNeoForgeClient() {
        ClientModConstructor.construct(StonecuttingUpgrade.MOD_ID, StonecuttingUpgradeClient::new);
    }
}
