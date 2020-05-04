package hamsteryds.darkarena.warlord.stat.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.clip.placeholderapi.PlaceholderAPI;

public class ArchmageChoose {
	public static Inventory modelChooseInventory = Bukkit
			.createInventory(WarlordInvHolder.ArchmageChooseInvHolder.INSTANCE, 45, "法师职业选择");

	public static void initModelChooseInventory() {
		ItemStack item = new ItemStack(Material.BLAZE_POWDER);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§6火焰法师");
		List<String> lore = new ArrayList<String>();
		lore.add("§7炽热的魔法，极高的输出……");
		lore.add("§7定位：§a输出型职业");
		lore.add("");
		lore.add("§7累计胜利：§a%warlord_{blaze_victory}% §7/ §a%warlord_{blaze_arena}%");
		lore.add("§7累计击杀：§a%warlord_{blaze_kill}%");
		lore.add("§7累计输出：§a%warlord_{blaze_atk}%");
		lore.add("§7累计最佳：§a%warlord_{blaze_mvp}%");
		lore.add("");
		lore.add("§a点击选择该职业作为游戏使用职业！");
		meta.setLore(lore);
		item.setItemMeta(meta);
		modelChooseInventory.setItem(11, item);
		
		item = new ItemStack(Material.PRISMARINE_SHARD);
		meta = item.getItemMeta();
		meta.setDisplayName("§3寒冰法师");
		lore = new ArrayList<String>();
		lore.add("§7寒气透骨，中枢力量……");
		lore.add("§7定位：§a夺旗型职业");
		lore.add("");
		lore.add("§7累计胜利：§a%warlord_{ice_victory}% §7/ §a%warlord_{ice_arena}%");
		lore.add("§7累计击杀：§a%warlord_{ice_kill}%");
		lore.add("§7累计输出：§a%warlord_{ice_atk}%");
		lore.add("§7累计最佳：§a%warlord_{ice_mvp}%");
		lore.add("");
		lore.add("§a点击选择该职业作为游戏使用职业！");
		meta.setLore(lore);
		item.setItemMeta(meta);
		modelChooseInventory.setItem(13, item);
		
		item = new ItemStack(Material.GHAST_TEAR);
		meta = item.getItemMeta();
		meta.setDisplayName("§b水系法师");
		lore = new ArrayList<String>();
		lore.add("§7来自深海，治疗兼输出……");
		lore.add("§7定位：§a辅助型职业");
		lore.add("");
		lore.add("§7累计胜利：§a%warlord_{water_victory}% §7/ §a%warlord_{water_arena}%");
		lore.add("§7累计击杀：§a%warlord_{water_kill}%");
		lore.add("§7累计输出：§a%warlord_{water_atk}%");
		lore.add("§7累计最佳：§a%warlord_{water_mvp}%");
		lore.add("");
		lore.add("§a点击选择该职业作为游戏使用职业！");
		meta.setLore(lore);
		item.setItemMeta(meta);
		modelChooseInventory.setItem(15, item);
	}

	public static Inventory getChooseInventory(Player player) {
		Inventory inv = Bukkit.createInventory(WarlordInvHolder.ArchmageChooseInvHolder.INSTANCE, 45, "法师职业选择");
		inv.setContents(modelChooseInventory.getContents());
		for (int slot = 0; slot < inv.getSize(); slot++) {
			ItemStack item = inv.getItem(slot);
			if (item == null)
				continue;
			ItemMeta meta = item.getItemMeta();
			if (meta == null)
				continue;
			if (meta.hasDisplayName()){
				meta.setDisplayName(PlaceholderAPI.setPlaceholders(player, meta.getDisplayName()));
			}
			if (meta.hasLore())
				meta.setLore(PlaceholderAPI.setPlaceholders(player, meta.getLore()));
			item.setItemMeta(meta);
		}
		return inv;
	}
}
