package fuzs.stonecuttingupgrade.common.config;

import fuzs.puzzleslib.common.api.config.v3.Config;
import fuzs.puzzleslib.common.api.config.v3.ConfigCore;

public class ClientConfig implements ConfigCore {
    @Config(description = "Re-select the last used recipe in a stonecutter when putting in more of the previous material.")
    public boolean rememberLastRecipe = true;
    @Config(description = "Press the space bar to refill the stonecutter input slot with items from your inventory.")
    public boolean quickMoveLastRecipeInput = true;
    @Config(description = "Refill the stonecutter input slot with full stacks, otherwise only single items are moved each time.")
    public MoveAllItems quickMoveAllItems = MoveAllItems.HOLDING_SHIFT;
}
