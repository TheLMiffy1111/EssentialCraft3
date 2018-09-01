package essentialcraft.common.item;

import DummyCore.Client.IItemColor;
import DummyCore.Client.IModelRegisterer;
import DummyCore.Client.ModelUtils;
import essentialcraft.common.capabilities.mru.CapabilityMRUHandler;
import essentialcraft.common.entity.EntitiesCore;
import essentialcraft.common.entity.EntityMRUPresence;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

//TODO remove this
public class ItemSpawnEggEC extends ItemMonsterPlacer implements IItemColor, IModelRegisterer {

	public ItemSpawnEggEC() {
		setHasSubtypes(true);
	}

	@Override
	public int getColorFromItemstack(ItemStack stack, int pass) {
		return 0xFFFFFF;
	}

	@Override
	public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);

		if(!player.capabilities.isCreativeMode) {
			stack.shrink(1);
		}

		if(world.isRemote) {
			return EnumActionResult.SUCCESS;
		}
		else {
			Block block = world.getBlockState(pos).getBlock();
			pos = pos.offset(facing);
			double d0 = 0.0D;

			if(facing == EnumFacing.UP) {
				d0 = block.getCollisionBoundingBox(world.getBlockState(pos.down()), world, pos.down()).maxY - 1;
			}

			Entity entity = spawnCreature(world, stack.getItemDamage(), pos.getX() + 0.5D, pos.getY()+ d0, pos.getZ() + 0.5D);

			if(entity != null) {
				if(entity instanceof EntityLivingBase && stack.hasDisplayName()) {
					((EntityLiving)entity).setCustomNameTag(stack.getDisplayName());
				}
			}

			return EnumActionResult.SUCCESS;
		}
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		String s = ("" + I18n.translateToLocal(this.getUnlocalizedName() + ".name")).trim();
		String s1 = EntitiesCore.REGISTERED_ENTITIES.get(stack.getItemDamage()).getName();

		if(s1 != null) {
			s = s + " " + I18n.translateToLocal("entity." + s1 + ".name");
		}

		return s;
	}

	@Override
	public void getSubItems(CreativeTabs t, NonNullList<ItemStack> l) {
		if(this.isInCreativeTab(t)) {
			for(int j = 0; j < EntitiesCore.REGISTERED_ENTITIES.size(); ++j) {
				l.add(new ItemStack(this, 1, j));
			}
		}
	}

	public static Entity spawnCreature(World world, int index, double x, double y, double z) {
		try {
			Entity entity = null;

			for(int j = 0; j < 1; ++j) {
				entity = EntitiesCore.REGISTERED_ENTITIES.get(index).newInstance(world);

				if(entity != null && entity instanceof EntityLivingBase) {
					EntityLivingBase entityliving = (EntityLivingBase)entity;
					entity.setLocationAndAngles(x, y, z, MathHelper.wrapDegrees(world.rand.nextFloat() * 360.0F), 0.0F);
					entityliving.rotationYawHead = entityliving.rotationYaw;
					entityliving.renderYawOffset = entityliving.rotationYaw;
					if(entity instanceof EntityLiving) {
						((EntityLiving)entityliving).onInitialSpawn(world.getDifficultyForLocation(new BlockPos(x, y, z)), (IEntityLivingData)null);
					}
					world.spawnEntity(entity);
					if(entity instanceof EntityLiving) {
						((EntityLiving)entityliving).playLivingSound();
					}
					if(entity instanceof EntityMRUPresence) {
						entity.getCapability(CapabilityMRUHandler.MRU_HANDLER_CAPABILITY, null).setMRU(500);
					}
				}
			}

			return entity;
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void registerModels() {
		ModelUtils.setItemModelSingleIcon(this, "essentialcraft:item/fruit_item");
	}
}
