package ec3.common.potion;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import ec3.common.item.BaublesModifier;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;

public class PotionRadiation extends Potion {

	public PotionRadiation(int p_i1573_1_, boolean p_i1573_2_,int p_i1573_3_) 
	{
		super(p_i1573_2_, p_i1573_3_);
		this.setIconIndex(4, 2);
		this.setEffectiveness(0.25D);
		this.setPotionName("potion.radiation");
		this.setRegistryName("essentialcraft", "potion.radiation");
	}

	public boolean isUsable()
	{
		return true;
	}

	public void performEffect(EntityLivingBase p_76394_1_, int p_76394_2_)
	{
		if(!p_76394_1_.getEntityWorld().isRemote && p_76394_1_.getEntityWorld().rand.nextInt(16) < p_76394_2_)
		{
			boolean divide = false;	
			if(p_76394_1_ instanceof EntityPlayer) {
				IBaublesItemHandler b = BaublesApi.getBaublesHandler((EntityPlayer) p_76394_1_);
				if(b != null)
				{
					for(int i = 0; i < b.getSlots(); ++i)
					{
						ItemStack is = b.getStackInSlot(i);
						if(is != null && is.getItem() != null && is.getItem() instanceof BaublesModifier && is.getItemDamage() == 11)
							divide = true;
					}
				}
			}

			int amplifier = p_76394_2_;
			if(divide)
				amplifier/=2;
			int maxHealth = (int) p_76394_1_.getMaxHealth()-(amplifier+1);
			float currentHealth = p_76394_1_.getHealth();
			if(maxHealth < 1)
				maxHealth = 1;
			if(currentHealth > maxHealth)
			{
				p_76394_1_.setHealth(maxHealth);
			}

		}
	}

	public boolean isReady(int p_76397_1_, int p_76397_2_)
	{
		return true;
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
