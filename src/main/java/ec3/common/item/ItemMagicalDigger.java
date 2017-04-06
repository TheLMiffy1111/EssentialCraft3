package ec3.common.item;

import java.util.List;

import DummyCore.Client.IModelRegisterer;
import DummyCore.Utils.Coord3D;
import DummyCore.Utils.MiscUtils;
import ec3.api.IItemRequiresMRU;
import ec3.utils.common.ECUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.ForgeHooks;

public class ItemMagicalDigger extends ItemPickaxe implements IItemRequiresMRU, IModelRegisterer {

	public ItemMagicalDigger() {
		super(ItemsCore.elemental);
		this.setMaxMRU(5000);
		this.maxStackSize = 1;
		this.bFull3D = false;
		this.setMaxDamage(0);
	}

	int maxMRU = 5000;

	public Item setMaxMRU(int max)
	{
		maxMRU = max;
		return this;
	}

	@Override
	public boolean setMRU(ItemStack stack, int amount) {
		if(MiscUtils.getStackTag(stack).getInteger("mru")+amount >= 0 && MiscUtils.getStackTag(stack).getInteger("mru")+amount<=MiscUtils.getStackTag(stack).getInteger("maxMRU"))
		{
			MiscUtils.getStackTag(stack).setInteger("mru", MiscUtils.getStackTag(stack).getInteger("mru")+amount);
			return true;
		}
		return false;
	}

	@Override
	public int getMRU(ItemStack stack) {
		return MiscUtils.getStackTag(stack).getInteger("mru");
	}

	public boolean isItemTool(ItemStack p_77616_1_)
	{
		return true;
	}

	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List<String> par3List, boolean par4) 
	{
		super.addInformation(par1ItemStack, par2EntityPlayer, par3List, par4);
		par3List.add(ECUtils.getStackTag(par1ItemStack).getInteger("mru") + "/" + ECUtils.getStackTag(par1ItemStack).getInteger("maxMRU") + " MRU");
	}

	@Override
	public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List<ItemStack> par3List)
	{
		for (int var4 = 0; var4 < 1; ++var4)
		{
			ItemStack min = new ItemStack(par1, 1, 0);
			ECUtils.initMRUTag(min, maxMRU);
			ItemStack max = new ItemStack(par1, 1, 0);
			ECUtils.initMRUTag(max, maxMRU);
			ECUtils.getStackTag(max).setInteger("mru", ECUtils.getStackTag(max).getInteger("maxMRU"));
			par3List.add(min);
			par3List.add(max);
		}
	}

	@Override
	public int getMaxMRU(ItemStack stack) {
		return this.maxMRU;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer, EnumHand hand)
	{
		par3EntityPlayer.swingArm(hand);
		par3EntityPlayer.swingProgress = 0.3F;
		return new ActionResult(EnumActionResult.PASS,par1ItemStack);
	}

	@Override
	public boolean isFull3D()
	{
		return true;
	}

	public float getStrVsBlock(ItemStack par1ItemStack, IBlockState par2Block)
	{
		if(this.getMRU(par1ItemStack) > 9)
		{
			return 32.0F;
		}
		return 1.0F;
	}

	public boolean canBreak(ItemStack s)
	{
		return this.getMRU(s)>9;
	}

	@Override
	public boolean onBlockDestroyed(ItemStack par1ItemStack, World par2World, IBlockState par3, BlockPos par4, EntityLivingBase par7EntityLivingBase)
	{
		if(par7EntityLivingBase instanceof EntityPlayer && !par7EntityLivingBase.isSneaking() && this.canBreak(par1ItemStack))
		{
			this.break3x3x3Blocks((EntityPlayer)par7EntityLivingBase, new Coord3D(par4.getX(),par4.getY(),par4.getZ()),par1ItemStack,par2World.getBlockState(par4).getBlock());
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
						if(!e.getEntityWorld().isRemote &&(ECUtils.tryToDecreaseMRUInStorage(e, -9) || this.setMRU(s, -9)))
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
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation("essentialcraft:item/magicalDigger", "inventory"));
	}
}
