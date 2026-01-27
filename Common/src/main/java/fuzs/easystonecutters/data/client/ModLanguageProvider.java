package fuzs.easystonecutters.data.client;

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
        translationBuilder.add(ModRegistry.MASONRY_HAMMER_ITEM.value(), "Masonry Hammer");
        translationBuilder.add(ModRegistry.MASONRY_REACH_ENCHANTMENT, "Masonry Reach");
        translationBuilder.add(HammerItem.getCurrentSelectionTranslationKey(), "Selection Mode: %s");
        translationBuilder.add(HammerItem.getChangedSelectionTranslationKey(), "Changed Selection Mode to %s");
        translationBuilder.add(SelectionMode.CUBE.getComponent(), "Cube");
        translationBuilder.add(SelectionMode.FLAT.getComponent(), "Flat");
        translationBuilder.add(SelectionMode.LINE.getComponent(), "Line");
    }
}
