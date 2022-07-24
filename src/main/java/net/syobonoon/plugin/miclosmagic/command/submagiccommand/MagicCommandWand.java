package net.syobonoon.plugin.miclosmagic.command.submagiccommand;

import net.syobonoon.plugin.miclosmagic.command.AbstractMagicCommand;
import net.syobonoon.plugin.miclosmagic.config.ConfigManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MagicCommandWand extends AbstractMagicCommand {
    public final static Material MAGIC_WAND = Material.DIAMOND_HOE;
    private final int DEFAULT_WAND_CUSTOM_NUM = 1;
    private final String DEFAULT_WAND_NAME = "魔法の杖";
    private final String DEFAULT_WAND_RARE = "レア度：未実装";
    private final String DEFAULT_WAND_LEFT = "左：魔法がセットされていません";
    private final String DEFAULT_WAND_RIGHT = "右：魔法がセットされていません";
    private final String DEFAULT_WAND_EXPLAIN_TITLE = "<説明>";
    private final String DEFAULT_WAND_EXPLAIN = "ただの魔法の杖";
    private final List<String> DEFAULT_WAND_LORE = new ArrayList<>();

    public boolean onCommand(CommandSender sender, String[] args){
        if (!sender.hasPermission("miclosmagic.wand")) return false;
        if (!(sender instanceof Player)) return false;
        if (args.length != 0) {
            sender.sendMessage(ChatColor.RED + "too many parameter!");
            return false;
        }

        Player p = (Player) sender;

        ItemStack wandItemStack = new ItemStack(MAGIC_WAND);
        ItemMeta wandItemMeta= wandItemStack.getItemMeta();

        wandItemMeta.setDisplayName(ChatColor.WHITE+DEFAULT_WAND_NAME);
        DEFAULT_WAND_LORE.add(ChatColor.WHITE+DEFAULT_WAND_RARE);
        DEFAULT_WAND_LORE.add(ChatColor.WHITE+DEFAULT_WAND_LEFT);
        DEFAULT_WAND_LORE.add(ChatColor.WHITE+DEFAULT_WAND_RIGHT);
        DEFAULT_WAND_LORE.add(ChatColor.WHITE+DEFAULT_WAND_EXPLAIN_TITLE);
        DEFAULT_WAND_LORE.add(ChatColor.WHITE+DEFAULT_WAND_EXPLAIN);

        wandItemMeta.setLore(DEFAULT_WAND_LORE);
        wandItemMeta.setCustomModelData(DEFAULT_WAND_CUSTOM_NUM);
        wandItemMeta.setUnbreakable(true);
        wandItemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        wandItemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        wandItemStack.setItemMeta(wandItemMeta);

        p.getInventory().addItem(wandItemStack);
        p.sendMessage(ChatColor.WHITE + "["+DEFAULT_WAND_NAME+"]" + ChatColor.GRAY + "を入手した");

        return true;
    }
}
