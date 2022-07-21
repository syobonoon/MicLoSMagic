package net.syobonoon.plugin.miclosmagic.listener;

import java.util.ArrayList;
import java.util.List;

import net.syobonoon.plugin.miclosmagic.magic.MagicEffect;
import net.syobonoon.plugin.miclosmagic.MagicWorld;
import net.syobonoon.plugin.miclosmagic.config.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;


public class MagicEvent implements Listener{
	private List<String> magic_name_list = new ArrayList<String>(MagicWorld.config.getMagicItemStack().keySet());
	private Plugin plugin;
	private MagicEffect mo;

	public MagicEvent(Plugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.plugin = plugin;
		this.mo = new MagicEffect(plugin);
	}

	//魔法の杖を使用する関数
	@EventHandler
	public void magicTrigger(PlayerInteractEvent e) {
		if (e.getHand().equals(EquipmentSlot.OFF_HAND)) return;

		//魔法の杖ではなかった
		Player p = e.getPlayer();
		ItemStack user_magicwand = p.getInventory().getItemInMainHand();
		if (MagicWandtype(user_magicwand) == -1) return;

		List<String> user_magicwand_lore = user_magicwand.getItemMeta().getLore();
		String user_magic_name = null;

		//左魔法
		if (e.getAction().equals(Action.LEFT_CLICK_AIR) || e.getAction().equals(Action.LEFT_CLICK_BLOCK)){
			user_magic_name = ChatColor.stripColor(user_magicwand_lore.get(1)).replace("左：", "");

			//魔法がセットされていなかったら
			if (user_magic_name.equals(ConfigManager.MAGIC_NOTSET_MESSAGE)) {
				p.sendMessage(ChatColor.GRAY+ ConfigManager.MAGIC_NOTSET_MESSAGE);
				return;
			}
			//魔法が存在しなかったら
			else if(!magic_name_list.contains(user_magic_name)) return;

		}
		//右魔法
		else if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
			user_magic_name = ChatColor.stripColor(user_magicwand_lore.get(2)).replace("右：", "");

			//魔法がセットされていなかったら
			if (user_magic_name.equals(ConfigManager.MAGIC_NOTSET_MESSAGE)) {
				p.sendMessage(ChatColor.GRAY+ ConfigManager.MAGIC_NOTSET_MESSAGE);
				return;
			}
			//魔法が存在しなかったら
			else if(!magic_name_list.contains(user_magic_name)) return;

		}

		if(user_magic_name == null) return;
		excute_magic(user_magic_name, p); //魔法を実行

		return;
	}

	//プレイヤーがログインしたら覚えている魔法を記録するymlを作成する
	@EventHandler
	public void onJoinRegisterConfig(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		MagicWorld.config.loadUUIDMagicConfig(p);
		return;
	}

	//魔法の杖でブロックの変更を防ぐ関数
	@EventHandler
	public void wandPreventBlockChange(PlayerInteractEvent e) {

		if (e.getHand().equals(EquipmentSlot.OFF_HAND)) return;

		//魔法の杖ではなかった
		Player p = e.getPlayer();
		ItemStack user_magicwand = p.getInventory().getItemInMainHand();
		if (MagicWandtype(user_magicwand) == -1) return;

		if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			Material clickedblick = e.getClickedBlock().getType();
			if (!(clickedblick.equals(Material.DIRT) || clickedblick.name().contains("GRASS") || clickedblick.name().equals("GRASS_PATH") || clickedblick.name().equals("COARSE_DIRT"))) return;
			e.setCancelled(true);
		}
		else if (e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
			e.setCancelled(true);
		}
		return;
	}

	//Fキーを押したときに魔法設定GUIを開く関数
	@EventHandler
	public void magicGUI(PlayerSwapHandItemsEvent e) {
		Player p = e.getPlayer();

		if (MagicWandtype(e.getOffHandItem()) == -1) return;

		e.setCancelled(true);
		Inventory inv = Bukkit.createInventory(null, ConfigManager.MAX_GUI+1, ConfigManager.MAGIC_GUI);

		if (MagicWandtype(e.getOffHandItem()) == 1) {

			int i = 0;
			for (ItemStack magic_itemstack : MagicWorld.config.getUUIDMagicItemStack(p.getUniqueId())) {

				if (i > ConfigManager.MAX_GUI) break;
				inv.setItem(i, magic_itemstack);
				i++;
			}

			inv.setItem(ConfigManager.MAGIC_RESET_GUI, MagicWorld.config.getMagicReset()); //魔法リセットアイテムをセット
			p.openInventory(inv);
			return;
		}
		else if (MagicWandtype(e.getOffHandItem()) == 2) {

			int i = 0;
			for (ItemStack magic_itemstack : MagicWorld.config.getMagicItemStack().values()) {

				if (i > ConfigManager.MAX_GUI) break;
				inv.setItem(i, magic_itemstack);//魔法一覧のhashmapからセットする
				i++;
			}

			inv.setItem(ConfigManager.MAGIC_RESET_GUI, MagicWorld.config.getMagicReset()); //魔法リセットアイテムをセット
			p.openInventory(inv);
			return;
		}
		return;
	}

	//ログアウトしたときにUUID.ymlを保存する関数
	@EventHandler
	public void quitsave(PlayerQuitEvent e) {
		MagicWorld.config.saveUUIDMagicConfig(e.getPlayer().getUniqueId());
	}

	//Fキーを押したときに魔法を登録する関数
	@EventHandler
	public void registerMagic(PlayerSwapHandItemsEvent e) {
		Player p = e.getPlayer();

		ItemStack magic_item = p.getInventory().getItemInMainHand();
		if (!isMagic(magic_item)) return;
		e.setCancelled(true);

		String magic_name_colored = magic_item.getItemMeta().getDisplayName();

		//すでに覚えていたら
		if (MagicWorld.config.isRememberMagic(p.getUniqueId(), ChatColor.stripColor(magic_name_colored))) {
			p.sendMessage(ChatColor.RED+"すでに魔法"+magic_name_colored+ChatColor.RED+"は覚えています");
			return;
		}

		MagicWorld.config.setUUIDMagicRegister(p.getUniqueId(), ChatColor.stripColor(magic_name_colored)); //UUID.ymlに魔法の登録

		//プレイヤーの手持ちの魔法を減らす
		int magic_item_amount = magic_item.getAmount();
		if(magic_item_amount > 1) {
			magic_item.setAmount(magic_item_amount - 1);
		}else{
			magic_item.setAmount(0);
		}

		p.sendMessage(ChatColor.GRAY + "魔法" + magic_name_colored + ChatColor.GRAY + "を習得しました");
		p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1F, 1F);
		Location loc_p = p.getLocation();
		loc_p.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, loc_p, 30, 1, 1, 1, 1);
		return;
	}

	//魔法設定GUIで登録する関数
	@EventHandler
    public void onInventoryEvent(InventoryClickEvent e) {
		Player p = (Player)e.getWhoClicked();

		if (!(e.isLeftClick() || e.isRightClick())) return;

		//魔法設定GUIではなかった場合
		if (!e.getView().getTitle().equals(ConfigManager.MAGIC_GUI)) return;

		e.setCancelled(true);//魔法設定GUIでは全てのアイテムの移動を禁止する

		//魔法設定GUIではないところをクリックした場合
		if (!(0 <= e.getRawSlot() && e.getRawSlot() <= ConfigManager.MAX_GUI)) return;

		//魔法の杖ではなかった場合
		ItemStack selected_wand_item = p.getInventory().getItemInMainHand();
		if (MagicWandtype(selected_wand_item) == -1) return;
		ItemMeta selected_wand_itemMeta = selected_wand_item.getItemMeta();

		//クリックしたアイテムが魔法ではなかった場合
		ItemStack clicked_item = e.getCurrentItem();
		if (!isMagic(clicked_item)) return;

		//魔法名が存在しなかったら
		ItemMeta clicked_itemMeta = clicked_item.getItemMeta();
		String user_magic_name_colored = clicked_itemMeta.getDisplayName();
		String user_magic_name = ChatColor.stripColor(user_magic_name_colored);

		List<String> lore_update = selected_wand_itemMeta.getLore();

		//魔法リセットをクリックした場合
		if (user_magic_name.equals(ConfigManager.MAGIC_RESET_NAME)) {
			lore_update.set(1, ChatColor.WHITE+"左："+ ConfigManager.MAGIC_NOTSET_MESSAGE);
			lore_update.set(2, ChatColor.WHITE+"右："+ ConfigManager.MAGIC_NOTSET_MESSAGE);
			selected_wand_itemMeta.setLore(lore_update);
			selected_wand_item.setItemMeta(selected_wand_itemMeta);
			p.sendMessage(ChatColor.GRAY+"魔法を取り外しました");
			return;
		}

		if(!magic_name_list.contains(user_magic_name)) return;

		//魔法の杖を左クリックで登録
		if (e.isLeftClick()) {
			lore_update.set(1, ChatColor.WHITE+"左："+user_magic_name_colored);
			p.sendMessage(ChatColor.GRAY+"左に"+user_magic_name_colored+ChatColor.GRAY+"を登録しました");

    	}
		//魔法の杖を右クリックで登録
		else if (e.isRightClick()) {
			lore_update.set(2, ChatColor.WHITE+"右："+user_magic_name_colored);
			p.sendMessage(ChatColor.GRAY+"右に"+user_magic_name_colored+ChatColor.GRAY+"を登録しました");

    	}
		selected_wand_itemMeta.setLore(lore_update);
		selected_wand_item.setItemMeta(selected_wand_itemMeta);
		return;
	}

	//飛翔物の魔法がmobに当たった関数
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
		if (!(e.getEntity() instanceof LivingEntity)) return; //当たったのが生きているmobではなかったら
		if (e.getEntity() instanceof ArmorStand) return; //家具だったら

		//魔法ではなかったら
		Entity projectiles = e.getDamager();
		if (!isMagicProjectiles(projectiles)) return;

		//メタデータから魔法の飛翔物の攻撃力を取り出す処理
		MetadataValue projectilesMeta = getMetaValue(projectiles.getMetadata(ConfigManager.MAGIC_KEY));
		int projectiles_damage = (int)projectilesMeta.value();

		if (projectiles instanceof Snowball) {
			Snowball snowball_entity = (Snowball)projectiles;
			if(snowball_entity.getShooter() instanceof Player) {
				Player p = (Player)snowball_entity.getShooter();
				if (!(e.getEntity() instanceof Monster)) return;
				((LivingEntity) e.getEntity()).damage(projectiles_damage, p);
			}
        }
		return;
	}

	//メタデータから該当するプラグインの値を取り出す関数
	private MetadataValue getMetaValue(List<MetadataValue> values) {
		for (MetadataValue v : values) {
			// 名前を比較して同じプラグインか確認
			if (v.getOwningPlugin().getName().equals(plugin.getName())) return v;
		}

		return null;
	}

	//魔法による飛翔物かどうか確認する関数
	private boolean isMagicProjectiles(Entity entity) {
		return entity.hasMetadata(ConfigManager.MAGIC_KEY);
	}

	//ItemStackが魔法かどうかを判定する関数
	private boolean isMagic(ItemStack user_magic) {
		if(user_magic == null || user_magic.getType() == Material.AIR) return false;

		//魔法、カスタムモデル
		if (!user_magic.getType().equals(ConfigManager.MAGIC_BOOK) || !user_magic.getItemMeta().hasCustomModelData()) return false;
		return true;
	}

	//ItemStackが魔法の杖かどうかを判定し、そのタイプを返す
	private int MagicWandtype(ItemStack user_magicwand) {
		if(user_magicwand == null || user_magicwand.getType() == Material.AIR) return -1;

		if (!user_magicwand.getType().equals(ConfigManager.MAGIC_WAND)) return -1;
		if (user_magicwand.getItemMeta().getCustomModelData() == 1) return 1;
		if (user_magicwand.getItemMeta().getCustomModelData() == 2) return 2;
		return -1;
	}

	//飛翔物を発射する関数
	private void run_projectiles(Player p, String projectiles_entity, int projectiles_amount, int projectiles_damage, int projectiles_velocity, String particle_type, String particle_animation) {
		if (projectiles_entity.equals("Snowball")) {
			for (int projectiles_angle = 0;projectiles_angle < projectiles_amount;projectiles_angle++) {
				Location loc_p = p.getEyeLocation();
				loc_p.setYaw(loc_p.getYaw()+(360.0F/projectiles_amount) * projectiles_angle);
				Snowball snowball = loc_p.getWorld().spawn(loc_p.add(loc_p.getDirection()), Snowball.class);
				snowball.setMetadata(ConfigManager.MAGIC_KEY, new FixedMetadataValue(this.plugin, projectiles_damage));
				snowball.setVelocity(loc_p.getDirection().multiply(projectiles_velocity));
				snowball.setShooter(p);
				if (!particle_type.equals("null")) run_particle_animation(p, particle_animation, particle_type);
			}
		}
		return;
	}

	//particleアニメーションを実行する関数
	private void run_particle_animation(Player p, String particle_animation, String particle_type) {
		if (particle_animation.equals("spiral")) mo.spiral(p, Particle.valueOf(particle_type));
		else if (particle_animation.equals("expand_wave")) mo.expand_wave(p, Particle.valueOf(particle_type));
		return;
	}

	//周囲のmobに直接系の魔法のダメージを与える関数
	private void run_direct_multiple(Player p, int range, int damage, boolean is_thunder, boolean is_explosive, boolean is_fire, int fire_time, String particle_type, String particle_animation) {
		Location loc_mob;
		List<Entity> mob_list = p.getNearbyEntities(range, range, range);
		for (Entity mob : mob_list) {
			if (mob instanceof Monster && !mob.isDead() && !(mob instanceof ArmorStand) && !(mob instanceof Player)) {
				loc_mob = mob.getLocation();
				if (is_thunder == true) mob.getWorld().strikeLightningEffect(loc_mob);//雷を落とす
				if (is_explosive == true) mob.getWorld().createExplosion(loc_mob, 0);//爆発する
				if (is_fire == true) mob.setFireTicks(20*fire_time);//火をつける
				((LivingEntity) mob).damage(damage, p);
				if (!particle_type.equals("null")) run_particle_animation(p, particle_animation, particle_type);
			}
		}
		return;
	}

	//魔法を実行する関数
	private void excute_magic(String user_magic_name, Player p) {
		int range = MagicWorld.config.getInt(user_magic_name+".Effect.range");//魔法の範囲
		int damage = MagicWorld.config.getInt(user_magic_name+".Effect.damage");//魔法の攻撃力
		String target = MagicWorld.config.getString(user_magic_name+".Effect.target");//ターゲットが単体か複数か
		Boolean is_thunder = MagicWorld.config.getBoolean(user_magic_name+".Effect.is_thunder");//雷
		Boolean is_explosive = MagicWorld.config.getBoolean(user_magic_name+".Effect.is_explosive");//爆発
		Boolean is_fire = MagicWorld.config.getBoolean(user_magic_name+".Effect.is_fire");//火
		int fire_time = MagicWorld.config.getInt(user_magic_name+".Effect.fire_time");//燃え続ける時間
		String projectiles_entity = MagicWorld.config.getString(user_magic_name+".Effect.projectiles_entity");//飛翔物の種類
		int projectiles_amount = MagicWorld.config.getInt(user_magic_name+".Effect.projectiles_amount");//飛翔物の個数
		int projectiles_damage = MagicWorld.config.getInt(user_magic_name+".Effect.projectiles_damage");//飛翔物のダメージ
		int projectiles_velocity = MagicWorld.config.getInt(user_magic_name+".Effect.projectiles_velocity");//飛翔物の速度
		String particle_type = MagicWorld.config.getString(user_magic_name+".Visual.particle_type");//パーティクルの種類
		String particle_animation = MagicWorld.config.getString(user_magic_name+".Visual.particle_animation");//パーティクルのアニメーション

		//飛翔物を発射する
		if (!projectiles_entity.equals("null") && projectiles_amount <= 360) {
			run_projectiles(p, projectiles_entity, projectiles_amount, projectiles_damage, projectiles_velocity, particle_type, particle_animation);
		}

		//周囲のmobに直接系の魔法のダメージを与える
		if (target.equals("multiple")) {
			run_direct_multiple(p, range, damage, is_thunder, is_explosive, is_fire, fire_time, particle_type, particle_animation);
		}

		return;
	}
}
