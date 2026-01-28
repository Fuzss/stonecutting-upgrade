package fuzs.easystonecutters.client.handler;

import fuzs.easystonecutters.client.util.HighlightedBlockMemory;
import fuzs.easystonecutters.client.util.HighlightedBlocksHolder;
import fuzs.easystonecutters.init.ModRegistry;
import fuzs.easystonecutters.network.client.ServerboundSelectHammeringBlocksMessage;
import fuzs.easystonecutters.world.entity.attachment.SelectedHammeringBlocks;
import fuzs.puzzleslib.api.event.v1.core.EventResultHolder;
import fuzs.puzzleslib.api.network.v4.MessageSender;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.Connection;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

public class HighlightedBlocksHandler {
    private static final InteractionHand[] HAND_VALUES = InteractionHand.values();

    private static HighlightedBlocksHolder highlightedBlocks = HighlightedBlocksHolder.EMPTY;
    private static SelectedHammeringBlocks lastSentSelectedHammeringBlocks = SelectedHammeringBlocks.EMPTY;

    public static ItemStack getHeldItemStack(Player player, TagKey<Item> tagKey) {
        for (InteractionHand interactionHand : HAND_VALUES) {
            ItemStack itemInHand = player.getItemInHand(interactionHand);
            if (itemInHand.is(tagKey)) {
                return itemInHand;
            }
        }

        return ItemStack.EMPTY;
    }

    public static HighlightedBlocksHolder getHighlightedBlocks() {
        return highlightedBlocks;
    }

    private static void setHighlightedBlocks(HighlightedBlocksHolder highlightedBlocks) {
        HighlightedBlocksHandler.highlightedBlocks = highlightedBlocks;
    }

    private static void resetHighlightedBlocks() {
        setHighlightedBlocks(HighlightedBlocksHolder.EMPTY);
    }

    public static void pickHighlightedBlocks(ClientLevel clientLevel, Camera camera, @Nullable HitResult hitResult) {
        if (camera.entity() instanceof Player player) {
            ItemStack itemStack = getHeldItemStack(player, ModRegistry.HAMMERS_ITEM_TAG);
            if (!itemStack.isEmpty()) {
                HighlightedBlocksHolder highlightedBlocks = getHighlightedBlocks();
                if (highlightedBlocks.isEmpty() || !highlightedBlocks.stillValid(clientLevel,
                        player,
                        itemStack,
                        hitResult)) {
                    setHighlightedBlocks(new HighlightedBlocksHolder(HighlightedBlockMemory.of(clientLevel,
                            player,
                            itemStack,
                            hitResult)));
                }

                return;
            }
        }

        resetHighlightedBlocks();
    }

    public static void onEndClientTick(Minecraft minecraft) {
        if (minecraft.level != null) {
            getHighlightedBlocks().testAllBlocks(minecraft.level, minecraft.hitResult);
        }
    }

    public static EventResultHolder<InteractionResult> onUseBlock(Player player, Level level, InteractionHand interactionHand, BlockHitResult hitResult) {
        if (player.getItemInHand(interactionHand).is(ModRegistry.HAMMERS_ITEM_TAG)) {
            ensureHasSentSelectedHammeringBlocks(level);
        }

        return EventResultHolder.pass();
    }

    private static void ensureHasSentSelectedHammeringBlocks(BlockGetter blockGetter) {
        SelectedHammeringBlocks selectedHammeringBlocks = getHighlightedBlocks().pack(blockGetter);
        if (!Objects.equals(selectedHammeringBlocks, lastSentSelectedHammeringBlocks)) {
            lastSentSelectedHammeringBlocks = selectedHammeringBlocks;
            MessageSender.broadcast(new ServerboundSelectHammeringBlocksMessage(lastSentSelectedHammeringBlocks));
        }
    }

    public static void onPlayerLeave(LocalPlayer player, MultiPlayerGameMode multiPlayerGameMode, Connection connection) {
        lastSentSelectedHammeringBlocks = SelectedHammeringBlocks.EMPTY;
    }
}
