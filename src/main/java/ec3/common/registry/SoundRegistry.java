package ec3.common.registry;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class SoundRegistry {
	public static void init() {
		recordLetsBeFriends = GameRegistry.register(new SoundEvent(new ResourceLocation("essentialcraft:records.letsbefriends")).setRegistryName("essentialcraft:records.letsbefriends"));
		recordArstotzkan = GameRegistry.register(new SoundEvent(new ResourceLocation("essentialcraft:records.arstotzkan")).setRegistryName("essentialcraft:records.arstotzkan"));
		recordSecret = GameRegistry.register(new SoundEvent(new ResourceLocation("essentialcraft:records.secret")).setRegistryName("essentialcraft:records.secret"));
		recordRopocalypse = GameRegistry.register(new SoundEvent(new ResourceLocation("essentialcraft:records.hologram")).setRegistryName("essentialcraft:records.hologram"));
		bookPageTurn = GameRegistry.register(new SoundEvent(new ResourceLocation("essentialcraft:sound.pageturn")).setRegistryName("essentialcraft:sound.pageturn"));
		machineDeepNoise = GameRegistry.register(new SoundEvent(new ResourceLocation("essentialcraft:sound.deepnoise")).setRegistryName("essentialcraft:sound.deepnoise"));
		potionTinnitus = GameRegistry.register(new SoundEvent(new ResourceLocation("essentialcraft:sound.tinnitus")).setRegistryName("essentialcraft:sound.tinnitus"));
		potionHeartbeat = GameRegistry.register(new SoundEvent(new ResourceLocation("essentialcraft:sound.heartbeat")).setRegistryName("essentialcraft:sound.heartbeat"));
		machineGenElectricity = GameRegistry.register(new SoundEvent(new ResourceLocation("essentialcraft:sound.gen_electricity")).setRegistryName("essentialcraft:sound.gen_electricity"));
		machineLightningHit = GameRegistry.register(new SoundEvent(new ResourceLocation("essentialcraft:sound.lightning_hit")).setRegistryName("essentialcraft:sound.lightning_hit"));
		entityMRUCUNoise = GameRegistry.register(new SoundEvent(new ResourceLocation("essentialcraft:sound.mrucu_noise")).setRegistryName("essentialcraft:sound.mrucu_noise"));
		gunBeam = GameRegistry.register(new SoundEvent(new ResourceLocation("essentialcraft:sound.beam")).setRegistryName("essentialcraft:sound.beam"));
		entityDemonSummon = GameRegistry.register(new SoundEvent(new ResourceLocation("essentialcraft:sound.mob.demon.summon")).setRegistryName("essentialcraft:sound.mob.demon.summon"));
		entityDemonSay = GameRegistry.register(new SoundEvent(new ResourceLocation("essentialcraft:sound.mob.demon.say")).setRegistryName("essentialcraft:sound.mob.demon.say"));
		entityDemonDepart = GameRegistry.register(new SoundEvent(new ResourceLocation("essentialcraft:sound.mob.demon.depart")).setRegistryName("essentialcraft:sound.mob.demon.depart"));
		entityDemonDoom = GameRegistry.register(new SoundEvent(new ResourceLocation("essentialcraft:sound.mob.demon.doom")).setRegistryName("essentialcraft:sound.mob.demon.doom"));
		entityDemonTrade = GameRegistry.register(new SoundEvent(new ResourceLocation("essentialcraft:sound.mob.demon.trade")).setRegistryName("essentialcraft:sound.mob.demon.trade"));
		entityHologramDamageMelee = GameRegistry.register(new SoundEvent(new ResourceLocation("essentialcraft:sound.mob.hologram.damage.melee")).setRegistryName("essentialcraft:sound.mob.hologram.damage.melee"));
		entityHologramDamageProjectile = GameRegistry.register(new SoundEvent(new ResourceLocation("essentialcraft:sound.mob.hologram.damage.projectile")).setRegistryName("essentialcraft:sound.mob.hologram.damage.projectile"));
		entityHologramShutdown = GameRegistry.register(new SoundEvent(new ResourceLocation("essentialcraft:sound.mob.hologram.shutdown")).setRegistryName("essentialcraft:sound.mob.hologram.shutdown"));
		entityHologramStop = GameRegistry.register(new SoundEvent(new ResourceLocation("essentialcraft:sound.mob.hologram.stop")).setRegistryName("essentialcraft:sound.mob.hologram.stop"));
		entityHologramStart = GameRegistry.register(new SoundEvent(new ResourceLocation("essentialcraft:sound.mob.hologram.start")).setRegistryName("essentialcraft:sound.mob.hologram.start"));
		entityOrbitalStrike = GameRegistry.register(new SoundEvent(new ResourceLocation("essentialcraft:sound.orbital_strike")).setRegistryName("essentialcraft:sound.orbital_strike"));
	}
	public static SoundEvent recordLetsBeFriends;
	public static SoundEvent recordArstotzkan;
	public static SoundEvent recordSecret;
	public static SoundEvent recordRopocalypse;
	public static SoundEvent bookPageTurn;
	public static SoundEvent machineDeepNoise;
	public static SoundEvent potionTinnitus;
	public static SoundEvent potionHeartbeat;
	public static SoundEvent machineGenElectricity;
	public static SoundEvent machineLightningHit;
	public static SoundEvent entityMRUCUNoise;
	public static SoundEvent gunBeam;
	public static SoundEvent entityDemonSummon;
	public static SoundEvent entityDemonSay;
	public static SoundEvent entityDemonDepart;
	public static SoundEvent entityDemonDoom;
	public static SoundEvent entityDemonTrade;
	public static SoundEvent entityHologramDamageMelee;
	public static SoundEvent entityHologramDamageProjectile;
	public static SoundEvent entityHologramShutdown;
	public static SoundEvent entityHologramStop;
	public static SoundEvent entityHologramStart;
	public static SoundEvent entityOrbitalStrike;
}
