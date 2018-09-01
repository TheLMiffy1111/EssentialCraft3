package essentialcraft.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import DummyCore.Utils.MiscUtils;
import essentialcraft.common.item.ItemsCore;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.oredict.OreDictionary;

public class OreSmeltingRecipe {

	public static final ArrayList<OreSmeltingRecipe> RECIPES = Lists.<OreSmeltingRecipe>newArrayList();
	public static final HashMap<String, OreSmeltingRecipe> RECIPE_MAP = Maps.<String,OreSmeltingRecipe>newHashMap();

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

	public OreSmeltingRecipe(String i, String s, int j, int k) {
		oreName = i;
		color = j;
		dropAmount = k;
		outputName = s;
	}

	public OreSmeltingRecipe(String i, String s, int j) {
		this(i, s, j, 1);
	}

	public OreSmeltingRecipe register() {
		boolean flag = true;
		for(OreSmeltingRecipe rec : RECIPES)
			if(rec.oreName == this.oreName)
				flag = false;
		if(flag) {
			RECIPES.add(this);
			RECIPE_MAP.put(oreName, this);
		}
		return this;
	}

	public static OreSmeltingRecipe addRecipe(String i, int j) {
		return new OreSmeltingRecipe(i, j).register();
	}

	public static OreSmeltingRecipe addRecipe(String i, int j, int k) {
		return new OreSmeltingRecipe(i, j, k).register();
	}

	public static OreSmeltingRecipe addRecipe(String i, String s, int j, int k) {
		return new OreSmeltingRecipe(i, s, j, k).register();
	}

	public static OreSmeltingRecipe addRecipe(String i, String s, int j) {
		return new OreSmeltingRecipe(i, s, j).register();
	}

	public static boolean removeRecipe(OreSmeltingRecipe rec) {
		return RECIPES.remove(rec) && RECIPE_MAP.remove(rec.oreName,rec);
	}

	public static int getColorFromItemStack(ItemStack stk) {
		if(stk.getItem() == ItemsCore.magicalAlloy) {
			if(!stk.hasTagCompound()) {
				if(stk.getItemDamage() < RECIPES.size())
					return RECIPES.get(stk.getItemDamage()).color;
				return 0xFFFFFF;
			}
			NBTTagCompound tag = stk.getTagCompound();
			if(tag.hasKey("ore"))
				return RECIPE_MAP.get(tag.getString("ore")).color;
		}
		return 0xFFFFFF;
	}

	public static String getLocalizedOreName(ItemStack stk) {
		if(stk.getItem() == ItemsCore.magicalAlloy) {
			if(stk.getItemDamage() >= OreSmeltingRecipe.RECIPES.size())
				return "";
			OreSmeltingRecipe ore;
			if(!stk.hasTagCompound()) {
				if(stk.getItemDamage() < RECIPES.size())
					ore = OreSmeltingRecipe.RECIPES.get(stk.getItemDamage());
				else
					return "";
			}
			else {
				NBTTagCompound tag = stk.getTagCompound();
				if(tag.hasKey("ore"))
					ore = RECIPE_MAP.get(tag.getString("ore"));
				else
					return "";
			}
			List<ItemStack> oreLst = OreDictionary.getOres(ore.oreName, false);
			if(oreLst != null && !oreLst.isEmpty())
				return oreLst.get(0).getDisplayName();
			else
				return I18n.translateToLocal("tile."+ore.oreName+".name");
		}
		return "";
	}

	public static ItemStack getAlloyStack(OreSmeltingRecipe rec, int stackSize) {
		ItemStack ret = new ItemStack(ItemsCore.magicalAlloy,stackSize,0);
		NBTTagCompound tag = MiscUtils.getStackTag(ret);
		tag.setString("ore", rec.oreName);
		return ret;
	}

	public static int getIndex(ItemStack stk) {
		if(stk.getItem() == ItemsCore.magicalAlloy && stk.hasTagCompound()) {
			NBTTagCompound tag = stk.getTagCompound();
			if(tag.hasKey("ore"))
				return RECIPES.indexOf(RECIPE_MAP.get(tag.getString("ore")));
		}
		return stk.getItemDamage();
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
		addRecipe("oreFireElemental","gemFireElemental",0xff0000,3);
		addRecipe("oreWaterElemental","gemWaterElemental",0x0000ff,3);
		addRecipe("oreEarthElemental","gemEarthElemental",0x7d5a3a,3);
		addRecipe("oreAirElemental","gemAirElemental",0xffffff,3);
		addRecipe("oreElemental","gemElemental",0xff00ff,3);
		addRecipe("oreMithriline","dustMithriline",0x00ff00,8);
		addRecipe("oreSaltpeter","dustSaltpeter",0x999595,5);
		addRecipe("oreSulfur","dustSulfur",0xffff99,5);
		addRecipe("orePlatinum",0x6f7889);
		addRecipe("oreMithril",0x3b525f);
	}
}
