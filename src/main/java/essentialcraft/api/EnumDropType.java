package essentialcraft.api;

import net.minecraft.block.material.MapColor;
import net.minecraft.util.IStringSerializable;

public enum EnumDropType implements IStringSerializable {
	FIRE(0,"fire",1,MapColor.ADOBE),
	WATER(1,"water",2,MapColor.WATER),
	EARTH(2,"earth",3,MapColor.DIRT),
	AIR(3,"air",4,MapColor.CLOTH),
	ELEMENTAL(4,"elemental",0,MapColor.PURPLE),
	MITHRILINE(5,"mithriline",-1,MapColor.GREEN);

	private final int index;
	private final String name;
	private final int indexOre;
	private final MapColor mapColor;

	private EnumDropType(int index, String name, int indexOre, MapColor mapColor) {
		this.index = index;
		this.name = name;
		this.indexOre = indexOre;
		this.mapColor = mapColor;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return name;
	}

	public int getIndex() {
		return index;
	}

	public int getIndexOre() {
		return indexOre;
	}

	public MapColor getMapColor() {
		return mapColor;
	}

	public static EnumDropType fromIndex(int i) {
		return values()[i%6];
	}

	public static EnumDropType fromIndexOre(int i) {
		return values()[(i+4)%5];
	}

	public static final EnumDropType[] CAN_BE_FARMED = {FIRE,WATER,EARTH,AIR};
	public static final EnumDropType[] NORMAL = {FIRE,WATER,EARTH,AIR,ELEMENTAL};
}
