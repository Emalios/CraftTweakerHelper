package fr.emalios;

import fr.emalios.command.ClearCommand;
import fr.emalios.command.ConfigCommand;
import fr.emalios.command.CopyCommand;
import fr.emalios.command.DisplayCommand;
import fr.emalios.config.PlayersConfig;
import fr.emalios.recipe.PlayersRecipes;
import fr.emalios.recipe.Recipes;
import fr.emalios.recipe.shapedrecipe.ShapedRecipe;
import mezz.jei.gui.recipes.RecipeLayout;
import mezz.jei.gui.recipes.RecipesGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.util.List;

@Mod("helpermod")
public class HelperMod {

    private final PlayersRecipes playersRecipes;
    private final PlayersConfig playersConfig;

    private static final Logger LOGGER = LogManager.getLogger();

    public HelperMod() {
        this.playersRecipes = new PlayersRecipes();
        this.playersConfig = new PlayersConfig();
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {}

    private void doClientStuff(final FMLClientSetupEvent event) {
        LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().gameSettings);
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {}

    private void processIMC(final InterModProcessEvent event) {}

    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        LOGGER.info("CtHelperModLoading...");
        new ConfigCommand(this.playersConfig).register(event.getCommandDispatcher());
        new CopyCommand(this.playersRecipes).register(event.getCommandDispatcher());
        new ClearCommand(this.playersRecipes).register(event.getCommandDispatcher());
        new DisplayCommand(this.playersRecipes).register(event.getCommandDispatcher());
    }

    @SubscribeEvent
    public void onMouseClickEvent(GuiScreenEvent.MouseClickedEvent.Pre event) {
        Screen gui = event.getGui();
        PlayerEntity playerEntity = gui.getMinecraft().player;
        if(playerEntity == null) {
            System.out.println("PLAYER NULL");
            return;
        }
        if(!this.playersConfig.getPlayerConfig(playerEntity).modActivated)
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
                processWithShapedRecipe(playerEntity, recipe);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void processWithShapedRecipe(PlayerEntity playerEntity, Object recipe) {
        net.minecraft.item.crafting.ShapedRecipe shapedRecipe = (net.minecraft.item.crafting.ShapedRecipe) recipe;
        ShapedRecipe stringShapedRecipe = new ShapedRecipe();
        stringShapedRecipe.setOutput(shapedRecipe.getRecipeOutput());
        LOGGER.log(Level.DEBUG, "GROUP : " + shapedRecipe.getGroup() + "\nSIZE : " + shapedRecipe.getIngredients().size());
        for (Ingredient ingredient : shapedRecipe.getIngredients()) {
            if(ingredient.serialize().toString().equals("[]")) {
                stringShapedRecipe.addIngredients("<item:minecraft:air>");
                continue;
            }
            stringShapedRecipe.addIngredients(ingredient);
        }
        this.playersRecipes.addRecipeToPlayer(playerEntity, stringShapedRecipe);
    }
}
