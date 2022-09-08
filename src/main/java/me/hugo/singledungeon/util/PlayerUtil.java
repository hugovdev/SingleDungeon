package me.hugo.singledungeon.util;

import me.hugo.singledungeon.player.DungeonPlayer;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;

public final class PlayerUtil {

    public static void cleanPlayer(DungeonPlayer dungeonPlayer, GameMode gameMode) {
        cleanPlayer(dungeonPlayer.getPlayer(), gameMode);
    }

    public static void cleanPlayer(Player player, GameMode gameMode) {
        player.setGameMode(gameMode);

        player.setHealth(20.0);
        player.setFoodLevel(20);

        player.setLevel(0);
        player.setExp(0.0f);

        player.setFireTicks(0);

        PlayerInventory inventory = player.getInventory();

        inventory.clear();
        inventory.setArmorContents(null);

        for (PotionEffect potionType : player.getActivePotionEffects()) {
            player.removePotionEffect(potionType.getType());
        }
    }

}
