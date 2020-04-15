package HamsterYDS.DarkArena.arena.normal.runner;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import HamsterYDS.DarkArena.DarkArena;
import HamsterYDS.DarkArena.arena.normal.util.Arena;

public class ArenaRunner implements Listener{
	public Arena arena;
	public BukkitTask task;
	public ArenaRunner(Arena arena) {
		this.arena=arena;
		Bukkit.getPluginManager().registerEvents(this,DarkArena.instance);
		task=new BukkitRunnable() {
			long counter=0;
			@Override
			public void run() {
				counter++;
				
			}
		}.runTaskTimer(DarkArena.instance, 0L, 1L);
	}
	public void stopRunner() {
		this.task.cancel();
		Bukkit.getPluginManager();
	}
}
