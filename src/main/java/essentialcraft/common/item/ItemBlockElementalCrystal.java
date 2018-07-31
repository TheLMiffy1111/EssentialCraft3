package essentialcraft.common.item;

import DummyCore.Client.IItemColor;
import DummyCore.Utils.MiscUtils;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockElementalCrystal extends ItemBlock implements IItemColor {

	public ItemBlockElementalCrystal(Block block) {
		super(block);
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
	}

	//Neutral: D5F3F4  213 243 244
	//Fire:    7D210B  130  33  11
	//Water:   3298D4   50 152 212
	//Earth:   114F1C   17  79  28
	//Air:     858D92  133 141 146

	@Override
	public int getColorFromItemstack(ItemStack stack, int tintIndex) {
		double fire = 0D;
		double water = 0D;
		double earth = 0D;
		double air = 0D;
		double neutral = 10D;
		if(MiscUtils.getStackTag(stack) != null) {
			fire = MiscUtils.getStackTag(stack).getFloat("fire");
			water = MiscUtils.getStackTag(stack).getFloat("water");
			earth = MiscUtils.getStackTag(stack).getFloat("earth");
			air = MiscUtils.getStackTag(stack).getFloat("air");
		}
		double total = fire+water+earth+air+neutral;
		fire /= total;
		water /= total;
		earth /= total;
		air /= total;
		neutral /= total;

		double red = 130D/255D*fire+50D/255D*water+17D/255D*earth+133D/255D*air+213D/255D*neutral;
		double green = 33D/255D*fire+152D/255D*water+79D/255D*earth+141D/255D*air+243D/255D*neutral;
		double blue = 11D/255D*fire+212D/255D*water+28D/255D*earth+146D/255D*air+244D/255D*neutral;

		return ((int)(red*255D)<<16)+((int)(blue*255D)<<8)+(int)(green*255D);
	}
}
