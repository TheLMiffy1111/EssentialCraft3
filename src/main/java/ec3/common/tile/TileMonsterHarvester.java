package ec3.common.tile;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.util.FakePlayer;
import DummyCore.Utils.DataStorage;
import DummyCore.Utils.DummyData;
import DummyCore.Utils.MiscUtils;
import ec3.api.ApiCore;
import ec3.utils.common.ECUtils;

public class TileMonsterHarvester extends TileMRUGeneric {
	public static float rad = 12F;
	public float rotation = 0F;
	public int destrTick;
	public static float cfgMaxMRU = ApiCore.DEVICE_MAX_MRU_GENERIC;
	public static boolean generatesCorruption = false;
	public static int genCorruption = 10;
	public static int mruUsage = 100;
	public static int mobDestructionTimer = 1440;
	public static boolean allowBossDuplication = false;
	public static boolean clearCopyInventory = true;
	
	public TileMonsterHarvester() {
		super();
		maxMRU = (int)cfgMaxMRU;
		setSlotsNum(6);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void update() {
		super.update();
		ECUtils.manage(this, 0);
		if(getWorld().isBlockIndirectlyGettingPowered(pos) == 0) {
			++destrTick;
			if(destrTick >= mobDestructionTimer) {
				destrTick = 0;
				List<EntityLivingBase> lst = getWorld().getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(pos.getX()-16, pos.getY()-16, pos.getZ()-16, pos.getX()+17, pos.getY()+17, pos.getZ()+17));
				if(!lst.isEmpty() && !getWorld().isRemote) {
					for(int i = 0; i < lst.size(); ++i) {
						EntityLivingBase e = lst.get(i);
						if(!(e instanceof EntityPlayer)) {
							if(getMRU() > mruUsage) {
								if(!e.isNonBoss() && !allowBossDuplication)
									return;
								
								EntityLivingBase copy = (EntityLivingBase)MiscUtils.cloneEntity(e);
								getWorld().spawnEntity(copy);
								if(clearCopyInventory) {
									copy.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, null);
									copy.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, null);
									copy.setItemStackToSlot(EntityEquipmentSlot.HEAD, null);
									copy.setItemStackToSlot(EntityEquipmentSlot.CHEST, null);
									copy.setItemStackToSlot(EntityEquipmentSlot.LEGS, null);
									copy.setItemStackToSlot(EntityEquipmentSlot.FEET, null);
								}
								FakePlayer player = new FakePlayer((WorldServer)e.world, ECUtils.EC3FakePlayerProfile);
								ItemStack stk = getStackInSlot(2);
								if(stk != null)
									player.inventory.setInventorySlotContents(player.inventory.currentItem, stk.copy());
								copy.setHealth(0.1F);
								player.attackTargetEntityWithCurrentItem(copy);
								player.setDead();
								setMRU(getMRU() - mruUsage);
								if(copy.getHealth() > 0)
									copy.setDead();
								if(generatesCorruption)
									ECUtils.increaseCorruptionAt(getWorld(), pos.getX(), pos.getY(), pos.getZ(), getWorld().rand.nextInt(genCorruption));
							}
						}
					}
				}
			}
		}
	}
	
	public static void setupConfig(Configuration cfg) {
		try {
			cfg.load();
			String[] cfgArrayString = cfg.getStringList("MonsterDuplicatorSettings", "tileentities", new String[] {
					"Max MRU:" + ApiCore.DEVICE_MAX_MRU_GENERIC,
					"MRU Usage Per Mob:100",
					"Can this device actually generate corruption:false",
					"The amount of corruption generated each tick(do not set to 0!):10",
					"Radius to duplicate mobs within:12.0",
					"Ticks required to duplicate mobs:1440",
					"Allow duplication of bosses:false",
					"Remove inventory of a duplicate before killing it:true"
			}, "");
			String dataString = "";
			
			for(int i = 0; i < cfgArrayString.length; ++i)
				dataString += "||" + cfgArrayString[i];
			
			DummyData[] data = DataStorage.parseData(dataString);
			
			mruUsage = Integer.parseInt(data[1].fieldValue);
			cfgMaxMRU = Float.parseFloat(data[0].fieldValue);
			generatesCorruption = Boolean.parseBoolean(data[2].fieldValue);
			genCorruption = Integer.parseInt(data[3].fieldValue);
			rad = Float.parseFloat(data[4].fieldValue);
			mobDestructionTimer = Integer.parseInt(data[5].fieldValue);
			allowBossDuplication = Boolean.parseBoolean(data[6].fieldValue);
			clearCopyInventory = Boolean.parseBoolean(data[7].fieldValue);
			
			cfg.save();
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
