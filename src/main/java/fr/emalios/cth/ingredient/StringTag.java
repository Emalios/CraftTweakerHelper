package fr.emalios.cth.ingredient;

import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.impl.item.MCItemStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

public class StringTag implements com.blamejared.crafttweaker.api.item.IIngredient {

    private final Ingredient ingredient;
    private final ItemStack[] matchingStacks;

    public StringTag(Ingredient ingredient) {
        this.ingredient = ingredient;
        this.matchingStacks = ingredient.getMatchingStacks();
    }

    @Override
    public boolean matches(IItemStack iItemStack) {
        for (ItemStack matchingStack : this.matchingStacks) {
            MCItemStack mcItemStack = new MCItemStack(matchingStack);
            if(mcItemStack.matches(iItemStack))
                return true;
        }
        return false;
    }

    @Override
    public Ingredient asVanillaIngredient() {
        return this.ingredient;
    }

    @Override
    public String getCommandString() {
        return this.ingredient.serialize().toString().replace("{", "<").replace("}", ">").replace("\"", "");
    }

    @Override
    public IItemStack[] getItems() {
        MCItemStack[] mcItemStacks = new MCItemStack[this.matchingStacks.length-1];
        for (int i = 0; i < this.matchingStacks.length; i++) {
            mcItemStacks[i] = new MCItemStack(this.matchingStacks[i]);
        }
        return mcItemStacks;
    }
}
