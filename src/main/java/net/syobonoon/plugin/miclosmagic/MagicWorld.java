package net.syobonoon.plugin.miclosmagic;

import java.util.ArrayList;
import java.util.List;

import net.syobonoon.plugin.miclosmagic.command.MagicCommandMaster;
import net.syobonoon.plugin.miclosmagic.config.ConfigManager;
import net.syobonoon.plugin.miclosmagic.listener.MagicListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class MagicWorld extends JavaPlugin {
	public static ConfigManager configManager;

	@Override
	public void onEnable() {
		configManager = new ConfigManager(this);
		new MagicListener(this);
		getCommand("magic").setExecutor(new MagicCommandMaster());

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
			configManager.saveUUIDMagicConfig(online_player.getUniqueId());
		}
	}

	//サーバーにいるすべての人の魔法configを読み込む
	public void allPlayerLoadConfig() {
		List<Player> online_players = new ArrayList<>(Bukkit.getOnlinePlayers());
		for (Player online_player : online_players) {
			configManager.loadUUIDMagicConfig(online_player);
		}
	}

}
