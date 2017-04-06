package ec3.common.block;

import DummyCore.Client.IModelRegisterer;
import ec3.common.tile.TileWindRune;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
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
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

public class BlockWindRune extends BlockContainer implements IModelRegisterer {

	public static final AxisAlignedBB BLOCK_AABB = new AxisAlignedBB(0, 0, 0, 1, 0.0625F, 1);

	public BlockWindRune() {
		super(Material.ROCK);
	}

	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return BLOCK_AABB;
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
		return BlockRenderLayer.SOLID;
	}

	public EnumBlockRenderType getRenderType(IBlockState s) {
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileWindRune();
	}

	@Override
	public boolean onBlockActivated(World par1World, BlockPos par2, IBlockState par3, EntityPlayer par4EntityPlayer, EnumHand par5, ItemStack par6, EnumFacing par7, float par8, float par9, float par10) {
		return ((TileWindRune)par1World.getTileEntity(par2)).action(par4EntityPlayer);
	}

	@Override
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation("essentialcraft:windRune", "inventory"));
	}
}
