package net.syobonoon.plugin.miclosmagic;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MagicCommand implements TabExecutor {
	private List<String> magic_name_list = new ArrayList<String>(MicLoSMagic.config.getMagicItemStack().keySet());

	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		boolean isSuccess = false;
		if (command.getName().equalsIgnoreCase("dainaim")) {
			isSuccess = dainaim(sender, args);
		}
		else if (command.getName().equalsIgnoreCase("dainaimreload")) {
			isSuccess = dainaimreload(sender, args);
		}
		else if(command.getName().equalsIgnoreCase("dwand")) {
			isSuccess = dwand(sender, args);
		}
		else if (command.getName().equalsIgnoreCase("dwandadmin")) {
			isSuccess = dwandadmin(sender, args);
		}
		return isSuccess;
	}

	@Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if (command.getName().equalsIgnoreCase("dainaim")) {
			if (args.length == 1) return magic_name_list;
		}
        return null;
    }

	//dainaim
	private boolean dainaim(CommandSender sender, String[] args) {
		if (!sender.hasPermission("miclosmagic.dainaim")) return false;
		if (!(sender instanceof Player)) return false;
		if (args.length == 0 || args.length >= 2) {
	        sender.sendMessage(ChatColor.RED + "parameter error");
	        return false;
	    }

		Player p = (Player) sender;

		//入力された魔法が存在しなかったら
		if (!magic_name_list.contains(args[0])) {
			p.sendMessage(ChatColor.RED + args[0] + " is not exist.");
			return false;
		}

		String magic_name = args[0];
		ItemStack magic = MicLoSMagic.config.getMagicItemStack().get(magic_name);//hashmapから魔法のitemstackを取り出す

		//ユーザーのインベントリに魔法を加える
		p.getInventory().addItem(magic);
		p.sendMessage(ChatColor.GRAY + "魔法[" + magic_name + "]を入手しました");

		return true;
	}

	//dainaimwand:魔法の杖を出す
	private boolean dwand(CommandSender sender, String[] args) {
		if (!sender.hasPermission("miclosmagic.wand")) return false;
		if (!(sender instanceof Player)) return false;
		if (args.length != 0) {
	        sender.sendMessage(ChatColor.RED + "too many parameter!");
	        return false;
	    }

		Player p = (Player) sender;

		List<String> magicwand_Lore = new ArrayList<>();
		int custom_num = 1; //カスタムモデル番号

		ItemStack magicwand = new ItemStack(Config.MAGIC_WAND);
		ItemMeta magicwandMeta = magicwand.getItemMeta();

		magicwandMeta.setDisplayName(ChatColor.WHITE+"魔法の杖");
		magicwand_Lore.add(ChatColor.WHITE+"レア度：未実装");
		magicwand_Lore.add(ChatColor.WHITE+"左："+Config.MAGIC_NOTSET_MESSAGE);
		magicwand_Lore.add(ChatColor.WHITE+"右："+Config.MAGIC_NOTSET_MESSAGE);
		magicwand_Lore.add(ChatColor.WHITE+"＜説明＞");
		magicwand_Lore.add(ChatColor.WHITE+"ただの魔法の杖");

		magicwandMeta.setLore(magicwand_Lore);
		magicwandMeta.setCustomModelData(custom_num);
		magicwandMeta.setUnbreakable(true);
		magicwandMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		magicwandMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		magicwand.setItemMeta(magicwandMeta);

		//ユーザーのインベントリに魔法の杖を加える
		p.getInventory().addItem(magicwand);
		p.sendMessage(ChatColor.GRAY + "魔法の杖を入手した");

		return true;
	}

	//dwandadmin:管理人用の魔法の杖を出す
	private boolean dwandadmin(CommandSender sender, String[] args) {
		if (!sender.hasPermission("miclosmagic.adminwand")) return false;
		if (!(sender instanceof Player)) return false;
		if (args.length != 0) {
	        sender.sendMessage(ChatColor.RED + "too many parameter!");
	        return false;
	    }

		Player p = (Player) sender;

		List<String> magicwand_Lore = new ArrayList<>();
		int custom_num = 2; //カスタムモデル番号

		ItemStack magicwand = new ItemStack(Config.MAGIC_WAND);
		ItemMeta magicwandMeta = magicwand.getItemMeta();

		magicwandMeta.setDisplayName(ChatColor.RED+"魔法の杖(管理者用)");
		magicwand_Lore.add(ChatColor.RED+"レア度：未実装");
		magicwand_Lore.add(ChatColor.RED+"左："+Config.MAGIC_NOTSET_MESSAGE);
		magicwand_Lore.add(ChatColor.RED+"右："+Config.MAGIC_NOTSET_MESSAGE);
		magicwand_Lore.add(ChatColor.RED+"＜説明＞");
		magicwand_Lore.add(ChatColor.RED+"この世のすべての魔法の記憶を持つ");

		magicwandMeta.setLore(magicwand_Lore);
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

	//dainaimreload:configをreloadする
	private boolean dainaimreload(CommandSender sender, String[] args) {
		if (!sender.hasPermission("miclosmagic.reload")) return false;
		if (args.length != 0) return false;
		MicLoSMagic.config.load_config();
		return true;
	}
}