package fr.emalios.recipe;

import fr.emalios.ingredient.StringIngredient;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class Recipe {

    protected final List<StringIngredient> ingredients;
    protected StringIngredient output;
    protected String recipeName;

    public Recipe() {
        this.ingredients = new ArrayList<>();
    }

    public void addIngredients(Ingredient ingredient) {
        this.ingredients.add(new StringIngredient(ingredient));
    }

    public void addIngredients(String ingredient) {
        this.ingredients.add(new StringIngredient(ingredient));
    }

    public void setOutput(ItemStack output) {
        if(output.getItem().getRegistryName() == null){
            this.recipeName = "null";
            return;
        }
        this.recipeName = output.getItem().getRegistryName().getPath();
        this.output = new StringIngredient(output);
    }
}
