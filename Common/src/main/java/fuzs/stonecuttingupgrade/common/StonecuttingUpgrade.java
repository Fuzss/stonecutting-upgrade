package fuzs.stonecuttingupgrade.common;

import fuzs.stonecuttingupgrade.common.config.ClientConfig;
import fuzs.puzzleslib.api.config.v3.ConfigHolder;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StonecuttingUpgrade implements ModConstructor {
    public static final String MOD_ID = "stonecuttingupgrade";
    public static final String MOD_NAME = "Stonecutting Upgrade";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public static final ConfigHolder CONFIG = ConfigHolder.builder(MOD_ID).client(ClientConfig.class);

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
