package hamsteryds.darkarena;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import hamsteryds.darkarena.warlord.PAPIExpansion;
import hamsteryds.darkarena.warlord.WarlordManager;
import hamsteryds.darkarena.warlord.listener.PlayerListener;
import hamsteryds.darkarena.warlord.util.WarlordTeam.TeamType;

public class DarkArena extends JavaPlugin {
	public static DarkArena instance;

	@Override
	public void onEnable() {
		instance = this;
		saveResource("warlord.yml", false);
		getCommand("warlord").setExecutor(this);
		new PAPIExpansion().register();
		WarlordManager.loadConfigWarlordArenas();
	}

	@Override 
	public boolean onCommand(CommandSender sender,Command cmd,String label,String[] contents) {
		if(label.equalsIgnoreCase("warlord")) {
			if(sender instanceof Player) {
				if(contents[0].equals("join"))
					switch(WarlordManager.arenas.get(contents[1]).addPlayer((Player) sender,TeamType.valueOf(contents[2]))) {
					case "STARTED":
						sender.sendMessage("§6[战争领主]§r无法加入游戏：已经开始");
						break;
					case "ARENAFULL":
						sender.sendMessage("§6[战争领主]§r无法加入游戏：人满");
						break;
					case "SUCCESSFULLY JOIN ANOTHER TEAM":
						sender.sendMessage("§6[战争领主]§r加入游戏成功：该队伍人满，自动加入另一队伍");
						break;
					case "SUCCESSFULLY JOIN CORRECT TEAM":
						sender.sendMessage("§6[战争领主]§r加入游戏成功");
						break;
					}
				if(contents[0].equals("rejoin")) {
					if(PlayerListener.rejoinBeforeEnding((Player) sender, PlayerListener.rejoinDatas.get(((Player) sender).getUniqueId()))) {
						sender.sendMessage("§6[战争领主]§r重新加入游戏成功");
					}else {
						sender.sendMessage("§6[战争领主]§r重新加入游戏失败");
					}
				}
			}
		}
		return true;
	}
}
