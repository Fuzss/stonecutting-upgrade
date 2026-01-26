package fuzs.easystonecutters.neoforge.client;

import fuzs.easystonecutters.EasyStonecutters;
import fuzs.easystonecutters.client.EasyStonecuttersClient;
import fuzs.easystonecutters.data.client.ModLanguageProvider;
import fuzs.easystonecutters.data.client.ModModelProvider;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.neoforge.api.data.v2.core.DataProviderHelper;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;

@Mod(value = EasyStonecutters.MOD_ID, dist = Dist.CLIENT)
public class EasyStonecuttersNeoForgeClient {

    public EasyStonecuttersNeoForgeClient() {
        ClientModConstructor.construct(EasyStonecutters.MOD_ID, EasyStonecuttersClient::new);
        DataProviderHelper.registerDataProviders(EasyStonecutters.MOD_ID,
                ModLanguageProvider::new,
                ModModelProvider::new);
    }
}
