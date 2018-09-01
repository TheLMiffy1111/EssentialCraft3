package essentialcraft.common.block;

import DummyCore.Client.IModelRegisterer;
import essentialcraft.common.mod.EssentialCraftCore;
import essentialcraft.common.tile.TileDarknessObelisk;
import essentialcraft.utils.cfg.Config;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Loader;

public class BlockDarknessObelisk extends BlockContainer implements IModelRegisterer {

	public static final AxisAlignedBB BLOCK_AABB = new AxisAlignedBB(0,0,0,1,2,1);

	public BlockDarknessObelisk() {
		super(Material.ROCK, MapColor.BLACK);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
	{
		return BLOCK_AABB;
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState blockstate) {
		IInventory inv = (IInventory)world.getTileEntity(pos);
		InventoryHelper.dropInventoryItems(world, pos, inv);
		super.breakBlock(world, pos, blockstate);
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
	public EnumBlockRenderType getRenderType(IBlockState s)
	{
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileDarknessObelisk();
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos par2, IBlockState par3, EntityPlayer player, EnumHand par5, EnumFacing par7, float par8, float par9, float par10) {
		if(player.isSneaking()) {
			return false;
		}
		if(!world.isRemote) {
			player.openGui(EssentialCraftCore.core, Config.guiID[0], world, par2.getX(), par2.getY(), par2.getZ());
			return true;
		}
		return true;
	}

	@Override
	public void registerModels() {
		if(Loader.isModLoaded("codechickenlib")) {
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation("essentialcraft:darknessobelisktemp", "inventory"));
			return;
		}
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation("essentialcraft:darknessobelisk", "inventory"));
	}
}
