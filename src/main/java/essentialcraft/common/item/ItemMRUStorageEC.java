package essentialcraft.common.item;

import java.util.List;
import java.util.Locale;

import DummyCore.Client.IItemColor;
import DummyCore.Client.IModelRegisterer;
import DummyCore.Utils.MathUtils;
import baubles.api.BaubleType;
import baubles.api.IBauble;
import essentialcraft.api.IMRUHandlerItem;
import essentialcraft.common.capabilities.mru.CapabilityMRUHandler;
import essentialcraft.common.capabilities.mru.MRUItemStorage;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemMRUStorageEC extends Item implements IBauble, IItemColor, IModelRegisterer {

	public static Capability<IMRUHandlerItem> MRU_HANDLER_ITEM_CAPABILITY = CapabilityMRUHandler.MRU_HANDLER_ITEM_CAPABILITY;
	public int[] maxMRU = new int[6];
	public static int[] colors = {0x555555, 0x665555, 0x775555, 0x885555, 0x995555, 0xaa5555, 0xbb5555, 0xcc5555, 0xdd5555, 0xee6666, 0xff6666};
	public static String[] dropNames = {"SoulShard", "SoulSphere", "DarkSoulMatter", "RedSoulMatter", "MatterOfEternity", "Unknown"};
	public ItemMRUStorageEC(int[] maxMRU) {
		super();
		this.maxMRU = maxMRU;
		setMaxStackSize(1);
		setHasSubtypes(true);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World world, List<String> list, ITooltipFlag flag) {
		super.addInformation(stack, world, list, flag);
		list.add(stack.getCapability(MRU_HANDLER_ITEM_CAPABILITY, null).getMRU() + "/" + stack.getCapability(MRU_HANDLER_ITEM_CAPABILITY, null).getMaxMRU() + " MRU");
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list) {
		if(this.isInCreativeTab(tab)) {
			for(int i = 0; i < 5; ++i) {
				ItemStack min = new ItemStack(this, 1, i);
				ItemStack max = new ItemStack(this, 1, i);
				min.getCapability(MRU_HANDLER_ITEM_CAPABILITY, null).setMRU(0);
				max.getCapability(MRU_HANDLER_ITEM_CAPABILITY, null).setMRU(maxMRU[i]);
				list.add(min);
				list.add(max);
			}
		}
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return getUnlocalizedName()+dropNames[Math.min(stack.getItemDamage(), dropNames.length-1)];
	}

	@Override
	public int getColorFromItemstack(ItemStack stack, int tintIndex) {
		int dam = stack.getItemDamage();
		if(dam != -1) {
			int currentMRU = stack.getCapability(MRU_HANDLER_ITEM_CAPABILITY, null).getMRU();
			int maxMRU = stack.getCapability(MRU_HANDLER_ITEM_CAPABILITY, null).getMaxMRU();
			int percentage = MathUtils.getPercentage(currentMRU, maxMRU);
			percentage /= 10;
			return colors[percentage];
		}
		return 16777215;
	}

	@Override
	public BaubleType getBaubleType(ItemStack itemstack) {
		return BaubleType.RING;
	}

	@Override
	public void onWornTick(ItemStack itemstack, EntityLivingBase player) {}

	@Override
	public void onEquipped(ItemStack itemstack, EntityLivingBase player) {}

	@Override
	public void onUnequipped(ItemStack itemstack, EntityLivingBase player) {}

	@Override
	public boolean canEquip(ItemStack itemstack, EntityLivingBase player) {
		return true;
	}

	@Override
	public boolean canUnequip(ItemStack itemstack, EntityLivingBase player) {
		return true;
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
		if(stack.getItemDamage() >= 0 && stack.getItemDamage() < 5) {
			return new MRUItemStorage(stack, maxMRU[stack.getItemDamage()], true);
		}
		return null;
	}

	@Override
	public void registerModels() {
		for(int i = 0; i < dropNames.length-1; i++) {
			ModelLoader.setCustomModelResourceLocation(this, i, new ModelResourceLocation("essentialcraft:item/storage", "type=" + dropNames[i].toLowerCase(Locale.ENGLISH)));
		}
	}
}
