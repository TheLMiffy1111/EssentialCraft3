package essentialcraft.common.tile;

import java.util.List;
import java.util.function.BiPredicate;

import DummyCore.Utils.BiPredicates;
import DummyCore.Utils.MathUtils;
import essentialcraft.common.block.BlocksCore;
import essentialcraft.common.item.ItemElementalFocus;
import essentialcraft.common.item.ItemElementalSword;
import essentialcraft.common.item.ItemEmber;
import essentialcraft.common.item.ItemsCore;
import essentialcraft.common.mod.EssentialCraftCore;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.config.Configuration;

public class TileEmberForge extends TileMRUGeneric {
	public int progressLevel, soundArray;

	public static boolean nightRequired = true;
	public static int timeRequired = 500;

	public TileEmberForge() {
		super(0);
		setSlotsNum(0);
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
		spawnParticles();
		if(getWorld().isBlockIndirectlyGettingPowered(pos) == 0) {
			if(structureChecker.test(getWorld(), getPos())) {
				boolean flag = false;
				if(world.isRemote) {
					for(int i = 0; i < 2; ++i) {
						getWorld().spawnParticle(EnumParticleTypes.FLAME, pos.getX()+getWorld().rand.nextDouble(),pos.getY()+1.1,pos.getZ()+getWorld().rand.nextDouble(),0,0,0);
					}
				}
				List<EntityItem> list = null;

				EntityItem ember_0 = null, ember_1 = null, ember_2 = null, ember_3 = null, focus_0 = null, focus_1 = null, focus_2 = null, focus_3 = null;
				list = getWorld().getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos.add( 2, 1, 0)),
						ei->ei.getItem().getItem() instanceof ItemEmber);
				if(!list.isEmpty()) {
					ember_0 = list.get(0);
				}

				list = getWorld().getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos.add(-2, 1, 0)),
						ei->ei.getItem().getItem() instanceof ItemEmber);
				if(!list.isEmpty()) {
					ember_1 = list.get(0);
				}

				list = getWorld().getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos.add( 0, 1, 2)),
						ei->ei.getItem().getItem() instanceof ItemEmber);
				if(!list.isEmpty()) {
					ember_2 = list.get(0);
				}

				list = getWorld().getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos.add( 0, 1,-2)),
						ei->ei.getItem().getItem() instanceof ItemEmber);
				if(!list.isEmpty()) {
					ember_3 = list.get(0);
				}

				list = getWorld().getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos.add( 2, 2, 2)),
						ei->ei.getItem().getItem() instanceof ItemElementalFocus);
				if(!list.isEmpty()) {
					focus_0 = list.get(0);
				}

				list = getWorld().getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos.add(-2, 2, 2)),
						ei->ei.getItem().getItem() instanceof ItemElementalFocus);
				if(!list.isEmpty()) {
					focus_1 = list.get(0);
				}

				list = getWorld().getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos.add( 2, 2, -2)),
						ei->ei.getItem().getItem() instanceof ItemElementalFocus);
				if(!list.isEmpty()) {
					focus_2 = list.get(0);
				}

				list = getWorld().getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos.add(-2, 2, -2)),
						ei->ei.getItem().getItem() instanceof ItemElementalFocus);
				if(!list.isEmpty()) {
					focus_3 = list.get(0);
				}

				--soundArray;
				if(isAllNonNull(ember_0, ember_1, ember_2, ember_3, focus_0, focus_1, focus_2, focus_3) &&
						(!nightRequired || getWorld().provider.isNether() || !getWorld().provider.hasSkyLight() || !getWorld().isDaytime())) {
					if(soundArray <= 0) {
						getWorld().playSound(null, pos, SoundEvents.BLOCK_PORTAL_TRIGGER, SoundCategory.BLOCKS, 1F, 0.2F);
						soundArray = 50;
					}
					++progressLevel;
					if(progressLevel >= timeRequired) {
						progressLevel = 0;
						if(!world.isRemote) {
							ember_0.getItem().shrink(1);
							if(ember_0.getItem().isEmpty()) {
								ember_0.setDead();
							}
							ember_1.getItem().shrink(1);
							if(ember_1.getItem().isEmpty()) {
								ember_1.setDead();
							}
							ember_2.getItem().shrink(1);
							if(ember_2.getItem().isEmpty()) {
								ember_2.setDead();
							}
							ember_3.getItem().shrink(1);
							if(ember_3.getItem().isEmpty()) {
								ember_3.setDead();
							}
							focus_0.getItem().shrink(1);
							if(focus_0.getItem().isEmpty()) {
								focus_0.setDead();
							}
							focus_1.getItem().shrink(1);
							if(focus_1.getItem().isEmpty()) {
								focus_1.setDead();
							}
							focus_2.getItem().shrink(1);
							if(focus_2.getItem().isEmpty()) {
								focus_2.setDead();
							}
							focus_3.getItem().shrink(1);
							if(focus_3.getItem().isEmpty()) {
								focus_3.setDead();
							}
							NBTTagCompound swordTag = new NBTTagCompound();
							swordTag.setString("ember_0", ember_0.getItem().getItem().getUnlocalizedName(ember_0.getItem()));
							swordTag.setString("ember_1", ember_1.getItem().getItem().getUnlocalizedName(ember_1.getItem()));
							swordTag.setString("ember_2", ember_2.getItem().getItem().getUnlocalizedName(ember_2.getItem()));
							swordTag.setString("ember_3", ember_3.getItem().getItem().getUnlocalizedName(ember_3.getItem()));
							swordTag.setString("focus_0", focus_0.getItem().getItem().getUnlocalizedName(focus_0.getItem()));
							swordTag.setString("focus_1", focus_1.getItem().getItem().getUnlocalizedName(focus_1.getItem()));
							swordTag.setString("focus_2", focus_2.getItem().getItem().getUnlocalizedName(focus_2.getItem()));
							swordTag.setString("focus_3", focus_3.getItem().getItem().getUnlocalizedName(focus_3.getItem()));
							ItemStack eSword = new ItemStack(ItemsCore.elementalSword, 1, 0);
							eSword.setTagCompound(swordTag);
							ItemElementalSword.setPrimaryAttribute(eSword);
							EntityItem elementalSword = new EntityItem(getWorld(), pos.getX()+0.5, pos.getY()+2.1, pos.getZ()+0.5, eSword);
							elementalSword.motionX = 0;
							elementalSword.motionY = 0.2;
							elementalSword.motionZ = 0;
							getWorld().spawnEntity(elementalSword);
						}
					}
					if(world.isRemote) {
						for(int i = 0; i < progressLevel/50; ++i) {
							getWorld().spawnParticle(EnumParticleTypes.LAVA, pos.getX()+getWorld().rand.nextDouble(), pos.getY()+1, pos.getZ()+getWorld().rand.nextDouble(), 0, 0, 0);
						}
					}
					flag = true;
				}
				else {
					progressLevel = 0;
					flag = false;
				}
				if(ember_0 != null) {
					ember_0.setPosition(pos.getX()+2.5, pos.getY()+1.25, pos.getZ()+0.5);
					ember_0.lifespan++;
					ember_0.motionX = 0;
					ember_0.motionY = 0;
					ember_0.motionZ = 0;
					if(world.isRemote) {
						for(int i = 0; i < 10; ++i) {
							getWorld().spawnParticle(EnumParticleTypes.REDSTONE, pos.getX()+0.5+(float)i/5, pos.getY()+0.9+i/12.5, pos.getZ()+0.5, 1, 0, 0);
						}
						getWorld().spawnParticle(EnumParticleTypes.FLAME, pos.getX()+2.5, pos.getY()+2, pos.getZ()+0.5, 0, 0, 0);
					}
					if(!flag) {
						getWorld().playSound(null, ember_0.getPosition(), SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.BLOCKS, 0.2F, 2.0F);
					}
				}
				if(ember_1 != null) {
					ember_1.setPosition(pos.getX()-1.5, pos.getY()+1.25, pos.getZ()+0.5);
					ember_1.lifespan++;
					ember_1.motionX = 0;
					ember_1.motionY = 0;
					ember_1.motionZ = 0;
					if(world.isRemote) {
						for(int i = 0; i < 10; ++i) {
							getWorld().spawnParticle(EnumParticleTypes.REDSTONE, pos.getX()+0.5-(float)i/5, pos.getY()+0.9+i/12.5, pos.getZ()+0.5, 1, 0, 0);
						}
						getWorld().spawnParticle(EnumParticleTypes.FLAME, pos.getX()-1.5, pos.getY()+2, pos.getZ()+0.5, 0, 0, 0);
					}
					if(!flag) {
						getWorld().playSound(null, ember_1.getPosition(), SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.BLOCKS, 0.2F, 2.0F);
					}
				}
				if(ember_2 != null) {
					ember_2.setPosition(pos.getX()+0.5, pos.getY()+1.25, pos.getZ()+2.5);
					ember_2.lifespan++;
					ember_2.motionX = 0;
					ember_2.motionY = 0;
					ember_2.motionZ = 0;
					if(world.isRemote) {
						for(int i = 0; i < 10; ++i) {
							getWorld().spawnParticle(EnumParticleTypes.REDSTONE, pos.getX()+0.5, pos.getY()+0.9+i/12.5, pos.getZ()+0.5+(float)i/5, 1, 0, 0);
						}
						getWorld().spawnParticle(EnumParticleTypes.FLAME, pos.getX()+0.5, pos.getY()+2, pos.getZ()+2.5, 0, 0, 0);
					}
					if(!flag) {
						getWorld().playSound(null, ember_2.getPosition(), SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.BLOCKS, 0.2F, 2.0F);
					}
				}
				if(ember_3 != null) {
					ember_3.setPosition(pos.getX()+0.5, pos.getY()+1.25, pos.getZ()-1.5);
					ember_3.lifespan++;
					ember_3.motionX = 0;
					ember_3.motionY = 0;
					ember_3.motionZ = 0;
					if(world.isRemote) {
						for(int i = 0; i < 10; ++i) {
							getWorld().spawnParticle(EnumParticleTypes.REDSTONE, pos.getX()+0.5, pos.getY()+0.9+i/12.5, pos.getZ()+0.5-(float)i/5, 1, 0, 0);
						}
						getWorld().spawnParticle(EnumParticleTypes.FLAME, pos.getX()+0.5, pos.getY()+2, pos.getZ()-1.5, 0, 0, 0);
					}
					if(!flag) {
						getWorld().playSound(null, ember_3.getPosition(), SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.BLOCKS, 0.2F, 2.0F);
					}
				}
				if(focus_0 != null) {
					focus_0.setPosition(pos.getX()+2.5, pos.getY()+2.25, pos.getZ()+2.5);
					focus_0.lifespan++;
					focus_0.motionX = 0;
					focus_0.motionY = 0;
					focus_0.motionZ = 0;
					if(world.isRemote) {
						getWorld().spawnParticle(EnumParticleTypes.PORTAL, pos.getX()+0.5, pos.getY()+0.9, pos.getZ()+0.5, 2, 1, 2);
						for(int i = 0; i < 10; ++i) {
							getWorld().spawnParticle(EnumParticleTypes.REDSTONE, pos.getX()+2.5, pos.getY()+2.7, pos.getZ()+2.5, 0, 0, 1);
						}
					}
				}
				if(focus_1 != null) {
					focus_1.setPosition(pos.getX()-1.5, pos.getY()+2.25, pos.getZ()+2.5);
					focus_1.lifespan++;
					focus_1.motionX = 0;
					focus_1.motionY = 0;
					focus_1.motionZ = 0;
					if(world.isRemote) {
						getWorld().spawnParticle(EnumParticleTypes.PORTAL, pos.getX()+0.5, pos.getY()+0.9, pos.getZ()+0.5, -2, 1, 2);
						for(int i = 0; i < 10; ++i) {
							getWorld().spawnParticle(EnumParticleTypes.REDSTONE, pos.getX()-1.5, pos.getY()+2.7, pos.getZ()+2.5, 0, 0, 1);
						}
					}
				}
				if(focus_2 != null) {
					focus_2.setPosition(pos.getX()+2.5, pos.getY()+2.25, pos.getZ()-1.5);
					focus_2.lifespan++;
					focus_2.motionX = 0;
					focus_2.motionY = 0;
					focus_2.motionZ = 0;
					if(world.isRemote) {
						getWorld().spawnParticle(EnumParticleTypes.PORTAL, pos.getX()+0.5, pos.getY()+0.9, pos.getZ()+0.5, 2, 1, -2);
						for(int i = 0; i < 10; ++i) {
							getWorld().spawnParticle(EnumParticleTypes.REDSTONE, pos.getX()+2.5, pos.getY()+2.7, pos.getZ()-1.5, 0, 0, 1);
						}
					}
				}
				if(focus_3 != null) {
					focus_3.setPosition(pos.getX()-1.5, pos.getY()+2.25, pos.getZ()-1.5);
					focus_3.lifespan++;
					focus_3.motionX = 0;
					focus_3.motionY = 0;
					focus_3.motionZ = 0;
					if(world.isRemote) {
						getWorld().spawnParticle(EnumParticleTypes.PORTAL, pos.getX()+0.5, pos.getY()+0.9, pos.getZ()+0.5, -2, 1, -2);
						for(int i = 0; i < 10; ++i) {
							getWorld().spawnParticle(EnumParticleTypes.REDSTONE, pos.getX()-1.5, pos.getY()+2.7, pos.getZ()-1.5, 0, 0, 1);
						}
					}
				}
			}
			else {
				progressLevel = 0;
			}
		}
	}

	protected static boolean isAllNonNull(Object... arr) {
		for(Object obj : arr) {
			if(obj == null) {
				return false;
			}
		}
		return true;
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
			testBlock(world, pos.add(-2, 0, 2), BlocksCore.voidStone) &&
			testBlock(world, pos.add( 2, 0,-2), BlocksCore.voidStone) &&
			testBlock(world, pos.add(-2, 0,-2), BlocksCore.voidStone) &&
			testBlock(world, pos.add( 2, 0, 0), BlocksCore.platingPale) &&
			testBlock(world, pos.add( 0, 0, 2), BlocksCore.platingPale) &&
			testBlock(world, pos.add(-2, 0, 0), BlocksCore.platingPale) &&
			testBlock(world, pos.add( 0, 0,-2), BlocksCore.platingPale) &&
			testBlock(world, pos.add( 2, 1, 2), BlocksCore.magicPlating) &&
			testBlock(world, pos.add(-2, 1, 2), BlocksCore.magicPlating) &&
			testBlock(world, pos.add(-2, 1,-2), BlocksCore.magicPlating) &&
			testBlock(world, pos.add( 2, 1,-2), BlocksCore.magicPlating)
			);

	public void spawnParticles() {
		if(world.isRemote && structureChecker.test(getWorld(), getPos())) {
			/*for(int i = 0; i < 100; ++i)*/ {
				EssentialCraftCore.proxy.spawnParticle("cSpellFX", pos.getX()+0.5F + MathUtils.randomFloat(getWorld().rand)*3, pos.getY(), pos.getZ()+0.5F + MathUtils.randomFloat(getWorld().rand)*3, 0,2, 0);
			}
		}
	}

	public static void setupConfig(Configuration cfg) {
		try {
			String category = "tileentities.emberforge";
			nightRequired = cfg.get(category, "NightRequired", true).getBoolean();
			timeRequired = cfg.get(category, "TicksRequired", 500).setMinValue(0).getInt();
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
