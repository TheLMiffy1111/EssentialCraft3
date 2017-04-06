package ec3.client.model;

import java.util.List;
import java.util.Random;

import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.ImmutableList;

import DummyCore.Utils.MiscUtils;
import ec3.api.GunRegistry;
import ec3.api.GunRegistry.GunMaterial;
import ec3.api.GunRegistry.LenseMaterial;
import ec3.api.GunRegistry.ScopeMaterial;
import ec3.common.item.ItemGun;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemOverride;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.block.model.ItemTransformVec3f;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.common.model.TRSRTransformation;

public class ModelGunHandler implements IPerspectiveAwareModel {

	public static IBakedModel blankItem;
	
	private IBakedModel baseModel;

	public ModelGunHandler(IBakedModel baseModel) {
		this.baseModel = baseModel;
	}

	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
		return baseModel.getQuads(state, side, rand);
	}

	@Override
	public boolean isAmbientOcclusion() {
		return baseModel.isAmbientOcclusion();
	}

	@Override
	public boolean isGui3d() {
		return baseModel.isGui3d();
	}

	@Override
	public boolean isBuiltInRenderer() {
		return baseModel.isBuiltInRenderer();
	}

	@Override
	public TextureAtlasSprite getParticleTexture() {
		return baseModel.getParticleTexture();
	}

	@Override
	public ItemCameraTransforms getItemCameraTransforms() {
		return baseModel.getItemCameraTransforms();
	}

	@Override
	public ItemOverrideList getOverrides() {
		return ModelGunOverrideHandler.INSTANCE;
	}

	@Override
	public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType cameraTransformType) {
		if(baseModel instanceof IPerspectiveAwareModel) {
			Matrix4f matrix4f = ((IPerspectiveAwareModel)baseModel).handlePerspective(cameraTransformType).getRight();
			return Pair.of(this, matrix4f);
		}
		else {
			ItemCameraTransforms itemCameraTransforms = baseModel.getItemCameraTransforms();
			ItemTransformVec3f itemTransformVec3f = itemCameraTransforms.getTransform(cameraTransformType);
			TRSRTransformation tr = new TRSRTransformation(itemTransformVec3f);
			Matrix4f mat = null;
			if (tr != null) {
				mat = tr.getMatrix();
			}

			return Pair.of(this, mat);
		}
	}

	public static class ModelGunOverrideHandler extends ItemOverrideList {

		public static final ModelGunOverrideHandler INSTANCE = new ModelGunOverrideHandler();

		public ModelGunOverrideHandler() {
			super(ImmutableList.<ItemOverride>of());
		}

		@Override
		public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, World world, EntityLivingBase entity) {
			if(stack.getItem() instanceof ItemGun) {
				NBTTagCompound tag = MiscUtils.getStackTag(stack);
				GunMaterial base = null;
				GunMaterial handle = null;
				GunMaterial device = null;
				ScopeMaterial scope = null;
				LenseMaterial lense = null;
				ItemGun iGun = (ItemGun)stack.getItem();

				if(!stack.hasTagCompound() || !stack.getTagCompound().hasKey("base")) {
					Random rnd = new Random(System.currentTimeMillis()/1000);
					base = GunRegistry.gunMaterials.get(rnd.nextInt(GunRegistry.gunMaterials.size()));
					handle = GunRegistry.gunMaterials.get(rnd.nextInt(GunRegistry.gunMaterials.size()));
					device = GunRegistry.gunMaterials.get(rnd.nextInt(GunRegistry.gunMaterials.size()));
					if(iGun.gunType.equalsIgnoreCase("sniper"))
						scope = GunRegistry.scopeMaterialsSniper.get(rnd.nextInt(GunRegistry.scopeMaterialsSniper.size()));
					else if(!iGun.gunType.equalsIgnoreCase("gatling"))
						scope = GunRegistry.scopeMaterials.get(rnd.nextInt(GunRegistry.scopeMaterials.size()));
					lense = GunRegistry.lenseMaterials.get(rnd.nextInt(GunRegistry.lenseMaterials.size()));
				}
				else {
					base = GunRegistry.getGunFromID(tag.getString("base"));
					handle = GunRegistry.getGunFromID(tag.getString("handle"));
					device = GunRegistry.getGunFromID(tag.getString("device"));
					if(iGun.gunType.equalsIgnoreCase("sniper"))
						scope = GunRegistry.getScopeSniperFromID(tag.getString("scope"));
					else
						scope = GunRegistry.getScopeFromID(tag.getString("scope"));
					lense = GunRegistry.getLenseFromID(tag.getString("lense"));
				}

				if(entity != null && entity.isSneaking() && tag.hasKey("scope") && iGun.gunType.equalsIgnoreCase("sniper")) {
					return new ModelGun(blankItem);
				}
				else {
					return new ModelGun(
							originalModel,
							base == null ? null : new ResourceLocation(base.baseTextures.get(iGun.gunType)),
							handle == null ? null : new ResourceLocation(handle.handleTextures.get(iGun.gunType)),
							device == null ? null : new ResourceLocation(device.deviceTextures.get(iGun.gunType)),
							scope == null ? null : new ResourceLocation(scope.textures.get(iGun.gunType)),
							lense == null ? null : new ResourceLocation(lense.textures.get(iGun.gunType))
							);
				}
			}
			return originalModel;
		}
	}
}
