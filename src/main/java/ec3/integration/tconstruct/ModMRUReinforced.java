package ec3.integration.tconstruct;

import java.util.List;

import com.google.common.collect.ImmutableList;

import ec3.utils.common.ECUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.translation.I18n;
import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.library.modifiers.ModifierAspect.DataAspect;
import slimeknights.tconstruct.library.modifiers.ModifierAspect.FreeFirstModifierAspect;
import slimeknights.tconstruct.library.modifiers.ModifierAspect.LevelAspect;
import slimeknights.tconstruct.library.modifiers.ModifierNBT;
import slimeknights.tconstruct.library.modifiers.ModifierNBT.IntegerNBT;
import slimeknights.tconstruct.library.modifiers.ModifierTrait;
import slimeknights.tconstruct.library.utils.TinkerUtil;

public class ModMRUReinforced extends ModifierTrait {
	public static final ModMRUReinforced INSTANCE = new ModMRUReinforced();

	public ModMRUReinforced() {
		super("mrureinforced", 0xAA00AA, 5, 0);
		aspects.clear();
		addAspects(new LevelAspect(this, maxLevel), new DataAspect(this, color), new FreeFirstModifierAspect(this, 1));
		addItem("matterOfEternity", 1, 1);
	}

	@Override
	public int onToolDamage(ItemStack tool, int damage, int newDamage, EntityLivingBase entity) {
		if(entity.getEntityWorld().isRemote) {
			return 0;
		}

		NBTTagCompound tag = TinkerUtil.getModifierTag(tool, identifier);
		float chance = getReinforcedChance(tag);
		if(chance >= random.nextFloat()) {
			if(entity instanceof EntityPlayer) {
				if(ECUtils.tryToDecreaseMRUInStorage((EntityPlayer)entity, -newDamage*10)) {
					newDamage -= damage;
				}
			}
		}

		return Math.max(0, newDamage);
	}

	@Override
	public String getLocalizedDesc() {
		return String.format(super.getLocalizedDesc(), new Object[] { Util.dfPercent.format(0.2D) });
	}

	@Override
	public List<String> getExtraInfo(ItemStack tool, NBTTagCompound modifierTag) {
		String loc = String.format("modifier.%s.extra", new Object[] { getIdentifier() });
		if(I18n.canTranslate(loc)) {
			float chance = getReinforcedChance(modifierTag);
			String chanceStr = Util.dfPercent.format((double)chance);

			return ImmutableList.of(Util.translateFormatted(loc, new Object[] { chanceStr }));
		}
		else {
			return super.getExtraInfo(tool, modifierTag);
		}
	}

	private float getReinforcedChance(NBTTagCompound modifierTag) {
		IntegerNBT data = ModifierNBT.readInteger(modifierTag);
		return (float)data.level * 0.2F;
	}
}
