package ec3.common.item;

import java.util.List;

import DummyCore.Client.IModelRegisterer;
import DummyCore.Utils.MiscUtils;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

public class ItemCollectedMonsterSpawner extends Item implements IModelRegisterer
{
	public ItemCollectedMonsterSpawner()
	{
		this.setHasSubtypes(true);
	}

	public EnumActionResult onItemUse(ItemStack stk, EntityPlayer placer, World w, BlockPos pos, EnumHand hand, EnumFacing side, float vecX, float vecY, float vecZ)
	{
		BlockPos nP = pos.offset(side);
		if(placer.canPlayerEdit(nP, side.getOpposite(), stk))
		{
			if(Blocks.MOB_SPAWNER.canPlaceBlockAt(w, nP))
			{
				NBTTagCompound tag = MiscUtils.getStackTag(stk);
				if(tag.hasKey("monsterSpawner"))
				{
					NBTTagCompound spawnerTag = tag.getCompoundTag("monsterSpawner");
					spawnerTag.setInteger("x", nP.getX());
					spawnerTag.setInteger("y", nP.getY());
					spawnerTag.setInteger("z", nP.getZ());
					if(w.setBlockState(nP, Blocks.MOB_SPAWNER.getDefaultState(), 3))
					{
						TileEntity tile = w.getTileEntity(nP);
						if(tile != null)
							tile.readFromNBT(spawnerTag);

						placer.swingArm(hand);
						--stk.stackSize;
					}
				}
			}
		}
		return EnumActionResult.PASS;
	}

	public void addInformation(ItemStack stk, EntityPlayer player, List<String> lst, boolean isCurrentItem) {
		NBTTagCompound tag = MiscUtils.getStackTag(stk);
		if(tag.hasKey("monsterSpawner")) {
			NBTTagCompound spawnerTag = tag.getCompoundTag("monsterSpawner");
			if(spawnerTag.hasKey("SpawnData")) {
				NBTTagCompound dataTag = spawnerTag.getCompoundTag("SpawnData");
				if(dataTag.hasKey("id")) {
					String id = dataTag.getString("id");
					id = "entity." + id + ".name";
					lst.add(I18n.translateToLocal(id));
				}
			}
		}
	}

	@Override
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation("essentialcraft:item/collectedSpawner", "inventory"));
	}
}
