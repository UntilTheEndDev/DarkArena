package hamsteryds.darkarena.warlord.util;

import org.bukkit.Location;

public class WarlordTeam {
	public Location spawnLocation;
	public int currentScore;
	public Location currentFlagLocation;
	public int currentPlayers;

	public WarlordTeam(Location spawnLocation, int currentScore) {
		this.spawnLocation = spawnLocation;
		this.currentScore = currentScore;
		this.currentFlagLocation = spawnLocation;
		this.currentPlayers = 0;
	}

	public enum TeamType {
		RED, BLUE;
	}
}
