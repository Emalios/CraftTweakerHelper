package fr.emalios.recipe;

import fr.emalios.ingredient.StringIngredient;
import net.minecraft.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class RecipeLine {

    private final List<StringIngredient> ingredients;

    public RecipeLine() {
        this.ingredients = new ArrayList<>();
    }

    public void addIngredient(Ingredient ingredient) {
        this.ingredients.add(new StringIngredient(ingredient));
    }

    public void addIngredient(String ingredient) {
        this.ingredients.add(new StringIngredient(ingredient));
    }

    @Override
    public String toString() {
        return this.getLine();
    }

    private String getLine() {
        StringBuilder builder = new StringBuilder("[");
        for (int i = 0; i < this.ingredients.size(); i++) {
            if(i == this.ingredients.size()-1) {
                builder.append(this.ingredients.get(i)).append("]");
                continue;
            }
            builder.append(this.ingredients.get(i)).append(", ");
        }
        return builder.toString();
    }

}
