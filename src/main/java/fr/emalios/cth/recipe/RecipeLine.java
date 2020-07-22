package fr.emalios.cth.recipe;

import com.blamejared.crafttweaker.api.item.IIngredient;

import java.util.ArrayList;
import java.util.List;

public class RecipeLine {

    private final List<IIngredient> ingredients;

    public RecipeLine() {
        this.ingredients = new ArrayList<>();
    }

    public void addIngredient(IIngredient ingredient) {
        this.ingredients.add(ingredient);
    }

    public List<IIngredient> getIngredients() {
        return this.ingredients;
    }

    private String getLine() {
        StringBuilder builder = new StringBuilder("[");
        for (int i = 0; i < this.ingredients.size(); i++) {
            if(i == this.ingredients.size()-1) {
                builder.append(this.ingredients.get(i).getCommandString()).append("]");
                continue;
            }
            builder.append(this.ingredients.get(i).getCommandString()).append(", ");
        }
        return builder.toString();
    }

    @Override
    public String toString() {
        return this.getLine();
    }

}
