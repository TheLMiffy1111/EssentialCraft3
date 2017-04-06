package ec3.common.item;

import java.util.List;

import DummyCore.Client.IModelRegisterer;
import DummyCore.Utils.Coord3D;
import DummyCore.Utils.DummyDistance;
import DummyCore.Utils.MiscUtils;
import ec3.common.tile.TileMagicalMirror;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

public class ItemControlRod extends Item implements IModelRegisterer {

	public ItemControlRod() {
		super();
		this.maxStackSize = 1;
	}

	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if(world.isRemote)
			return EnumActionResult.PASS;
		if(stack.getTagCompound() == null)
		{
			TileEntity tile = world.getTileEntity(pos);
			if(tile != null)
			{
				if(tile instanceof TileMagicalMirror)
				{
					MiscUtils.getStackTag(stack).setIntArray("pos", new int[]{pos.getX(),pos.getY(),pos.getZ()});
					player.sendMessage(new TextComponentString("Mirror linked to the wand!").setStyle(new Style().setColor(TextFormatting.GREEN)));
					return EnumActionResult.SUCCESS;
				}
			}
		}else
		{
			TileEntity tile = world.getTileEntity(pos);
			if(tile != null)
			{if(tile instanceof IInventory)
			{
				int[] o = MiscUtils.getStackTag(stack).getIntArray("pos");
				float distance = new DummyDistance(new Coord3D(pos.getX(),pos.getY(),pos.getZ()),new Coord3D(o[0],o[1],o[2])).getDistance();
				if(distance <= TileMagicalMirror.cfgMaxDistance)
				{
					TileEntity tile1 = world.getTileEntity(new BlockPos(o[0],o[1],o[2]));
					if(tile1 != null && tile1 instanceof TileMagicalMirror)
					{
						((TileMagicalMirror)tile1).inventoryPos = new Coord3D(pos.getX()+0.5F,pos.getY()+0.5F,pos.getZ()+0.5F);
						player.sendMessage(new TextComponentString("Mirror linked to the inventory!").setStyle(new Style().setColor(TextFormatting.GREEN)));
						stack.setTagCompound(null);
						return EnumActionResult.SUCCESS;
					}
				}
			}
			}
		}
		return EnumActionResult.PASS;
	}

	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List<String> par3List, boolean par4) 
	{
		if(par1ItemStack.getTagCompound() != null)
		{
			int[] coord = MiscUtils.getStackTag(par1ItemStack).getIntArray("pos");
			par3List.add("Currently linked to Mirror At:");
			par3List.add("x: "+coord[0]);
			par3List.add("y: "+coord[1]);
			par3List.add("z: "+coord[2]);
			par3List.add("dimension: "+MiscUtils.getStackTag(par1ItemStack).getInteger("dim"));
		}
	}

	public static int[] getCoords(ItemStack stack)
	{
		return MiscUtils.getStackTag(stack).getIntArray("pos");
	}


	public boolean createTag(ItemStack stack)
	{
		if(stack.getTagCompound() == null)
		{
			NBTTagCompound tag = new NBTTagCompound();
			tag.setIntArray("pos", new int[]{0,0,0});
			return true;
		}
		return false;
	}

	@Override
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation("essentialcraft:item/controlRod", "inventory"));
	}
}
