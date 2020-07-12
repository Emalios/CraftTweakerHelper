package fr.emalios.recipe.shapedrecipe;

import fr.emalios.recipe.Recipe;

public class ShapedRecipe extends Recipe {

    public ShapedRecipe() {
        super();
    }

    @Override
    public String toString() {
        if(this.ingredients.size() < 9)
            return "null, <9";
        String beforeLine = "//"+this.recipeName;
        String initLine = "craftingTable.addShaped(\"" + this.recipeName + "\", " + this.output + ", [";
        String firstLine = this.getFirstLine();
        String secondLine = this.getSecondLine();
        String thirdLine = this.getThirdLine();
        return String.format("%s\n%s\n%s\n%s\n%s\n", beforeLine, initLine, firstLine, secondLine, thirdLine);
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
