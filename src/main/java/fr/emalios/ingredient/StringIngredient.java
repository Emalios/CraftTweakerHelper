package fr.emalios.ingredient;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

public class StringIngredient {

    private final String string;

    public StringIngredient(ItemStack itemStack) {
        this.string = "<item:" + itemStack.getItem().getRegistryName() + ">";
    }

    public StringIngredient(Ingredient ingredient) {
        this.string = ingredient.serialize().toString().replace("{", "<").replace("}", ">").replace("\"", "");
    }
    public StringIngredient(String ingredient) {
        this.string = ingredient;
    }

    @Override
    public String toString() {
        return this.string;
    }
}
