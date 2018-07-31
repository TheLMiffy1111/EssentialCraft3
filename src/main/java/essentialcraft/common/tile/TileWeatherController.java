package essentialcraft.common.tile;

import essentialcraft.api.ApiCore;
import essentialcraft.common.item.ItemsCore;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.common.config.Configuration;

public class TileWeatherController extends TileMRUGeneric {

	public static int cfgMaxMRU = ApiCore.DEVICE_MAX_MRU_GENERIC;
	public static int mruUsage = 25;
	public static int requiredTicks = 200;

	public int progressLevel = 0;

	public TileWeatherController() {
		super(cfgMaxMRU);
		setSlotsNum(3);
	}

	@Override
	public void update() {
		super.update();
		mruStorage.update(getPos(), getWorld(), getStackInSlot(0));
		if(world.isBlockIndirectlyGettingPowered(pos) == 0) {
			if(getStackInSlot(2).isEmpty() || (getStackInSlot(2).getItem() == Items.GLASS_BOTTLE && getStackInSlot(2).getCount() < getInventoryStackLimit() && getStackInSlot(2).getCount() < getStackInSlot(2).getMaxStackSize())) {
				if(getStackInSlot(1).getItem() == ItemsCore.clearing_catalyst && world.isRaining()) {
					if(mruStorage.getMRU() >= mruUsage) {
						mruStorage.extractMRU(mruUsage, true);
						++progressLevel;
						if(world.isRemote) {
							world.playSound(pos.getX()+0.5D, pos.getY()+0.5D, pos.getZ()+0.5D, SoundEvents.ENTITY_MINECART_RIDING, SoundCategory.BLOCKS, 0.1F, 0.8F+(float)progressLevel/requiredTicks, false);
						}
						if(progressLevel >= requiredTicks) {
							progressLevel = 0;
							decrStackSize(1, 1);
							if(getStackInSlot(2).isEmpty()) {
								setInventorySlotContents(2, new ItemStack(Items.GLASS_BOTTLE));
							}
							else {
								getStackInSlot(2).grow(1);
							}
							if(world.provider.hasSkyLight() && world.getGameRules().getBoolean("doWeatherCycle")) {
								if(!world.isRemote) {
									int time = (300 + world.rand.nextInt(600)) * 20;
									WorldInfo worldinfo = world.getWorldInfo();
									worldinfo.setCleanWeatherTime(time);
									worldinfo.setRainTime(0);
									worldinfo.setThunderTime(0);
									worldinfo.setRaining(false);
									worldinfo.setThundering(false);
								}
							}
							else {
								world.rainingStrength = 0F;
								world.prevRainingStrength = 0F;
								world.thunderingStrength = 0F;
								world.prevThunderingStrength = 0F;
							}
							if(world.isRemote) {
								world.playSound(pos.getX()+0.5D, pos.getY()+0.5D, pos.getZ()+0.5D, SoundEvents.ENTITY_FIREWORK_LARGE_BLAST, SoundCategory.BLOCKS, 2F, 1F, false);
							}
						}
					}
				}
				else if(getStackInSlot(1).getItem() == ItemsCore.raining_catalyst && (!world.isRaining() || world.isThundering())) {
					if(mruStorage.getMRU() >= mruUsage) {
						mruStorage.extractMRU(mruUsage, true);
						++progressLevel;
						if(world.isRemote) {
							world.playSound(pos.getX()+0.5D, pos.getY()+0.5D, pos.getZ()+0.5D, SoundEvents.ENTITY_MINECART_RIDING, SoundCategory.BLOCKS, 0.1F, 0.8F+(float)progressLevel/requiredTicks, false);
						}
						if(progressLevel >= requiredTicks) {
							progressLevel = 0;
							decrStackSize(1, 1);
							if(getStackInSlot(2).isEmpty()) {
								setInventorySlotContents(2, new ItemStack(Items.GLASS_BOTTLE));
							}
							else {
								getStackInSlot(2).grow(1);
							}
							if(world.provider.hasSkyLight() && world.getGameRules().getBoolean("doWeatherCycle")) {
								if(!world.isRemote) {
									int time = (300 + world.rand.nextInt(600)) * 20;
									WorldInfo worldinfo = world.getWorldInfo();
									worldinfo.setCleanWeatherTime(0);
									worldinfo.setRainTime(time);
									worldinfo.setThunderTime(time);
									worldinfo.setRaining(true);
									worldinfo.setThundering(false);
								}
							}
							else {
								world.rainingStrength = 1F;
								world.prevRainingStrength = 1F;
								world.thunderingStrength = 0F;
								world.prevThunderingStrength = 0F;
							}
							if(world.isRemote) {
								world.playSound(pos.getX()+0.5D, pos.getY()+0.5D, pos.getZ()+0.5D, SoundEvents.ENTITY_FIREWORK_LARGE_BLAST, SoundCategory.BLOCKS, 2F, 1F, false);
							}
						}
					}
				}
				else if(getStackInSlot(1).getItem() == ItemsCore.thundering_catalyst && !world.isThundering()) {
					if(mruStorage.getMRU() >= mruUsage) {
						mruStorage.extractMRU(mruUsage, true);
						++progressLevel;
						if(world.isRemote) {
							world.playSound(pos.getX()+0.5D, pos.getY()+0.5D, pos.getZ()+0.5D, SoundEvents.ENTITY_MINECART_RIDING, SoundCategory.BLOCKS, 0.1F, 0.8F+(float)progressLevel/requiredTicks, false);
						}
						if(progressLevel >= requiredTicks) {
							progressLevel = 0;
							decrStackSize(1, 1);
							if(getStackInSlot(2).isEmpty()) {
								setInventorySlotContents(2, new ItemStack(Items.GLASS_BOTTLE));
							}
							else {
								getStackInSlot(2).grow(1);
							}
							if(world.provider.hasSkyLight() && world.getGameRules().getBoolean("doWeatherCycle")) {
								if(!world.isRemote) {
									int time = (300 + world.rand.nextInt(600)) * 20;
									WorldInfo worldinfo = world.getWorldInfo();
									worldinfo.setCleanWeatherTime(0);
									worldinfo.setRainTime(time);
									worldinfo.setThunderTime(time);
									worldinfo.setRaining(true);
									worldinfo.setThundering(true);
								}
							}
							else {
								world.rainingStrength = 1F;
								world.prevRainingStrength = 1F;
								world.thunderingStrength = 1F;
								world.prevThunderingStrength = 1F;
							}
							if(world.isRemote) {
								world.playSound(pos.getX()+0.5D, pos.getY()+0.5D, pos.getZ()+0.5D, SoundEvents.ENTITY_FIREWORK_LARGE_BLAST, SoundCategory.BLOCKS, 2F, 1F, false);
							}
						}
					}
				}
				else {
					progressLevel = 0;
				}
			}
			else {
				progressLevel = 0;
			}
		}
	}

	@Override
	public int[] getOutputSlots() {
		return new int[] {2};
	}

	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return INFINITE_EXTENT_AABB;
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return slot == 0 ? isBoundGem(stack) :
			slot == 1 ? stack.getItem() == ItemsCore.clearing_catalyst || stack.getItem() == ItemsCore.raining_catalyst || stack.getItem() == ItemsCore.thundering_catalyst : false;
	}

	public static void setupConfig(Configuration cfg) {
		try {
			String category = "tileentities.weathercontroller";
			cfgMaxMRU = cfg.get(category, "MaxMRU", ApiCore.DEVICE_MAX_MRU_GENERIC).setMinValue(1).getInt();
			mruUsage = cfg.get(category, "MRUUsage", 25).setMinValue(0).setMaxValue(cfgMaxMRU).getInt();
			requiredTicks = cfg.get(category, "RequiredTicks", 200).setMinValue(0).getInt();
		}
		catch(Exception e) {
			return;
		}
	}
}
