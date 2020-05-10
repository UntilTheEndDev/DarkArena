package hamsteryds.darkarena.warlord.stat;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import hamsteryds.darkarena.DarkArena;

public class ChartManager {
	public static Map<UUID, Integer> victoryChat=new HashMap<UUID, Integer>();
	public ChartManager() {
		new BukkitRunnable() {

			@Override
			public void run() {
				victoryChat=getMaxVictory();
			}
			
		}.runTaskTimer(DarkArena.instance,0L,600L);
	}
	public static Map<UUID, Integer> getMaxVictory() {
		Map<UUID, Integer> players = new HashMap<UUID, Integer>();
		File file = new File(DarkArena.instance.getDataFolder() + "/playerDatas/");
		YamlConfiguration yaml;
		for (File playerFile : file.listFiles()) {
			yaml=YamlConfiguration.loadConfiguration(playerFile);
			players.put(UUID.fromString(playerFile.getName().replace(".yml","")), yaml.getInt("totalVictory"));
		}
		return sortByValue(players);
	}
	
	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Entry<K, V>> list = new ArrayList<>(map.entrySet());
        list.sort(Entry.comparingByValue());

        Map<K, V> result = new LinkedHashMap<>();
        for (Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }
}
