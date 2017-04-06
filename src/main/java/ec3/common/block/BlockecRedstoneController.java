package ec3.common.block;

import DummyCore.Client.IModelRegisterer;
import ec3.common.tile.TileecRedstoneController;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

public class BlockecRedstoneController extends BlockContainer implements IModelRegisterer {

	protected BlockecRedstoneController(Material p_i45386_1_) {
		super(p_i45386_1_);
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileecRedstoneController();
	}

	public boolean onBlockActivated(World par1World, BlockPos par2, IBlockState par3, EntityPlayer par4EntityPlayer, EnumHand par5, ItemStack par6, EnumFacing par7, float par8, float par9, float par10) {
		if(par4EntityPlayer.isSneaking()) {
			TileecRedstoneController rc = (TileecRedstoneController)par1World.getTileEntity(par2);
			rc.setting += 1;
			if(rc.setting >= 11)
				rc.setting = 0;
			if(par4EntityPlayer.getEntityWorld().isRemote)
				par4EntityPlayer.sendMessage(new TextComponentString(I18n.translateToLocal("ec3.txt.redstone_"+rc.setting)));
		}
		else {
			TileecRedstoneController rc = (TileecRedstoneController)par1World.getTileEntity(par2);
			if(par4EntityPlayer.getEntityWorld().isRemote)
				par4EntityPlayer.sendMessage(new TextComponentString(I18n.translateToLocal("ec3.txt.redstone_"+rc.setting)));
		}
		return true;
	}

	public boolean canProvidePower(IBlockState s) {
		return true;
	}

	public int getWeakPower(IBlockState s, IBlockAccess w, BlockPos p, EnumFacing f){
		TileecRedstoneController rc = (TileecRedstoneController)w.getTileEntity(p);

		return rc.outputRedstone() ? 15 : 0;
	}

	public EnumBlockRenderType getRenderType(IBlockState s)
	{
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation("essentialcraft:ecRedstoneController", "inventory"));
	}
}
