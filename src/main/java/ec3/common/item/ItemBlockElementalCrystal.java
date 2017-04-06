package ec3.common.item;

import java.util.List;

import ec3.common.tile.TileElementalCrystal;
import DummyCore.Utils.MiscUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemBlockElementalCrystal extends ItemBlock implements IItemColor {

	public ItemBlockElementalCrystal(Block p_i45328_1_) {
		super(p_i45328_1_);
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
	}

	//Neutral: D5F3F4  213 243 244
	//Fire:    7D210B  130  33  11
	//Water:   3298D4   50 152 212
	//Earth:   114F1C   17  79  28
	//Air:     858D92  133 141 146

	@Override
	public int getColorFromItemstack(ItemStack stack, int tintIndex) {
		double fire = 0D;
		double water = 0D;
		double earth = 0D;
		double air = 0D;
		double neutral = 10D;
		if(MiscUtils.getStackTag(stack) != null) {
			fire = MiscUtils.getStackTag(stack).getFloat("fire");
			water = MiscUtils.getStackTag(stack).getFloat("water");
			earth = MiscUtils.getStackTag(stack).getFloat("earth");
			air = MiscUtils.getStackTag(stack).getFloat("air");
		}
		double total = fire+water+earth+air+neutral;
		fire /= total;
		water /= total;
		earth /= total;
		air /= total;
		neutral /= total;

		double red = ((130D/255D)*fire)+((50D/255D)*water)+((17D/255D)*earth)+((133D/255D)*air)+((213D/255D)*neutral);
		double green = ((33D/255D)*fire)+((152D/255D)*water)+((79D/255D)*earth)+((141D/255D)*air)+((243D/255D)*neutral);
		double blue = ((11D/255D)*fire)+((212D/255D)*water)+((28D/255D)*earth)+((146D/255D)*air)+((244D/255D)*neutral);

		return (((int)(red*255D))<<16)+(((int)(blue*255D))<<8)+((int)(green*255D));
	}

	public EnumActionResult onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, BlockPos pos, EnumHand hand, EnumFacing facing, float par8, float par9, float par10) {
		float size = 0;
		float fire = 0;
		float water = 0;
		float earth = 0;
		float air = 0;
		if(MiscUtils.getStackTag(par1ItemStack) != null) {
			size = MiscUtils.getStackTag(par1ItemStack).getFloat("size");
			fire = MiscUtils.getStackTag(par1ItemStack).getFloat("fire");
			water = MiscUtils.getStackTag(par1ItemStack).getFloat("water");
			earth = MiscUtils.getStackTag(par1ItemStack).getFloat("earth");
			air = MiscUtils.getStackTag(par1ItemStack).getFloat("air");
		}
		Block i1 = par3World.getBlockState(pos).getBlock();

		if(i1 == Blocks.SNOW && (i1.getMetaFromState(par3World.getBlockState(pos)) & 7) < 1)
		{
			facing = EnumFacing.UP;
		}
		else if(i1 != Blocks.VINE && i1 != Blocks.TALLGRASS && i1 != Blocks.DEADBUSH
				&& (i1 == null || !i1.isReplaceable(par3World, pos)))
		{
			if(facing == EnumFacing.DOWN)
			{
				pos = pos.down();
			}

			if(facing == EnumFacing.UP)
			{
				pos = pos.up();
			}

			if(facing == EnumFacing.NORTH)
			{
				pos = pos.north();
			}

			if(facing == EnumFacing.SOUTH)
			{
				pos = pos.south();
			}

			if(facing == EnumFacing.WEST)
			{
				pos = pos.west();
			}

			if(facing == EnumFacing.EAST)
			{
				pos = pos.east();
			}
		}

		if(par1ItemStack.stackSize == 0)
		{
			return EnumActionResult.PASS;
		}
		else if(!par2EntityPlayer.canPlayerEdit(pos, facing, par1ItemStack))
		{
			return EnumActionResult.PASS;
		}
		else if(pos.getY() == 255)
		{
			return EnumActionResult.PASS;
		}
		else if(par3World.canBlockBePlaced(this.block, pos, false, facing, par2EntityPlayer, par1ItemStack))
		{
			Block block = this.block;
			int j1 = this.getMetadata(par1ItemStack.getItemDamage());
			IBlockState k1 = block.getStateForPlacement(par3World, pos, facing, par8, par9, par10, j1, par2EntityPlayer);

			if (placeBlockAt(par1ItemStack, par2EntityPlayer, par3World, pos, facing, par8, par9, par10, k1))
			{
				par3World.setBlockState(pos, k1, 3);
				par3World.playSound(par2EntityPlayer, (double)((float)pos.getX() + 0.5F), (double)((float)pos.getY() + 0.5F), (double)((float)pos.getZ() + 0.5F), block.getSoundType().getPlaceSound(), SoundCategory.BLOCKS, (block.getSoundType().getVolume() + 1.0F) / 2.0F, block.getSoundType().getPitch() * 0.8F);
				--par1ItemStack.stackSize;
				TileElementalCrystal c = (TileElementalCrystal) par3World.getTileEntity(pos);
				if(c != null)
				{
					c.size = size;
					c.fire = fire;
					c.water = water;
					c.earth = earth;
					c.air = air;
				}

			}

			return EnumActionResult.SUCCESS;
		}
		else
		{
			return EnumActionResult.PASS;
		}
	}

	/**
	 * Returns the metadata of the block which this Item (ItemBlock) can place
	 */
	public int getMetadata(int par1) {
		return par1;
	}

	public void getSubItems(Item p_150895_1_, CreativeTabs p_150895_2_, List<ItemStack> p_150895_3_) {
		for(int i = 0; i < 5; ++i) {
			for(int i1 = 0; i1 < 4; ++i1) {
				ItemStack crystalStack = new ItemStack(p_150895_1_,1,0);
				NBTTagCompound tag = new NBTTagCompound();
				tag.setFloat("size", i*25);
				float[] elements = new float[]{0,0,0,0};
				elements[i1] = 100F;
				tag.setFloat("fire", elements[0]);
				tag.setFloat("water", elements[1]);
				tag.setFloat("earth", elements[2]);
				tag.setFloat("air", elements[3]);
				crystalStack.setTagCompound(tag);
				p_150895_3_.add(crystalStack);
			}{
				ItemStack crystalStack = new ItemStack(p_150895_1_,1,0);
				NBTTagCompound tag = new NBTTagCompound();
				tag.setFloat("size", i*25);
				float[] elements = new float[]{100,100,100,100};
				tag.setFloat("fire", elements[0]);
				tag.setFloat("water", elements[1]);
				tag.setFloat("earth", elements[2]);
				tag.setFloat("air", elements[3]);
				crystalStack.setTagCompound(tag);
				p_150895_3_.add(crystalStack);
			}{
				ItemStack crystalStack = new ItemStack(p_150895_1_,1,0);
				NBTTagCompound tag = new NBTTagCompound();
				tag.setFloat("size", i*25);
				float[] elements = new float[]{0,0,0,0};
				tag.setFloat("fire", elements[0]);
				tag.setFloat("water", elements[1]);
				tag.setFloat("earth", elements[2]);
				tag.setFloat("air", elements[3]);
				crystalStack.setTagCompound(tag);
				p_150895_3_.add(crystalStack);
			}
		}
	}
}
