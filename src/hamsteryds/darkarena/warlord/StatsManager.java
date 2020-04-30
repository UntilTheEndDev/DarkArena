package hamsteryds.darkarena.warlord;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import hamsteryds.darkarena.DarkArena;

public class StatsManager implements Listener {
	public static class Player$1 {
		public int totalMatch;
		public int totalVictory;
		public int totalATK;
		public int totalCure;
		public int totalATKMVP;
		public int totalCureMVP;

		public Player$1(int totalMatch, int totalVictory, int totalATK, int totalCure, int totalATKMVP,
				int totalCureMVP) {
			this.totalMatch = totalMatch; 
			this.totalVictory = totalVictory; 
			this.totalATK = totalATK; 
			this.totalCure = totalCure; 
			this.totalATKMVP = totalATKMVP;
			this.totalCureMVP = totalCureMVP;
		}

	}

	public static HashMap<UUID, Player$1> playerDatas = new HashMap<UUID, Player$1>();

	public StatsManager() {
		for(World world:Bukkit.getWorlds()) 
			for(Player player:world.getPlayers())
				loadData(player);
	}
	
	public static void loadData(Player player) {
		UUID uuid = player.getUniqueId();
		File file = new File(DarkArena.instance.getDataFolder() + "/playerdata/warlord/", uuid.toString() + ".yml");

		YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
		Player$1 data = new Player$1(0, 0, 0, 0, 0, 0);
		if (file.exists())
			data = new Player$1(yaml.getInt("totalMatch"), yaml.getInt("totalVictory"), yaml.getInt("totalATK"),
					yaml.getInt("totalCure"), yaml.getInt("totalATKMVP"), yaml.getInt("totalCureMVP"));
		playerDatas.put(uuid, data);
		saveData(player);
	}

	public static void saveData(Player player) {
		UUID uuid = player.getUniqueId();
		File file = new File(DarkArena.instance.getDataFolder() + "/playerdata/warlord/", uuid.toString() + ".yml");

		YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
		Player$1 data = playerDatas.get(uuid);
		yaml.set("totalMatch", data.totalMatch);
		yaml.set("totalVictory", data.totalVictory);
		yaml.set("totalATK", data.totalATK);
		yaml.set("totalCure", data.totalCure);
		yaml.set("totalATKMVP", data.totalATKMVP);
		yaml.set("totalCureMVP", data.totalCureMVP);

		try {
			yaml.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		loadData(event.getPlayer());
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		loadData(event.getPlayer());
	}

	public static void saveAll() {
		for(World world:Bukkit.getWorlds()) 
			for(Player player:world.getPlayers())
				saveData(player);
	}
}
