package net.syobonoon.plugin.miclosmagic.command.submagiccommand;

import net.syobonoon.plugin.miclosmagic.command.AbstractMagicCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MagicCommandBook extends AbstractMagicCommand {

    public boolean onCommand(CommandSender sender, String[] args){
        if (!sender.hasPermission("magicworld.magicbook")) return false;
        if (!(sender instanceof Player)) return false;
        if (args.length != 2) {
            sender.sendMessage(ChatColor.RED + "parameter error");
            return false;
        }

        Player p = (Player)sender;
        String magicName = args[1];
        if (!magicMap.contains(magicName)) {
            p.sendMessage(ChatColor.RED + magicName + " is not exist.");
            return false;
        }

        ItemStack magic = getMagicBook().get(magicName);
        p.getInventory().addItem(magic);
        p.sendMessage(ChatColor.GRAY + "魔法[" + magicName + "]を入手しました");

        return true;
    }
}
