package essentialcraft.common.tile;

import DummyCore.Utils.MiscUtils;
import essentialcraft.api.ApiCore;
import essentialcraft.common.item.ItemSoulStone;
import essentialcraft.common.registry.SoundRegistry;
import essentialcraft.utils.common.ECUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.common.config.Configuration;

public class TileMatrixAbsorber extends TileMRUGeneric {

	public int sndTime;
	public static int cfgMaxMRU =  ApiCore.GENERATOR_MAX_MRU_GENERIC/10;
	public static float cfgBalance = 1F;
	public static int mruGenerated = 1;
	public static int mruUsage = 10;
	public static boolean requestImmidiateSync;

	public TileMatrixAbsorber() {
		super(cfgMaxMRU);
		mruStorage.setBalance(cfgBalance);
		slot0IsBoundGem = false;
		setSlotsNum(1);
	}

	public boolean canGenerateMRU() {
		return false;
	}

	@Override
	public void update() {
		super.update();
		boolean t = false;
		if(getWorld().isBlockIndirectlyGettingPowered(pos) == 0) {
			ItemStack stk = getStackInSlot(0);
			if(stk.getItem() instanceof ItemSoulStone) {
				if(stk.getTagCompound() != null) {
					String username = stk.getTagCompound().getString("playerName");
					if(getWorld().getMinecraftServer() != null && getWorld().getMinecraftServer().getPlayerList() != null) {
						EntityPlayer p = MiscUtils.getPlayerFromUUID(username);
						if(p != null) {
							if(mruStorage.getMRU() < mruStorage.getMaxMRU()) {
								int current = ECUtils.getData(p).getPlayerUBMRU();
								if(current - mruUsage >= 0) {
									ECUtils.getData(p).modifyUBMRU(current - mruUsage);
									if(requestImmidiateSync) {
										ECUtils.requestSync(p);
										syncTick = 0;
									}
									mruStorage.addMRU(mruGenerated, true);
									for(int o = 0; o < 10; ++o) {
										getWorld().spawnParticle(EnumParticleTypes.REDSTONE, pos.getX()+0.25D+getWorld().rand.nextDouble()/2.2D, pos.getY()+0.25D+(float)o/20, pos.getZ()+0.25D+getWorld().rand.nextDouble()/2.2D, 1.0D, 0.0D, 1.0D);
									}
									t = true;
								}
							}
						}
					}
				}
			}
			--sndTime;
			if(t && sndTime <= 0) {
				sndTime = 400;
				getWorld().playSound(pos.getX()+0.5D, pos.getY()+0.5D, pos.getZ()+0.5D, SoundRegistry.machineDeepNoise, SoundCategory.BLOCKS, 0.01F, 2F, false);
			}
			if(!t)
				sndTime = 0;
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound i) {
		super.readFromNBT(i);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound i) {
		return super.writeToNBT(i);
	}

	public static void setupConfig(Configuration cfg) {
		try {
			String category = "tileentities.matrixabsorber";
			cfgMaxMRU = cfg.get(category, "MaxMRU", ApiCore.GENERATOR_MAX_MRU_GENERIC/10).setMinValue(1).getInt();
			cfgBalance = (float)cfg.get(category, "Balance", 1D).setMinValue(0D).setMaxValue(2D).getDouble();
			mruGenerated = cfg.get(category, "MRUGenerated", 1).setMinValue(0).getInt();
			mruUsage = cfg.get(category, "UBMRUUsage", 10).setMinValue(0).getInt();
			requestImmidiateSync = cfg.get(category, "RequestImmediateSync", true).getBoolean();
		}
		catch(Exception e) {
			return;
		}
	}

	@Override
	public int[] getOutputSlots() {
		return new int[] {0};
	}
}
