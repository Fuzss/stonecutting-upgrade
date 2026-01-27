package fuzs.easystonecutters.init;

import fuzs.easystonecutters.EasyStonecutters;
import fuzs.easystonecutters.world.item.HammerItem;
import fuzs.easystonecutters.world.item.component.SelectionMode;
import fuzs.easystonecutters.world.item.crafting.HammeringRecipe;
import fuzs.puzzleslib.api.data.v2.AbstractDatapackRegistriesProvider;
import fuzs.puzzleslib.api.init.v3.registry.RegistryManager;
import fuzs.puzzleslib.api.init.v3.tags.TagFactory;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.AddValue;
import net.minecraft.world.item.enchantment.effects.EnchantmentValueEffect;

public class ModRegistry {
    public static final RegistrySetBuilder REGISTRY_SET_BUILDER = new RegistrySetBuilder().add(Registries.ENCHANTMENT,
            ModRegistry::bootstrapEnchantments);
    static final RegistryManager REGISTRIES = RegistryManager.from(EasyStonecutters.MOD_ID);
    public static final Holder.Reference<DataComponentType<EnchantmentValueEffect>> HAMMER_INTERACTION_RANGE_ENCHANTMENT_EFFECT_COMPONENT_TYPE = REGISTRIES.registerEnchantmentEffectComponentType(
            "hammer_interaction_range",
            (DataComponentType.Builder<EnchantmentValueEffect> builder) -> {
                return builder.persistent(EnchantmentValueEffect.CODEC);
            });
    public static final Holder.Reference<DataComponentType<Unit>> CONTROLS_SELECTION_MODE_ENCHANTMENT_EFFECT_COMPONENT_TYPE = REGISTRIES.registerEnchantmentEffectComponentType(
            "controls_selection_mode",
            (DataComponentType.Builder<Unit> builder) -> {
                return builder.persistent(Unit.CODEC);
            });
    public static final Holder.Reference<DataComponentType<SelectionMode>> SELECTION_DATA_COMPONENT_TYPE = REGISTRIES.registerDataComponentType(
            "selection",
            (DataComponentType.Builder<SelectionMode> builder) -> {
                return builder.persistent(SelectionMode.CODEC).networkSynchronized(SelectionMode.STREAM_CODEC);
            });
    public static final Holder.Reference<Item> MASONRY_HAMMER_ITEM = REGISTRIES.registerItem("masonry_hammer",
            HammerItem::new,
            () -> {
                return new Item.Properties().durability(288)
                        .component(SELECTION_DATA_COMPONENT_TYPE.value(), SelectionMode.DEFAULT_SELECTION_MODE);
            });
    public static final Holder.Reference<RecipeType<HammeringRecipe>> HAMMERING_RECIPE_TYPE = REGISTRIES.registerRecipeType(
            "hammering");
    public static final Holder.Reference<RecipeSerializer<HammeringRecipe>> HAMMERING_RECIPE_SERIALIZER = REGISTRIES.register(
            Registries.RECIPE_SERIALIZER,
            "hammering",
            HammeringRecipe.Serializer::new);
    public static final Holder.Reference<RecipeBookCategory> HAMMERING_RECIPE_BOOK_CATEGORY = REGISTRIES.register(
            Registries.RECIPE_BOOK_CATEGORY,
            "hammering",
            RecipeBookCategory::new);
    public static final ResourceKey<Enchantment> MASONRY_REACH_ENCHANTMENT = REGISTRIES.registerEnchantment(
            "masonry_reach");

    static final TagFactory TAGS = TagFactory.make(EasyStonecutters.MOD_ID);
    public static final TagKey<Item> HAMMER_ENCHANTABLE_ITEM_TAG = TAGS.registerItemTag("enchantable/hammer");

    public static void boostrap() {
        // NO-OP
    }

    public static void bootstrapEnchantments(BootstrapContext<Enchantment> context) {
        AbstractDatapackRegistriesProvider.registerEnchantment(context,
                MASONRY_REACH_ENCHANTMENT,
                Enchantment.enchantment(Enchantment.definition(context.lookup(Registries.ITEM)
                                        .getOrThrow(HAMMER_ENCHANTABLE_ITEM_TAG),
                                2,
                                3,
                                Enchantment.dynamicCost(5, 9),
                                Enchantment.dynamicCost(20, 9),
                                4,
                                EquipmentSlotGroup.MAINHAND))
                        .withSpecialEffect(HAMMER_INTERACTION_RANGE_ENCHANTMENT_EFFECT_COMPONENT_TYPE.value(),
                                new AddValue(LevelBasedValue.perLevel(1.0F)))
                        .withEffect(CONTROLS_SELECTION_MODE_ENCHANTMENT_EFFECT_COMPONENT_TYPE.value()));
    }
}
