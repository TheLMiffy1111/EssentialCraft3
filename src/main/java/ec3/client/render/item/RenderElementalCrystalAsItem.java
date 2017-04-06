package ec3.client.render.item;

import java.util.List;

import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.ImmutableList;

import DummyCore.Utils.MathUtils;
import DummyCore.Utils.MiscUtils;
import codechicken.lib.render.item.IItemRenderer;
import codechicken.lib.util.TransformUtils;
import ec3.client.model.ModelElementalCrystal;
import ec3.common.item.ItemBlockElementalCrystal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.client.model.IPerspectiveAwareModel.MapWrapper;

public class RenderElementalCrystalAsItem implements IItemRenderer, IPerspectiveAwareModel {

	public static final ResourceLocation textures = new ResourceLocation("essentialcraft:textures/models/MCrystalTex.png");
	public static final ResourceLocation neutral = new ResourceLocation("essentialcraft:textures/models/MCrystalTex.png");
	public static final ResourceLocation fire = new ResourceLocation("essentialcraft:textures/models/FCrystalTex.png");
	public static final ResourceLocation water = new ResourceLocation("essentialcraft:textures/models/WCrystalTex.png");
	public static final ResourceLocation earth = new ResourceLocation("essentialcraft:textures/models/ECrystalTex.png");
	public static final ResourceLocation air = new ResourceLocation("essentialcraft:textures/models/ACrystalTex.png");
	public static final ModelElementalCrystal crystal = new ModelElementalCrystal();

	@Override
	public void renderItem(ItemStack item) {
		NBTTagCompound tag = MiscUtils.getStackTag(item);
		float size = 100F;
		float fireF = 0F;
		float waterF = 0F;
		float earthF = 0F;
		float airF = 0F;

		if(tag.hasKey("size"))
			size = tag.getFloat("size");

		if(tag.hasKey("fire"))
			fireF = tag.getFloat("fire");
		if(tag.hasKey("water"))
			waterF = tag.getFloat("water");
		if(tag.hasKey("earth"))
			earthF = tag.getFloat("earth");
		if(tag.hasKey("air"))
			airF = tag.getFloat("air");

		GlStateManager.pushMatrix();
		float scale = MathUtils.getPercentage((int) size, 100)/100F;
		GlStateManager.translate(0.5F, 1.7F-(1.0F-scale)*1.4F, 0.5F);

		GlStateManager.scale(scale, scale, scale);
		GlStateManager.rotate(180, 1, 0, 0);
		Minecraft.getMinecraft().renderEngine.bindTexture(textures);
		GlStateManager.disableLighting();
		GlStateManager.disableCull();
		GlStateManager.disableBlend();
		GlStateManager.color(1, 1, 1, 0.5F);
		GlStateManager.enableBlend();
		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		crystal.renderModel(0.0625F);

		Minecraft.getMinecraft().renderEngine.bindTexture(fire);
		GlStateManager.color(1, 1, 1, fireF/100F);
		crystal.renderModel(0.0625F);

		Minecraft.getMinecraft().renderEngine.bindTexture(water);
		GlStateManager.color(1, 1, 1, waterF/100F);
		crystal.renderModel(0.0625F);

		Minecraft.getMinecraft().renderEngine.bindTexture(earth);
		GlStateManager.color(1, 1, 1, earthF/100F);
		crystal.renderModel(0.0625F);

		Minecraft.getMinecraft().renderEngine.bindTexture(air);
		GlStateManager.color(1, 1, 1, airF/100F);
		crystal.renderModel(0.0625F);

		GlStateManager.enableLighting();
		GlStateManager.popMatrix();
	}

	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
		return ImmutableList.<BakedQuad>of();
	}

	@Override
	public boolean isAmbientOcclusion() {
		return false;
	}

	@Override
	public boolean isGui3d() {
		return false;
	}

	@Override
	public boolean isBuiltInRenderer() {
		return true;
	}

	@Override
	public TextureAtlasSprite getParticleTexture() {
		return null;
	}

	@Override
	public ItemCameraTransforms getItemCameraTransforms() {
		return ItemCameraTransforms.DEFAULT;
	}

	@Override
	public ItemOverrideList getOverrides() {
		return ItemOverrideList.NONE;
	}

	@Override
	public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType cameraTransformType) {
		return MapWrapper.handlePerspective(this, TransformUtils.DEFAULT_BLOCK.getTransforms(), cameraTransformType);
	}
}
