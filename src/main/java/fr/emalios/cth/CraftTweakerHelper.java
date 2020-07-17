package fr.emalios.cth;

import fr.emalios.cth.client.event.MouseClickListener;
import fr.emalios.cth.client.event.ServerStartingListener;
import fr.emalios.cth.config.PlayerConfig;
import fr.emalios.cth.recipe.PlayerRecipes;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("crafttweakerhelper")
public class CraftTweakerHelper {

    private static final Logger LOGGER = LogManager.getLogger();

    public CraftTweakerHelper() {
        PlayerRecipes playerRecipes = new PlayerRecipes();
        PlayerConfig playerConfig = new PlayerConfig();

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        MinecraftForge.EVENT_BUS.register(new MouseClickListener(playerRecipes, playerConfig));
        MinecraftForge.EVENT_BUS.register(new ServerStartingListener(playerRecipes, playerConfig, LOGGER));
    }

    private void setup(final FMLCommonSetupEvent event) {}

    private void doClientStuff(final FMLClientSetupEvent event) {
        LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().gameSettings);
        LOGGER.info("Check dependencies...");
        if(!checkDependencies("jei", "crafttweaker")) {
            LOGGER.error("At least one of dependencies are missing !");
            event.setCanceled(true);
        }
    }

    private boolean checkDependencies(String... dependencies) {
        for (String dependency : dependencies) {
            if(!ModList.get().isLoaded(dependency)) {
                LOGGER.error("Dependency : " + dependency + " is missing, please add it");
                return false;
            }
        }
        return true;
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {}

    private void processIMC(final InterModProcessEvent event) {}
}
