package ec3.common.potion;

import DummyCore.Utils.MiscUtils;
import ec3.common.registry.PotionRegistry;
import ec3.common.registry.SoundRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;

public class PotionMindfoldParadox extends Potion {

	public PotionMindfoldParadox(int p_i1573_1_, boolean p_i1573_2_,int p_i1573_3_) 
	{
		super(p_i1573_2_, p_i1573_3_);
		this.setIconIndex(7, 2);
		this.setPotionName("potion.paradox");
		this.setRegistryName("essentialcraft", "potion.paradox");
	}

	public boolean isUsable()
	{
		return true;
	}

	public void performEffect(EntityLivingBase p_76394_1_, int p_76394_2_)
	{
		int duration = p_76394_1_.getActivePotionEffect(PotionRegistry.paradox).getDuration();
		if(duration == 2000)
		{
			if(p_76394_1_.getEntityWorld().isRemote)
				p_76394_1_.getEntityWorld().playSound(p_76394_1_.posX,p_76394_1_.posY,p_76394_1_.posZ, SoundRegistry.potionTinnitus, SoundCategory.PLAYERS, 100, 1, true);
			MiscUtils.setShaders(2);
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

	public boolean shouldRenderInvText(PotionEffect effect)
	{
		return false;
	}

	/**
	 * Called to draw the this Potion onto the player's inventory when it's active.
	 * This can be used to e.g. render Potion icons from your own texture.
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param effect the active PotionEffect
	 * @param mc the Minecraft instance, for convenience
	 */
	public void renderInventoryEffect(int x, int y, PotionEffect effect, net.minecraft.client.Minecraft mc)
	{ 
		String s1 = I18n.format(effect.getEffectName(), new Object[0]);
		mc.fontRendererObj.drawStringWithShadow(s1, x + 10 + 18, y + 6, 16777215);
		mc.fontRendererObj.drawStringWithShadow(net.minecraft.util.text.translation.I18n.translateToLocal("potion.paradox.txt"), x + 10 + 18, y + 16, 16777215);
	}

	static final ResourceLocation rl = new ResourceLocation("essentialcraft", "textures/special/potions.png");

}
