package ec3.common.potion;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;

public class PotionPurpleFlame extends Potion {

	public PotionPurpleFlame(int p_i1573_1_, boolean p_i1573_2_,int p_i1573_3_) 
	{
		super(p_i1573_2_, p_i1573_3_);
		this.setIconIndex(6, 2);
		this.setEffectiveness(0.25D);
		this.setPotionName("potion.purpleFlame");
		this.setRegistryName("essentialcraft", "potion.purpleFlame");
	}

	public boolean isUsable()
	{
		return true;
	}

	public void performEffect(EntityLivingBase p_76394_1_, int p_76394_2_)
	{
		if(!p_76394_1_.getEntityWorld().isRemote && p_76394_1_.getEntityWorld().rand.nextInt(16) < p_76394_2_)
		{
			p_76394_1_.attackEntityFrom(DamageSource.magic, 5);
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
