package ec3.common.item;

import DummyCore.Client.IModelRegisterer;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemRecord;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.client.model.ModelLoader;

public class ItemRecord_Mod extends ItemRecord implements IModelRegisterer {
	
	public String textureName = "";
	
	protected ItemRecord_Mod(String p_i45350_1_, SoundEvent sound) {
		super(p_i45350_1_, sound);
	}
    
    public ItemRecord_Mod setTextureName(String name) {
    	textureName = name;
    	return this;
    }

	@Override
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation("essentialcraft:item/" + getRegistryName().getResourcePath(), "inventory"));
	}
}
