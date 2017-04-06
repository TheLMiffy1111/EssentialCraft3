package ec3.common.tile;

import java.util.ArrayList;
import java.util.List;

import DummyCore.Utils.Coord3D;
import DummyCore.Utils.DataStorage;
import DummyCore.Utils.DummyData;
import DummyCore.Utils.Lightning;
import DummyCore.Utils.MathUtils;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.common.config.Configuration;
import ec3.api.ApiCore;
import ec3.common.block.BlocksCore;
import ec3.common.registry.SoundRegistry;

public class TileMRUReactor extends TileMRUGeneric {

	public int ticksBeforeStructureCheck;

	public boolean isStructureCorrect, cycle;

	public static float cfgMaxMRU = ApiCore.GENERATOR_MAX_MRU_GENERIC;
	public static float cfgBalance = -1F;
	public static float mruGenerated = 50;
	public static boolean damage = true;

	public List<Lightning> lightnings = new ArrayList<Lightning>();

	public TileMRUReactor() {
		super();
		balance = 0;
		maxMRU = (int)cfgMaxMRU;
		slot0IsBoundGem = false;
	}

	public boolean isStructureCorrect() {
		return isStructureCorrect;
	}

	public void initStructure() {
		isStructureCorrect = true;
		Cycle:
			for(int dx = -2; dx <= 2; ++dx) {
				for(int dz = -1; dz <= 1; ++dz) {
					for(int dy = -1; dy <= 1; ++dy) {
						Block b_c = getWorld().getBlockState(pos.add(dx, dy, dz)).getBlock();
						if(dy == -1) {
							if(b_c != BlocksCore.magicPlating) {
								isStructureCorrect = false;
								break Cycle;
							}
						}
						if(dy == 0) {
							if(dx == 0 && dz == 0) {
								if(b_c != BlocksCore.reactor) {
									isStructureCorrect = false;
									break Cycle;
								}
							}
							else if((dx == 1 && dz == 0) || (dx == -1 && dz == 0)) {
								if(b_c != BlocksCore.reactorSupport) {
									isStructureCorrect = false;
									break Cycle;
								}
							}
							else if(b_c != Blocks.AIR) {
								isStructureCorrect = false;
								break Cycle;
							}
						}
						if(dy == 1) {
							if((dx == 1 && dz == 0) || (dx == -1 && dz == 0)) {
								if(b_c != BlocksCore.reactorSupport) {
									isStructureCorrect = false;
									break Cycle;
								}
							}
							else if(b_c != Blocks.AIR) {
								isStructureCorrect = false;
								break Cycle;
							}
						}
					}
				}
			}
		if(!isStructureCorrect) {
			isStructureCorrect = true;
			Cycle:
				for(int dx = -1; dx <= 1; ++dx) {
					for(int dz = -2; dz <= 2; ++dz) {
						for(int dy = -1; dy <= 1; ++dy) {
							Block b_c = getWorld().getBlockState(pos.add(dx, dy, dz)).getBlock();
							if(dy == -1) {
								if(b_c != BlocksCore.magicPlating) {
									isStructureCorrect = false;
									break Cycle;
								}
							}
							if(dy == 0) {
								if(dx == 0 && dz == 0) {
									if(b_c != BlocksCore.reactor) {
										isStructureCorrect = false;
										break Cycle;
									}
								}
								else if((dx == 0 && dz == 1) || (dx == 0 && dz == -1)) {
									if(b_c != BlocksCore.reactorSupport) {
										isStructureCorrect = false;
										break Cycle;
									}
								}
								else if(b_c != Blocks.AIR) {
									isStructureCorrect = false;
									break Cycle;
								}
							}
							if(dy == 1) {
								{
									if((dx == 0 && dz == 1) || (dx == 0 && dz == -1)) {
										if(b_c != BlocksCore.reactorSupport) {
											isStructureCorrect = false;
											break Cycle;
										}
									}
									else if(b_c != Blocks.AIR) {
										isStructureCorrect = false;
										break Cycle;
									}
								}
							}
						}
					}
				}
		}
		getWorld().markBlockRangeForRenderUpdate(pos.getX()-1, pos.getY(), pos.getZ()-1, pos.getX()+1, pos.getY(), pos.getZ()+1);
	}

	public boolean canGenerateMRU() {
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void update() {
		if(ticksBeforeStructureCheck <= 0) {
			ticksBeforeStructureCheck = 20;
			initStructure();
		}
		else
			--ticksBeforeStructureCheck;
		super.update();
		if(isStructureCorrect()) {
			List<EntityLivingBase> lst = getWorld().getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(pos.getX()-3, pos.getY()-3, pos.getZ()-3, pos.getX()+4, pos.getY()+4, pos.getZ()+4));
			if(!lst.isEmpty() && damage) {
				for(int i = 0; i < lst.size(); ++i) {
					EntityLivingBase e = lst.get(i);
					e.attackEntityFrom(DamageSource.magic, 5);
				}
			}
			setMRU((int) (getMRU()+mruGenerated));
			if(getMRU() > getMaxMRU())
				setMRU(getMaxMRU());
			if(cycle) {
				setBalance(getBalance() + 0.01F);
				if(getBalance() > 2.0F)
					cycle = false;
			}
			else {
				setBalance(getBalance() - 0.01F);
				if(getBalance() < 0.01F)
					cycle = true;
			}
			if(cfgBalance != -1)
				setBalance(cfgBalance);
			if(getWorld().isRemote) {
				if(getWorld().rand.nextFloat() < 0.05F)
					getWorld().playSound(pos.getX()+0.5F,pos.getY()+1.0F,pos.getZ()+0.5F, SoundRegistry.machineGenElectricity, SoundCategory.BLOCKS, 1F, 1F, true);
				if(lightnings.size() <= 20) {
					Lightning l = new Lightning(getWorld().rand, new Coord3D(0.5F, 1.0F, 0.5F), new Coord3D(0.5F+MathUtils.randomFloat(getWorld().rand), 1.0F+MathUtils.randomFloat(getWorld().rand), 0.5F+MathUtils.randomFloat(getWorld().rand)), 0.2F, 1.0F, 0.2F, 1.0F);
					lightnings.add(l);
				}
				for(int i = 0; i < lightnings.size(); ++i) {
					Lightning lt = lightnings.get(i);
					if(lt.renderTicksExisted >= 21)
						lightnings.remove(i);
				}
			}
		}
	}

	public static void setupConfig(Configuration cfg) {
		try {
			cfg.load();
			String[] cfgArrayString = cfg.getStringList("MRUReactorSettings", "tileentities", new String[]{
					"Max MRU:" + ApiCore.GENERATOR_MAX_MRU_GENERIC,
					"Default balance(-1 is random):-1.0",
					"MRU generated per tick:50",
					"Damage Entities around:true"
			}, "");
			String dataString = "";

			for(int i = 0; i < cfgArrayString.length; ++i)
				dataString += "||" + cfgArrayString[i];

			DummyData[] data = DataStorage.parseData(dataString);

			cfgMaxMRU = Float.parseFloat(data[0].fieldValue);
			cfgBalance = Float.parseFloat(data[1].fieldValue);
			mruGenerated = Float.parseFloat(data[2].fieldValue);
			damage = Boolean.parseBoolean(data[3].fieldValue);

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
