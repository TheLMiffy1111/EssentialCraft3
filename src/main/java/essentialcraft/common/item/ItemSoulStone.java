package essentialcraft.common.item;

import java.lang.reflect.Method;
import java.util.List;

import DummyCore.Client.IItemColor;
import DummyCore.Client.IModelRegisterer;
import DummyCore.Utils.MiscUtils;
import essentialcraft.api.IPlayerData;
import essentialcraft.common.mod.EssentialCraftCore;
import essentialcraft.network.PacketNBT;
import essentialcraft.utils.common.ECUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemSoulStone extends Item implements IItemColor, IModelRegisterer {

	int clientTimer = 0;
	public ItemSoulStone() {
		super();
		this.maxStackSize = 1;
	}

	@Override
	public void onUpdate(ItemStack stk, World w, Entity e, int slotnum, boolean held) {
		if(stk.getTagCompound() != null && stk.getItemDamage() == 0 && stk.getTagCompound().hasKey("bloodInfused")) {
			stk.getTagCompound().removeTag("bloodInfused");
			stk.setItemDamage(1);
		}
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);
		if(!world.isRemote && !player.isSneaking()) {
			NBTTagCompound playerTag = MiscUtils.getStackTag(stack);
			playerTag.setString("playerName", MiscUtils.getUUIDFromPlayer(player).toString());
			stack.setTagCompound(playerTag);
		}
		if(stack.getTagCompound() != null && !world.isRemote && player.isSneaking()) {
			MiscUtils.getStackTag(stack).removeTag("playerName");
		}
		return super.onItemRightClick(world, player, hand);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World world, List<String> list, ITooltipFlag par4) {
		if(stack.getTagCompound() != null) {
			String username = stack.getTagCompound().getString("playerName");
			EntityPlayer player = Minecraft.getMinecraft().player;
			if(player != null) {
				if(ECUtils.playerDataExists(username)) {
					IPlayerData data = ECUtils.getData(player);
					int currentEnergy = data.getPlayerUBMRU();
					int att = data.getMatrixTypeID();
					list.add(TextFormatting.DARK_GRAY+"Tracking MRU Matrix of "+TextFormatting.GOLD+MiscUtils.getUsernameFromUUID(username));
					list.add(TextFormatting.DARK_GRAY+"Detected "+TextFormatting.GREEN+currentEnergy+TextFormatting.DARK_GRAY+" UBMRU Energy");

					String at = "Neutral";
					switch(att) {
					case 0: {
						at = TextFormatting.GREEN+"Neutral";
						break;
					}
					case 1: {
						at = TextFormatting.RED+"Chaos";
						break;
					}
					case 2: {
						at = TextFormatting.BLUE+"Frozen";
						break;
					}
					case 3: {
						at = TextFormatting.LIGHT_PURPLE+"Magic";
						break;
					}
					case 4: {
						at = TextFormatting.GRAY+"Shade";
						break;
					}
					default: {
						at = TextFormatting.GREEN+"Unknown";
						break;
					}
					}
					list.add(TextFormatting.DARK_GRAY+"MRU Matrix twists with "+at+TextFormatting.DARK_GRAY+" Energies");
					if(data.isWindbound()) {
						list.add(TextFormatting.DARK_GRAY+"The player is "+TextFormatting.GREEN+"Windbound"+TextFormatting.DARK_GRAY);
					}
				}
			}
			else {
				list.add(TextFormatting.DARK_GRAY+"The MRU Matrix of the owner is too weak to track");
				if(clientTimer == 0) {
					NBTTagCompound sTag = new NBTTagCompound();
					sTag.setString("syncplayer", username);
					sTag.setString("sender", MiscUtils.getUUIDFromPlayer(player).toString());
					EssentialCraftCore.network.sendToServer(new PacketNBT(sTag).setID(1));
					clientTimer = 100;
				}
				else {
					--clientTimer;
				}
			}
			this.addBloodMagicDescription(stack, player, list, par4);
		}
	}

	@SideOnly(Side.CLIENT)
	public void addBloodMagicDescription(ItemStack stack, EntityPlayer player, List<String> list, ITooltipFlag par4) {
		if(Loader.isModLoaded("bloodmagic")) {
			if(stack.getItemDamage() == 1) {
				String username = stack.getTagCompound().getString("playerName");

				if(EssentialCraftCore.clazzExists("WayofTime.bloodmagic.api.BloodMagicAPI")) {
					try {
						Class<?> classNetworkHelper = Class.forName("WayofTime.bloodmagic.api.util.helper.NetworkHelper");
						Method getSoulNetwork = classNetworkHelper.getMethod("getSoulNetwork", EntityPlayer.class);
						Class<?> classSoulNetwork = Class.forName("WayofTime.bloodmagic.api.saving.SoulNetwork");
						Method getCurrentEssence = classSoulNetwork.getMethod("getCurrentEssence");

						int currentEssence = (Integer)getCurrentEssence.invoke(getSoulNetwork.invoke(null, player));
						list.add(TextFormatting.DARK_GRAY+"Detected "+TextFormatting.DARK_RED+currentEssence+TextFormatting.DARK_GRAY+" Life Essence");
					}
					catch(Exception e) {
						e.printStackTrace();
						list.add(TextFormatting.DARK_GRAY+"The owner's life network is pure and untouched");
					}
				}
				if(EssentialCraftCore.clazzExists("WayofTime.bloodmagic.apibutnotreally.BloodMagicAPI")) {
					try {
						Class<?> classNetworkHelper = Class.forName("WayofTime.bloodmagic.apibutnotreally.util.helper.NetworkHelper");
						Method getSoulNetwork = classNetworkHelper.getMethod("getSoulNetwork", EntityPlayer.class);
						Class<?> classSoulNetwork = Class.forName("WayofTime.bloodmagic.apibutnotreally.saving.SoulNetwork");
						Method getCurrentEssence = classSoulNetwork.getMethod("getCurrentEssence");

						int currentEssence = (Integer)getCurrentEssence.invoke(getSoulNetwork.invoke(null, player));
						list.add(TextFormatting.DARK_GRAY+"Detected "+TextFormatting.DARK_RED+currentEssence+TextFormatting.DARK_GRAY+" Life Essence");
					}
					catch(Exception e) {
						e.printStackTrace();
						list.add(TextFormatting.DARK_GRAY+"The owner's life network is pure and untouched");
					}
				}
			}
		}
	}

	@Override
	public int getColorFromItemstack(ItemStack p_82790_1_, int p_82790_2_) {
		if(p_82790_1_.getItemDamage() == 1) {
			return 0xFF0000;
		}
		return 0xFFFFFF;
	}

	@Override
	public void registerModels() {
		for(int i = 0; i < 2; i++) {
			ModelLoader.setCustomModelResourceLocation(this, i, new ModelResourceLocation("essentialcraft:item/soulstone", "inventory"));
		}
	}
}
