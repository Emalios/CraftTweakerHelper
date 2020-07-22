package fr.emalios.cth.client.event;

import com.blamejared.crafttweaker.api.item.IIngredient;
import com.blamejared.crafttweaker.impl.item.MCIngredientList;
import com.blamejared.crafttweaker.impl.item.MCItemStack;
import fr.emalios.cth.config.PlayerConfig;
import fr.emalios.cth.ingredient.StringTag;
import fr.emalios.cth.recipe.PlayerRecipes;
import fr.emalios.cth.recipe.RecipeConverter;
import fr.emalios.cth.recipe.RecipeLine;
import fr.emalios.cth.recipe.shapedrecipe.ShapedZSRecipe;
import mezz.jei.gui.recipes.RecipeLayout;
import mezz.jei.gui.recipes.RecipesGui;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.StringTextComponent;
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
    private final RecipeConverter recipeConverter;

    public MouseClickListener(PlayerRecipes playerRecipes, PlayerConfig playerConfig) {
        this.playerRecipes = playerRecipes;
        this.playerConfig = playerConfig;
        this.recipeConverter = new RecipeConverter(playerConfig);
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
            if (recipe instanceof ShapedRecipe) {
                notifyPlayer(playerEntity);
                processWithShapedRecipe(recipe);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void notifyPlayer(ClientPlayerEntity playerEntity) {
        playerEntity.sendStatusMessage(new StringTextComponent("§cYou have been copy recipe"), false);
    }

    private void processWithShapedRecipe(Object recipe) {
        ShapedRecipe shapedRecipe = (ShapedRecipe) recipe;
        System.out.println(shapedRecipe.getId().toString());
        ShapedZSRecipe zsRecipe = new ShapedZSRecipe();
        zsRecipe.setOutput(new MCItemStack(shapedRecipe.getRecipeOutput()));
        convertRecipe(shapedRecipe, zsRecipe);
        this.playerRecipes.addRecipe(zsRecipe);
    }

    private void convertRecipe(ShapedRecipe shapedRecipe, ShapedZSRecipe stringShapedRecipe) {
        NonNullList<Ingredient> list = shapedRecipe.getIngredients();
        int numberOfLines = shapedRecipe.getRecipeHeight();
        int numberOfColumn = shapedRecipe.getRecipeWidth();
        for (int i = 0; i < numberOfLines; i++) {
            RecipeLine recipeLine = new RecipeLine();
            for (int j = 0; j < numberOfColumn; j++) {
                int index = j + numberOfColumn*i;
                if(isEmpty(index, list)) {
                    continue;
                }
                addIngredientToRecipeLine(recipeLine, list.get(index));
            }
            stringShapedRecipe.addRecipeLine(recipeLine);
        }
    }

    private void addIngredientToRecipeLine(RecipeLine recipeLine, Ingredient ingredient) {
        IIngredient iIngredient = IIngredient.fromIngredient(ingredient);
        if(iIngredient instanceof MCIngredientList) {
            recipeLine.addIngredient(new StringTag(ingredient));
            return;
        }
        recipeLine.addIngredient((MCItemStack) iIngredient);
    }

    private boolean isEmpty(int index, NonNullList<Ingredient> list) {
        if(list.size() <= index)
            return true;
        Ingredient ingredient = list.get(index);
        return ingredient.serialize().toString().equals("[]");
    }

}
