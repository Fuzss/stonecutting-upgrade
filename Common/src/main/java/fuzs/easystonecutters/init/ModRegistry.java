package fuzs.easystonecutters.init;

import fuzs.easystonecutters.EasyStonecutters;
import fuzs.easystonecutters.world.entity.attachment.SelectedHammeringBlocks;
import fuzs.easystonecutters.world.item.HammerItem;
import fuzs.easystonecutters.world.item.component.SelectionMode;
import fuzs.puzzleslib.api.attachment.v4.DataAttachmentRegistry;
import fuzs.puzzleslib.api.attachment.v4.DataAttachmentType;
import fuzs.puzzleslib.api.data.v2.AbstractDatapackRegistriesProvider;
import fuzs.puzzleslib.api.init.v3.registry.RegistryManager;
import fuzs.puzzleslib.api.init.v3.tags.TagFactory;
import fuzs.puzzleslib.api.network.v4.PlayerSet;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.item.component.Weapon;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.AddValue;
import net.minecraft.world.item.enchantment.effects.EnchantmentValueEffect;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.Collections;

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
    public static final Holder.Reference<Item> WOODEN_HAMMER_ITEM = REGISTRIES.registerItem("wooden_hammer",
            HammerItem::new,
            () -> {
                return hammerItemProperties(ToolMaterial.WOOD, 1.0F, -2.8F);
            });
    public static final Holder.Reference<Item> COPPER_HAMMER_ITEM = REGISTRIES.registerItem("copper_hammer",
            HammerItem::new,
            () -> {
                return hammerItemProperties(ToolMaterial.COPPER, 1.0F, -2.8F);
            });
    public static final Holder.Reference<Item> STONE_HAMMER_ITEM = REGISTRIES.registerItem("stone_hammer",
            HammerItem::new,
            () -> {
                return hammerItemProperties(ToolMaterial.STONE, 1.0F, -2.8F);
            });
    public static final Holder.Reference<Item> GOLDEN_HAMMER_ITEM = REGISTRIES.registerItem("golden_hammer",
            HammerItem::new,
            () -> {
                return hammerItemProperties(ToolMaterial.GOLD, 1.0F, -2.8F);
            });
    public static final Holder.Reference<Item> IRON_HAMMER_ITEM = REGISTRIES.registerItem("iron_hammer",
            HammerItem::new,
            () -> {
                return hammerItemProperties(ToolMaterial.IRON, 1.0F, -2.8F);
            });
    public static final Holder.Reference<Item> DIAMOND_HAMMER_ITEM = REGISTRIES.registerItem("diamond_hammer",
            HammerItem::new,
            () -> {
                return hammerItemProperties(ToolMaterial.DIAMOND, 1.0F, -2.8F);
            });
    public static final Holder.Reference<Item> NETHERITE_HAMMER_ITEM = REGISTRIES.registerItem("netherite_hammer",
            HammerItem::new,
            () -> {
                return hammerItemProperties(ToolMaterial.NETHERITE, 1.0F, -2.8F).fireResistant();
            });
    public static final Holder.Reference<CreativeModeTab> CREATIVE_MODE_TAB = REGISTRIES.registerCreativeModeTab(
            GOLDEN_HAMMER_ITEM);
    public static final ResourceKey<Enchantment> MASONRY_REACH_ENCHANTMENT = REGISTRIES.registerEnchantment(
            "masonry_reach");

    public static final DataAttachmentType<Entity, Holder<Block>> SELECTED_BLOCK_ATTACHMENT_TYPE = DataAttachmentRegistry.<Holder<Block>>entityBuilder()
            .defaultValue(EntityType.PLAYER, Blocks.AIR.builtInRegistryHolder())
            .copyOnDeath()
            .persistent(BuiltInRegistries.BLOCK.holderByNameCodec())
            .networkSynchronized(ByteBufCodecs.holderRegistry(Registries.BLOCK), PlayerSet::ofEntity)
            .build(EasyStonecutters.id("selected_block"));
    public static final DataAttachmentType<Entity, SelectedHammeringBlocks> SELECTED_HAMMERING_BLOCKS_ATTACHMENT_TYPE = DataAttachmentRegistry.<SelectedHammeringBlocks>entityBuilder()
            .defaultValue(EntityType.PLAYER, SelectedHammeringBlocks.EMPTY)
            .build(EasyStonecutters.id("selected_hammering_blocks"));

    static final TagFactory TAGS = TagFactory.make(EasyStonecutters.MOD_ID);
    public static final TagKey<Item> HAMMERS_ITEM_TAG = TAGS.registerItemTag("hammers");
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

    private static Item.Properties hammerItemProperties(ToolMaterial toolMaterial, float attackDamage, float attackSpeed) {
        HolderGetter<Block> holderGetter = BuiltInRegistries.acquireBootstrapRegistrationLookup(BuiltInRegistries.BLOCK);
        return toolMaterial.applyCommonProperties(new Item.Properties())
                .component(DataComponents.TOOL, new Tool(Collections.emptyList(), 1.0F, 1, true))
                .attributes(toolMaterial.createToolAttributes(attackDamage, attackSpeed))
                .component(DataComponents.WEAPON, new Weapon(1, 0.0F))
                .component(SELECTION_DATA_COMPONENT_TYPE.value(), SelectionMode.DEFAULT_SELECTION_MODE);
    }
}
