package fuzs.easystonecutters.world.item;

import fuzs.easystonecutters.init.ModRegistry;
import fuzs.easystonecutters.world.item.component.SelectionMode;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.StonecutterMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class PocketStonecutterItem extends Item {
    private static final Component CONTAINER_TITLE = Component.translatable("container.stonecutter");

    public PocketStonecutterItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand interactionHand) {
        if (level instanceof ServerLevel serverLevel) {
            player.openMenu(new SimpleMenuProvider((int containerId, Inventory inventory, Player playerX) -> {
                return new StonecutterMenu(containerId,
                        inventory,
                        ContainerLevelAccess.create(serverLevel, playerX.blockPosition())) {
                    @Override
                    public boolean stillValid(Player player) {
                        return player.isAlive() && player.getItemInHand(interactionHand).is(PocketStonecutterItem.this);
                    }
                };
            }, CONTAINER_TITLE));
            player.awardStat(Stats.INTERACT_WITH_STONECUTTER);
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        return super.useOn(context);
    }

    public static void cycleSelectionMode(ItemStack itemStack) {
        itemStack.set(ModRegistry.SELECTION_DATA_COMPONENT_TYPE.value(), getSelectionMode(itemStack).cycle());
    }

    public static SelectionMode getSelectionMode(ItemStack itemStack) {
        return itemStack.get(ModRegistry.SELECTION_DATA_COMPONENT_TYPE.value());
    }

    public static boolean increaseCharge(ItemStack itemStack) {
        return setCharge(itemStack, getCharge(itemStack) + 1);
    }

    public static boolean decreaseCharge(ItemStack itemStack) {
        return setCharge(itemStack, getCharge(itemStack) - 1);
    }

    private static boolean setCharge(ItemStack itemStack, int charge) {
        byte newCharge = (byte) Mth.clamp(charge, 0, 3);
        byte oldCharge = itemStack.getOrDefault(ModRegistry.CHARGE_DATA_COMPONENT_TYPE.value(), (byte) 0);
        if (newCharge != oldCharge) {
            itemStack.set(ModRegistry.CHARGE_DATA_COMPONENT_TYPE.value(), newCharge);
            return true;
        } else {
            return false;
        }
    }

    public static int getCharge(ItemStack itemStack) {
        return itemStack.getOrDefault(ModRegistry.CHARGE_DATA_COMPONENT_TYPE.value(), (byte) 0);
    }
}
