package fr.emalios.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import fr.emalios.recipe.PlayerRecipes;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.StringTextComponent;

public class ClearCommand {

    private final PlayerRecipes playerRecipes;

    public ClearCommand(PlayerRecipes playerRecipes) {
        this.playerRecipes = playerRecipes;
    }

    public void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(LiteralArgumentBuilder.<CommandSource>literal("cth")
                .then(LiteralArgumentBuilder.<CommandSource>literal("clear")
                        .executes(context -> {
                            PlayerEntity playerEntity = context.getSource().asPlayer();
                            this.playerRecipes.clearRecipes();
                            playerEntity.sendMessage(new StringTextComponent("§7You're recipes has been cleared"));
                            return 1;
                        })
                )
        );
    }

}
