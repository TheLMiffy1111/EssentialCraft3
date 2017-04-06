package ec3.common.world.event;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import DummyCore.Utils.MathUtils;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import ec3.api.IWorldEvent;
import ec3.common.item.BaublesModifier;
import ec3.utils.cfg.Config;
import ec3.utils.common.ECUtils;

public class WorldEvent_SunArray implements IWorldEvent{

	@Override
	public void onEventBeginning(World w) {
		ECUtils.sendChatMessageToAllPlayersInDim(Config.dimensionID, TextFormatting.RED+"This is going to be a hot day...");
	}

	@Override
	public void worldTick(World w, int leftoverTime) {
		
	}

	@Override
	public void playerTick(EntityPlayer p, int leftoverTime) {
		boolean ignoreSun = false;
    	IBaublesItemHandler b = BaublesApi.getBaublesHandler(p);
    	if(b != null)
    	{
    		for(int i = 0; i < b.getSlots(); ++i)
    		{
    			ItemStack is = b.getStackInSlot(i);
    			if(is != null && is.getItem() != null && is.getItem() instanceof BaublesModifier && is.getItemDamage() == 19)
    				ignoreSun = true;
    		}
    	}
		if(!p.capabilities.isCreativeMode && p.dimension == Config.dimensionID && p.getEntityWorld().canBlockSeeSky(new BlockPos(MathHelper.floor(p.posX), MathHelper.floor(p.posY+2), MathHelper.floor(p.posZ))) && !ignoreSun)
		{
			p.attackEntityFrom(DamageSource.onFire, 1);
			p.setFire(10);
		}
		if(p.dimension == Config.dimensionID)
		{
			EntityFallingBlock sand = new EntityFallingBlock(p.getEntityWorld(), Math.floor(p.posX+MathUtils.randomDouble(p.getEntityWorld().rand)*128)+0.5D, 255, Math.floor(p.posZ+MathUtils.randomDouble(p.getEntityWorld().rand)*128)+0.5D, Blocks.FIRE.getDefaultState());
			sand.fallTime = 3;
			sand.shouldDropItem = false;
			if(!p.getEntityWorld().isRemote)
				p.getEntityWorld().spawnEntity(sand);
		}
		
	}

	@Override
	public void onEventEnd(World w) {
		ECUtils.sendChatMessageToAllPlayersInDim(Config.dimensionID, TextFormatting.GREEN+"The suns are back to normal!");
	}

	@Override
	public int getEventDuration(World w) {
		return 12000;
	}

	@Override
	public boolean possibleToApply(World w) {
		return w.getWorldTime() % 24000L == 0;
	}

	@Override
	public float getEventProbability(World w) {
		return 0.3F;
	}

	@Override
	public String getEventID() {
		return "ec3.event.sunArray";
	}

}
