package ec3.common.item;

import java.util.List;

import DummyCore.Client.IModelRegisterer;
import ec3.common.inventory.ContainerFilter;
import ec3.common.mod.EssentialCraftCore;
import ec3.utils.cfg.Config;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

public class ItemFilter extends Item implements IModelRegisterer {

	public ActionResult<ItemStack> onItemRightClick(ItemStack is, World w, EntityPlayer p, EnumHand hand)
	{
		p.openGui(EssentialCraftCore.core, Config.guiID[0], w, 0, -1, 0);
		return new ActionResult(EnumActionResult.PASS, is);
	}

	protected int containerMatchesItem(Container openContainer) 
	{       
		return 0;
	}

	public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List<ItemStack> par3List)
	{
		for(int var4 = 0; var4 < 4; ++var4)
		{
			ItemStack min = new ItemStack(par1, 1, var4);
			par3List.add(min);
		}
	}

	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List<String> par3List, boolean par4) 
	{
		if(par1ItemStack.getItemDamage() == 1 || par1ItemStack.getItemDamage() == 3)
			par3List.add(I18n.translateToLocal("ec3.txt.desc.advanced"));
		if(par1ItemStack.getItemDamage() > 1)
			par3List.add(I18n.translateToLocal("ec3.txt.desc.blacklist"));
		else
			par3List.add(I18n.translateToLocal("ec3.txt.desc.whitelist"));
	}

	@Override
	public void onUpdate(ItemStack itemStack, World world, Entity entity, int indexInInventory, boolean isCurrentItem)
	{
		if (world.isRemote || !isCurrentItem || !(entity instanceof EntityPlayer))
		{
			return;
		}
		if(((EntityPlayer)entity).openContainer == null || !(((EntityPlayer) entity).openContainer instanceof ContainerFilter))
		{
			return;
		}
		int containerType = containerMatchesItem(((EntityPlayer)entity).openContainer);
		if (containerType == 0) 
		{
			ContainerFilter c = (ContainerFilter)((EntityPlayer)entity).openContainer;
			c.saveToNBT(itemStack);
		}
	}

	public ItemFilter()
	{
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
	}

	@Override
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation("essentialcraft:item/itemFilter", "type=whitelist"));
		ModelLoader.setCustomModelResourceLocation(this, 1, new ModelResourceLocation("essentialcraft:item/itemFilter", "type=whitelist_advanced"));
		ModelLoader.setCustomModelResourceLocation(this, 2, new ModelResourceLocation("essentialcraft:item/itemFilter", "type=blacklist"));
		ModelLoader.setCustomModelResourceLocation(this, 3, new ModelResourceLocation("essentialcraft:item/itemFilter", "type=blacklist_advanced"));
	}
}
