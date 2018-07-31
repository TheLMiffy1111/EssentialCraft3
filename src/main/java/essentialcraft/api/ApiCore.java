package essentialcraft.api;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.common.collect.HashMultimap;

import DummyCore.Utils.DummyDistance;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fml.common.FMLCommonHandler;

/**
 *
 * @author Modbder
 * @Description Just some nifty functions to help you out.
 */
public class ApiCore {

	/**
	 * A usual amount of MRU all generators would have
	 */
	public static final int GENERATOR_MAX_MRU_GENERIC = 10000;

	/**
	 * A usual amount of MRU all devices would have
	 */
	public static final int DEVICE_MAX_MRU_GENERIC = 5000;

	/**
	 * A list of all items, which allow the player to see MRUCUs and MRU particles
	 */
	public static final List<Item> MRU_VISIBLE_LIST = new ArrayList<Item>();

	/**
	 * A list of reductions the armor can have
	 */
	public static final HashMap<Item, ArrayList<Float>> ITEM_RESISTANCE_MAP = new HashMap<Item, ArrayList<Float>>();

	/**
	 * All categories the Book Of Knowledge can have
	 */
	public static final List<CategoryEntry> CATEGORY_LIST = new ArrayList<CategoryEntry>();

	/**
	 * A list of all discoveries bound to generic ItemStack
	 */
	public static final HashMap<String, DiscoveryEntry> IS_TO_DISCOVERY_MAP = new HashMap<String, DiscoveryEntry>();

	@CapabilityInject(IMRUHandler.class)
	public static Capability<IMRUHandler> MRU_HANDLER_CAPABILITY = null;

	@CapabilityInject(IMRUHandlerItem.class)
	public static Capability<IMRUHandlerItem> MRU_HANDLER_ITEM_CAPABILITY = null;

	@CapabilityInject(IMRUHandlerEntity.class)
	public static Capability<IMRUHandlerEntity> MRU_HANDLER_ENTITY_CAPABILITY = null;

	/**
	 * Use this to get a full information on the player - it's UBMRU, Balance and Corruption status
	 * @param p - the player to get the data of. Please check, that it is not null and is not a FakePlayer!
	 * @return The corresponding player data, or null if something went wrong
	 */
	public static IPlayerData getPlayerData(EntityPlayer p)
	{
		try
		{
			Class<?> ecUtilsClass = Class.forName("essentialcraft.utils.common.ECUtils");
			Method getData = ecUtilsClass.getMethod("getData", EntityPlayer.class);
			return (IPlayerData)getData.invoke(null, p);

		}catch(Exception e)
		{
			return null;
		}
	}

	/**
	 * Allows a specified block to be a part of a specified structure.
	 * @param structure - the structure the block can be a part of
	 * @param registered - the block that is registered. Not metadata sensitive!
	 */
	public static void registerBlockInStructure(EnumStructureType structure, Block registered)
	{
		try
		{
			Class<?> ecUtilsClass = Class.forName("essentialcraft.utils.common.ECUtils");
			Field hashMultimapFld = ecUtilsClass.getDeclaredField("STRUCTURE_TO_BLOCKS_MAP");
			hashMultimapFld.setAccessible(true);
			HashMultimap<EnumStructureType,Block> hashMap = (HashMultimap<EnumStructureType, Block>)hashMultimapFld.get(null);
			hashMap.put(structure, registered);
		}catch(Exception e)
		{
			return;
		}
	}

	/**
	 * Allows a block to 'resist' MRUCU effects on the player. Also that block will be tougher for the corruption no grow on
	 * @param registered - the block to register
	 * @param metadata - the block's metadata. Use -1 or OreDictionary.WILDCARD_VALUE to make the check ignore metadata.
	 * @param resistance - the resistance the block will have. All non-registered have 1.
	 */
	public static void registerBlockMRUResistance(Block registered, int metadata, float resistance)
	{
		try
		{
			Class<?> ecUtilsClass = Class.forName("essentialcraft.utils.common.ECUtils");
			Method regBlk = ecUtilsClass.getMethod("registerBlockResistance", Block.class,int.class,float.class);
			regBlk.setAccessible(true);
			regBlk.invoke(null,registered,metadata,resistance);
		}catch(Exception e)
		{
			return;
		}
	}

	/**
	 * Finds a DiscoveryEntry by the given ItemStack. The ItemStack would either be in the list of items at one of the pages, or will be a crafting result.
	 * @param referal - the ItemStack to lookup.
	 * @return A valid DiscoveryEntry if was found, null otherwise
	 */
	public static DiscoveryEntry findDiscoveryByIS(ItemStack referal)
	{
		if(referal.isEmpty() || referal.getItem() == null)
			return null;
		int size = referal.getCount();
		referal.setCount(1);
		DiscoveryEntry de = ApiCore.IS_TO_DISCOVERY_MAP.get(referal.toString());
		referal.setCount(size);
		return de;
	}

	/**
	 * Registers an item as one allowed to grant the player MRUCU and MRU vision
	 * @param i - the item to register
	 */
	public static void allowItemToSeeMRU(Item i)
	{
		MRU_VISIBLE_LIST.add(i);
	}

	public static void setItemResistances(Item item_0, float i, float j, float k)
	{
		ArrayList<Float> red = new ArrayList<Float>();
		red.add(i);
		red.add(j);
		red.add(k);
		ITEM_RESISTANCE_MAP.put(item_0, red);
	}

	public static boolean tryToDecreaseMRUInStorage(EntityPlayer player, int amount)
	{
		try
		{
			Class<?> ecUtilsClass = Class.forName("essentialcraft.utils.common.ECUtils");
			Method tryToDecreaseMRUInStorage = ecUtilsClass.getMethod("tryToDecreaseMRUInStorage", EntityPlayer.class,int.class);
			return Boolean.parseBoolean(tryToDecreaseMRUInStorage.invoke(null, player,-amount).toString());
		}
		catch(Exception e)
		{
			return false;
		}
	}

	public static void increaseCorruptionAt(World w, float x, float y, float z, int amount)
	{
		try
		{
			Class<?> ecUtilsClass = Class.forName("essentialcraft.utils.common.ECUtils");
			Method increaseCorruptionAt = ecUtilsClass.getMethod("increaseCorruptionAt", World.class,float.class,float.class,float.class,int.class);
			increaseCorruptionAt.setAccessible(true);
			increaseCorruptionAt.invoke(null, w,x,y,z,amount);
		}catch(Exception e)
		{
			return;
		}
	}

	public static Entity getClosestMRUCUEntity(World w, BlockPos c, int radius) {
		List<Entity> l = w.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(c).grow(radius, radius/2, radius), e->e.hasCapability(MRU_HANDLER_ENTITY_CAPABILITY, null));
		Entity ret = null;
		if(!l.isEmpty()) {
			double currentDistance = 0;
			double dominatingDistance = 0;
			int dominatingIndex = 0;
			DummyCore.Utils.Coord3D main = new DummyCore.Utils.Coord3D(c.getX()+0.5D,c.getY()+0.5D,c.getZ()+0.5D);
			for(int i = 0; i < l.size(); ++i) 	{
				Entity pressence = l.get(i);
				DummyCore.Utils.Coord3D current = new DummyCore.Utils.Coord3D(pressence.posX,pressence.posY,pressence.posZ);
				DummyDistance dist = new DummyDistance(main,current);
				if(i == 0) {
					dominatingIndex = i;
					dominatingDistance = dist.getDistance();
				}
				else {
					currentDistance = dist.getDistance();
					if(currentDistance < dominatingDistance) {
						dominatingIndex = i;
						dominatingDistance = dist.getDistance();
					}
				}
			}
			try {
				ret = l.get(dominatingIndex);
			}
			catch(IndexOutOfBoundsException e) {}
		}
		return ret;
	}

	public static IMRUHandlerEntity getClosestMRUCU(World w, BlockPos c, int radius) {
		return getClosestMRUCUEntity(w, c, radius).getCapability(MRU_HANDLER_ENTITY_CAPABILITY, null);
	}

	public static void registerTexture(ResourceLocation rl) {
		if(FMLCommonHandler.instance().getEffectiveSide().isServer())
			return;
		try {
			Class<?> coreClass = Class.forName("essentialcraft.common.mod.EssentialCraftCore");
			Class<?> proxyClass = Class.forName("essentialcraft.proxy.CommonProxy");
			Field proxyField = coreClass.getDeclaredField("proxy");
			proxyField.setAccessible(true);
			Method regMethod = proxyClass.getDeclaredMethod("registerTexture", ResourceLocation.class);
			regMethod.setAccessible(true);
			regMethod.invoke(proxyField.get(null), rl);
		}
		catch(Exception e) {

		}
	}
}
