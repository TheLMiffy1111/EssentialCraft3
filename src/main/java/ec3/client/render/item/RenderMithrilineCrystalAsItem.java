package ec3.client.render.item;

import java.util.List;

import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.ImmutableList;

import codechicken.lib.render.item.IItemRenderer;
import codechicken.lib.util.TransformUtils;
import ec3.client.render.tile.RenderMithrilineCrystal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.IPerspectiveAwareModel;

public class RenderMithrilineCrystalAsItem implements IItemRenderer, IPerspectiveAwareModel {

	@Override
	public void renderItem(ItemStack item) {
		int metadata = item.getItemDamage();
		GlStateManager.pushMatrix();
		GlStateManager.translate(0.5F,-0.5F,0.5F);
		GlStateManager.scale(2, 2, 2);
		Minecraft.getMinecraft().renderEngine.bindTexture(metadata == 0 ? RenderMithrilineCrystal.textures_mithriline : metadata == 3 ? RenderMithrilineCrystal.textures_pale : metadata == 6 ? RenderMithrilineCrystal.textures_void : metadata == 9 ? RenderMithrilineCrystal.textures_demonic : RenderMithrilineCrystal.textures_shade);
		RenderMithrilineCrystal.model.renderAll();
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
