package hamsteryds.darkarena.warlord.util;

import java.util.UUID;

import org.bukkit.Location;

public class WarlordTeam {
	public Location spawnLocation;
	public int currentScore;
	public Location currentFlagLocation;
	public int currentPlayers;
	public boolean isRespawningFlag;
	public UUID whoIsCarrying;

	public WarlordTeam(Location spawnLocation, int currentScore) {
		this.spawnLocation = spawnLocation;
		this.currentScore = currentScore;
		this.currentFlagLocation = spawnLocation;
		this.currentPlayers = 0;
		this.isRespawningFlag = false;
		this.whoIsCarrying = null;
	}

	public enum TeamType {
		RED, BLUE;
	}
}
