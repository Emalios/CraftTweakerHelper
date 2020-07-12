package fr.emalios;

import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.runtime.IRecipesGui;
import mezz.jei.gui.ingredients.GuiIngredient;
import mezz.jei.gui.recipes.RecipeGuiLogic;
import mezz.jei.gui.recipes.RecipeLayout;
import mezz.jei.gui.recipes.RecipesGui;
import mezz.jei.input.IClickedIngredient;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.gui.recipebook.RecipeOverlayGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IRecipeHolder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.tags.Tag;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraftforge.client.event.GuiContainerEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.IRecipeContainer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
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
import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("helpermod")
public class HelperMod {

    private StringRecipes stringRecipes;

    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public HelperMod() {
        this.stringRecipes = new StringRecipes();
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        // some preinit code
        LOGGER.info("HELLO FROM PREINIT");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
        LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().gameSettings);
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        // some example code to dispatch IMC to another mod
        InterModComms.sendTo("helpermod", "helloworld", () -> {
            LOGGER.info("Hello world from the MDK");
            return "Hello world";
        });
    }

    private void processIMC(final InterModProcessEvent event) {
        // some example code to receive and process InterModComms from other mods
        LOGGER.info("Got IMC {}", event.getIMCStream().
                map(m -> m.getMessageSupplier().get()).
                collect(Collectors.toList()));
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    @SubscribeEvent
    public void onMouseClickEvent(GuiScreenEvent.MouseClickedEvent event) {
        System.out.println("KAPPA");
        Screen gui = event.getGui();
        if (!(gui instanceof RecipesGui))
            return;
        System.out.println("instance of recipevctudsifsess gui");
        RecipesGui recipesGui = (RecipesGui) gui;
        try {
            Field field = FieldUtils.getField(recipesGui.getClass(), "recipeLayouts", true);
            List<RecipeLayout> layouts = (List<RecipeLayout>) field.get(recipesGui);
            if (layouts == null)
                return;
            System.out.println(layouts.toString() + "\n" + "Size : " + layouts.size());
            RecipeLayout layout = layouts.get(0);
            field = FieldUtils.getField(layout.getClass(), "recipe", true);
            Object recipe = field.get(layout);
            if (recipe == null) {
                System.out.println("recipe null");
                return;
            }
            if (!(recipe instanceof ShapedRecipe)) {
                System.out.println("don't instanceof shaped recipe");
                return;
            }
            ShapedRecipe shapedRecipe = (ShapedRecipe) recipe;

            StringRecipe stringRecipe = new StringRecipe();
            stringRecipe.setOutput(new StringItemStack(shapedRecipe.getRecipeOutput()));
            for (Ingredient ingredient : shapedRecipe.getIngredients()) {
                System.out.println(ingredient.serialize().toString());
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}

    /*
    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            // register a new block here
            LOGGER.info("HELLO from Register Block");
        }
    }

     */

                    /*
            for (GuiIngredient<ItemStack> value : layout.getItemStacks().getGuiIngredients().values()) {
                if(value.getDisplayedIngredient() == null)
                    continue;
                if(value.getDisplayedIngredient().getItem().getRegistryName() == null)
                    continue;
                System.out.println(value.getAllIngredients());
                System.out.println(value.isInput() + " ; " + value.getDisplayedIngredient().getItem().getRegistryName().getPath());
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

             */
