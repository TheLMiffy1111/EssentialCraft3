package essentialcraft.common.item;

import java.util.List;

import DummyCore.Client.IModelRegisterer;
import essentialcraft.common.inventory.ContainerCraftingFrame;
import essentialcraft.common.inventory.InventoryCraftingFrame;
import essentialcraft.common.mod.EssentialCraftCore;
import essentialcraft.utils.cfg.Config;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemCraftingFrame extends Item implements IModelRegisterer {

	@Override
	public ActionResult<ItemStack> onItemRightClick(World w, EntityPlayer p, EnumHand h)
	{
		p.openGui(EssentialCraftCore.core, Config.guiID[0], w, 0, -2, 0);
		return super.onItemRightClick(w, p, h);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World player, List<String> list, ITooltipFlag par4)
	{
		super.addInformation(stack, player, list, par4);
		InventoryCraftingFrame inv = new InventoryCraftingFrame(stack);
		if(inv != null)
		{
			list.add("Current recipe:");
			for(int i = 0; i < 9; ++i)
			{
				ItemStack stk = inv.getStackInSlot(i);

				if(stk.isEmpty())
					list.add(i+": Empty");
				else
					list.add(i+": "+stk.getDisplayName());
			}

			ItemStack stk = inv.getStackInSlot(9);
			if(stk.isEmpty())
				list.add("Result: None");
			else
				list.add("Result: "+stk.getDisplayName());
		}
	}

	@Override
	public void onUpdate(ItemStack itemStack, World world, Entity entity, int indexInInventory, boolean isCurrentItem) {
		if(!isCurrentItem || !(entity instanceof EntityPlayer)) {
			return;
		}
		if(((EntityPlayer)entity).openContainer == null || !(((EntityPlayer)entity).openContainer instanceof ContainerCraftingFrame))
		{
			return;
		}
		ContainerCraftingFrame c = (ContainerCraftingFrame)((EntityPlayer)entity).openContainer;
		c.saveToNBT(itemStack);
	}

	public ItemCraftingFrame() {
		this.setMaxStackSize(1);
	}

	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		return !oldStack.getItem().equals(newStack.getItem());
	}

	@Override
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation("essentialcraft:item/craftingframe", "inventory"));
	}
}
