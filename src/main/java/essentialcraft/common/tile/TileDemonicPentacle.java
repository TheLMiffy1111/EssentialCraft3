package essentialcraft.common.tile;

import java.util.List;
import java.util.function.BiPredicate;

import essentialcraft.api.IESPEHandler;
import essentialcraft.common.block.BlocksCore;
import essentialcraft.common.capabilities.espe.CapabilityESPEHandler;
import essentialcraft.common.entity.EntityDemon;
import essentialcraft.common.mod.EssentialCraftCore;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class TileDemonicPentacle extends TileEntity implements ITickable {

	public int tier = -1;
	public int sCheckTick = 0;
	protected static Vec3i[] coords = {
			new Vec3i(2, 1, 2), new Vec3i(2, 1, -2), new Vec3i(-2, 1, 2), new Vec3i(-2, 1, -2),
			new Vec3i(3, 0, 2), new Vec3i(3, 0, -2), new Vec3i(-3, 0, 2), new Vec3i(-3, 0, -2),
			new Vec3i(2, 0, 3), new Vec3i(2, 0, -3), new Vec3i(-2, 0, 3), new Vec3i(-2, 0, -3),
			new Vec3i(3, 1, 0), new Vec3i(-3, 1, 0), new Vec3i(0, 1, 3), new Vec3i(0, 1, -3)
	};

	public boolean consumeEnderstarEnergy(int consumed) {
		double aconsumed = 0;
		for(int i = 0; i < coords.length; ++i) {
			TileEntity tile = getWorld().getTileEntity(pos.add(coords[i]));
			if(tile == null) {
				continue;
			}
			if(tile.hasCapability(CapabilityESPEHandler.ESPE_HANDLER_CAPABILITY, null)) {
				IESPEHandler handler = tile.getCapability(CapabilityESPEHandler.ESPE_HANDLER_CAPABILITY, null);
				double req = consumed - aconsumed;
				aconsumed += handler.extractESPE(req, false);
				if(aconsumed >= consumed) {
					break;
				}
			}
		}
		if(aconsumed < consumed) {
			return false;
		}

		aconsumed = 0;
		for(int i = 0; i < coords.length; ++i) {
			TileEntity tile = getWorld().getTileEntity(pos.add(coords[i]));
			if(tile == null) {
				continue;
			}
			if(tile.hasCapability(CapabilityESPEHandler.ESPE_HANDLER_CAPABILITY, null)) {
				IESPEHandler handler = tile.getCapability(CapabilityESPEHandler.ESPE_HANDLER_CAPABILITY, null);
				double req = consumed - aconsumed;
				aconsumed += handler.extractESPE(req, true);
				if(aconsumed >= consumed) {
					return true;
				}
			}
		}
		return false;
	}

	public int getEnderstarEnergy() {
		if(tier == -1) {
			return 0;
		}

		double energy = 0;

		for(int i = 0; i < coords.length; ++i) {
			TileEntity tile = getWorld().getTileEntity(pos.add(coords[i]));
			if(tile == null) {
				continue;
			}
			if(tile.hasCapability(CapabilityESPEHandler.ESPE_HANDLER_CAPABILITY, null)) {
				IESPEHandler handler = tile.getCapability(CapabilityESPEHandler.ESPE_HANDLER_CAPABILITY, null);
				energy += handler.getESPE();
			}
		}

		return MathHelper.floor(energy);
	}

	@Override
	public void update() {
		List<EntityDemon> demons = getWorld().getEntitiesWithinAABB(EntityDemon.class, new AxisAlignedBB(pos));
		if(!demons.isEmpty()) {
			for(int i = 0; i < 4; ++i) {
				int time = (int)((getWorld().getWorldTime()+9*i)%36) * 10;
				double timeSin = Math.sin(Math.toRadians(time)) * 1.1D;
				double timeCos = Math.cos(Math.toRadians(time)) * 1.1D;
				double x = pos.getX() + 0.5D + timeSin;
				double y = pos.getY();
				double z = pos.getZ() + 0.5D + timeCos;
				EssentialCraftCore.proxy.SmokeFX(x, y, z, 0, 0.1D, 0, 1, 1, 0, 0);
			}
		}

		if(sCheckTick == 0) {
			checkStructureAndTier();
			sCheckTick = 40;
		}
		else {
			--sCheckTick;
		}

		if(getWorld().isRemote && tier >= 0) {
			int movement = (int)(getWorld().getWorldTime() % 60);
			if(movement > 30)
				movement = 60 - movement;
			for(int i = 0; i < 4; ++i) {
				if(getWorld().getTileEntity(pos.add(coords[i])) instanceof TileMithrilineCrystal) {
					getWorld().spawnParticle(EnumParticleTypes.REDSTONE, pos.getX()+coords[i].getX()+0.5D, pos.getY()+coords[i].getY()+movement/30D, pos.getZ()+coords[i].getZ()+0.5D, -1, 1, 0);
					getWorld().spawnParticle(EnumParticleTypes.REDSTONE, pos.getX()+coords[i].getX()+0.5D, pos.getY()+coords[i].getY()+2+movement/30D, pos.getZ()+coords[i].getZ()+0.5D, -1, 1, 0);
				}
			}
			for(int i = 4; i < 12; ++i) {
				if(getWorld().getTileEntity(pos.add(coords[i])) instanceof TileMithrilineCrystal) {
					getWorld().spawnParticle(EnumParticleTypes.REDSTONE, pos.getX()+coords[i].getX()+0.5D, pos.getY()+coords[i].getY()+movement/30D, pos.getZ()+coords[i].getZ()+0.5D, -1, 0, 1);
					getWorld().spawnParticle(EnumParticleTypes.REDSTONE, pos.getX()+coords[i].getX()+0.5D, pos.getY()+coords[i].getY()+2+movement/30D, pos.getZ()+coords[i].getZ()+0.5D, -1, 0, 1);
				}
			}
			for(int i = 12; i < 16; ++i) {
				if(getWorld().getTileEntity(pos.add(coords[i])) instanceof TileMithrilineCrystal) {
					getWorld().spawnParticle(EnumParticleTypes.REDSTONE, pos.getX()+coords[i].getX()+0.5D, pos.getY()+coords[i].getY()+movement/30D, pos.getZ()+coords[i].getZ()+0.5D, 0.1D, 0, 0.1D);
					getWorld().spawnParticle(EnumParticleTypes.REDSTONE, pos.getX()+coords[i].getX()+0.5D, pos.getY()+coords[i].getY()+2+movement/30D, pos.getZ()+coords[i].getZ()+0.5D, 0.1D, 0, 0.1D);
				}
			}
		}
	}

	protected static boolean testBlock(IBlockAccess world, BlockPos pos, Block block) {
		return world.getBlockState(pos).getBlock() == block;
	}

	protected static boolean testESPEHandler(IBlockAccess world, BlockPos pos, int minTier) {
		TileEntity tile = world.getTileEntity(pos);
		return tile != null &&
				tile.hasCapability(CapabilityESPEHandler.ESPE_HANDLER_CAPABILITY, null) &&
				tile.getCapability(CapabilityESPEHandler.ESPE_HANDLER_CAPABILITY, null).getTier() >= minTier;
	}

	protected static BiPredicate<IBlockAccess, BlockPos> tier0Checker = (w, cp)->
	testBlock(w, cp.add( 0, 0, 0), BlocksCore.voidStone) &&
	testBlock(w, cp.add( 2, 0, 0), BlocksCore.voidStone) &&
	testBlock(w, cp.add(-2, 0, 0), BlocksCore.voidStone) &&
	testBlock(w, cp.add( 0, 0,-2), BlocksCore.voidStone) &&
	testBlock(w, cp.add( 0, 0, 2), BlocksCore.voidStone) &&
	testBlock(w, cp.add( 3, 0, 1), BlocksCore.voidStone) &&
	testBlock(w, cp.add( 3, 0,-1), BlocksCore.voidStone) &&
	testBlock(w, cp.add(-3, 0, 1), BlocksCore.voidStone) &&
	testBlock(w, cp.add(-3, 0,-1), BlocksCore.voidStone) &&
	testBlock(w, cp.add( 1, 0, 3), BlocksCore.voidStone) &&
	testBlock(w, cp.add(-1, 0, 3), BlocksCore.voidStone) &&
	testBlock(w, cp.add( 1, 0,-3), BlocksCore.voidStone) &&
	testBlock(w, cp.add(-1, 0,-3), BlocksCore.voidStone) &&
	testBlock(w, cp.add( 3, 1, 0), Blocks.GLOWSTONE) &&
	testBlock(w, cp.add(-3, 1, 0), Blocks.GLOWSTONE) &&
	testBlock(w, cp.add( 0, 1, 3), Blocks.GLOWSTONE) &&
	testBlock(w, cp.add( 0, 1,-3), Blocks.GLOWSTONE) &&
	testBlock(w, cp.add( 2, 1, 2), BlocksCore.invertedBlock) &&
	testBlock(w, cp.add(-2, 1, 2), BlocksCore.invertedBlock) &&
	testBlock(w, cp.add( 2, 1,-2), BlocksCore.invertedBlock) &&
	testBlock(w, cp.add(-2, 1,-2), BlocksCore.invertedBlock) &&
	testBlock(w, cp.add( 1, 0, 0), BlocksCore.platingPale) &&
	testBlock(w, cp.add(-1, 0, 0), BlocksCore.platingPale) &&
	testBlock(w, cp.add( 0, 0, 1), BlocksCore.platingPale) &&
	testBlock(w, cp.add( 0, 0,-1), BlocksCore.platingPale) &&
	testBlock(w, cp.add( 2, 0, 1), BlocksCore.platingPale) &&
	testBlock(w, cp.add( 2, 0,-1), BlocksCore.platingPale) &&
	testBlock(w, cp.add(-2, 0, 1), BlocksCore.platingPale) &&
	testBlock(w, cp.add(-2, 0,-1), BlocksCore.platingPale) &&
	testBlock(w, cp.add( 1, 0, 2), BlocksCore.platingPale) &&
	testBlock(w, cp.add( 1, 0,-2), BlocksCore.platingPale) &&
	testBlock(w, cp.add(-1, 0, 2), BlocksCore.platingPale) &&
	testBlock(w, cp.add(-1, 0,-2), BlocksCore.platingPale) &&
	testBlock(w, cp.add( 3, 0, 2), BlocksCore.platingPale) &&
	testBlock(w, cp.add( 3, 0,-2), BlocksCore.platingPale) &&
	testBlock(w, cp.add(-3, 0, 2), BlocksCore.platingPale) &&
	testBlock(w, cp.add(-3, 0,-2), BlocksCore.platingPale) &&
	testBlock(w, cp.add( 2, 0, 3), BlocksCore.platingPale) &&
	testBlock(w, cp.add( 2, 0,-3), BlocksCore.platingPale) &&
	testBlock(w, cp.add(-2, 0, 3), BlocksCore.platingPale) &&
	testBlock(w, cp.add(-2, 0,-3), BlocksCore.platingPale) &&
	testESPEHandler(w, cp.add( 2, 2, 2), 0) &&
	testESPEHandler(w, cp.add(-2, 2, 2), 0) &&
	testESPEHandler(w, cp.add( 2, 2,-2), 0) &&
	testESPEHandler(w, cp.add(-2, 2,-2), 0) &&
	testESPEHandler(w, cp.add( 3, 1, 2), 1) &&
	testESPEHandler(w, cp.add( 3, 1,-2), 1) &&
	testESPEHandler(w, cp.add(-3, 1, 2), 1) &&
	testESPEHandler(w, cp.add(-3, 1,-2), 1) &&
	testESPEHandler(w, cp.add( 2, 1, 3), 1) &&
	testESPEHandler(w, cp.add( 2, 1,-3), 1) &&
	testESPEHandler(w, cp.add(-2, 1, 3), 1) &&
	testESPEHandler(w, cp.add(-2, 1,-3), 1) &&
	testESPEHandler(w, cp.add( 3, 2, 0), 2) &&
	testESPEHandler(w, cp.add(-3, 2, 0), 2) &&
	testESPEHandler(w, cp.add( 0, 2, 3), 2) &&
	testESPEHandler(w, cp.add( 0, 2,-3), 2);

	public void checkStructureAndTier() {
		World w = getWorld();
		BlockPos cp = pos.down();
		tier = -1;

		if(tier0Checker.test(w, cp))
			tier = 0;
	}
}
