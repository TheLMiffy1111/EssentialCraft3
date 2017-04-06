package ec3.common.block;

import DummyCore.Client.IModelRegisterer;
import DummyCore.Utils.EnumLayer;
import DummyCore.Utils.MiscUtils;
import ec3.common.mod.EssentialCraftCore;
import ec3.common.tile.TileRayTower;
import ec3.utils.cfg.Config;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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

	public boolean canPlaceBlockAt(World p_149742_1_, BlockPos p_149742_2_)
	{
		return p_149742_1_.getBlockState(p_149742_2_).getBlock().isReplaceable(p_149742_1_, p_149742_2_) && p_149742_1_.isAirBlock(p_149742_2_.up());
	}

	public boolean isOpaqueCube(IBlockState s)
	{
		return false;
	}

	public boolean isFullCube(IBlockState s)
	{
		return false;
	}

	@Override
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT_MIPPED;
	}
	
	public EnumBlockRenderType getRenderType(IBlockState s)
	{
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public boolean onBlockActivated(World par1World, BlockPos par2, IBlockState par3, EntityPlayer par4EntityPlayer, EnumHand par5, ItemStack par6, EnumFacing par7, float par8, float par9, float par10) {
		if (par1World.isRemote)
		{
			return true;
		}else
		{
			if(!par4EntityPlayer.isSneaking())
			{
				if(par3.getValue(LAYER) == EnumLayer.BOTTOM)
				{
					par4EntityPlayer.openGui(EssentialCraftCore.core, Config.guiID[0], par1World, par2.getX(), par2.getY(), par2.getZ());
				}else
				{
					par4EntityPlayer.openGui(EssentialCraftCore.core, Config.guiID[0], par1World, par2.getX(), par2.getY()-1, par2.getZ());
				}
				return true;
			}else
			{
				return false;
			}
		}
	}

	@Override
	public void breakBlock(World par1World, BlockPos par2Pos, IBlockState par3State) {
		MiscUtils.dropItemsOnBlockBreak(par1World, par2Pos.getX(), par2Pos.getY(), par2Pos.getZ(), par3State.getBlock(), par3State.getValue(LAYER).getIndexTwo());
		if(par1World.getBlockState(par2Pos.down()).getBlock() == this)
		{
			par1World.setBlockToAir(par2Pos.down());
		}
		if(par1World.getBlockState(par2Pos.up()).getBlock() == this)
		{
			par1World.setBlockToAir(par2Pos.up());
		}
		super.breakBlock(par1World, par2Pos, par3State);
		par1World.removeTileEntity(par2Pos);
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
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation("essentialcraft:rayTower", "inventory"));
	}
}
