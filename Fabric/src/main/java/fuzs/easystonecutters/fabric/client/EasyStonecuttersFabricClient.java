package fuzs.easystonecutters.fabric.client;

import fuzs.easystonecutters.EasyStonecutters;
import fuzs.easystonecutters.client.EasyStonecuttersClient;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import net.fabricmc.api.ClientModInitializer;

public class EasyStonecuttersFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientModConstructor.construct(EasyStonecutters.MOD_ID, EasyStonecuttersClient::new);
    }
}
