package ec3.common.item;

import DummyCore.Client.IModelRegisterer;
import ec3.common.block.BlocksCore;
import ec3.utils.common.ECUtils;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

public class ItemMagicLantern extends ItemStoresMRUInNBT implements IModelRegisterer {

	public ItemMagicLantern() {
		super();	
		this.setMaxMRU(5000);
		this.maxStackSize = 1;
		this.bFull3D = true;
	}

	@Override
	public void onUpdate(ItemStack itemStack, World world, Entity entity, int indexInInventory, boolean isCurrentItem)
	{
		if(!world.isRemote)
		{
			int fX = MathHelper.floor(entity.posX);
			int fY = MathHelper.floor(entity.posY);
			int fZ = MathHelper.floor(entity.posZ);
			Block b = world.getBlockState(new BlockPos(fX, fY, fZ)).getBlock();
			if(b == Blocks.AIR)
			{
				if(isCurrentItem)
				{
					world.setBlockState(new BlockPos(fX, fY, fZ), BlocksCore.torch.getStateFromMeta(1), 3);
					world.scheduleUpdate(new BlockPos(fX, fY, fZ), BlocksCore.torch, 20);
				}
			}
		}
		super.onUpdate(itemStack, world, entity, indexInInventory, isCurrentItem);
	}


	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing dir, float hitX, float hitY, float hitZ)
	{
		Block b = world.getBlockState(pos.offset(dir)).getBlock();
		if(b == Blocks.AIR)
		{
			if(player.inventory.hasItemStack(new ItemStack(ItemsCore.magicalSlag)) && (ECUtils.tryToDecreaseMRUInStorage(player, -100) || this.setMRU(stack, -100)))
			{
				int slotID = -1;
				for(int i = 0; i < player.inventory.getSizeInventory(); ++i)
				{
					ItemStack stk = player.inventory.getStackInSlot(i);
					if(stk != null && stk.getItem() == ItemsCore.magicalSlag)
					{
						slotID = i;
						break;
					}
				}
				if(slotID != -1)
				{
					player.inventory.decrStackSize(slotID, 1);
					world.setBlockState(pos.offset(dir), BlocksCore.torch.getStateFromMeta(0), 3);
					player.swingArm(hand);
				}
			}
		}
		return EnumActionResult.PASS;
	}

	@Override
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation("essentialcraft:item/magicalLantern", "inventory"));
	}
}
