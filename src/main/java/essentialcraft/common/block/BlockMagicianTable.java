package essentialcraft.common.block;

import DummyCore.Client.IModelRegisterer;
import essentialcraft.api.MagicianTableUpgrades;
import essentialcraft.common.mod.EssentialCraftCore;
import essentialcraft.common.tile.TileMagicianTable;
import essentialcraft.utils.cfg.Config;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

public class BlockMagicianTable extends BlockContainer implements IModelRegisterer {

	public BlockMagicianTable() {
		super(Material.ROCK, MapColor.PURPLE);
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState blockstate) {
		TileMagicianTable table = (TileMagicianTable) world.getTileEntity(pos);
		InventoryHelper.dropInventoryItems(world, pos, table);
		if(table.upgrade != -1) {
			ItemStack dropped = MagicianTableUpgrades.createStackByUpgradeID(table.upgrade);
			InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), dropped);
		}
		super.breakBlock(world, pos, blockstate);
	}

	@Override
	public boolean isOpaqueCube(IBlockState s)
	{
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState s)
	{
		return false;
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int p_149915_2_) {
		return new TileMagicianTable();
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos par2, IBlockState par3, EntityPlayer player, EnumHand par5, EnumFacing par7, float par8, float par9, float par10) {
		ItemStack currentItem = player.getHeldItem(par5);
		if(currentItem.isEmpty() || !MagicianTableUpgrades.isItemUpgrade(currentItem)) {
			TileMagicianTable table = (TileMagicianTable)world.getTileEntity(par2);
			if(player.isSneaking()) {
				if(table.upgrade != -1) {
					ItemStack dropped = MagicianTableUpgrades.createStackByUpgradeID(table.upgrade);
					if(!dropped.isEmpty()) {
						if(!world.isRemote) {
							EntityItem itm = new EntityItem(world, par2.getX()+0.5D, par2.getY()+1.5D, par2.getZ()+0.5D, dropped);
							itm.setPickupDelay(30);
							table.upgrade = -1;
							table.syncTick = 0;
							world.spawnEntity(itm);
						}
						return true;
					}
				}
				return false;
			}

			if(!world.isRemote) {
				player.openGui(EssentialCraftCore.core, Config.guiID[0], world, par2.getX(), par2.getY(), par2.getZ());
				return true;
			}
			return true;
		}
		else if(!player.isSneaking()) {
			TileMagicianTable table = (TileMagicianTable) world.getTileEntity(par2);
			if(table.upgrade == -1) {
				table.upgrade = MagicianTableUpgrades.getUpgradeIDByItemStack(currentItem);
				table.syncTick = 0;
				player.inventory.decrStackSize(player.inventory.currentItem, 1);
			}
			return true;
		}
		return false;
	}

	@Override
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation("essentialcraft:magiciantable", "inventory"));
	}
}
