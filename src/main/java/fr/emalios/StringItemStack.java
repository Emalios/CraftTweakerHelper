package fr.emalios;

import net.minecraft.item.ItemStack;

public class StringItemStack {

    private final ItemStack itemStack;

    public StringItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public String getRecipeName() {
        if(this.itemStack.getItem().getRegistryName() == null)
            return "null";
        return this.itemStack.getItem().getRegistryName().getPath();
    }

    @Override
    public String toString() {
        return "<item:" + this.itemStack.getItem().toString() + ">";
    }
}
