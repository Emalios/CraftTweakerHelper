package fr.emalios.cth.recipe.shapedrecipe;

import fr.emalios.cth.recipe.ZSRecipe;

public class ShapedZSRecipe extends ZSRecipe {

    public ShapedZSRecipe() {
        super();
    }

    @Override
    protected String getRecipeType() {
        return "Shaped";
    }

    @Override
    protected String getAction() {
        return "add";
    }

    @Override
    protected String getRecipeTable() {
        return "craftingTable";
    }

    @Override
    public String toString() {
        String beforeLine = "//"+this.output.getDisplayName();
        String removeLine = this.getRemoveLine();
        String initLine = this.getRecipeTable() + "." + this.getAction() + this.getRecipeType() + ".addShaped(\"" + this.recipeName + "\", " + this.output + ", [";
        StringBuilder builder = new StringBuilder(beforeLine).append("\n").append(removeLine).append(initLine).append("\n");
        for (int i = 0; i < super.recipeLines.size(); i++) {
            if(i == super.recipeLines.size()-1) {
                builder.append(super.recipeLines.get(i)).append("]);\n");
                continue;
            }
            builder.append(super.recipeLines.get(i)).append(",\n");
        }
        return builder.toString();
    }

    @Override
    protected String getRemoveLine() {
        return this.getRecipeTable() + ".removeByName(\"" + this.output.getRegistryName() + "\")\n";
    }

}
