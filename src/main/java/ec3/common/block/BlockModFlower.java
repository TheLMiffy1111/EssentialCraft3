package ec3.common.block;

import DummyCore.Client.IModelRegisterer;
import net.minecraft.block.BlockBush;
import net.minecraft.block.SoundType;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

public class BlockModFlower extends BlockBush implements IModelRegisterer {

	public BlockModFlower() {
		super();
		this.setSoundType(SoundType.PLANT);
	}

	@Override
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation("essentialcraft:flowerGreen", "inventory"));
	}
}
