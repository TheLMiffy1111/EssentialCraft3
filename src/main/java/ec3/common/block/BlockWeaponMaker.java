package ec3.common.block;

import java.util.List;

import DummyCore.Client.IModelRegisterer;
import DummyCore.Client.ModelUtils;
import DummyCore.Utils.MiscUtils;
import ec3.api.GunRegistry.GunType;
import ec3.common.item.ItemsCore;
import ec3.common.mod.EssentialCraftCore;
import ec3.common.tile.TileWeaponMaker;
import ec3.utils.cfg.Config;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

public class BlockWeaponMaker extends BlockContainer implements IModelRegisterer {

	public static final PropertyEnum<GunType> TYPE = PropertyEnum.<GunType>create("type", GunType.class);
	
	public BlockWeaponMaker() {
		super(Material.ROCK);
		setDefaultState(blockState.getBaseState().withProperty(TYPE, GunType.PISTOL));
	}
	
    public int damageDropped(IBlockState state)
    {
        return state.getValue(TYPE).getIndex();
    }
	
    public boolean isOpaqueCube(IBlockState s)
    {
        return false;
    }

    @Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		return new ItemStack(this,1,state.getValue(TYPE).getIndex());
	}

	@Override
    public void breakBlock(World par1World, BlockPos par2Pos, IBlockState par3State) {
		MiscUtils.dropItemsOnBlockBreak(par1World, par2Pos.getX(), par2Pos.getY(), par2Pos.getZ(), par3State.getBlock(), par3State.getValue(TYPE).getIndex());
		super.breakBlock(par1World, par2Pos, par3State);
    }

	public EnumBlockRenderType getRenderType(IBlockState s)
	{
		return EnumBlockRenderType.MODEL;
	}
	
	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int metadata) {
		return new TileWeaponMaker();
	}

	@Override
	public boolean onBlockActivated(World par1World, BlockPos par2, IBlockState par3, EntityPlayer par4EntityPlayer, EnumHand par5, ItemStack par6, EnumFacing par7, float par8, float par9, float par10) {
	    if(par1World.isRemote) {
	        return true;
	    }
	    else {
	     	if(!par4EntityPlayer.isSneaking()) {
	       		par4EntityPlayer.openGui(EssentialCraftCore.core, Config.guiID[0], par1World, par2.getX(), par2.getY(), par2.getZ());
	           	return true;
	       	}
	       	else {
	       		return false;
	       	}
	    }
	}
	
    @Override
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
		for(int i = 0; i < 4; i++) {
			list.add(new ItemStack(this,1,i));
		}
	}

	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List<String> par3List, boolean par4) 
    {
    	switch(par1ItemStack.getItemDamage())
    	{
	    	case 0:
	    	{	
	    		par3List.add(new ItemStack(ItemsCore.pistol).getDisplayName());
	    		break;
	    	}
	    	case 1:
	    	{	
	    		par3List.add(new ItemStack(ItemsCore.rifle).getDisplayName());
	    		break;
	    	}
	    	case 2:
	    	{	
	    		par3List.add(new ItemStack(ItemsCore.sniper).getDisplayName());
	    		break;
	    	}
	    	case 3:
	    	{	
	    		par3List.add(new ItemStack(ItemsCore.gatling).getDisplayName());
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

	@Override
	public void registerModels() {
		ModelLoader.setCustomStateMapper(this, new StateMap.Builder().ignore(TYPE).build());
		ModelLoader.setCustomMeshDefinition(Item.getItemFromBlock(this), new ModelUtils.MeshDefinitionSingleIcon("essentialcraft:weaponMaker"));
		ModelBakery.registerItemVariants(Item.getItemFromBlock(this), new ModelResourceLocation("essentialcraft:weaponMaker", "inventory"));
	}
}
