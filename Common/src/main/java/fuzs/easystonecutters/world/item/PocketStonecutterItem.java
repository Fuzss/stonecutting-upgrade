package fuzs.easystonecutters.world.item;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
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

    public PocketStonecutterItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand interactionHand) {
        if (level instanceof ServerLevel serverLevel) {
            ItemStack itemInHand = player.getItemInHand(interactionHand);
            player.openMenu(new SimpleMenuProvider((int containerId, Inventory inventory, Player playerX) -> {
                return new StonecutterMenu(containerId,
                        inventory,
                        ContainerLevelAccess.create(serverLevel, playerX.blockPosition())) {
                    @Override
                    public boolean stillValid(Player player) {
                        return player.isAlive() && player.getItemInHand(interactionHand).is(PocketStonecutterItem.this);
                    }
                };
            }, this.getName(itemInHand)));
            player.awardStat(Stats.INTERACT_WITH_STONECUTTER);
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        return super.useOn(context);
    }
}
