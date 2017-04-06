package ec3.common.item;

import DummyCore.Client.IModelRegisterer;
import DummyCore.Utils.MathUtils;
import ec3.api.OreSmeltingRecipe;
import ec3.utils.common.ECUtils;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.oredict.OreDictionary;

public class ItemMagmaticWand extends ItemStoresMRUInNBT implements IModelRegisterer {

	public ItemMagmaticWand() {
		super();	
		this.setMaxMRU(5000);
		this.maxStackSize = 1;
		this.bFull3D = true;
	}

	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		ItemStack ore = new ItemStack(world.getBlockState(pos).getBlock(),1,world.getBlockState(pos).getBlock().getMetaFromState(world.getBlockState(pos)));
		if(ore != null && !world.isRemote)
		{
			int[] oreIds = OreDictionary.getOreIDs(ore);

			String oreName = "Unknown";
			if(oreIds.length > 0)
				oreName = OreDictionary.getOreName(oreIds[0]);
			int metadata = -1;
			for(int i = 0; i < OreSmeltingRecipe.values().length; ++i)
			{
				OreSmeltingRecipe oreColor = OreSmeltingRecipe.values()[i];
				if(oreName.equalsIgnoreCase(oreColor.oreName))
				{
					metadata = i;
					break;
				}
			}
			if(!player.getEntityWorld().isRemote && metadata != -1 && (ECUtils.tryToDecreaseMRUInStorage(player, -500) || this.setMRU(stack, -500)))
			{
				int suggestedStackSize = OreSmeltingRecipe.values()[metadata].dropAmount;
				if(world.rand.nextFloat() <= 0.33F)
					suggestedStackSize = OreSmeltingRecipe.values()[metadata].dropAmount*2;
				ItemStack sugStk = new ItemStack(ItemsCore.magicalAlloy,suggestedStackSize,metadata);

				GameType type = GameType.SURVIVAL;
				if(player.capabilities.isCreativeMode)
					type = GameType.CREATIVE;
				if(!player.capabilities.allowEdit)
					type = GameType.ADVENTURE;

				int be = ForgeHooks.onBlockBreakEvent(player.getEntityWorld(), type, (EntityPlayerMP)player, pos);
				if(be != -1)
				{
					world.setBlockToAir(pos);
					EntityItem drop = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), sugStk);
					drop.motionX = MathUtils.randomDouble(world.rand)/10;
					drop.motionY = MathUtils.randomDouble(world.rand)/10;
					drop.motionZ = MathUtils.randomDouble(world.rand)/10;
					world.spawnEntity(drop);
				}

				player.swingArm(hand);
			}
		}
		return EnumActionResult.PASS;
	}

	@Override
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation("essentialcraft:item/magmaticStaff", "inventory"));
	}
}
