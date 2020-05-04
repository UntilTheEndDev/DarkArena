package hamsteryds.darkarena.warlord.stat.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.clip.placeholderapi.PlaceholderAPI;

public class ArchmageInfo {
	public static Inventory modelInfoInventory = Bukkit.createInventory(ArchmageInfoInvHolder.INSTANCE, 45, "法师职业信息");
	public static class ArchmageInfoInvHolder implements InventoryHolder {
		public static final ArchmageInfoInvHolder INSTANCE = new ArchmageInfoInvHolder();

		@Override
		public Inventory getInventory() {
			return null;
		}
	}
	
	public static Inventory getInfoInventory(Player player) {
		Inventory inv = Bukkit.createInventory(ArchmageInfoInvHolder.INSTANCE, 54, "法师职业信息");
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
	
	public static void initModelInfoInventory() {
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
}
