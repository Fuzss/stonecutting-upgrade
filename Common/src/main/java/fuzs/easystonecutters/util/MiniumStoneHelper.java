package fuzs.easystonecutters.util;

import fuzs.easystonecutters.network.ClientboundTransmutationParticleMessage;
import fuzs.easystonecutters.world.item.MasonryHammerItem;
import fuzs.puzzleslib.api.network.v4.MessageSender;
import fuzs.puzzleslib.api.network.v4.PlayerSet;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
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

    public static void transmuteBlocks(BlockPos blockPosition, List<BlockPos> blockPositions, Level level, Block ingredient, Block result, ItemStack itemInHand) {
        int miniumStoneCharge = MasonryHammerItem.getCharge(itemInHand) * 3;
        boolean schedulePlaySound = false;
        for (BlockPos blockPos : blockPositions) {
            if (blockPosition.distManhattan(blockPos) <= miniumStoneCharge) {
                BlockState blockState = level.getBlockState(blockPos);
                if (blockState.is(ingredient)) {
                    level.setBlockAndUpdate(blockPos, result.withPropertiesOf(blockState));
                    if (level instanceof ServerLevel serverLevel) {
                        schedulePlaySound = true;
                        MessageSender.broadcast(PlayerSet.nearPosition(blockPos, serverLevel),
                                new ClientboundTransmutationParticleMessage(blockPos, blockState));
                    }
                }
            }
        }

        if (schedulePlaySound) {
            level.playSound(null,
                    blockPosition,
                    SoundEvents.UI_STONECUTTER_TAKE_RESULT,
                    SoundSource.BLOCKS,
                    1.0F,
                    1.0F);
        }
    }
}
