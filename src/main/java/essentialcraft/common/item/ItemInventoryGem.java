package essentialcraft.common.item;

import java.util.List;

import DummyCore.Client.IItemColor;
import DummyCore.Client.IModelRegisterer;
import DummyCore.Utils.Coord3D;
import DummyCore.Utils.MiscUtils;
import essentialcraft.common.block.BlockRayTower;
import essentialcraft.common.block.BlocksCore;
import essentialcraft.common.item.ItemBoundGem.MeshDefinitionBoundGem;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;

public class ItemInventoryGem extends Item implements IItemColor, IModelRegisterer {

	public static Coord3D currentlyClicked;
	public static int clickTicks;

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		ItemStack stack = player.getHeldItem(hand);
		if(stack.getTagCompound() != null && MiscUtils.getStackTag(stack).hasKey("pos"))
			return EnumActionResult.PASS;

		if(world.getBlockState(pos).getBlock() == BlocksCore.rayTower && world.getBlockState(pos).getValue(BlockRayTower.LAYER).getIndexTwo() == 1)
		{
			pos = pos.down();
		}
		TileEntity t = world.getTileEntity(pos);
		if(t != null && !world.isRemote) {
			if(t.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null) && !world.isRemote) {
				ItemStack is = createTag(stack);
				MiscUtils.getStackTag(is).setIntArray("pos", new int[]{pos.getX(),pos.getY(),pos.getZ()});
				MiscUtils.getStackTag(is).setInteger("dim", player.dimension);
				MiscUtils.getStackTag(is).setBoolean("created", !player.isSneaking());
				if(stack.getCount() <= 0)
					player.inventory.setInventorySlotContents(player.inventory.currentItem, ItemStack.EMPTY);

				if(!player.inventory.addItemStackToInventory(is))
					player.dropItem(is, false);

				if(player.openContainer != null)
					player.openContainer.detectAndSendChanges();

				world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 1.0F, 2.0F);
				return EnumActionResult.SUCCESS;
			}
		}

		return EnumActionResult.PASS;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
	{
		ItemStack stack = player.getHeldItem(hand);
		if(stack.getTagCompound() != null && !world.isRemote && player.isSneaking())
		{
			if(stack.getTagCompound().getBoolean("created")) {
				stack.setTagCompound(null);
				world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.BLOCK_NOTE_BASS, SoundCategory.PLAYERS, 1.0F, 0.01F);
			}
			else {
				MiscUtils.getStackTag(stack).setBoolean("created", true);
			}
		}
		if(stack.getTagCompound() != null && world.isRemote && !player.isSneaking())
		{
			int[] c = MiscUtils.getStackTag(stack).getIntArray("pos");
			currentlyClicked = new Coord3D(c[0],c[1],c[2]);
			clickTicks = 100;
		}
		return super.onItemRightClick(world, player, hand);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World player, List<String> list, ITooltipFlag par4)
	{
		if(stack.getTagCompound() != null)
		{
			int[] coord = MiscUtils.getStackTag(stack).getIntArray("pos");
			list.add("Currently Bound To Inventory At:");
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

	@Override
	public EnumRarity getRarity(ItemStack stack)
	{
		return stack.getTagCompound() != null ? EnumRarity.EPIC : EnumRarity.COMMON;
	}

	public ItemStack createTag(ItemStack stack)
	{
		ItemStack retStk = stack.copy();
		retStk.setCount(1);
		stack.shrink(1);

		if(retStk.getTagCompound() == null)
		{
			NBTTagCompound tag = new NBTTagCompound();
			tag.setIntArray("pos", new int[]{0,0,0});
			return retStk;
		}
		return retStk;
	}

	@Override
	public int getColorFromItemstack(ItemStack stk, int pass) {
		return 0xff66ff;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels() {
		ModelLoader.setCustomMeshDefinition(this, MeshDefinitionBoundGem.INSTANCE);
		ModelBakery.registerItemVariants(this, new ModelResourceLocation("essentialcraft:item/bound_gem", "active=true"), new ModelResourceLocation("essentialcraft:item/bound_gem", "active=false"));
	}
}
