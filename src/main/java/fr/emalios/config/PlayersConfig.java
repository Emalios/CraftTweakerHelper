package fr.emalios.config;

import net.minecraft.entity.player.PlayerEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayersConfig {

    private Map<UUID, Config> playersConfig;

    public PlayersConfig() {
        this.playersConfig = new HashMap<>();
    }

    public Config getPlayerConfig(PlayerEntity playerEntity) {
        if(!this.playersConfig.containsKey(playerEntity.getUniqueID()))
            this.playersConfig.put(playerEntity.getUniqueID(), new Config());
        return this.playersConfig.get(playerEntity.getUniqueID());
    }

}
