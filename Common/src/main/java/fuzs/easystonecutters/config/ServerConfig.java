package fuzs.easystonecutters.config;

import fuzs.puzzleslib.api.config.v3.Config;
import fuzs.puzzleslib.api.config.v3.ConfigCore;

public class ServerConfig implements ConfigCore {
    @Config(description = "Allow only recipes yielding a full block for in-world stonecutting.")
    public boolean onlySolidBlockRecipes = true;
}
