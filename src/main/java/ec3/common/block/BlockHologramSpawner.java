package ec3.common.block;

import DummyCore.Client.IModelRegisterer;
import ec3.common.entity.EntityHologram;
import ec3.utils.cfg.Config;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

public class BlockHologramSpawner extends Block implements IModelRegisterer {

	public static final AxisAlignedBB BLOCK_AABB = new AxisAlignedBB(0,0,0,1,0.5F,1);

	public BlockHologramSpawner() 
	{
		super(Material.ROCK);
	}

	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return BLOCK_AABB;
	}

	public boolean onBlockActivated(World w, BlockPos p, IBlockState s, EntityPlayer ep, EnumHand h, ItemStack is, EnumFacing f, float hx, float hy, float hz) {
		if(!w.isRemote && w.provider != null && (w.provider.getDimension() == Config.dimensionID || Config.allowHologramInOtherDimensions)) {
			w.setBlockToAir(p);
			EntityHologram hg = new EntityHologram(w);
			hg.setPositionAndRotation(p.getX()+0.5D, p.getY()+5, p.getZ()+0.5D, 0, 0);
			w.spawnEntity(hg);
		}
		return true;
	}

	@Override
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation("essentialcraft:holopad", "inventory"));
	}
}
