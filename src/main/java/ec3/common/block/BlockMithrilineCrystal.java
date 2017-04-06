package ec3.common.block;

import java.util.List;

import DummyCore.Client.IModelRegisterer;
import DummyCore.Client.ModelUtils;
import DummyCore.Utils.EnumLayer;
import ec3.common.tile.TileMithrilineCrystal;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Loader;

public class BlockMithrilineCrystal extends BlockContainer implements IModelRegisterer {

	public static final PropertyEnum<CrystalType> TYPE = PropertyEnum.<CrystalType>create("type", CrystalType.class);
	public static final PropertyEnum<EnumLayer> LAYER = PropertyEnum.<EnumLayer>create("layer", EnumLayer.class, EnumLayer.LAYERTHREE);
	
	public BlockMithrilineCrystal(Material p_i45394_1_) {
		super(p_i45394_1_);
		this.setSoundType(SoundType.GLASS);
		setDefaultState(blockState.getBaseState().withProperty(TYPE, CrystalType.MITHRILINE).withProperty(LAYER, EnumLayer.BOTTOM));
	}

    public int damageDropped(IBlockState s)
    {
    	return s.getValue(TYPE).getIndex()*3;
    }
	
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
    {
        return new ItemStack(this, 1, state.getValue(TYPE).getIndex()*3);
    }
    
	public BlockMithrilineCrystal() {
		super(Material.ROCK);
		this.setSoundType(SoundType.GLASS);
	}
	
    public void getSubBlocks(Item p_149666_1_, CreativeTabs p_149666_2_, List<ItemStack> p_149666_3_)
    {
        p_149666_3_.add(new ItemStack(p_149666_1_, 1, 0));
        p_149666_3_.add(new ItemStack(p_149666_1_, 1, 3));
        p_149666_3_.add(new ItemStack(p_149666_1_, 1, 6));
        p_149666_3_.add(new ItemStack(p_149666_1_, 1, 9));
        p_149666_3_.add(new ItemStack(p_149666_1_, 1, 12));
    }
	
	@Override
	public void breakBlock(World par1World, BlockPos par2Pos, IBlockState par3State) {
		super.breakBlock(par1World, par2Pos, par3State);
		int par6 = par3State.getValue(LAYER).getIndexThree();
		if(par6 == 0)
		{
			par1World.setBlockToAir(par2Pos.up());
			par1World.setBlockToAir(par2Pos.up(2));
		}
		if(par6 == 1)
		{
			par1World.setBlockToAir(par2Pos.down());
			par1World.setBlockToAir(par2Pos.up());
		}
		if(par6 == 2)
		{
			par1World.setBlockToAir(par2Pos.down());
			par1World.setBlockToAir(par2Pos.down(2));
		}
    }
	
    public void onBlockAdded(World w, BlockPos p, IBlockState s)
    {
        super.onBlockAdded(w, p, s);
        int meta = getMetaFromState(s);
        if(meta%3 == 0)
        {
        	w.setBlockState(p.up(), getStateFromMeta(meta+1), 3);
        	w.setBlockState(p.up(2), getStateFromMeta(meta+1), 3);
        }
    }
    
    public boolean canPlaceBlockAt(World p_149742_1_, BlockPos p_149742_2_)
    {
        return p_149742_1_.getBlockState(p_149742_2_).getBlock().isReplaceable(p_149742_1_, p_149742_2_) && p_149742_1_.getBlockState(p_149742_2_.up()).getBlock().isReplaceable(p_149742_1_, p_149742_2_.up()) && p_149742_1_.getBlockState(p_149742_2_.up(2)).getBlock().isReplaceable(p_149742_1_, p_149742_2_.up(2));
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
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return p_149915_2_%3 == 0 ? new TileMithrilineCrystal() : null;
	}
	
    public float getEnchantPowerBonus(World world, BlockPos pos)
    {
    	if(world.getBlockState(pos).getValue(LAYER) == EnumLayer.BOTTOM) {
	    	int meta = world.getBlockState(pos).getValue(TYPE).getIndex();
	    	return meta == 0 ? 7.5F : meta == 1 ? 15 : meta == 2 ? 30 : meta == 3 ? 60 : meta == 4 ? 120 : 0;
    	}
    	else
    		return 0;
    }
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(TYPE, CrystalType.fromIndex(meta%15/3)).withProperty(LAYER, EnumLayer.fromIndexThree(meta%3));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(TYPE).getIndex()*3+state.getValue(LAYER).getIndexThree();
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, TYPE, LAYER);
	}

	@Override
	public void registerModels() {
		ModelLoader.setCustomStateMapper(this, new StateMap.Builder().ignore(LAYER).build());
		if(!Loader.isModLoaded("codechickenlib") && !Loader.isModLoaded("CodeChickenLib")) {
			for(int i = 0; i < 5; i++) {
				ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), i*3, new ModelResourceLocation("essentialcraft:mithrilineCrystalInv", "type=" + CrystalType.fromIndex(i).getName()));
			}
		}
		else {
			ModelUtils.setItemModelSingleIcon(Item.getItemFromBlock(this), "essentialcraft:mithrilineCrystal");
		}
	}
	
	public static enum CrystalType implements IStringSerializable {
		MITHRILINE(0,"mithriline"),
		PALE(1,"pale"),
		VOID(2,"void"),
		DEMONIC(3,"demonic"),
		SHADE(4,"shade");
		
		private int index;
		private String name;
		
		private CrystalType(int i, String s) {
			index = i;
			name = s;
		}
		
		public String getName() {
			return name;
		}
		
		public String toString() {
			return name;
		}
		
		public int getIndex() {
			return index;
		}
		
		public static CrystalType fromIndex(int i) {
			return values()[i];
		}
	}
}
