package ec3.common.block;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.mojang.authlib.GameProfile;

import DummyCore.Client.IModelRegisterer;
import DummyCore.Utils.MathUtils;
import DummyCore.Utils.MiscUtils;
import ec3.common.mod.EssentialCraftCore;
import ec3.common.tile.TileAnimalSeparator;
import ec3.common.tile.TileCrafter;
import ec3.common.tile.TileCreativeMRUSource;
import ec3.utils.cfg.Config;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBlockSpecial;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.common.util.FakePlayer;

public class BlockRedstoneDeviceNotSided extends BlockContainer implements IModelRegisterer {

	public static final PropertyEnum<DeviceType> TYPE = PropertyEnum.<DeviceType>create("type", DeviceType.class);
	public static final String[] names = new String[]{
			"replanter",
			"itemShuffler",
			"crafter",
			"breeder",
			"creativeMRUStorage",
			"shearingStation",
			"childSeparator",
			"adultSeparator"
	};

	public BlockRedstoneDeviceNotSided() {
		super(Material.ROCK);
	}

	public EnumBlockRenderType getRenderType(IBlockState s)
	{
		return EnumBlockRenderType.MODEL;
	}

	public void getSubBlocks(Item p_149666_1_, CreativeTabs p_149666_2_, List<ItemStack> p_149666_3_)
	{
		for(int i = 0; i < names.length; ++i)
			p_149666_3_.add(new ItemStack(p_149666_1_, 1, i));
	}

	public int damageDropped(IBlockState meta)
	{
		return meta.getValue(TYPE).getIndex();
	}

	public void shear(Entity en, IShearable e)
	{
		if(e.isShearable(new ItemStack(Items.SHEARS), en.getEntityWorld(), en.getPosition()))
		{
			List<ItemStack> items = e.onSheared(new ItemStack(Items.SHEARS), en.getEntityWorld(), en.getPosition(), 2);
			for(ItemStack is : items)
			{
				if(is != null)
				{
					EntityItem itm = new EntityItem(en.getEntityWorld(),en.posX,en.posY,en.posZ,is);
					if(!en.getEntityWorld().isRemote)
						en.getEntityWorld().spawnEntity(itm);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void breed(EntityItem e)
	{
		if(e.getEntityItem() != null)
		{
			AxisAlignedBB aabb = new AxisAlignedBB(e.posX-0.5D, e.posY-0.5D, e.posZ-0.5D, e.posX+0.5D, e.posY+0.5D, e.posZ+0.5D).expand(3, 3, 3);
			List<EntityAnimal> animals = e.getEntityWorld().getEntitiesWithinAABB(EntityAnimal.class,aabb);
			for(EntityAnimal animal : animals)
			{
				if(animal.isBreedingItem(e.getEntityItem()) && animal.getGrowingAge() == 0 && !animal.isInLove())
				{
					FakePlayer fake = new FakePlayer((WorldServer) e.getEntityWorld(), breederFakePlayerProfile);

					animal.setInLove(fake);

					fake = null;
					--e.getEntityItem().stackSize;
					if(invalidate(e))
						break;
				}
			}
		}
	}

	public void shuffle(EntityItem e)
	{
		if(!e.getEntityWorld().isRemote)
		{
			e.setPositionAndRotation(e.posX+MathUtils.randomDouble(e.getEntityWorld().rand), e.posY, e.posZ+MathUtils.randomDouble(e.getEntityWorld().rand), 0, 0);
		}
	}

	public void plant(EntityItem e) {
		BlockPos p = e.getPosition();
		if(e.getEntityItem() != null && !e.getEntityWorld().isRemote) {
			ItemStack stk = e.getEntityItem();
			if(stk.getItem() instanceof ItemBlock) {//cacti, grass
				Block b = Block.getBlockFromItem(stk.getItem());
				if(b instanceof IPlantable) {
					if(e.getEntityWorld().isAirBlock(p) && e.getEntityWorld().getBlockState(p.down()).getBlock().canSustainPlant(e.getEntityWorld().getBlockState(p.down()), e.getEntityWorld(), p.down(), EnumFacing.UP, IPlantable.class.cast(b))) {
						FakePlayer user = new FakePlayer((WorldServer)e.getEntityWorld(),planterFakePlayerProfile);

						stk.getItem().onItemUse(stk, user, e.getEntityWorld(), p.down(), EnumHand.MAIN_HAND, EnumFacing.UP, 0, 0, 0);

						invalidate(e);

						user = null;
					}
				}
			}
			else if(stk.getItem() instanceof IPlantable) {//seeds
				if(e.getEntityWorld().isAirBlock(p) && e.getEntityWorld().getBlockState(p.down()).getBlock().canSustainPlant(e.getEntityWorld().getBlockState(p.down()), e.getEntityWorld(), p.down(), EnumFacing.UP, IPlantable.class.cast(stk.getItem()))) {
					FakePlayer user = new FakePlayer((WorldServer) e.getEntityWorld(),planterFakePlayerProfile);

					stk.getItem().onItemUse(stk, user, e.getEntityWorld(), p.down(), EnumHand.MAIN_HAND, EnumFacing.UP, 0, 0, 0);

					invalidate(e);

					user = null;
				}
			}
			else if(stk.getItem() instanceof ItemBlockSpecial) {//reeds
				Block b = ItemBlockSpecial.class.cast(stk.getItem()).getBlock();
				if(b instanceof IPlantable) {
					if(e.getEntityWorld().isAirBlock(p) && e.getEntityWorld().getBlockState(p.down()).getBlock().canSustainPlant(e.getEntityWorld().getBlockState(p.down()), e.getEntityWorld(), p.down(), EnumFacing.UP, IPlantable.class.cast(b))) {
						FakePlayer user = new FakePlayer((WorldServer)e.getEntityWorld(),planterFakePlayerProfile);

						stk.getItem().onItemUse(stk, user, e.getEntityWorld(), p.down(), EnumHand.MAIN_HAND, EnumFacing.UP, 0, 0, 0);

						invalidate(e);

						user = null;
					}
				}
			}
		}
	}

	public boolean invalidate(EntityItem e)
	{
		if(e.getEntityItem() == null || e.getEntityItem().stackSize <= 0)
		{
			e.setDead();
			return true;
		}

		return false;
	}

	public boolean canProvidePower()
	{
		return true;
	}

	@SuppressWarnings("unchecked")
	public void neighborChanged(IBlockState s, World w, BlockPos p, Block n) 
	{
		if(s.getValue(TYPE).getIndex() == 0)
		{
			if(w.isBlockIndirectlyGettingPowered(p) > 0 || w.getStrongPower(p) > 0)
			{
				AxisAlignedBB aabb = new AxisAlignedBB(p).expand(12, 12, 12);
				List<EntityItem> items = w.getEntitiesWithinAABB(EntityItem.class, aabb);
				for(EntityItem itm : items)
				{
					if(!itm.isDead)
						plant(itm);
				}
			}
		}
		if(s.getValue(TYPE).getIndex() == 1)
		{
			if(w.isBlockIndirectlyGettingPowered(p) > 0 || w.getStrongPower(p) > 0)
			{
				AxisAlignedBB aabb = new AxisAlignedBB(p).expand(12, 12, 12);
				List<EntityItem> items = w.getEntitiesWithinAABB(EntityItem.class, aabb);
				for(EntityItem itm : items)
				{
					if(!itm.isDead)
						shuffle(itm);
				}
			}
		}
		if(s.getValue(TYPE).getIndex() == 3)
		{
			if(w.isBlockIndirectlyGettingPowered(p) > 0 || w.getStrongPower(p) > 0)
			{
				AxisAlignedBB aabb = new AxisAlignedBB(p).expand(12, 12, 12);
				List<EntityItem> items = w.getEntitiesWithinAABB(EntityItem.class, aabb);
				for(EntityItem itm : items)
				{
					if(!itm.isDead)
						breed(itm);
				}
			}
		}
		if(s.getValue(TYPE).getIndex() == 5)
		{
			if(w.isBlockIndirectlyGettingPowered(p) > 0 || w.getStrongPower(p) > 0)
			{
				AxisAlignedBB aabb = new AxisAlignedBB(p).expand(12, 12, 12);
				List<Entity> entities = w.getEntitiesWithinAABB(Entity.class, aabb);
				List<IShearable> sheep = new ArrayList<IShearable>();
				for(Entity e : entities) {
					if(e instanceof IShearable) {
						sheep.add((IShearable)e);
					}
				}
				for(IShearable sh : sheep)
				{
					shear((Entity)sh,sh);
				}
			}
		}
		if(s.getValue(TYPE).getIndex() == 6 || s.getValue(TYPE).getIndex() == 7)
		{
			TileAnimalSeparator.class.cast(w.getTileEntity(p)).separate(s.getValue(TYPE).getIndex() == 6);
		}
	}

	public static GameProfile planterFakePlayerProfile = new GameProfile(UUID.fromString("5cd89d0b-e9ba-0000-89f4-badbb05964ac"), "[EC3]Planter");
	public static GameProfile breederFakePlayerProfile = new GameProfile(UUID.fromString("5cd89d0b-e9ba-0000-89f4-badbb05964ad"), "[EC3]Breeder");

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int meta) {
		return meta == 2 ? new TileCrafter() : meta == 4 ? new TileCreativeMRUSource() : meta == 6 || meta == 7 ? new TileAnimalSeparator() : null;
	}

	@Override
	public boolean onBlockActivated(World par1World, BlockPos par2, IBlockState par3, EntityPlayer par4EntityPlayer, EnumHand par5, ItemStack par6, EnumFacing par7, float par8, float par9, float par10) {
		if(par1World.getTileEntity(par2) == null || par3.getValue(TYPE).getIndex() == 4)
			return false;
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
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(TYPE, DeviceType.fromIndex(meta%8));
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
	public void breakBlock(World par1World, BlockPos par2Pos, IBlockState par3State) {
		if(par1World.getTileEntity(par2Pos) != null)
			MiscUtils.dropItemsOnBlockBreak(par1World, par2Pos.getX(), par2Pos.getY(), par2Pos.getZ(), par3State.getBlock(), 0);
		super.breakBlock(par1World, par2Pos, par3State);
	}

	@Override
	public void registerModels() {
		for(int i = 0; i < DeviceType.values().length; i++) {
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), i, new ModelResourceLocation("essentialcraft:device", "type=" + DeviceType.fromIndex(i).getName()));
		}
	}

	public static enum DeviceType implements IStringSerializable {
		REPLANTER("replanter"),
		ITEMSHUFFLER("item_shuffler"),
		CRAFTER("crafter"),
		BREEDER("breeder"),
		CREATIVEMRUSTORAGE("creative_mru_storage"),
		SHEARINGSTATION("shearing_station"),
		CHILDSEPARATOR("child_separator"),
		ADULTSEPARATOR("adult_separator");

		private int index;
		private String name;

		private DeviceType(String s) {
			index = ordinal();
			name = s;
		}

		public String getName() {
			return name;
		}

		public String toString() {
			return name;
		}

		public int getIndex() {
			return index;
		}

		public static DeviceType fromIndex(int i) {
			return values()[i];
		}
	}
}
