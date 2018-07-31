package essentialcraft.common.item;

import java.util.List;

import DummyCore.Client.IModelRegisterer;
import essentialcraft.common.inventory.ContainerFilter;
import essentialcraft.common.mod.EssentialCraftCore;
import essentialcraft.utils.cfg.Config;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemFilter extends Item implements IModelRegisterer {

	@Override
	public ActionResult<ItemStack> onItemRightClick(World w, EntityPlayer p, EnumHand hand)
	{
		p.openGui(EssentialCraftCore.core, Config.guiID[0], w, 0, -1, 0);
		return super.onItemRightClick(w, p, hand);
	}

	protected int containerMatchesItem(Container openContainer)
	{
		return 0;
	}

	@Override
	public void getSubItems(CreativeTabs par2CreativeTabs, NonNullList<ItemStack> list)
	{
		if(this.isInCreativeTab(par2CreativeTabs))
			for(int var4 = 0; var4 < 4; ++var4)
			{
				ItemStack min = new ItemStack(this, 1, var4);
				list.add(min);
			}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World player, List<String> list, ITooltipFlag par4)
	{
		if(stack.getItemDamage() == 1 || stack.getItemDamage() == 3)
			list.add(I18n.translateToLocal("essentialcraft.txt.desc.advanced"));
		if(stack.getItemDamage() > 1)
			list.add(I18n.translateToLocal("essentialcraft.txt.desc.blacklist"));
		else
			list.add(I18n.translateToLocal("essentialcraft.txt.desc.whitelist"));
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
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation("essentialcraft:item/itemfilter", "type=whitelist"));
		ModelLoader.setCustomModelResourceLocation(this, 1, new ModelResourceLocation("essentialcraft:item/itemfilter", "type=whitelist_advanced"));
		ModelLoader.setCustomModelResourceLocation(this, 2, new ModelResourceLocation("essentialcraft:item/itemfilter", "type=blacklist"));
		ModelLoader.setCustomModelResourceLocation(this, 3, new ModelResourceLocation("essentialcraft:item/itemfilter", "type=blacklist_advanced"));
	}
}
