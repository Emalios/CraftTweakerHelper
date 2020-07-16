package fr.emalios.cth.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import fr.emalios.cth.config.PlayerConfig;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.StringTextComponent;

import java.lang.reflect.Field;

import static com.mojang.brigadier.arguments.BoolArgumentType.bool;
import static com.mojang.brigadier.arguments.BoolArgumentType.getBool;

public class ConfigCommand {

    private final PlayerConfig playerConfig;

    public ConfigCommand(PlayerConfig playerConfig) {
        this.playerConfig = playerConfig;
    }

    public void register(CommandDispatcher<CommandSource> dispatcher) {
        for (Field declaredField : PlayerConfig.class.getDeclaredFields()) {
            String name = declaredField.getName();
            registerConfig(dispatcher, declaredField, name);
        }
    }

    private void registerConfig(CommandDispatcher<CommandSource> dispatcher, Field declaredField, String name) {
        dispatcher.register(LiteralArgumentBuilder.<CommandSource>literal("cth")
                .then(LiteralArgumentBuilder.<CommandSource>literal("config")
                        .then(LiteralArgumentBuilder.<CommandSource>literal(name)
                                .then(RequiredArgumentBuilder.<CommandSource, Boolean>argument("boolean", bool())
                                        .executes((CommandContext<CommandSource> context) -> {
                                            PlayerEntity playerEntity = context.getSource().asPlayer();
                                            boolean bool = getBool(context, "boolean");
                                            try {
                                                declaredField.set(this.playerConfig, bool);
                                            } catch (IllegalAccessException e) {
                                                e.printStackTrace();
                                            }
                                            playerEntity.sendMessage(new StringTextComponent("§7You have set config §b" + name + " §7to §8" + bool));
                                            return 1;
                                        })
                                ).executes(context -> {
                                    PlayerEntity playerEntity = context.getSource().asPlayer();
                                    try {
                                        playerEntity.sendMessage(new StringTextComponent("§7Config §b" + name +" §7is set to §8" + declaredField.getBoolean(this.playerConfig)));
                                    } catch (IllegalAccessException e) {
                                        e.printStackTrace();
                                    }
                                    return 1;
                                })
                        )
                )
        );
    }

}
