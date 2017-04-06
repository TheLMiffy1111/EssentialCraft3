package ec3.common.item;

import java.util.List;

import DummyCore.Client.IModelRegisterer;
import ec3.utils.common.ECUtils;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

public class ItemSpikyShield extends ItemStoresMRUInNBT implements IModelRegisterer {

	public ItemSpikyShield() {
		super();	
		this.setMaxMRU(5000);
		this.maxStackSize = 1;
		this.bFull3D = true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ItemStack onItemUseFinish(ItemStack p_77654_1_, World p_77654_2_, EntityLivingBase p_77654_3_)
	{
		if(p_77654_3_ instanceof EntityPlayer && (ECUtils.tryToDecreaseMRUInStorage((EntityPlayer)p_77654_3_, -100) || this.setMRU(p_77654_1_, -100)))
		{
			List<EntityMob> mobs = p_77654_2_.getEntitiesWithinAABB(EntityMob.class, new AxisAlignedBB(p_77654_3_.posX-5, p_77654_3_.posY-2, p_77654_3_.posZ-5, p_77654_3_.posX+5, p_77654_3_.posY+2, p_77654_3_.posZ+5));
			if(!mobs.isEmpty())
			{
				for(int i = 0; i < mobs.size(); ++i)
				{
					EntityMob mob = mobs.get(i);
					mob.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer)p_77654_3_), 12F);
				}
			}
		}
		return p_77654_1_;
	}

	public void onUsingTick(ItemStack stack, EntityLivingBase player, int count)
	{
		player.hurtResistantTime = 20;
	}

	/**
	 * How long it takes to use or consume an item
	 */
	public int getMaxItemUseDuration(ItemStack p_77626_1_)
	{
		return 40;
	}

	/**
	 * returns the action that specifies what animation to play when the items is being used
	 */
	public EnumAction getItemUseAction(ItemStack p_77661_1_)
	{
		return EnumAction.BLOCK;
	}

	/**
	 * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer, enumHand
	 */
	public ActionResult<ItemStack> onItemRightClick(ItemStack p_77659_1_, World p_77659_2_, EntityPlayer p_77659_3_, EnumHand hand)
	{
		if((ECUtils.tryToDecreaseMRUInStorage(p_77659_3_, -50*40) || this.setMRU(p_77659_1_, -50*40)))
		{
			p_77659_3_.setActiveHand(hand);
		}
		return new ActionResult(EnumActionResult.PASS,p_77659_1_);
	}

	@Override
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation("essentialcraft:item/spikyShield", "inventory"));
	}
}
