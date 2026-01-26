package fuzs.easystonecutters;

import fuzs.easystonecutters.config.ClientConfig;
import fuzs.easystonecutters.config.ServerConfig;
import fuzs.easystonecutters.init.ModRegistry;
import fuzs.easystonecutters.network.ClientboundTransmutationParticleMessage;
import fuzs.easystonecutters.network.client.ServerboundStoneTransmutationMessage;
import fuzs.puzzleslib.api.config.v3.ConfigHolder;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.puzzleslib.api.core.v1.context.PayloadTypesContext;
import fuzs.puzzleslib.api.event.v1.BuildCreativeModeTabContentsCallback;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EasyStonecutters implements ModConstructor {
    public static final String MOD_ID = "easystonecutters";
    public static final String MOD_NAME = "Easy Stonecutters";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public static final ConfigHolder CONFIG = ConfigHolder.builder(MOD_ID)
            .client(ClientConfig.class)
            .server(ServerConfig.class);

    @Override
    public void onConstructMod() {
        ModRegistry.boostrap();
        registerEventHandlers();
    }

    private static void registerEventHandlers() {
        BuildCreativeModeTabContentsCallback.buildCreativeModeTabContents(CreativeModeTabs.TOOLS_AND_UTILITIES)
                .register((CreativeModeTab creativeModeTab, CreativeModeTab.ItemDisplayParameters itemDisplayParameters, CreativeModeTab.Output output) -> {
                    output.accept(ModRegistry.POCKET_STONECUTTER_ITEM.value());
                });
    }

    @Override
    public void onRegisterPayloadTypes(PayloadTypesContext context) {
        context.playToServer(ServerboundStoneTransmutationMessage.class,
                ServerboundStoneTransmutationMessage.STREAM_CODEC);
        context.playToClient(ClientboundTransmutationParticleMessage.class,
                ClientboundTransmutationParticleMessage.STREAM_CODEC);
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}
