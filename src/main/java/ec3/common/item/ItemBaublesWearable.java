package ec3.common.item;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import DummyCore.Client.IModelRegisterer;
import DummyCore.Utils.MiscUtils;
import baubles.api.BaubleType;
import baubles.api.IBauble;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

public class ItemBaublesWearable extends Item implements IBauble, IModelRegisterer {

	public static String[] baubleType = new String[]{"amulet","belt","ring"};

	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World w, EntityPlayer p, EnumHand h)
	{
		NBTTagCompound bTag = MiscUtils.getStackTag(stack);
		if(!bTag.hasKey("type"))
		{
			initRandomTag(stack,w.rand);
		}
		return new ActionResult(EnumActionResult.PASS,stack);
	}

	public static void initRandomTag(ItemStack stk, Random rand)
	{
		NBTTagCompound bTag = MiscUtils.getStackTag(stk);
		int type = rand.nextInt(3);
		bTag.setInteger("type", type);
		bTag.setInteger("b", rand.nextInt(6));
		bTag.setInteger("t", rand.nextInt(6));
		bTag.setFloat("mrucr", rand.nextFloat()/10);
		bTag.setFloat("mrurr", rand.nextFloat()/10);
		bTag.setFloat("car", rand.nextFloat()/10);
		stk.setTagCompound(bTag);
	}

	public void addInformation(ItemStack stack, EntityPlayer p_77624_2_, List<String> p_77624_3_, boolean p_77624_4_) 
	{
		NBTTagCompound bTag = MiscUtils.getStackTag(stack);
		if(bTag.hasKey("type"))
		{
			ArrayList<Float> fltLst = new ArrayList();
			fltLst.add(bTag.getFloat("mrucr"));
			fltLst.add(bTag.getFloat("mrurr"));
			fltLst.add(bTag.getFloat("car"));
			p_77624_3_.add(TextFormatting.GOLD+"+"+((int)(fltLst.get(0)*100))+"% "+TextFormatting.DARK_PURPLE+"to MRUCorruption resistance");
			p_77624_3_.add(TextFormatting.GOLD+"+"+((int)(fltLst.get(1)*100))+"% "+TextFormatting.DARK_PURPLE+"to MRURadiation resistance");
			p_77624_3_.add(TextFormatting.GOLD+"-"+((int)(fltLst.get(2)*100))+"% "+TextFormatting.DARK_PURPLE+"to Corruption affection");
		}
	}

	@Override
	public BaubleType getBaubleType(ItemStack itemstack) {
		NBTTagCompound bTag = MiscUtils.getStackTag(itemstack);
		if(bTag.hasKey("type"))
		{
			int type = bTag.getInteger("type");
			switch(type)
			{
			case 0:
				return BaubleType.AMULET;
			case 1:
				return BaubleType.BELT;
			case 2:
				return BaubleType.RING;
			//case 3:
			//	return BaubleType.TRINKET;
			//case 4:
			//	return BaubleType.HEAD;
			//case 5:
			//	return BaubleType.BODY;
			//case 6:
			//	return BaubleType.CHARM;
			}
		}
		return BaubleType.TRINKET;
	}

	public void onEquipped(ItemStack itemstack, EntityLivingBase player) {}

	public void onUnequipped(ItemStack itemstack, EntityLivingBase player) {}

	@Override
	public boolean canEquip(ItemStack itemstack, EntityLivingBase player) {
		return true;
	}

	@Override
	public boolean canUnequip(ItemStack itemstack, EntityLivingBase player) {
		return true;
	}
	@Override
	public void registerModels() {
		ModelLoader.setCustomMeshDefinition(this, new MeshDefinitionBaublesWearable());
		ArrayList<ModelResourceLocation> names = new ArrayList<ModelResourceLocation>();
		for(int bottomInt = 0; bottomInt < 6; bottomInt++) {
			for(int topInt = 0; topInt < 6; topInt++) {
				names.add(new ModelResourceLocation("essentialcraft:item/baublesAmulet", "bottom=" + bottomInt + "," + "top=" + topInt));
				names.add(new ModelResourceLocation("essentialcraft:item/baublesBelt", "bottom=" + bottomInt + "," + "top=" + topInt));
				names.add(new ModelResourceLocation("essentialcraft:item/baublesRing", "bottom=" + bottomInt + "," + "top=" + topInt));
				//names.add(new ModelResourceLocation("essentialcraft:item/baublesTrinket", "bottom=" + bottomInt + "," + "top=" + topInt));
				//names.add(new ModelResourceLocation("essentialcraft:item/baublesHead", "bottom=" + bottomInt + "," + "top=" + topInt));
				//names.add(new ModelResourceLocation("essentialcraft:item/baublesBody", "bottom=" + bottomInt + "," + "top=" + topInt));
				//names.add(new ModelResourceLocation("essentialcraft:item/baublesCharm", "bottom=" + bottomInt + "," + "top=" + topInt));
			}
		}
		ModelBakery.registerItemVariants(this, names.toArray(new ModelResourceLocation[0]));
	}

	public static class MeshDefinitionBaublesWearable implements ItemMeshDefinition {
		@Override
		public ModelResourceLocation getModelLocation(ItemStack stk) {
			NBTTagCompound bTag = MiscUtils.getStackTag(stk);
			if(bTag.hasKey("type")) {
				int type = bTag.getInteger("type");
				int bottomInt = bTag.getInteger("b");
				int topInt = bTag.getInteger("t");
				switch(type) {
				case 0:
					return new ModelResourceLocation("essentialcraft:item/baublesAmulet", "bottom=" + bottomInt + "," + "top=" + topInt);
				case 1:
					return new ModelResourceLocation("essentialcraft:item/baublesBelt", "bottom=" + bottomInt + "," + "top=" + topInt);
				case 2:
					return new ModelResourceLocation("essentialcraft:item/baublesRing", "bottom=" + bottomInt + "," + "top=" + topInt);
				//case 3:
				//	return new ModelResourceLocation("essentialcraft:item/baublesTrinket", "bottom=" + bottomInt + "," + "top=" + topInt);
				//case 4:
				//	return new ModelResourceLocation("essentialcraft:item/baublesHead", "bottom=" + bottomInt + "," + "top=" + topInt);
				//case 5:
				//	return new ModelResourceLocation("essentialcraft:item/baublesBody", "bottom=" + bottomInt + "," + "top=" + topInt);
				//case 6:
				//	return new ModelResourceLocation("essentialcraft:item/baublesCharm", "bottom=" + bottomInt + "," + "top=" + topInt);
				}
			}
			return new ModelResourceLocation("essentialcraft:item/baublesAmulet", "bottom=0,top=0");
		}
	}
}
