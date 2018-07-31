package essentialcraft.common.item;

import DummyCore.Client.IItemColor;
import DummyCore.Client.IModelRegisterer;
import DummyCore.Utils.MiscUtils;
import essentialcraft.utils.common.ECUtils;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.client.model.ModelLoader;

public class ItemBiomeWand extends ItemMRUGeneric implements IModelRegisterer, IItemColor {

	public ItemBiomeWand() {
		super();
		this.maxStackSize = 1;
		this.bFull3D = true;
	}

	public static boolean isBiomeSaved(ItemStack stack)
	{
		NBTTagCompound tag = MiscUtils.getStackTag(stack);
		return tag.hasKey("biome");
	}

	public static int getBiomeID(ItemStack stack)
	{
		NBTTagCompound tag = MiscUtils.getStackTag(stack);
		if(isBiomeSaved(stack))
			return tag.getInteger("biome");
		return -1;
	}

	public static void setBiomeID(ItemStack stack, int bID, boolean remove)
	{
		NBTTagCompound tag = MiscUtils.getStackTag(stack);
		if(remove)
		{
			tag.removeTag("biome");
			return;
		}else
		{
			tag.setInteger("biome", bID);
		}
		stack.setTagCompound(tag);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		ItemStack stack = player.getHeldItem(hand);
		if(!player.isSneaking())
		{
			if(isBiomeSaved(stack))
			{
				if(ECUtils.playerUseMRU(player, stack, 100))
				{
					for(int x = pos.getX()-1; x <= pos.getX()+1; ++x)
					{
						for(int z = pos.getZ()-1; z <= pos.getZ()+1; ++z)
						{
							MiscUtils.changeBiome(world, Biome.getBiome(getBiomeID(stack)), x, z);
							player.swingArm(hand);
						}
					}
				}
			}else
			{
				int cbiome = Biome.getIdForBiome(world.getBiome(pos));
				setBiomeID(stack,cbiome,false);
				player.swingArm(hand);
			}
		}else
		{
			setBiomeID(stack,0,true);
			player.swingArm(hand);
		}
		return EnumActionResult.PASS;
	}

	@Override
	public int getColorFromItemstack(ItemStack stack, int par2)
	{
		if(isBiomeSaved(stack))
			return Biome.getBiome(getBiomeID(stack)).getFoliageColorAtPos(BlockPos.ORIGIN);
		return 0xffffff;
	}

	@Override
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation("essentialcraft:item/biomewand", "inventory"));
	}
}
