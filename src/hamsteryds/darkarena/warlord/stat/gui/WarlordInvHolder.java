package hamsteryds.darkarena.warlord.stat.gui;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class WarlordInvHolder {
	public static class ArchmageChooseInvHolder implements InventoryHolder {
		public static final ArchmageChooseInvHolder INSTANCE = new ArchmageChooseInvHolder();
	
		@Override
		public Inventory getInventory() {
			return null;
		}
	}
	
	public static class ArchmageAttributeInvHolder implements InventoryHolder {
		public static final ArchmageAttributeInvHolder INSTANCE = new ArchmageAttributeInvHolder();
	
		@Override
		public Inventory getInventory() {
			return null;
		}
	}

	public static class ArchmageInfoInvHolder implements InventoryHolder {
		public static final ArchmageInfoInvHolder INSTANCE = new ArchmageInfoInvHolder();
	
		@Override
		public Inventory getInventory() {
			return null;
		}
	}

	public static class ArchmageSkillInvHolder implements InventoryHolder {
		public static final ArchmageSkillInvHolder INSTANCE = new ArchmageSkillInvHolder();
	
		@Override
		public Inventory getInventory() {
			return null;
		}
	}

}
