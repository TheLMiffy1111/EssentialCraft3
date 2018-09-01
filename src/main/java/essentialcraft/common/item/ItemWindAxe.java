package essentialcraft.common.item;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import DummyCore.Utils.MiscUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemWindAxe extends ItemAxeEC {

	public ItemWindAxe(ToolMaterial m)
	{
		super(m);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand p_77648_6_, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		return ItemsCore.wind_elemental_hoe.onItemUse(player, world, pos, p_77648_6_, facing, hitX, hitY, hitZ);
	}

	@Override
	public boolean hitEntity(ItemStack weapon, EntityLivingBase attacked, EntityLivingBase attacker)
	{
		if(attacker instanceof EntityPlayer)
		{
			//Totally the same code as in
			//#link essentialcraft.utils.common.ECEventHandler
			EntityPlayer p = (EntityPlayer)attacker;

			ItemStack currentTool = weapon;

			if(!p.getEntityWorld().isRemote && currentTool.getItem() instanceof ItemTool && ((ItemTool)currentTool.getItem()).getToolMaterialName().equals(ItemsCore.windElemental.name()))
			{
				String clazz = "sword";

				String currentToolClass = "";
				if(currentTool.getItem() instanceof ItemPickaxe)
					currentToolClass = "pickaxe";
				if(currentTool.getItem() instanceof ItemAxe)
					currentToolClass = "axe";
				if(currentTool.getItem() instanceof ItemSpade)
					currentToolClass = "shovel";
				if(currentTool.getItem() instanceof ItemHoe)
					currentToolClass = "hoe";
				if(currentTool.getItem() instanceof ItemSword)//<--- yet again, copy/paste is bad!
					currentToolClass = "sword";

				NBTTagCompound toolTag = new NBTTagCompound();
				NBTTagCompound genericTag = new NBTTagCompound();

				currentTool.writeToNBT(toolTag);

				if(toolTag.hasKey("tag"))
				{
					genericTag = toolTag.getCompoundTag("tag").copy();
					toolTag.getCompoundTag("tag").removeTag("pickaxe");
					toolTag.getCompoundTag("tag").removeTag("axe");
					toolTag.getCompoundTag("tag").removeTag("shovel");
					toolTag.getCompoundTag("tag").removeTag("hoe");
					toolTag.getCompoundTag("tag").removeTag("sword");

					toolTag.getCompoundTag("tag").removeTag("tag");
				}

				Set<String> tags = genericTag.getKeySet();
				List<String> tagKeyLst = new ArrayList<String>();
				tags.forEach(name->tagKeyLst.add(name));

				if(tagKeyLst.indexOf("pickaxe") != -1)
					tagKeyLst.remove("pickaxe");
				if(tagKeyLst.indexOf("axe") != -1)
					tagKeyLst.remove("axe");
				if(tagKeyLst.indexOf("shovel") != -1)
					tagKeyLst.remove("shovel");
				if(tagKeyLst.indexOf("hoe") != -1)
					tagKeyLst.remove("hoe");
				if(tagKeyLst.indexOf("sword") != -1)
					tagKeyLst.remove("sword");

				for(int i = 0; i < tagKeyLst.size(); ++i)
				{
					genericTag.removeTag(tagKeyLst.get(i));
				}

				ItemStack efficent = ItemStack.EMPTY;

				if(genericTag.hasKey(clazz))
				{
					NBTTagCompound loadFrom = genericTag.getCompoundTag(clazz).copy();
					genericTag.removeTag(clazz);
					efficent = new ItemStack(loadFrom);
				}else
				{
					if(clazz.equalsIgnoreCase("pickaxe"))
						efficent = new ItemStack(ItemsCore.wind_elemental_pick,1,currentTool.getItemDamage());
					if(clazz.equalsIgnoreCase("shovel"))
						efficent = new ItemStack(ItemsCore.wind_elemental_shovel,1,currentTool.getItemDamage());
					if(clazz.equalsIgnoreCase("hoe"))
						efficent = new ItemStack(ItemsCore.wind_elemental_hoe,1,currentTool.getItemDamage());
					if(clazz.equalsIgnoreCase("sword"))
						efficent = new ItemStack(ItemsCore.wind_elemental_sword,1,currentTool.getItemDamage());
					if(clazz.equalsIgnoreCase("axe"))
						efficent = new ItemStack(ItemsCore.wind_elemental_axe,1,currentTool.getItemDamage());
				}

				if(!efficent.isEmpty() && efficent.getItem() != null)
				{
					NBTTagCompound anotherTag = MiscUtils.getStackTag(efficent);

					if(genericTag.hasKey("pickaxe"))
						anotherTag.setTag("pickaxe", genericTag.getTag("pickaxe"));
					if(genericTag.hasKey("axe"))
						anotherTag.setTag("axe", genericTag.getTag("axe"));
					if(genericTag.hasKey("shovel"))
						anotherTag.setTag("shovel", genericTag.getTag("shovel"));
					if(genericTag.hasKey("hoe"))
						anotherTag.setTag("hoe", genericTag.getTag("hoe"));
					if(genericTag.hasKey("sword"))
						anotherTag.setTag("sword", genericTag.getTag("sword"));

					if(toolTag != null)
						anotherTag.setTag(currentToolClass, toolTag);

					p.inventory.setInventorySlotContents(p.inventory.currentItem, efficent);
				}
			}
		}
		return true;
	}
}
