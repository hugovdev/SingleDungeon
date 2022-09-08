package me.hugo.singledungeon.listener;

import me.hugo.singledungeon.SingleDungeon;
import me.hugo.singledungeon.game.Game;
import me.hugo.singledungeon.player.DungeonPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public class EntityDeath implements Listener {

    private final SingleDungeon main;

    public EntityDeath(SingleDungeon main) {
        this.main = main;
    }

    @EventHandler
    public void onMobDeath(EntityDeathEvent event) {
        LivingEntity deadMob = event.getEntity();

        if (!deadMob.hasMetadata("dungeon")) return;

        event.getDrops().clear();
        event.setDroppedExp(0);
        event.setDeathSound(Sound.BLOCK_NOTE_BLOCK_PLING);

        Game game = main.getMobRegistry().getFromMob(deadMob);

        game.getCurrentMobs().remove(deadMob);
    }

    @EventHandler
    public void onPlayerDeath(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (player.getHealth() - event.getFinalDamage() > 0.0) return;

        DungeonPlayer dungeonPlayer = main.getPlayerRegistry().get(player);
        Game currentGame = dungeonPlayer.getCurrentGame();

        if (currentGame == null) return;

        event.setCancelled(true);
        currentGame.end();
        player.sendMessage(Component.text("You died!", NamedTextColor.RED));
    }

}
