package fr.emalios.recipe.shapedrecipe;

import fr.emalios.recipe.Recipe;

public class ShapedRecipe extends Recipe {

    public ShapedRecipe() {
        super();
    }

    @Override
    public String toString() {
        String beforeLine = "//"+this.recipeName;
        String initLine = "craftingTable.addShaped(\"" + this.recipeName + "\", " + this.output + ", [";
        StringBuilder builder = new StringBuilder(beforeLine).append("\n").append(initLine).append("\n");
        for (int i = 0; i < super.recipeLines.size(); i++) {
            if(i == super.recipeLines.size()-1) {
                builder.append(super.recipeLines.get(i)).append("]\n");
                continue;
            }
            builder.append(super.recipeLines.get(i)).append(",\n");
        }
        return builder.toString();
    }

}
