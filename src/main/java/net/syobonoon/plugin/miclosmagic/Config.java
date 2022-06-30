package net.syobonoon.plugin.miclosmagic;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

public class Config {
	private Plugin plugin;
	private ItemStack magic_reset;
	private FileConfiguration config = null;
	private HashMap<String, ItemStack> magic_itemstack = new HashMap<String, ItemStack>();
	private HashMap<String, ChatColor> magic_rare_color = new HashMap<String, ChatColor>();
	private HashMap<String, ChatColor> magic_attribution_color = new HashMap<String, ChatColor>();
	private HashMap<UUID, FileConfiguration> uuid_magic_map = new HashMap<UUID, FileConfiguration>();

	public final static Material MAGIC_BOOK = Material.FEATHER; //魔法の本
	public final static Material MAGIC_WAND = Material.DIAMOND_HOE; //魔法の杖
	public final static String MAGIC_GUI = "Magic GUI";
	public final static String MAGIC_ADMIN_GUI = "Magic ADMIN GUI";
	public final static String MAGIC_KEY = "magicIdentifer";
	public final static String MAGIC_NOTSET_MESSAGE = "魔法がセットされていません";
	public final static int MAX_GUI = 53;
	public final static int MAGIC_RESET_GUI = 45; //魔法をリセットするインベントリ番号
	public final static String MAGIC_RESET_NAME = "魔法リセット";

	public Config(Plugin plugin) {
		this.plugin = plugin;
		load_config();
		load_rare_color();
		load_attribution_color();
		load_magics();
	}

	public void load_config() {
		plugin.saveDefaultConfig();
		if (config != null) {
			plugin.reloadConfig();
			plugin.getServer().broadcastMessage(ChatColor.GREEN+"MicLoSMagic reload completed");
		}
		config = plugin.getConfig();
	}

	public void load_magics() {
		int custom_num;
		String rare;
		String attribution;

		for (String magic_name : config.getKeys(false)) {
			List<String> magicLore = new ArrayList<>();

			custom_num = getInt(magic_name+".Setting.custom_model");//CustomModelDataを取得する
			rare = getString(magic_name+".Setting.rare");
			attribution = getString(magic_name+".Setting.attribution");

			ItemStack magic = new ItemStack(MAGIC_BOOK);
			ItemMeta metamagic = magic.getItemMeta();
			metamagic.setDisplayName(magic_rare_color.get(rare) + magic_name);
			magicLore.add(ChatColor.WHITE + "レア度：" + magic_rare_color.get(rare) + rare);
			magicLore.add(ChatColor.WHITE + "属性：" + magic_attribution_color.get(attribution) + attribution);
			magicLore.add(ChatColor.WHITE + "種類：" + getString(magic_name+".Setting.magic_type"));
			magicLore.add(ChatColor.WHITE + "＜説明＞");
			magicLore.add(ChatColor.WHITE + getString(magic_name+".Setting.lore"));

			metamagic.setLore(magicLore);
			metamagic.setCustomModelData(custom_num);
			magic.setItemMeta(metamagic);

			magic_itemstack.put(magic_name, magic);
		}

		//魔法リセット用
		magic_reset = new ItemStack(MAGIC_BOOK);
		ItemMeta metamagic_reset = magic_reset.getItemMeta();
		metamagic_reset.setDisplayName(MAGIC_RESET_NAME);
		metamagic_reset.setCustomModelData(MAGIC_RESET_GUI);
		magic_reset.setItemMeta(metamagic_reset);
	}

	public void load_rare_color() {
		magic_rare_color.put("-1", ChatColor.RED);
		magic_rare_color.put("1", ChatColor.WHITE);
		magic_rare_color.put("2", ChatColor.YELLOW);
		magic_rare_color.put("3", ChatColor.AQUA);
		magic_rare_color.put("4", ChatColor.DARK_GREEN);
		magic_rare_color.put("5", ChatColor.DARK_PURPLE);
		magic_rare_color.put("EX", ChatColor.GOLD);
	}

	public void load_attribution_color() {
		magic_attribution_color.put("火", ChatColor.RED);
		magic_attribution_color.put("水", ChatColor.BLUE);
		magic_attribution_color.put("風", ChatColor.GREEN);
		magic_attribution_color.put("土", ChatColor.GOLD);
		magic_attribution_color.put("光", ChatColor.YELLOW);
		magic_attribution_color.put("闇", ChatColor.BLACK);
	}

	public int getInt(String key) {
		return config.getInt(key);
	}

	public boolean getBoolean(String key) {
		return config.getBoolean(key);
	}

	public String getString(String key) {
		return config.getString(key);
	}

	public ItemStack getMagicReset() {
		return this.magic_reset;
	}

	//レア度に応じた色
	public HashMap<String, ChatColor> getMagicRareColor() {
		return this.magic_rare_color;
	}

	//プレイヤーが覚えている魔法を取得
	public HashMap<UUID, FileConfiguration> getUUIDMagicMap() {
		return this.uuid_magic_map;
	}

	//プレイヤー全体が覚えている魔法mapをメモリにロード
	public void setUUIDMagicMap(UUID uuid_p, FileConfiguration config_p) {
		this.uuid_magic_map.put(uuid_p, config_p);
	}

	//指定したプレイヤーが覚えている魔法ItemStackリストを取得
	public List<ItemStack> getUUIDMagicItemStack(UUID uuid_p) {
		FileConfiguration config_p = this.uuid_magic_map.get(uuid_p);
		List<ItemStack> playerMagicItemStackList = new ArrayList<ItemStack>();

		for (String magic_name : config_p.getStringList("Magic")) {
			playerMagicItemStackList.add(getMagicItemStack().get(magic_name));
		}

		return playerMagicItemStackList;
	}

	//指定したプレイヤーが指定した魔法を覚えているか確認する
	public boolean isRememberMagic(UUID uuid_p, String magic_name) {
		FileConfiguration config_p = this.uuid_magic_map.get(uuid_p);
		List<String> uuid_magic_list = config_p.getStringList("Magic");
		if (uuid_magic_list.contains(magic_name)) return true;
		else return false;
	}

	//指定したプレイヤーに魔法を追加する
	public void setUUIDMagicRegister(UUID uuid_p, String magic_name) {
		FileConfiguration config_p = this.uuid_magic_map.get(uuid_p);
		List<String> uuid_magic_list = config_p.getStringList("Magic");
		uuid_magic_list.add(magic_name);
		config_p.set("Magic", uuid_magic_list);
	}

	//指定したプレイヤーが覚えている魔法をUUID.ymlにセーブする
	public void saveUUIDMagicConfig(UUID uuid_p) {
		if (!this.uuid_magic_map.containsKey(uuid_p)) return;
		FileConfiguration config_p = this.uuid_magic_map.get(uuid_p);
		try {
			config_p.save(new File(plugin.getDataFolder()+File.separator+"userdata"+File.separator+uuid_p+".yml"));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	//指定したプレイヤーのUUID.ymlをメモリにロードする関数
	public void loadUUIDMagicConfig(Player p) {
		FileConfiguration userconfig = null;
		File userfile = new File(plugin.getDataFolder()+File.separator+"userdata"+File.separator+p.getUniqueId()+".yml");
		if (!userfile.exists()) {
			try {
				userfile.createNewFile();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			userconfig = YamlConfiguration.loadConfiguration(userfile);
			userconfig.createSection("Magic");
            try {
				userconfig.save(userfile);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
        } else {
        	userconfig = YamlConfiguration.loadConfiguration(userfile);
        }

		if (userconfig == null) return;

		setUUIDMagicMap(p.getUniqueId(), userconfig); //現在鯖にいるプレイヤーのmapに追加
	}

	//属性に応じた色
	public HashMap<String, ChatColor> getMagicAttributionColor(){
		return this.magic_attribution_color;
	}

	//魔法名と魔法のItemStackのリスト
	public HashMap<String, ItemStack> getMagicItemStack() {
		return this.magic_itemstack;
	}

	//テストメッセージを送る関数
    public void sendTestMessage(String test_str) {
    	plugin.getServer().broadcastMessage(ChatColor.AQUA+test_str);
    }
}
