package fuzs.easystonecutters.util;

import fuzs.easystonecutters.world.item.PocketStonecutterItem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MiniumStoneHelper {
    private static final InteractionHand[] HAND_VALUES = InteractionHand.values();

    @Nullable
    public static InteractionHand getHandHoldingItem(Player player, Item item) {
        for (InteractionHand interactionHand : HAND_VALUES) {
            ItemStack itemInHand = player.getItemInHand(interactionHand);
            if (itemInHand.is(item)) {
                return interactionHand;
            }
        }

        return null;
    }

    public static void transmuteBlocks(BlockPos blockPosition, List<BlockPos> blocks, Level level, Block ingredient, Block result, ItemStack itemInHand) {
        int miniumStoneCharge = PocketStonecutterItem.getCharge(itemInHand) * 3;
        for (BlockPos blockPos : blocks) {
            if (blockPosition.distManhattan(blockPos) <= miniumStoneCharge) {
                BlockState blockState = level.getBlockState(blockPos);
                if (blockState.is(ingredient)) {
                    level.setBlockAndUpdate(blockPos, result.withPropertiesOf(blockState));
                    level.levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, blockPos, Block.getId(blockState));
                }
            }
        }
    }
}
