package hamsteryds.darkarena.warlord.stat.gui;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

import hamsteryds.darkarena.warlord.stat.gui.ArchmageSkill.ArchmageSkillInvHolder;

public class ArchmageAttribute {
	public static Inventory modelAttributeInventory = Bukkit.createInventory(ArchmageSkillInvHolder.INSTANCE, 54, "法师属性升级");
}
