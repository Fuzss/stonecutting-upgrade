package fuzs.easystonecutters.world.item.crafting;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fuzs.easystonecutters.init.ModRegistry;
import fuzs.easystonecutters.world.item.crafting.display.BlockSlotDisplay;
import fuzs.puzzleslib.api.init.v3.registry.ResourceKeyHelper;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.item.crafting.display.StonecutterRecipeDisplay;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.List;
import java.util.function.Function;

public final class TransmutationInWorldRecipe extends SingleItemRecipe {
    public static final Component TRANSMUTATION_IN_WORLD_COMPONENT = ResourceKeyHelper.getComponent(ModRegistry.TRANSMUTATION_IN_WORLD_RECIPE_TYPE.key());

    public TransmutationInWorldRecipe(String group, Block ingredient, Block result) {
        this(group, Ingredient.of(ingredient), new ItemStack(result));
    }

    private TransmutationInWorldRecipe(TransmutationInWorldRecipe recipe) {
        this(recipe.group(), recipe.input(), recipe.result());
    }

    public TransmutationInWorldRecipe(String group, Ingredient ingredient, ItemStack result) {
        super(group, ingredient, result);
    }

    @Override
    public RecipeSerializer<? extends SingleItemRecipe> getSerializer() {
        return ModRegistry.TRANSMUTATION_IN_WORLD_RECIPE_SERIALIZER.value();
    }

    @Override
    public RecipeType<? extends SingleItemRecipe> getType() {
        return ModRegistry.TRANSMUTATION_IN_WORLD_RECIPE_TYPE.value();
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return ModRegistry.TRANSMUTATION_IN_WORLD_RECIPE_BOOK_CATEGORY.value();
    }

    @Override
    public boolean matches(SingleRecipeInput container, Level level) {
        return this.input().test(container.getItem(0));
    }

    @Override
    public ItemStack result() {
        return super.result();
    }

    @Override
    public boolean showNotification() {
        return false;
    }

    @Override
    public List<RecipeDisplay> display() {
        return List.of(new StonecutterRecipeDisplay(this.input().display(),
                this.resultDisplay(),
                new SlotDisplay.ItemSlotDisplay(ModRegistry.POCKET_STONECUTTER_ITEM)));
    }

    public SlotDisplay resultDisplay() {
        return new BlockSlotDisplay(this.getBlockResult());
    }

    public Block getBlockIngredient() {
        return ((BlockItem) this.input().items().findFirst().map(Holder::value).orElseThrow()).getBlock();
    }

    public Block getBlockResult() {
        return ((BlockItem) this.result().getItem()).getBlock();
    }

    public static class Serializer extends SingleItemRecipe.Serializer<TransmutationInWorldRecipe> {

        public Serializer() {
            super(TransmutationInWorldRecipe::new);
        }

        @Override
        public MapCodec<TransmutationInWorldRecipe> codec() {
            return RecordCodecBuilder.mapCodec(instance -> {
                return instance.group(super.codec().forGetter(Function.identity()))
                        .apply(instance, TransmutationInWorldRecipe::new);
            });
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, TransmutationInWorldRecipe> streamCodec() {
            return StreamCodec.composite(super.streamCodec(), Function.identity(), TransmutationInWorldRecipe::new);
        }
    }
}
