package essentialcraft.common.block;

import java.util.Random;

import DummyCore.Client.IModelRegisterer;
import essentialcraft.common.item.ItemsCore;
import essentialcraft.common.tile.TileMagicalDisplay;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

public class BlockMagicalDisplay extends BlockContainer implements IModelRegisterer {

	public static final PropertyDirection FACING = PropertyDirection.create("facing");

	public BlockMagicalDisplay() {
		super(Material.ROCK, MapColor.AIR);
		setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.DOWN));
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return ItemsCore.genericItem;
	}

	@Override
	public int damageDropped(IBlockState p_149692_1_)
	{
		return 27;
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState blockstate) {
		IInventory inv = (IInventory)world.getTileEntity(pos);
		InventoryHelper.dropInventoryItems(world, pos, inv);
		super.breakBlock(world, pos, blockstate);
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		return new ItemStack(ItemsCore.genericItem,1,27);
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileMagicalDisplay();
	}

	@Override
	public boolean onBlockActivated(World w, BlockPos pos, IBlockState par3, EntityPlayer p, EnumHand par5, EnumFacing p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_)
	{
		ItemStack is = p.getHeldItem(par5);
		TileMagicalDisplay display = (TileMagicalDisplay)w.getTileEntity(pos);
		if(!is.isEmpty())
		{
			if(display.getStackInSlot(0).isEmpty())
			{
				ItemStack sett = is.copy();
				sett.setCount(1);
				display.setInventorySlotContents(0, sett);
				p.inventory.decrStackSize(p.inventory.currentItem, 1);

			}else {
				ItemStack dropped = display.getStackInSlot(0);
				if(!dropped.isEmpty() && !w.isRemote) {
					if(dropped.getCount() == 0)dropped.setCount(1);
					EntityItem itm = new EntityItem(w, pos.getX()+0.5D, pos.getY()+0.5D, pos.getZ()+0.5D, dropped);
					itm.setPickupDelay(30);
					display.setInventorySlotContents(0, ItemStack.EMPTY);
					w.spawnEntity(itm);
				}
			}
			display.syncTick = 0;
		}
		else
		{
			if(p.isSneaking())
			{
				ItemStack dropped = display.getStackInSlot(0);
				if(!dropped.isEmpty() && !w.isRemote) {
					if(dropped.getCount() == 0)dropped.setCount(1);
					EntityItem itm = new EntityItem(w, pos.getX()+0.5D, pos.getY()+0.5D, pos.getZ()+0.5D, dropped);
					itm.setPickupDelay(30);
					display.setInventorySlotContents(0, ItemStack.EMPTY);
					w.spawnEntity(itm);
					display.syncTick = 1;
				}
			}
			else
			{
				++display.type;
				if(display.type >= 3)
					display.type = 0;
				display.syncTick = 1;
			}
		}
		return true;
	}

	@Override
	public IBlockState getStateForPlacement(World w, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, int meta, EntityLivingBase p)
	{
		return this.getDefaultState().withProperty(FACING, side);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		int metadata = state.getValue(FACING).getIndex();
		if(metadata == 0)
		{
			return new AxisAlignedBB(0, 0.95F, 0, 1, 1, 1F);
		}
		if(metadata == 1)
		{
			return new AxisAlignedBB(0, 0, 0, 1, 0.05F, 1F);
		}
		if(metadata == 2)
		{
			return new AxisAlignedBB(0, 0, 0.95F, 1, 1, 1F);
		}
		if(metadata == 3)
		{
			return new AxisAlignedBB(0, 0, 0.0F, 1, 1, 0.05F);
		}
		if(metadata == 4)
		{
			return new AxisAlignedBB(0.95F, 0, 0, 1, 1, 1F);
		}
		if(metadata == 5)
		{
			return new AxisAlignedBB(0, 0, 0, 0.05F, 1, 1);
		}
		return super.getBoundingBox(state, source, pos);
	}

	@Override
	public boolean isOpaqueCube(IBlockState s)
	{
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState s)
	{
		return false;
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public IBlockState withRotation(IBlockState state, Rotation rot) {
		return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
	}

	@Override
	public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
		return state.withRotation(mirrorIn.toRotation(state.getValue(FACING)));
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(FACING, EnumFacing.getFront(meta%6));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(FACING).getIndex();
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING);
	}

	@Override
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation("essentialcraft:magicaldisplay", "inventory"));
	}
}
