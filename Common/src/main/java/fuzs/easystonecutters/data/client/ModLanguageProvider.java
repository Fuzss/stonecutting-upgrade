package fuzs.easystonecutters.data.client;

import fuzs.easystonecutters.EasyStonecutters;
import fuzs.easystonecutters.init.ModRegistry;
import fuzs.easystonecutters.world.item.HammerItem;
import fuzs.easystonecutters.world.item.component.SelectionMode;
import fuzs.puzzleslib.api.client.data.v2.AbstractLanguageProvider;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;

public class ModLanguageProvider extends AbstractLanguageProvider {

    public ModLanguageProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addTranslations(TranslationBuilder translationBuilder) {
        translationBuilder.add(ModRegistry.CREATIVE_MODE_TAB.value(), EasyStonecutters.MOD_NAME);
        translationBuilder.add(ModRegistry.WOODEN_HAMMER_ITEM.value(), "Wooden Hammer");
        translationBuilder.add(ModRegistry.COPPER_HAMMER_ITEM.value(), "Copper Hammer");
        translationBuilder.add(ModRegistry.STONE_HAMMER_ITEM.value(), "Stone Hammer");
        translationBuilder.add(ModRegistry.GOLDEN_HAMMER_ITEM.value(), "Golden Hammer");
        translationBuilder.add(ModRegistry.IRON_HAMMER_ITEM.value(), "Iron Hammer");
        translationBuilder.add(ModRegistry.DIAMOND_HAMMER_ITEM.value(), "Diamond Hammer");
        translationBuilder.add(ModRegistry.NETHERITE_HAMMER_ITEM.value(), "Netherite Hammer");
        translationBuilder.add(ModRegistry.MASONRY_REACH_ENCHANTMENT, "Masonry Reach");
        translationBuilder.add(HammerItem.getCurrentSelectionTranslationKey(), "Selection Mode: %s");
        translationBuilder.add(HammerItem.getChangedSelectionTranslationKey(), "Changed Selection Mode to %s");
        translationBuilder.add(SelectionMode.CUBE.getComponent(), "Cube");
        translationBuilder.add(SelectionMode.FLAT.getComponent(), "Flat");
        translationBuilder.add(SelectionMode.LINE.getComponent(), "Line");
    }
}
