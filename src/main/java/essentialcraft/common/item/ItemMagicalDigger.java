package essentialcraft.common.item;

import java.util.List;

import DummyCore.Client.IModelRegisterer;
import DummyCore.Utils.Coord3D;
import essentialcraft.api.IMRUHandlerItem;
import essentialcraft.common.capabilities.mru.CapabilityMRUHandler;
import essentialcraft.common.capabilities.mru.MRUItemStorage;
import essentialcraft.utils.common.ECUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemMagicalDigger extends ItemPickaxe implements IModelRegisterer {

	public ItemMagicalDigger() {
		super(ItemsCore.elemental);
		this.maxStackSize = 1;
		this.bFull3D = false;
		this.setMaxDamage(0);
	}

	public static Capability<IMRUHandlerItem> MRU_HANDLER_ITEM_CAPABILITY = CapabilityMRUHandler.MRU_HANDLER_ITEM_CAPABILITY;
	public int maxMRU = 5000;

	@Override
	public boolean isEnchantable(ItemStack p_77616_1_)
	{
		return true;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World player, List<String> list, ITooltipFlag par4)
	{
		super.addInformation(stack, player, list, par4);
		list.add(stack.getCapability(MRU_HANDLER_ITEM_CAPABILITY, null).getMRU() + "/" + stack.getCapability(MRU_HANDLER_ITEM_CAPABILITY, null).getMaxMRU() + " MRU");
	}

	@Override
	public void getSubItems(CreativeTabs par2CreativeTabs, NonNullList<ItemStack> list)
	{
		if(this.isInCreativeTab(par2CreativeTabs)) {
			ItemStack min = new ItemStack(this, 1, 0);
			ItemStack max = new ItemStack(this, 1, 0);
			min.getCapability(MRU_HANDLER_ITEM_CAPABILITY, null).setMRU(0);
			max.getCapability(MRU_HANDLER_ITEM_CAPABILITY, null).setMRU(maxMRU);
			list.add(min);
			list.add(max);
		}
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
	{
		player.swingArm(hand);
		player.swingProgress = 0.3F;
		return super.onItemRightClick(world, player, hand);
	}

	@Override
	public boolean isFull3D()
	{
		return true;
	}

	@Override
	public float getStrVsBlock(ItemStack stack, IBlockState par2Block)
	{
		if(stack.getCapability(MRU_HANDLER_ITEM_CAPABILITY, null).getMRU() >= 9)
		{
			return 32.0F;
		}
		return 1.0F;
	}

	public boolean canBreak(ItemStack s)
	{
		return s.getCapability(MRU_HANDLER_ITEM_CAPABILITY, null).getMRU()>=9;
	}

	@Override
	public boolean onBlockDestroyed(ItemStack stack, World world, IBlockState par3, BlockPos par4, EntityLivingBase par7EntityLivingBase)
	{
		if(par7EntityLivingBase instanceof EntityPlayer && !par7EntityLivingBase.isSneaking() && this.canBreak(stack))
		{
			this.break3x3x3Blocks((EntityPlayer)par7EntityLivingBase, new Coord3D(par4.getX(),par4.getY(),par4.getZ()),stack,world.getBlockState(par4).getBlock());
		}
		return true;
	}

	public void break3x3x3Blocks(EntityPlayer e, Coord3D c, ItemStack s, Block id)
	{
		for(int x = -1; x <= 1; ++x)
		{
			for(int y = -1; y <= 1; ++y)
			{
				for(int z = -1; z <= 1; ++z)
				{
					Coord3D c00rd = new Coord3D(c.x+x,c.y+y,c.z+z);
					for(int v = 0; v < 10; ++v)
						e.getEntityWorld().spawnParticle(EnumParticleTypes.REDSTONE, c.x+x+e.getEntityWorld().rand.nextFloat(),c.y+y+e.getEntityWorld().rand.nextFloat(),c.z+z+e.getEntityWorld().rand.nextFloat(), 1.0D, 0.0D, 1.0D);
					e.getEntityWorld().playSound(e, e.getPosition(), SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.2F, 6.0F);
					Block b = e.getEntityWorld().getBlockState(new BlockPos((int)c.x+x,(int)c.y+y,(int)c.z+z)).getBlock();
					if(b != null && b == id)
					{
						if(ECUtils.playerUseMRU(e, s, 9) && !e.getEntityWorld().isRemote)
						{
							this.breakBlock(e, c00rd, s);
						}
					}
				}
			}
		}
	}

	public void breakBlock(EntityPlayer e, Coord3D coord, ItemStack s)
	{
		int x = (int) coord.x;
		int y = (int) coord.y;
		int z = (int) coord.z;
		BlockPos p = new BlockPos(x,y,z);
		if(this.canBreak(s))
		{
			Block b = e.getEntityWorld().getBlockState(p).getBlock();
			GameType type = GameType.SURVIVAL;
			if(e.capabilities.isCreativeMode)
				type = GameType.CREATIVE;
			if(!e.capabilities.allowEdit)
				type = GameType.ADVENTURE;

			int be = ForgeHooks.onBlockBreakEvent(e.getEntityWorld(), type, (EntityPlayerMP)e, p);
			if(be != -1)
			{
				b.harvestBlock(e.getEntityWorld(), e, p, e.getEntityWorld().getBlockState(p), e.getEntityWorld().getTileEntity(p), s);
				e.getEntityWorld().setBlockToAir(p);
			}
		}
	}

	@Override
	public boolean canHarvestBlock(IBlockState par1Block, ItemStack itemStack)
	{
		return true;
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
		return new MRUItemStorage(stack, maxMRU);
	}

	@Override
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation("essentialcraft:item/magicaldigger", "inventory"));
	}
}
