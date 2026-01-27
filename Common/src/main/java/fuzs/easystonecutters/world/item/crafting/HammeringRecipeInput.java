package fuzs.easystonecutters.world.item.crafting;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.block.state.BlockState;

/**
 * @see net.minecraft.world.item.crafting.SingleRecipeInput
 */
public record HammeringRecipeInput(BlockState input) implements RecipeInput {

    @Override
    public ItemStack getItem(int index) {
        if (index != 0) {
            throw new IllegalArgumentException("No item for index " + index);
        } else {
            return new ItemStack(this.input.getBlock());
        }
    }

    @Override
    public int size() {
        return 1;
    }
}
