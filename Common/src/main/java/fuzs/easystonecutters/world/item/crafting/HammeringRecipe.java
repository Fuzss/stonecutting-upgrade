package fuzs.easystonecutters.world.item.crafting;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fuzs.easystonecutters.init.ModRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public record HammeringRecipe(HolderSet<Block> blocks) implements Recipe<HammeringRecipeInput> {

    public Ingredient ingredient() {
        return Ingredient.of(this.input.value());
    }

    @Override
    public boolean matches(HammeringRecipeInput input, Level level) {
        return input.input().is(this.input);
    }

    public boolean matches(BlockState input, Level level) {
        return this.matches(new HammeringRecipeInput(input), level);
    }

    @Override
    public ItemStack assemble(HammeringRecipeInput input, HolderLookup.Provider registries) {
        return ItemStack.EMPTY;
    }

    public BlockState assemble(BlockState input, int recipeIndex) {
        return recipeIndex < this.blocks.size() ? this.blocks.get(recipeIndex).value().withPropertiesOf(input) :
                Blocks.AIR.defaultBlockState();
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
                        RegistryCodecs.homogeneousList(Registries.BLOCK).fieldOf("blocks").forGetter(HammeringRecipe::blocks))
                .apply(instance, HammeringRecipe::new));
        private static final StreamCodec<RegistryFriendlyByteBuf, HammeringRecipe> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.holderSet(Registries.BLOCK),
                HammeringRecipe::blocks,
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
