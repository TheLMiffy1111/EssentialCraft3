package essentialcraft.common.entity;

import java.util.Collections;
import java.util.List;

import DummyCore.Utils.MathUtils;
import essentialcraft.api.DemonTrade;
import essentialcraft.common.block.BlockDemonicPentacle;
import essentialcraft.common.item.ItemsCore;
import essentialcraft.common.mod.EssentialCraftCore;
import essentialcraft.common.registry.SoundRegistry;
import essentialcraft.common.tile.TileDemonicPentacle;
import essentialcraft.utils.cfg.Config;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;

public class EntityDemon extends EntityLiving implements IInventory {

	public ItemStack inventory = ItemStack.EMPTY;
	public ItemStack desiredItem = ItemStack.EMPTY;
	public static final DataParameter<ItemStack> DESIRED = EntityDataManager.<ItemStack>createKey(EntityDemon.class, DataSerializers.ITEM_STACK);

	@Override
	protected boolean canDespawn()
	{
		return false;
	}

	public EntityDemon(World w)
	{
		super(w);
		if(w.rand.nextBoolean()) {
			desiredItem = new ItemStack(ItemsCore.soul,1+w.rand.nextInt(7),w.rand.nextInt(DemonTrade.allMobs.size()));
		}
		else {
			desiredItem = DemonTrade.trades.get(DemonTrade.allMobs.size() + w.rand.nextInt(DemonTrade.trades.size() - DemonTrade.allMobs.size())).desiredItem;
		}
	}

	@Override
	protected SoundEvent getAmbientSound()
	{
		return this.getEntityWorld().rand.nextBoolean() ? SoundRegistry.entityDemonSay : SoundRegistry.entityDemonSummon;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource s)
	{
		return SoundRegistry.entityDemonDepart;
	}

	@Override
	public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_)
	{
		this.playSound(getHurtSound(p_70097_1_), 1, 1);
		this.setDead();
		for (int i = 0; i < 400; ++i)
		{
			double d2 = this.rand.nextGaussian() * 0.02D;
			double d0 = this.rand.nextGaussian() * 0.02D;
			double d1 = this.rand.nextGaussian() * 0.02D;
			this.getEntityWorld().spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, this.posX + this.rand.nextFloat() * this.width * 2.0F - this.width, this.posY + this.rand.nextFloat() * this.height, this.posZ + this.rand.nextFloat() * this.width * 2.0F - this.width, d2, d0, d1);
		}
		return false;
	}

	@Override
	public void onUpdate()
	{
		if(!this.getEntityWorld().isRemote)
			this.getDataManager().set(DESIRED,desiredItem);

		super.onUpdate();

		if(!this.getStackInSlot(0).isEmpty() && !this.desiredItem.isEmpty())
		{
			if(this.desiredItem.getItemDamage() != OreDictionary.WILDCARD_VALUE && this.getStackInSlot(0).isItemEqual(desiredItem) && this.getStackInSlot(0).getCount() >= this.desiredItem.getCount() || this.desiredItem.getItemDamage() == OreDictionary.WILDCARD_VALUE && this.getStackInSlot(0).getItem() == this.desiredItem.getItem() && this.getStackInSlot(0).getCount() >= this.desiredItem.getCount())
			{
				this.setDead();
				for (int i = 0; i < 400; ++i)
				{
					double d2 = this.rand.nextGaussian() * 0.02D;
					double d0 = this.rand.nextGaussian() * 0.02D;
					double d1 = this.rand.nextGaussian() * 0.02D;
					this.getEntityWorld().spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, this.posX + this.rand.nextFloat() * this.width * 2.0F - this.width, this.posY + this.rand.nextFloat() * this.height, this.posZ + this.rand.nextFloat() * this.width * 2.0F - this.width, d2, d0, d1);
				}
				this.getEntityWorld().playSound(this.posX,this.posY,this.posZ,SoundRegistry.entityDemonDoom,SoundCategory.HOSTILE, this.getSoundVolume(), this.getSoundPitch(),false);
				ItemStack result = new ItemStack(ItemsCore.genericItem,3+this.getEntityWorld().rand.nextInt(6),52);
				EntityItem itm = new EntityItem(this.getEntityWorld(),this.posX,this.posY,this.posZ,result);
				if(!this.getEntityWorld().isRemote)
					this.getEntityWorld().spawnEntity(itm);
			}
		}

		if(this.getEntityWorld().isRaining() && this.getEntityWorld().canBlockSeeSky(new BlockPos(MathHelper.floor(posX), MathHelper.floor(posY+1), MathHelper.floor(posZ))))
		{
			for(int i = 0; i < 20; ++i)
				this.getEntityWorld().spawnParticle(EnumParticleTypes.SMOKE_NORMAL, posX+MathUtils.randomDouble(getRNG()), posY+1.3D+MathUtils.randomDouble(getRNG())*2, posZ+MathUtils.randomDouble(getRNG()), 0, 0.1D, 0);
		}
		if(this.ticksExisted % 40 == 0)
		{
			for(int dx = -1; dx <= 1; ++dx)
			{
				for(int dz = -1; dz <= 1; ++dz)
				{
					Block b = this.getEntityWorld().getBlockState(new BlockPos(MathHelper.floor(posX)+dx, MathHelper.floor(posY), MathHelper.floor(posZ)+dz)).getBlock();
					if(b instanceof BlockDemonicPentacle)
					{
						TileDemonicPentacle tile = (TileDemonicPentacle) this.getEntityWorld().getTileEntity(new BlockPos(MathHelper.floor(posX)+dx, MathHelper.floor(posY), MathHelper.floor(posZ)+dz));
						if(tile.tier >= 0)
							return;
					}
				}
			}
			this.attackEntityFrom(DamageSource.OUT_OF_WORLD, 1);
		}
		if(this.ticksExisted % 20 == 0)
		{
			List<EntityMob> zombies = this.getEntityWorld().getEntitiesWithinAABB(EntityMob.class, new AxisAlignedBB(posX-0.5D, posY-0.5D, posZ-0.5D, posX+0.5D, posY+0.5D, posZ+0.5D).grow(12, 12, 12));
			if(!zombies.isEmpty())
			{
				EntityMob z = zombies.get(getRNG().nextInt(zombies.size()));
				if(z.isEntityAlive())
				{
					this.swingArm(EnumHand.MAIN_HAND);
					z.attackEntityFrom(DamageSource.causeMobDamage(this), z.getMaxHealth()*1.6F);
					this.getEntityWorld().createExplosion(this, z.posX, z.posY, z.posZ, 2, false);
				}
			}
		}
		if(this.getEntityWorld().isRemote)
			this.desiredItem = this.getDataManager().get(DESIRED);
		//EssentialCraftCore.proxy.SmokeFX(posX,posY+1.5D+MathUtils.randomDouble(getRNG()),posZ,MathUtils.randomDouble(getRNG())/18,-0.09D+MathUtils.randomDouble(getRNG())/18,MathUtils.randomDouble(getRNG())/18,3,1,0.6D-this.getEntityWorld().rand.nextDouble()/3D,0.2D);
	}


	@Override
	protected void entityInit()
	{
		super.entityInit();
		this.dataManager.register(DESIRED, new ItemStack(Items.APPLE,1,0));
	}

	@Override
	public Iterable<ItemStack> getHeldEquipment()
	{
		return Collections.emptySet();
	}

	@Override
	public ItemStack getItemStackFromSlot(EntityEquipmentSlot slotIn)
	{
		return ItemStack.EMPTY;
	}

	@Override
	public void setItemStackToSlot(EntityEquipmentSlot slotIn, ItemStack p_70062_2_) {}

	@Override
	public int getSizeInventory()
	{
		return 1;
	}

	@Override
	public ItemStack getStackInSlot(int slot)
	{
		return this.inventory;
	}

	@Override
	public ItemStack decrStackSize(int slot, int i) {
		this.inventory.shrink(i);
		if(this.inventory.getCount() <= 0)
			this.setInventorySlotContents(0, ItemStack.EMPTY);
		return this.inventory;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stk)
	{
		this.inventory = stk;
	}

	@Override
	public String getName()
	{
		return "demon";
	}

	@Override
	public boolean hasCustomName()
	{
		return false;
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}

	@Override
	public void markDirty() {}

	@Override
	public boolean isUsableByPlayer(EntityPlayer p) {
		return !this.isDead && p.dimension == this.dimension && this.getPositionVector().squareDistanceTo(p.posX, p.posY, p.posZ) <= 64D;
	}

	@Override
	public void openInventory(EntityPlayer p) {}

	@Override
	public void closeInventory(EntityPlayer p) {}

	@Override
	public boolean processInteract(EntityPlayer p, EnumHand hand)
	{
		this.playSound(SoundRegistry.entityDemonTrade, this.getSoundVolume(), this.getSoundPitch());
		p.openGui(EssentialCraftCore.core, Config.guiID[1], this.getEntityWorld(), MathHelper.floor(posX), MathHelper.floor(posY), MathHelper.floor(posZ));
		return true;
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound tag)
	{
		super.writeEntityToNBT(tag);
		if(!this.desiredItem.isEmpty())
		{
			NBTTagCompound itemTag = new NBTTagCompound();
			this.desiredItem.writeToNBT(itemTag);
			tag.setTag("desired", itemTag);
		}
		else
			tag.removeTag("desired");
		if(!this.inventory.isEmpty())
		{
			NBTTagCompound itemTag = new NBTTagCompound();
			this.inventory.writeToNBT(itemTag);
			tag.setTag("inventory", itemTag);
		}
		else
			tag.removeTag("inventory");
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound tag)
	{
		super.readEntityFromNBT(tag);
		if(tag.hasKey("desired"))
			this.desiredItem = new ItemStack(tag.getCompoundTag("desired"));
		if(tag.hasKey("inventory"))
			this.inventory = new ItemStack(tag.getCompoundTag("inventory"));
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stk)
	{
		return true;
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		ItemStack stk = inventory;
		inventory = ItemStack.EMPTY;
		return stk;
	}

	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public void clear() {
		inventory = ItemStack.EMPTY;
	}

	@Override
	public ItemStack getPickedResult(RayTraceResult target) {
		return new ItemStack(ItemsCore.entityEgg,1,EntitiesCore.registeredEntities.indexOf(ForgeRegistries.ENTITIES.getValue(EntityList.getKey(this.getClass()))));
	}

	@Override
	public boolean isEmpty() {
		return inventory.isEmpty();
	}
}
