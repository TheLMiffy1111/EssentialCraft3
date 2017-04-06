package ec3.common.block;

import DummyCore.Client.IModelRegisterer;
import DummyCore.Utils.MiscUtils;
import ec3.common.mod.EssentialCraftCore;
import ec3.common.tile.TileMagicalHopper;
import ec3.utils.cfg.Config;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

public class BlockMagicalHopper extends BlockContainer implements IModelRegisterer {
	
	public static final PropertyDirection FACING = PropertyDirection.create("facing");
	
	public BlockMagicalHopper() {
		super(Material.ROCK);
		setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.DOWN));
	}
	
	@Override
    public void breakBlock(World par1World, BlockPos par2Pos, IBlockState par3State) {
		MiscUtils.dropItemsOnBlockBreak(par1World, par2Pos.getX(), par2Pos.getY(), par2Pos.getZ(), par3State.getBlock(), 0);
		super.breakBlock(par1World, par2Pos, par3State);
    }
	
	public EnumBlockRenderType getRenderType(IBlockState s)
	{
		return EnumBlockRenderType.MODEL;
	}
	
	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int metadata) {
		return new TileMagicalHopper();
	}
	
    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return this.getDefaultState().withProperty(FACING, facing);
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
	public boolean onBlockActivated(World par1World, BlockPos par2, IBlockState par3, EntityPlayer par4EntityPlayer, EnumHand par5, ItemStack par6, EnumFacing par7, float par8, float par9, float par10) {
	    if(par1World.isRemote) {
	        return true;
	    }
	    else {
	     	if(!par4EntityPlayer.isSneaking()) {
	       		par4EntityPlayer.openGui(EssentialCraftCore.core, Config.guiID[0], par1World, par2.getX(), par2.getY(), par2.getZ());
	           	return true;
	       	}
	       	else {
	       		return false;
	       	}
	    }
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING);
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
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation("essentialcraft:magicalHopper", "inventory"));
	}
}
