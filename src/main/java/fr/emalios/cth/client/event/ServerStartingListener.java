package fr.emalios.cth.client.event;

import fr.emalios.cth.command.ClearCommand;
import fr.emalios.cth.command.ConfigCommand;
import fr.emalios.cth.command.CopyCommand;
import fr.emalios.cth.command.DisplayCommand;
import fr.emalios.cth.config.PlayerConfig;
import fr.emalios.cth.recipe.PlayerRecipes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import org.apache.logging.log4j.Logger;

public class ServerStartingListener {

    private final PlayerRecipes playerRecipes;
    private final PlayerConfig playerConfig;
    private final Logger logger;

    public ServerStartingListener(PlayerRecipes playerRecipes, PlayerConfig playerConfig, Logger logger) {
        this.playerRecipes = playerRecipes;
        this.playerConfig = playerConfig;
        this.logger = logger;
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        logger.info("CtHelperModLoading...");
        logger.info("Register commands...");
        new ConfigCommand(this.playerConfig).register(event.getCommandDispatcher());
        new CopyCommand(this.playerRecipes).register(event.getCommandDispatcher());
        new ClearCommand(this.playerRecipes).register(event.getCommandDispatcher());
        new DisplayCommand(this.playerRecipes).register(event.getCommandDispatcher());
        logger.info("Register commands successfully !");
    }

}
