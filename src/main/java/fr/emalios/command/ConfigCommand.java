package fr.emalios.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import fr.emalios.config.PlayersConfig;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.StringTextComponent;

import static com.mojang.brigadier.arguments.BoolArgumentType.bool;
import static com.mojang.brigadier.arguments.BoolArgumentType.getBool;

public class ConfigCommand {

    private final PlayersConfig playersConfig;

    public ConfigCommand(PlayersConfig playersConfig) {
        this.playersConfig = playersConfig;
    }

    public void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(LiteralArgumentBuilder.<CommandSource>literal("cth")
                .then(LiteralArgumentBuilder.<CommandSource>literal("config")
                        .then(LiteralArgumentBuilder.<CommandSource>literal("modActivated")
                                .then(RequiredArgumentBuilder.<CommandSource, Boolean>argument("boolean", bool())
                                        .executes((CommandContext<CommandSource> context) -> {
                                            PlayerEntity playerEntity = context.getSource().asPlayer();
                                            boolean bool = getBool(context, "boolean");
                                            this.playersConfig.getPlayerConfig(playerEntity).modActivated = bool;
                                            playerEntity.sendMessage(new StringTextComponent("§7You have set config §bmodActivated §7to §8" + bool));
                                            return 1;
                                        })
                                ).executes(context -> {
                                    PlayerEntity playerEntity = context.getSource().asPlayer();
                                    playerEntity.sendMessage(new StringTextComponent("§7Config §bmodActivated §7is set to §8" + this.playersConfig.getPlayerConfig(playerEntity).modActivated));
                                    return 1;
                                })
                        )
                )
        );
    }

}
