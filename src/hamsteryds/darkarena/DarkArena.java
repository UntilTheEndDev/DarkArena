package hamsteryds.darkarena;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import hamsteryds.darkarena.warlord.PAPIExpansion;
import hamsteryds.darkarena.warlord.WarlordManager;
import hamsteryds.darkarena.warlord.listener.PlayerListener;
import hamsteryds.darkarena.warlord.stat.ArchmageManager;
import hamsteryds.darkarena.warlord.stat.StatsManager;
import hamsteryds.darkarena.warlord.util.WarlordTeam.TeamType;

public class DarkArena extends JavaPlugin {
	public static DarkArena instance;

	@Override
	public void onEnable() {
		instance = this;
		
		getCommand("warlord").setExecutor(this);
		
		WarlordManager.loadConfigWarlordArenas();
		
		this.getServer().getPluginManager().registerEvents(new StatsManager(),this);
		
		new PAPIExpansion().register();
		
		ArchmageManager.initAll();
	}

	@Override
	public void onDisable() {
		StatsManager.saveAll();
	}
	
	@Override 
	public boolean onCommand(CommandSender sender,Command cmd,String label,String[] contents) {
		if(label.equalsIgnoreCase("warlord")) {
			if(sender instanceof Player) {
				Player player=(Player) sender;
				if(contents[0].equals("join")){
					if(!WarlordManager.arenas.containsKey(contents[1])){
						player.sendMessage("§6[战争领主]§r无法加入游戏：没有此赛场");
						return true;
					}
					if(!(contents[2].equalsIgnoreCase("RED")||contents[2].equalsIgnoreCase("BLUE"))){
						player.sendMessage("§6[战争领主]§r无法加入游戏：没有此队伍");
						return true;
					}
					switch(WarlordManager.arenas.get(contents[1]).addPlayer(player,TeamType.valueOf(contents[2]))) {
					case "STARTED":
						player.sendMessage("§6[战争领主]§r无法加入游戏：已经开始");
						break;
					case "ARENAFULL":
						player.sendMessage("§6[战争领主]§r无法加入游戏：人满");
						break;
					case "SUCCESSFULLY JOIN ANOTHER TEAM":
						player.sendMessage("§6[战争领主]§r加入游戏成功：该队伍人满，自动加入另一队伍");
						break;
					case "SUCCESSFULLY JOIN CORRECT TEAM":
						player.sendMessage("§6[战争领主]§r加入游戏成功");
						break;
					}
				}
				if(contents[0].equals("rejoin")) {
					if(PlayerListener.rejoinBeforeEnding(player, PlayerListener.rejoinDatas.get(player.getUniqueId()))) {
						player.sendMessage("§6[战争领主]§r重新加入游戏成功");
					}else {
						player.sendMessage("§6[战争领主]§r重新加入游戏失败");
					}
				}
				if(contents[0].equals("archmagelevel")) {
					StatsManager.Player$1 pl=StatsManager.playerDatas.get(player.getUniqueId());
					player.openInventory(pl.archmage.getSkillInventory(pl));
				}
			}
		}
		return true;
	}
}
