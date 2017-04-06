package ec3.common.item;

import java.util.List;

import DummyCore.Client.IModelRegisterer;
import ec3.common.inventory.ContainerCraftingFrame;
import ec3.common.inventory.InventoryCraftingFrame;
import ec3.common.mod.EssentialCraftCore;
import ec3.utils.cfg.Config;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

public class ItemCraftingFrame extends Item implements IModelRegisterer {

	public ActionResult<ItemStack> onItemRightClick(ItemStack is, World w, EntityPlayer p, EnumHand h)
	{
		p.openGui(EssentialCraftCore.core, Config.guiID[0], w, 0, -2, 0);
		return new ActionResult(EnumActionResult.PASS, is);
	}

	protected int containerMatchesItem(Container openContainer) 
	{       
		return 0;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) 
	{
		InventoryCraftingFrame inv = new InventoryCraftingFrame(par1ItemStack);
		if(inv != null)
		{
			par3List.add("Current recipe:");
			for(int i = 0; i < 9; ++i)
			{
				ItemStack stk = inv.getStackInSlot(i);

				if(stk == null)
					par3List.add(i+": Empty");
				else
					par3List.add(i+": "+stk.getDisplayName());
			}

			ItemStack stk = inv.getStackInSlot(9);
			if(stk == null)
				par3List.add("Result: None");
			else
				par3List.add("Result: "+stk.getDisplayName());
		}
	}

	@Override
	public void onUpdate(ItemStack itemStack, World world, Entity entity, int indexInInventory, boolean isCurrentItem)
	{
		if(world.isRemote || !isCurrentItem || !(entity instanceof EntityPlayer))
		{
			return;
		}
		if(((EntityPlayer)entity).openContainer == null || !(((EntityPlayer)entity).openContainer instanceof ContainerCraftingFrame))
		{
			return;
		}
		int containerType = containerMatchesItem(((EntityPlayer)entity).openContainer);
		if(containerType == 0) 
		{
			ContainerCraftingFrame c = (ContainerCraftingFrame)((EntityPlayer)entity).openContainer;
			c.saveToNBT(itemStack);
		}
	}

	public ItemCraftingFrame()
	{
		this.setMaxStackSize(1);
	}

	@Override
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation("essentialcraft:item/craftingFrame", "inventory"));
	}
}
