package me.hugo.singledungeon.command;

import me.hugo.singledungeon.SingleDungeon;
import me.hugo.singledungeon.util.LocationUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.annotation.Description;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import java.util.stream.Collectors;

@Command("singledungeon")
@Description("Main Single Dungeons setup command")
@CommandPermission("singledungeon.admin")
public final class SingleDungeonCommand {

    private final SingleDungeon main;

    public SingleDungeonCommand(SingleDungeon main) {
        this.main = main;
    }

    @Default
    @Description("Default output when unknown command or not subcommand!")
    public void onDefault(Player player) {
        player.sendMessage(Component.text("Single Dungeon ", NamedTextColor.GREEN).append(Component.text("v" + main.getDescription().getVersion(), NamedTextColor.AQUA)));
        player.sendMessage(Component.text("Please type \"/singledungeon help\" to learn more!", NamedTextColor.GREEN));
    }

    @Subcommand("help")
    @Description("Shows the list of commands for the plugin!")
    public void onHelpCommand(Player player) {
        player.sendMessage(Component.text("Single Dungeon Help Command List", NamedTextColor.GREEN));

        player.sendMessage(Component.text("/singledungeon setplayerspawn", NamedTextColor.AQUA)
                .append(Component.text(" - Set where the players will spawn when entering the dungeon!", NamedTextColor.GREEN)));
        player.sendMessage(Component.text("/singledungeon addmobspawn", NamedTextColor.AQUA)
                .append(Component.text(" - Add a mob spawn location to the dungeon!", NamedTextColor.GREEN)));
    }

    @Subcommand("setplayerspawn")
    @Description("Set where the players will spawn when entering the dungeon!")
    public void onSetPlayerSpawn(Player player) {
        long currentTime = System.currentTimeMillis();

        main.getConfig().set("game-data.player-spawn-location", LocationUtil.getStringByLocation(player.getLocation(), true));
        main.saveConfig();

        player.sendMessage(Component.text("Saved location in " + (System.currentTimeMillis() - currentTime) + "ms!", NamedTextColor.GREEN));
    }

    @Subcommand("addmobspawn")
    @Description("Add a mob spawn location to the dungeon!")
    public void addMobSpawnLocation(Player player) {
        long currentTime = System.currentTimeMillis();

        main.getMobSpawnLocations().add(player.getLocation());
        main.getConfig().set("game-data.mob-spawn-locations", main.getMobSpawnLocations().stream()
                .map(location -> LocationUtil.getStringByLocation(location, true)).collect(Collectors.toList()));
        main.saveConfig();

        player.sendMessage(Component.text("Saved locations in " + (System.currentTimeMillis() - currentTime) + "ms!", NamedTextColor.GREEN));
    }

}
