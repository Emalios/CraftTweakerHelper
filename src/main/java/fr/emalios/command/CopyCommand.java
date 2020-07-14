package fr.emalios.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import fr.emalios.recipe.PlayerRecipes;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

public class CopyCommand {

    private final PlayerRecipes playerRecipes;

    public CopyCommand(PlayerRecipes playerRecipes) {
        this.playerRecipes = playerRecipes;
    }

    public void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(LiteralArgumentBuilder.<CommandSource>literal("cth")
                .then(LiteralArgumentBuilder.<CommandSource>literal("copy")
                        .executes(context -> {
                            ServerPlayerEntity playerEntity = context.getSource().asPlayer();
                            TextComponent textComponent = new StringTextComponent("§7Click here to copy");
                            Style style = new Style();
                            String stringRecipe = this.playerRecipes.toString();
                            if(stringRecipe == null) {
                                playerEntity.sendMessage(new StringTextComponent("§7You don't have any recipe save !"));
                                return 1;
                            }
                            style.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, stringRecipe));
                            style.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new StringTextComponent("§ccopy zenscript")));
                            textComponent.setStyle(style);
                            playerEntity.sendMessage(textComponent);
                            return 1;
                        })
                )
        );
    }

}
