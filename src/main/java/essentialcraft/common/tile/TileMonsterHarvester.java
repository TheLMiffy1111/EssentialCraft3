package essentialcraft.common.tile;

import java.util.List;
import java.util.UUID;

import essentialcraft.api.ApiCore;
import essentialcraft.utils.common.ECUtils;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.util.FakePlayer;

public class TileMonsterHarvester extends TileMRUGeneric {

	public static double rad = 12;
	public int destrTick;
	public static int cfgMaxMRU = ApiCore.DEVICE_MAX_MRU_GENERIC;
	public static boolean generatesCorruption = false;
	public static int genCorruption = 10;
	public static int mruUsage = 100;
	public static int mobDestructionTimer = 1440;
	public static boolean allowBossDuplication = false;
	public static boolean clearCopyInventory = true;

	public TileMonsterHarvester() {
		super(cfgMaxMRU);
		setSlotsNum(6);
	}

	@Override
	public void update() {
		super.update();
		mruStorage.update(getPos(), getWorld(), getStackInSlot(0));
		if(getWorld().isBlockIndirectlyGettingPowered(pos) == 0) {
			++destrTick;
			if(destrTick >= mobDestructionTimer) {
				destrTick = 0;
				List<EntityLivingBase> lst = getWorld().getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(pos).grow(rad, rad, rad));
				if(!lst.isEmpty() && !getWorld().isRemote) {
					for(int i = 0; i < lst.size(); ++i) {
						EntityLivingBase e = lst.get(i);
						if(!(e instanceof EntityPlayer)) {
							if(mruStorage.getMRU() >= mruUsage) {
								if(!e.isNonBoss() && !allowBossDuplication)
									return;
								mruStorage.extractMRU(mruUsage, true);

								if(!world.isRemote) {
									EntityLivingBase copy = (EntityLivingBase)EntityList.newEntity(e.getClass(), getWorld());
									copy.readFromNBT(e.writeToNBT(new NBTTagCompound()));
									copy.setUniqueId(UUID.randomUUID());
									getWorld().spawnEntity(copy);
									if(clearCopyInventory) {
										copy.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, ItemStack.EMPTY);
										copy.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, ItemStack.EMPTY);
										copy.setItemStackToSlot(EntityEquipmentSlot.HEAD, ItemStack.EMPTY);
										copy.setItemStackToSlot(EntityEquipmentSlot.CHEST, ItemStack.EMPTY);
										copy.setItemStackToSlot(EntityEquipmentSlot.LEGS, ItemStack.EMPTY);
										copy.setItemStackToSlot(EntityEquipmentSlot.FEET, ItemStack.EMPTY);
									}
									FakePlayer player = new FakePlayer((WorldServer)e.world, ECUtils.EC3FakePlayerProfile);
									ItemStack stk = getStackInSlot(2);
									if(!stk.isEmpty())
										player.inventory.setInventorySlotContents(player.inventory.currentItem, stk.copy());
									copy.setHealth(0.01F);
									player.attackTargetEntityWithCurrentItem(copy);
									player.setDead();
									if(copy.getHealth() > 0)
										copy.setDead();
									if(generatesCorruption)
										ECUtils.randomIncreaseCorruptionAt(getWorld(), pos, getWorld().rand, (genCorruption));
								}
							}
						}
					}
				}
			}
		}
	}

	public static void setupConfig(Configuration cfg) {
		try {
			String category = "tileentities.monsterduplicator";
			cfgMaxMRU = cfg.get(category, "MaxMRU", ApiCore.DEVICE_MAX_MRU_GENERIC).setMinValue(1).getInt();
			mruUsage = cfg.get(category, "MRUUsage", 100, "MRU Usage Per Mob").setMinValue(0).getInt();
			generatesCorruption = cfg.get(category, "GenerateCorruption", false).getBoolean();
			genCorruption = cfg.get(category, "MaxCorruptionGen", 10, "Max amount of corruption generated per tick").setMinValue(0).getInt();
			rad = cfg.get(category, "Radius", 12D).setMinValue(0D).getDouble();
			mobDestructionTimer = cfg.get(category, "TicksRequired", 1440).setMinValue(1).getInt();
			allowBossDuplication = cfg.get(category, "AllowBossDuplication", false).getBoolean();
			clearCopyInventory = cfg.get(category, "ClearInventory", true).getBoolean();
		}
		catch(Exception e) {
			return;
		}
	}

	@Override
	public int[] getOutputSlots() {
		return new int[0];
	}
}
