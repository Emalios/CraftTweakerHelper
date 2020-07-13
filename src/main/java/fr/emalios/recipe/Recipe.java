package fr.emalios.recipe;

import fr.emalios.ingredient.StringIngredient;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Recipe {

    protected final List<RecipeLine> recipeLines;
    protected StringIngredient output;
    protected String recipeName;

    public Recipe() {
        this.recipeLines = new ArrayList<>();
    }

    public void addRecipeLine(RecipeLine recipeLine) {
        this.recipeLines.add(recipeLine);
    }

    public void setOutput(ItemStack output) {
        if(output.getItem().getRegistryName() == null){
            this.recipeName = "null";
            return;
        }
        this.recipeName = output.getItem().getRegistryName().getPath();
        this.output = new StringIngredient(output);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recipe recipe = (Recipe) o;
        return Objects.equals(recipeName, recipe.recipeName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recipeName);
    }
}
