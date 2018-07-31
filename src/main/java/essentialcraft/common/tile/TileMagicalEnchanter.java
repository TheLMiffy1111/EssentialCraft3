package essentialcraft.common.tile;

import java.util.List;
import java.util.Random;

import essentialcraft.api.ApiCore;
import essentialcraft.utils.common.ECUtils;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.config.Configuration;

public class TileMagicalEnchanter extends TileMRUGeneric {

	List<EnchantmentData> enchants;
	public int progressLevel = -1;
	public int tickCount;
	public float pageFlip;
	public float pageFlipPrev;
	public float flipT;
	public float flipA;
	public float bookSpread;
	public float bookSpreadPrev;
	public float bookRotation;
	public float bookRotationPrev;
	public float tRot;
	private static final Random rand = new Random();

	public static int cfgMaxMRU = ApiCore.DEVICE_MAX_MRU_GENERIC;
	public static boolean generatesCorruption = false;
	public static int genCorruption = 2;
	public static int mruUsage = 100;
	public static int maxEnchantmentLevel = 60;

	public TileMagicalEnchanter() {
		super(cfgMaxMRU);
		setSlotsNum(3);
	}

	@Override
	public void update() {
		super.update();
		mruStorage.update(getPos(), getWorld(), getStackInSlot(0));

		if(getWorld().isBlockIndirectlyGettingPowered(pos) == 0)
			tryEnchant();

		this.bookSpreadPrev = this.bookSpread;
		this.bookRotationPrev = this.bookRotation;
		EntityPlayer entityplayer = this.world.getClosestPlayer(this.pos.getX() + 0.5D, this.pos.getY() + 0.5D, this.pos.getZ() + 0.5D, 3.0D, false);

		if(entityplayer != null) {
			double d0 = entityplayer.posX - (this.pos.getX() + 0.5D);
			double d1 = entityplayer.posZ - (this.pos.getZ() + 0.5D);
			this.tRot = (float)MathHelper.atan2(d1, d0);
			this.bookSpread += 0.1F;

			if(this.bookSpread < 0.5F || rand.nextInt(40) == 0) {
				float f1 = this.flipT;

				while(true) {
					this.flipT += rand.nextInt(4) - rand.nextInt(4);

					if(f1 != this.flipT) {
						break;
					}
				}
			}
		}
		else {
			this.tRot += 0.02F;
			this.bookSpread -= 0.1F;
		}

		while(this.bookRotation >= Math.PI) {
			this.bookRotation -= Math.PI * 2F;
		}

		while(this.bookRotation < -Math.PI) {
			this.bookRotation += Math.PI * 2F;
		}

		while(this.tRot >= Math.PI) {
			this.tRot -= Math.PI * 2F;
		}

		while(this.tRot < -Math.PI) {
			this.tRot += Math.PI * 2F;
		}

		float f2;

		for(f2 = this.tRot - this.bookRotation; f2 >= Math.PI; f2 -= Math.PI * 2F) {
			;
		}

		while(f2 < -Math.PI) {
			f2 += Math.PI * 2F;
		}

		this.bookRotation += f2 * 0.4F;
		this.bookSpread = MathHelper.clamp(this.bookSpread, 0.0F, 1.0F);
		++this.tickCount;
		this.pageFlipPrev = this.pageFlip;
		float f = (this.flipT - this.pageFlip) * 0.4F;
		float f3 = 0.2F;
		f = MathHelper.clamp(f, -0.2F, 0.2F);
		this.flipA += (f - this.flipA) * 0.9F;
		this.pageFlip += this.flipA;
	}

	@Override
	public void readFromNBT(NBTTagCompound i) {
		progressLevel = i.getInteger("work");
		super.readFromNBT(i);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound i) {
		i.setInteger("work", progressLevel);
		return super.writeToNBT(i);
	}

	public void tryEnchant() {
		if(canItemBeEnchanted() && mruStorage.getMRU() >= mruUsage) {
			mruStorage.extractMRU(mruUsage, true);
			if(generatesCorruption)
				ECUtils.randomIncreaseCorruptionAt(getWorld(), pos, getWorld().rand, genCorruption);
			++progressLevel;
			if(progressLevel >= getRequiredTimeToAct()) {
				enchant();
				progressLevel = -1;
			}
		}
		if(!canItemBeEnchanted()) {
			progressLevel = -1;
			enchants = null;
		}
	}

	public void enchant() {
		List<EnchantmentData> enchants = getEnchantmentsForStack(getStackInSlot(1));
		ItemStack enchanted = getStackInSlot(1).copy();
		enchanted.setCount(1);
		decrStackSize(1, 1);
		for(int m = 0; m < enchants.size(); ++m) {
			EnchantmentData d = enchants.get(m);
			if(d != null) {
				if(enchanted.getItem() == Items.BOOK) {
					enchanted = new ItemStack(Items.ENCHANTED_BOOK, 1, 0);
				}
				enchanted.addEnchantment(d.enchantment, d.enchantmentLevel);
			}
		}
		setInventorySlotContents(2, enchanted);
		enchants = null;
	}

	public List<EnchantmentData> getEnchantmentsForStack(ItemStack stack) {
		if(enchants == null)
			enchants = EnchantmentHelper.buildEnchantmentList(getWorld().rand, stack, getMaxPower(), false);
		return enchants;
	}

	public int getRequiredTimeToAct() {
		return getMaxPower()*10;
	}

	public int getRequiredMRU() {
		return getMaxPower()*1000;
	}

	public int getMaxPower() {
		int l = 0;
		for(int x = -2; x <= 2; ++x) {
			for(int y = 0; y <= 2; ++y) {
				for(int z = -2; z <= 2; ++z) {
					if(x != 0 || y != 0 || z != 0) {
						l += ForgeHooks.getEnchantPower(getWorld(), pos.add(x, y, z));
					}
				}
			}
		}
		if(l > maxEnchantmentLevel)
			l = maxEnchantmentLevel;
		return l;
	}

	public boolean canItemBeEnchanted() {
		try {
			ItemStack s = getStackInSlot(1);
			if(!s.isEmpty() && getMaxPower() > 0 && mruStorage.getMRU() > mruUsage && getStackInSlot(2).isEmpty()) {
				if(s.isItemEnchantable() && getEnchantmentsForStack(s) != null && !getEnchantmentsForStack(s).isEmpty()) {
					return true;
				}
			}
			return false;
		}
		catch (Exception e) {
			return false;
		}
	}

	public static void setupConfig(Configuration cfg) {
		try {
			String category = "tileentities.magicalenchanter";
			cfgMaxMRU = cfg.get(category, "MaxMRU", ApiCore.DEVICE_MAX_MRU_GENERIC).setMinValue(1).getInt();
			mruUsage = cfg.get(category, "MRUUsage", 100).setMinValue(0).setMaxValue(cfgMaxMRU).getInt();
			maxEnchantmentLevel = cfg.get(category, "MaxEnchantLevel", 60).setMinValue(0).getInt();
			generatesCorruption = cfg.get(category, "GenerateCorruption", false).getBoolean();
			genCorruption = cfg.get(category, "MaxCorruptionGen", 2, "Max amount of corruption generated per tick").setMinValue(0).getInt();
		}
		catch(Exception e) {
			return;
		}
	}

	@Override
	public int[] getOutputSlots() {
		return new int[] {2};
	}

	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return new AxisAlignedBB(pos, pos.add(1, 2, 1));
	}
}
