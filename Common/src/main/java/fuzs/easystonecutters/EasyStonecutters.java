package fuzs.easystonecutters;

import fuzs.easystonecutters.config.ClientConfig;
import fuzs.easystonecutters.init.ModRegistry;
import fuzs.easystonecutters.network.client.ServerboundSelectHammeringBlocksMessage;
import fuzs.puzzleslib.api.config.v3.ConfigHolder;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.puzzleslib.api.core.v1.context.PayloadTypesContext;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EasyStonecutters implements ModConstructor {
    public static final String MOD_ID = "easystonecutters";
    public static final String MOD_NAME = "Easy Stonecutters";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public static final ConfigHolder CONFIG = ConfigHolder.builder(MOD_ID).client(ClientConfig.class);

    @Override
    public void onConstructMod() {
        ModRegistry.boostrap();
    }

    @Override
    public void onRegisterPayloadTypes(PayloadTypesContext context) {
        context.playToServer(ServerboundSelectHammeringBlocksMessage.class,
                ServerboundSelectHammeringBlocksMessage.STREAM_CODEC);
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}
