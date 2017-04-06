package ec3.common.item;

import DummyCore.Client.IModelRegisterer;
import ec3.utils.common.WindRelations;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

public class ItemWindKeeper extends Item implements IModelRegisterer {

	public int rel;
	public String textureName;

	public ItemWindKeeper(int windReleased)
	{
		super();
		rel = windReleased;
	}

	public ItemWindKeeper setTextureName(String name) {
		textureName = name;
		return this;
	}

	public ActionResult<ItemStack> onItemRightClick(ItemStack p_77659_1_, World p_77659_2_, EntityPlayer p_77659_3_, EnumHand p_77659_4_)
	{
		--p_77659_1_.stackSize;
		p_77659_3_.playSound(SoundEvents.BLOCK_GLASS_BREAK, 1, 1);
		if(!p_77659_2_.isRemote)
		{
			WindRelations.increasePlayerWindRelations(p_77659_3_, rel);
			p_77659_3_.sendMessage(new TextComponentString(TextFormatting.DARK_AQUA+""+TextFormatting.ITALIC+"You hear a very pale 'Thank you'..."));
		}
		return new ActionResult(EnumActionResult.PASS, p_77659_1_);
	}

	@Override
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation("essentialcraft:item/" + getRegistryName().getResourcePath(), "inventory"));
	}
}
