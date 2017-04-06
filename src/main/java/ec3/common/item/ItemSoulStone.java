package ec3.common.item;

import java.util.List;

import DummyCore.Client.IModelRegisterer;
import DummyCore.Utils.MiscUtils;
import ec3.common.mod.EssentialCraftCore;
import ec3.network.PacketNBT;
import ec3.utils.common.ECUtils;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Loader;

public class ItemSoulStone extends Item implements IItemColor, IModelRegisterer {

	int clientTimer = 0;
	public ItemSoulStone() {
		super();
		this.maxStackSize = 1;
	}

	public void onUpdate(ItemStack stk, World w, Entity e, int slotnum, boolean held) 
	{
		if(stk.getTagCompound() != null && stk.getItemDamage() == 0 && stk.getTagCompound().hasKey("bloodInfused"))
		{
			stk.getTagCompound().removeTag("bloodInfused");
			stk.setItemDamage(1);
		}
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer, EnumHand hand)
	{
		if(!par2World.isRemote && !par3EntityPlayer.isSneaking())
		{
			NBTTagCompound playerTag = MiscUtils.getStackTag(par1ItemStack);
			playerTag.setString("playerName", MiscUtils.getUUIDFromPlayer(par3EntityPlayer).toString());
			par1ItemStack.setTagCompound(playerTag);
		}
		if(par1ItemStack.getTagCompound() != null && !par2World.isRemote && par3EntityPlayer.isSneaking())
		{
			MiscUtils.getStackTag(par1ItemStack).removeTag("playerName");
		}
		return new ActionResult(EnumActionResult.PASS,par1ItemStack);
	}

	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List<String> par3List, boolean par4) 
	{
		if(par1ItemStack.getTagCompound() != null)
		{
			String username = par1ItemStack.getTagCompound().getString("playerName");
			EntityPlayer player = par2EntityPlayer;
			if(player != null)
			{
				if(ECUtils.playerDataExists(username))
				{
					int currentEnergy = ECUtils.getData(player).getPlayerUBMRU();
					int att = ECUtils.getData(player).getMatrixTypeID();
					par3List.add(TextFormatting.DARK_GRAY+"Tracking MRU Matrix of "+TextFormatting.GOLD+MiscUtils.getUsernameFromUUID(username));
					par3List.add(TextFormatting.DARK_GRAY+"Detected "+TextFormatting.GREEN+currentEnergy+TextFormatting.DARK_GRAY+" UBMRU Energy");

					String at = "Neutral";
					switch(att)
					{
					case 0:
					{
						at = TextFormatting.GREEN+"Neutral";
						break;
					}
					case 1:
					{
						at = TextFormatting.RED+"Chaos";
						break;
					}
					case 2:
					{
						at = TextFormatting.BLUE+"Frozen";
						break;
					}
					case 3:
					{
						at = TextFormatting.LIGHT_PURPLE+"Magic";
						break;
					}
					case 4:
					{
						at = TextFormatting.GRAY+"Shade";
						break;
					}
					default:
					{
						at = TextFormatting.GREEN+"Unknown";
						break;
					}
					}
					par3List.add(TextFormatting.DARK_GRAY+"MRU Matrix twists with "+at+TextFormatting.DARK_GRAY+" energies.");
				}
			}else
			{
				par3List.add(TextFormatting.DARK_GRAY+"The MRU Matrix of the owner is too pale to track...");
				if(clientTimer == 0)
				{
					NBTTagCompound sTag = new NBTTagCompound();
					sTag.setString("syncplayer", username);
					sTag.setString("sender", MiscUtils.getUUIDFromPlayer(par2EntityPlayer).toString());
					EssentialCraftCore.network.sendToServer(new PacketNBT(sTag).setID(1));
					clientTimer = 100;

				}else
				{
					--clientTimer;
				}
			}
			this.addBloodMagicDescription(par1ItemStack, par2EntityPlayer, par3List, par4);
		}
	}

	public void addBloodMagicDescription(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List<String> par3List, boolean par4)
	{
		if(par1ItemStack.getItemDamage() == 1)
		{
			String username = par1ItemStack.getTagCompound().getString("playerName");

			try
			{
				if(Loader.isModLoaded("BloodMagic")) {
					int currentEssence = WayofTime.bloodmagic.api.util.helper.NetworkHelper.getSoulNetwork(par2EntityPlayer).getCurrentEssence();
					par3List.add(TextFormatting.DARK_GRAY+"Detected "+TextFormatting.DARK_RED+currentEssence+TextFormatting.DARK_GRAY+" Life Essence.");
				}
			}catch(Exception e)
			{
				par3List.add(TextFormatting.DARK_GRAY+"The owner's life network is pure and untouched...");
			}
		}else if(par1ItemStack.getTagCompound() != null)
		{
			par3List.add(TextFormatting.DARK_GRAY+"The owner's life network is pure and untouched...");
		}
	}

	public int getColorFromItemstack(ItemStack p_82790_1_, int p_82790_2_)
	{
		if(p_82790_1_.getItemDamage() == 1)
		{
			return 0xff0000;
		}
		return 16777215;
	}

	@Override
	public void registerModels() {
		for(int i = 0; i < 2; i++) {
			ModelLoader.setCustomModelResourceLocation(this, i, new ModelResourceLocation("essentialcraft:item/soulStone", "inventory"));
		}
	}
}
