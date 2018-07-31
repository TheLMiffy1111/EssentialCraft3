package essentialcraft.common.item;

import java.util.List;

import DummyCore.Client.IModelRegisterer;
import DummyCore.Utils.Coord3D;
import DummyCore.Utils.DummyDistance;
import DummyCore.Utils.IItemOverlayElement;
import DummyCore.Utils.MathUtils;
import DummyCore.Utils.MiscUtils;
import essentialcraft.utils.common.ECUtils;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemMagicalBuilder extends ItemMRUGeneric implements IModelRegisterer {

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World player, List<String> list, ITooltipFlag par4)
	{
		switch(stack.getItemDamage())
		{
		case 0:
		{
			list.add(I18n.translateToLocal("essentialcraft.txt.fillMode.normal"));
			break;
		}
		case 1:
		{
			list.add(I18n.translateToLocal("essentialcraft.txt.fillMode.air"));
			break;
		}
		case 2:
		{
			list.add(I18n.translateToLocal("essentialcraft.txt.fillMode.replaceSelected"));
			break;
		}
		case 3:
		{
			list.add(I18n.translateToLocal("essentialcraft.txt.fillMode.replaceButSelected"));
			break;
		}
		case 4:
		{
			list.add(I18n.translateToLocal("essentialcraft.txt.fillMode.replaceAll"));
			break;
		}
		}

		if(this.hasFirstPoint(stack))
			list.add(I18n.translateToLocal("essentialcraft.txt.p1")+"| X:"+this.getFirstPoint(stack).x+", Y:"+this.getFirstPoint(stack).y+", Z:"+this.getFirstPoint(stack).z);
		if(this.hasSecondPoint(stack))
			list.add(I18n.translateToLocal("essentialcraft.txt.p2")+"| X:"+this.getSecondPoint(stack).x+", Y:"+this.getSecondPoint(stack).y+", Z:"+this.getSecondPoint(stack).z);
		if(this.hasStoredBlock(stack) && !this.retrieveStackFromNBT(stack).isEmpty())
			list.add(I18n.translateToLocal("essentialcraft.txt.storedStack")+": "+this.retrieveStackFromNBT(stack).getDisplayName());

		list.add(" ");

		super.addInformation(stack, player, list, par4);
	}

	@Override
	public void getSubItems(CreativeTabs par2CreativeTabs, NonNullList<ItemStack> list)
	{
		if(this.isInCreativeTab(par2CreativeTabs))
			for(int i = 0; i < 5; ++i)
			{
				ItemStack min = new ItemStack(this, 1, i);
				ItemStack max = new ItemStack(this, 1, i);
				min.getCapability(MRU_HANDLER_ITEM_CAPABILITY, null).setMRU(0);
				max.getCapability(MRU_HANDLER_ITEM_CAPABILITY, null).setMRU(maxMRU);
				list.add(min);
				list.add(max);
			}
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World w, EntityPlayer p, EnumHand hand)
	{
		ItemStack is = p.getHeldItem(hand);
		RayTraceResult mop = this.rayTrace(w, p, p.capabilities.isCreativeMode);

		if(mop!=null && mop.typeOfHit==Type.BLOCK)
		{
			if(!p.isSneaking())
			{
				boolean hasPos1 = this.hasFirstPoint(is);
				boolean hasPos2 = this.hasSecondPoint(is);

				if(!hasPos1)
				{
					this.setFirstPoint(is, mop.getBlockPos().getX(), mop.getBlockPos().getY(), mop.getBlockPos().getZ());
					if(p.getEntityWorld().isRemote)
						p.sendMessage(new TextComponentString("[Magical Builder] First position: "+mop.getBlockPos().getX()+","+mop.getBlockPos().getY()+","+mop.getBlockPos().getZ()).setStyle(new Style().setColor(TextFormatting.DARK_PURPLE)));
					return super.onItemRightClick(w, p, hand);
				}

				if(!hasPos2)
				{
					Coord3D first = this.getFirstPoint(is);
					Coord3D second = new Coord3D(mop.getBlockPos().getX(), mop.getBlockPos().getY(), mop.getBlockPos().getZ());
					DummyDistance dist = new DummyDistance(first,second);
					if(dist.getDistance() > 48)
					{
						if(p.getEntityWorld().isRemote)
							p.sendMessage(new TextComponentString("[Magical Builder]The distance between points ("+dist.getDistance()+") is too large, max: 48!").setStyle(new Style().setColor(TextFormatting.DARK_PURPLE)));
						return super.onItemRightClick(w, p, hand);
					}
					this.setSecondPoint(is, mop.getBlockPos().getX(), mop.getBlockPos().getY(), mop.getBlockPos().getZ());
					if(p.getEntityWorld().isRemote)
						p.sendMessage(new TextComponentString("[Magical Builder] Second position: "+mop.getBlockPos().getX()+","+mop.getBlockPos().getY()+","+mop.getBlockPos().getZ()).setStyle(new Style().setColor(TextFormatting.DARK_PURPLE)));
					return super.onItemRightClick(w, p, hand);
				}

				if(hasPos1 && hasPos2)
				{
					if(p.getEntityWorld().isRemote)
						p.sendMessage(new TextComponentString("[Magical Builder]Both points already set! Shift-rightclick air to reset!").setStyle(new Style().setColor(TextFormatting.DARK_PURPLE)));
				}
			}else
			{
				if(this.setStoredStack(is, w, mop.getBlockPos().getX(), mop.getBlockPos().getY(), mop.getBlockPos().getZ()))
				{
					if(!this.retrieveStackFromNBT(is).isEmpty())
					{
						if(p.getEntityWorld().isRemote)
							p.sendMessage(new TextComponentString("[Magical Builder]Set the block to: "+this.retrieveStackFromNBT(is).getDisplayName()).setStyle(new Style().setColor(TextFormatting.DARK_PURPLE)));
					}
				}
			}
		}else if(mop==null)
		{
			if(p.isSneaking())
			{
				this.resetPoints(is);

				if(p.getEntityWorld().isRemote)
					p.sendMessage(new TextComponentString("[Magical Builder]Both points reseted!").setStyle(new Style().setColor(TextFormatting.DARK_PURPLE)));
			}else
			{
				if(this.hasFirstPoint(is) && this.hasSecondPoint(is) && (this.hasStoredBlock(is) && !this.retrieveStackFromNBT(is).isEmpty() || is.getItemDamage() == 1))
				{
					int setted = this.setAreaToBlock(p, is);
					if(p.getEntityWorld().isRemote)
						p.sendMessage(new TextComponentString("[Magical Builder]Filled selected area! "+setted+" blocks got replaced!").setStyle(new Style().setColor(TextFormatting.DARK_PURPLE)));
				}

			}
		}

		return super.onItemRightClick(w, p, hand);
	}

	public Coord3D getFirstPoint(ItemStack is)
	{
		return new Coord3D(MiscUtils.getStackTag(is).getInteger("p1_x"),MiscUtils.getStackTag(is).getInteger("p1_y"),MiscUtils.getStackTag(is).getInteger("p1_z"));
	}

	public Coord3D getSecondPoint(ItemStack is)
	{
		return new Coord3D(MiscUtils.getStackTag(is).getInteger("p2_x"),MiscUtils.getStackTag(is).getInteger("p2_y"),MiscUtils.getStackTag(is).getInteger("p2_z"));
	}

	public boolean resetPoints(ItemStack is)
	{
		NBTTagCompound tag = MiscUtils.getStackTag(is);
		if(!tag.hasKey("p1_x") && !tag.hasKey("p2_x"))
			return false;

		tag.removeTag("p1_x");
		tag.removeTag("p1_y");
		tag.removeTag("p1_z");
		tag.removeTag("p2_x");
		tag.removeTag("p2_y");
		tag.removeTag("p2_z");

		return true;
	}

	public boolean hasFirstPoint(ItemStack is)
	{
		return MiscUtils.getStackTag(is).hasKey("p1_x");
	}

	public boolean hasSecondPoint(ItemStack is)
	{
		return MiscUtils.getStackTag(is).hasKey("p2_x");
	}

	public void setFirstPoint(ItemStack is, int x, int y, int z)
	{
		MiscUtils.getStackTag(is).setInteger("p1_x", x);
		MiscUtils.getStackTag(is).setInteger("p1_y", y);
		MiscUtils.getStackTag(is).setInteger("p1_z", z);
	}

	public void setSecondPoint(ItemStack is, int x, int y, int z)
	{
		MiscUtils.getStackTag(is).setInteger("p2_x", x);
		MiscUtils.getStackTag(is).setInteger("p2_y", y);
		MiscUtils.getStackTag(is).setInteger("p2_z", z);
	}

	public boolean hasStoredBlock(ItemStack is)
	{
		return MiscUtils.getStackTag(is).hasKey("storedStackTag");
	}

	public ItemStack retrieveStackFromNBT(ItemStack is)
	{
		if(!hasStoredBlock(is))
			return ItemStack.EMPTY;

		return new ItemStack(MiscUtils.getStackTag(is).getCompoundTag("storedStackTag"));
	}

	public void nullifyStoredStack(ItemStack is)
	{
		MiscUtils.getStackTag(is).removeTag("storedStackTag");
	}

	public boolean setStoredStack(ItemStack is, World w, int x, int y, int z)
	{
		if(!w.isAirBlock(new BlockPos(x, y, z)))
		{
			ItemStack stored = new ItemStack(w.getBlockState(new BlockPos(x, y, z)).getBlock(),1,w.getBlockState(new BlockPos(x, y, z)).getBlock().getMetaFromState(w.getBlockState(new BlockPos(x, y, z))));
			NBTTagCompound tag = new NBTTagCompound();
			stored.writeToNBT(tag);
			MiscUtils.getStackTag(is).setTag("storedStackTag", tag);
			return true;
		}
		return false;
	}

	public int findPlayerISSlot(EntityPlayer e, ItemStack is)
	{
		if(is.isEmpty())
			return -1;

		if(e.capabilities.isCreativeMode)
			return Integer.MAX_VALUE;

		for(int i = 0; i < e.inventory.getSizeInventory(); ++i)
		{
			ItemStack stk = e.inventory.getStackInSlot(i);
			if(!stk.isEmpty() && stk.isItemEqual(is))
				return i;
		}
		return -1;
	}

	public int decreasePlayerStackInSlot(EntityPlayer e, ItemStack is, int slot)
	{
		if(e.capabilities.isCreativeMode)
			return Integer.MAX_VALUE;

		e.inventory.decrStackSize(slot, 1);
		if(e.inventory.getStackInSlot(slot).isEmpty() || e.inventory.getStackInSlot(slot).getCount() <= 0)
			return findPlayerISSlot(e,is);

		return slot;
	}

	public int setAreaToBlock(EntityPlayer e, ItemStack is)
	{
		Coord3D start = this.getFirstPoint(is);
		Coord3D end = this.getSecondPoint(is);
		int diffX = MathHelper.floor(MathUtils.module(end.x-start.x));
		int diffY = MathHelper.floor(MathUtils.module(end.y-start.y));
		int diffZ = MathHelper.floor(MathUtils.module(end.z-start.z));
		ItemStack setTo = this.retrieveStackFromNBT(is);
		int slotNum = findPlayerISSlot(e,setTo);
		int itemsSet = 0;
		if(is.getItemDamage() == 1)
			slotNum = Integer.MAX_VALUE;

		if(is.getItemDamage() == 2 || is.getItemDamage() == 3)
			slotNum = this.hasStoredBlock(is) && !setTo.isEmpty() ? Integer.MAX_VALUE : -1;

		if(slotNum != -1)
		{
			for(int x = 0; x <= diffX; ++x)
			{
				int dx = x;
				if(start.x >= end.x)
					dx = MathHelper.floor(end.x + x);
				else
					dx = MathHelper.floor(start.x + x);

				for(int y = 0; y <= diffY; ++y)
				{
					int dy = y;
					if(start.y >= end.y)
						dy = MathHelper.floor(end.y + y);
					else
						dy = MathHelper.floor(start.y + y);

					for(int z = 0; z <= diffZ; ++z)
					{
						int dz = z;
						if(start.z >= end.z)
							dz = (int) (end.z + z);
						else
							dz = (int) (start.z + z);

						ItemStack settedTo = setTo.isEmpty() ? ItemStack.EMPTY : setTo.copy();
						BlockPos dp = new BlockPos(dx, dy, dz);

						if(is.getItemDamage() == 0)
						{
							if(e.getEntityWorld().isAirBlock(dp))
							{
								if(!e.canPlayerEdit(dp, EnumFacing.DOWN, settedTo))
									continue;

								if(!ECUtils.playerUseMRU(e, is, 25))
									return itemsSet;

								slotNum = this.decreasePlayerStackInSlot(e, settedTo, slotNum);
								//e.getEntityWorld().setBlock(dx, dy, dz, Block.getBlockFromItem(setTo.getItem()), setTo.getItemDamage(), 3);
								if(ForgeHooks.onPlaceItemIntoWorld(settedTo, e, e.getEntityWorld(), dp, EnumFacing.DOWN, 0, 0, 0, EnumHand.MAIN_HAND) == EnumActionResult.SUCCESS)
									++itemsSet;
								else
								{
									settedTo.setCount(1);
									if(!e.inventory.addItemStackToInventory(settedTo))
										e.dropItem(settedTo, false);
								}

								if(slotNum == -1)
									return itemsSet;
							}
						}
						if(is.getItemDamage() == 1)
						{
							if(!e.canPlayerEdit(dp, EnumFacing.DOWN, settedTo))
								continue;

							if(!ECUtils.playerUseMRU(e, is, 250))
								return itemsSet;

							if(e.getEntityWorld().getBlockState(dp).getBlockHardness(e.getEntityWorld(), dp) >= 0 && !e.getEntityWorld().isRemote)
							{
								GameType type = GameType.SURVIVAL;
								if(e.capabilities.isCreativeMode)
									type = GameType.CREATIVE;
								if(!e.capabilities.allowEdit)
									type = GameType.ADVENTURE;

								int be = ForgeHooks.onBlockBreakEvent(e.getEntityWorld(), type, (EntityPlayerMP)e, dp);
								if(be != -1)
								{
									e.getEntityWorld().getBlockState(dp).getBlock().dropBlockAsItem(e.getEntityWorld(), dp, e.getEntityWorld().getBlockState(new BlockPos(dx, dy, dz)), 0);
									e.getEntityWorld().setBlockToAir(dp);
									++itemsSet;
								}
							}
						}
						if(is.getItemDamage() == 2)
						{
							if(!e.canPlayerEdit(dp, EnumFacing.DOWN, settedTo))
								continue;

							if(e.getEntityWorld().getBlockState(dp).getBlockHardness(e.getEntityWorld(), dp) >= 0 && !e.getEntityWorld().isRemote)
							{
								ItemStack worldStack = new ItemStack(e.getEntityWorld().getBlockState(dp).getBlock(),1,e.getEntityWorld().getBlockState(dp).getBlock().getMetaFromState(e.getEntityWorld().getBlockState(dp)));
								if(!worldStack.isEmpty() && setTo.isItemEqual(worldStack))
								{
									if(!ECUtils.playerUseMRU(e, is, 250))
										return itemsSet;

									GameType type = GameType.SURVIVAL;
									if(e.capabilities.isCreativeMode)
										type = GameType.CREATIVE;
									if(!e.capabilities.allowEdit)
										type = GameType.ADVENTURE;

									int be = ForgeHooks.onBlockBreakEvent(e.getEntityWorld(), type, (EntityPlayerMP)e, dp);
									if(be != -1)
									{
										e.getEntityWorld().getBlockState(dp).getBlock().dropBlockAsItem(e.getEntityWorld(), dp, e.getEntityWorld().getBlockState(dp), 0);
										e.getEntityWorld().setBlockToAir(dp);
										++itemsSet;
									}
								}
							}
						}
						if(is.getItemDamage() == 3)
						{
							if(!e.canPlayerEdit(dp, EnumFacing.DOWN, settedTo))
								continue;

							if(e.getEntityWorld().getBlockState(dp).getBlockHardness(e.getEntityWorld(), dp) >= 0 && !e.getEntityWorld().isRemote)
							{
								ItemStack worldStack = new ItemStack(e.getEntityWorld().getBlockState(dp).getBlock(),1,e.getEntityWorld().getBlockState(dp).getBlock().getMetaFromState(e.getEntityWorld().getBlockState(dp)));
								if(!worldStack.isEmpty() && !setTo.isItemEqual(worldStack))
								{
									if(!ECUtils.playerUseMRU(e, is, 250))
										return itemsSet;

									GameType type = GameType.SURVIVAL;
									if(e.capabilities.isCreativeMode)
										type = GameType.CREATIVE;
									if(!e.capabilities.allowEdit)
										type = GameType.ADVENTURE;

									int be = ForgeHooks.onBlockBreakEvent(e.getEntityWorld(), type, (EntityPlayerMP)e, dp);
									if(be != -1)
									{
										e.getEntityWorld().getBlockState(dp).getBlock().dropBlockAsItem(e.getEntityWorld(), dp, e.getEntityWorld().getBlockState(dp), 0);
										e.getEntityWorld().setBlockToAir(dp);
										++itemsSet;
									}
								}
							}
						}
						if(is.getItemDamage() == 4)
						{
							if(!e.canPlayerEdit(dp, EnumFacing.DOWN, settedTo))
								continue;

							if(e.getEntityWorld().isAirBlock(dp))
							{
								if(!ECUtils.playerUseMRU(e, is, 25))
									return itemsSet;

								slotNum = this.decreasePlayerStackInSlot(e, setTo, slotNum);

								if(ForgeHooks.onPlaceItemIntoWorld(settedTo, e, e.getEntityWorld(), dp, EnumFacing.DOWN, 0, 0, 0, EnumHand.MAIN_HAND) == EnumActionResult.SUCCESS)
									++itemsSet;
								else
								{
									settedTo.setCount(1);
									if(!e.inventory.addItemStackToInventory(settedTo))
										e.dropItem(settedTo, false);
								}

								if(slotNum == -1)
									return itemsSet;
							}else if(e.getEntityWorld().getBlockState(dp).getBlockHardness(e.getEntityWorld(), dp) >= 0 && !e.getEntityWorld().isRemote)
							{
								slotNum = this.decreasePlayerStackInSlot(e, setTo, slotNum);

								if(!ECUtils.playerUseMRU(e, is, 300))
									return itemsSet;

								GameType type = GameType.SURVIVAL;
								if(e.capabilities.isCreativeMode)
									type = GameType.CREATIVE;
								if(!e.capabilities.allowEdit)
									type = GameType.ADVENTURE;

								int be = ForgeHooks.onBlockBreakEvent(e.getEntityWorld(), type, (EntityPlayerMP)e, dp);
								if(be != -1)
								{
									e.getEntityWorld().getBlockState(dp).getBlock().dropBlockAsItem(e.getEntityWorld(), dp, e.getEntityWorld().getBlockState(dp), 0);
									e.getEntityWorld().setBlockState(dp, Block.getBlockFromItem(setTo.getItem()).getStateFromMeta(setTo.getItemDamage()), 3);
									++itemsSet;
								}

								if(slotNum == -1)
									return itemsSet;
							}
						}
					}
				}
			}
		}
		return itemsSet;
	}

	@Override
	public void registerModels() {
		for(int i = 0; i < 5; i++) {
			ModelLoader.setCustomModelResourceLocation(this, i, new ModelResourceLocation("essentialcraft:item/magicalbuilder", "inventory"));
		}
		MiscUtils.addItemOverlayElement(this, ItemOverlayMagicalBuilder.INSTANCE);
	}

	public static class ItemOverlayMagicalBuilder implements IItemOverlayElement {
		public static final ItemOverlayMagicalBuilder INSTANCE = new ItemOverlayMagicalBuilder();

		@Override
		public void renderItemOverlayIntoGUI(FontRenderer fr, ItemStack item, int x, int y, String text) {
			GlStateManager.disableDepth();
			ItemMagicalBuilder builder = (ItemMagicalBuilder)item.getItem();
			if(builder.hasStoredBlock(item)) {
				if(!builder.retrieveStackFromNBT(item).isEmpty()) {
					ItemStack rendered = builder.retrieveStackFromNBT(item);
					GlStateManager.pushMatrix();

					GlStateManager.translate(8+x, 8+y, 0);
					GlStateManager.scale(0.5F, 0.5F, 0.5F);
					Minecraft.getMinecraft().getRenderItem().renderItemIntoGUI(rendered, 0, 0);
					GlStateManager.scale(2, 2, 2);

					GlStateManager.popMatrix();
				}
			}
			GlStateManager.enableDepth();
		}
	}
}
