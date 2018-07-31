package essentialcraft.common.tile;

import java.util.ArrayList;
import java.util.List;

import DummyCore.Utils.Coord3D;
import DummyCore.Utils.Lightning;
import DummyCore.Utils.MathUtils;
import essentialcraft.api.ApiCore;
import essentialcraft.common.block.BlocksCore;
import essentialcraft.common.registry.SoundRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.config.Configuration;

public class TileMRUReactor extends TileMRUGeneric {

	public int ticksBeforeStructureCheck;

	public boolean isStructureCorrect, cycle;

	public static int cfgMaxMRU = ApiCore.GENERATOR_MAX_MRU_GENERIC;
	public static float cfgBalance = -1F;
	public static int mruGenerated = 50;
	public static boolean damage = true;

	public List<Lightning> lightnings = new ArrayList<Lightning>();

	public TileMRUReactor() {
		super(cfgMaxMRU);
		mruStorage.setBalance(0F);
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
							else if(dx == 1 && dz == 0 || dx == -1 && dz == 0) {
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
							if(dx == 1 && dz == 0 || dx == -1 && dz == 0) {
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
								else if(dx == 0 && dz == 1 || dx == 0 && dz == -1) {
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
									if(dx == 0 && dz == 1 || dx == 0 && dz == -1) {
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
					e.attackEntityFrom(DamageSource.MAGIC, 5);
				}
			}
			mruStorage.addMRU(mruGenerated, true);
			if(cfgBalance < 0) {
				if(cycle) {
					mruStorage.setBalance(Math.round((mruStorage.getBalance() + 0.01F)*100)/100F);
					if(mruStorage.getBalance() > 1.99F) {
						cycle = false;
					}
				}
				else {
					mruStorage.setBalance(Math.round((mruStorage.getBalance() - 0.01F)*100)/100F);
					if(mruStorage.getBalance() < 0.01F) {
						cycle = true;
					}
				}
			}
			else {
				mruStorage.setBalance(cfgBalance);
			}
			if(getWorld().isRemote) {
				if(getWorld().rand.nextFloat() < 0.05F)
					getWorld().playSound(pos.getX()+0.5F,pos.getY()+1.0F,pos.getZ()+0.5F, SoundRegistry.machineGenElectricity, SoundCategory.BLOCKS, 1F, 1F, true);
				if(lightnings.size() <= 20) {
					Lightning l = new Lightning(getWorld().rand, new Coord3D(0.5F, 1.0F, 0.5F), new Coord3D(0.5F+MathUtils.randomFloat(getWorld().rand), 1.0F+MathUtils.randomFloat(getWorld().rand), 0.5F+MathUtils.randomFloat(getWorld().rand)), 0.2F, 1.0F, 0.2F, 1.0F);
					lightnings.add(l);
				}
				for(int i = 0; i < lightnings.size(); ++i) {
					Lightning lt = lightnings.get(i);
					if(lt.renderTicksExisted >= 21) {
						lightnings.remove(i);
						--i;
					}
				}
			}
		}
	}

	public static void setupConfig(Configuration cfg) {
		try {
			String category = "tileentities.mrureactor";
			cfgMaxMRU = cfg.get(category, "MaxMRU", ApiCore.GENERATOR_MAX_MRU_GENERIC).setMinValue(1).getInt();
			cfgBalance = (float)cfg.get(category, "Balance", -1D, "Default balance (-1 is cyclic)").setMinValue(-1D).setMaxValue(2D).getDouble();
			mruGenerated = cfg.get(category, "MRUGenerated", 50).setMinValue(0).getInt();
			damage = cfg.get(category, "DamageEntitiesAround", true).getBoolean();
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
