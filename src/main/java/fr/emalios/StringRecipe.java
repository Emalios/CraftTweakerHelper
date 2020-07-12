package fr.emalios;

import java.util.ArrayList;
import java.util.List;

public class StringRecipe {

    private final List<StringItemStack> ingredients;
    private StringItemStack output;

    public StringRecipe() {
        this.ingredients = new ArrayList<>();
    }

    public void addIngredients(StringItemStack ingredient) {
        this.ingredients.add(ingredient);
    }

    public void setOutput(StringItemStack output) {
        this.output = output;
    }

    @Override
    public String toString() {
        String initLine = "craftingTable.addShaped(" + this.output.getRecipeName() + ", " + this.output + ", [";
        String firstLine = this.getFirstLine();
        String secondLine = this.getSecondLine();
        String thirdLine = this.getThirdLine();
        return String.format("%s\n%s\n%s\n%s", initLine, firstLine, secondLine, thirdLine);
    }

    private String getThirdLine() {
        return String.format("[%s, %s, %s]]);", this.ingredients.get(6), this.ingredients.get(7), this.ingredients.get(8));
    }

    private String getSecondLine() {
        return String.format("[%s, %s, %s],", this.ingredients.get(3), this.ingredients.get(4), this.ingredients.get(5));
    }

    private String getFirstLine() {
        return String.format("[%s, %s, %s],", this.ingredients.get(0), this.ingredients.get(1), this.ingredients.get(2));
    }
}
