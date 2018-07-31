package essentialcraft.common.block;

import java.util.List;

import DummyCore.Client.IModelRegisterer;
import DummyCore.Client.ModelUtils;
import essentialcraft.api.GunRegistry.GunType;
import essentialcraft.common.item.ItemsCore;
import essentialcraft.common.mod.EssentialCraftCore;
import essentialcraft.common.tile.TileWeaponMaker;
import essentialcraft.utils.cfg.Config;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockWeaponMaker extends BlockContainer implements IModelRegisterer {

	public static final PropertyEnum<GunType> TYPE = PropertyEnum.<GunType>create("type", GunType.class);

	public BlockWeaponMaker() {
		super(Material.ROCK);
		setDefaultState(blockState.getBaseState().withProperty(TYPE, GunType.PISTOL));
	}

	@Override
	public int damageDropped(IBlockState state)
	{
		return state.getValue(TYPE).getIndex();
	}

	@Override
	public boolean isOpaqueCube(IBlockState s)
	{
		return false;
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		return new ItemStack(this,1,state.getValue(TYPE).getIndex());
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState blockstate) {
		IInventory inv = (IInventory)world.getTileEntity(pos);
		InventoryHelper.dropInventoryItems(world, pos, inv);
		super.breakBlock(world, pos, blockstate);
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState s)
	{
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int metadata) {
		return new TileWeaponMaker();
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos par2, IBlockState par3, EntityPlayer player, EnumHand par5, EnumFacing par7, float par8, float par9, float par10) {
		if(player.isSneaking()) {
			return false;
		}
		if(!world.isRemote) {
			player.openGui(EssentialCraftCore.core, Config.guiID[0], world, par2.getX(), par2.getY(), par2.getZ());
			return true;
		}
		return true;
	}

	@Override
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list) {
		for(int i = 0; i < 4; i++) {
			list.add(new ItemStack(this,1,i));
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World player, List<String> list, ITooltipFlag par4)
	{
		switch(stack.getItemDamage()) {
		case 0: {
			list.add(new ItemStack(ItemsCore.pistol).getDisplayName());
			break;
		}
		case 1: {
			list.add(new ItemStack(ItemsCore.rifle).getDisplayName());
			break;
		}
		case 2: {
			list.add(new ItemStack(ItemsCore.sniper).getDisplayName());
			break;
		}
		case 3: {
			list.add(new ItemStack(ItemsCore.gatling).getDisplayName());
			break;
		}
		}
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(TYPE, GunType.fromIndex(meta%4));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(TYPE).getIndex();
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, TYPE);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels() {
		ModelLoader.setCustomStateMapper(this, new StateMap.Builder().ignore(TYPE).build());
		ModelLoader.setCustomMeshDefinition(Item.getItemFromBlock(this), new ModelUtils.MeshDefinitionSingleIcon("essentialcraft:weaponmaker"));
		ModelBakery.registerItemVariants(Item.getItemFromBlock(this), new ModelResourceLocation("essentialcraft:weaponmaker", "inventory"));
	}
}
