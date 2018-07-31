package essentialcraft.client.render.entity;

import org.lwjgl.opengl.GL11;

import DummyCore.Utils.DrawUtils;
import essentialcraft.common.entity.EntityHologram;
import essentialcraft.common.item.ItemsCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderHologram extends RenderBiped<EntityHologram>
{
	private static final ResourceLocation textures = new ResourceLocation("essentialcraft","textures/entities/boss.png");
	/** The model of the enderman */
	private ModelBiped model;
	public RenderHologram()
	{
		super(Minecraft.getMinecraft().getRenderManager(), new ModelBiped(1,0,64,32), 0.5F);
		this.model = (ModelBiped)super.mainModel;
	}

	public RenderHologram(RenderManager rm)
	{
		super(rm, new ModelBiped(0,0,64,32), 0.5F);
		this.model = (ModelBiped)super.mainModel;
	}

	@Override
	protected void preRenderCallback(EntityHologram entity, float partialTicks)  {
		float s = 1.0F;
		GlStateManager.scale(s, s, s);

		GlStateManager.disableAlpha();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_ALPHA);

		GlStateManager.color(1, 1, 1, 0.2F);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityHologram entity) {
		return textures;
	}

	@Override
	public void doRender(EntityHologram h, double x, double y, double z, float entityYaw, float partialTicks) {
		super.doRender(h, x, y, z, entityYaw, partialTicks);

		int meta = 76;

		if(h.attackID == -1)
			meta = 76;

		if(h.attackID == 0)
			meta = 70;

		if(h.attackID == 1)
			meta = 73;

		if(h.attackID == 2)
			meta = 72;

		if(h.attackID == 3)
			meta = 71;

		DrawUtils.renderItemStack_Full(new ItemStack(ItemsCore.genericItem,1,meta), x, y, z, (h.ticksExisted+partialTicks)%360, 0, 1, 1, 1, 0, 2.4F, 0);
	}

	public static class Factory implements IRenderFactory<EntityHologram> {
		@Override
		public Render<? super EntityHologram> createRenderFor(RenderManager manager) {
			return new RenderHologram(manager);
		}
	}
}