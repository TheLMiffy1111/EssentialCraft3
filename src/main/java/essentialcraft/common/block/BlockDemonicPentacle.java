package essentialcraft.common.block;

import DummyCore.Client.IModelRegisterer;
import essentialcraft.common.entity.EntityDemon;
import essentialcraft.common.tile.TileDemonicPentacle;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

public class BlockDemonicPentacle extends BlockContainer implements IModelRegisterer {

	public static final AxisAlignedBB BLOCK_AABB = new AxisAlignedBB(0,0,0,1,0.0625D,1);

	public BlockDemonicPentacle() {
		super(Material.ROCK, MapColor.AIR);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return BLOCK_AABB;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		return NULL_AABB;
	}

	@Override
	public boolean isOpaqueCube(IBlockState s) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState s) {
		return false;
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState s) {
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileDemonicPentacle();
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState blockstate, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		TileDemonicPentacle pentacle = (TileDemonicPentacle)world.getTileEntity(pos);
		if(pentacle.consumeEnderstarEnergy(666)) {
			EntityDemon demon = new EntityDemon(world);
			demon.setPositionAndRotation(pos.getX()+0.5D, pos.getY()+0.1D, pos.getZ()+0.5D, 0, 0);
			demon.playLivingSound();
			if(!world.isRemote) {
				world.spawnEntity(demon);
			}
		}
		else {
			if(world.isRemote) {
				player.sendMessage(new TextComponentTranslation("essentialcraft.txt.noEnergy").setStyle(new Style().setColor(TextFormatting.RED)));
			}
		}
		return true;
	}

	@Override
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation("essentialcraft:demonicpentacle", "inventory"));
	}
}
