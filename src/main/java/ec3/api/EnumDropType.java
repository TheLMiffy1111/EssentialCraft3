package ec3.api;

import net.minecraft.util.IStringSerializable;

public enum EnumDropType implements IStringSerializable {
	FIRE(0,"fire",1),
	WATER(1,"water",2),
	EARTH(2,"earth",3),
	AIR(3,"air",4),
	ELEMENTAL(4,"elemental",0),
	MITHRILINE(5,"mithriline",-1);
	
	private int index;
	private String name;
	private int indexOre;
	
	private EnumDropType(int i, String s, int i2) {
		index = i;
		name = s;
		indexOre = i2;
	}
	
	public String getName() {
		return name;
	}
	
	public String toString() {
		return name;
	}
	
	public int getIndex() {
		return index;
	}
	
	public int getIndexOre() {
		return indexOre;
	}
	
	public static EnumDropType fromIndex(int i) {
		return values()[i%6];
	}
	
	public static EnumDropType fromIndexOre(int i) {
		return values()[(i+4)%5];
	}
	
	public static final EnumDropType[] CANBEFARMED = new EnumDropType[] {FIRE,WATER,EARTH,AIR};
	public static final EnumDropType[] NORMAL = new EnumDropType[] {FIRE,WATER,EARTH,AIR,ELEMENTAL};
}
