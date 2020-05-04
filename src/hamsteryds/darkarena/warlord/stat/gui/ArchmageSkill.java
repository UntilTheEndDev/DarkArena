package hamsteryds.darkarena.warlord.stat.gui;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import hamsteryds.darkarena.DarkArena;
import hamsteryds.darkarena.warlord.stat.StatsManager;
import me.clip.placeholderapi.PlaceholderAPI;

public class ArchmageSkill {
	public static Inventory modelSkillInventory = Bukkit.createInventory(ArchmageSkillInvHolder.INSTANCE, 54, "法师技能升级");
	public static HashMap<Integer, HashMap<String, Double>> sk1Figures = new HashMap<Integer, HashMap<String, Double>>();

	public static class ArchmageSkillInvHolder implements InventoryHolder {
		public static final ArchmageSkillInvHolder INSTANCE = new ArchmageSkillInvHolder();

		@Override
		public Inventory getInventory() {
			return null;
		}
	}

	public static void initSkFigures() {
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

	public static void initModelSkillInventory() {
		initSkFigures();
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

	public static Inventory getSkillInventory(Player player) {
		StatsManager.Player$1 pl = StatsManager.playerDatas.get(player.getUniqueId());
		Inventory inv = Bukkit.createInventory(ArchmageSkillInvHolder.INSTANCE, 54, "法师技能升级");
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

	public static ItemStack getSkItem(int level, int cost, String displayName, String blaze, String ice, String water) {
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

	public static String intToRoman(int num) {
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
