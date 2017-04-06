package ec3.utils.common;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import com.mojang.authlib.GameProfile;

import DummyCore.Utils.Coord3D;
import DummyCore.Utils.DummyData;
import DummyCore.Utils.DummyDistance;
import DummyCore.Utils.DummyPacketHandler;
import DummyCore.Utils.DummyPacketIMSG;
import DummyCore.Utils.MathUtils;
import DummyCore.Utils.MiscUtils;
import DummyCore.Utils.Notifier;
import DummyCore.Utils.ScheduledServerAction;
import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import ec3.api.ApiCore;
import ec3.api.EnumStructureType;
import ec3.api.IItemAllowsSeeingMRUCU;
import ec3.api.IMRUPressence;
import ec3.api.IMRUStorage;
import ec3.api.ISpell;
import ec3.api.ITEHasMRU;
import ec3.api.IWorldEvent;
import ec3.api.MagicianTableRecipe;
import ec3.api.MagicianTableRecipes;
import ec3.api.RadiatingChamberRecipe;
import ec3.api.RadiatingChamberRecipes;
import ec3.api.ShapedFurnaceRecipe;
import ec3.api.WorldEventLibrary;
import ec3.common.entity.EntityMRUPresence;
import ec3.common.inventory.InventoryMagicFilter;
import ec3.common.item.ItemBaublesWearable;
import ec3.common.item.ItemBoundGem;
import ec3.common.item.ItemFilter;
import ec3.common.mod.EssentialCraftCore;
import ec3.common.registry.PotionRegistry;
import ec3.common.tile.TileMRUChunkLoader;
import ec3.common.tile.TileMRUReactor;
import ec3.common.tile.TileRayTower;
import ec3.network.PacketNBT;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager.LoadingCallback;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class ECUtils {
	public static Hashtable<EnumStructureType,List<Block>> allowedBlocks = new Hashtable<EnumStructureType, List<Block>>(); 
	public static Hashtable<String, Float> mruResistance = new Hashtable<String, Float>();
	public static Hashtable<String, Boolean> ignoreMeta = new Hashtable<String, Boolean>();
	public static List<SpellEntry> spells = new ArrayList<SpellEntry>();
	public static Hashtable<UUID,PlayerGenericData> playerDataTable = new Hashtable<UUID, PlayerGenericData>();
	private static final List<ScheduledServerAction> actions = new ArrayList<ScheduledServerAction>();
	public static NBTTagCompound ec3WorldTag = new NBTTagCompound();
	public static TicketHandler handler;

	public static TicketHandler getTHObj() {
		if(handler == null)
			handler = new TicketHandler();
		return handler;
	}

	public static class TicketHandler implements LoadingCallback {
		//Might move to DummyCore and create a better chunkloading system
		@Override
		public void ticketsLoaded(List<Ticket> tickets, World world) {
			for(Ticket ticket : tickets) {
				int x = ticket.getModData().getInteger("xCoord");
				int y = ticket.getModData().getInteger("yCoord");
				int z = ticket.getModData().getInteger("zCoord");
				
				TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
				
				if(tileEntity instanceof TileMRUChunkLoader) {
					((TileMRUChunkLoader)tileEntity).forceChunks(ticket);
				}
			}
		}
	}

	public static void requestSync(EntityPlayer e)
	{
		NBTTagCompound tag = new NBTTagCompound();
		getData(e).writeToNBTTagCompound(tag);
		PacketNBT pkt = new PacketNBT(tag).setID(0);
		EssentialCraftCore.network.sendTo(pkt, (EntityPlayerMP)e);
	}

	public static PlayerGenericData getData(EntityPlayer e)
	{
		return playerDataExists(e) ? playerDataTable.get(MiscUtils.getUUIDFromPlayer(e)) : createPlayerData(e);
	}

	/**
	 * Should only be used for CLIENT, but may be used for the SERVER
	 */
	public static PlayerGenericData getData(UUID uuid)
	{
		return playerDataExists(uuid) ? playerDataTable.get(uuid) : null;
	}

	/**
	 * Should only be used for CLIENT, but may be used for the SERVER
	 */
	public static boolean playerDataExists(UUID uuid)
	{
		return playerDataTable.containsKey(uuid);
	}

	public static boolean playerDataExists(EntityPlayer e)
	{
		return playerDataTable.containsKey(MiscUtils.getUUIDFromPlayer(e));
	}

	public static boolean playerDataExists(String username) {
		try {
			return playerDataTable.containsKey(UUID.fromString(username));
		}
		catch(IllegalArgumentException e) {
			return false;
		}
	}

	public static PlayerGenericData createPlayerData(EntityPlayer e)
	{
		if(e != null) {
			PlayerGenericData dat = new PlayerGenericData(e);
			playerDataTable.put(MiscUtils.getUUIDFromPlayer(e), dat);
			return dat;
		}
		else
			return null;
	}

	public static void changePlayerPositionOnClient(EntityPlayer e)
	{
		if(!e.getEntityWorld().isRemote)
		{
			DummyData aaa = new DummyData("x",e.posX);
			DummyData aab = new DummyData("y",e.posY);
			DummyData aac = new DummyData("z",e.posZ);
			DummyData aad = new DummyData("yaw",e.rotationYaw);
			DummyData aae = new DummyData("pitch",e.rotationPitch);
			DummyPacketIMSG pkt = new DummyPacketIMSG("||mod:ec3.player.position"+aaa+""+aab+""+aac+""+aad+""+aae);
			DummyPacketHandler.sendToPlayer(pkt, (EntityPlayerMP) e);
		}
	}

	public static void playSoundToAllNearby(double x, double y, double z, String sound, float volume, float pitch, double radius, int dim)
	{
		DummyData aaa = new DummyData("x",x);
		DummyData aab = new DummyData("y",y);
		DummyData aac = new DummyData("z",z);
		DummyData aad = new DummyData("vol",volume);
		DummyData aae = new DummyData("pitch",pitch);
		DummyData aaf = new DummyData("sound",sound);
		DummyPacketIMSG pkt = new DummyPacketIMSG("||mod:ec3.sound"+aaa+""+aab+""+aac+""+aad+""+aae+""+aaf);
		DummyPacketHandler.sendToAllAround(pkt, new TargetPoint(dim, x, y, z, radius));
	}

	public static void readOrCreatePlayerData(EntityPlayer e, NBTTagCompound tag)
	{
		if(e != null && tag != null) {
			if(!playerDataTable.containsKey(e.getName()))
			{
				PlayerGenericData dat = new PlayerGenericData(e);
				playerDataTable.put(MiscUtils.getUUIDFromPlayer(e), dat);
			}
			playerDataTable.get(MiscUtils.getUUIDFromPlayer(e)).readFromNBTTagCompound(tag);
		}
	}

	public static void createNBTTag(ItemStack stack)
	{

		if(stack.hasTagCompound())
		{
			return;
		}
		NBTTagCompound itemTag = new NBTTagCompound();
		stack.setTagCompound(itemTag);
	}

	public static NBTTagCompound getStackTag(ItemStack stack)
	{
		createNBTTag(stack);
		return stack.getTagCompound();
	}

	public static NBTTagCompound getOrCreateNbtData(ItemStack itemStack)
	{
		NBTTagCompound ret = itemStack.getTagCompound();
		if(ret == null)
		{
			ret = new NBTTagCompound();
			itemStack.setTagCompound(ret);
		}
		return ret;
	}

	public static void initMRUTag(ItemStack stack, int maxMRU)
	{
		if(maxMRU == 0)
		{
			try
			{
				Class<? extends Item> itemClz = stack.getItem().getClass();
				Field f = itemClz.getField("maxMRU");
				f.setInt(stack.getItem(), 5000);
				maxMRU = 5000;
			}catch(Exception e)
			{
				//Silent error tracking
			}
		}
		getStackTag(stack).setInteger("maxMRU", maxMRU);
		if(!getStackTag(stack).hasKey("mru"))
			getStackTag(stack).setInteger("mru", 0);
	}

	public static void registerBlockResistance(Block blk, int meta, float resistance)
	{
		DummyData dt = new DummyData(blk.getUnlocalizedName(),meta);
		ignoreMeta.put(blk.getUnlocalizedName(), meta == -1);
		mruResistance.put(dt.toString(), resistance);
	}

	public static boolean canPlayerSeeMRU(EntityPlayer player)
	{
		ItemStack currentItem = player.getHeldItemMainhand();
		ItemStack offhandItem = player.getHeldItemOffhand();

		ItemStack headwear = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);

		return (currentItem != null && ApiCore.allowsSeeingMRU.contains(currentItem.getItem())) ||
				(headwear != null && ApiCore.allowsSeeingMRU.contains(headwear.getItem())) ||
				(offhandItem != null && ApiCore.allowsSeeingMRU.contains(offhandItem.getItem())) ||
				(currentItem != null && currentItem.getItem() instanceof IItemAllowsSeeingMRUCU) ||
				(headwear != null && headwear.getItem() instanceof IItemAllowsSeeingMRUCU) ||
				(offhandItem != null && offhandItem.getItem() instanceof IItemAllowsSeeingMRUCU);
	}

	public static boolean canSpellWork(ItemStack spell, ISpell spell_2, int ubmru, int attune,EntityPlayer player)
	{
		NBTTagCompound tag = MiscUtils.getStackTag(spell);
		if(tag.hasKey("cooldown"))
		{
			if(tag.getInteger("cooldown") <= 0)
			{
				if(ubmru >= spell_2.getUBMRURequired(spell))
				{
					if(spell_2.getAttunementRequired(spell) == -1 || spell_2.getAttunementRequired(spell) == attune)
					{
						return true;
					}
				}
			}
		}else
		{
			if(ubmru >= spell_2.getUBMRURequired(spell))
			{
				if(spell_2.getAttunementRequired(spell) == -1 || spell_2.getAttunementRequired(spell) == attune)
				{
					return true;
				}
			}
		}
		return false;
	}

	public static boolean tryToDecreaseMRUInStorage(EntityPlayer player, int amount)
	{
		if(player.capabilities.isCreativeMode)
		{
			return true;
		}
		IBaublesItemHandler _gen_var_0_0 = BaublesApi.getBaublesHandler(player);
		for(int _gen_int_0 = 0; _gen_int_0 < _gen_var_0_0.getSlots(); ++_gen_int_0)
		{
			ItemStack _gen_var_1 = _gen_var_0_0.getStackInSlot(_gen_int_0);
			if(_gen_var_1 != null)
			{
				if(_gen_var_1.getItem() instanceof IMRUStorage)
				{
					if(((IMRUStorage)(_gen_var_1.getItem())).setMRU(_gen_var_1, amount))
					{
						return true;
					}
				}
			}
		}
		InventoryPlayer _gen_var_0 = player.inventory;
		for(int _gen_int_0 = 0; _gen_int_0 < _gen_var_0.mainInventory.length; ++_gen_int_0)
		{
			ItemStack _gen_var_1 = _gen_var_0.mainInventory[_gen_int_0];
			if(_gen_var_1 != null)
			{
				if(_gen_var_1.getItem() instanceof IMRUStorage)
				{
					if(((IMRUStorage)(_gen_var_1.getItem())).setMRU(_gen_var_1, amount))
					{
						return true;
					}
				}
			}
		}

		return false;

	}


	public static void saveMRUState(ITEHasMRU tile, NBTTagCompound saveTag)
	{
		saveTag.setFloat("mru", tile.getMRU());
		saveTag.setFloat("maxMRU", tile.getMaxMRU());
		saveTag.setFloat("balance", tile.getBalance());
		saveTag.setString("uuid", tile.getUUID().toString());
	}

	public static void loadMRUState(ITEHasMRU tile, NBTTagCompound loadTag)
	{
		tile.setBalance(loadTag.getFloat("balance"));
		tile.setMRU((int) loadTag.getFloat("mru"));
		tile.setMaxMRU(loadTag.getFloat("maxMRU"));
	}

	public static void createMRUCUAt(World w, Coord3D c, int MRU, float balance, boolean flag, boolean canAlwaysStay)
	{
		EntityMRUPresence mru = new EntityMRUPresence(w);
		mru.setPositionAndRotation(c.x, c.y, c.z, 0, 0);
		if(!w.isRemote)
		{
			mru.setMRU(MRU);
			mru.setBalance(balance);
			mru.setFlag(flag);
			mru.setAlwaysStay(canAlwaysStay);
			w.spawnEntity(mru);
		}
	}

	public static void mruIn(TileEntity tile, int slotNum) {
		if(tile instanceof IInventory && tile instanceof ITEHasMRU && !tile.getWorld().isRemote) {
			IInventory inv = (IInventory) tile;
			ITEHasMRU mrut = (ITEHasMRU) tile;

			if(inv.getStackInSlot(slotNum) != null && inv.getStackInSlot(slotNum).getItem() instanceof ItemBoundGem && inv.getStackInSlot(slotNum).getTagCompound() != null) {
				ItemStack s = inv.getStackInSlot(slotNum);
				int[] o = ItemBoundGem.getCoords(s);
				if(MathUtils.getDifference(tile.getPos().getX(), o[0]) <= 16 && MathUtils.getDifference(tile.getPos().getY(), o[1]) <= 16 && MathUtils.getDifference(tile.getPos().getZ(), o[2]) <= 16) {
					BlockPos o1 = new BlockPos(o[0],o[1],o[2]);
					if(tile.getWorld().getTileEntity(o1) != null && tile.getWorld().getTileEntity(o1) instanceof ITEHasMRU) {
						ITEHasMRU t = (ITEHasMRU) tile.getWorld().getTileEntity(o1);
						if(t != tile && t != null) {
							if(mrut.getMRU() < mrut.getMaxMRU()) {
								int mru = t.getMRU();
								if(mru > mrut.getMaxMRU() - mrut.getMRU()) {
									if(t.setMRU(mru-(mrut.getMaxMRU() - mrut.getMRU()))) {
										mrut.setMRU(mrut.getMaxMRU());
									}
								}
								else {
									if(t.setMRU(0)) {
										mrut.setMRU(mrut.getMRU()+mru);
									}
								}
								mrut.setBalance((mrut.getBalance()+t.getBalance())/2);
							}
						}
					}
				}
			}
			if(mrut.getMRU() < 0)
				mrut.setMRU(0);
		}
	}

	@SuppressWarnings("unchecked")
	public static boolean increaseCorruptionAt(World w, float x, float y, float z, int amount)
	{
		Coord3D c = new Coord3D(x,y,z);
		try {
			EntityMRUPresence mru = (EntityMRUPresence)getClosestMRUCU(w, c, 32);
			if(mru != null) {
				if(!w.isRemote) {
					mru.setMRU(mru.getMRU()+amount);
				}
			}
			else {
				createMRUCUAt(w, c, amount, 1.0F+MathUtils.randomFloat(w.rand), true, false);
			}
			List<EntityPlayer> players = w.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(x, y, z, x+1, y+1, z+1).expand(6D, 3D, 6D));
			for(int i = 0; i < players.size(); ++i) {
				if(!w.isRemote)
					calculateAndAddMRUCorruptionPE(players.get(i));
			}
		}
		catch(Exception e) {
			return false;
		}
		return false;
	}

	public static void calculateAndAddMRUCorruptionPE(EntityPlayer player)
	{
		boolean hasEffect = player.getActivePotionEffect(PotionRegistry.mruCorruptionPotion) != null;
		if(hasEffect)
		{
			int currentDuration = player.getActivePotionEffect(PotionRegistry.mruCorruptionPotion).getDuration();
			int newDuration = currentDuration+2;
			int newModifier = currentDuration/2000;
			player.removePotionEffect(PotionRegistry.mruCorruptionPotion);
			player.addPotionEffect(new PotionEffect(PotionRegistry.mruCorruptionPotion,newDuration,newModifier));
		}else
		{
			player.addPotionEffect(new PotionEffect(PotionRegistry.mruCorruptionPotion,200,0));
		}
	}

	public static void calculateAndAddPE(EntityPlayer player, Potion potion, int index, int index2)
	{
		boolean hasEffect = player.getActivePotionEffect(potion) != null;
		if(hasEffect)
		{
			int currentDuration = player.getActivePotionEffect(potion).getDuration();
			int newDuration = currentDuration+index2;
			int newModifier = currentDuration/index;
			player.removePotionEffect(potion);
			player.addPotionEffect(new PotionEffect(potion,newDuration,newModifier));
		}else
		{
			player.addPotionEffect(new PotionEffect(potion,index2,0));
		}
	}

	@SuppressWarnings("unchecked")
	public static IMRUPressence getClosestMRUCU(World w, DummyCore.Utils.Coord3D c, int radius)
	{
		List<Entity> l = w.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(c.x, c.y, c.z, c.x+1, c.y+1, c.z+1).expand(radius, radius/2, radius));
		IMRUPressence ret = null;
		if(!l.isEmpty())
		{
			List<IMRUPressence> actualList = new ArrayList<IMRUPressence>();
			for(Entity e : l) {
				if(e instanceof IMRUPressence) {
					actualList.add((IMRUPressence)e);
				}
			}
			double currentDistance = 0;
			double dominatingDistance = 0;
			int dominatingIndex = 0;
			DummyCore.Utils.Coord3D main = new DummyCore.Utils.Coord3D(c.x,c.y,c.z);
			for(int i = 0; i < actualList.size(); ++i)
			{
				if(actualList.get(i) instanceof EntityMRUPresence)
				{
					EntityMRUPresence pressence = EntityMRUPresence.class.cast(actualList.get(i));
					DummyCore.Utils.Coord3D current = new DummyCore.Utils.Coord3D(pressence.posX,pressence.posY,pressence.posZ);
					DummyDistance dist = new DummyDistance(main,current);
					if(i == 0)
					{
						dominatingIndex = i;
						dominatingDistance = dist.getDistance();
					}else
					{
						currentDistance = dist.getDistance();
						if(currentDistance < dominatingDistance)
						{
							dominatingIndex = i;
							dominatingDistance = dist.getDistance();
						}
					}
				}
			}
			try {
				ret = actualList.get(dominatingIndex);
			}
			catch(IndexOutOfBoundsException e) {}
		}
		return ret;
	}

	public static void spawnMRUParticles(TileEntity tile, int slotNum) {
		IInventory inv = (IInventory) tile;
		if(tile.getWorld().isRemote) {
			if(inv.getStackInSlot(slotNum) != null && inv.getStackInSlot(slotNum).getItem() instanceof ItemBoundGem && inv.getStackInSlot(slotNum).getTagCompound() != null) {
				ItemStack s = inv.getStackInSlot(slotNum);
				int[] o = ItemBoundGem.getCoords(s);
				if(MathUtils.getDifference(tile.getPos().getX(), o[0]) <= 16 && MathUtils.getDifference(tile.getPos().getY(), o[1]) <= 16 && MathUtils.getDifference(tile.getPos().getZ(), o[2]) <= 16) {
					BlockPos o1 = new BlockPos(o[0],o[1],o[2]);
					if(tile.getWorld().getTileEntity(o1) != null && tile.getWorld().getTileEntity(o1) instanceof ITEHasMRU) {
						float balance = ((ITEHasMRU)tile.getWorld().getTileEntity(o1)).getBalance();
						float colorRRender = 0.0F;
						float colorGRender = 1.0F;
						float colorBRender = 1.0F;

						float colorRNormal = 0.0F;
						float colorGNormal = 1.0F;
						float colorBNormal = 1.0F;

						float colorRChaos = 1.0F;
						float colorGChaos = 0.0F;
						float colorBChaos = 0.0F;

						float colorRFrozen = 0.0F;
						float colorGFrozen = 0.0F;
						float colorBFrozen = 1.0F;
						if(balance!=1.0F) {
							if(balance<1.0F) {
								float diff = balance;
								if(diff < 0.01F)
									diff = 0.0F;
								colorRRender = (colorRNormal*diff) + (colorRFrozen*(1.0F-diff));
								colorGRender = (colorGNormal*diff) + (colorGFrozen*(1.0F-diff));
								colorBRender = (colorBNormal*diff) + (colorBFrozen*(1.0F-diff));
							}
							if(balance>1.0F) {
								float diff = 2.0F-balance;
								if(diff < 0.01F)
									diff = 0.0F;
								colorRRender = (colorRNormal*diff) + (colorRChaos*(1.0F-diff));
								colorGRender = (colorGNormal*diff) + (colorGChaos*(1.0F-diff));
								colorBRender = (colorBNormal*diff) + (colorBChaos*(1.0F-diff));
							}
						}
						if(tile instanceof TileRayTower) {
							if(tile.getWorld().getTileEntity(o1) instanceof TileRayTower)
								EssentialCraftCore.proxy.MRUFX((float) (o[0]+0.5D), (float) (o[1]+1.85D), (float) (o[2]+0.5D), tile.getPos().getX()-o[0], tile.getPos().getY()-o[1]+0.25D, tile.getPos().getZ()-o[2],colorRRender,colorGRender,colorBRender);
							else if(tile.getWorld().getTileEntity(o1) instanceof TileMRUReactor)
								EssentialCraftCore.proxy.MRUFX((float) (o[0]+0.5D), (float) (o[1]+1.1D), (float) (o[2]+0.5D), tile.getPos().getX()-o[0], tile.getPos().getY()-o[1]+0.8D, tile.getPos().getZ()-o[2],colorRRender,colorGRender,colorBRender);
							else
								EssentialCraftCore.proxy.MRUFX((float) (o[0]+0.5D), (float) (o[1]+0.5D), (float) (o[2]+0.5D), tile.getPos().getX()-o[0], tile.getPos().getY()-o[1]+1.5D, tile.getPos().getZ()-o[2],colorRRender,colorGRender,colorBRender);
						}
						else {
							if(tile.getWorld().getTileEntity(o1) instanceof TileRayTower)
								EssentialCraftCore.proxy.MRUFX((float) (o[0]+0.5D), (float) (o[1]+1.85D), (float) (o[2]+0.5D), tile.getPos().getX()-o[0], tile.getPos().getY()-o[1]-1.5D, tile.getPos().getZ()-o[2],colorRRender,colorGRender,colorBRender);
							else if(tile.getWorld().getTileEntity(o1) instanceof TileMRUReactor)
								EssentialCraftCore.proxy.MRUFX((float) (o[0]+0.5D), (float) (o[1]+1.1D), (float) (o[2]+0.5D), tile.getPos().getX()-o[0], tile.getPos().getY()-o[1]-0.6D, tile.getPos().getZ()-o[2],colorRRender,colorGRender,colorBRender);
							else
								EssentialCraftCore.proxy.MRUFX((float) (o[0]+0.5D), (float) (o[1]+0.5D), (float) (o[2]+0.5D), tile.getPos().getX()-o[0], tile.getPos().getY()-o[1], tile.getPos().getZ()-o[2],colorRRender,colorGRender,colorBRender);
						}
					}
				}
			}
		}
	}

	public static float getGenResistance(int index, EntityPlayer p)
	{
		float resistance = 0F;
		for(int i = 0; i < p.inventory.armorInventory.length; ++i)
		{
			ItemStack armorStk = p.inventory.armorInventory[i];
			if(armorStk != null)
			{
				Item itm = armorStk.getItem();
				if(ApiCore.reductionsTable.containsKey(itm))
				{
					ArrayList<Float> lst = ApiCore.reductionsTable.get(itm);
					resistance += lst.get(index);
				}
			}
		}
		resistance /= 4;
		IBaublesItemHandler baublesInventory = BaublesApi.getBaublesHandler(p);
		if(baublesInventory != null)
		{
			for(int i = 0; i < baublesInventory.getSlots(); ++i)
			{
				ItemStack stk = baublesInventory.getStackInSlot(i);
				if(stk != null && stk.getItem() instanceof ItemBaublesWearable && MiscUtils.getStackTag(stk).hasKey("type"))
				{
					NBTTagCompound bTag = MiscUtils.getStackTag(stk);
					ArrayList<Float> fltLst = new ArrayList<Float>();
					fltLst.add(bTag.getFloat("mrucr"));
					fltLst.add(bTag.getFloat("mrurr"));
					fltLst.add(bTag.getFloat("car"));
					resistance += fltLst.get(index);
				}
			}
		}

		float retFlt = 1.0F - resistance;
		if(retFlt < 0)retFlt = 0;
		return retFlt;
	}

	public static void newWorldEvent(World w)
	{
		if(WorldEventLibrary.currentEvent == null)
		{
			IWorldEvent event = WorldEventLibrary.selectRandomEffect(w);
			if(event != null && WorldEventLibrary.currentEvent == null)
			{
				WorldEventLibrary.currentEvent = event;
				WorldEventLibrary.currentEventDuration = event.getEventDuration(w);
				event.onEventBeginning(w);
			}
		}
	}

	public static void endEvent(World w)
	{
		if(WorldEventLibrary.currentEvent != null)
		{
			if(WorldEventLibrary.currentEventDuration-20 <= 0)
			{
				WorldEventLibrary.currentEvent.onEventEnd(w);
				WorldEventLibrary.currentEvent = null;
				WorldEventLibrary.currentEventDuration = -1;
			}else
			{
				WorldEventLibrary.currentEventDuration -= 20;
			}
		}
	}

	public static void requestCurrentEventSyncForPlayer(EntityPlayerMP player)
	{
		PacketNBT syncPacket = new PacketNBT(ec3WorldTag).setID(2);
		EssentialCraftCore.network.sendTo(syncPacket, player);
	}

	public static void requestCurrentEventSync()
	{
		PacketNBT syncPacket = new PacketNBT(ec3WorldTag).setID(2);
		EssentialCraftCore.network.sendToAll(syncPacket);
	}

	public static int getActiveEventDuration()
	{
		return ec3WorldTag.getInteger("currentEventDuration");
	}

	public static String getActiveEvent()
	{
		return ec3WorldTag.getString("currentEvent");
	}

	public static boolean hasActiveEvent()
	{
		return !ec3WorldTag.hasNoTags() && ec3WorldTag.getString("currentEvent") != null && !ec3WorldTag.getString("currentEvent").isEmpty();
	}

	public static boolean isEventActive(String id)
	{
		return ec3WorldTag.getString("currentEvent").equalsIgnoreCase(id);
	}

	public static void sendChatMessageToAllPlayersInDim(int dimID,String msg)
	{
		for(int i = 0; i < FMLCommonHandler.instance().getMinecraftServerInstance().getCurrentPlayerCount(); ++i)
		{
			EntityPlayer player = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUsername(FMLCommonHandler.instance().getMinecraftServerInstance().getOnlinePlayerNames()[i]);
			if(player.dimension == dimID)
			{
				player.sendMessage(new TextComponentString(msg));
			}
		}
	}

	/**
	 * 0 - ShapedRecipe
	 * 1 - ShapelessRecipe
	 * 2 - ShapedOreRecipe
	 * 3 - ShapelessOreRecipe
	 * 4 - ShapedFurnaceRecipe
	 * 5 - RadiatingChamberRecipe
	 * 6 - MagicianTableRecipe
	 * 7 - ?StructureRecipe
	 * @param searched The IS you want to find
	 * @param recipeType ID
	 * @return The actual recipe or null if none found
	 */
	@SuppressWarnings("unchecked")
	public static IRecipe findRecipeByIS(ItemStack searched, int recipeType)
	{
		if(recipeType == 0 || recipeType == 1)
		{
			List<IRecipe> rec = CraftingManager.getInstance().getRecipeList();
			for(int i = 0; i < rec.size(); ++i)
			{
				IRecipe recipe = rec.get(i);
				if(recipe instanceof ShapedRecipes)
				{
					ShapedRecipes mRecipe = (ShapedRecipes) recipe;
					ItemStack output = mRecipe.getRecipeOutput();
					if(ItemStack.areItemStackTagsEqual(output, searched) && output.isItemEqual(searched))
						return new ShapedRecipes(mRecipe.recipeWidth,mRecipe.recipeHeight,mRecipe.recipeItems,mRecipe.getRecipeOutput());
				}
				if(recipe instanceof ShapelessRecipes)
				{
					ShapelessRecipes mRecipe = (ShapelessRecipes) recipe;
					ItemStack output = mRecipe.getRecipeOutput();
					if(output.isItemEqual(searched))
						return new ShapelessRecipes(mRecipe.getRecipeOutput(),mRecipe.recipeItems);
				}
			}
		}
		if(recipeType == 2 || recipeType == 3)
		{
			List<IRecipe> rec = CraftingManager.getInstance().getRecipeList();
			for(int i = 0; i < rec.size(); ++i)
			{
				IRecipe recipe = rec.get(i);
				if(recipe instanceof ShapedOreRecipe)
				{
					ShapedOreRecipe mRecipe = (ShapedOreRecipe) recipe;
					ItemStack output = mRecipe.getRecipeOutput();
					if(ItemStack.areItemStackTagsEqual(output, searched) && output.isItemEqual(searched))
						return copyShapedOreRecipe(mRecipe);
				}
				if(recipe instanceof ShapelessOreRecipe)
				{
					ShapelessOreRecipe mRecipe = (ShapelessOreRecipe) recipe;
					ItemStack output = mRecipe.getRecipeOutput();
					if(output.isItemEqual(searched))
						return copyShapelessOreRecipe(mRecipe);
				}
			}
		}
		if(recipeType == 4) {
			Set<?> $s = FurnaceRecipes.instance().getSmeltingList().entrySet();

			Iterator<?> $i = $s.iterator();

			while($i.hasNext()) {
				try {
					Object obj = $i.next();
					Entry<?, ?> entry = (Entry<?, ?>) obj;
					ItemStack key = (ItemStack) entry.getKey();
					ItemStack value = (ItemStack) entry.getValue();
					if(value.isItemEqual(searched)) {
						return new ShapedFurnaceRecipe(key,value);
					}
				}
				catch(Exception e) {
					e.printStackTrace();
					continue;
				}
			}

		}
		if(recipeType == 5) {
			return new RadiatingChamberRecipe(RadiatingChamberRecipes.getRecipeByResult(searched));
		}
		if(recipeType == 6) {
			return new MagicianTableRecipe(MagicianTableRecipes.getRecipeByResult(searched));
		}
		return null;
	}

	public static ShapelessOreRecipe copyShapelessOreRecipe(ShapelessOreRecipe recipe) {
		ShapelessOreRecipe ret = null;
		ArrayList<Object> lst = recipe.getInput();
		Object[] returnObj = new Object[lst.size()];
		for(int i = 0; i < lst.size(); ++i) {
			Object work = lst.get(i);
			if(work instanceof ItemStack || work instanceof Block || work instanceof Item || work instanceof String)
				returnObj[i] = work;
			if(work instanceof List<?>) {
				if(!((List<ItemStack>)work).isEmpty()) {
					ItemStack listStk = ((List<ItemStack>)work).get(0);
					if(OreDictionary.getOreIDs(listStk) != null && OreDictionary.getOreIDs(listStk).length > 0) {
						String oreDictName = OreDictionary.getOreName(OreDictionary.getOreIDs(listStk)[0]);
						returnObj[i] = oreDictName;
					}
				}
			}
		}
		ret = new ShapelessOreRecipe(recipe.getRecipeOutput(), returnObj);
		return ret;
	}

	public static ShapedOreRecipe copyShapedOreRecipe(ShapedOreRecipe recipe) {
		ShapedOreRecipe ret = new ShapedOreRecipe(recipe.getRecipeOutput(),new Object[]{"ooo","ooo","ooo",'o',Items.STICK});
		try {
			Class<ShapedOreRecipe> sorClazz = ShapedOreRecipe.class;
			Field wFld = sorClazz.getDeclaredFields()[4];
			wFld.setAccessible(true);
			Field hFld = sorClazz.getDeclaredFields()[5];
			hFld.setAccessible(true);
			hFld.getInt(recipe);
			Field inputFld = sorClazz.getDeclaredFields()[3];
			inputFld.setAccessible(true);
			Object[] obj = (Object[])inputFld.get(recipe);
			Field inputFld_acess = sorClazz.getDeclaredFields()[3];
			inputFld_acess.setAccessible(true);
			inputFld_acess.set(ret, obj);

		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		return ret;
	}


	public static boolean oreDictionaryCompare(ItemStack stk, ItemStack stk1) {
		if(stk == null || stk1 == null)
			return false;

		if(OreDictionary.getOreIDs(stk) == null || OreDictionary.getOreIDs(stk).length == 0 || OreDictionary.getOreIDs(stk1) == null || OreDictionary.getOreIDs(stk1).length == 0)
			return false;

		int[] ids = OreDictionary.getOreIDs(stk);
		int[] ids1 = OreDictionary.getOreIDs(stk1);

		for(int i = 0; i < ids.length; ++i) {
			for(int j = 0; j < ids1.length; ++j) {
				if(ids[i] == ids1[j])
					return true;
			}
		}

		return false;
	}

	public static boolean canFilterAcceptItem(IInventory filterInventory, ItemStack is, ItemStack filter) {
		if(filter.getItemDamage() == 0 || filter.getItemDamage() == 2) {
			for(int i = 0; i < filterInventory.getSizeInventory(); ++i) {
				ItemStack f = filterInventory.getStackInSlot(i);
				if(f != null) {
					if(f.getItem() instanceof ItemFilter) {
						if(canFilterAcceptItem(new InventoryMagicFilter(f),is,f))
							return true;
					}
					else if(filter.getItemDamage() == 2) {
						if(!f.isItemEqual(is) || !ItemStack.areItemStackTagsEqual(f, is))
							return true;
						else
							return false;
					}
					else if(f.isItemEqual(is) && ItemStack.areItemStackTagsEqual(f, is))
						return true;
				}
			}
		}else
		{
			boolean ignoreMeta = MiscUtils.getStackTag(filter).getBoolean("ignoreMeta");
			boolean ignoreNBT = MiscUtils.getStackTag(filter).getBoolean("ignoreNBT");
			boolean ignoreOreDict = MiscUtils.getStackTag(filter).getBoolean("ignoreOreDict");
			for(int i = 0; i < filterInventory.getSizeInventory(); ++i)
			{
				ItemStack f = filterInventory.getStackInSlot(i);
				if(f != null)
				{
					if(f.getItem() instanceof ItemFilter)
					{
						if(canFilterAcceptItem(new InventoryMagicFilter(f),is,f))
							return true;
					}else
					{
						if(filter.getItemDamage() == 1)
						{
							if(oreDictionaryCompare(is,f) || ignoreOreDict)
							{
								if(ItemStack.areItemStackTagsEqual(f, is) || ignoreNBT)
								{
									return true;
								}
							}else
							{
								if(ItemStack.areItemStacksEqual(is, f) || (is.getItem() == f.getItem() && ignoreMeta))
								{
									if(ItemStack.areItemStackTagsEqual(f, is) || ignoreNBT)
									{
										return true;
									}
								}
							}
						}else
						{
							if(!oreDictionaryCompare(is,f) || ignoreOreDict)
							{
								if(!ItemStack.areItemStackTagsEqual(f, is) || ignoreNBT)
								{
									return true;
								}else
									return false;
							}else
							{
								if(!ItemStack.areItemStacksEqual(is, f) || (is.getItem() == f.getItem() && ignoreMeta))
								{
									if(!ItemStack.areItemStackTagsEqual(f, is) || ignoreNBT)
									{
										return true;
									}else
										return false;
								}else
									return false;
							}
						}
					}
				}
			}
		}
		return filter.getItemDamage() > 1;
	}

	public static void spawnItemFX(TileEntity source, TileEntity destination)
	{
		double sX = source.getPos().getX() + 0.5D;
		double sY = source.getPos().getY() + 0.5D;
		double sZ = source.getPos().getZ() + 0.5D;
		double mX = destination.getPos().getX()+0.5D - sX;
		double mY = destination.getPos().getY()+0.5D - sY;
		double mZ = destination.getPos().getZ()+0.5D - sZ;
		String dataString = new String();
		dataString += "||mod:EC3.Particle.Item";
		dataString += "||x:"+sX+"||y:"+sY+"||z:"+sZ;
		dataString += "||mX:"+mX+"||mY:"+mY+"||mZ:"+mZ;
		DummyPacketIMSG pkt = new DummyPacketIMSG(dataString);
		DummyPacketHandler.sendToAll(pkt);
	}

	public static void spawnItemFX(double sX, double sY, double sZ, double dX, double dY, double dZ)
	{
		double mX = dX - sX;
		double mY = dY - sY;
		double mZ = dZ - sZ;
		String dataString = new String();
		dataString += "||mod:EC3.Particle.Item";
		dataString += "||x:"+sX+"||y:"+sY+"||z:"+sZ;
		dataString += "||mX:"+mX+"||mY:"+mY+"||mZ:"+mZ;
		DummyPacketIMSG pkt = new DummyPacketIMSG(dataString);
		DummyPacketHandler.sendToAll(pkt);
	}

	protected static void actionsTick() {
		if(!actions.isEmpty())
			for(int i = 0; i < actions.size(); ++i) {
				ScheduledServerAction ssa = actions.get(i);
				--ssa.actionTime;
				if(ssa.actionTime <= 0) {
					ssa.execute();
					actions.remove(i);
				}
			}
	}

	public static void addScheduledAction(ScheduledServerAction ssa) {
		if(FMLCommonHandler.instance().getEffectiveSide() != Side.SERVER)
			Notifier.notifyCustomMod("EssentialCraft", "[WARNING][SEVERE]Trying to add a scheduled server action not on server side, aborting!");

		actions.add(ssa);
	}

	public static void requestScheduledTileSync(TileEntity tile, EntityPlayer requester) {
		Side s = FMLCommonHandler.instance().getEffectiveSide();
		if(s == Side.CLIENT) {
			if(tile.getWorld() == null || tile.getWorld().provider == null)
				return;

			NBTTagCompound clientData = new NBTTagCompound();
			clientData.setString("playername", MiscUtils.getUUIDFromPlayer(requester).toString());
			clientData.setInteger("x", tile.getPos().getX());
			clientData.setInteger("y", tile.getPos().getY());
			clientData.setInteger("z", tile.getPos().getZ());
			clientData.setInteger("dim", tile.getWorld().provider.getDimension());
			PacketNBT packet = new PacketNBT(clientData).setID(7);
			EssentialCraftCore.network.sendToServer(packet);
		}
		else {
			addScheduledAction(new ServerToClientSyncAction(requester, tile));
		}
	}

	public static void balanceIn(TileEntity tile, int slotNum) {
		if(tile instanceof IInventory && tile instanceof ITEHasMRU) {
			IInventory inv = (IInventory) tile;
			ITEHasMRU mrut = (ITEHasMRU) tile;
			if(inv.getStackInSlot(slotNum) != null && inv.getStackInSlot(slotNum).getItem() instanceof ItemBoundGem && inv.getStackInSlot(slotNum).getTagCompound() != null) {
				ItemStack s = inv.getStackInSlot(slotNum);
				int[] o = ItemBoundGem.getCoords(s);
				if(MathUtils.getDifference(tile.getPos().getX(), o[0]) <= 16 && MathUtils.getDifference(tile.getPos().getY(), o[1]) <= 16 && MathUtils.getDifference(tile.getPos().getZ(), o[2]) <= 16) {
					BlockPos o1 = new BlockPos(o[0],o[1],o[2]);
					if(tile.getWorld().getTileEntity(o1) != null && tile.getWorld().getTileEntity(o1) instanceof ITEHasMRU) {
						mrut.setBalance(((ITEHasMRU) tile.getWorld().getTileEntity(o1)).getBalance());
					}
				}
			}
		}
	}

	public static void manage(TileEntity tile, int slotNum) {
		mruIn(tile, 0);
		spawnMRUParticles(tile, 0);
		balanceIn(tile, 0);
	}

	public static GameProfile EC3FakePlayerProfile = new GameProfile(UUID.fromString("5cd89d0b-e9ba-0000-89f4-b5dbb05963da"), "[EC3]");
}
