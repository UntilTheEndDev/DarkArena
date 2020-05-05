package hamsteryds.darkarena;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import hamsteryds.darkarena.warlord.PAPIExpansion;
import hamsteryds.darkarena.warlord.WarlordManager;
import hamsteryds.darkarena.warlord.item.KitsManager;
import hamsteryds.darkarena.warlord.listener.PlayerListener;
import hamsteryds.darkarena.warlord.stat.ArchmageManager;
import hamsteryds.darkarena.warlord.stat.StatsManager;
import hamsteryds.darkarena.warlord.stat.container.Archmage.ChangeType;
import hamsteryds.darkarena.warlord.stat.container.Archmage.VariableType;
import hamsteryds.darkarena.warlord.stat.gui.ArchmageChoose;
import hamsteryds.darkarena.warlord.stat.gui.ArchmageInfo;
import hamsteryds.darkarena.warlord.stat.gui.ArchmageSkill;
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
		
		KitsManager.initAll();
		
		checkUpdate();
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
				if(contents[0].equals("archmage")) {
					StatsManager.Player$1 pl=StatsManager.playerDatas.get(player.getUniqueId());
					player.openInventory(ArchmageInfo.getInfoInventory(player));
				}
				if(contents[0].equals("archmagelevel")) {
					StatsManager.Player$1 pl=StatsManager.playerDatas.get(player.getUniqueId());
					player.openInventory(ArchmageSkill.getSkillInventory(player));
				}
				if(contents[0].equals("archmagechoose")) {
					StatsManager.Player$1 pl=StatsManager.playerDatas.get(player.getUniqueId());
					player.openInventory(ArchmageChoose.getChooseInventory(player));
				}
				if(contents[0].equals("money")) {
					Player changee=Bukkit.getPlayer(contents[2]);
					if(changee==null)
						return true;
					StatsManager.Player$1 pl=StatsManager.playerDatas.get(changee.getUniqueId());
					switch(contents[1]) {
					case "give":
						pl.money+=Integer.valueOf(contents[3]);
						player.sendMessage("§6[战争领主]§r成功给予玩家§a"+contents[2]+"§r金币§a"+contents[3]+"§r个");
						changee.sendMessage("§6[战争领主]§r您的账户上添加了§a"+contents[3]+"§r个金币");
						break;
					case "take":
						pl.money-=Integer.valueOf(contents[3]);
						player.sendMessage("§6[战争领主]§r成功扣除玩家§a"+contents[2]+"§r金币§a"+contents[3]+"§r个");
						changee.sendMessage("§6[战争领主]§r您的账户上扣除了§a"+contents[3]+"§r个金币");
						break;
					case "set":
						pl.money=Integer.valueOf(contents[3]);
						player.sendMessage("§6[战争领主]§r成功设置玩家§a"+contents[2]+"§r金币为§a"+contents[3]+"§r个");
						changee.sendMessage("§6[战争领主]§r您的账户被设置为了§a"+contents[3]+"§r个金币");
						break;
					default:
						break;
					}
				}
				if(contents[0].equals("modify")) {
					Player changee=Bukkit.getPlayer(contents[1]);
					if(changee==null)
						return true;
					StatsManager.Player$1 pl=StatsManager.playerDatas.get(changee.getUniqueId());
					pl.archmage.changeLevel(VariableType.valueOf(contents[2]),ChangeType.valueOf(contents[3]),Integer.valueOf(contents[4]));
					player.sendMessage("§6[战争领主]§r修改玩家等级成功"); 
				}
			}
		}
		return true;
	}
	String latestVersion;
    boolean isLatest = true;
	public void checkUpdate() {
        new BukkitRunnable() {
            public void run() {
                latestVersion = getLatestVersion();
                if (latestVersion == null) {
                    return;
                }
                if (latestVersion.equalsIgnoreCase(getDescription().getVersion())) {
                    getServer().getConsoleSender().sendMessage("战争领主-插件已经是最新版啦~");
                } else {
                	getServer().getConsoleSender().sendMessage("战争领主-插件不是最新稳定版，请更换~");
                    isLatest = false;
                }
            }
        }.runTaskAsynchronously(this);
    }

    public static String getLatestVersion() {
        HttpURLConnection connection = null;
        try {
            int timeout = 5000;
            URL url = new URL("https://untiltheend.coding.net/p/DarkArena/d/DarkArena/git/raw/master/DAversion.txt");
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(timeout);
            InputStream inStream = connection.getInputStream();
            final StringBuilder builder = new StringBuilder(255);
            int byteRead;
            while ((byteRead = inStream.read()) != -1) {
                builder.append((char) byteRead);
            }
            return builder.toString();
        } catch (Exception exception) {
            DarkArena.instance.getLogger().log(Level.WARNING, "Failed to get update info.", exception);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return null; //这个return代表获取不到版本时返回的字符串，当然你可以改别的
    }
}
