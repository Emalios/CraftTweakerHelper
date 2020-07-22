package fr.emalios.cth.recipe;

import java.util.HashSet;
import java.util.Set;

public class PlayerRecipes {

    private Set<ZSRecipe> ZSRecipes;

    public PlayerRecipes() {
        this.ZSRecipes = new HashSet<>();
    }

    public void addRecipe(ZSRecipe ZSRecipe) {
        this.ZSRecipes.add(ZSRecipe);
    }

    public void clearRecipes() {
        this.ZSRecipes = new HashSet<>();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (ZSRecipe ZSRecipe : ZSRecipes) {
            stringBuilder.append(ZSRecipe).append("\n");
        }
        return stringBuilder.toString();
    }
}
