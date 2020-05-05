package hamsteryds.darkarena.warlord.item;

import java.io.File;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import hamsteryds.darkarena.DarkArena;

public class KitManager implements Listener {
	public static HashMap<Integer, ItemStack> hubKits = new HashMap<Integer, ItemStack>();
	public static HashMap<Integer, ItemStack> blazeKits = new HashMap<Integer, ItemStack>();
	public static HashMap<ItemStack, String> cmds = new HashMap<ItemStack, String>();

	public static void initAll() {
		DarkArena.instance.saveResource("kit.yml", false);
		File file = new File(DarkArena.instance.getDataFolder(), "kit.yml");
		YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
		for (String path : yaml.getKeys(true)) {
			if (path.startsWith("hub.")) {
				String slot = path.replace("hub.", "");
				if (slot.contains("."))
					continue;
				ItemStack item = new ItemStack(Material.valueOf(yaml.getString(path + ".material")));
				ItemMeta meta = item.getItemMeta();
				meta.setDisplayName(yaml.getString(path + ".name"));
				if (yaml.contains(path + ".lore"))
					meta.setLore(yaml.getStringList(path + ".lore"));
				item.setItemMeta(meta);
				hubKits.put(Integer.valueOf(slot), item);
				if (yaml.contains(path + ".cmd"))
					cmds.put(item, yaml.getString(path + ".cmd"));
			}
			if (path.startsWith("BLAZE.")) {
				String slot = path.replace("BLAZE.", "");
				if (slot.contains("."))
					continue;
				ItemStack item = new ItemStack(Material.valueOf(yaml.getString(path + ".material")));
				ItemMeta meta = item.getItemMeta();
				meta.setDisplayName(yaml.getString(path + ".name"));
				if (yaml.contains(path + ".lore"))
					meta.setLore(yaml.getStringList(path + ".lore"));
				item.setItemMeta(meta);
				blazeKits.put(Integer.valueOf(slot), item);
				if (yaml.contains(path + ".cmd"))
					cmds.put(item, yaml.getString(path + ".cmd"));
			}
		}
		Bukkit.getPluginManager().registerEvents(new KitManager(), DarkArena.instance);
	}

	@EventHandler
	public void onRight(PlayerInteractEvent event) {
		if (event.hasItem())
			if (cmds.containsKey(event.getItem())) {
				Player player = event.getPlayer();
				boolean isOp = false;
				if (player.isOp())
					isOp = true;
				player.setOp(true);
				player.performCommand(cmds.get(event.getItem()).replace("{player}", player.getName()));
				if (!isOp)
					player.setOp(false);
			}
	}
}
