package fr.emalios.recipe;

import fr.emalios.recipe.shapedrecipe.ShapedRecipe;

import java.util.HashSet;
import java.util.Set;

public class PlayerRecipes {

    private Set<Recipe> recipes;

    public PlayerRecipes() {
        this.recipes = new HashSet<>();
    }

    public void addRecipe(Recipe recipe) {
        this.recipes.add(recipe);
    }

    public void clearRecipes() {
        this.recipes = new HashSet<>();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Recipe recipe : recipes) {
            stringBuilder.append(recipe).append("\n");
        }
        return stringBuilder.toString();
    }
}
