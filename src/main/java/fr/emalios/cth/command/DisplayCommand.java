package fr.emalios.cth.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import fr.emalios.cth.recipe.PlayerRecipes;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.StringTextComponent;

public class DisplayCommand {

    private final PlayerRecipes playerRecipes;

    public DisplayCommand(PlayerRecipes playerRecipes) {
        this.playerRecipes = playerRecipes;
    }

    public void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(LiteralArgumentBuilder.<CommandSource>literal("cth")
                .then(LiteralArgumentBuilder.<CommandSource>literal("display")
                        .executes(context -> {
                            PlayerEntity playerEntity = context.getSource().asPlayer();
                            playerEntity.sendMessage(new StringTextComponent("§7" + this.playerRecipes.toString()));
                            return 1;
                        })
                )
        );
    }
}
