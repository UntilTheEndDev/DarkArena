package hamsteryds.darkarena.warlord.stat;

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
		public int totalKill;
		public int currentPlayStreak;
		public int highestPlayStreak;
		public int money;
		public ArchmageManager archmage;

		public Player$1(int totalMatch, int totalVictory, int totalATK, int totalCure, int totalATKMVP,
				int totalCureMVP, int totalKill, int currentPlayStreak, int highestPlayStreak, int money,
				ArchmageManager archmage) {
			this.totalMatch = totalMatch;
			this.totalVictory = totalVictory;
			this.totalATK = totalATK;
			this.totalCure = totalCure;
			this.totalATKMVP = totalATKMVP;
			this.totalCureMVP = totalCureMVP;
			this.totalKill = totalKill;
			this.currentPlayStreak = currentPlayStreak;
			this.highestPlayStreak = highestPlayStreak;
			this.money = money;
			this.archmage = archmage;
		}

	}

	public static HashMap<UUID, Player$1> playerDatas = new HashMap<UUID, Player$1>();

	public StatsManager() {
		for (World world : Bukkit.getWorlds())
			for (Player player : world.getPlayers())
				loadData(player);
	}

	public static void loadData(Player player) {
		UUID uuid = player.getUniqueId();
		File file = new File(DarkArena.instance.getDataFolder() + "/playerdata/warlord/", uuid.toString() + ".yml");

		YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
		Player$1 data = new Player$1(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, new ArchmageManager());
		if (file.exists())
			data = new Player$1(yaml.getInt("totalMatch"), yaml.getInt("totalVictory"), yaml.getInt("totalATK"),
					yaml.getInt("totalCure"), yaml.getInt("totalATKMVP"), yaml.getInt("totalCureMVP"),
					yaml.getInt("totalKill"), yaml.getInt("currentPlayStreak"), yaml.getInt("highestPlayStreak"),
					yaml.getInt("money"), new ArchmageManager(
							yaml.getInt("archmage.sk1Level"), yaml.getInt("archmage.sk2Level"), yaml.getInt("archmage.sk3Level"),
							yaml.getInt("archmage.sk4Level"), yaml.getInt("archmage.sk5Level"), yaml.getInt("archmage.attrib1Level"),
							yaml.getInt("archmage.attrib2Level"), yaml.getInt("archmage.attrib3Level"), yaml.getInt("archmage.attrib4Level"),
							yaml.getInt("archmage.attrib5Level"), yaml.getString("archmage.trainer"), yaml.getString("archmage.mastery"),
							(Integer[])yaml.getIntegerList("archmage.blazeStats").toArray(),
							(Integer[])yaml.getIntegerList("archmage.iceStats").toArray(),
							(Integer[])yaml.getIntegerList("archmage.waterStats").toArray()));
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
		yaml.set("totalKill", data.totalKill);
		yaml.set("currentPlayStreak", data.currentPlayStreak);
		yaml.set("highestPlayStreak", data.highestPlayStreak);
		yaml.set("money", data.money);

		yaml.set("archmage.sk1Level", data.archmage.sk1Level);
		yaml.set("archmage.sk2Level", data.archmage.sk2Level);
		yaml.set("archmage.sk3Level", data.archmage.sk3Level);
		yaml.set("archmage.sk4Level", data.archmage.sk4Level);
		yaml.set("archmage.sk5Level", data.archmage.sk5Level);
		yaml.set("archmage.attrib1Level", data.archmage.attrib1Level);
		yaml.set("archmage.attrib2Level", data.archmage.attrib2Level);
		yaml.set("archmage.attrib3Level", data.archmage.attrib3Level);
		yaml.set("archmage.attrib4Level", data.archmage.attrib4Level);
		yaml.set("archmage.attrib5Level", data.archmage.attrib5Level);
		yaml.set("archmage.trainer", data.archmage.trainer.toString());
		yaml.set("archmage.mastery", data.archmage.mastery.toString());
		
		yaml.set("archmage.blazeStats", data.archmage.blazeStats);
		yaml.set("archmage.iceStats", data.archmage.iceStats);
		yaml.set("archmage.waterStats", data.archmage.waterStats);

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
		for (World world : Bukkit.getWorlds())
			for (Player player : world.getPlayers())
				saveData(player);
	}
}
