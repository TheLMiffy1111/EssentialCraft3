package ec3.common.item;

import java.util.List;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import DummyCore.Client.IModelRegisterer;
import DummyCore.Utils.MiscUtils;
import ec3.api.IItemRequiresMRU;
import ec3.utils.common.ECUtils;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

public class ItemHolyMace extends ItemSword implements IItemRequiresMRU, IModelRegisterer /*ItemStoresMRUInNBT*/ {
	
	public ItemHolyMace() {
		super(ItemsCore.elemental);
		this.setMaxMRU(5000);
		this.maxStackSize = 1;
        this.bFull3D = true;
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
	
    public boolean hitEntity(ItemStack p_77644_1_, EntityLivingBase p_77644_2_, EntityLivingBase p_77644_3_)
    {
    	if(p_77644_3_ instanceof EntityPlayer)
    	{
	        if((ECUtils.tryToDecreaseMRUInStorage((EntityPlayer) p_77644_3_, -250) || this.setMRU(p_77644_1_, -250)))
	        {
	        	return true;
	        }
    	}
        return false;
    }
    
	
	@Override
	public void onUpdate(ItemStack s, World world, Entity entity, int indexInInventory, boolean isCurrentItem)
	{
		ECUtils.initMRUTag(s, maxMRU);
	}
	
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot s, ItemStack stk)
    {
        Multimap<String, AttributeModifier> multimap = HashMultimap.<String, AttributeModifier>create();
        if(s == EntityEquipmentSlot.MAINHAND)
        	multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", 12, 0));
        return multimap;
    }

	@Override
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation("essentialcraft:item/holyMace", "inventory"));
	}
}
