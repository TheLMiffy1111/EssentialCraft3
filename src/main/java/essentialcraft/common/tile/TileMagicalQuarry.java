package essentialcraft.common.tile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.mojang.authlib.GameProfile;

import essentialcraft.api.ApiCore;
import essentialcraft.common.item.ItemGenericEC;
import essentialcraft.common.item.ItemsCore;
import essentialcraft.common.mod.EssentialCraftCore;
import essentialcraft.network.PacketNBT;
import essentialcraft.utils.common.ECUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

public class TileMagicalQuarry extends TileMRUGeneric {

	public int progressLevel;
	public int miningX;
	public int miningY;
	public int miningZ;
	public boolean flag;

	public static int cfgMaxMRU = ApiCore.DEVICE_MAX_MRU_GENERIC;
	public static boolean generatesCorruption = true;
	public static int genCorruption = 2;
	public static boolean ignoreLiquids = true;
	public static int mruUsage = 8;
	public static double efficencyPerUpgrade = 0.5D;
	public static double blockHardnessModifier = 9D;

	public static final List<Object> voidList = new ArrayList<Object>();

	static {
		voidList.add(Blocks.COBBLESTONE);
		voidList.add(Blocks.DIRT);
		voidList.add(Blocks.GRASS);
		voidList.add(Blocks.STONE);
		voidList.add(Blocks.SAND);
		voidList.add(Blocks.SANDSTONE);
		voidList.add(Blocks.TALLGRASS);
		voidList.add(Blocks.RED_FLOWER);
		voidList.add(Blocks.YELLOW_FLOWER);
		voidList.add(Blocks.BROWN_MUSHROOM);
		voidList.add(Blocks.RED_MUSHROOM);
		voidList.add(Blocks.LEAVES);
		voidList.add(Blocks.LEAVES2);
		voidList.add(Items.WHEAT_SEEDS);
	}

	public TileMagicalQuarry() {
		super(cfgMaxMRU);
		setSlotsNum(5);
	}

	public boolean canGenerateMRU() {
		return false;
	}

	@Override
	public void update() {
		if(syncTick == 10)
			syncTick = 0;
		super.update();
		mruStorage.update(getPos(), getWorld(), getStackInSlot(0));
		if(getWorld().isBlockIndirectlyGettingPowered(pos) == 0)
			mine();
		if(!getWorld().isRemote)
			collectItems();
	}

	@Override
	public void readFromNBT(NBTTagCompound i) {
		super.readFromNBT(i);
		progressLevel = i.getInteger("progressLevel");
		miningX = i.getInteger("miningX");
		miningY = i.getInteger("miningY");
		miningZ = i.getInteger("miningZ");
		flag = i.getBoolean("localFlag");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound i) {
		super.writeToNBT(i);
		i.setInteger("progressLevel", progressLevel);
		i.setInteger("miningX", miningX);
		i.setInteger("miningY", miningY);
		i.setInteger("miningZ", miningZ);
		i.setBoolean("localFlag", flag);
		return i;
	}

	public boolean hasInventoryUpgrade() {
		ItemStack s = ItemGenericEC.getStkByName("inventoryUpgrade");
		for(int i = 0; i < getSizeInventory(); ++i) {
			if(!getStackInSlot(i).isEmpty() && getStackInSlot(i).isItemEqual(s))
				return true;
		}
		return false;
	}

	public boolean hasSmeltingUpgrade() {
		ItemStack s = ItemGenericEC.getStkByName("blazingUpgrade");
		for(int i = 0; i < getSizeInventory(); ++i) {
			if(!getStackInSlot(i).isEmpty() && getStackInSlot(i).isItemEqual(s))
				return true;
		}
		return false;
	}

	public boolean hasVoidUpgrade() {
		ItemStack s = ItemGenericEC.getStkByName("voidUpgrade");
		for(int i = 0; i < getSizeInventory(); ++i) {
			if(!getStackInSlot(i).isEmpty() && getStackInSlot(i).isItemEqual(s))
				return true;
		}
		return false;
	}

	public boolean hasSilkyUpgrade() {
		if(hasSmeltingUpgrade())
			return false;

		ItemStack s = ItemGenericEC.getStkByName("silkyUpgrade");
		for(int i = 0; i < getSizeInventory(); ++i) {
			if(!getStackInSlot(i).isEmpty() && getStackInSlot(i).isItemEqual(s))
				return true;
		}
		return false;
	}

	public boolean hasFortuneUpgrade() {
		if(hasSilkyUpgrade())
			return false;

		ItemStack s = ItemGenericEC.getStkByName("fortuneUpgrade");
		for(int i = 0; i < getSizeInventory(); ++i) {
			if(!getStackInSlot(i).isEmpty() && getStackInSlot(i).isItemEqual(s))
				return true;
		}
		return false;
	}

	public boolean hasMiningUpgrade() {
		ItemStack s = ItemGenericEC.getStkByName("diamondUpgrade");
		for(int i = 0; i < getSizeInventory(); ++i) {
			if(!getStackInSlot(i).isEmpty() && getStackInSlot(i).isItemEqual(s))
				return true;
		}
		return false;
	}

	public IItemHandler getInventory() {
		TileEntity t = getWorld().getTileEntity(pos.up());
		if(t != null && t.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN)) {
			return t.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN);
		}
		return null;
	}

	public double getEfficency() {
		double f = efficencyPerUpgrade + 1;
		ItemStack s = ItemGenericEC.getStkByName("efficencyUpgrade");
		for(int i = 0; i < getSizeInventory(); ++i) {
			if(!getStackInSlot(i).isEmpty() && getStackInSlot(i).isItemEqual(s))
				f += getStackInSlot(i).getCount() * efficencyPerUpgrade;
		}
		return hasSmeltingUpgrade() ? f/2 : f;
	}

	public int getMiningRange() {
		int f = 5;
		ItemStack s = ItemGenericEC.getStkByName("diamondUpgrade");
		for(int i = 0; i < getSizeInventory(); ++i) {
			if(!getStackInSlot(i).isEmpty() && getStackInSlot(i).isItemEqual(s))
				f += getStackInSlot(i).getCount();
		}
		return f;
	}

	public boolean canMineBlock(Block b) {
		if(b == Blocks.BEDROCK)
			return false;
		if(b == Blocks.OBSIDIAN && !hasMiningUpgrade())
			return false;
		return true;
	}

	public boolean shouldInstaMine(Block b) {
		return b instanceof BlockLiquid && ignoreLiquids || b instanceof IFluidBlock && ignoreLiquids || b == null || b == Blocks.AIR;
	}

	public int determineFortune() {
		int fortune = 0;
		ItemStack s = ItemGenericEC.getStkByName("fortuneUpgrade");
		for(int i = 0; i < getSizeInventory(); ++i) {
			if(!getStackInSlot(i).isEmpty() && getStackInSlot(i).isItemEqual(s))
				fortune += getStackInSlot(i).getCount();
		}

		if(fortune <= 128)
			fortune = 5;
		if(fortune <= 64)
			fortune = 4;
		if(fortune <= 32)
			fortune = 3;
		if(fortune <= 16)
			fortune = 2;
		if(fortune <= 8)
			fortune = 1;

		return fortune;
	}

	public boolean mineBlock(Block b) {
		if(canMineBlock(b)) {
			NBTTagCompound currentMinedCoords = new NBTTagCompound();
			currentMinedCoords.setInteger("x", pos.getX());
			currentMinedCoords.setInteger("y", pos.getY());
			currentMinedCoords.setInteger("z", pos.getZ());
			currentMinedCoords.setInteger("mx", miningX);
			currentMinedCoords.setInteger("my", miningY);
			currentMinedCoords.setInteger("mz", miningZ);
			PacketNBT packet = new PacketNBT(currentMinedCoords).setID(4);
			EssentialCraftCore.network.sendToAllAround(packet, new TargetPoint(getWorld().provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 16+getMiningRange()));
			BlockPos mining = new BlockPos(miningX, miningY, miningZ);

			if(shouldInstaMine(b)) {
				getWorld().setBlockToAir(mining);
				return true;
			}
			else {
				double required = b.getBlockHardness(getWorld().getBlockState(mining),getWorld(), mining)*blockHardnessModifier;
				if(mruStorage.getMRU() >= (int)(mruUsage/4*getEfficency())) {
					mruStorage.extractMRU((int)(mruUsage/4*getEfficency()), true);
					progressLevel += getEfficency();
				}
				if(progressLevel >= required) {
					FakePlayer quarryFakePlayer = new FakePlayer((WorldServer)getWorld(), quarryFakePlayerProfile);

					progressLevel = 0;
					if(hasMiningUpgrade())
						quarryFakePlayer.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(ItemsCore.wind_elemental_pick));
					else
						quarryFakePlayer.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(ItemsCore.weak_elemental_pick));

					if(hasFortuneUpgrade())
						quarryFakePlayer.getHeldItemMainhand().addEnchantment(Enchantments.FORTUNE, determineFortune());
					if(hasSilkyUpgrade())
						quarryFakePlayer.getHeldItemMainhand().addEnchantment(Enchantments.SILK_TOUCH, 1);

					b.harvestBlock(getWorld(), quarryFakePlayer, mining, getWorld().getBlockState(mining), getWorld().getTileEntity(mining), quarryFakePlayer.getHeldItemMainhand());

					getWorld().setBlockToAir(mining);
					if(generatesCorruption)
						ECUtils.randomIncreaseCorruptionAt(getWorld(), pos, getWorld().rand, (genCorruption));

					quarryFakePlayer = null;
				}
			}
		}
		return false;
	}

	public boolean isMainColomnMined() {
		int r = 3;
		while(++r <= pos.getY()-2) {
			Block b = getWorld().getBlockState(pos.down(r)).getBlock();
			if(b != null && b.getBlockHardness(getWorld().getBlockState(pos.down(r)),getWorld(),pos.down(r)) >= 0 && b != Blocks.AIR && !(b instanceof BlockLiquid && ignoreLiquids) && !(b instanceof IFluidBlock && ignoreLiquids) && canMineBlock(b) && canMineBlock(b))
				return false;
		}
		return true;
	}

	public int genMiningColomnY(int current) {
		int r = 3;
		while(++r <= pos.getY()-2) {
			Block b = getWorld().getBlockState(pos.down(r)).getBlock();
			if(b != null && b.getBlockHardness(getWorld().getBlockState(pos.down(r)),getWorld(),pos.down(r)) >= 0 && b != Blocks.AIR && !(b instanceof BlockLiquid && ignoreLiquids) && !(b instanceof IFluidBlock && ignoreLiquids) && canMineBlock(b))
				return pos.getY()-r;
		}
		return current;
	}

	public boolean isRowMined() {
		int rad = getMiningRange();
		for(int x = -rad; x <= rad; ++x) {
			for(int z = -rad; z <= rad; ++z) {
				Block b = getWorld().getBlockState(new BlockPos(pos.getX()+x, miningY, pos.getZ()+z)).getBlock();
				if(b != null && b.getBlockHardness(getWorld().getBlockState(new BlockPos(pos.getX()+x, miningY, pos.getZ()+z)),getWorld(),new BlockPos(pos.getX()+x, miningY, pos.getZ()+z)) != -1 && b != Blocks.AIR && !(b instanceof BlockLiquid && ignoreLiquids) && !(b instanceof IFluidBlock && ignoreLiquids) && canMineBlock(b))
					return false;
			}
		}
		return true;
	}

	public boolean canWork() {
		return getEfficency() > 0 && mruStorage.getMRU()>=mruUsage;
	}

	public void mine() {
		if(canWork() && !getWorld().isRemote) {
			if(isMainColomnMined()) {
				if(!flag) {
					flag = true;
					miningY = pos.getY()-4;
				}
				flag = isMainColomnMined();

				if(isRowMined()) {
					--miningY;
				}
				else {
					int rad = getMiningRange();
					Fort:
						for(int x = -rad; x <= rad; ++x) {
							for(int z = -rad; z <= rad; ++z)
							{
								BlockPos cp = new BlockPos(pos.getX()+x, miningY, pos.getZ()+z);
								if(getWorld().isAreaLoaded(new StructureBoundingBox(pos.getX()+x-1, miningY-1, pos.getZ()+z-1, pos.getX()+x+1, miningY+1, pos.getZ()+z+1)) && getWorld().isBlockLoaded(cp) && getWorld().getBlockState(cp).getBlock() != null && getWorld().getBlockState(cp).getBlockHardness(getWorld(),cp) != -1 && getWorld().getBlockState(cp).getBlock() != Blocks.AIR && !(getWorld().getBlockState(cp).getBlock() instanceof BlockLiquid) && !(getWorld().getBlockState(cp).getBlock() instanceof IFluidBlock))
								{
									miningX = pos.getX()+x;
									miningZ = pos.getZ()+z;
									mineBlock(getWorld().getBlockState(new BlockPos(pos.getX()+x, miningY, pos.getZ()+z)).getBlock());
									break Fort;
								}
							}
						}
				}
			}
			else {
				flag = false;
				miningY = genMiningColomnY(miningY);
				miningX = pos.getX();
				miningZ = pos.getZ();
				BlockPos mining = new BlockPos(miningX, miningY, miningZ);
				if(getWorld().isAreaLoaded(new StructureBoundingBox(miningX-1, miningY-1, miningZ-1, miningX+1, miningY+1, miningZ+1)) && getWorld().isBlockLoaded(mining)){
					if(getWorld().getBlockState(mining).getBlock() != null && getWorld().getBlockState(mining) != Blocks.AIR && !(getWorld().getBlockState(mining).getBlock() instanceof BlockLiquid) && !(getWorld().getBlockState(mining).getBlock() instanceof IFluidBlock)){
						if(mineBlock(getWorld().getBlockState(mining).getBlock()))
							--miningY;
					}
					else
						--miningY;
				}
			}
		}
	}

	public void collectItems() {
		List<EntityItem> l = getWorld().getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(miningX, miningY, miningZ, miningX+1, miningY+1, miningZ+1).expand(4D, 2D, 4D));
		if(!l.isEmpty()) {
			for(int i = 0; i < l.size(); ++i) {
				EntityItem item = l.get(i);
				ItemStack s = item.getItem();
				if(hasSmeltingUpgrade() && s != null) {
					ItemStack forged = FurnaceRecipes.instance().getSmeltingResult(s);
					if(!forged.isEmpty()) {
						ItemStack copy = forged.copy();
						copy.setCount(s.getCount());
						if(hasFortuneUpgrade()) {
							int fortune = determineFortune();
							for(int i1 = 0; i1 < s.getCount(); ++i1) {
								copy.grow(getWorld().rand.nextInt(fortune));
							}
						}
						s = copy;
						item.setItem(copy);
					}
				}
				if(hasVoidUpgrade()) {
					if(voidItemStack(s)) {
						item.setPositionAndRotation(0, 0, 0, 0, 0);
						item.setDead();
						return;
					}
				}
				item.setPositionAndRotation(0, 0, 0, 0, 0);
				item.setDead();
				if(hasInventoryUpgrade())
					insertItem(s);
				else
					spitItem(s);
			}
		}
	}

	public boolean voidItemStack(ItemStack s) {
		return s.isEmpty() || voidList.contains(s.getItem() instanceof ItemBlock ? Block.getBlockFromItem(s.getItem()) : s.getItem());
	}

	public void spitItem(ItemStack s) {
		EntityItem item = new EntityItem(getWorld(), pos.getX(), pos.getY(), pos.getZ(), s);
		item.setPositionAndRotation(pos.getX()+0.5D, pos.getY()+2, pos.getZ()+0.5D, 0, 0);
		getWorld().spawnEntity(item);
	}

	public void insertItem(ItemStack stack) {
		if(stack.isEmpty())
			return;

		IItemHandler destInventory = getInventory();
		if(destInventory != null) {
			for(int slot = 0; slot < destInventory.getSlots() && !stack.isEmpty(); slot++) {
				ItemStack itemstack = destInventory.getStackInSlot(slot);

				if(destInventory.insertItem(slot, stack, true).isEmpty()) {
					boolean insertedItem = false;
					boolean inventoryWasEmpty = isEmpty(destInventory);

					if(itemstack.isEmpty()) {
						destInventory.insertItem(slot, stack, false);
						stack = ItemStack.EMPTY;
						insertedItem = true;
					}
					else if(ItemHandlerHelper.canItemStacksStack(itemstack, stack)) {
						int originalSize = stack.getCount();
						stack = destInventory.insertItem(slot, stack, false);
						insertedItem = originalSize < stack.getCount();
					}
				}
			}
		}

		spitItem(stack);
	}

	private static boolean isEmpty(IItemHandler itemHandler) {
		for (int slot = 0; slot < itemHandler.getSlots(); slot++) {
			ItemStack stackInSlot = itemHandler.getStackInSlot(slot);
			if(stackInSlot.getCount() > 0) {
				return false;
			}
		}
		return true;
	}

	public static void setupConfig(Configuration cfg) {
		try {
			String category = "tileentities.magicalquarry";
			cfgMaxMRU = cfg.get(category, "MaxMRU", ApiCore.DEVICE_MAX_MRU_GENERIC).setMinValue(1).getInt();
			mruUsage = cfg.get(category, "MRUUsage", 8).setMinValue(0).setMaxValue(cfgMaxMRU).getInt();
			ignoreLiquids = cfg.get(category, "IgnoreLiquids", true).getBoolean();
			generatesCorruption = cfg.get(category, "GenerateCorruption", true).getBoolean();
			genCorruption = cfg.get(category, "MaxCorruptionGen", 2, "Max amount of corruption generated per tick").setMinValue(0).getInt();
			efficencyPerUpgrade = cfg.get(category, "EfficiencyPerUpgrade", 0.5D).setMinValue(Double.MIN_NORMAL).getDouble();
			blockHardnessModifier = cfg.get(category, "BlockHardnessModifier", 9D).setMinValue(0D).getDouble();
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
	public AxisAlignedBB getRenderBoundingBox() {
		return INFINITE_EXTENT_AABB;
	}

	public static GameProfile quarryFakePlayerProfile = new GameProfile(UUID.fromString("5cd89d0b-e9ba-0000-89f4-badbb05963dd"), "[EC3]Quarry");
}
