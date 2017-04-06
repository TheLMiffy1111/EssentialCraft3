package ec3.common.block;

import java.util.Random;

import DummyCore.Client.IModelRegisterer;
import ec3.common.item.ItemsCore;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

public class BlockMagicalFruit extends Block implements IModelRegisterer {

	public BlockMagicalFruit() {
		super(Material.CACTUS);
		this.setSoundType(SoundType.PLANT);
	}

	public Item getItemDropped(IBlockState p_149650_1_, Random p_149650_2_, int p_149650_3_)
	{
		return ItemsCore.fruit;
	}

	/**
	 * Returns the quantity of items to drop on block destruction.
	 */
	public int quantityDropped(Random p_149745_1_)
	{
		return 3 + p_149745_1_.nextInt(5);
	}

	/**
	 * Returns the usual quantity dropped by the block plus a bonus of 1 to 'i' (inclusive).
	 */
	public int quantityDroppedWithBonus(int p_149679_1_, Random p_149679_2_)
	{
		int j = this.quantityDropped(p_149679_2_) + p_149679_2_.nextInt(1 + p_149679_1_);

		if (j > 9)
		{
			j = 9;
		}

		return j;
	}

	@Override
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation("essentialcraft:fruit", "inventory"));
	}
}
