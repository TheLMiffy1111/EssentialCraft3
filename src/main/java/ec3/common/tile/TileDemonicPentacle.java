package ec3.common.tile;

import java.util.List;

import ec3.common.block.BlockMithrilineCrystal;
import ec3.common.block.BlocksCore;
import ec3.common.entity.EntityDemon;
import ec3.common.mod.EssentialCraftCore;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class TileDemonicPentacle extends TileEntity implements ITickable {
	public int tier = -1;
	public int sCheckTick = 0;
	public int energy;
	public int energyCheck = 0;
	
	public boolean consumeEnderstarEnergy(int consumed) {
		int[][] coords = new int[][]{
			{2, 1, 2}, {2, 1, -2}, {-2, 1, 2}, {-2, 1, -2},
			{3, 0, 2}, {3, 0, -2}, {-3, 0, 2}, {-3, 0, -2},
			{2, 0, 3}, {2, 0, -3}, {-2, 0, 3}, {-2, 0, -3},
			{3, 1, 0}, {-3, 1, 0}, {0, 1, 3}, {0, 1, -3}
		};
		int tierCheck = coords.length;

		double aconsumed = 0;
		double consumeModifier = 1D;
		for(int i = 0; i < tierCheck; ++i) {
			TileEntity tile = getWorld().getTileEntity(pos.add(coords[i][0],coords[i][1],coords[i][2]));
			if(tile instanceof TileMithrilineCrystal) {
				TileMithrilineCrystal tmc = TileMithrilineCrystal.class.cast(tile);
				if(tmc.energy>TileMithrilineCrystal.maxEnergy)
					tmc.energy = TileMithrilineCrystal.maxEnergy;
					aconsumed += tmc.energy;
				if(aconsumed * consumeModifier >= consumed)
					break;
			}
		}
		if(aconsumed*consumeModifier < consumed)
			return false;
		
		aconsumed = 0;
		for(int i = 0; i < tierCheck; ++i) {
			TileEntity tile = getWorld().getTileEntity(pos.add(coords[i][0],coords[i][1],coords[i][2]));
			if(tile instanceof TileMithrilineCrystal) {
				TileMithrilineCrystal tmc = TileMithrilineCrystal.class.cast(tile);
				
				aconsumed += tmc.energy;
				tmc.energy = 0;
				if(aconsumed * consumeModifier >= consumed) {
					double diff = aconsumed * consumeModifier - consumed;
					if(diff > 0)
						tmc.energy += MathHelper.floor(diff);
					return true;
				}
			}
		}
		return false;
	}
	
	public int getEnderstarEnergy() {
		if(tier == -1)
			return 0;
		
		int[][] coords = new int[][] {
			{2, 1, 2}, {2, 1, -2}, {-2, 1, 2}, {-2, 1, -2},
			{3, 0, 2}, {3, 0, -2}, {-3, 0, 2}, {-3, 0, -2},
			{2, 0, 3}, {2, 0, -3}, {-2, 0, 3}, {-2, 0, -3},
			{3, 1, 0}, {-3, 1, 0}, {0, 1, 3}, {0, 1, -3}
		};
		
		int energy = 0;
		
		for(int i = 0; i < coords.length; ++i) {
			TileEntity tile = getWorld().getTileEntity(pos.add(coords[i][0],coords[i][1],coords[i][2]));
			if(tile instanceof TileMithrilineCrystal) {
				energy += MathHelper.floor(TileMithrilineCrystal.class.cast(tile).energy);
			}
		}
		
		return energy;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void update() {
		List<EntityDemon> demons = getWorld().getEntitiesWithinAABB(EntityDemon.class, new AxisAlignedBB(pos));
		if(!demons.isEmpty()) {
			for(int i = 0; i < 4; ++i) {
				double time = (getWorld().getWorldTime()+9*i)%36 * 10;
				double timeSin = Math.sin(Math.toRadians(time)) * 1.1D;
				double timeCos = Math.cos(Math.toRadians(time)) * 1.1D;
				double x = pos.getX() + 0.5D + timeSin;
				double y = pos.getY();
				double z = pos.getZ() + 0.5D + timeCos;
				EssentialCraftCore.proxy.SmokeFX(x, y, z, 0, 0.1D, 0, 1, 1, 0, 0);
			}
		}
		if(energyCheck == 0) {
			energyCheck = 100;
			energy = getEnderstarEnergy();
		}
		else
			--energyCheck;
		
		if(sCheckTick == 0) {
			checkStructureAndTier();
			sCheckTick = 200;
		}
		else
			--sCheckTick;
		
		float movement = getWorld().getWorldTime() % 60;
		if(movement > 30)
			movement = 30 - movement + 30F;
		int[][] coords = new int[][]{
			{2, 1, 2}, {2, 1, -2}, {-2, 1, 2}, {-2, 1, -2},
			{3, 0, 2}, {3, 0, -2}, {-3, 0, 2}, {-3, 0, -2},
			{2, 0, 3}, {2, 0, -3}, {-2, 0, 3}, {-2, 0, -3},
			{3, 1, 0}, {-3, 1, 0}, {0, 1, 3}, {0, 1, -3}
		};
		if(tier >= 0) {
			for(int i = 0; i < 4; ++i) {
				getWorld().spawnParticle(EnumParticleTypes.REDSTONE, pos.getX()+coords[i][0]+0.5D, pos.getY()+coords[i][1]+movement/30, pos.getZ()+coords[i][2]+0.5D, -1, 1, 0);
				getWorld().spawnParticle(EnumParticleTypes.REDSTONE, pos.getX()+coords[i][0]+0.5D, pos.getY()+coords[i][1]+2+movement/30, pos.getZ()+coords[i][2]+0.5D, -1, 1, 0);
			}
			for(int i = 4; i < 12; ++i) {
				getWorld().spawnParticle(EnumParticleTypes.REDSTONE, pos.getX()+coords[i][0]+0.5D, pos.getY()+coords[i][1]+movement/30, pos.getZ()+coords[i][2]+0.5D, -1, 0, 1);
				getWorld().spawnParticle(EnumParticleTypes.REDSTONE, pos.getX()+coords[i][0]+0.5D, pos.getY()+coords[i][1]+2+movement/30, pos.getZ()+coords[i][2]+0.5D, -1, 0, 1);
			}
			for(int i = 12; i < 16; ++i) {
				getWorld().spawnParticle(EnumParticleTypes.REDSTONE, pos.getX()+coords[i][0]+0.5D, pos.getY()+coords[i][1]+movement/30, pos.getZ()+coords[i][2]+0.5D, 0.1D, 0, 0.1D);
				getWorld().spawnParticle(EnumParticleTypes.REDSTONE, pos.getX()+coords[i][0]+0.5D, pos.getY()+coords[i][1]+2+movement/30, pos.getZ()+coords[i][2]+0.5D, 0.1D, 0, 0.1D);
			}
		}
	}
	
	public void checkStructureAndTier() {
		World w = getWorld();
		BlockPos cp = pos.down();
		tier = -1;
		
		if(
				w.getBlockState(cp) == BlocksCore.voidStone &&
				w.getBlockState(cp.add(2, 0, 0)).getBlock() == BlocksCore.voidStone &&	
				w.getBlockState(cp.add(-2, 0, 0)).getBlock() == BlocksCore.voidStone &&
				w.getBlockState(cp.add(0, 0, -2)).getBlock() == BlocksCore.voidStone &&
				w.getBlockState(cp.add(0, 0, 2)).getBlock() == BlocksCore.voidStone &&
				w.getBlockState(cp.add(3, 0, 1)).getBlock() == BlocksCore.voidStone &&
				w.getBlockState(cp.add(3, 0, -1)).getBlock() == BlocksCore.voidStone &&
				w.getBlockState(cp.add(-3, 0, 1)).getBlock() == BlocksCore.voidStone &&
				w.getBlockState(cp.add(-3, 0, -1)).getBlock() == BlocksCore.voidStone &&
				w.getBlockState(cp.add(1, 0, 3)).getBlock() == BlocksCore.voidStone &&
				w.getBlockState(cp.add(-1, 0, 3)).getBlock() == BlocksCore.voidStone &&
				w.getBlockState(cp.add(1, 0, -3)).getBlock() == BlocksCore.voidStone &&
				w.getBlockState(cp.add(-1, 0, -3)).getBlock() == BlocksCore.voidStone &&
				w.getBlockState(cp.add(3, 1, 0)).getBlock() == Blocks.GLOWSTONE &&
				w.getBlockState(cp.add(-3, 1, 0)).getBlock() == Blocks.GLOWSTONE &&
				w.getBlockState(cp.add(0, 1, 3)).getBlock() == Blocks.GLOWSTONE &&
				w.getBlockState(cp.add(0, 1, -3)).getBlock() == Blocks.GLOWSTONE &&
				w.getBlockState(cp.add(2, 1, 2)).getBlock() == BlocksCore.invertedBlock &&
				w.getBlockState(cp.add(-2, 1, 2)).getBlock() == BlocksCore.invertedBlock &&
				w.getBlockState(cp.add(2, 1, -2)).getBlock() == BlocksCore.invertedBlock &&
				w.getBlockState(cp.add(-2, 1, -2)).getBlock() == BlocksCore.invertedBlock &&
				w.getBlockState(cp.add(1, 0, 0)).getBlock() == BlocksCore.platingPale &&
				w.getBlockState(cp.add(-1, 0, 0)).getBlock() == BlocksCore.platingPale &&
				w.getBlockState(cp.add(0, 0, 1)).getBlock() == BlocksCore.platingPale &&
				w.getBlockState(cp.add(0, 0, -1)).getBlock() == BlocksCore.platingPale &&
				w.getBlockState(cp.add(2, 0, 1)).getBlock() == BlocksCore.platingPale &&
				w.getBlockState(cp.add(2, 0, -1)).getBlock() == BlocksCore.platingPale &&	
				w.getBlockState(cp.add(-2, 0, 1)).getBlock() == BlocksCore.platingPale &&
				w.getBlockState(cp.add(-2, 0, -1)).getBlock() == BlocksCore.platingPale &&
				w.getBlockState(cp.add(1, 0, 2)).getBlock() == BlocksCore.platingPale &&
				w.getBlockState(cp.add(1, 0, -2)).getBlock() == BlocksCore.platingPale &&
				w.getBlockState(cp.add(-1, 0, 2)).getBlock() == BlocksCore.platingPale &&
				w.getBlockState(cp.add(-1, 0, -2)).getBlock() == BlocksCore.platingPale &&
				w.getBlockState(cp.add(3, 0, 2)).getBlock() == BlocksCore.platingPale &&
				w.getBlockState(cp.add(3, 0, -2)).getBlock() == BlocksCore.platingPale &&
				w.getBlockState(cp.add(-3, 0, 2)).getBlock() == BlocksCore.platingPale &&
				w.getBlockState(cp.add(-3, 0, -2)).getBlock() == BlocksCore.platingPale &&
				w.getBlockState(cp.add(2, 0, 3)).getBlock() == BlocksCore.platingPale &&
				w.getBlockState(cp.add(2, 0, -3)).getBlock() == BlocksCore.platingPale &&
				w.getBlockState(cp.add(-2, 0, 3)).getBlock() == BlocksCore.platingPale &&
				w.getBlockState(cp.add(-2, 0, -3)).getBlock() == BlocksCore.platingPale &&
				w.getBlockState(cp.add(3, 0, 2)).getBlock() == BlocksCore.mithrilineCrystal &&
				w.getBlockState(cp.add(3, 1, -2)).getBlock() == BlocksCore.mithrilineCrystal &&
				w.getBlockState(cp.add(-3, 1, 2)).getBlock() == BlocksCore.mithrilineCrystal &&
				w.getBlockState(cp.add(-3, 1, -2)).getBlock() == BlocksCore.mithrilineCrystal &&
				w.getBlockState(cp.add(2, 1, 3)).getBlock() == BlocksCore.mithrilineCrystal &&
				w.getBlockState(cp.add(2, 1, -3)).getBlock() == BlocksCore.mithrilineCrystal &&
				w.getBlockState(cp.add(-2, 1, 3)).getBlock() == BlocksCore.mithrilineCrystal &&
				w.getBlockState(cp.add(-2, 1, -3)).getBlock() == BlocksCore.mithrilineCrystal &&
				w.getBlockState(cp.add(3, 1, 2)).getValue(BlockMithrilineCrystal.TYPE).getIndex() == 1 &&
				w.getBlockState(cp.add(3, 1, -2)).getValue(BlockMithrilineCrystal.TYPE).getIndex() == 1 &&
				w.getBlockState(cp.add(-3, 1, 2)).getValue(BlockMithrilineCrystal.TYPE).getIndex() == 1 &&
				w.getBlockState(cp.add(-3, 1, -2)).getValue(BlockMithrilineCrystal.TYPE).getIndex() == 1 &&
				w.getBlockState(cp.add(2, 1, 3)).getValue(BlockMithrilineCrystal.TYPE).getIndex() == 1 &&
				w.getBlockState(cp.add(2, 1, -3)).getValue(BlockMithrilineCrystal.TYPE).getIndex() == 1 &&
				w.getBlockState(cp.add(-2, 1, 3)).getValue(BlockMithrilineCrystal.TYPE).getIndex() == 1 &&
				w.getBlockState(cp.add(-2, 1, -3)).getValue(BlockMithrilineCrystal.TYPE).getIndex() == 1 &&
				w.getBlockState(cp.add(2, 2, 2)).getBlock() == BlocksCore.mithrilineCrystal &&
				w.getBlockState(cp.add(-2, 2, 2)).getBlock() == BlocksCore.mithrilineCrystal &&
				w.getBlockState(cp.add(2, 2, -2)).getBlock() == BlocksCore.mithrilineCrystal &&
				w.getBlockState(cp.add(-2, 2, -2)).getBlock() == BlocksCore.mithrilineCrystal &&
				w.getBlockState(cp.add(2, 2, 2)).getValue(BlockMithrilineCrystal.TYPE).getIndex() == 0 &&
				w.getBlockState(cp.add(-2, 2, 2)).getValue(BlockMithrilineCrystal.TYPE).getIndex() == 0 &&
				w.getBlockState(cp.add(2, 2, -2)).getValue(BlockMithrilineCrystal.TYPE).getIndex() == 0 &&
				w.getBlockState(cp.add(-2, 2, -2)).getValue(BlockMithrilineCrystal.TYPE).getIndex() == 0 &&
				w.getBlockState(cp.add(3, 2, 0)).getBlock() == BlocksCore.mithrilineCrystal &&
				w.getBlockState(cp.add(-3, 2, 0)).getBlock() == BlocksCore.mithrilineCrystal &&
				w.getBlockState(cp.add(0, 2, 3)).getBlock() == BlocksCore.mithrilineCrystal &&
				w.getBlockState(cp.add(0, 2, -3)).getBlock() == BlocksCore.mithrilineCrystal &&
				w.getBlockState(cp.add(3, 2, 0)).getValue(BlockMithrilineCrystal.TYPE).getIndex() == 2 &&
				w.getBlockState(cp.add(-3, 2, 0)).getValue(BlockMithrilineCrystal.TYPE).getIndex() == 2 &&
				w.getBlockState(cp.add(0, 2, 3)).getValue(BlockMithrilineCrystal.TYPE).getIndex() == 2 &&
				w.getBlockState(cp.add(0, 2, -3)).getValue(BlockMithrilineCrystal.TYPE).getIndex() == 2
		)
			tier = 0;
	}
}
