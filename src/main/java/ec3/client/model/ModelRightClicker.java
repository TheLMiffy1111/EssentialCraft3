package ec3.client.model;

import java.util.List;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableList;

import ec3.common.block.BlockRightClicker;
import ec3.common.block.BlocksCore;
import ec3.common.tile.TileRightClicker;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.property.IExtendedBlockState;

public class ModelRightClicker implements IBakedModel {

	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
		if(!(state.getBlock() instanceof BlockRightClicker))
			return Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelManager().getMissingModel().getQuads(state, side, rand);

		BlockRenderLayer layer = MinecraftForgeClient.getRenderLayer();
		IBlockState heldState = ((IExtendedBlockState)state).getValue(BlockRightClicker.STATE);
		IBlockAccess heldWorld = ((IExtendedBlockState)state).getValue(BlockRightClicker.WORLD);
		BlockPos heldPos = ((IExtendedBlockState)state).getValue(BlockRightClicker.POS);

		if(heldWorld == null || heldPos == null) {
			return ImmutableList.<BakedQuad>of();
		}

		Minecraft mc = Minecraft.getMinecraft();
		if(heldState == null && layer == BlockRenderLayer.SOLID) {
			ModelResourceLocation path = new ModelResourceLocation("essentialcraft:rightClicker", "facing=" + state.getValue(BlockRightClicker.FACING).getName() + "," + "type=" + state.getValue(BlockRightClicker.TYPE).getName());
			return mc.getBlockRendererDispatcher().getBlockModelShapes().getModelManager().getModel(path).getQuads(state, side, rand);
		}
		else if(heldState != null) {
			if(heldState.getBlock().canRenderInLayer(heldState, layer)) {
				IBlockState actual = heldState.getBlock().getActualState(heldState, new FakeBlockAccess(heldWorld), heldPos);

				// Steal camo's model
				IBakedModel model = mc.getBlockRendererDispatcher().getBlockModelShapes().getModelForState(actual);

				// Their model can be smart too
				IBlockState extended = heldState.getBlock().getExtendedState(actual, new FakeBlockAccess(heldWorld), heldPos);
				return model.getQuads(extended, side, rand);
			}
		}

		return ImmutableList.<BakedQuad>of(); // Nothing renders
	}

	@Override
	public boolean isAmbientOcclusion() {
		return true;
	}

	@Override
	public boolean isGui3d() {
		return true;
	}

	@Override
	public boolean isBuiltInRenderer() {
		return false;
	}

	@Override
	public TextureAtlasSprite getParticleTexture() {
		return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("essentialcraft:blocks/fortifiedStone");
	}

	@Override
	public ItemCameraTransforms getItemCameraTransforms() {
		return ItemCameraTransforms.DEFAULT;
	}

	@Override
	public ItemOverrideList getOverrides() {
		return ItemOverrideList.NONE;
	}

	private static class FakeBlockAccess implements IBlockAccess {

		private final IBlockAccess compose;

		private FakeBlockAccess(IBlockAccess compose) {
			this.compose = compose;
		}

		@Override
		public TileEntity getTileEntity(BlockPos pos) {
			return compose.getTileEntity(pos);
		}

		@Override
		public int getCombinedLight(BlockPos pos, int lightValue) {
			return 15 << 20 | 15 << 4;
		}

		@Override
		public IBlockState getBlockState(BlockPos pos) {
			IBlockState state = compose.getBlockState(pos);
			if(state.getBlock() instanceof BlockRightClicker) {
				state = ((TileRightClicker)compose.getTileEntity(pos)).getState();
			}
			return state == null ? Blocks.AIR.getDefaultState() : state;
		}

		@Override
		public boolean isAirBlock(BlockPos pos) {
			return compose.isAirBlock(pos);
		}

		@Override
		public Biome getBiome(BlockPos pos) {
			return compose.getBiome(pos);
		}

		@Override
		public int getStrongPower(BlockPos pos, EnumFacing direction) {
			return compose.getStrongPower(pos, direction);
		}

		@Override
		public WorldType getWorldType() {
			return compose.getWorldType();
		}

		@Override
		public boolean isSideSolid(BlockPos pos, EnumFacing side, boolean _default) {
			return compose.isSideSolid(pos, side, _default);
		}
	}
}
