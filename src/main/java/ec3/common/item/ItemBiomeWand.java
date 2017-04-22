package ec3.common.item;

import DummyCore.Client.IItemColor;
import DummyCore.Client.IModelRegisterer;
import DummyCore.Utils.MiscUtils;
import ec3.utils.common.ECUtils;
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

public class ItemBiomeWand extends ItemStoresMRUInNBT implements IModelRegisterer, IItemColor {

	public ItemBiomeWand() {
		super();	
		this.setMaxMRU(5000);
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

	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if(!player.isSneaking())
		{
			if(isBiomeSaved(stack))
			{
				if(ECUtils.tryToDecreaseMRUInStorage(player, -100) || this.setMRU(stack, -100))
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

	public int getColorFromItemstack(ItemStack par1ItemStack, int par2)
	{
		if(isBiomeSaved(par1ItemStack))
			return Biome.getBiome(getBiomeID(par1ItemStack)).getFoliageColorAtPos(BlockPos.ORIGIN);
		return 0xffffff;
	}

	@Override
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation("essentialcraft:item/biomeWand", "inventory"));
	}
}
