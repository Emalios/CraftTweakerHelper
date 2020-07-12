package fr.emalios;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class StringRecipe {

    private final List<StringIngredient> ingredients;
    private StringIngredient output;
    private String recipeName;

    public StringRecipe() {
        this.ingredients = new ArrayList<>();
    }

    public void addIngredients(Ingredient ingredient) {
        this.ingredients.add(new StringIngredient(ingredient));
    }

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
        String beforeLine = "//"+this.recipeName;
        String initLine = "craftingTable.addShaped(\"" + this.recipeName + "\", " + this.output + ", [";
        String firstLine = this.getFirstLine();
        String secondLine = this.getSecondLine();
        String thirdLine = this.getThirdLine();
        return String.format("%s\n%s\n%s\n%s\n%s", beforeLine, initLine, firstLine, secondLine, thirdLine);
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
