package net.syobonoon.plugin.miclosmagic.command;

import java.util.ArrayList;
import java.util.List;

import net.syobonoon.plugin.miclosmagic.command.submagiccommand.MagicCommandBook;
import net.syobonoon.plugin.miclosmagic.command.submagiccommand.MagicCommandReload;
import net.syobonoon.plugin.miclosmagic.command.submagiccommand.MagicCommandWand;
import net.syobonoon.plugin.miclosmagic.command.submagiccommand.MagicCommandWandMaster;
import net.syobonoon.plugin.miclosmagic.config.ConfigManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MagicCommandFactory {

    public AbstractMagicCommand createCommand(String option) {
		AbstractMagicCommand calledCommand;

		switch(option){
			case "book":
				calledCommand = new MagicCommandBook();
				break;

			case "reload":
				calledCommand = new MagicCommandReload();
				break;

			case "wand":
				calledCommand = new MagicCommandWand();
				break;

			case "wandmaster":
				calledCommand = new MagicCommandWandMaster();
				break;

			default:
				calledCommand = null;
				break;
		}

		return calledCommand;
	}
}