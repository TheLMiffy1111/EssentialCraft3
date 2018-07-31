package essentialcraft.common.block;

import DummyCore.Client.IModelRegisterer;
import DummyCore.Utils.EnumLayer;
import essentialcraft.common.mod.EssentialCraftCore;
import essentialcraft.common.tile.TileRayTower;
import essentialcraft.utils.cfg.Config;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

public class BlockRayTower extends BlockContainer implements IModelRegisterer {

	public static final PropertyEnum<EnumLayer> LAYER = PropertyEnum.<EnumLayer>create("layer", EnumLayer.class, EnumLayer.LAYERTWO);

	protected BlockRayTower() {
		super(Material.IRON);
		setDefaultState(this.blockState.getBaseState().withProperty(LAYER, EnumLayer.BOTTOM));
	}

	@Override
	public void onBlockAdded(World w, BlockPos p, IBlockState s)
	{
		super.onBlockAdded(w, p, s);
		if(w.isAirBlock(p.up()))
		{
			if(s.getValue(LAYER) == EnumLayer.BOTTOM)
			{
				w.setBlockState(p.up(), this.getDefaultState().withProperty(LAYER, EnumLayer.TOP),3);
			}
		}
	}

	@Override
	public boolean canPlaceBlockAt(World p_149742_1_, BlockPos p_149742_2_)
	{
		return p_149742_1_.getBlockState(p_149742_2_).getBlock().isReplaceable(p_149742_1_, p_149742_2_) && p_149742_1_.isAirBlock(p_149742_2_.up());
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
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT_MIPPED;
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState s)
	{
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos par2, IBlockState par3, EntityPlayer player, EnumHand par5, EnumFacing par7, float par8, float par9, float par10) {
		if(player.isSneaking()) {
			return false;
		}
		if(!world.isRemote) {
			if(par3.getValue(LAYER) == EnumLayer.BOTTOM) {
				player.openGui(EssentialCraftCore.core, Config.guiID[0], world, par2.getX(), par2.getY(), par2.getZ());
			}
			else {
				player.openGui(EssentialCraftCore.core, Config.guiID[0], world, par2.getX(), par2.getY()-1, par2.getZ());
			}
			return true;
		}
		return true;
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState blockstate) {
		IInventory inv = (IInventory)world.getTileEntity(pos);
		InventoryHelper.dropInventoryItems(world, pos, inv);
		if(world.getBlockState(pos.down()).getBlock() == this) {
			world.setBlockToAir(pos.down());
		}
		if(world.getBlockState(pos.up()).getBlock() == this) {
			world.setBlockToAir(pos.up());
		}
		super.breakBlock(world, pos, blockstate);
		world.removeTileEntity(pos);
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileRayTower();
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(LAYER, EnumLayer.fromIndexTwo(meta%2));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(LAYER).getIndexTwo();
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, LAYER);
	}

	@Override
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation("essentialcraft:raytower", "inventory"));
	}
}
