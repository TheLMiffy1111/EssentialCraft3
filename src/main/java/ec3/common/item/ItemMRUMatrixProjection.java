package ec3.common.item;

import java.util.List;

import DummyCore.Client.IModelRegisterer;
import DummyCore.Utils.MiscUtils;
import ec3.utils.common.ECUtils;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

public class ItemMRUMatrixProjection extends Item implements IModelRegisterer {

	public static String[] names = new String[]{"empty","chaos","frozen","magic","shade"};

	public ItemMRUMatrixProjection() {
		super();
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
	}

	public String getUnlocalizedName(ItemStack p_77667_1_)
	{
		return super.getUnlocalizedName(p_77667_1_)+"_"+names[Math.min(p_77667_1_.getItemDamage(),names.length-1)];
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer, EnumHand hand)
	{
		if(par1ItemStack.getTagCompound() != null)
		{
			String username = par1ItemStack.getTagCompound().getString("playerName");
			if(username.equals(par3EntityPlayer.getGameProfile().getId()))
				par3EntityPlayer.setActiveHand(hand);
		}
		if(par1ItemStack.getTagCompound() == null && !par2World.isRemote && !par3EntityPlayer.isSneaking())
		{
			NBTTagCompound playerTag = new NBTTagCompound();
			playerTag.setString("playerName", MiscUtils.getUUIDFromPlayer(par3EntityPlayer).toString());
			par1ItemStack.setTagCompound(playerTag);
		}
		return new ActionResult(EnumActionResult.PASS,par1ItemStack);
	}

	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List<String> par3List, boolean par4) 
	{
		if(par1ItemStack.getTagCompound() != null)
		{
			String username = MiscUtils.getUsernameFromUUID(par1ItemStack.getTagCompound().getString("playerName"));
			par3List.add(TextFormatting.DARK_GRAY+"Thus is a projection of "+TextFormatting.GOLD+username+TextFormatting.DARK_GRAY+"'s MRU Matrix");
		}
	}

	public void getSubItems(Item p_150895_1_, CreativeTabs p_150895_2_, List<ItemStack> p_150895_3_)
	{
		for(int i = 0; i < 5; ++i)
		{
			p_150895_3_.add(new ItemStack(p_150895_1_,1,i));
		}
	}

	public EnumAction getItemUseAction(ItemStack p_77661_1_)
	{
		return EnumAction.BLOCK;
	}

	public int getMaxItemUseDuration(ItemStack p_77626_1_)
	{
		return 200;
	}

	public void onUsingTick(ItemStack stack, EntityLivingBase player, int count)
	{
		if(count % 40 == 0)
		{
			player.playSound(SoundEvents.BLOCK_PORTAL_TRAVEL, 0.3F, 2);
			player.playSound(SoundEvents.BLOCK_PORTAL_TRIGGER, 0.3F, 2);
		}
		if(count == 100)
			player.addPotionEffect(new PotionEffect(MobEffects.NAUSEA,200,0,true,true));
		if(count <= 50)
		{
			player.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS,2000,0,true,true));
		}

	}

	public ItemStack onItemUseFinish(ItemStack p_77654_1_, World p_77654_2_, EntityLivingBase p_77654_3_)
	{
		if(p_77654_3_ instanceof EntityPlayer) {
			p_77654_3_.playSound(SoundEvents.BLOCK_PORTAL_TRIGGER, 0.3F, 0.01F);
			p_77654_3_.addPotionEffect(new PotionEffect(MobEffects.WITHER,40*19,0,true,true));
			p_77654_3_.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS,40*19,4,true,true));
			p_77654_3_.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS,40*19,4,true,true));
			p_77654_3_.addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE,40*19,4,true,true));
			p_77654_3_.addPotionEffect(new PotionEffect(MobEffects.HUNGER,40*19,0,true,true));
			if(!p_77654_3_.getEntityWorld().isRemote)
			{
				p_77654_3_.sendMessage(new TextComponentString(TextFormatting.AQUA+"Your MRU Matrix twists with new colors!"));
				ECUtils.getData((EntityPlayer)p_77654_3_).modifyMatrixType(p_77654_1_.getItemDamage());
			}
			((EntityPlayer)p_77654_3_).inventory.decrStackSize(((EntityPlayer)p_77654_3_).inventory.currentItem, 1);
		}
		return p_77654_1_;
	}

	@Override
	public void registerModels() {
		for(int i = 0; i < names.length; i++) {
			ModelLoader.setCustomModelResourceLocation(this, i, new ModelResourceLocation("essentialcraft:item/mruMatrixProjection", "type=" + names[i]));
		}
	}
}
