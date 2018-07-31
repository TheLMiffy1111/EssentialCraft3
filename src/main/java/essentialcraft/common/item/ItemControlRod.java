package essentialcraft.common.item;

import java.util.List;

import DummyCore.Client.IModelRegisterer;
import DummyCore.Utils.Coord3D;
import DummyCore.Utils.DummyDistance;
import DummyCore.Utils.MiscUtils;
import essentialcraft.common.tile.TileMagicalMirror;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemControlRod extends Item implements IModelRegisterer {

	public ItemControlRod() {
		super();
		this.maxStackSize = 1;
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		ItemStack stack = player.getHeldItem(hand);
		if(world.isRemote)
			return EnumActionResult.SUCCESS;
		if(stack.getTagCompound() == null) {
			TileEntity tile = world.getTileEntity(pos);
			if(tile != null) {
				if(tile instanceof TileMagicalMirror) {
					MiscUtils.getStackTag(stack).setIntArray("pos", new int[]{pos.getX(),pos.getY(),pos.getZ()});
					player.sendMessage(new TextComponentString("Mirror linked to the wand!").setStyle(new Style().setColor(TextFormatting.GREEN)));
					return EnumActionResult.SUCCESS;
				}
			}
		}
		else {
			TileEntity tile = world.getTileEntity(pos);
			if(tile != null) {
				if(tile instanceof IInventory) {
					int[] o = MiscUtils.getStackTag(stack).getIntArray("pos");
					float distance = new DummyDistance(new Coord3D(pos.getX(),pos.getY(),pos.getZ()),new Coord3D(o[0],o[1],o[2])).getDistance();
					if(distance <= TileMagicalMirror.cfgMaxDistance) {
						TileEntity tile1 = world.getTileEntity(new BlockPos(o[0],o[1],o[2]));
						if(tile1 != null && tile1 instanceof TileMagicalMirror) {
							((TileMagicalMirror)tile1).inventoryPos = pos;
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

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World player, List<String> list, ITooltipFlag par4)
	{
		super.addInformation(stack, player, list, par4);
		if(stack.getTagCompound() != null)
		{
			int[] coord = MiscUtils.getStackTag(stack).getIntArray("pos");
			list.add("Currently linked to Mirror At:");
			list.add("x: "+coord[0]);
			list.add("y: "+coord[1]);
			list.add("z: "+coord[2]);
			list.add("dimension: "+MiscUtils.getStackTag(stack).getInteger("dim"));
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
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation("essentialcraft:item/controlrod", "inventory"));
	}
}
