package hamsteryds.darkarena;

import org.bukkit.plugin.java.JavaPlugin;

public class DarkArena extends JavaPlugin{
	public static DarkArena instance;
	@Override public void onEnable() {
		instance=this;
	}
}
