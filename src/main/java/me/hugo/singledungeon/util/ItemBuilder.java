package me.hugo.singledungeon.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ItemBuilder {
    private ItemStack is;

    public ItemBuilder(Material m) {
        this(m, 1);
    }

    public ItemBuilder(ItemStack is) {
        this.is = is;
    }

    public ItemBuilder(Material m, int amount) {
        is = new ItemStack(m, amount);
    }

    public ItemBuilder clone() {
        return new ItemBuilder(is);
    }

    public ItemBuilder setDurability(short dur) {
        is.setDurability(dur);
        return this;
    }

    public ItemBuilder setType(Material m) {
        is.setType(m);
        return this;
    }

    public ItemBuilder addBookEnchantment(Enchantment enchantment, int level) {
        EnchantmentStorageMeta meta = (EnchantmentStorageMeta) is.getItemMeta();
        meta.addStoredEnchant(enchantment, level, true);
        is.setItemMeta(meta);
        return this;
    }

    public ItemBuilder hideAttributes() {
        ItemMeta im = is.getItemMeta();
        im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_DYE, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);
        im.setUnbreakable(true);
        is.setItemMeta(im);
        return this;
    }

    public ItemBuilder setName(String name) {
        ItemMeta im = is.getItemMeta();
        im.displayName(Component.text(ChatColor.translateAlternateColorCodes('&', name)));
        is.setItemMeta(im);
        return this;
    }

    public ItemBuilder setName(Component name) {
        ItemMeta im = is.getItemMeta();
        im.displayName(name.decoration(TextDecoration.ITALIC, false));
        is.setItemMeta(im);
        return this;
    }


    public List<String> getLore() {
        return is.getItemMeta().getLore();
    }

    public ItemBuilder addUnsafeEnchantment(Enchantment ench, int level) {
        is.addUnsafeEnchantment(ench, level);
        return this;
    }

    public ItemBuilder removeEnchantment(Enchantment ench) {
        is.removeEnchantment(ench);
        return this;
    }

    public ItemBuilder setSkullOwner(String owner) {
        try {
            SkullMeta im = (SkullMeta) is.getItemMeta();
            im.setOwner(owner);
            is.setItemMeta(im);
        } catch (ClassCastException expected) {
        }
        return this;
    }

    public ItemBuilder setSkullOwner(OfflinePlayer owner) {
        try {
            SkullMeta im = (SkullMeta) is.getItemMeta();
            im.setOwningPlayer(owner);
            is.setItemMeta(im);
        } catch (ClassCastException expected) {
        }
        return this;
    }

    public static String colorize(List<String> list) {
        return ChatColor.translateAlternateColorCodes('&', list.toString());
    }

    public ItemBuilder addEnchant(Enchantment ench, int level) {
        if (ench != null) {
            ItemMeta im = is.getItemMeta();
            im.addEnchant(ench, level, true);
            is.setItemMeta(im);
        }
        return this;
    }

    public ItemBuilder setInfinityDurability() {
        is.setDurability(Short.MAX_VALUE);
        return this;
    }

    public ItemBuilder setUnbreakble(boolean unbreakble) {
        is.getItemMeta().setUnbreakable(unbreakble);
        return this;
    }

    public ItemBuilder setLore(String... lore) {
        ItemMeta im = is.getItemMeta();

        im.lore(Arrays.stream(lore).map(string -> LegacyComponentSerializer.legacyAmpersand().deserialize(string).decoration(TextDecoration.ITALIC, false)).collect(Collectors.toList()));
        is.setItemMeta(im);
        return this;
    }

    public ItemBuilder setLore(Component... lore) {
        ItemMeta im = is.getItemMeta();

        im.lore(Arrays.asList(lore));
        is.setItemMeta(im);
        return this;
    }

    public ItemBuilder setLore(List<Component> lore) {
        ItemMeta im = is.getItemMeta();

        im.lore(lore);
        is.setItemMeta(im);
        return this;
    }

    public ItemBuilder setCustomModelData(Integer data) {
        if (data == null || data == -1) return this;
        ItemMeta im = is.getItemMeta();

        im.setCustomModelData(data);

        is.setItemMeta(im);
        return this;
    }

    public ItemBuilder setLeatherArmorColor(Color color) {
        try {
            LeatherArmorMeta im = (LeatherArmorMeta) is.getItemMeta();
            im.setColor(color);
            is.setItemMeta(im);
        } catch (ClassCastException expected) {
        }
        return this;
    }

    public ItemStack toItemStack() {
        return is;
    }

    public static int getAmount(Player arg0, ItemStack arg1) {
        if (arg1 == null) return 0;
        int amount = 0;
        for (int i = 0; i < 36; i++) {
            ItemStack slot = arg0.getInventory().getItem(i);
            if (slot == null || !slot.isSimilar(arg1)) continue;
            amount += slot.getAmount();
        }
        return amount;
    }

}

