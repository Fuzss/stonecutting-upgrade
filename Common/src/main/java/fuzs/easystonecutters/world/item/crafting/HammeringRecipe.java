package fuzs.easystonecutters.world.item.crafting;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fuzs.easystonecutters.init.ModRegistry;
import fuzs.puzzleslib.api.network.v4.codec.ExtraStreamCodecs;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Function;

public record HammeringRecipe(String group,
                              Either<BlockState, Holder<Block>> input,
                              Either<BlockState, Holder<Block>> result) implements Recipe<HammeringRecipeInput> {

    public HammeringRecipe(String group, Holder<Block> input, Holder<Block> result) {
        this(group, Either.right(input), Either.right(result));
    }

    public Ingredient ingredient() {
        return Ingredient.of(this.input.map(BlockBehaviour.BlockStateBase::getBlock, Holder::value));
    }

    public ItemStack itemResult() {
        return new ItemStack(this.result.map(BlockBehaviour.BlockStateBase::getBlock, Holder::value));
    }

    @Override
    public boolean matches(HammeringRecipeInput input, Level level) {
        return this.input.map((BlockState blockState) -> {
            return blockState == input.input();
        }, (Holder<Block> holder) -> {
            return input.input().is(holder);
        });
    }

    public boolean matches(BlockState input, Level level) {
        return this.matches(new HammeringRecipeInput(input), level);
    }

    @Override
    public ItemStack assemble(HammeringRecipeInput input, HolderLookup.Provider registries) {
        return this.itemResult();
    }

    public BlockState assemble(BlockState input) {
        return this.result.map(Function.identity(), (Holder<Block> holder) -> holder.value().withPropertiesOf(input));
    }

    public SlotDisplay resultDisplay() {
        return new SlotDisplay.ItemStackSlotDisplay(this.itemResult());
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.NOT_PLACEABLE;
    }

    @Override
    public RecipeSerializer<? extends Recipe<HammeringRecipeInput>> getSerializer() {
        return ModRegistry.HAMMERING_RECIPE_SERIALIZER.value();
    }

    @Override
    public RecipeType<? extends Recipe<HammeringRecipeInput>> getType() {
        return ModRegistry.HAMMERING_RECIPE_TYPE.value();
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return ModRegistry.HAMMERING_RECIPE_BOOK_CATEGORY.value();
    }

    /**
     * @see SingleItemRecipe.Serializer
     */
    public static class Serializer implements RecipeSerializer<HammeringRecipe> {
        private static final MapCodec<HammeringRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Codec.STRING.optionalFieldOf("group", "").forGetter(HammeringRecipe::group),
                Codec.either(BlockState.CODEC, BuiltInRegistries.BLOCK.holderByNameCodec())
                        .fieldOf("input")
                        .forGetter(HammeringRecipe::input),
                Codec.either(BlockState.CODEC, BuiltInRegistries.BLOCK.holderByNameCodec())
                        .fieldOf("result")
                        .forGetter(HammeringRecipe::result)).apply(instance, HammeringRecipe::new));
        private static final StreamCodec<RegistryFriendlyByteBuf, HammeringRecipe> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.STRING_UTF8,
                HammeringRecipe::group,
                ByteBufCodecs.either(ExtraStreamCodecs.BLOCK_STATE, ByteBufCodecs.holderRegistry(Registries.BLOCK)),
                HammeringRecipe::input,
                ByteBufCodecs.either(ExtraStreamCodecs.BLOCK_STATE, ByteBufCodecs.holderRegistry(Registries.BLOCK)),
                HammeringRecipe::result,
                HammeringRecipe::new);

        @Override
        public MapCodec<HammeringRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, HammeringRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
