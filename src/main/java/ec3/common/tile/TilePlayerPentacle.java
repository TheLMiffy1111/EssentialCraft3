package ec3.common.tile;

import ec3.common.block.BlockMithrilineCrystal;
import ec3.common.block.BlocksCore;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TilePlayerPentacle extends TileEntity implements ITickable {
	public int tier = -1;
	public int sCheckTick = 0;
	
	public int getEnderstarEnergy() {
		int[][] coords = new int[][] {
			{2, 0, 1}, {2, 0, -1}, {-2, 0, 1}, {-2, 0, -1},
			{1, 0, 2}, {-1, 0, 2}, {1, 0, -2}, {-1, 0, -2},
			{1, 1, 1}, {-1, 1, 1}, {1, 1, -1}, {-1, 1, -1},
			{2, 2, 2}, {-2, 2, 2}, {2, 2, -2}, {-2, 2, -2},
			{2, 3, 0}, {-2, 3, 0}, {0, 3, 2}, {0, 3, -2}
		};
		int tierCheck = 0;
		if(tier == 0)
			tierCheck = 8;
		if(tier == 1)
			tierCheck = 12;
		if(tier == 2)
			tierCheck = 16;
		if(tier >= 3)
			tierCheck = 20;
		double energy = 0;
		double consumeModifier = 0.36D;
		for(int i = 0; i < tierCheck; ++i) {
			TileEntity tile = getWorld().getTileEntity(pos.add(coords[i][0], coords[i][1], coords[i][2]));
			if(tile instanceof TileMithrilineCrystal) {
				TileMithrilineCrystal tmc = (TileMithrilineCrystal)tile;
				if(tmc.energy > TileMithrilineCrystal.maxEnergy)
					tmc.energy = TileMithrilineCrystal.maxEnergy;
				if(tier < 3)
					energy += tmc.energy;
				else
					energy += tmc.energy*2;
			}
		}
		
		return MathHelper.floor(energy*consumeModifier);
	}
	
	public boolean consumeEnderstarEnergy(int consumed) {
		int[][] coords = new int[][] {
			{2, 0, 1}, {2, 0, -1}, {-2, 0, 1}, {-2, 0, -1},
			{1, 0, 2}, {-1, 0, 2}, {1, 0, -2}, {-1, 0, -2},
			{1, 1, 1}, {-1, 1, 1}, {1, 1, -1}, {-1, 1, -1},
			{2, 2, 2}, {-2, 2, 2}, {2, 2, -2}, {-2, 2, -2},
			{2, 3, 0}, {-2, 3, 0}, {0, 3, 2}, {0, 3, -2}
		};
		int tierCheck = 0;
		if(tier == 0)
			tierCheck = 8;
		if(tier == 1)
			tierCheck = 12;
		if(tier == 2)
			tierCheck = 16;
		if(tier >= 3)
			tierCheck = 20;
		double aconsumed = 0;
		double consumeModifier = 0.36D;
		for(int i = 0; i < tierCheck; ++i) {
			TileEntity tile = getWorld().getTileEntity(pos.add(coords[i][0], coords[i][1], coords[i][2]));
			if(tile instanceof TileMithrilineCrystal) {
				TileMithrilineCrystal tmc = TileMithrilineCrystal.class.cast(tile);
				if(tmc.energy>TileMithrilineCrystal.maxEnergy)
					tmc.energy = TileMithrilineCrystal.maxEnergy;
				if(tier < 3)
					aconsumed += tmc.energy;
				else
					aconsumed += tmc.energy*2;
				if(aconsumed*consumeModifier >= consumed)
					break;
			}
		}
		if(aconsumed*consumeModifier < consumed)
			return false;
		
		aconsumed = 0;
		for(int i = 0; i < tierCheck; ++i) {
			TileEntity tile = getWorld().getTileEntity(pos.add(coords[i][0], coords[i][1], coords[i][2]));
			if(tile instanceof TileMithrilineCrystal) {
				TileMithrilineCrystal tmc = TileMithrilineCrystal.class.cast(tile);
				
				if(tier < 3)
					aconsumed += tmc.energy;
				else
					aconsumed += tmc.energy*2;
				tmc.energy = 0;
				if(aconsumed*consumeModifier >= consumed) {
					double diff = consumed - aconsumed*consumeModifier;
					if(diff > 0)
						tmc.energy += MathHelper.floor(diff);
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public void update() {
		if(sCheckTick == 0) {
			checkStructureAndTier();
			sCheckTick = 200;
		}else
			--sCheckTick;
		
		float movement = getWorld().getWorldTime() % 60;
		if(movement > 30)
			movement = 60F - movement;
		int[][] coords = new int[][] {
			{2, 0, 1}, {2, 0, -1}, {-2, 0, 1}, {-2, 0, -1},
			{1, 0, 2}, {-1, 0, 2}, {1, 0, -2}, {-1, 0, -2},
			{1, 1, 1}, {-1, 1, 1}, {1, 1, -1}, {-1, 1, -1},
			{2, 2, 2}, {-2, 2, 2}, {2, 2, -2}, {-2, 2, -2},
			{2, 3, 0}, {-2, 3, 0}, {0, 3, 2}, {0, 3, -2}
		};
		if(tier >= 0) {
			for(int i = 0; i < 8; ++i) {
				getWorld().spawnParticle(EnumParticleTypes.REDSTONE, pos.getX()+coords[i][0]+0.5D, pos.getY()+coords[i][1]+movement/30, pos.getZ()+coords[i][2]+0.5D, -1, 1, 0);
				getWorld().spawnParticle(EnumParticleTypes.REDSTONE, pos.getX()+coords[i][0]+0.5D, pos.getY()+coords[i][1]+2+movement/30, pos.getZ()+coords[i][2]+0.5D, -1, 1, 0);
			}
		}
		if(tier >= 1) {
			for(int i = 8; i < 12; ++i) {
				getWorld().spawnParticle(EnumParticleTypes.REDSTONE, pos.getX()+coords[i][0]+0.5D, pos.getY()+coords[i][1]+movement/30, pos.getZ()+coords[i][2]+0.5D, -1, 0, 1);
				getWorld().spawnParticle(EnumParticleTypes.REDSTONE, pos.getX()+coords[i][0]+0.5D, pos.getY()+coords[i][1]+2+movement/30, pos.getZ()+coords[i][2]+0.5D, -1, 0, 1);
			}
		}
		if(tier >= 2) {
			for(int i = 12; i < 16; ++i) {
				getWorld().spawnParticle(EnumParticleTypes.REDSTONE, pos.getX()+coords[i][0]+0.5D, pos.getY()+coords[i][1]+movement/30, pos.getZ()+coords[i][2]+0.5D, 0.1D, 0, 0.1D);
				getWorld().spawnParticle(EnumParticleTypes.REDSTONE, pos.getX()+coords[i][0]+0.5D, pos.getY()+coords[i][1]+2+movement/30, pos.getZ()+coords[i][2]+0.5D, 0.1D, 0, 0.1D);
			}
		}
		if(tier >= 3) {
			for(int i = 16; i < 20; ++i) {
				getWorld().spawnParticle(EnumParticleTypes.REDSTONE, pos.getX()+coords[i][0]+0.5D, pos.getY()+coords[i][1]+movement/30, pos.getZ()+coords[i][2]+0.5D, 1D, 0, 0D);
				getWorld().spawnParticle(EnumParticleTypes.REDSTONE, pos.getX()+coords[i][0]+0.5D, pos.getY()+coords[i][1]+2+movement/30, pos.getZ()+coords[i][2]+0.5D, 1D, 0, 0D);
			}
		}
	}
	
	public void checkStructureAndTier() {
		World w = getWorld();
		BlockPos cp = pos.down();
		tier = -1;
		if(
				w.getBlockState(cp.add(0, 0, 0)).getBlock() == BlocksCore.invertedBlock &&
				w.getBlockState(cp.add(1, 0, 0)).getBlock() == BlocksCore.invertedBlock &&
				w.getBlockState(cp.add(-1, 0, 0)).getBlock() == BlocksCore.invertedBlock &&
				w.getBlockState(cp.add(0, 0, 1)).getBlock() == BlocksCore.invertedBlock &&
				w.getBlockState(cp.add(0, 0, -1)).getBlock() == BlocksCore.invertedBlock &&
				w.getBlockState(cp.add(2, 0, -1)).getBlock() == BlocksCore.invertedBlock &&
				w.getBlockState(cp.add(2, 0, 1)).getBlock() == BlocksCore.invertedBlock &&
				w.getBlockState(cp.add(-2, 0, -1)).getBlock() == BlocksCore.invertedBlock &&
				w.getBlockState(cp.add(-2, 0, 1)).getBlock() == BlocksCore.invertedBlock &&
				w.getBlockState(cp.add(-1, 0, 2)).getBlock() == BlocksCore.invertedBlock &&
				w.getBlockState(cp.add(1, 0, 2)).getBlock() == BlocksCore.invertedBlock &&
				w.getBlockState(cp.add(-1, 0, -2)).getBlock() == BlocksCore.invertedBlock &&
				w.getBlockState(cp.add(1, 0, -2)).getBlock() == BlocksCore.invertedBlock &&
				w.getBlockState(cp.add(-1, 1, 2)).getBlock() == BlocksCore.mithrilineCrystal &&
				w.getBlockState(cp.add(1, 1, 2)).getBlock() == BlocksCore.mithrilineCrystal &&
				w.getBlockState(cp.add(-1, 1, -2)).getBlock() == BlocksCore.mithrilineCrystal &&
				w.getBlockState(cp.add(1, 1, -2)).getBlock() == BlocksCore.mithrilineCrystal &&
				w.getBlockState(cp.add(2, 1, -1)).getBlock() == BlocksCore.mithrilineCrystal &&
				w.getBlockState(cp.add(2, 1, 1)).getBlock() == BlocksCore.mithrilineCrystal &&
				w.getBlockState(cp.add(-2, 1, -1)).getBlock() == BlocksCore.mithrilineCrystal &&
				w.getBlockState(cp.add(-2, 1, 1)).getBlock() == BlocksCore.mithrilineCrystal &&
				w.getBlockState(cp.add(-1, 1, 2)).getValue(BlockMithrilineCrystal.TYPE).getIndex() == 0 &&
				w.getBlockState(cp.add(1, 1, 2)).getValue(BlockMithrilineCrystal.TYPE).getIndex() == 0 &&
				w.getBlockState(cp.add(-1, 1, -2)).getValue(BlockMithrilineCrystal.TYPE).getIndex() == 0 &&
				w.getBlockState(cp.add(1, 1, -2)).getValue(BlockMithrilineCrystal.TYPE).getIndex() == 0 &&
				w.getBlockState(cp.add(2, 1, -1)).getValue(BlockMithrilineCrystal.TYPE).getIndex() == 0 &&
				w.getBlockState(cp.add(2, 1, 1)).getValue(BlockMithrilineCrystal.TYPE).getIndex() == 0 &&
				w.getBlockState(cp.add(-2, 1, -2)).getValue(BlockMithrilineCrystal.TYPE).getIndex() == 0 &&
				w.getBlockState(cp.add(-2, 1, 1)).getValue(BlockMithrilineCrystal.TYPE).getIndex() == 0
				)
			tier = 0;
		else return;
		if(
				w.getBlockState(cp.add(1, 1, 1)).getBlock() == BlocksCore.blockPale &&
				w.getBlockState(cp.add(-1, 1, 1)).getBlock() == BlocksCore.blockPale &&
				w.getBlockState(cp.add(1, 1, -1)).getBlock() == BlocksCore.blockPale &&
				w.getBlockState(cp.add(-1, 1, -1)).getBlock() == BlocksCore.blockPale &&
				w.getBlockState(cp.add(1, 2, 1)).getBlock() == BlocksCore.mithrilineCrystal &&
				w.getBlockState(cp.add(-1, 2, 1)).getBlock() == BlocksCore.mithrilineCrystal &&
				w.getBlockState(cp.add(1, 2, -1)).getBlock() == BlocksCore.mithrilineCrystal &&
				w.getBlockState(cp.add(-1, 2, -1)).getBlock() == BlocksCore.mithrilineCrystal &&
				w.getBlockState(cp.add(1, 2, 1)).getValue(BlockMithrilineCrystal.TYPE).getIndex() == 1 &&
				w.getBlockState(cp.add(-1, 2, 1)).getValue(BlockMithrilineCrystal.TYPE).getIndex() == 1 &&
				w.getBlockState(cp.add(1, 2, -1)).getValue(BlockMithrilineCrystal.TYPE).getIndex() == 1 &&
				w.getBlockState(cp.add(-1, 2, -1)).getValue(BlockMithrilineCrystal.TYPE).getIndex() == 1
				)
			tier = 1;
		else return;
		if(
				w.getBlockState(cp.add(2, 1, 2)).getBlock() == BlocksCore.voidStone &&
				w.getBlockState(cp.add(-2, 1, 2)).getBlock() == BlocksCore.voidStone &&
				w.getBlockState(cp.add(2, 1, -2)).getBlock() == BlocksCore.voidStone &&
				w.getBlockState(cp.add(-2, 1, -2)).getBlock() == BlocksCore.voidStone &&
				w.getBlockState(cp.add(2, 2, 2)).getBlock() == BlocksCore.voidStone &&
				w.getBlockState(cp.add(-2, 2, 2)).getBlock() == BlocksCore.voidStone &&
				w.getBlockState(cp.add(2, 2, -2)).getBlock() == BlocksCore.voidStone &&
				w.getBlockState(cp.add(-2, 2, -2)).getBlock() == BlocksCore.voidStone &&
				w.getBlockState(cp.add(2, 3, 2)).getBlock() == BlocksCore.mithrilineCrystal &&
				w.getBlockState(cp.add(-2, 3, 2)).getBlock() == BlocksCore.mithrilineCrystal &&
				w.getBlockState(cp.add(2, 3, -2)).getBlock() == BlocksCore.mithrilineCrystal &&
				w.getBlockState(cp.add(-2, 3, -2)).getBlock() == BlocksCore.mithrilineCrystal &&
				w.getBlockState(cp.add(2, 3, 2)).getValue(BlockMithrilineCrystal.TYPE).getIndex() == 2 &&
				w.getBlockState(cp.add(-2, 3, 2)).getValue(BlockMithrilineCrystal.TYPE).getIndex() == 2 &&
				w.getBlockState(cp.add(2, 3, -2)).getValue(BlockMithrilineCrystal.TYPE).getIndex() == 2 &&
				w.getBlockState(cp.add(-2, 3, -2)).getValue(BlockMithrilineCrystal.TYPE).getIndex() == 2
				)
			tier = 2;
		else return;
		if(
				w.getBlockState(cp.add(2, 0, 0)) == BlocksCore.demonicPlating &&
				w.getBlockState(cp.add(-2, 0, 0)) == BlocksCore.demonicPlating &&
				w.getBlockState(cp.add(0, 0, 2)) == BlocksCore.demonicPlating &&
				w.getBlockState(cp.add(0, 0, -2)) == BlocksCore.demonicPlating &&
				w.getBlockState(cp.add(2, 4, 0)) == BlocksCore.mithrilineCrystal &&
				w.getBlockState(cp.add(-2, 4, 0)) == BlocksCore.mithrilineCrystal	&&
				w.getBlockState(cp.add(0, 4, 2)) == BlocksCore.mithrilineCrystal &&
				w.getBlockState(cp.add(0, 4, -2)) == BlocksCore.mithrilineCrystal &&
				w.getBlockState(cp.add(2, 4, 0)).getValue(BlockMithrilineCrystal.TYPE).getIndex() == 3 &&
				w.getBlockState(cp.add(-2, 4, 0)).getValue(BlockMithrilineCrystal.TYPE).getIndex() == 3 &&
				w.getBlockState(cp.add(0, 4, 2)).getValue(BlockMithrilineCrystal.TYPE).getIndex() == 3 &&
				w.getBlockState(cp.add(0, 4, -2)).getValue(BlockMithrilineCrystal.TYPE).getIndex() == 3
				)
			tier = 3;
		else return;
	}
}
