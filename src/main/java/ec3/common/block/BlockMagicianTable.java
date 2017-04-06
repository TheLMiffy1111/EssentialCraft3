package ec3.common.block;

import DummyCore.Client.IModelRegisterer;
import DummyCore.Utils.MiscUtils;
import ec3.api.MagicianTableUpgrades;
import ec3.common.mod.EssentialCraftCore;
import ec3.common.tile.TileMagicianTable;
import ec3.utils.cfg.Config;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.item.EntityItem;
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

public class BlockMagicianTable extends BlockContainer implements IModelRegisterer {
	
	public BlockMagicianTable() {
		super(Material.ROCK);
	}
	
	public BlockMagicianTable(Material p_i45394_1_) {
		super(p_i45394_1_);
	}
	
	@Override
    public void breakBlock(World par1World, BlockPos par2Pos, IBlockState par3State) {
		MiscUtils.dropItemsOnBlockBreak(par1World, par2Pos.getX(), par2Pos.getY(), par2Pos.getZ(), par3State.getBlock(), 0);
		TileMagicianTable table = (TileMagicianTable) par1World.getTileEntity(par2Pos);
		if(table.upgrade != -1) {
			ItemStack dropped = MagicianTableUpgrades.createStackByUpgradeID(table.upgrade);
			if(dropped != null) {
				if(dropped.stackSize == 0)dropped.stackSize = 1;
				EntityItem itm = new EntityItem(par1World, par2Pos.getX()+0.5D, par2Pos.getY()+1.5D, par2Pos.getZ()+0.5D, dropped);
				itm.setPickupDelay(30);;
				table.upgrade = -1;
				table.syncTick = 0;
				par1World.spawnEntity(itm);
			}
		}
		super.breakBlock(par1World, par2Pos, par3State);
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

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileMagicianTable();
	}
	
	@Override
	public boolean onBlockActivated(World par1World, BlockPos par2, IBlockState par3, EntityPlayer par4EntityPlayer, EnumHand par5, ItemStack par6, EnumFacing par7, float par8, float par9, float par10) {
		if(par1World.isRemote) {
			return true;
		}
		else {
			if(!par4EntityPlayer.isSneaking()) {
				ItemStack currentItem = par6;
				if(currentItem == null || !MagicianTableUpgrades.isItemUpgrade(currentItem)) {
					par4EntityPlayer.openGui(EssentialCraftCore.core, Config.guiID[0], par1World, par2.getX(), par2.getY(), par2.getZ());
					return true;
				}
				else {
					TileMagicianTable table = (TileMagicianTable) par1World.getTileEntity(par2);
					if(table.upgrade != -1) {
						ItemStack dropped = MagicianTableUpgrades.createStackByUpgradeID(table.upgrade);
						if(dropped != null) {
							if(dropped.stackSize == 0)
								dropped.stackSize = 1;
							EntityItem itm = new EntityItem(par1World, par2.getX()+0.5D, par2.getY()+1.5D, par2.getZ()+0.5D, dropped);
							itm.setPickupDelay(30);
							table.upgrade = -1;
							table.syncTick = 0;
							par1World.spawnEntity(itm);
						}
					}
					else {
						table.upgrade = MagicianTableUpgrades.getUpgradeIDByItemStack(currentItem);
						table.syncTick = 0;
						par4EntityPlayer.inventory.decrStackSize(par4EntityPlayer.inventory.currentItem, 1);
					}
					return true;
				}
			}
			else {
				TileMagicianTable table = (TileMagicianTable)par1World.getTileEntity(par2);
				if(table.upgrade != -1) {
					ItemStack dropped = MagicianTableUpgrades.createStackByUpgradeID(table.upgrade);
					if(dropped != null) {
						if(dropped.stackSize == 0)dropped.stackSize = 1;
						EntityItem itm = new EntityItem(par1World, par2.getZ()+0.5D, par2.getY()+1.5D, par2.getZ()+0.5D, dropped);
						itm.setPickupDelay(30);
						table.upgrade = -1;
						table.syncTick = 0;
						par1World.spawnEntity(itm);
					}
				}
				return false;
			}
		}
	}

	@Override
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation("essentialcraft:magicianTable", "inventory"));
	}
}
