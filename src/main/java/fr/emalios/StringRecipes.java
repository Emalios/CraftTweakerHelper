package fr.emalios;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StringRecipes {
    
    private final Set<StringRecipe> recipes;
    
    public StringRecipes() {
        this.recipes = new HashSet<>();
    }

    public void addRecipe(StringRecipe recipe) {
        this.recipes.add(recipe);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (StringRecipe recipe : recipes) {
            stringBuilder.append(recipe).append("\n");
        }
        return stringBuilder.toString();
    }
}
