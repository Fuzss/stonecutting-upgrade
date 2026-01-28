package fuzs.easystonecutters.data.tags;

import fuzs.easystonecutters.init.ModRegistry;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import fuzs.puzzleslib.api.data.v2.tags.AbstractTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;

public class ModItemTagsProvider extends AbstractTagProvider<Item> {

    public ModItemTagsProvider(DataProviderContext context) {
        super(Registries.ITEM, context);
    }

    @Override
    public void addTags(HolderLookup.Provider registries) {
        this.tag(ModRegistry.HAMMERS_ITEM_TAG)
                .add(ModRegistry.WOODEN_HAMMER_ITEM.value(),
                        ModRegistry.COPPER_HAMMER_ITEM.value(),
                        ModRegistry.STONE_HAMMER_ITEM.value(),
                        ModRegistry.GOLDEN_HAMMER_ITEM.value(),
                        ModRegistry.IRON_HAMMER_ITEM.value(),
                        ModRegistry.DIAMOND_HAMMER_ITEM.value(),
                        ModRegistry.NETHERITE_HAMMER_ITEM.value());
        this.tag(ModRegistry.HAMMER_ENCHANTABLE_ITEM_TAG).addTag(ModRegistry.HAMMERS_ITEM_TAG);
        this.tag(ItemTags.DURABILITY_ENCHANTABLE).addTag(ModRegistry.HAMMERS_ITEM_TAG);
    }
}
