package fuzs.easystonecutters.world.item;

import fuzs.easystonecutters.EasyStonecutters;
import fuzs.easystonecutters.init.ModRegistry;
import fuzs.easystonecutters.world.entity.attachment.SelectedHammeringBlocks;
import fuzs.easystonecutters.world.item.component.SelectionMode;
import fuzs.puzzleslib.api.item.v2.EnchantingHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Vector3i;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

public class HammerItem extends Item {

    public HammerItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand interactionHand) {
        // TODO open the screen for configuration
        ItemStack itemInHand = player.getItemInHand(interactionHand);
        InteractionResult interactionResult = this.cycleSelectionMode(player,
                itemInHand,
                player.isSecondaryUseActive());
        return interactionResult.consumesAction() ? interactionResult : super.use(level, player, interactionHand);
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

    @Override
    public InteractionResult useOn(UseOnContext context) {
        BlockPos clickedPosition = context.getClickedPos();
        BlockState blockState = context.getLevel().getBlockState(clickedPosition);
        if (context.isSecondaryUseActive()) {
            ModRegistry.SELECTED_BLOCK_ATTACHMENT_TYPE.set(context.getPlayer(), blockState.getBlockHolder());
            return InteractionResult.SUCCESS;
        } else {
            Player player = context.getPlayer();
            if (player != null) {
                if (context.getLevel() instanceof ServerLevel serverLevel) {
                    SelectedHammeringBlocks selectedHammeringBlocks = ModRegistry.SELECTED_HAMMERING_BLOCKS_ATTACHMENT_TYPE.getOrDefault(
                            player,
                            SelectedHammeringBlocks.EMPTY);
                    Holder<Block> holder = ModRegistry.SELECTED_BLOCK_ATTACHMENT_TYPE.getOrDefault(player,
                            Blocks.AIR.builtInRegistryHolder());
                    if (Objects.equals(clickedPosition, selectedHammeringBlocks.clickedPosition()) && !holder.value()
                            .defaultBlockState()
                            .isAir()) {
                        int interactionRange = getInteractionRange(context.getItemInHand(), player);
                        boolean hasReplacedBlock = false;
                        for (BlockPos blockPos : selectedHammeringBlocks.blockPositions()) {
                            BlockPos clickedPositionDistance = blockPos.subtract(clickedPosition);
                            int max = new Vector3i(clickedPositionDistance.getX(),
                                    clickedPositionDistance.getY(),
                                    clickedPositionDistance.getZ()).absolute().maxComponent();
                            if (max <= interactionRange) {
                                BlockState otherBlockState = context.getLevel().getBlockState(blockPos);
                                if (otherBlockState.is(blockState.getBlock())) {
                                    hasReplacedBlock = true;
                                    serverLevel.setBlockAndUpdate(blockPos,
                                            holder.value().withPropertiesOf(otherBlockState));
                                    serverLevel.levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK,
                                            blockPos,
                                            Block.getId(blockState));
                                }
                            }
                        }

                        if (hasReplacedBlock) {
                            context.getItemInHand().hurtAndBreak(1, player, context.getHand());
                        }
                    }
                }

                return InteractionResult.SUCCESS;
            }

            return super.useOn(context);
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
        return createTranslationKey("tooltip.current_selection");
    }

    public static String getChangedSelectionTranslationKey() {
        return createTranslationKey("tooltip.changed_selection");
    }

    public static String createTranslationKey(String translationKey) {
        return EasyStonecutters.id("hammer").toLanguageKey(Registries.elementsDirPath(Registries.ITEM), translationKey);
    }
}
