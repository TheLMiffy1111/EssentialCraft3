package ec3.common.block;

import DummyCore.Client.IModelRegisterer;
import ec3.common.tile.TileMRUCoil_Hardener;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

public class BlockMRUCoil_Hardener extends BlockContainer implements IModelRegisterer {
	
	protected BlockMRUCoil_Hardener(Material p_i45386_1_) {
		super(p_i45386_1_);
	}
	
	public BlockMRUCoil_Hardener() {
		super(Material.ROCK);
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

	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileMRUCoil_Hardener();
	}

	public EnumBlockRenderType getRenderType(IBlockState s)
	{
		return EnumBlockRenderType.MODEL;
	}
	
	@Override
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation("essentialcraft:mruCoilHardener", "inventory"));
	}
}
