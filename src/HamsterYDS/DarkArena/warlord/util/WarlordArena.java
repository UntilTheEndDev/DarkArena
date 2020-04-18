package hamsteryds.darkarena.warlord.util;

public class WarlordArena {
	public String arenaId;
	public boolean isWaiting;
	public boolean isRunning;
	public long lastTime;
	public WarlordArena(String arenaId, boolean isWaiting, boolean isRunning, long lastTime) {
		this.arenaId = arenaId;
		this.isWaiting = isWaiting;
		this.isRunning = isRunning;
	}
}
