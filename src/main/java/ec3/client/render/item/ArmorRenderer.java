package ec3.client.render.item;

import java.util.List;

import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.ImmutableList;

import DummyCore.Utils.DrawUtils;
import codechicken.lib.render.item.IItemRenderer;
import codechicken.lib.util.TransformUtils;
import ec3.client.model.ModelArmorEC3;
import ec3.common.item.ItemArmorMod;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.IPerspectiveAwareModel;

public class ArmorRenderer implements IItemRenderer, IPerspectiveAwareModel {
	
	public static final ModelArmorEC3 theModel = new ModelArmorEC3(1.0F);

	@Override
	public void renderItem(ItemStack item) {
		if(item.getItem() instanceof ItemArmorMod) {
			ItemArmorMod armor = (ItemArmorMod)item.getItem();
			EntityEquipmentSlot atype = armor.armorType;
			String texture = armor.getArmorTexture(item, null, atype, null);
			int index = texture.indexOf(":");
			if(index != -1) {
				String modName = texture.substring(0, index);
				String tName = texture.substring(index+1);
				DrawUtils.bindTexture(modName, tName);
                ModelBiped modelbiped = theModel;
                modelbiped.bipedHead.showModel = atype == EntityEquipmentSlot.HEAD;
                modelbiped.bipedHeadwear.showModel = atype == EntityEquipmentSlot.HEAD;
                modelbiped.bipedBody.showModel = atype == EntityEquipmentSlot.CHEST || atype == EntityEquipmentSlot.LEGS;
                modelbiped.bipedRightArm.showModel = atype == EntityEquipmentSlot.CHEST;
                modelbiped.bipedLeftArm.showModel = atype == EntityEquipmentSlot.CHEST;
                modelbiped.bipedRightLeg.showModel = atype == EntityEquipmentSlot.LEGS || atype == EntityEquipmentSlot.FEET;
                modelbiped.bipedLeftLeg.showModel = atype == EntityEquipmentSlot.LEGS || atype == EntityEquipmentSlot.FEET;
                if(atype == EntityEquipmentSlot.HEAD)
                	GlStateManager.translate(0.5F, 1.7F, 0.5F);
                if(atype == EntityEquipmentSlot.CHEST)
                	GlStateManager.translate(0.5F, 2.5F, 0.5F);
                if(atype == EntityEquipmentSlot.LEGS)
                	GlStateManager.translate(0.5F, 3.2F, 0.5F);
                if(atype == EntityEquipmentSlot.FEET)
                	GlStateManager.translate(0.5F, 3.2F, 0.5F);
                GlStateManager.rotate(180, 1, 0, 0);
                GlStateManager.scale(2F, 2F, 2F);
                modelbiped.render(Minecraft.getMinecraft().player, 0, 0, 0F, 0F, 0, 0.0625F);
			}
		}
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
