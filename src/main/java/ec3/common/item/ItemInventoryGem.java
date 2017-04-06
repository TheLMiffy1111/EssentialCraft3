package ec3.common.item;

import java.util.List;

import DummyCore.Client.IModelRegisterer;
import DummyCore.Utils.Coord3D;
import DummyCore.Utils.MiscUtils;
import ec3.common.block.BlockRayTower;
import ec3.common.block.BlocksCore;
import ec3.common.item.ItemBoundGem.MeshDefinitionBoundGem;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
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

public class ItemInventoryGem extends Item implements IItemColor, IModelRegisterer {

	public static Coord3D currentlyClicked;
	public static int clickTicks;

	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if(stack.getTagCompound() != null && MiscUtils.getStackTag(stack).hasKey("pos"))
			return EnumActionResult.PASS;

		if(world.getBlockState(pos).getBlock() == BlocksCore.rayTower && world.getBlockState(pos).getValue(BlockRayTower.LAYER).getIndexTwo() == 1)
		{
			pos = pos.down();
		}
		TileEntity t = world.getTileEntity(pos);
		if(t != null && !world.isRemote)
		{
			if(t instanceof IInventory && !world.isRemote)
			{
				ItemStack is = createTag(stack);
				MiscUtils.getStackTag(is).setIntArray("pos", new int[]{pos.getX(),pos.getY(),pos.getZ()});
				MiscUtils.getStackTag(is).setInteger("dim", player.dimension);
				MiscUtils.getStackTag(is).setBoolean("created", !player.isSneaking());
				world.playSound(player, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 1.0F, 2.0F);
				if(stack.stackSize <= 0)
					player.inventory.setInventorySlotContents(player.inventory.currentItem, null);

				if(!player.inventory.addItemStackToInventory(is))
					player.dropItem(is, false);

				if(player.openContainer != null)
					player.openContainer.detectAndSendChanges();

				return EnumActionResult.SUCCESS;
			}
		}

		return EnumActionResult.PASS;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer, EnumHand hand)
	{
		if(par1ItemStack.getTagCompound() != null && !par2World.isRemote && par3EntityPlayer.isSneaking())
		{
			if(par1ItemStack.getTagCompound().getBoolean("created")) {
				par1ItemStack.setTagCompound(null);
				par2World.playSound(par3EntityPlayer.posX, par3EntityPlayer.posY, par3EntityPlayer.posZ, SoundEvents.ENTITY_PLAYER_BREATH, SoundCategory.PLAYERS, 1.0F, 0.01F, false);
			}
			else {
				MiscUtils.getStackTag(par1ItemStack).setBoolean("created", true);
			}
		}
		if(par1ItemStack.getTagCompound() != null && par2World.isRemote && !par3EntityPlayer.isSneaking())
		{
			int[] c = MiscUtils.getStackTag(par1ItemStack).getIntArray("pos");
			currentlyClicked = new Coord3D(c[0],c[1],c[2]);
			clickTicks = 100;
		}
		return new ActionResult(EnumActionResult.PASS, par1ItemStack);
	}

	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List<String> par3List, boolean par4) 
	{
		if(par1ItemStack.getTagCompound() != null)
		{
			int[] coord = MiscUtils.getStackTag(par1ItemStack).getIntArray("pos");
			par3List.add("Currently Bound To Inventory At:");
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

	public EnumRarity getRarity(ItemStack par1ItemStack)
	{
		return par1ItemStack.getTagCompound() != null ? EnumRarity.EPIC : EnumRarity.COMMON;
	}	 

	public ItemStack createTag(ItemStack stack)
	{
		ItemStack retStk = stack.copy();
		retStk.stackSize = 1;
		stack.stackSize -= 1;

		if(retStk.getTagCompound() == null)
		{
			NBTTagCompound tag = new NBTTagCompound();
			tag.setIntArray("pos", new int[]{0,0,0});
			return retStk;
		}
		return retStk;
	}

	public int getColorFromItemstack(ItemStack stk, int pass) {
		return 0xff66ff;
	}

	@Override
	public void registerModels() {
		ModelLoader.setCustomMeshDefinition(this, MeshDefinitionBoundGem.INSTANCE);
		ModelBakery.registerItemVariants(this, new ModelResourceLocation("essentialcraft:item/bound_gem", "active=true"), new ModelResourceLocation("essentialcraft:item/bound_gem", "active=false"));
	}
}
