package fuzs.stonecuttingupgrade.fabric.client;

import fuzs.stonecuttingupgrade.StonecuttingUpgrade;
import fuzs.stonecuttingupgrade.client.StonecuttingUpgradeClient;
import fuzs.puzzleslib.common.api.client.core.v1.ClientModConstructor;
import net.fabricmc.api.ClientModInitializer;

public class StonecuttingUpgradeFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientModConstructor.construct(StonecuttingUpgrade.MOD_ID, StonecuttingUpgradeClient::new);
    }
}
