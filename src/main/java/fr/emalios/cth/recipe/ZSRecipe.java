package fr.emalios.cth.recipe;

import com.blamejared.crafttweaker.impl.item.MCItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class ZSRecipe {

    protected final List<RecipeLine> recipeLines;
    protected MCItemStack output;
    protected String recipeName;

    public ZSRecipe() {
        this.recipeLines = new ArrayList<>();
    }

    public void addRecipeLine(RecipeLine recipeLine) {
        this.recipeLines.add(recipeLine);
    }

    protected abstract String getRecipeType();

    protected abstract String getAction();

    protected abstract String getRecipeTable();

    public void setOutput(MCItemStack output) {
        this.recipeName = output.getDisplayName().replace(" ", "_").toLowerCase();
        this.output = output;
    }

    @Override
    public String toString() {
        return "ZSRecipe";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ZSRecipe ZSRecipe = (ZSRecipe) o;
        return Objects.equals(recipeName, ZSRecipe.recipeName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recipeName);
    }

    protected abstract String getRemoveLine();
}
