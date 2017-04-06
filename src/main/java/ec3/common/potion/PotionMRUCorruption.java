package ec3.common.potion;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import ec3.common.item.BaublesModifier;
import ec3.utils.common.ECUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;

public class PotionMRUCorruption extends Potion {

	public PotionMRUCorruption(int p_i1573_1_, boolean p_i1573_2_,int p_i1573_3_) 
	{
		super(p_i1573_2_, p_i1573_3_);
		this.setIconIndex(3, 1);
		this.setEffectiveness(0.25D);
		this.setPotionName("potion.mruCorruption");
		this.setRegistryName("essentialcraft", "potion.mruCorruption");
	}
	
    public boolean isUsable()
    {
        return true;
    }
	
    public void performEffect(EntityLivingBase p_76394_1_, int p_76394_2_)
    {
    	if(!p_76394_1_.getEntityWorld().isRemote && p_76394_1_.getEntityWorld().rand.nextInt(16) < p_76394_2_)
    	{
    		float damIndex = 5;
    		if(p_76394_1_ instanceof EntityPlayer)
    		{
    			damIndex *= ECUtils.getGenResistance(0, (EntityPlayer) p_76394_1_);
				boolean heal = false;
	        	IBaublesItemHandler b = BaublesApi.getBaublesHandler((EntityPlayer) p_76394_1_);
	        	if(b != null)
	        	{
	        		for(int i = 0; i < b.getSlots(); ++i)
	        		{
	        			ItemStack is = b.getStackInSlot(i);
	        			if(is != null && is.getItem() != null && is.getItem() instanceof BaublesModifier && is.getItemDamage() == 10)
	        				heal = true;
	        		}
	        	}
	        	if(!heal)
	        		p_76394_1_.attackEntityFrom(DamageSource.magic, damIndex);
	        	else
	        		p_76394_1_.heal(damIndex);
    		}
    	}
    }
    
    public boolean isReady(int p_76397_1_, int p_76397_2_)
    {
    	return p_76397_1_ % 20 == 0;
    }
    
    public boolean hasStatusIcon()
    {
        return true;
    }
    
    public int getStatusIconIndex()
    {
    	Minecraft.getMinecraft().renderEngine.bindTexture(rl);
        return super.getStatusIconIndex();
    }
    
    static final ResourceLocation rl = new ResourceLocation("essentialcraft", "textures/special/potions.png");
}
