package fr.emalios.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import fr.emalios.recipe.PlayersRecipes;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.StringTextComponent;

public class ClearCommand {

    private final PlayersRecipes playersRecipes;

    public ClearCommand(PlayersRecipes playersRecipes) {
        this.playersRecipes = playersRecipes;
    }

    public void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(LiteralArgumentBuilder.<CommandSource>literal("cth")
                .then(LiteralArgumentBuilder.<CommandSource>literal("clear")
                        .executes(context -> {
                            PlayerEntity playerEntity = context.getSource().asPlayer();
                            this.playersRecipes.clearRecipe(playerEntity);
                            playerEntity.sendMessage(new StringTextComponent("§7You're recipes has been cleared"));
                            return 1;
                        })
                )
        );
    }

}
