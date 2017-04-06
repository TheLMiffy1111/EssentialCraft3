package ec3.common.item;

import java.util.List;

import DummyCore.Client.IModelRegisterer;
import DummyCore.Utils.MiscUtils;
import ec3.common.mod.EssentialCraftCore;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

public class ItemKnowledgeBook extends Item implements IModelRegisterer {

	public ItemKnowledgeBook() {
		super();
		this.maxStackSize = 1;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer, EnumHand hand)
	{
		EssentialCraftCore.proxy.openBookGUIForPlayer();
		return new ActionResult(EnumActionResult.PASS, par1ItemStack);
	}

	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List<String> par3List, boolean par4) 
	{
		NBTTagCompound theTag = MiscUtils.getStackTag(par1ItemStack);
		par3List.add("\u00a76" + I18n.translateToLocal("ec3.txt.book.containedKnowledge"));
		int tier = theTag.getInteger("tier");
		for(int i = 0; i <= tier; ++i)
		{
			par3List.add("\u00a77-\u00a7o" + I18n.translateToLocal("ec3.txt.book.tier_"+i));
		}
	}

	public void getSubItems(Item p_150895_1_, CreativeTabs p_150895_2_, List<ItemStack> p_150895_3_)
	{
		for(int i = 0; i < 5; ++i)
		{
			ItemStack book = new ItemStack(p_150895_1_);
			NBTTagCompound bookTag = new NBTTagCompound();
			bookTag.setInteger("tier", i);
			book.setTagCompound(bookTag);
			p_150895_3_.add(book);
		}
	}

	@Override
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation("essentialcraft:item/research_book", "inventory"));
	}
}
