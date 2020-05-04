package hamsteryds.darkarena.warlord.stat;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import hamsteryds.darkarena.DarkArena;
import hamsteryds.darkarena.warlord.stat.gui.ArchmageInfo;
import hamsteryds.darkarena.warlord.stat.gui.ArchmageSkill;
import hamsteryds.darkarena.warlord.stat.gui.WarlordInvHolder;

public class ArchmageManager {
	public static class InventoryListener implements Listener {
		private Set<UUID> openers = new HashSet<UUID>();

		@EventHandler
		public void onOpen(InventoryOpenEvent event) {
			if (event.getInventory().getHolder() instanceof WarlordInvHolder.ArchmageSkillInvHolder
					|| event.getInventory().getHolder() instanceof WarlordInvHolder.ArchmageInfoInvHolder)
				openers.add(event.getPlayer().getUniqueId());
		}

		@EventHandler
		public void onClose(InventoryCloseEvent event) {
			if (event.getInventory().getHolder() instanceof WarlordInvHolder.ArchmageSkillInvHolder
					|| event.getInventory().getHolder() instanceof WarlordInvHolder.ArchmageInfoInvHolder)
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
			if (event.getClickedInventory().getHolder() instanceof WarlordInvHolder.ArchmageSkillInvHolder) {
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
					player.openInventory(ArchmageSkill.getSkillInventory(player));
				}
				if (slot == 48)
					player.openInventory(ArchmageInfo.getInfoInventory(player));
			}
			if (event.getClickedInventory().getHolder() instanceof WarlordInvHolder.ArchmageInfoInvHolder) {
				event.setCancelled(true);
				Player player = (Player) event.getWhoClicked();
				StatsManager.Player$1 pl = StatsManager.playerDatas.get(player.getUniqueId());
				if (event.getSlot() == 31) {
					event.getWhoClicked().openInventory(ArchmageSkill.getSkillInventory(player));
				}
			}
		}
	}

	public enum ArchmageType {
		BLAZE("火焰法师"), ICE("寒冰法师"), WATER("水系法师"), NULL("无");
		public String name;

		ArchmageType(String name) {
			this.name = name;
		}
	}

	public static void initAll() {
		DarkArena.instance.saveResource("archmage.yml", false);
		ArchmageInfo.initModelInfoInventory();
		ArchmageSkill.initModelSkillInventory();
		Bukkit.getPluginManager().registerEvents(new InventoryListener(), DarkArena.instance);
	}
}
