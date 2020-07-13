package fr.emalios.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import fr.emalios.recipe.PlayersRecipes;
import fr.emalios.recipe.Recipes;
import net.minecraft.command.CommandSource;
import net.minecraft.util.text.StringTextComponent;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class CopyCommand {

    private final PlayersRecipes playersRecipes;

    public CopyCommand(PlayersRecipes playersRecipes) {
        this.playersRecipes = playersRecipes;
    }

    public void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(LiteralArgumentBuilder.<CommandSource>literal("cth")
                .then(LiteralArgumentBuilder.<CommandSource>literal("copy")
                        .executes(context -> {
                            StringSelection selection = new StringSelection(this.playersRecipes.getPlayerStringRecipes(context.getSource().asPlayer()));
                            System.out.println("TOCOPY : \n" + this.playersRecipes.toString());
                            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                            clipboard.setContents(selection, null);
                            context.getSource().asPlayer().sendMessage(new StringTextComponent("§7You have been copy recipes"));
                            return 1;
                        })
                )
        );
    }

    public static void main(String[] args) {

        String str = "String destined for clipboard\nvyuevrever";

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Clipboard clipboard = toolkit.getSystemClipboard();
        StringSelection strSel = new StringSelection(str);
        clipboard.setContents(strSel, null);

    }

}
