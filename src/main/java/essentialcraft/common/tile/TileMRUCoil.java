package essentialcraft.common.tile;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import DummyCore.Utils.Coord3D;
import DummyCore.Utils.DataStorage;
import DummyCore.Utils.DummyData;
import DummyCore.Utils.Lightning;
import DummyCore.Utils.MathUtils;
import DummyCore.Utils.MiscUtils;
import essentialcraft.api.ApiCore;
import essentialcraft.api.EnumStructureType;
import essentialcraft.common.block.BlocksCore;
import essentialcraft.common.capabilities.mru.CapabilityMRUHandler;
import essentialcraft.common.item.ItemPlayerList;
import essentialcraft.common.item.ItemsCore;
import essentialcraft.common.registry.SoundRegistry;
import essentialcraft.utils.common.ECUtils;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.config.Configuration;

public class TileMRUCoil extends TileMRUGeneric {
	public float rad = 1F;

	public Lightning localLightning;

	public Lightning monsterLightning;

	public int height;

	public int ticksBeforeStructureCheck, lightningTicks;

	public boolean isStructureCorrect;

	public static int cfgMaxMRU = ApiCore.DEVICE_MAX_MRU_GENERIC*10;
	public static boolean generatesCorruption = false;
	public static int genCorruption = 50;
	public static int mruUsage = 200;
	public static boolean hurtPlayers = true;
	public static boolean hurtPassive = true;
	public static float damage = 18F;
	public static double radiusModifier = 1D;

	public TileMRUCoil() {
		super(cfgMaxMRU);
		setSlotsNum(2);
	}

	public boolean canWork() {
		return isStructureCorrect && mruStorage.getMRU() >= mruUsage;
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
		Set<Block> allowed = ECUtils.STRUCTURE_TO_BLOCKS_MAP.get(EnumStructureType.MRU_COIL);
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
		Set<Block> cBl = new HashSet<Block>();
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

	@Override
	public void update() {
		super.update();
		mruStorage.update(getPos(), getWorld(), getStackInSlot(0));
		--ticksBeforeStructureCheck;
		if(ticksBeforeStructureCheck <= 0) {
			ticksBeforeStructureCheck = 20;
			initStructure();
		}
		if(getWorld().isBlockIndirectlyGettingPowered(pos) == 0) {
			if(getWorld().isRemote) {
				if(canWork()) {
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
				List<EntityLivingBase> entities = getWorld().getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(pos).grow(rad*radiusModifier, rad*radiusModifier, rad*radiusModifier));
				if(entities != null && !entities.isEmpty() && monsterLightning == null) {
					Ford:
						for(EntityLivingBase b : entities) {
							if(b instanceof EntityPlayer) {
								if(!((EntityPlayer)b).capabilities.isCreativeMode && hurtPlayers) {
									ItemStack is = getStackInSlot(1);
									if(is.getItem() instanceof ItemPlayerList) {
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
								if(b.hasCapability(CapabilityMRUHandler.MRU_HANDLER_CAPABILITY, null))
									continue Ford;
								attack(b);
								break Ford;
							}
						}
				}
			}
		}
	}

	public void attack(EntityLivingBase b) {
		if(mruStorage.getMRU() >= mruUsage && !b.isDead && b.hurtTime <= 0 && b.hurtResistantTime <= 0) {
			if(generatesCorruption)
				ECUtils.randomIncreaseCorruptionAt(getWorld(), pos, getWorld().rand, (genCorruption));
			b.attackEntityFrom(DamageSource.MAGIC, damage);
			if(getWorld().isRemote && monsterLightning == null)
				getWorld().playSound(pos.getX()+0.5F,pos.getY()+0.5F,pos.getZ()+0.5F, SoundRegistry.machineLightningHit, SoundCategory.BLOCKS, 2F, 2F, false);
			monsterLightning = new Lightning(getWorld().rand, new Coord3D(0.5F,0.8F,0.5F), new Coord3D(b.posX-pos.getX()+0.5D, b.posY-pos.getY()+0.8D, b.posZ-pos.getZ()+0.5D), 0.1F, 1F, 0.0F, 0.7F);
			mruStorage.extractMRU(mruUsage, true);
		}
	}

	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return INFINITE_EXTENT_AABB;
	}

	public static void setupConfig(Configuration cfg) {
		try {
			String category = "tileentities.mrucoil";
			cfgMaxMRU = cfg.get(category, "MaxMRU", ApiCore.DEVICE_MAX_MRU_GENERIC*10).setMinValue(1).getInt();
			mruUsage = cfg.get(category, "MRUUsage", 1000, "MRU Usage per hit").setMinValue(0).setMaxValue(cfgMaxMRU).getInt();
			generatesCorruption = cfg.get(category, "GenerateCorruption", false).getBoolean();
			genCorruption = cfg.get(category, "MaxCorruptionGen", 20, "Max amount of corruption generated per tick").setMinValue(0).getInt();
			hurtPlayers = cfg.get(category, "DamagePlayer", true).getBoolean();
			hurtPassive = cfg.get(category, "DamagePassive", true).getBoolean();
			damage = (float)cfg.get(category, "Damage", 18D).setMinValue(0D).getDouble();
			radiusModifier = cfg.get(category, "RadiusMultiplier", 1D).setMinValue(0D).getDouble();
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
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return slot == 0 ? isBoundGem(stack) : stack.getItem() == ItemsCore.playerList;
	}
}
