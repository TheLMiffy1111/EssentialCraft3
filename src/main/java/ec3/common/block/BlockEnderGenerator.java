package ec3.common.block;

import DummyCore.Client.IModelRegisterer;
import ec3.common.mod.EssentialCraftCore;
import ec3.common.tile.TileEnderGenerator;
import ec3.utils.cfg.Config;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
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
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Loader;

public class BlockEnderGenerator extends BlockContainer implements IModelRegisterer {

	protected BlockEnderGenerator(Material p_i45394_1_) {
		super(p_i45394_1_);
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
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEnderGenerator();
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
	public void registerModels() {
		if(Loader.isModLoaded("codechickenlib") || Loader.isModLoaded("CodeChickenLib")) {
			ModelLoader.setCustomStateMapper(this, new StateMapperBase() {
				@Override
				protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
					return new ModelResourceLocation("essentialcraft:enderGeneratorTemp", "normal");
				}
			});
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation("essentialcraft:enderGeneratorTemp", "inventory"));
			return;
		}
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation("essentialcraft:enderGenerator", "inventory"));
	}
}
