package ec3.common.item;

import DummyCore.Client.IModelRegisterer;
import ec3.common.entity.EntityDividerProjectile;
import ec3.common.registry.AchievementRegistry;
import ec3.common.registry.SoundRegistry;
import ec3.utils.common.ECUtils;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

public class ItemDividerGun extends ItemStoresMRUInNBT implements IModelRegisterer {

	@Override
	public void onUpdate(ItemStack itemStack, World world, Entity entity, int indexInInventory, boolean isCurrentItem)
	{
		super.onUpdate(itemStack, world, entity, indexInInventory, isCurrentItem);
		if(entity instanceof EntityPlayer && entity.ticksExisted % 20 == 0)
			EntityPlayer.class.cast(entity).addStat(AchievementRegistry.achievementList.get("hologramGun"));
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack stk, World w, EntityPlayer p, EnumHand h)
	{
		p.setActiveHand(h);
		return new ActionResult(EnumActionResult.PASS, stk);
	}

	public EnumAction getItemUseAction(ItemStack p_77661_1_)
	{
		return EnumAction.BOW;
	}

	/**
	 * How long it takes to use or consume an item
	 */
	public int getMaxItemUseDuration(ItemStack p_77626_1_)
	{
		return 20;
	}

	public ItemStack onItemUseFinish(ItemStack stk, World w, EntityLivingBase p) {
		if(p instanceof EntityPlayer && (ECUtils.tryToDecreaseMRUInStorage((EntityPlayer)p, -5000) || this.setMRU(stk, -5000))) {
			w.playSound(p.posX, p.posY, p.posZ, SoundRegistry.gunBeam, SoundCategory.PLAYERS, 1, 2, false);
			EntityDividerProjectile proj = new EntityDividerProjectile(w,p);
			proj.setHeadingFromThrower(p, p.rotationPitch, p.rotationYaw, 0.0F, 1.5F, 1.0F);
			if(!w.isRemote)
				w.spawnEntity(proj);
		}
		return stk;
	}

	@Override
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation("essentialcraft:item/dividerGun", "inventory"));
	}
}
