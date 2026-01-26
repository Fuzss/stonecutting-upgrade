package fuzs.easystonecutters.init;

import fuzs.easystonecutters.EasyStonecutters;
import fuzs.easystonecutters.world.item.PocketStonecutterItem;
import fuzs.puzzleslib.api.init.v3.registry.RegistryManager;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Recipe;

public class ModRegistry {
    static final RegistryManager REGISTRIES = RegistryManager.from(EasyStonecutters.MOD_ID);
    public static final Holder.Reference<DataComponentType<ResourceKey<Recipe<?>>>> SELECTED_RECIPE_DATA_COMPONENT_TYPE = REGISTRIES.registerDataComponentType(
            "selected_recipe",
            (DataComponentType.Builder<ResourceKey<Recipe<?>>> builder) -> {
                return builder.persistent(Recipe.KEY_CODEC);
            });
    public static final Holder.Reference<Item> POCKET_STONECUTTER_ITEM = REGISTRIES.registerItem("pocket_stonecutter",
            PocketStonecutterItem::new,
            () -> new Item.Properties().stacksTo(1));

    public static void boostrap() {
        // NO-OP
    }
}
