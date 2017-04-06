package ec3.common.item;

import DummyCore.Client.IModelRegisterer;
import ec3.common.entity.EntityOrbitalStrike;
import ec3.common.registry.AchievementRegistry;
import ec3.utils.common.ECUtils;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

public class ItemOrbitalRemote extends ItemStoresMRUInNBT implements IModelRegisterer {

	@Override
	public void onUpdate(ItemStack itemStack, World world, Entity entity, int indexInInventory, boolean isCurrentItem)
	{
		super.onUpdate(itemStack, world, entity, indexInInventory, isCurrentItem);
		if(entity instanceof EntityPlayer && entity.ticksExisted % 20 == 0)
			EntityPlayer.class.cast(entity).addStat(AchievementRegistry.achievementList.get("hologramRemote"));
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack stk, World w, EntityPlayer p, EnumHand hand)
	{
		float f = 1;
		double d0 = p.prevPosX + (p.posX - p.prevPosX) * (double)f;
		double d1 = p.prevPosY + (p.posY - p.prevPosY) * (double)f + (double)p.getEyeHeight();
		double d2 = p.prevPosZ + (p.posZ - p.prevPosZ) * (double)f;

		Vec3d lookVec = new Vec3d(d0, d1, d2);
		float f1 = p.prevRotationPitch + (p.rotationPitch - p.prevRotationPitch);
		float f2 = p.prevRotationYaw + (p.rotationYaw - p.prevRotationYaw);
		float f3 = MathHelper.cos(-f2 * 0.017453292F - (float)Math.PI);
		float f4 = MathHelper.sin(-f2 * 0.017453292F - (float)Math.PI);
		float f5 = -MathHelper.cos(-f1 * 0.017453292F);
		float f6 = MathHelper.sin(-f1 * 0.017453292F);
		float f7 = f4 * f5;
		float f8 = f3 * f5;
		double d3 = 32.0D;
		Vec3d distanced = lookVec.addVector((double)f7 * d3, (double)f6 * d3, (double)f8 * d3);
		RayTraceResult mop = p.getEntityWorld().rayTraceBlocks(lookVec, distanced, true, false, false);
		if(mop != null && mop.typeOfHit == Type.BLOCK)
		{
			if((ECUtils.tryToDecreaseMRUInStorage(p, -10000) || this.setMRU(stk, -10000)))
			{
				EntityOrbitalStrike eos = new EntityOrbitalStrike(w,mop.getBlockPos().getX()+0.5D,mop.getBlockPos().getY()+1,mop.getBlockPos().getZ()+0.5D,128,3,p);

				if(!w.isRemote)
					w.spawnEntity(eos);
			}
		}

		return new ActionResult(EnumActionResult.PASS,stk);
	}

	@Override
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation("essentialcraft:item/orbitalRemote", "inventory"));
	}
}
