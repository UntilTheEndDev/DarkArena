package hamsteryds.darkarena.warlord.runner;

import java.util.HashMap;
import java.util.List;

import hamsteryds.darkarena.warlord.util.WarlordArena;
import hamsteryds.darkarena.warlord.util.WarlordPlayer;
import hamsteryds.darkarena.warlord.util.WarlordTeam;

public class WarlordManager {
	public static HashMap<String,WarlordArena> arenas=new HashMap<String,WarlordArena>();
	public static HashMap<String,List<WarlordPlayer>> players=new HashMap<String,List<WarlordPlayer>>();
	public static HashMap<String,List<WarlordTeam>> teams=new HashMap<String,List<WarlordTeam>>();
	
	public static void loadConfigWarlordArenas() {
		
	}
}
