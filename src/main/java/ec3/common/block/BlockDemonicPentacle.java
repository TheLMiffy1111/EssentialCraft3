package ec3.common.block;

import DummyCore.Client.IModelRegisterer;
import ec3.common.entity.EntityDemon;
import ec3.common.tile.TileDemonicPentacle;
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
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

public class BlockDemonicPentacle extends BlockContainer implements IModelRegisterer {

	public static final AxisAlignedBB BLOCK_AABB = new AxisAlignedBB(0,0,0,1,0.625F,1);

	public BlockDemonicPentacle() {
		super(Material.ROCK);
	}

	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos)
	{
		return BLOCK_AABB;
	}

	public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos)
	{
		return BLOCK_AABB.offset(pos);
	}

	public boolean isOpaqueCube(IBlockState s)
	{
		return false;
	}

	@Override
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.SOLID;
	}

	public boolean isFullCube(IBlockState s)
	{
		return false;
	}

	public EnumBlockRenderType getRenderType(IBlockState s)
	{
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileDemonicPentacle();
	}

	@Override
	public boolean onBlockActivated(World par1World, BlockPos par2, IBlockState par3, EntityPlayer par4EntityPlayer, EnumHand par5, ItemStack par6, EnumFacing par7, float par8, float par9, float par10) {
		TileDemonicPentacle pentacle = (TileDemonicPentacle)par1World.getTileEntity(par2);
		if(pentacle.consumeEnderstarEnergy(666)) {
			EntityDemon demon = new EntityDemon(par1World);
			demon.setPositionAndRotation(par2.getX()+0.5D, par2.getY()+0.1D, par2.getZ()+0.5D, 0, 0);
			demon.playLivingSound();
			if(!par1World.isRemote)
				par1World.spawnEntity(demon);
		}
		else {
			if(par1World.isRemote)
				par4EntityPlayer.sendMessage(new TextComponentString(I18n.translateToLocal("ec3.txt.noEnergy")).setStyle(new Style().setColor(TextFormatting.RED)));
		}
		return true;
	}

	@Override
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation("essentialcraft:demonicPentacle", "inventory"));
	}
}
