package fr.emalios.cth.client.event;

import fr.emalios.cth.config.PlayerConfig;
import fr.emalios.cth.recipe.PlayerRecipes;
import fr.emalios.cth.recipe.RecipeLine;
import fr.emalios.cth.recipe.shapedrecipe.ShapedRecipe;
import mezz.jei.gui.recipes.RecipeLayout;
import mezz.jei.gui.recipes.RecipesGui;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.util.List;

public class MouseClickListener {

    private final PlayerRecipes playerRecipes;
    private final PlayerConfig playerConfig;

    public MouseClickListener(PlayerRecipes playerRecipes, PlayerConfig playerConfig) {
        this.playerRecipes = playerRecipes;
        this.playerConfig = playerConfig;
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onMouseClickEvent(GuiScreenEvent.MouseClickedEvent.Pre event) {
        Screen gui = event.getGui();
        ClientPlayerEntity playerEntity = gui.getMinecraft().player;
        if(playerEntity == null)
            return;
        if(!this.playerConfig.modActivated)
            return;
        if (!(gui instanceof RecipesGui))
            return;
        RecipesGui recipesGui = (RecipesGui) gui;
        try {
            Field field = FieldUtils.getField(recipesGui.getClass(), "recipeLayouts", true);
            List<RecipeLayout> layouts = (List<RecipeLayout>) field.get(recipesGui);
            if (layouts == null)
                return;
            RecipeLayout layout = layouts.get(0);
            field = FieldUtils.getField(layout.getClass(), "recipe", true);
            Object recipe = field.get(layout);
            if (recipe == null) {
                System.out.println("recipe null");
                return;
            }
            if (recipe instanceof net.minecraft.item.crafting.ShapedRecipe) {
                processWithShapedRecipe(recipe);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void processWithShapedRecipe(Object recipe) {
        net.minecraft.item.crafting.ShapedRecipe shapedRecipe = (net.minecraft.item.crafting.ShapedRecipe) recipe;
        ShapedRecipe stringShapedRecipe = new ShapedRecipe();
        stringShapedRecipe.setOutput(shapedRecipe.getRecipeOutput());
        processThing(shapedRecipe, stringShapedRecipe);
        this.playerRecipes.addRecipe(stringShapedRecipe);
    }

    private void processThing(net.minecraft.item.crafting.ShapedRecipe shapedRecipe, ShapedRecipe stringShapedRecipe) {
        NonNullList<Ingredient> list = shapedRecipe.getIngredients();
        int numberOfLines = shapedRecipe.getRecipeHeight();
        int numberOfColumn = shapedRecipe.getRecipeWidth();
        for (int i = 0; i < numberOfLines; i++) {
            RecipeLine recipeLine = new RecipeLine();
            for (int j = 0; j < numberOfColumn; j++) {
                int index = j + numberOfColumn*i;
                if(isEmpty(index, list)) {
                    recipeLine.addIngredient("<item:minecraft:air>");
                    continue;
                }
                recipeLine.addIngredient(list.get(index));
            }
            stringShapedRecipe.addRecipeLine(recipeLine);
        }
    }

    private boolean isEmpty(int index, NonNullList<Ingredient> list) {
        if(list.size() <= index)
            return true;
        Ingredient ingredient = list.get(index);
        return ingredient.serialize().toString().equals("[]");
    }

}
