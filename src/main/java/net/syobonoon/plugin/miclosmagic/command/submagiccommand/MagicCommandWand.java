package net.syobonoon.plugin.miclosmagic.command.submagiccommand;

import net.syobonoon.plugin.miclosmagic.command.AbstractMagicCommand;
import net.syobonoon.plugin.miclosmagic.config.ConfigManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class MagicCommandWand extends AbstractMagicCommand {

    public boolean onCommand(CommandSender sender, String[] args){
        if (!sender.hasPermission("miclosmagic.wand")) return false;
        if (!(sender instanceof Player)) return false;
        if (args.length != 0) {
            sender.sendMessage(ChatColor.RED + "too many parameter!");
            return false;
        }

        Player p = (Player) sender;

        List<String> wandLore = new ArrayList<>();
        int customNum = 1; //カスタムモデル番号

        ItemStack wandItemStack = new ItemStack(MagicItem.MAGIC_WAND);
        ItemMeta wandItemMeta= wandItemStack.getItemMeta();

        wandItemMeta.setDisplayName(ChatColor.WHITE+"魔法の杖");
        wandLore.add(ChatColor.WHITE+"レア度：未実装");
        wandLore.add(ChatColor.WHITE+"左："+ MagicItem.MAGIC_NOTSET_MESSAGE);
        wandLore.add(ChatColor.WHITE+"右："+ MagicItem.MAGIC_NOTSET_MESSAGE);
        wandLore.add(ChatColor.WHITE+"＜説明＞");
        wandLore.add(ChatColor.WHITE+"ただの魔法の杖");

        wandItemMeta.setLore(wandLore);
        wandItemMeta.setCustomModelData(customNum);
        wandItemMeta.setUnbreakable(true);
        wandItemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        wandItemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        wandItemStack.setItemMeta(wandItemMeta);

        //ユーザーのインベントリに魔法の杖を加える
        p.getInventory().addItem(wandItemStack);
        p.sendMessage(ChatColor.WHITE + "[魔法の杖]" + ChatColor.GRAY + "を入手した");

        return true;
    }
}
