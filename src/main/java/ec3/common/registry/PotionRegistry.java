package ec3.common.registry;

import DummyCore.Utils.MiscUtils;
import ec3.common.potion.PotionChaosInfluence;
import ec3.common.potion.PotionFrozenMind;
import ec3.common.potion.PotionMRUCorruption;
import ec3.common.potion.PotionMindfoldParadox;
import ec3.common.potion.PotionPurpleFlame;
import ec3.common.potion.PotionRadiation;
import ec3.common.potion.PotionShadeCorruption;
import ec3.common.potion.PotionUnnormalLightness;
import ec3.common.potion.PotionWindTouch;
import net.minecraft.potion.Potion;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class PotionRegistry {

	public static void registerPotions()
	{
		mruCorruptionPotion = new PotionMRUCorruption(0, true, 0xff00ff);
		chaosInfluence = new PotionChaosInfluence(0, true, 0xff0000);
		frozenMind = new PotionFrozenMind(0, true, 0x0000ff);
		windTouch = new PotionWindTouch(0, true, 0xccffcc);
		paranormalLightness = new PotionUnnormalLightness(0, true, 0xffffcc);
		radiation = new PotionRadiation(0, true, 0x660066);
		paradox = new PotionMindfoldParadox(0, true, 0xffffff);
		
		GameRegistry.register(mruCorruptionPotion);
		GameRegistry.register(chaosInfluence);
		GameRegistry.register(frozenMind);
		GameRegistry.register(windTouch);
		GameRegistry.register(paranormalLightness);
		GameRegistry.register(radiation);
		GameRegistry.register(paradox);
	}

	public static PotionMRUCorruption mruCorruptionPotion;
	public static PotionFrozenMind frozenMind;
	public static PotionChaosInfluence chaosInfluence;
	public static PotionWindTouch windTouch;
	public static PotionUnnormalLightness paranormalLightness;
	public static PotionRadiation radiation;
	public static PotionShadeCorruption shadeCorruption;
	public static PotionPurpleFlame purpleFlame;
	public static PotionMindfoldParadox paradox;
}
