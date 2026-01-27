package fuzs.easystonecutters.init;

import com.mojang.serialization.Codec;
import fuzs.easystonecutters.EasyStonecutters;
import fuzs.easystonecutters.world.item.MasonryHammerItem;
import fuzs.easystonecutters.world.item.component.SelectionMode;
import fuzs.easystonecutters.world.item.crafting.TransmutationInWorldRecipe;
import fuzs.easystonecutters.world.item.crafting.display.BlockSlotDisplay;
import fuzs.puzzleslib.api.init.v3.registry.RegistryManager;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.display.SlotDisplay;

public class ModRegistry {
    static final RegistryManager REGISTRIES = RegistryManager.from(EasyStonecutters.MOD_ID);
    public static final Holder.Reference<DataComponentType<ResourceKey<Recipe<?>>>> SELECTED_RECIPE_DATA_COMPONENT_TYPE = REGISTRIES.registerDataComponentType(
            "selected_recipe",
            (DataComponentType.Builder<ResourceKey<Recipe<?>>> builder) -> {
                return builder.persistent(Recipe.KEY_CODEC);
            });
    public static final Holder.Reference<DataComponentType<Byte>> CHARGE_DATA_COMPONENT_TYPE = REGISTRIES.registerDataComponentType(
            "charge",
            (DataComponentType.Builder<Byte> builder) -> {
                return builder.persistent(Codec.BYTE).networkSynchronized(ByteBufCodecs.BYTE);
            });
    public static final Holder.Reference<DataComponentType<SelectionMode>> SELECTION_DATA_COMPONENT_TYPE = REGISTRIES.registerDataComponentType(
            "selection",
            (DataComponentType.Builder<SelectionMode> builder) -> {
                return builder.persistent(SelectionMode.CODEC).networkSynchronized(SelectionMode.STREAM_CODEC);
            });
    public static final Holder.Reference<Item> MASONRY_HAMMER_ITEM = REGISTRIES.registerItem("masonry_hammer",
            MasonryHammerItem::new,
            () -> {
                return new Item.Properties().durability(768)
                        .component(CHARGE_DATA_COMPONENT_TYPE.value(), (byte) 2)
                        .component(SELECTION_DATA_COMPONENT_TYPE.value(), SelectionMode.FLAT);
            });
    public static final Holder.Reference<SlotDisplay.Type<BlockSlotDisplay>> BLOCK_SLOT_DISPLAY = REGISTRIES.register(
            Registries.SLOT_DISPLAY,
            "block",
            () -> {
                return new SlotDisplay.Type<>(BlockSlotDisplay.MAP_CODEC, BlockSlotDisplay.STREAM_CODEC);
            });
    public static final Holder.Reference<RecipeType<TransmutationInWorldRecipe>> TRANSMUTATION_IN_WORLD_RECIPE_TYPE = REGISTRIES.registerRecipeType(
            "transmutation_in_world");
    public static final Holder.Reference<RecipeSerializer<TransmutationInWorldRecipe>> TRANSMUTATION_IN_WORLD_RECIPE_SERIALIZER = REGISTRIES.register(
            Registries.RECIPE_SERIALIZER,
            "transmutation_in_world",
            TransmutationInWorldRecipe.Serializer::new);
    public static final Holder.Reference<RecipeBookCategory> TRANSMUTATION_IN_WORLD_RECIPE_BOOK_CATEGORY = REGISTRIES.register(
            Registries.RECIPE_BOOK_CATEGORY,
            "transmutation_in_world",
            RecipeBookCategory::new);

    public static void boostrap() {
        // NO-OP
    }
}
