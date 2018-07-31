package essentialcraft.client.model;

import java.util.List;
import java.util.Random;

import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.ImmutableList;

import DummyCore.Utils.MiscUtils;
import essentialcraft.api.GunRegistry;
import essentialcraft.api.GunRegistry.GunMaterial;
import essentialcraft.api.GunRegistry.LenseMaterial;
import essentialcraft.api.GunRegistry.ScopeMaterial;
import essentialcraft.common.item.ItemGun;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemOverride;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class ModelGunHandler implements IBakedModel {

	public static IBakedModel blankItem;

	private final IBakedModel baseModel;

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
	public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType cameraTransformType) {
		Matrix4f matrix4f = baseModel.handlePerspective(cameraTransformType).getRight();
		return Pair.of(this, matrix4f);
	}

	@Override
	public ItemOverrideList getOverrides() {
		return ModelGunOverrideHandler.INSTANCE;
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
					base = GunRegistry.GUN_MATERIALS.get(rnd.nextInt(GunRegistry.GUN_MATERIALS.size()));
					handle = GunRegistry.GUN_MATERIALS.get(rnd.nextInt(GunRegistry.GUN_MATERIALS.size()));
					device = GunRegistry.GUN_MATERIALS.get(rnd.nextInt(GunRegistry.GUN_MATERIALS.size()));
					if(iGun.gunType.equalsIgnoreCase("sniper"))
						scope = GunRegistry.SCOPE_MATERIALS_SNIPER.get(rnd.nextInt(GunRegistry.SCOPE_MATERIALS_SNIPER.size()));
					else if(!iGun.gunType.equalsIgnoreCase("gatling"))
						scope = GunRegistry.SCOPE_MATERIALS.get(rnd.nextInt(GunRegistry.SCOPE_MATERIALS.size()));
					lense = GunRegistry.LENSE_MATERIALS.get(rnd.nextInt(GunRegistry.LENSE_MATERIALS.size()));
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
