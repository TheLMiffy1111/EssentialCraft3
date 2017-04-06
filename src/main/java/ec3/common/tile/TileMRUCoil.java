package ec3.common.tile;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.common.config.Configuration;
import DummyCore.Utils.Coord3D;
import DummyCore.Utils.DataStorage;
import DummyCore.Utils.DummyData;
import DummyCore.Utils.Lightning;
import DummyCore.Utils.MathUtils;
import DummyCore.Utils.MiscUtils;
import ec3.api.ApiCore;
import ec3.api.EnumStructureType;
import ec3.api.IMRUPressence;
import ec3.common.block.BlocksCore;
import ec3.common.item.ItemPlayerList;
import ec3.common.item.ItemsCore;
import ec3.common.registry.SoundRegistry;
import ec3.utils.common.ECUtils;

public class TileMRUCoil extends TileMRUGeneric {
	public float rad = 1F;
	
	public Lightning localLightning;
	
	public Lightning monsterLightning;
	
	public int height;
	
	public int ticksBeforeStructureCheck, lightningTicks;
	
	public boolean isStructureCorrect;
	
	public static float cfgMaxMRU = ApiCore.DEVICE_MAX_MRU_GENERIC*10;
	public static boolean generatesCorruption = false;
	public static int genCorruption = 50;
	public static int mruUsage = 200;
	public static boolean hurtPlayers = true;
	public static boolean hurtPassive = true;
	public static float damage = 18F;
	public static float radiusModifier = 1F;
	
	public TileMRUCoil() {
		super();
		maxMRU = (int)cfgMaxMRU;
		setSlotsNum(2);
	}
	
	public boolean isStructureCorrect() {
		return isStructureCorrect && getMRU() > mruUsage;
	}
	
	public void initStructure() {
		height = 0;
		rad = 0;
		isStructureCorrect = false;
		int dy = pos.getY()-1;
		BlockPos.MutableBlockPos dp = new BlockPos.MutableBlockPos(pos.down());
		while(getWorld().getBlockState(dp).getBlock() == BlocksCore.mruCoilHardener) {
			--dy;
			dp.setY(dy);
			++height;
		}
		List<Block> allowed = ECUtils.allowedBlocks.get(EnumStructureType.MRUCoil);
		Block b_0 = getWorld().getBlockState(dp.add(0, 0, 0)).getBlock();
		Block b_1 = getWorld().getBlockState(dp.add(1, 0, 0)).getBlock();
		Block b_2 = getWorld().getBlockState(dp.add(-1, 0, 0)).getBlock();
		Block b_3 = getWorld().getBlockState(dp.add(1, 0, 1)).getBlock();
		Block b_4 = getWorld().getBlockState(dp.add(-1, 0, 1)).getBlock();
		Block b_5 = getWorld().getBlockState(dp.add(1, 0, -1)).getBlock();
		Block b_6 = getWorld().getBlockState(dp.add(-1, 0, -1)).getBlock();
		Block b_7 = getWorld().getBlockState(dp.add(0, 0, 1)).getBlock();
		Block b_8 = getWorld().getBlockState(dp.add(0, 0, -1)).getBlock();
		
		Block b_0_0 = getWorld().getBlockState(dp.add(0, 0, -2)).getBlock();
		Block b_0_1 = getWorld().getBlockState(dp.add(0, 0, 2)).getBlock();
		Block b_0_2 = getWorld().getBlockState(dp.add(2, 0, 0)).getBlock();
		Block b_0_3 = getWorld().getBlockState(dp.add(-2, 0, 0)).getBlock();
		
		Block b_1_0 = getWorld().getBlockState(dp.add(0, 0, -3)).getBlock();
		Block b_1_1 = getWorld().getBlockState(dp.add(1, 0, -3)).getBlock();
		Block b_1_2 = getWorld().getBlockState(dp.add(-1, 0, -3)).getBlock();
		Block b_1_3 = getWorld().getBlockState(dp.add(0, 0, 3)).getBlock();
		Block b_1_4 = getWorld().getBlockState(dp.add(1, 0, 3)).getBlock();
		Block b_1_5 = getWorld().getBlockState(dp.add(-1, 0, 3)).getBlock();
		Block b_1_6 = getWorld().getBlockState(dp.add(3, 0, 0)).getBlock();
		Block b_1_7 = getWorld().getBlockState(dp.add(3, 0, -1)).getBlock();
		Block b_1_8 = getWorld().getBlockState(dp.add(3, 0, 1)).getBlock();
		Block b_1_9 = getWorld().getBlockState(dp.add(-3, 0, 0)).getBlock();
		Block b_1_10 = getWorld().getBlockState(dp.add(-3, 0, -1)).getBlock();
		Block b_1_11 = getWorld().getBlockState(dp.add(-3, 0, 1)).getBlock();
		List<Block> cBl = new ArrayList<Block>();
		cBl.add(b_0);
		cBl.add(b_1);
		cBl.add(b_2);
		cBl.add(b_3);
		cBl.add(b_4);
		cBl.add(b_5);
		cBl.add(b_6);
		cBl.add(b_7);
		cBl.add(b_8);
		cBl.add(b_0_0);
		cBl.add(b_0_1);
		cBl.add(b_0_2);
		cBl.add(b_0_3);
		cBl.add(b_1_0);
		cBl.add(b_1_1);
		cBl.add(b_1_2);
		cBl.add(b_1_3);
		cBl.add(b_1_4);
		cBl.add(b_1_5);
		cBl.add(b_1_6);
		cBl.add(b_1_7);
		cBl.add(b_1_8);
		cBl.add(b_1_9);
		cBl.add(b_1_10);
		cBl.add(b_1_11);
		
		if(allowed.containsAll(cBl)) {
			isStructureCorrect = true;
			rad = height + 3;
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void update() {
		if(getWorld().isBlockIndirectlyGettingPowered(pos) == 0) {
			if(getWorld().isRemote) {
				if(isStructureCorrect()) {
					if(localLightning == null) {
							localLightning = new Lightning(getWorld().rand, new Coord3D(0.5F, 0.9F, 0.5F), new Coord3D(0.5F + MathUtils.randomDouble(getWorld().rand), 0.9F + MathUtils.randomDouble(getWorld().rand), 0.5F + MathUtils.randomDouble(getWorld().rand)), 0.1F, 1.0F, 0.2F, 1.0F);
					}
					else if(localLightning.renderTicksExisted >= 20)
							localLightning = null;
				}
				else
					localLightning = null;
			}
			if(monsterLightning != null) {
				if(!getWorld().isRemote)
					++lightningTicks;
				if(monsterLightning.renderTicksExisted >= 20 && getWorld().isRemote)
					monsterLightning = null;
				if(lightningTicks >= 20 && !getWorld().isRemote) {
					lightningTicks = 0;
					monsterLightning = null;
				}
			}
			if(isStructureCorrect) {
				List<EntityLivingBase> entities = getWorld().getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(pos).expand(rad*radiusModifier, rad*radiusModifier, rad*radiusModifier));
				if(entities != null && !entities.isEmpty() && monsterLightning == null) {
					Ford:
						for(EntityLivingBase b : entities) {
							if(b instanceof EntityPlayer) {
								if(!((EntityPlayer)b).capabilities.isCreativeMode && hurtPlayers) {
									ItemStack is = getStackInSlot(1);
									if(is != null && is.getItem() instanceof ItemPlayerList) {
										NBTTagCompound itemTag = MiscUtils.getStackTag(is);
										if(!itemTag.hasKey("usernames"))
											itemTag.setString("usernames", "||username:null");
										String str = itemTag.getString("usernames");
										DummyData[] dt = DataStorage.parseData(str);
										for(int i = 0; i < dt.length; ++i) {
											String username = dt[i].fieldValue;
											String playerName = MiscUtils.getUUIDFromPlayer((EntityPlayer)b).toString();
											if(username.equals(playerName))
												continue Ford;
										}
										attack(b);
										
									}
									else
										attack(b);
								}
							}
							else {
								if(!(b instanceof IMob) && !hurtPassive)
									continue Ford;
								if(b instanceof IMRUPressence)
									continue Ford;
								attack(b);
								break Ford;
							}
						}
				}
			}
			if(ticksBeforeStructureCheck <= 0) {
				ticksBeforeStructureCheck = 20;
				initStructure();
			}
			else
				--ticksBeforeStructureCheck;
		}
		super.update();
		ECUtils.manage(this, 0);
	}
	
	public void attack(EntityLivingBase b) {
		if(getMRU() > mruUsage && !b.isDead && b.hurtTime <= 0 && b.hurtResistantTime <= 0) {
			if(generatesCorruption)
				ECUtils.increaseCorruptionAt(getWorld(), pos.getX(), pos.getY(), pos.getZ(), getWorld().rand.nextInt(genCorruption));
			b.attackEntityFrom(DamageSource.magic, damage);
			if(getWorld().isRemote && monsterLightning == null)
				getWorld().playSound(pos.getX()+0.5F,pos.getY()+0.5F,pos.getZ()+0.5F, SoundRegistry.machineLightningHit, SoundCategory.BLOCKS, 2F, 2F, false);
			monsterLightning = new Lightning(getWorld().rand, new Coord3D(0.5F,0.8F,0.5F), new Coord3D(b.posX-pos.getX()+0.5D, b.posY-pos.getY()+0.8D, b.posZ-pos.getZ()+0.5D), 0.1F, 1F, 0.0F, 0.7F);
			if(!getWorld().isRemote)
				setMRU(getMRU() - mruUsage);
		}
	}
	
	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		AxisAlignedBB bb = INFINITE_EXTENT_AABB;
		return bb;
	}
	
	public static void setupConfig(Configuration cfg) {
		try {
			cfg.load();
			String[] cfgArrayString = cfg.getStringList("MRUCoilSettings", "tileentities", new String[] {
					"Max MRU:"+ApiCore.DEVICE_MAX_MRU_GENERIC*10,
					"MRU Usage per hit:1000",
					"Can this device actually generate corruption:false",
					"The amount of corruption generated each tick(do not set to 0!):20",
	    			"Can the coil attack players, and, therefore, requires a Whitelist:true",
	    			"Can the coil attack passive entities, like sheep:true",
	    			"Damage per hit:18.0",
	    			"Radius multiplier:1.0"
			}, "");
			String dataString = "";
			
			for(int i = 0; i < cfgArrayString.length; ++i)
				dataString += "||" + cfgArrayString[i];
	    		
			DummyData[] data = DataStorage.parseData(dataString);
			
			mruUsage = Integer.parseInt(data[1].fieldValue);
			cfgMaxMRU = Float.parseFloat(data[0].fieldValue);
			generatesCorruption = Boolean.parseBoolean(data[2].fieldValue);
			genCorruption = Integer.parseInt(data[3].fieldValue);
			hurtPlayers = Boolean.parseBoolean(data[4].fieldValue);
			hurtPassive = Boolean.parseBoolean(data[5].fieldValue);
			damage = Float.parseFloat(data[6].fieldValue);
			radiusModifier = Float.parseFloat(data[7].fieldValue);
			
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
	
	@Override
	public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
		return p_94041_1_ == 0 ? isBoundGem(p_94041_2_) : p_94041_2_.getItem() == ItemsCore.playerList;
	}
}
