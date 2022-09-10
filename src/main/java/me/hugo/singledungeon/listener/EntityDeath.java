package me.hugo.singledungeon.listener;

import me.hugo.singledungeon.SingleDungeon;
import me.hugo.singledungeon.game.Game;
import me.hugo.singledungeon.player.DungeonPlayer;
import me.hugo.singledungeon.stats.PlayerStat;
import me.hugo.singledungeon.util.StringUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.util.Ticks;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.concurrent.ThreadLocalRandom;

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

        Game game = main.getMobRegistry().getFromMob(deadMob);

        game.getPlayer().addMobKill();
        game.getCurrentMobs().remove(deadMob);
        main.getMobRegistry().removeMob(deadMob);
    }

    @EventHandler
    public void onEntityHit(EntityDamageEvent event) {
        Entity deadMob = event.getEntity();

        if (!deadMob.hasMetadata("dungeon")) return;
        Game game = main.getMobRegistry().getFromMob(deadMob);

        game.getPlayer().getPlayer().playSound(deadMob.getLocation(), Sound.ENTITY_ZOMBIE_HURT, 1.0f, ThreadLocalRandom.current().nextFloat(0.6f, 1.2f));
    }

    @EventHandler
    public void onPlayerDeath(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (player.getHealth() - event.getFinalDamage() > 0.0) return;

        DungeonPlayer dungeonPlayer = main.getPlayerRegistry().get(player);
        Game currentGame = dungeonPlayer.getCurrentGame();

        if (currentGame == null) return;

        event.setCancelled(true);

        player.sendMessage(Component.text("You died! ", NamedTextColor.RED).append(Component.text("Better luck next time!", NamedTextColor.WHITE)));
        player.sendMessage(Component.text("You survived ", NamedTextColor.GREEN)
                .append(Component.text(StringUtil.formatTime(dungeonPlayer.getTimeAlive()), NamedTextColor.AQUA))
                .append(Component.text(" with "))
                .append(Component.text(dungeonPlayer.getCurrentMobKills(), NamedTextColor.AQUA))
                .append(Component.text(" kills!")));

        currentGame.end();
        dungeonPlayer.addToStat(PlayerStat.DEATHS);

        player.showTitle(Title.title(Component.text("YOU DIED!", NamedTextColor.RED, TextDecoration.BOLD),
                Component.text("Better luck next time!"),
                Title.Times.times(Ticks.duration(10), Ticks.duration(20), Ticks.duration(10))));
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 0.3f);
    }

}
