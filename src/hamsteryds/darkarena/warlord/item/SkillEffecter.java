package hamsteryds.darkarena.warlord.item;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import hamsteryds.darkarena.DarkArena;
import hamsteryds.darkarena.warlord.WarlordManager;
import hamsteryds.darkarena.warlord.item.skill.NormalSkill;

public class SkillEffecter implements Listener{
	private String arenaId;
	public static HashMap<String,NormalSkill> skills=new HashMap<String,NormalSkill>();
	
	public SkillEffecter(String arenaId) {
		this.arenaId=arenaId;
		Bukkit.getServer().getPluginManager().registerEvents(this,DarkArena.instance);
	}
	
	@EventHandler
	public void onClick(PlayerInteractEvent event) {
		if (!WarlordManager.arenas.get(this.arenaId).isRunning)
			return;
		if (!event.hasItem())
			return;
		ItemStack item = event.getItem();
		if (item.hasItemMeta())
			if (item.getItemMeta().hasDisplayName()){
				String message=effect(item, event.getPlayer());
				if(!message.equalsIgnoreCase(""))
					event.getPlayer().sendMessage(message);
			}
	}
	
	
	public String effect(ItemStack item, Player player) {
		Material material = item.getType();
		if (material == ItemFactory.fromLegacy(Material.SULPHUR))
			return "§6[战争领主]§r技能冷却中……还剩下"+item.getAmount()+"秒";
		String itemName = item.getItemMeta().getDisplayName();
		if(!skills.containsKey(itemName))
			return "";
		NormalSkill skill=skills.get(itemName);
		if(WarlordManager.players.get(this.arenaId).get(player.getUniqueId()).magicka<skill.minusPP)
			return "§6[战争领主]§r魔法值不足……需要魔法值："+skill.minusPP+"点";
		setCooldown(item,skill.cooldown);
		switch(skill.name) {
		case "时空断裂": break;
		case "奥术护盾": break;
		case "火球术": break;
		case "霜冻术": break;
		case "火焰喷发": break;
		case "霜冻术": break;
		
		default: break;
		}
		
		if (itemName.contains("时空断裂")) {
			Location loc = player.getLocation().clone();
			new BukkitRunnable() {
				int counter = 0;

				@Override
				public void run() {
					loc.getWorld().spawnParticle(Particle.CLOUD, loc, 10);
					counter++;
					if (counter >= 4 * 5) {
						loc.getWorld().spawnParticle(Particle.END_ROD, loc, 2);
						loc.getWorld().spawnParticle(Particle.END_ROD, player.getLocation(), 2);
						player.teleport(loc);
						player.setHealth((player.getHealth() + player.getMaxHealth() * 0.25) >= player.getMaxHealth()
								? player.getMaxHealth()
								: (player.getHealth() + player.getMaxHealth() * 0.25));
						player.sendMessage("§6[战争领主]§r已经传送回原地并恢复大量血量！");
						cancel();
						return;
					}
				}
			}.runTaskTimer(DarkArena.instance, 0L, 5L);
		}
		
		if(itemName.contains("奥术护盾")) {
		}
		
		return "";
	}

	private static void setCooldown(ItemStack item, int cd) {
		Material material = item.getType();
		new BukkitRunnable() {
			int counter = 0;

			@Override
			public void run() {
				item.setType(Material.SULPHUR);
				item.setAmount(cd - counter);
				if (counter++ >= cd) {
					item.setType(material);
					cancel();
					return;
				}
			}
		}.runTaskTimer(DarkArena.instance, 0L, 20L);
	}
}
