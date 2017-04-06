package ec3.common.item;

import java.util.List;

import DummyCore.Client.IModelRegisterer;
import ec3.utils.common.ECUtils;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

public class ItemMagnetizingStaff extends ItemStoresMRUInNBT implements IModelRegisterer {

	public ItemMagnetizingStaff() {
		super();	
		this.setMaxMRU(5000);
		this.maxStackSize = 1;
		this.bFull3D = true;
	}

	public int getMaxItemUseDuration(ItemStack p_77626_1_)
	{
		return Integer.MAX_VALUE;
	}

	public void onUsingTick(ItemStack stack, EntityLivingBase player, int count)
	{
		if(count % 8 == 0 && player instanceof EntityPlayer && (ECUtils.tryToDecreaseMRUInStorage((EntityPlayer)player, -50) || this.setMRU(stack, -50)))
		{
			player.swingArm(EnumHand.MAIN_HAND);
			List<EntityItem> items = player.getEntityWorld().getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(player.posX-0.5D, player.posY, player.posZ-0.5D, player.posX+0.5D, player.posY+1, player.posZ+0.5D).expand(12D, 6D, 12D));
			for(EntityItem item : items)
			{
				if(player.posX < item.posX)
					item.motionX -= 0.1F;
				else
					item.motionX += 0.1F;

				if(player.posY < item.posY)
					item.motionY -= 0.1F;
				else
					item.motionY += 0.5F;

				if(player.posZ < item.posZ)
					item.motionZ -= 0.1F;
				else
					item.motionZ += 0.1F;
			}

			//Split//

			List<EntityXPOrb> orbs = player.getEntityWorld().getEntitiesWithinAABB(EntityXPOrb.class, new AxisAlignedBB(player.posX-0.5D, player.posY, player.posZ-0.5D, player.posX+0.5D, player.posY+1, player.posZ+0.5D).expand(12D, 6D, 12D));
			for(EntityXPOrb item : orbs)
			{
				if(player.posX < item.posX)
					item.motionX -= 0.1F;
				else
					item.motionX += 0.1F;

				if(player.posY < item.posY)
					item.motionY -= 0.1F;
				else
					item.motionY += 0.5F;

				if(player.posZ < item.posZ)
					item.motionZ -= 0.1F;
				else
					item.motionZ += 0.1F;
			}
		}
	}

	public EnumAction getItemUseAction(ItemStack p_77661_1_)
	{
		return EnumAction.BOW;
	}

	public ActionResult<ItemStack> onItemRightClick(ItemStack p_77659_1_, World p_77659_2_, EntityPlayer p_77659_3_, EnumHand hand)
	{
		p_77659_3_.setActiveHand(hand);
		return new ActionResult(EnumActionResult.PASS,p_77659_1_);
	}

	@Override
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation("essentialcraft:item/magnetizingStaff", "inventory"));
	}
}
