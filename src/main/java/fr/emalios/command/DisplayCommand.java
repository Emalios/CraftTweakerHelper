package fr.emalios.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import fr.emalios.recipe.PlayersRecipes;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.StringTextComponent;

public class DisplayCommand {

    private final PlayersRecipes playersRecipes;

    public DisplayCommand(PlayersRecipes playersRecipes) {
        this.playersRecipes = playersRecipes;
    }

    public void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(LiteralArgumentBuilder.<CommandSource>literal("cth")
                .then(LiteralArgumentBuilder.<CommandSource>literal("display")
                        .executes(context -> {
                            PlayerEntity playerEntity = context.getSource().asPlayer();
                            playerEntity.sendMessage(new StringTextComponent("§7" + this.playersRecipes.getPlayerStringRecipes(playerEntity)));
                            return 1;
                        })
                )
        );
    }
}
