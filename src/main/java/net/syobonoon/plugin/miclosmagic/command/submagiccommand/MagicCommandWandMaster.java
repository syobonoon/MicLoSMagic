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

public class MagicCommandWandMaster extends AbstractMagicCommand {

    public boolean onCommand(CommandSender sender, String[] args){
        if (!sender.hasPermission("magicworld.masterwand")) return false;
        if (!(sender instanceof Player)) return false;
        if (args.length != 0) {
            sender.sendMessage(ChatColor.RED + "too many parameter!");
            return false;
        }

        Player p = (Player) sender;

        List<String> wandLore = new ArrayList<>();
        int custom_num = 2; //カスタムモデル番号

        ItemStack magicwand = new ItemStack(ConfigManager.MAGIC_WAND);
        ItemMeta magicwandMeta = magicwand.getItemMeta();

        magicwandMeta.setDisplayName(ChatColor.RED+"魔法の杖(管理者用)");
        wandLore.add(ChatColor.RED+"レア度：未実装");
        wandLore.add(ChatColor.RED+"左："+ ConfigManager.MAGIC_NOTSET_MESSAGE);
        wandLore.add(ChatColor.RED+"右："+ ConfigManager.MAGIC_NOTSET_MESSAGE);
        wandLore.add(ChatColor.RED+"＜説明＞");
        wandLore.add(ChatColor.RED+"この世のすべての魔法の記憶を持つ");

        magicwandMeta.setLore(wandLore);
        magicwandMeta.setCustomModelData(custom_num);
        magicwandMeta.setUnbreakable(true);
        magicwandMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        magicwandMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        magicwand.setItemMeta(magicwandMeta);

        //ユーザーのインベントリに魔法の杖を加える
        p.getInventory().addItem(magicwand);
        p.sendMessage(ChatColor.GRAY + "魔法の杖(管理者用)を入手した");

        return true;
    }
}
