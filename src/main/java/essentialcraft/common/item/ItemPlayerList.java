package essentialcraft.common.item;

import java.util.List;

import DummyCore.Client.IModelRegisterer;
import DummyCore.Utils.DataStorage;
import DummyCore.Utils.DummyData;
import DummyCore.Utils.MiscUtils;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemPlayerList extends Item implements IModelRegisterer {

	public ItemPlayerList() {
		super();
		this.maxStackSize = 1;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
	{
		NBTTagCompound itemTag = MiscUtils.getStackTag(player.getHeldItem(hand));
		if(!itemTag.hasKey("usernames"))
			itemTag.setString("usernames", "||username:null");
		String str = itemTag.getString("usernames");
		DummyData[] dt = DataStorage.parseData(str);
		boolean canAddUsername = true;
		for(int i = 0; i < dt.length; ++i)
		{
			if(dt[i].fieldValue.equals(MiscUtils.getUUIDFromPlayer(player).toString()))
				canAddUsername = false;
		}
		if(canAddUsername)
		{
			str+="||username:"+MiscUtils.getUUIDFromPlayer(player).toString();
		}
		itemTag.setString("usernames", str);
		return super.onItemRightClick(world, player, hand);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World player, List<String> list, ITooltipFlag par4)
	{
		if(stack.getTagCompound() != null)
		{
			list.add("Allowed Players:");
			NBTTagCompound itemTag = MiscUtils.getStackTag(stack);
			if(!itemTag.hasKey("usernames"))
				itemTag.setString("usernames", "||username:null");
			String str = itemTag.getString("usernames");
			DummyData[] dt = DataStorage.parseData(str);
			for(int i = 0; i < dt.length; ++i)
			{
				String name = dt[i].fieldValue;
				if(!name.equals("null"))
				{
					list.add(" -"+MiscUtils.getUsernameFromUUID(name));
				}
			}
		}
	}

	@Override
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation("minecraft:paper", "inventory"));
	}
}
