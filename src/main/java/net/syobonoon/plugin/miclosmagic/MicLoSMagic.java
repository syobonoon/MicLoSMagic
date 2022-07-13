package net.syobonoon.plugin.miclosmagic;

import java.util.ArrayList;
import java.util.List;

import net.syobonoon.plugin.miclosmagic.command.MagicCommand;
import net.syobonoon.plugin.miclosmagic.config.Config;
import net.syobonoon.plugin.miclosmagic.listener.MagicEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class MicLoSMagic extends JavaPlugin {
	public static Config config;

	@Override
	public void onEnable() {
		config = new Config(this);
		new MagicEvent(this);
		getCommand("dainaim").setExecutor(new MagicCommand());
		getCommand("dainaimreload").setExecutor(new MagicCommand());
		getCommand("dwand").setExecutor(new MagicCommand());
		getCommand("dwandadmin").setExecutor(new MagicCommand());

		allPlayerLoadConfig();
		getLogger().info("onEnable");
	}

	@Override
	public void onDisable() {
		allPlayerSaveConfig();
		getLogger().info("onDisable");
	}

	//サーバーにいるすべての人の魔法configを書き込む
	public void allPlayerSaveConfig() {
		List<Player> online_players = new ArrayList<>(Bukkit.getOnlinePlayers());
		for (Player online_player : online_players) {
			config.saveUUIDMagicConfig(online_player.getUniqueId());
		}
	}

	//サーバーにいるすべての人の魔法configを読み込む
	public void allPlayerLoadConfig() {
		List<Player> online_players = new ArrayList<>(Bukkit.getOnlinePlayers());
		for (Player online_player : online_players) {
			config.loadUUIDMagicConfig(online_player);
		}
	}

}
