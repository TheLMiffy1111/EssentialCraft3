package ec3.client.model;

import java.awt.Color;
import java.util.List;

import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.ImmutableList;
import com.google.common.primitives.Ints;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.block.model.ItemTransformVec3f;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.common.model.TRSRTransformation;

public class ModelGun implements IPerspectiveAwareModel {

	static final float DISTANCE_BEHIND_ITEM_FACE = 0.46875F;
	static final float DELTA_FOR_OVERLAP = 6.25E-5F;

	private IBakedModel baseModel;

	private ResourceLocation base;
	private ResourceLocation handle;
	private ResourceLocation device;
	private ResourceLocation scope;
	private ResourceLocation lense;

	public ModelGun(IBakedModel baseModel) {
		this(baseModel, null, null, null, null, null);
	}

	public ModelGun(IBakedModel baseModel, ResourceLocation base, ResourceLocation handle, ResourceLocation device, ResourceLocation scope, ResourceLocation lense) {
		this.baseModel = baseModel;
		this.base = base;
		this.handle = handle;
		this.device = device;
		this.scope = scope;
		this.lense = lense;
		if(this.base == null)
			this.base = new ResourceLocation("essentialcraft:items/null");
		if(this.handle == null)
			this.handle = new ResourceLocation("essentialcraft:items/null");
		if(this.device == null)
			this.device = new ResourceLocation("essentialcraft:items/null");
		if(this.scope == null)
			this.scope = new ResourceLocation("essentialcraft:items/null");
		if(this.lense == null)
			this.lense = new ResourceLocation("essentialcraft:items/null");
	}

	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
		BakedQuad baseNorth = createBakedQuadForFace(0.5F,1,0.5F,1,-DISTANCE_BEHIND_ITEM_FACE+(DELTA_FOR_OVERLAP),0,
				Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(base.toString()),EnumFacing.NORTH);
		BakedQuad baseSouth = createBakedQuadForFace(0.5F,1,0.5F,1,-DISTANCE_BEHIND_ITEM_FACE+(DELTA_FOR_OVERLAP),0,
				Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(base.toString()),EnumFacing.SOUTH);
		BakedQuad handleNorth = createBakedQuadForFace(0.5F,1,0.5F,1,-DISTANCE_BEHIND_ITEM_FACE+(DELTA_FOR_OVERLAP*2),0,
				Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(handle.toString()),EnumFacing.NORTH);
		BakedQuad handleSouth = createBakedQuadForFace(0.5F,1,0.5F,1,-DISTANCE_BEHIND_ITEM_FACE+(DELTA_FOR_OVERLAP*2),0,
				Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(handle.toString()),EnumFacing.SOUTH);
		BakedQuad deviceNorth = createBakedQuadForFace(0.5F,1,0.5F,1,-DISTANCE_BEHIND_ITEM_FACE+(DELTA_FOR_OVERLAP*3),0,
				Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(device.toString()),EnumFacing.NORTH);
		BakedQuad deviceSouth = createBakedQuadForFace(0.5F,1,0.5F,1,-DISTANCE_BEHIND_ITEM_FACE+(DELTA_FOR_OVERLAP*3),0,
				Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(device.toString()),EnumFacing.SOUTH);
		BakedQuad scopeNorth = createBakedQuadForFace(0.5F,1,0.5F,1,-DISTANCE_BEHIND_ITEM_FACE+(DELTA_FOR_OVERLAP*4),0,
				Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(scope.toString()),EnumFacing.NORTH);
		BakedQuad scopeSouth = createBakedQuadForFace(0.5F,1,0.5F,1,-DISTANCE_BEHIND_ITEM_FACE+(DELTA_FOR_OVERLAP*4),0,
				Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(scope.toString()),EnumFacing.SOUTH);
		BakedQuad lenseNorth = createBakedQuadForFace(0.5F,1,0.5F,1,-DISTANCE_BEHIND_ITEM_FACE+(DELTA_FOR_OVERLAP*5),0,
				Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(lense.toString()),EnumFacing.NORTH);
		BakedQuad lenseSouth = createBakedQuadForFace(0.5F,1,0.5F,1,-DISTANCE_BEHIND_ITEM_FACE+(DELTA_FOR_OVERLAP*5),0,
				Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(lense.toString()),EnumFacing.SOUTH);

		ImmutableList.Builder<BakedQuad> builder = ImmutableList.<BakedQuad>builder();
		builder.addAll(baseModel.getQuads(state, side, rand));
		builder.add(baseNorth,baseSouth,handleNorth,handleSouth,deviceNorth,deviceSouth,scopeNorth,scopeSouth,lenseNorth,lenseSouth);
		return builder.build();
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
		return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(base.toString());
	}

	@Override
	public ItemCameraTransforms getItemCameraTransforms() {
		return baseModel.getItemCameraTransforms();
	}

	@Override
	public ItemOverrideList getOverrides() {
		return ItemOverrideList.NONE;
	}

	@Override
	public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType cameraTransformType) {
		if(baseModel instanceof IPerspectiveAwareModel) {
			Matrix4f matrix4f = ((IPerspectiveAwareModel)baseModel).handlePerspective(cameraTransformType).getRight();
			if(cameraTransformType == TransformType.GUI) {
				BakedQuad overlay0 = createBakedQuadForFace(15/16,2/16,8/16,14/16,-DISTANCE_BEHIND_ITEM_FACE+(DELTA_FOR_OVERLAP),0,
						Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("essentialcraft:specia"),EnumFacing.SOUTH);
				BakedQuad overlay1;
				BakedQuad overlay2;
			}
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

	private BakedQuad createBakedQuadForFace(float centreLR, float width, float centreUD, float height, float forwardDisplacement, int itemRenderLayer, TextureAtlasSprite texture, EnumFacing face) {
		float x1, x2, x3, x4;
		float y1, y2, y3, y4;
		float z1, z2, z3, z4;

		switch(face) {
		case UP:
			x1 = x2 = centreLR + width/2.0F;
			x3 = x4 = centreLR - width/2.0F;
			z1 = z4 = centreUD + height/2.0F;
			z2 = z3 = centreUD - height/2.0F;
			y1 = y2 = y3 = y4 = 1 + forwardDisplacement;
			break;
		case DOWN:
			x1 = x2 = centreLR + width/2.0F;
			x3 = x4 = centreLR - width/2.0F;
			z1 = z4 = centreUD - height/2.0F;
			z2 = z3 = centreUD + height/2.0F;
			y1 = y2 = y3 = y4 = 0 - forwardDisplacement;
			break;
		case WEST:
			z1 = z2 = centreLR + width/2.0F;
			z3 = z4 = centreLR - width/2.0F;
			y1 = y4 = centreUD - height/2.0F;
			y2 = y3 = centreUD + height/2.0F;
			x1 = x2 = x3 = x4 = 0 - forwardDisplacement;
			break;
		case EAST:
			z1 = z2 = centreLR - width/2.0F;
			z3 = z4 = centreLR + width/2.0F;
			y1 = y4 = centreUD - height/2.0F;
			y2 = y3 = centreUD + height/2.0F;
			x1 = x2 = x3 = x4 = 1 + forwardDisplacement;
			break;
		case NORTH:
			x1 = x2 = centreLR + width/2.0F;
			x3 = x4 = centreLR - width/2.0F;
			y1 = y4 = centreUD - height/2.0F;
			y2 = y3 = centreUD + height/2.0F;
			z1 = z2 = z3 = z4 = 0 - forwardDisplacement;
			break;
		case SOUTH:
			x1 = x2 = centreLR + width/2.0F;
			x3 = x4 = centreLR - width/2.0F;
			y1 = y4 = centreUD - height/2.0F;
			y2 = y3 = centreUD + height/2.0F;
			z1 = z2 = z3 = z4 = 1 + forwardDisplacement;
			break;
		default:
			return null;
		}

		return new BakedQuad(Ints.concat(vertexToInts(x1, y1, z1, Color.WHITE.getRGB(), texture, 16, 16),
				vertexToInts(x2, y2, z2, Color.WHITE.getRGB(), texture, 16, 0),
				vertexToInts(x3, y3, z3, Color.WHITE.getRGB(), texture, 0, 0),
				vertexToInts(x4, y4, z4, Color.WHITE.getRGB(), texture, 0, 16)),
				itemRenderLayer, face, texture, true, net.minecraft.client.renderer.vertex.DefaultVertexFormats.ITEM);
	}

	private int[] vertexToInts(float x, float y, float z, int color, TextureAtlasSprite texture, float u, float v) {
		return new int[] {
				Float.floatToRawIntBits(x),
				Float.floatToRawIntBits(y),
				Float.floatToRawIntBits(z),
				color,
				Float.floatToRawIntBits(texture.getInterpolatedU(u)),
				Float.floatToRawIntBits(texture.getInterpolatedV(v)),
				0
		};
	}
}
