package ec3.common.tile;

import java.util.List;

import ec3.common.block.BlocksCore;
import ec3.common.item.ItemElementalFocus;
import ec3.common.item.ItemElementalSword;
import ec3.common.item.ItemEmber;
import ec3.common.item.ItemsCore;
import ec3.common.mod.EssentialCraftCore;
import DummyCore.Utils.DataStorage;
import DummyCore.Utils.DummyData;
import DummyCore.Utils.MathUtils;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.config.Configuration;

public class TileEmberForge extends TileMRUGeneric {
	public int progressLevel, soundArray;
	
	public static boolean nightRequired = true;
	public static int timeRequired = 500;
	
	public TileEmberForge() {
		super();
		setSlotsNum(0);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
		super.readFromNBT(par1NBTTagCompound);
		progressLevel = par1NBTTagCompound.getInteger("progress");
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound par1NBTTagCompound) {
		super.writeToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setInteger("progress", progressLevel);
		return par1NBTTagCompound;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void update() {
		maxMRU = 50000;
		super.update();
		spawnParticles();
		if(isStructureCorrect()) {
			boolean flag = false;
			for(int i = 0; i < 2; ++i) {
				getWorld().spawnParticle(EnumParticleTypes.FLAME, pos.getX()+getWorld().rand.nextDouble(),pos.getY()+1.1,pos.getZ()+getWorld().rand.nextDouble(),0,0,0);
			}
			List<EntityItem> l_0 = getWorld().getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos.getX()+2, pos.getY(), pos.getZ(), pos.getX()+3, pos.getY()+2, pos.getZ()+1));
			EntityItem ember_0 = null;
			if(!l_0.isEmpty() && l_0.get(0).getEntityItem().getItem() instanceof ItemEmber)
				ember_0 = l_0.get(0); 
			List<EntityItem> l_1 = getWorld().getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos.getX()-2, pos.getY(), pos.getZ(), pos.getX()-1, pos.getY()+2, pos.getZ()+1));
			EntityItem ember_1 = null;
			if(!l_1.isEmpty() && l_1.get(0).getEntityItem().getItem() instanceof ItemEmber)
				ember_1 = l_1.get(0); 
			List<EntityItem> l_2 = getWorld().getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ()+2, pos.getX()+1, pos.getY()+2, pos.getZ()+3));
			EntityItem ember_2 = null;
			if(!l_2.isEmpty() && l_2.get(0).getEntityItem().getItem() instanceof ItemEmber)
				ember_2 = l_2.get(0); 
			List<EntityItem> l_3 = getWorld().getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ()-2, pos.getX()+1, pos.getY()+2, pos.getZ()-1));
			EntityItem ember_3 = null;
			if(!l_3.isEmpty() && l_3.get(0).getEntityItem().getItem() instanceof ItemEmber)
				ember_3 = l_3.get(0); 
			List<EntityItem> l_4 = getWorld().getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos.getX()+2, pos.getY()+2, pos.getZ()+2, pos.getX()+3, pos.getY()+4, pos.getZ()+3));
			EntityItem focus_0 = null;
			if(!l_4.isEmpty() && l_4.get(0).getEntityItem().getItem() instanceof ItemElementalFocus)
				focus_0 = l_4.get(0); 
			List<EntityItem> l_41 = getWorld().getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos.getX()-2, pos.getY()+2, pos.getZ()+2, pos.getX()-1, pos.getY()+4, pos.getZ()+3));
			EntityItem focus_1 = null;
			if(!l_41.isEmpty() && l_41.get(0).getEntityItem().getItem() instanceof ItemElementalFocus)
				focus_1 = l_41.get(0);
			List<EntityItem> l_411 = getWorld().getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos.getX()+2, pos.getY()+2, pos.getZ()-2, pos.getX()+3, pos.getY()+4, pos.getZ()-1));
			EntityItem focus_2 = null;
			if(!l_411.isEmpty() && l_411.get(0).getEntityItem().getItem() instanceof ItemElementalFocus)
				focus_2 = l_411.get(0); 
			List<EntityItem> l_4111 = getWorld().getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos.getX()-2, pos.getY()+2, pos.getZ()-2, pos.getX()-1, pos.getY()+4, pos.getZ()-1));
			EntityItem focus_3 = null;
			if(!l_4111.isEmpty() && l_4111.get(0).getEntityItem().getItem() instanceof ItemElementalFocus)
				focus_3 = l_4111.get(0); 
			--soundArray;
			if(ember_0 != null && ember_1 != null && ember_2 != null && ember_3 != null && focus_0 != null && focus_1 != null && focus_2 != null && focus_3 != null && (!getWorld().isDaytime() || !nightRequired)) {
				if(soundArray <= 0) {
					getWorld().playSound(null, pos, SoundEvents.BLOCK_PORTAL_TRIGGER, SoundCategory.BLOCKS, 1F, 0.2F);
					soundArray = 50;
				}
				++progressLevel;
				if(progressLevel >= timeRequired) {
					progressLevel = 0;
					ember_0.setDead();
					ember_1.setDead();
					ember_2.setDead();
					ember_3.setDead();
					focus_0.setDead();
					focus_1.setDead();
					focus_2.setDead();
					focus_3.setDead();
					NBTTagCompound swordTag = new NBTTagCompound();
					swordTag.setString("ember_0", ember_0.getEntityItem().getItem().getUnlocalizedName(ember_0.getEntityItem()));
					swordTag.setString("ember_1", ember_1.getEntityItem().getItem().getUnlocalizedName(ember_1.getEntityItem()));
					swordTag.setString("ember_2", ember_2.getEntityItem().getItem().getUnlocalizedName(ember_2.getEntityItem()));
					swordTag.setString("ember_3", ember_3.getEntityItem().getItem().getUnlocalizedName(ember_3.getEntityItem()));
					swordTag.setString("focus_0", focus_0.getEntityItem().getItem().getUnlocalizedName(focus_0.getEntityItem()));
					swordTag.setString("focus_1", focus_1.getEntityItem().getItem().getUnlocalizedName(focus_1.getEntityItem()));
					swordTag.setString("focus_2", focus_2.getEntityItem().getItem().getUnlocalizedName(focus_2.getEntityItem()));
					swordTag.setString("focus_3", focus_3.getEntityItem().getItem().getUnlocalizedName(focus_3.getEntityItem()));
					ItemStack eSword = new ItemStack(ItemsCore.elementalSword, 1, 0);
					eSword.setTagCompound(swordTag);
					ItemElementalSword.setPrimaryAttribute(eSword);
					EntityItem elementalSword = new EntityItem(getWorld(), pos.getX()+0.5, pos.getY()+2.1, pos.getZ()+0.5, eSword);
					if(!getWorld().isRemote)
						getWorld().spawnEntity(elementalSword);
				}
				for(int i = 0; i < progressLevel/50; ++i)
					getWorld().spawnParticle(EnumParticleTypes.LAVA, pos.getX()+getWorld().rand.nextDouble(), pos.getY()+1, pos.getZ()+getWorld().rand.nextDouble(), 0, 0, 0);
				flag = true;
			}
			else {
				progressLevel = 0;
				flag = false;
			}
			if(focus_0 != null) {
				focus_0.setPosition(pos.getX()+2.5, pos.getY()+2.5, pos.getZ()+2.5);
				focus_0.motionX = 0;
				focus_0.motionY = 0;
				focus_0.motionZ = 0;
				getWorld().spawnParticle(EnumParticleTypes.PORTAL, pos.getX()+0.5, pos.getY()+0.9, pos.getZ()+0.5, 2, 1, 2);
				for(int i = 0; i < 10; ++i)
					getWorld().spawnParticle(EnumParticleTypes.REDSTONE, pos.getX()+2.5, pos.getY()+2.7, pos.getZ()+2.5, 0, 0, 1);
			}
			if(focus_1 != null) {
				focus_1.setPosition(pos.getX()-1.5, pos.getY()+2.5, pos.getZ()+2.5);
				focus_1.motionX = 0;
				focus_1.motionY = 0;
				focus_1.motionZ = 0;
				getWorld().spawnParticle(EnumParticleTypes.PORTAL, pos.getX()+0.5, pos.getY()+0.9, pos.getZ()+0.5, -2, 1, 2);
				for(int i = 0; i < 10; ++i)
					getWorld().spawnParticle(EnumParticleTypes.REDSTONE, pos.getX()-1.5, pos.getY()+2.7, pos.getZ()+2.5, 0, 0, 1);
			}
			if(focus_2 != null) {
				focus_2.setPosition(pos.getX()+2.5, pos.getY()+2.5, pos.getZ()-1.5);
				focus_2.motionX = 0;
				focus_2.motionY = 0;
				focus_2.motionZ = 0;
				getWorld().spawnParticle(EnumParticleTypes.PORTAL, pos.getX()+0.5, pos.getY()+0.9, pos.getZ()+0.5, 2, 1, -2);
				for(int i = 0; i < 10; ++i)
					getWorld().spawnParticle(EnumParticleTypes.REDSTONE, pos.getX()+2.5, pos.getY()+2.7, pos.getZ()-1.5, 0, 0, 1);
			}
			if(focus_3 != null) {
				focus_3.setPosition(pos.getX()-1.5, pos.getY()+2.5, pos.getZ()-1.5);
				focus_3.motionX = 0;
				focus_3.motionY = 0;
				focus_3.motionZ = 0;
				getWorld().spawnParticle(EnumParticleTypes.PORTAL, pos.getX()+0.5, pos.getY()+0.9, pos.getZ()+0.5, -2, 1, -2);
				for(int i = 0; i < 10; ++i)
					getWorld().spawnParticle(EnumParticleTypes.REDSTONE, pos.getX()-1.5, pos.getY()+2.7, pos.getZ()-1.5, 0, 0, 1);
			}
			if(ember_0 != null) {
				ember_0.setPosition(pos.getX()+2.5, pos.getY()+1.5, pos.getZ()+0.5);
				ember_0.motionX = 0;
				ember_0.motionY = 0;
				ember_0.motionZ = 0;
				for(int i = 0; i < 10; ++i)
					getWorld().spawnParticle(EnumParticleTypes.REDSTONE, pos.getX()+0.5+(float)i/5, pos.getY()+0.9+(float)i/12.5, pos.getZ()+0.5, 1, 0, 0);
				getWorld().spawnParticle(EnumParticleTypes.FLAME, pos.getX()+2.5, pos.getY()+2, pos.getZ()+0.5, 0, 0, 0);
				if(!flag)
					getWorld().playSound(null, ember_0.getPosition(), SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.BLOCKS, 0.2F, 2.0F);
    		}
			if(ember_1!= null) {
				ember_1.setPosition(pos.getX()-1.5, pos.getY()+1.5, pos.getZ()+0.5);
				ember_1.motionX = 0;
				ember_1.motionY = 0;
				ember_1.motionZ = 0;
				for(int i = 0; i < 10; ++i)
					getWorld().spawnParticle(EnumParticleTypes.REDSTONE, pos.getX()+0.5-(float)i/5, pos.getY()+0.9+(float)i/12.5, pos.getZ()+0.5, 1, 0, 0);
				getWorld().spawnParticle(EnumParticleTypes.FLAME, pos.getX()-1.5, pos.getY()+2, pos.getZ()+0.5, 0, 0, 0);
				if(!flag)
					getWorld().playSound(null, ember_1.getPosition(), SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.BLOCKS, 0.2F, 2.0F);
			}
			if(ember_2 != null) {
				ember_2.setPosition(pos.getX()+0.5, pos.getY()+1.5, pos.getZ()+2.5);
				ember_2.motionX = 0;
				ember_2.motionY = 0;
				ember_2.motionZ = 0;
				for(int i = 0; i < 10; ++i)
					getWorld().spawnParticle(EnumParticleTypes.REDSTONE, pos.getX()+0.5, pos.getY()+0.9+(float)i/12.5, pos.getZ()+0.5+(float)i/5, 1, 0, 0);
				getWorld().spawnParticle(EnumParticleTypes.FLAME, pos.getX()+0.5, pos.getY()+2, pos.getZ()+2.5, 0, 0, 0);
				if(!flag)
					getWorld().playSound(null, ember_2.getPosition(), SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.BLOCKS, 0.2F, 2.0F);
			}
			if(ember_3 != null) {
				ember_3.setPosition(pos.getX()+0.5, pos.getY()+1.5, pos.getZ()-1.5);
				ember_3.motionX = 0;
				ember_3.motionY = 0;
				ember_3.motionZ = 0;
				for(int i = 0; i < 10; ++i)
					getWorld().spawnParticle(EnumParticleTypes.REDSTONE, pos.getX()+0.5, pos.getY()+0.9+(float)i/12.5, pos.getZ()+0.5-(float)i/5, 1, 0, 0);
				getWorld().spawnParticle(EnumParticleTypes.FLAME, pos.getX()+0.5, pos.getY()+2, pos.getZ()-1.5, 0, 0, 0);
				if(!flag)
					getWorld().playSound(null, ember_3.getPosition(), SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.BLOCKS, 0.2F, 2.0F);
				}
		}
	}
	
	public boolean isStructureCorrect() {
		boolean flag = true;
		for(int x = -2; x <= 2; ++x) {
			for(int z = -2; z <= 2; ++z) {
				flag = getWorld().getBlockState(new BlockPos(pos.getX()+x, pos.getY()-1, pos.getZ()+z)).getBlock() == BlocksCore.voidStone;
				if(!flag)
					return false;
			}
		}
		flag =
				getWorld().getBlockState(pos.add(2, 0, 2)).getBlock() == BlocksCore.voidStone &&
				getWorld().getBlockState(pos.add(-2, 0, 2)).getBlock() == BlocksCore.voidStone &&
				getWorld().getBlockState(pos.add(2, 0, -2)).getBlock() == BlocksCore.voidStone &&
				getWorld().getBlockState(pos.add(-2, 0, -2)).getBlock() == BlocksCore.voidStone &&
				getWorld().getBlockState(pos.add(2, 0, 0)).getBlock() != Blocks.AIR &&
				getWorld().getBlockState(pos.add(0, 0, 2)).getBlock() != Blocks.AIR &&
				getWorld().getBlockState(pos.add(-2, 0, 0)).getBlock() != Blocks.AIR &&
				getWorld().getBlockState(pos.add(0, 0, -2)).getBlock() != Blocks.AIR &&
				getWorld().getBlockState(pos.add(2, 1, 2)).getBlock() != Blocks.AIR &&
				getWorld().getBlockState(pos.add(-2, 1, 2)).getBlock() != Blocks.AIR &&
				getWorld().getBlockState(pos.add(-2, 1, -2)).getBlock() != Blocks.AIR &&
				getWorld().getBlockState(pos.add(2, 1, -2)).getBlock() != Blocks.AIR;
		return flag;
	}
	
	public void spawnParticles() {
		if(isStructureCorrect()) {
			/*for(int i = 0; i < 100; ++i)*/ {
				EssentialCraftCore.proxy.spawnParticle("cSpellFX", pos.getX()+0.5F + MathUtils.randomFloat(getWorld().rand)*3, pos.getY(), pos.getZ()+0.5F + MathUtils.randomFloat(getWorld().rand)*3, 0,2, 0);
			}
		}
	}
	
	public static void setupConfig(Configuration cfg) {
		try {
			cfg.load();
			String[] cfgArrayString = cfg.getStringList("EmberForgeSettings", "tileentities", new String[] {
					"Is night time required:true",
					"Required time to craft a sword:500"
			}, "");
			String dataString = "";
			
			for(int i = 0; i < cfgArrayString.length; ++i)
				dataString += "||" + cfgArrayString[i];
			
			DummyData[] data = DataStorage.parseData(dataString);
			
			timeRequired = Integer.parseInt(data[1].fieldValue);
			nightRequired = Boolean.parseBoolean(data[0].fieldValue);
			
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
