package ec3.common.item;

import java.util.List;

import DummyCore.Client.IModelRegisterer;
import DummyCore.Client.ModelUtils;
import DummyCore.Utils.MiscUtils;
import ec3.common.entity.EntityMRUPresence;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ItemMRUMover extends Item implements IModelRegisterer {
	
	public String textureName;
    
    public int getMaxItemUseDuration(ItemStack par1ItemStack)
    {
        return 72000;
    }
    
    @Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		return !oldStack.getItem().equals(newStack.getItem()) || MiscUtils.getStackTag(oldStack).getBoolean("active") != MiscUtils.getStackTag(newStack).getBoolean("active");
	}

	public EnumAction getItemUseAction(ItemStack par1ItemStack)
    {
        return EnumAction.BOW;
    }
    
    public void onUsingTick(ItemStack stack, EntityLivingBase player, int count)
    {
    	NBTTagCompound tag = MiscUtils.getStackTag(stack);
    	tag.setBoolean("active", true);
    	Vec3d mainLookVec = player.getLookVec();
    	for(int i = 0; i < 20; ++i)
		{
			Vec3d additionalVec = mainLookVec.addVector(mainLookVec.xCoord*i, mainLookVec.yCoord*i, mainLookVec.zCoord*i);
			List<EntityMRUPresence> entityList = player.getEntityWorld().getEntitiesWithinAABB(EntityMRUPresence.class, new AxisAlignedBB(player.posX+additionalVec.xCoord-1, player.posY+additionalVec.yCoord-2, player.posZ+additionalVec.zCoord-1, player.posX+additionalVec.xCoord+1, player.posY+additionalVec.yCoord+2, player.posZ+additionalVec.zCoord+1));
			if(!entityList.isEmpty())
			{
				EntityMRUPresence presence = (EntityMRUPresence)entityList.get(player.getEntityWorld().rand.nextInt(entityList.size()));
				player.getEntityWorld().spawnParticle(EnumParticleTypes.PORTAL, player.posX, player.posY, player.posZ, presence.posX-player.posX, presence.posY-player.posY-1, presence.posZ-player.posZ);
				float moveX = 0;
				float moveY = 0;
				float moveZ = 0;
				moveX = (float) -(mainLookVec.xCoord/10);
				moveY = (float) -(mainLookVec.yCoord/10);
				moveZ = (float) -(mainLookVec.zCoord/10);
				//if(!presence.getEntityWorld().isRemote)
				{
					if(!player.isSneaking())
						presence.setPositionAndRotation(presence.posX+moveX, presence.posY+moveY, presence.posZ+moveZ, 0, 0);
					else
						presence.setPositionAndRotation(presence.posX-moveX, presence.posY-moveY, presence.posZ-moveZ, 0, 0);
					if(count % 20 == 0)
						stack.damageItem(1, player);
				}
					
				break;
			}
		}
    }
    
    
    
    public ActionResult<ItemStack> onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer, EnumHand hand)
    {
        par3EntityPlayer.setActiveHand(hand);
        return new ActionResult(EnumActionResult.PASS, par1ItemStack);
    }
    
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving)
    {
    	NBTTagCompound tag = MiscUtils.getStackTag(stack);
    	tag.setBoolean("active", false);
        return stack;
    }
    
    @Override
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
    	NBTTagCompound tag = MiscUtils.getStackTag(stack);
    	tag.setBoolean("active", false);
	}
	
	public ItemMRUMover setTextureName(String name) {
		textureName = name;
		return this;
	}

	@Override
	public void registerModels() {
		ModelUtils.setItemModelNBTActive(this, "essentialcraft:item/" + getRegistryName().getResourcePath());
	}
}
