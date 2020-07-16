package fr.emalios.cth.recipe;

import fr.emalios.cth.ingredient.StringIngredient;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class Recipe {

    protected final List<RecipeLine> recipeLines;
    protected StringIngredient output;
    protected String recipeName;

    public Recipe() {
        this.recipeLines = new ArrayList<>();
    }

    public void addRecipeLine(RecipeLine recipeLine) {
        this.recipeLines.add(recipeLine);
    }

    protected abstract String getRecipeType();

    protected abstract String getAction();

    protected abstract String getRecipeTable();

    public void setOutput(ItemStack output) {
        if(output.getItem().getRegistryName() == null){
            this.recipeName = "null";
            return;
        }
        this.recipeName = output.getItem().getRegistryName().getPath();
        this.output = new StringIngredient(output);
    }

    @Override
    public String toString() {
        return "Recipe{}";
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
