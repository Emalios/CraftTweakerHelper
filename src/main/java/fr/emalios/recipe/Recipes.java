package fr.emalios.recipe;

import fr.emalios.recipe.shapedrecipe.ShapedRecipe;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Recipes {

    private final Set<Recipe> recipes;

    public Recipes() {
        this.recipes = new HashSet<>();
    }

    public void addRecipe(Recipe recipe) {
        this.recipes.add(recipe);
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
