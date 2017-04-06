package ec3.common.item;

import java.util.List;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import DummyCore.Client.IModelRegisterer;
import DummyCore.Utils.MiscUtils;
import ec3.api.IItemRequiresMRU;
import ec3.utils.common.ECUtils;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

public class ItemChaosFork extends ItemSword implements IItemRequiresMRU, IModelRegisterer {

	public ItemChaosFork() {
		super(ItemsCore.elemental);	
		this.setMaxMRU(5000);
		this.maxStackSize = 1;
		this.bFull3D = true;
		this.setMaxDamage(0);
	}

	int maxMRU = 5000;

	public Item setMaxMRU(int max)
	{
		maxMRU = max;
		return this;
	}

	@Override
	public boolean setMRU(ItemStack stack, int amount) {
		if(MiscUtils.getStackTag(stack).getInteger("mru")+amount >= 0 && MiscUtils.getStackTag(stack).getInteger("mru")+amount<=MiscUtils.getStackTag(stack).getInteger("maxMRU"))
		{
			MiscUtils.getStackTag(stack).setInteger("mru", MiscUtils.getStackTag(stack).getInteger("mru")+amount);
			return true;
		}
		return false;
	}

	@Override
	public int getMRU(ItemStack stack) {
		return MiscUtils.getStackTag(stack).getInteger("mru");
	}

	public boolean isItemTool(ItemStack p_77616_1_)
	{
		return true;
	}

	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List<String> par3List, boolean par4) 
	{
		super.addInformation(par1ItemStack, par2EntityPlayer, par3List, par4);
		par3List.add(ECUtils.getStackTag(par1ItemStack).getInteger("mru") + "/" + ECUtils.getStackTag(par1ItemStack).getInteger("maxMRU") + " MRU");
	}

	@Override
	public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List<ItemStack> par3List)
	{
		for (int var4 = 0; var4 < 1; ++var4)
		{
			ItemStack min = new ItemStack(par1, 1, 0);
			ECUtils.initMRUTag(min, maxMRU);
			ItemStack max = new ItemStack(par1, 1, 0);
			ECUtils.initMRUTag(max, maxMRU);
			ECUtils.getStackTag(max).setInteger("mru", ECUtils.getStackTag(max).getInteger("maxMRU"));
			par3List.add(min);
			par3List.add(max);
		}
	}

	@Override
	public int getMaxMRU(ItemStack stack) {
		return this.maxMRU;
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack p_77654_1_, World p_77654_2_, EntityLivingBase p_77654_3_)
	{
		Vec3d playerLookVec = p_77654_3_.getLookVec();
		p_77654_3_.motionX += playerLookVec.xCoord;
		p_77654_3_.motionY += playerLookVec.yCoord;
		p_77654_3_.motionZ += playerLookVec.zCoord;
		return p_77654_1_;
	}

	/**
	 * How long it takes to use or consume an item
	 */
	public int getMaxItemUseDuration(ItemStack p_77626_1_)
	{
		return 32;
	}

	public boolean hitEntity(ItemStack p_77644_1_, EntityLivingBase p_77644_2_, EntityLivingBase p_77644_3_)
	{
		try {
			if(p_77644_3_ instanceof EntityPlayer)
			{
				EntityPlayer player = (EntityPlayer) p_77644_3_;
				if((ECUtils.tryToDecreaseMRUInStorage((EntityPlayer)p_77644_3_, -250) || this.setMRU(p_77644_1_, -250)))
				{
					int att = ECUtils.getData(player).getMatrixTypeID();
					if(att == 1)
					{
						PotionEffect eff = p_77644_2_.getActivePotionEffect(MobEffects.MINING_FATIGUE);
						if(eff != null && p_77644_2_.hurtResistantTime == 0 || p_77644_2_.hurtResistantTime >= 15 && eff != null)
						{
							int buffLevel = eff.getAmplifier();
							p_77644_2_.addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE,100,eff.getAmplifier()+1));
							p_77644_3_.addPotionEffect(new PotionEffect(MobEffects.STRENGTH,100,buffLevel));
							return true;
						}
						else
						{
							if(p_77644_2_.hurtResistantTime == 0 || p_77644_2_.hurtResistantTime >= 15)
							{
								int buffLevel = 0;
								p_77644_2_.addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE,100,0));
								p_77644_3_.addPotionEffect(new PotionEffect(MobEffects.STRENGTH,100,buffLevel));
								return true;
							}
						}

					}
				}
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot equipmentSlot, ItemStack stack)
	{
		Multimap<String, AttributeModifier> multimap = HashMultimap.<String, AttributeModifier>create();
		if(equipmentSlot == EntityEquipmentSlot.MAINHAND)
			multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", 6, 0));
		return multimap;
	}

	/**
	 * returns the action that specifies what animation to play when the items is being used
	 */
	public EnumAction getItemUseAction(ItemStack p_77661_1_)
	{
		return EnumAction.BOW;
	}

	/**
	 * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
	 */
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand)
	{
		playerIn.setActiveHand(hand);
		return new ActionResult(EnumActionResult.PASS, itemStackIn);
	}

	@Override
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation("essentialcraft:item/chaosFork", "inventory"));
	}
}
