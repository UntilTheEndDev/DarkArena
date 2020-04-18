package hamsteryds.darkarena.warlord.util;

import org.bukkit.Location;

public class WarlordTeam {
	public Location spawnLocation;
	public int currentFlags;
	public int currentScore;
	public Location currentFlagLocation;

	public WarlordTeam(Location spawnLocation, int currentFlags, int currentScore) {
		this.spawnLocation = spawnLocation;
		this.currentFlags = currentFlags;
		this.currentScore = currentScore;
		this.currentFlagLocation = spawnLocation;
	}

	public enum TeamType {
		RED, BLUE;
	}
}
