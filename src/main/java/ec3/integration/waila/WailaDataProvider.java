package ec3.integration.waila;

import java.util.List;

import ec3.api.ITEHasMRU;
import ec3.common.block.BlockMimic;
import ec3.common.block.BlockRightClicker;
import ec3.common.item.ItemBoundGem;
import ec3.common.tile.TileMimic;
import ec3.common.tile.TileRightClicker;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;

public class WailaDataProvider implements IWailaDataProvider {

	@Override
	public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
		if(accessor.getTileEntity() != null) {
			if(accessor.getTileEntity() instanceof TileRightClicker) {
				TileRightClicker tile = (TileRightClicker)accessor.getTileEntity();
				return tile.getStackInSlot(10) != null && tile.getStackInSlot(10).getItem() instanceof ItemBlock ? tile.getStackInSlot(10) : null;
			}
			if(accessor.getTileEntity() instanceof TileMimic) {
				TileMimic tile = (TileMimic)accessor.getTileEntity();
				return tile.getState() != null ? tile.getState().getBlock().getPickBlock(tile.getState(), accessor.getMOP(), accessor.getWorld(), accessor.getPosition(), accessor.getPlayer()) : null;
			}
		}
		return null;
	}

	@Override
	public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
		return currenttip;
	}

	@Override
	public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
		if(accessor.getTileEntity() != null) {
			if(accessor.getTileEntity() instanceof TileRightClicker && ((IInventory)accessor.getTileEntity()).getStackInSlot(10) != null)
				return currenttip;
			if(accessor.getTileEntity() instanceof ITEHasMRU) {
				ITEHasMRU tile = (ITEHasMRU)accessor.getTileEntity();
				if(tile.getMaxMRU() > 0) {
					currenttip.add("MRU: " + tile.getMRU() + "/" + tile.getMaxMRU());
					float balance = tile.getBalance();
					String str = Float.toString(((ITEHasMRU)tile).getBalance());
					if(str.length() > 6)
						str = str.substring(0, 6);
					for(int i = str.length()-1; i > 2; --i) {
						char c = str.charAt(i);
						if(c == '0')
							str = str.substring(0, i);
					}
					TextFormatting color = TextFormatting.AQUA;
					if(balance < 1)
						color = TextFormatting.BLUE;
					if(balance > 1)
						color = TextFormatting.RED;
					currenttip.add("Balance: " + color + str);
					if(accessor.getTileEntity() instanceof IInventory) {
						IInventory tInv = (IInventory)accessor.getTileEntity();
						if(tInv.getSizeInventory() > 0) {
							ItemStack tryBoundGem = tInv.getStackInSlot(0);
							if(tryBoundGem != null) {
								if(tryBoundGem.getItem() instanceof ItemBoundGem) {
									ItemBoundGem itm = (ItemBoundGem)tryBoundGem.getItem();
									itm.addInformation(tryBoundGem, null, currenttip, true);
								}
							}
						}
					}
				}
			}
		}

		return currenttip;
	}

	@Override
	public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
		return currenttip;
	}

	@Override
	public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, BlockPos pos) {
		return tag;
	}

	public static void callbackRegister(IWailaRegistrar registrar) {
		WailaDataProvider wdp = new WailaDataProvider();
		registrar.registerBodyProvider(wdp, Block.class);
		registrar.registerStackProvider(wdp, BlockRightClicker.class);
		registrar.registerStackProvider(wdp, BlockMimic.class);
	}
}
