package ec3.common.tile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.mojang.authlib.GameProfile;

import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import DummyCore.Utils.DataStorage;
import DummyCore.Utils.DummyData;
import DummyCore.Utils.MiscUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.util.FakePlayer;
import ec3.api.ApiCore;
import ec3.common.item.ItemGenericEC3;
import ec3.common.item.ItemsCore;
import ec3.common.mod.EssentialCraftCore;
import ec3.network.PacketNBT;
import ec3.utils.common.ECUtils;

public class TileMagicalQuarry extends TileMRUGeneric {
	
	public int progressLevel;
	public int miningX;
	public int miningY;
	public int miningZ;
	public boolean flag;
	
	public static float cfgMaxMRU = ApiCore.DEVICE_MAX_MRU_GENERIC;
	public static boolean generatesCorruption = true;
	public static int genCorruption = 2;
	public static boolean ignoreLiquids = true;
	public static float mruUsage = 8;
	public static float efficencyPerUpgrade = 0.5F;
	public static float blockHardnessModifier = 9F;
	
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
		super();
		maxMRU = (int)cfgMaxMRU;
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
		if(getWorld().isBlockIndirectlyGettingPowered(pos) == 0)
			mine();
		if(!getWorld().isRemote)
			collectItems();
		
		ECUtils.manage(this, 0);
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
		ItemStack s = ItemGenericEC3.getStkByName("inventoryUpgrade");
		for(int i = 0; i < getSizeInventory(); ++i) {
			if(getStackInSlot(i) != null && getStackInSlot(i).isItemEqual(s))
				return true;
		}
		return false;
	}
	
	public boolean hasSmeltingUpgrade() {
    	ItemStack s = ItemGenericEC3.getStkByName("blazingUpgrade");
    	for(int i = 0; i < getSizeInventory(); ++i) {
    		if(getStackInSlot(i) != null && getStackInSlot(i).isItemEqual(s))
    			return true;
    	}
    	return false;
	}
	
	public boolean hasVoidUpgrade() {
		ItemStack s = ItemGenericEC3.getStkByName("voidUpgrade");
		for(int i = 0; i < getSizeInventory(); ++i) {
			if(getStackInSlot(i) != null && getStackInSlot(i).isItemEqual(s))
				return true;
		}
		return false;
	}
	
	public boolean hasSilkyUpgrade() {
		if(hasSmeltingUpgrade())
			return false;
		
		ItemStack s = ItemGenericEC3.getStkByName("silkyUpgrade");
		for(int i = 0; i < getSizeInventory(); ++i) {
			if(getStackInSlot(i) != null && getStackInSlot(i).isItemEqual(s))
				return true;
		}
		return false;
	}
	
	public boolean hasFortuneUpgrade() {
		if(hasSilkyUpgrade())
			return false;
		
		ItemStack s = ItemGenericEC3.getStkByName("fortuneUpgrade");
		for(int i = 0; i < getSizeInventory(); ++i) {
			if(getStackInSlot(i) != null && getStackInSlot(i).isItemEqual(s))
				return true;
		}
		return false;
	}
    
	public boolean hasMiningUpgrade() {
		ItemStack s = ItemGenericEC3.getStkByName("diamondUpgrade");
		for(int i = 0; i < getSizeInventory(); ++i) {
			if(getStackInSlot(i) != null && getStackInSlot(i).isItemEqual(s))
				return true;
		}
		return false;
	}
    
	public boolean hasInventory() {
		TileEntity t = getWorld().getTileEntity(pos.up());
		if(t != null && t instanceof IInventory)
			return hasSpaceInInv((IInventory)t);
		return false;
	}
    
	public IInventory getInventory() {
		TileEntity t = getWorld().getTileEntity(pos.up());
		if(t != null && t instanceof IInventory)
			return (IInventory)t;
		return null;
	}
	
	public boolean hasSpaceInInv(IInventory inv) {
		for(int i = 0; i < inv.getSizeInventory(); ++i) {
			if(inv.getStackInSlot(i) == null)
				return true;
		}
		return false;
	}
	
	public float getEfficency() {
		float f = efficencyPerUpgrade + 1;
		ItemStack s = ItemGenericEC3.getStkByName("efficencyUpgrade");
		for(int i = 0; i < getSizeInventory(); ++i) {
			if(getStackInSlot(i) != null && getStackInSlot(i).isItemEqual(s))
				f += (float)getStackInSlot(i).stackSize * efficencyPerUpgrade;
		}
		return hasSmeltingUpgrade() ? f/2 : f;
	}
	
	public int getMiningRange() {
		int f = 5;
		ItemStack s = ItemGenericEC3.getStkByName("diamondUpgrade");
		for(int i = 0; i < getSizeInventory(); ++i) {
			if(getStackInSlot(i) != null && getStackInSlot(i).isItemEqual(s))
				f += getStackInSlot(i).stackSize;
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
		return (b instanceof BlockLiquid && ignoreLiquids) || b == null || b == Blocks.AIR;
	}
	
	public int determineFortune() {
		int fortune = 0;
		ItemStack s = ItemGenericEC3.getStkByName("fortuneUpgrade");
		for(int i = 0; i < getSizeInventory(); ++i) {
			if(getStackInSlot(i) != null && getStackInSlot(i).isItemEqual(s))
				fortune += getStackInSlot(i).stackSize;
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
				float required = b.getBlockHardness(getWorld().getBlockState(mining),getWorld(), mining)*blockHardnessModifier;
				if(setMRU((int)(getMRU() - (mruUsage/4*getEfficency()))))
					progressLevel += getEfficency();
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
						ECUtils.increaseCorruptionAt(getWorld(), pos.getX(), pos.getY(), pos.getZ(), getWorld().rand.nextInt(genCorruption));
					
					quarryFakePlayer = null;
				}
			}
		}
		return false;
	}
	
	public boolean isMainColomnMined() {
		int r = pos.getY()-2;
		while(--r >= 0) {
			Block b = getWorld().getBlockState(pos.down(r)).getBlock();
			if((b != null && b.getBlockHardness(getWorld().getBlockState(pos.down(r)),getWorld(),pos.down(r)) != -1 && b != Blocks.AIR && !(b instanceof BlockLiquid && ignoreLiquids) && canMineBlock(b)) && canMineBlock(b))
				return false;
		}
		return true;
	}
	
	public int genMiningColomnY(int current)  {
		int r = pos.getY()-2;
		while(--r >= 0) {
			Block b = getWorld().getBlockState(pos.down(r)).getBlock();
			if((b != null && b.getBlockHardness(getWorld().getBlockState(pos.down(r)),getWorld(),pos.down(r)) != -1 && b != Blocks.AIR && !(b instanceof BlockLiquid && ignoreLiquids) && canMineBlock(b)) && canMineBlock(b))
				return r;
		}
		return current;
	}
	
	public boolean isRowMined() {
		int rad = getMiningRange();
		for(int x = -rad; x <= rad; ++x) {
			for(int z = -rad; z <= rad; ++z) {
				Block b = getWorld().getBlockState(new BlockPos(pos.getX()+x, miningY, pos.getZ()+z)).getBlock();
				if(b != null && b.getBlockHardness(getWorld().getBlockState(new BlockPos(pos.getX()+x, miningY, pos.getZ()+z)),getWorld(),new BlockPos(pos.getX()+x, miningY, pos.getZ()+z)) != -1 && b != Blocks.AIR && !(b instanceof BlockLiquid && ignoreLiquids) && canMineBlock(b))
					return false;
			}
		}
		return true;
	}
	
	public boolean canWork() {
		return getEfficency() > 0 && getMRU()>mruUsage;
	}
	
	public void mine() {
		if(canWork() && !getWorld().isRemote) {
			if(isMainColomnMined()) {
				
				if(!flag) {
					flag = true;
					miningY = pos.getY()-3;
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
								if(getWorld().isAreaLoaded(new StructureBoundingBox(pos.getX()+x-1, miningY-1, pos.getZ()+z-1, pos.getX()+x+1, miningY+1, pos.getZ()+z+1)) && getWorld().isBlockLoaded(cp) && getWorld().getBlockState(cp).getBlock() != null && getWorld().getBlockState(cp).getBlockHardness(getWorld(),cp) != -1 && getWorld().getBlockState(cp).getBlock() != Blocks.AIR && !(getWorld().getBlockState(cp).getBlock() instanceof BlockLiquid))
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
					if(getWorld().getBlockState(mining).getBlock() != null && getWorld().getBlockState(mining) != Blocks.AIR && !(getWorld().getBlockState(mining).getBlock() instanceof BlockLiquid)){
						if(mineBlock(getWorld().getBlockState(mining).getBlock()))
							--miningY;
					}
					else
						--miningY;
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public void collectItems() {
		List<EntityItem> l = getWorld().getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(miningX, miningY, miningZ, miningX+1, miningY+1, miningZ+1).expand(4D, 2D, 4D));
		if(!l.isEmpty()) {
			for(int i = 0; i < l.size(); ++i) {
				EntityItem item = l.get(i);
				ItemStack s = item.getEntityItem();
				if(hasSmeltingUpgrade() && s != null) {
					ItemStack forged = FurnaceRecipes.instance().getSmeltingResult(s);
					if(forged != null) {
						ItemStack copy = forged.copy();
						copy.stackSize *= s.stackSize;
						if(hasFortuneUpgrade()) {
							int fortune = determineFortune();
							for(int i1 = 0; i1 < s.stackSize; ++i1) {
								copy.stackSize += getWorld().rand.nextInt(fortune);
							}
						}
						s = copy;
						item.setEntityItemStack(copy);
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
				if(hasInventoryUpgrade() && hasInventory())
					insertItem(s);
				else
					splitItem(s);
			}
		}
	}
	
	public boolean voidItemStack(ItemStack s) {
		return s == null || voidList.contains(s.getItem() instanceof ItemBlock ? Block.getBlockFromItem(s.getItem()) : s.getItem());
	}
	
	public void splitItem(ItemStack s) {
		EntityItem item = new EntityItem(getWorld(), pos.getX(), pos.getY(), pos.getZ(), s);
		item.setPositionAndRotation(pos.getX()+0.5D, pos.getY()+2, pos.getZ()+0.5D, 0, 0);
		getWorld().spawnEntity(item);
	}
	
	public void insertItem(ItemStack s) {
		if(s == null)
			return;
		
		if(hasSpaceInInv(getInventory())) {
			for(int i = 0; i < getInventory().getSizeInventory(); ++i) {
				ItemStack stk = getInventory().getStackInSlot(i);
				if(stk != null && stk.isItemEqual(s) && stk.stackSize + s.stackSize <= stk.getMaxStackSize()) {
					stk.stackSize += s.stackSize;
					return;
				}
			}
			for(int i = 0; i < getInventory().getSizeInventory(); ++i) {
				ItemStack stk = getInventory().getStackInSlot(i);
				if(stk == null) {
					getInventory().setInventorySlotContents(i, s);
					return;
				}
			}
			splitItem(s);
		}
		else
			splitItem(s);
	}
	
	public static void setupConfig(Configuration cfg) {
		try {
			cfg.load();
			String[] cfgArrayString = cfg.getStringList("MagicalQuarrySettings", "tileentities", new String[] {
					"Max MRU:" + ApiCore.DEVICE_MAX_MRU_GENERIC,
					"MRU Usage:8",
					"Should not mine liquids:true",
					"Can this device actually generate corruption:true",
					"The amount of corruption generated each tick(do not set to 0!):2",
					"Efficency added to the device per upgrade:0.5",
					"Block hardness modifier:9.0"
			}, "");
			String dataString = "";
			
			for(int i = 0; i < cfgArrayString.length; ++i)
				dataString += "||" + cfgArrayString[i];
			
			DummyData[] data = DataStorage.parseData(dataString);
			
			cfgMaxMRU = Float.parseFloat(data[0].fieldValue);
			mruUsage = Integer.parseInt(data[1].fieldValue);
			ignoreLiquids = Boolean.parseBoolean(data[2].fieldValue);
			generatesCorruption = Boolean.parseBoolean(data[3].fieldValue);
			genCorruption = Integer.parseInt(data[4].fieldValue);
			efficencyPerUpgrade = Float.parseFloat(data[5].fieldValue);
			blockHardnessModifier = Float.parseFloat(data[6].fieldValue);
			
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
	
	public static GameProfile quarryFakePlayerProfile = new GameProfile(UUID.fromString("5cd89d0b-e9ba-0000-89f4-badbb05963dd"), "[EC3]Quarry");
}
