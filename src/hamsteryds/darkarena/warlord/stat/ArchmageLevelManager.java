package hamsteryds.darkarena.warlord.stat;

import org.bukkit.inventory.Inventory;

public class ArchmageLevelManager {
	public int sk1Level = 0;
	public int sk2Level = 0;
	public int sk3Level = 0;
	public int sk4Level = 0;
	public int sk5Level = 0;
	public int attrib1Level = 0;
	public int attrib2Level = 0;
	public int attrib3Level = 0;
	public int attrib4Level = 0;
	public int attrib5Level = 0;
	public ArchmageType trainer = ArchmageType.BLAZE;
	public ArchmageType mastery = ArchmageType.NULL;

	public ArchmageLevelManager(int sk1Level, int sk2Level, int sk3Level, int sk4Level, int sk5Level, int attrib1Level,
			int attrib2Level, int attrib3Level, int attrib4Level, int attrib5Level, String trainer, String mastery) {
		this.sk1Level = sk1Level;
		this.sk2Level = sk2Level;
		this.sk3Level = sk3Level;
		this.sk4Level = sk4Level;
		this.sk5Level = sk5Level;
		this.attrib1Level = attrib1Level;
		this.attrib2Level = attrib2Level;
		this.attrib3Level = attrib3Level;
		this.attrib4Level = attrib4Level;
		this.attrib5Level = attrib5Level;
		this.trainer = ArchmageType.valueOf(trainer);
		this.mastery = ArchmageType.valueOf(mastery);
	}

	public Inventory getSkillInventory() {
		
	}
	
	enum ArchmageType {
		BLAZE("火焰法师"), ICE("寒冰法师"), WATER("水系法师"), NULL("无");
		public String name;

		ArchmageType(String name) {
			this.name = name;
		}
	}
}
