package ec3.api;

import java.util.ArrayList;

import com.google.common.collect.Lists;

public class OreSmeltingRecipe {

	public static final ArrayList<OreSmeltingRecipe> RECIPES = Lists.<OreSmeltingRecipe>newArrayList();

	public String oreName;
	public String outputName;
	public int color;
	public int dropAmount;

	public OreSmeltingRecipe(String i, int j) {
		this(i,j,1);
	}

	public OreSmeltingRecipe(String i, int j, int k) {
		this(i,"",j,k);
	}

	public OreSmeltingRecipe(String i,String s, int j, int k) {
		oreName = i;
		color = j;
		dropAmount = k;
		outputName = s;
	}

	public OreSmeltingRecipe(String i ,String s, int j) {
		this(i,s,j,1);
	}

	public OreSmeltingRecipe register() {
		boolean flag = true;
		for(OreSmeltingRecipe rec : RECIPES)
			if(rec.oreName == this.oreName)
				flag = false;
		if(flag)
			RECIPES.add(this);
		return this;
	}

	public static OreSmeltingRecipe addRecipe(String i, int j) {
		return new OreSmeltingRecipe(i,j).register();
	}

	public static OreSmeltingRecipe addRecipe(String i, int j, int k) {
		return new OreSmeltingRecipe(i,j,k).register();
	}

	public static OreSmeltingRecipe addRecipe(String i, String s, int j, int k) {
		return new OreSmeltingRecipe(i,s,j,k).register();
	}

	public static OreSmeltingRecipe addRecipe(String i, String s, int j) {
		return new OreSmeltingRecipe(i,s,j).register();
	}
	
	public static boolean removeRecipe(OreSmeltingRecipe rec) {
		return RECIPES.remove(rec);
	}

	public static OreSmeltingRecipe[] values() {
		return RECIPES.toArray(new OreSmeltingRecipe[0]);
	}

	static {
		addRecipe("oreCoal","gemCoal",0x343434);
		addRecipe("oreIron",0xe2c0aa);
		addRecipe("oreGold",0xf8af2b);
		addRecipe("oreDiamond","gemDiamond",0x5decf5);
		addRecipe("oreEmerald","gemEmerald",0x17dd62);
		addRecipe("oreQuartz","gemQuartz",0xd1beb1);
		addRecipe("oreRedstone","dustRedstone",0x8f0303,8);
		addRecipe("oreLapis","gemLapis",0x1c40a9,16);
		addRecipe("oreCopper",0xbc4800);
		addRecipe("oreTin",0xc3e9ff);
		addRecipe("oreLead",0x7c8cc7);
		addRecipe("oreSilver",0xf0fdfe);
		addRecipe("oreCobalt",0x002568);
		addRecipe("oreArdite",0xc9a537);
		addRecipe("oreNickel",0xe5e4bd);
		addRecipe("oreAluminum",0xc5c5c5);
		addRecipe("oreUranium",0x41b200);
		addRecipe("oreIridium",0xebffff);
		addRecipe("oreAlchemite","gemAlchemite",0xff0e27,5);
		addRecipe("oreShardFire","shardFire",0xff0000,3);
		addRecipe("oreShardWater","shardWater",0x0000ff,3);
		addRecipe("oreShardEarth","shardEarth",0x7d5a3a,3);
		addRecipe("oreShardAir","shardAir",0xffffff,3);
		addRecipe("oreShardElemental","shardElemental",0xff00ff,3);
		addRecipe("oreDustMithriline","dustMithriline",0x00ff00,8);
		addRecipe("oreSaltpeter","dustSaltpeter",0x999595,5);
		addRecipe("oreSulfur","dustSulfur",0xffff99,5);
		addRecipe("orePlatinum",0x3333ff);
		addRecipe("oreMithril",0x3333ff);
	}
}
