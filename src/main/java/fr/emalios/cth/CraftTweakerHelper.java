package fr.emalios.cth;

import fr.emalios.cth.command.ClearCommand;
import fr.emalios.cth.command.ConfigCommand;
import fr.emalios.cth.command.CopyCommand;
import fr.emalios.cth.command.DisplayCommand;
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
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.util.List;

@Mod("crafttweakerhelper")
public class CraftTweakerHelper {

    private final PlayerRecipes playersRecipes;
    private final PlayerConfig playerConfig;

    private static final Logger LOGGER = LogManager.getLogger();

    public CraftTweakerHelper() {
        this.playersRecipes = new PlayerRecipes();
        this.playerConfig = new PlayerConfig();
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {}

    private void doClientStuff(final FMLClientSetupEvent event) {
        LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().gameSettings);
        LOGGER.info("Check dependencies...");
        if(!ModList.get().isLoaded("crafttweaker")) {
            LOGGER.error("Craftweaker is missing");
            event.setCanceled(true);
        }
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {}

    private void processIMC(final InterModProcessEvent event) {}

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        LOGGER.info("CtHelperModLoading...");
        new ConfigCommand(this.playerConfig).register(event.getCommandDispatcher());
        new CopyCommand(this.playersRecipes).register(event.getCommandDispatcher());
        new ClearCommand(this.playersRecipes).register(event.getCommandDispatcher());
        new DisplayCommand(this.playersRecipes).register(event.getCommandDispatcher());
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
        this.playersRecipes.addRecipe(stringShapedRecipe);
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
