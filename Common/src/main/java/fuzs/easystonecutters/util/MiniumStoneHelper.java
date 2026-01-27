package fuzs.easystonecutters.util;

import fuzs.easystonecutters.network.ClientboundDestroyBlockEffectMessage;
import fuzs.easystonecutters.world.item.HammerItem;
import fuzs.easystonecutters.world.item.crafting.HammeringRecipe;
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
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

import java.util.List;

public class MiniumStoneHelper {
    private static final InteractionHand[] HAND_VALUES = InteractionHand.values();

    public static @Nullable InteractionHand getHandHoldingItem(Player player, Item item) {
        for (InteractionHand interactionHand : HAND_VALUES) {
            ItemStack itemInHand = player.getItemInHand(interactionHand);
            if (itemInHand.is(item)) {
                return interactionHand;
            }
        }

        return null;
    }

    public static void transmuteBlocks(Level level, Player player, ItemStack itemInHand, BlockPos clickedPosition, List<BlockPos> blockPositions, HammeringRecipe recipe) {
        int miniumStoneCharge = HammerItem.getInteractionRange(itemInHand, player) * 3;
        boolean hasPlayedSound = false;
        for (BlockPos offsetPosition : blockPositions) {
            if (clickedPosition.distManhattan(offsetPosition) <= miniumStoneCharge) {
                BlockState blockState = level.getBlockState(offsetPosition);
                if (recipe.matches(blockState, level)) {
                    level.setBlockAndUpdate(offsetPosition, recipe.assemble(blockState));
                    if (level instanceof ServerLevel serverLevel) {
                        MessageSender.broadcast(PlayerSet.nearPosition(offsetPosition, serverLevel),
                                new ClientboundDestroyBlockEffectMessage(offsetPosition, blockState));
                        if (!hasPlayedSound) {
                            hasPlayedSound = true;
                            serverLevel.playSound(null,
                                    clickedPosition,
                                    SoundEvents.UI_STONECUTTER_TAKE_RESULT,
                                    SoundSource.BLOCKS,
                                    1.0F,
                                    1.0F);
                        }
                    }
                }
            }
        }
    }
}
