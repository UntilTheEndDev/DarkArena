package hamsteryds.darkarena;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import hamsteryds.darkarena.warlord.papi.PAPIExpansion;
import hamsteryds.darkarena.warlord.runner.WarlordManager;
import hamsteryds.darkarena.warlord.util.WarlordTeam.TeamType;

public class DarkArena extends JavaPlugin{
	public static DarkArena instance;
	@Override public void onEnable() {
		instance=this;
		saveResource("warlord.yml", false); 
		getCommand("warlord").setExecutor(this); 
		new PAPIExpansion().register();
		WarlordManager.loadConfigWarlordArenas();
	}
	@Override 
	public boolean onCommand(CommandSender sender,Command cmd,String label,String[] contents) {
		if(label.equalsIgnoreCase("warlord")) {
			if(sender instanceof Player) {
				if(WarlordManager.joinArena((Player) sender,contents[0],TeamType.valueOf(contents[1]))) {
					sender.sendMessage("加入成功");
				}else {
					sender.sendMessage("加入失败：人满或已经开始");
				}
			}
		}
		return true;
	}
}
