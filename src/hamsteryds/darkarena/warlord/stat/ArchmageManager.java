package hamsteryds.darkarena.warlord.stat;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import hamsteryds.darkarena.DarkArena;
import me.clip.placeholderapi.PlaceholderAPI;

public class ArchmageManager {
	static class SkillInvHolder implements InventoryHolder {
		public static final SkillInvHolder INSTANCE = new SkillInvHolder();

		@Override
		public Inventory getInventory() {
			// TODO Auto-generated method stub
			return null;
		}
	}

	static class InfoInvHolder implements InventoryHolder {
		public static final InfoInvHolder INSTANCE = new InfoInvHolder();

		@Override
		public Inventory getInventory() {
			// TODO Auto-generated method stub
			return null;
		}
	}

	public static class InventoryListener implements Listener {
		private Set<UUID> openers = new HashSet<UUID>();

		@EventHandler
		public void onOpen(InventoryOpenEvent event) {
			if (event.getInventory().getHolder() instanceof SkillInvHolder
					|| event.getInventory().getHolder() instanceof InfoInvHolder)
				openers.add(event.getPlayer().getUniqueId());
		}

		@EventHandler
		public void onClose(InventoryCloseEvent event) {
			if (event.getInventory().getHolder() instanceof SkillInvHolder
					|| event.getInventory().getHolder() instanceof InfoInvHolder)
				openers.remove(event.getPlayer().getUniqueId());
		}

		@EventHandler
		public void onInteract(InventoryClickEvent event) {
			if (openers.contains(event.getWhoClicked().getUniqueId()))
				event.setCancelled(true);
		}

		@EventHandler
		public void onClick(InventoryClickEvent event) {
			if (event.getClickedInventory() == null)
				return;
			if (event.getClickedInventory().getHolder() instanceof SkillInvHolder) {
				event.setCancelled(true);
				ItemStack item = event.getCurrentItem();
				int slot = event.getSlot();
				Player player = (Player) event.getWhoClicked();
				StatsManager.Player$1 pl = StatsManager.playerDatas.get(player.getUniqueId());
				if (item.getDurability() == 13) {
					List<String> lore = item.getItemMeta().getLore();
					int needMoney = Integer.valueOf(lore.get(8).replace("§7花费：§6", ""));
					pl.money -= needMoney;
					if (slot < 9)
						pl.archmage.sk1Level++;
					if (9 <= slot && slot < 18)
						pl.archmage.sk2Level++;
					if (18 <= slot && slot < 27)
						pl.archmage.sk3Level++;
					if (27 <= slot && slot < 36)
						pl.archmage.sk4Level++;
					if (36 <= slot && slot < 45)
						pl.archmage.sk5Level++;
					player.openInventory(getSkillInventory(player));
				}
				if (slot == 48)
					player.openInventory(getInfoInventory(player));
			}
			if (event.getClickedInventory().getHolder() instanceof InfoInvHolder) {
				event.setCancelled(true);
				Player player = (Player) event.getWhoClicked();
				StatsManager.Player$1 pl = StatsManager.playerDatas.get(player.getUniqueId());
				if (event.getSlot() == 31) {
					event.getWhoClicked().openInventory(getSkillInventory(player));
				}
			}
		}
	}

	public static Inventory getInfoInventory(Player player) {
		Inventory inv = Bukkit.createInventory(InfoInvHolder.INSTANCE, 54, "法师职业信息");
		inv.setContents(modelInfoInventory.getContents());
		for (int slot = 0; slot < 54; slot++) {
			ItemStack item = inv.getItem(slot);
			if (item == null)
				continue;
			if (!item.hasItemMeta())
				continue;
			ItemMeta meta = item.getItemMeta();
			if (meta.hasLore()) {
				List<String> lore = meta.getLore();
				for (int index = 0; index < lore.size(); index++) {
					lore.set(index, PlaceholderAPI.setPlaceholders(player, lore.get(index)));
				}
				meta.setLore(lore);
			}
			item.setItemMeta(meta);
		}
		return inv;
	}

	public static Inventory getSkillInventory(Player player) {
		StatsManager.Player$1 pl = StatsManager.playerDatas.get(player.getUniqueId());
		Inventory inv = Bukkit.createInventory(SkillInvHolder.INSTANCE, 54, "法师技能升级");
		inv.setContents(modelSkillInventory.getContents());
		List<String> infoLore = new ArrayList<String>();
		infoLore.add("§7目前等级：§a%warlord_{archmage_level}%");
		infoLore.add("§7目前选择：§a%warlord_{archmage_trainer}%");
		infoLore.add("§7目前金币：§a%warlord_{money}%");
		for (int slot = 0; slot < 9; slot++) {
			ItemStack item = inv.getItem(slot);
			ItemMeta meta = item.getItemMeta();
			List<String> lore = meta.getLore();
			if (pl.archmage.sk1Level >= slot + 1) {
				item.setDurability((short) 5);
				lore.remove(8);
			} else {
				if (pl.archmage.sk1Level + 1 == slot + 1
						&& pl.money >= Integer.valueOf(item.getItemMeta().getLore().get(8).replace("§7花费：§6", "")))
					item.setDurability((short) 13);
				lore.remove(9);
			}
			int specialIndex = -1;
			switch (pl.archmage.trainer) {
			case BLAZE:
				specialIndex = 2;
				break;
			case ICE:
				specialIndex = 4;
				break;
			case WATER:
				specialIndex = 6;
				break;
			case NULL:
				specialIndex = 2;
				break;
			}
			for (int index = 0; index < lore.size(); index++) {
				String line = lore.get(index);
				if (index == specialIndex) {
					line = line.replace("§7", "§a");
					line = line.replace("{", "§c{");
					line = line.replace("}", "}§a");
				}
				for (String charset : sk1Figures.get(slot + 1).keySet()) {
					line = line.replace(charset,
							String.format("%.2f", sk1Figures.get(slot + 1).get(charset) * 100.0) + "%");
				}
				lore.set(index, line);
			}
			if (pl.archmage.sk1Level == slot + 1) {
				infoLore.add(lore.get(specialIndex));
			}
			for (int index = 0; index < infoLore.size(); index++) {
				infoLore.set(index, PlaceholderAPI.setPlaceholders(player, infoLore.get(index)));
			}
			meta.setLore(lore);
			item.setItemMeta(meta);
		}
		ItemMeta meta = inv.getItem(49).getItemMeta();
		meta.setLore(infoLore);
		inv.getItem(49).setItemMeta(meta);
		return inv;
	}

	public enum ArchmageType {
		BLAZE("火焰法师"), ICE("寒冰法师"), WATER("水系法师"), NULL("无");
		public String name;

		ArchmageType(String name) {
			this.name = name;
		}
	}

	public static Inventory modelInfoInventory = Bukkit.createInventory(InfoInvHolder.INSTANCE, 45, "法师职业信息");
	public static Inventory modelSkillInventory = Bukkit.createInventory(SkillInvHolder.INSTANCE, 54, "法师技能升级");
	public static Inventory modelAttributeInventory = Bukkit.createInventory(SkillInvHolder.INSTANCE, 54, "法师属性升级");
	public static HashMap<Integer, HashMap<String, Double>> sk1Figures = new HashMap<Integer, HashMap<String, Double>>();

	public static void initAll() {
		DarkArena.instance.saveResource("archmage.yml", false);
		initSkFigures();
		initModelInfoInventory();
		initModelSkillInventory();
		Bukkit.getPluginManager().registerEvents(new InventoryListener(), DarkArena.instance);
	}

	private static void initSkFigures() {
		File file = new File(DarkArena.instance.getDataFolder(), "archmage.yml");
		YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
		for (String level : yaml.getKeys(false)) {
			HashMap<String, Double> figure = new HashMap<String, Double>();
			figure.put("{blaze}", yaml.getDouble(level + ".sk1.blaze"));
			figure.put("{ice1}", yaml.getDouble(level + ".sk1.ice1"));
			figure.put("{ice2}", yaml.getDouble(level + ".sk1.ice2"));
			figure.put("{water}", yaml.getDouble(level + ".sk1.water"));
			sk1Figures.put(Integer.valueOf(level.replace("level", "")), figure);
		}
	}

	private static void initModelInfoInventory() {
		ItemStack item = new ItemStack(Material.PAINTING);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§a我的法师信息");
		List<String> lore = new ArrayList<String>();
		lore.add("§7目前等级：§a%warlord_{archmage_level}%");
		lore.add("");
		lore.add("§7累计战绩：§6火焰§7 §l| §5寒冰§7 §l| §b水系§7法师");
		lore.add("§7法师击杀数：§6%warlord_{blaze_kill}%§7 §l| §5%warlord_{ice_kill}%§7 §l| §b%warlord_{water_kill}%");
		lore.add("§7法师总场数：§6%warlord_{blaze_arena}%§7 §l| §5%warlord_{ice_arena}%§7 §l| §b%warlord_{water_arena}%");
		lore.add(
				"§7法师总胜场：§6%warlord_{blaze_victory}%§7 §l| §5%warlord_{ice_victory}%§7 §l| §b%warlord_{water_victory}%");
		lore.add("§7法师总输出：§6%warlord_{blaze_atk}%§7 §l| §5%warlord_{ice_atk}%§7 §l| §b%warlord_{water_atk}%");
		lore.add("§7法师MVP数：§6%warlord_{blaze_mvp}%§7 §l| §5%warlord_{ice_mvp}%§7 §l| §b%warlord_{water_mvp}%");
		meta.setLore(lore);
		item.setItemMeta(meta);
		modelInfoInventory.setItem(13, item);

		item = new ItemStack(Material.POTION);
		meta = item.getItemMeta();
		meta.setDisplayName("§a专精与大师");
		lore = new ArrayList<String>();
		lore.add("§7专精激活-你所驾驭的职业（巨量加成）");
		lore.add("");
		lore.add("§7目前选择的角色：§a%warlord_{archmage_trainer}%");
		lore.add("§7专精角色：§a%warlord_{archmage_mastery}%");
		lore.add("");
		lore.add("§a点击解锁或设置专精");
		meta.setLore(lore);
		item.setItemMeta(meta);
		modelInfoInventory.setItem(29, item);

		item = new ItemStack(Material.DIAMOND_SWORD);
		meta = item.getItemMeta();
		meta.setDisplayName("§a升级法师技能等级");
		lore = new ArrayList<String>();
		lore.add("§7升级以让你的技能变得更厉害");
		lore.add("");
		lore.add("§a点击进入升级技能界面");
		meta.setLore(lore);
		item.setItemMeta(meta);
		modelInfoInventory.setItem(31, item);

		item = new ItemStack(Material.SKULL_ITEM);
		meta = item.getItemMeta();
		meta.setDisplayName("§a升级法师属性等级");
		lore = new ArrayList<String>();
		lore.add("§7升级以让你的属性变得更厉害");
		lore.add("");
		lore.add("§a点击进入升级属性界面");
		meta.setLore(lore);
		item.setItemMeta(meta);
		modelInfoInventory.setItem(33, item);
	}

	private static void initModelSkillInventory() {
		int[] costs = new int[9];
		costs[0] = 1560;
		for (int i = 1; i < 9; i++) {
			costs[i] = (int) (costs[i - 1] * 1.7);
			costs[i] -= costs[i] % 10;
		}
		for (int slot = 0; slot < 9; slot++)
			modelSkillInventory.setItem(slot, getSkItem(slot, costs[slot], "一技能", "§7火焰法师：火球术额外造成{blaze}的伤害",
					"§7寒冰法师：霜冻术额外造成{ice1}的伤害，额外降低{ice2}的移动速度", "§7水系法师：涌泉术额外回复{water}生命值"));
		for (int slot = 0; slot < 9; slot++)
			modelSkillInventory.setItem(slot + 9,
					getSkItem(slot, costs[slot], "二技能", "§7火焰法师：未开放", "§7寒冰法师：未开放", "§7水系法师：未开放"));
		for (int slot = 0; slot < 9; slot++)
			modelSkillInventory.setItem(slot + 18,
					getSkItem(slot, costs[slot], "三技能", "§7火焰法师：未开放", "§7寒冰法师：未开放", "§7水系法师：未开放"));
		for (int slot = 0; slot < 9; slot++)
			modelSkillInventory.setItem(slot + 27,
					getSkItem(slot, costs[slot], "四技能", "§7火焰法师：未开放", "§7寒冰法师：未开放", "§7水系法师：未开放"));
		for (int slot = 0; slot < 9; slot++)
			modelSkillInventory.setItem(slot + 36,
					getSkItem(slot, costs[slot], "终极技能", "§7火焰法师：未开放", "§7寒冰法师：未开放", "§7水系法师：未开放"));
		ItemStack back = new ItemStack(Material.ARROW);
		ItemMeta meta = back.getItemMeta();
		meta.setDisplayName("§6返回上一层");
		back.setItemMeta(meta);
		modelSkillInventory.setItem(48, back);
		ItemStack info = new ItemStack(Material.EMERALD);
		meta = info.getItemMeta();
		meta.setDisplayName("§a个人信息");
		info.setItemMeta(meta);
		modelSkillInventory.setItem(49, info);
	}

	private static ItemStack getSkItem(int level, int cost, String displayName, String blaze, String ice,
			String water) {
		ItemStack item = new ItemStack(Material.WOOL);
		item.setDurability((short) 14);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§c" + displayName + "升级" + intToRoman(level + 1));
		List<String> lore = new ArrayList<String>();
		lore.add("§7升级" + displayName + "等级，也能提高其他专精职业的技能等级。");
		lore.add("");
		lore.add(blaze);
		lore.add("");
		lore.add(ice);
		lore.add("");
		lore.add(water);
		lore.add("");
		lore.add("§7花费：§6" + cost);
		lore.add("§a已激活该等级！");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

	private static String intToRoman(int num) {
		Map<Integer, String> lookup = new LinkedHashMap<>();
		lookup.put(1000, "M");
		lookup.put(900, "CM");
		lookup.put(500, "D");
		lookup.put(400, "CD");
		lookup.put(100, "C");
		lookup.put(90, "XC");
		lookup.put(50, "L");
		lookup.put(40, "XL");
		lookup.put(10, "X");
		lookup.put(9, "IX");
		lookup.put(5, "V");
		lookup.put(4, "IV");
		lookup.put(1, "I");

		StringBuilder res = new StringBuilder();

		for (Integer key : lookup.keySet()) {
			int n = num / key;
			if (n == 0)
				continue;
			for (int i = n; i > 0; i--) {
				res.append(lookup.get(key));
			}
			num -= n * key;
			if (num == 0) {
				break;
			}
		}
		return res.toString();
	}
}
