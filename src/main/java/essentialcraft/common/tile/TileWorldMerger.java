package essentialcraft.common.tile;

import java.util.List;
import java.util.function.BiPredicate;

import DummyCore.Utils.BiPredicates;
import DummyCore.Utils.MathUtils;
import essentialcraft.api.ApiCore;
import essentialcraft.common.block.BlocksCore;
import essentialcraft.common.mod.EssentialCraftCore;
import essentialcraft.common.world.gen.WorldGenMerge;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.config.Configuration;

public class TileWorldMerger extends TileMRUGeneric {

	public static int cfgMaxMRU = ApiCore.DEVICE_MAX_MRU_GENERIC*2;
	public static int mruUsage = 100;
	public static int requiredTicks = 1000;

	public int progressLevel = 0;

	public TileWorldMerger() {
		super(cfgMaxMRU);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		progressLevel = nbt.getInteger("progress");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setInteger("progress", progressLevel);
		return nbt;
	}

	@Override
	public void update() {
		super.update();
		mruStorage.update(getPos(), getWorld(), getStackInSlot(0));
		spawnParticles();
		if(getWorld().isBlockIndirectlyGettingPowered(pos) == 0) {
			if(structureChecker.test(getWorld(), getPos())) {
				EntityItem star = null;
				List<EntityItem> list = world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos.up()),
						ei->ei.getItem().getItem() == Items.NETHER_STAR);
				if(!list.isEmpty()) {
					star = list.get(0);
				}
				if(star != null) {
					star.setPosition(pos.getX()+0.5, pos.getY()+1, pos.getZ()+0.5);
					star.lifespan++;
					star.motionX = 0;
					star.motionY = 0;
					star.motionZ = 0;
					if(mruStorage.getMRU() >= mruUsage) {
						if(world.isRemote) {
							world.spawnParticle(EnumParticleTypes.FLAME, pos.getX() + 2.5D + MathUtils.randomDouble(world.rand) / 2.0D, pos.getY() + 2.1D, pos.getZ() + 2.5D + MathUtils.randomDouble(world.rand) / 2.0D, -0.12D, -0.05D, -0.12D);
							world.spawnParticle(EnumParticleTypes.FLAME, pos.getX() + 2.5D + MathUtils.randomDouble(world.rand) / 2.0D, pos.getY() + 2.1D, pos.getZ() - 1.5D + MathUtils.randomDouble(world.rand) / 2.0D, -0.12D, -0.05D, 0.12D);
							world.spawnParticle(EnumParticleTypes.FLAME, pos.getX() - 1.5D + MathUtils.randomDouble(world.rand) / 2.0D, pos.getY() + 2.1D, pos.getZ() - 1.5D + MathUtils.randomDouble(world.rand) / 2.0D, 0.12D, -0.05D, 0.12D);
							world.spawnParticle(EnumParticleTypes.FLAME, pos.getX() - 1.5D + MathUtils.randomDouble(world.rand) / 2.0D, pos.getY() + 2.1D, pos.getZ() + 2.5D + MathUtils.randomDouble(world.rand) / 2.0D, 0.12D, -0.05D, -0.12D);
							world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, pos.getX() + 0.5D + MathUtils.randomDouble(world.rand) / 3.0D, pos.getY() + 1.1D, pos.getZ() + 0.5D + MathUtils.randomDouble(world.rand) / 3.0D, 0.0D, 0.0D, 0.0D);
						}
						mruStorage.extractMRU(mruUsage, true);
						++progressLevel;
						if(progressLevel >= requiredTicks) {
							progressLevel = 0;
							star.getItem().shrink(1);
							if(star.getItem().isEmpty()) {
								star.setDead();
							}
							if(!world.isRemote) {
								new WorldGenMerge(world.rand).generate(world, world.rand, pos.up(10));
							}
							world.playSound(pos.getX(), pos.getY()+10, pos.getZ(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 2, 1, false);
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

	public void spawnParticles() {
		if(world.isRemote && structureChecker.test(getWorld(), getPos())) {
			/*for(int i = 0; i < 100; ++i)*/ {
				EssentialCraftCore.proxy.spawnParticle("cSpellFX", pos.getX()+0.5F + MathUtils.randomFloat(getWorld().rand)*3, pos.getY(), pos.getZ()+0.5F + MathUtils.randomFloat(getWorld().rand)*3, 0,2, 0);
			}
		}
	}

	protected static boolean testBlock(IBlockAccess world, BlockPos pos, Block block) {
		return world.getBlockState(pos).getBlock() == block;
	}

	protected static BiPredicate<IBlockAccess, BlockPos> structureChecker = BiPredicates.<IBlockAccess, BlockPos>and(
			(world, pos)->{
				for(int x = -2; x <= 2; ++x) {
					for(int z = -2; z <= 2; ++z) {
						if(!testBlock(world, pos.add(x, -1, z), BlocksCore.voidStone)) {
							return false;
						}
					}
				}
				return true;
			}, (world, pos)->
			testBlock(world, pos.add( 2, 0, 2), BlocksCore.voidStone) &&
			testBlock(world, pos.add( 2, 0,-2), BlocksCore.voidStone) &&
			testBlock(world, pos.add(-2, 0, 2), BlocksCore.voidStone) &&
			testBlock(world, pos.add(-2, 0,-2), BlocksCore.voidStone) &&
			testBlock(world, pos.add( 2, 1, 2), Blocks.GLOWSTONE) &&
			testBlock(world, pos.add( 2, 1,-2), Blocks.GLOWSTONE) &&
			testBlock(world, pos.add(-2, 1, 2), Blocks.GLOWSTONE) &&
			testBlock(world, pos.add(-2, 1,-2), Blocks.GLOWSTONE)
			);

	@Override
	public int[] getOutputSlots() {
		return new int[0];
	}

	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return INFINITE_EXTENT_AABB;
	}

	public static void setupConfig(Configuration cfg) {
		try {
			String category = "tileentities.worldmerger";
			cfgMaxMRU = cfg.get(category, "MaxMRU", ApiCore.DEVICE_MAX_MRU_GENERIC*2).setMinValue(1).getInt();
			mruUsage = cfg.get(category, "MRUUsage", 100).setMinValue(0).setMaxValue(cfgMaxMRU).getInt();
			requiredTicks = cfg.get(category, "RequiredTicks", 1000).setMinValue(1).getInt();
		}
		catch(Exception e) {
			return;
		}
	}
}
