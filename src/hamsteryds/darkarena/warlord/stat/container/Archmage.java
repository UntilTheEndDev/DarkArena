package hamsteryds.darkarena.warlord.stat.container;

import java.util.ArrayList;
import java.util.List;

import hamsteryds.darkarena.warlord.stat.ArchmageManager.ArchmageType;

public class Archmage {
	public int sk1Level;
	public int sk2Level;
	public int sk3Level;
	public int sk4Level;
	public int sk5Level;
	public int attrib1Level;
	public int attrib2Level;
	public int attrib3Level;
	public int attrib4Level;
	public int attrib5Level;
	public List<Integer> blazeStats;
	public List<Integer> iceStats;
	public List<Integer> waterStats;
	public ArchmageType trainer = ArchmageType.BLAZE;
	public ArchmageType mastery = ArchmageType.NULL;

	public Archmage(int sk1Level, int sk2Level, int sk3Level, int sk4Level, int sk5Level, int attrib1Level,
			int attrib2Level, int attrib3Level, int attrib4Level, int attrib5Level, String trainer, String mastery,
			List<Integer> blazeStats, List<Integer> iceStats, List<Integer> waterStats) {
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
		this.blazeStats = blazeStats;
		this.iceStats = iceStats;
		this.waterStats = waterStats;
	}

	public Archmage() {
		this.sk1Level = 0;
		this.sk2Level = 0;
		this.sk3Level = 0;
		this.sk4Level = 0;
		this.sk5Level = 0;
		this.attrib1Level = 0;
		this.attrib2Level = 0;
		this.attrib3Level = 0;
		this.attrib4Level = 0;
		this.attrib5Level = 0;
		this.trainer = ArchmageType.BLAZE;
		this.mastery = ArchmageType.NULL;
		ArrayList<Integer> model = new ArrayList<Integer>();
		model.add(0);
		model.add(0);
		model.add(0);
		model.add(0);
		model.add(0);
		this.blazeStats = (List<Integer>) model.clone();
		this.iceStats = (List<Integer>) model.clone();
		this.waterStats = (List<Integer>) model.clone();
	}

	public void changeLevel(VariableType variable, ChangeType type, int value) {
		switch (variable) {
		case at1:
			this.attrib1Level = newValue(this.attrib1Level, value, type);
			break;
		case at2:
			this.attrib2Level = newValue(this.attrib2Level, value, type);
			break;
		case at3:
			this.attrib3Level = newValue(this.attrib3Level, value, type);
			break;
		case at4:
			this.attrib4Level = newValue(this.attrib4Level, value, type);
			break;
		case at5:
			this.attrib5Level = newValue(this.attrib5Level, value, type);
			break;
		case sk1:
			this.sk1Level = newValue(this.sk1Level, value, type);
			break;
		case sk2:
			this.sk2Level = newValue(this.sk2Level, value, type);
			break;
		case sk3:
			this.sk3Level = newValue(this.sk3Level, value, type);
			break;
		case sk4:
			this.sk4Level = newValue(this.sk4Level, value, type);
			break;
		case sk5:
			this.sk5Level = newValue(this.sk5Level, value, type);
			break;
		}

	}

	public int newValue(int currentValue, int value, ChangeType type) {
		switch (type) {
		case add:
			currentValue += value;
			break;
		case minus:
			currentValue -= value;
			break;
		case set:
			currentValue = value;
			break;
		}
		return currentValue;
	}

	public static enum VariableType {
		sk1, sk2, sk3, sk4, sk5, at1, at2, at3, at4, at5;
	}

	public static enum ChangeType {
		add, minus, set;
	}

	public void addFigure(int kill, int arena, int victory, int atk, int mvp) {
		switch (this.trainer) {
		case BLAZE:
			this.blazeStats.set(0, blazeStats.get(0) + kill);
			this.blazeStats.set(1, blazeStats.get(1) + arena);
			this.blazeStats.set(2, blazeStats.get(2) + victory);
			this.blazeStats.set(3, blazeStats.get(3) + atk);
			this.blazeStats.set(4, blazeStats.get(4) + mvp);
			break;
		case ICE:
			this.iceStats.set(0, iceStats.get(0) + kill);
			this.iceStats.set(1, iceStats.get(1) + arena);
			this.iceStats.set(2, iceStats.get(2) + victory);
			this.iceStats.set(3, iceStats.get(3) + atk);
			this.iceStats.set(4, iceStats.get(4) + mvp);
			break;
		case WATER:
			this.waterStats.set(0, waterStats.get(0) + kill);
			this.waterStats.set(1, waterStats.get(1) + arena);
			this.waterStats.set(2, waterStats.get(2) + victory);
			this.waterStats.set(3, waterStats.get(3) + atk);
			this.waterStats.set(4, waterStats.get(4) + mvp);
			break;
		case NULL:
			break;
		}
	}

	public int getTotalLevel() {
		return sk1Level + sk2Level + sk3Level + sk4Level + sk5Level + attrib1Level + attrib2Level + attrib3Level
				+ attrib4Level + attrib5Level;
	}
}
