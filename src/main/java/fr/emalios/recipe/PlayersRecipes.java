package fr.emalios.recipe;

import net.minecraft.entity.player.PlayerEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayersRecipes {

    private final Map<UUID, Recipes> playerRecipes;

    public PlayersRecipes() {
        this.playerRecipes = new HashMap<>();
    }

    public String getPlayerStringRecipes(PlayerEntity playerEntity) {
        if(!this.playerRecipes.containsKey(playerEntity.getUniqueID()))
            return null;
        return this.playerRecipes.get(playerEntity.getUniqueID()).toString();
    }

    public void addRecipeToPlayer(PlayerEntity playerEntity, Recipe recipe) {
        if(!this.playerRecipes.containsKey(playerEntity.getUniqueID()))
            this.playerRecipes.put(playerEntity.getUniqueID(), new Recipes());
        this.playerRecipes.get(playerEntity.getUniqueID()).addRecipe(recipe);
    }

    public void clearRecipe(PlayerEntity playerEntity) {
        if(!this.playerRecipes.containsKey(playerEntity.getUniqueID())) {
            this.playerRecipes.put(playerEntity.getUniqueID(), new Recipes());
            return;
        }
        this.playerRecipes.replace(playerEntity.getUniqueID(), new Recipes());
    }

    /*
    public Recipes getPlayerRecipes(PlayerEntity playerEntity) {
        if(!this.playerRecipes.containsKey(playerEntity.getUniqueID()))
            this.playerRecipes.put(playerEntity.getUniqueID(), new Recipes());
        return this.playerRecipes.get(playerEntity.getUniqueID());
    }

     */
}
