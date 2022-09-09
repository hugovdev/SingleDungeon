package me.hugo.singledungeon.game.action.list;

import me.hugo.singledungeon.SingleDungeon;
import me.hugo.singledungeon.game.Game;
import me.hugo.singledungeon.game.action.GameStateAction;
import me.hugo.singledungeon.player.DungeonPlayer;
import me.hugo.singledungeon.util.StringUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public final class FightingGameStateAction implements GameStateAction {

    private final List<String> zombieAdjectives = Arrays.asList("Silly", "Crazy", "Smart", "Sad", "Happy", "Lovely", "Jumpy", "Formidable", "Strange", "Ominous", "Weird", "Smelly", "Strong", "Weak", "Speedy");

    @Override
    public void gameTick(SingleDungeon main, Game game) {
        DungeonPlayer dungeonPlayer = game.getPlayer();

        dungeonPlayer.getPlayer().sendActionBar(
                Component.text("Mob Kills: ", NamedTextColor.GREEN)
                        .append(Component.text(dungeonPlayer.getCurrentMobKills(), NamedTextColor.GOLD))
                        .append(Component.text(" - Time Alive: "))
                        .append(Component.text(StringUtil.formatTime(dungeonPlayer.getTimeAlive()), NamedTextColor.GOLD)));

        dungeonPlayer.addAliveTime();

        // Mob Cap
        int mobCap = 10;
        if (game.getCurrentMobs().size() >= mobCap) return;

        for (Location mobSpawn : main.getMobSpawnLocations()) {
            // If after spawning a mob (or not) the cap is met, we return.
            if (game.getCurrentMobs().size() >= mobCap) return;

            // Some randomness per spawnpoint for a mob to spawn there or not
            if (ThreadLocalRandom.current().nextInt(0, 7) != 0) continue;

            spawnMob(main, game, mobSpawn);
        }
    }

    public void spawnMob(SingleDungeon main, Game game, Location location) {
        Player bukkitPlayer = game.getPlayer().getPlayer();
        ThreadLocalRandom random = ThreadLocalRandom.current();

        Zombie entity = (Zombie) location.getWorld().spawnEntity(location, EntityType.ZOMBIE);
        entity.setSilent(true);
        entity.setCustomNameVisible(true);
        entity.setCollidable(false);
        entity.setAdult();

        EntityEquipment equipment = entity.getEquipment();

        equipment.setItemInMainHand(getRandomSword());
        equipment.setHelmet(getRandomPiece("HELMET"));
        equipment.setChestplate(getRandomPiece("CHESTPLATE"));
        equipment.setLeggings(getRandomPiece("LEGGINGS"));
        equipment.setBoots(getRandomPiece("BOOTS"));

        float health = random.nextFloat(10.0f, 20.0f);

        entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(health);
        entity.setHealth(health);

        entity.customName(Component.text(getRandomAdjective() + " Zombie", NamedTextColor.AQUA));

        entity.setTarget(bukkitPlayer);

        entity.setMetadata("dungeon", new FixedMetadataValue(main, "dungeon"));
        main.getMobRegistry().registerMob(entity, game);

        bukkitPlayer.playSound(location, Sound.ENTITY_ZOMBIE_AMBIENT, 1.0f, 1.0f);

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player == bukkitPlayer) continue;

            player.hideEntity(main, entity);
        }

        game.getCurrentMobs().add(entity);
    }

    /*
    50% chance to be empty
    50% chance of being a random sword
     */
    private ItemStack getRandomSword() {
        ThreadLocalRandom random = ThreadLocalRandom.current();

        if (random.nextInt(0, 2) == 1) {
            return null;
        }

        int randomSword = random.nextInt(0, 4);

        return switch (randomSword) {
            case 0 -> new ItemStack(Material.DIAMOND_SWORD);
            case 1 -> new ItemStack(Material.WOODEN_SWORD);
            case 2 -> new ItemStack(Material.STONE_SWORD);
            case 3 -> new ItemStack(Material.IRON_SWORD);
            default -> null;
        };
    }

    /*
    50% chance to be empty
    50% chance of being a random piece of armor
     */
    private ItemStack getRandomPiece(String slot) {
        ThreadLocalRandom random = ThreadLocalRandom.current();

        if (random.nextInt(0, 2) == 1) {
            return null;
        }

        int randomSword = random.nextInt(0, 4);

        return switch (randomSword) {
            case 0 -> new ItemStack(Material.valueOf("DIAMOND_" + slot.toUpperCase()));
            case 1 -> new ItemStack(Material.valueOf("IRON_" + slot.toUpperCase()));
            case 2 -> new ItemStack(Material.valueOf("CHAINMAIL_" + slot.toUpperCase()));
            case 3 -> new ItemStack(Material.valueOf("LEATHER_" + slot.toUpperCase()));
            default -> null;
        };
    }

    private String getRandomAdjective() {
        return zombieAdjectives.get(ThreadLocalRandom.current().nextInt(0, zombieAdjectives.size()));
    }

}
