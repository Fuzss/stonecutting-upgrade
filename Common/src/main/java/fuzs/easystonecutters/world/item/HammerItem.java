package fuzs.easystonecutters.world.item;

import fuzs.easystonecutters.init.ModRegistry;
import fuzs.easystonecutters.world.item.component.SelectionMode;
import fuzs.puzzleslib.api.item.v2.EnchantingHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;

public class HammerItem extends Item {

    public HammerItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack itemInHand = player.getItemInHand(interactionHand);
        InteractionResult interactionResult = this.cycleSelectionMode(player,
                itemInHand,
                player.isSecondaryUseActive());
        return interactionResult.consumesAction() ? interactionResult : super.use(level, player, interactionHand);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        InteractionResult interactionResult = this.cycleSelectionMode(context.getPlayer(),
                context.getItemInHand(),
                context.isSecondaryUseActive());
        return interactionResult.consumesAction() ? interactionResult : super.useOn(context);
    }

    private InteractionResult cycleSelectionMode(@Nullable Player player, ItemStack itemStack, boolean isSecondaryUseActive) {
        if (isSecondaryUseActive && EnchantmentHelper.has(itemStack,
                ModRegistry.CONTROLS_SELECTION_MODE_ENCHANTMENT_EFFECT_COMPONENT_TYPE.value())) {
            SelectionMode selectionMode = getSelectionMode(itemStack).cycle();
            itemStack.set(ModRegistry.SELECTION_DATA_COMPONENT_TYPE.value(), selectionMode);
            if (player != null && !player.level().isClientSide()) {
                player.displayClientMessage(Component.translatable(getChangedSelectionTranslationKey(),
                        selectionMode.getComponent()), true);
            }

            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.PASS;
        }
    }

    public static SelectionMode getSelectionMode(ItemStack itemStack) {
        return itemStack.getOrDefault(ModRegistry.SELECTION_DATA_COMPONENT_TYPE.value(),
                SelectionMode.DEFAULT_SELECTION_MODE);
    }

    public static int getInteractionRange(ItemStack itemStack, Entity entity) {
        return Math.round(EnchantingHelper.getUnfilteredValueEffectBonus(itemStack,
                entity,
                ModRegistry.HAMMER_INTERACTION_RANGE_ENCHANTMENT_EFFECT_COMPONENT_TYPE.value()));
    }

    public static String getCurrentSelectionTranslationKey() {
        return ModRegistry.MASONRY_HAMMER_ITEM.value().getDescriptionId() + ".tooltip.current_selection";
    }

    public static String getChangedSelectionTranslationKey() {
        return ModRegistry.MASONRY_HAMMER_ITEM.value().getDescriptionId() + ".tooltip.changed_selection";
    }
}
