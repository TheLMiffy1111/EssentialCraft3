package ec3.common.item;

import ec3.api.ApiCore;
import ec3.api.MagicianTableUpgrades;
import ec3.common.mod.EssentialCraftCore;
import ec3.common.registry.SoundRegistry;
import DummyCore.Items.ItemRegistry;
import DummyCore.Utils.MiscUtils;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;

import static ec3.utils.cfg.Config.getIdForItem;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class ItemsCore {
	
	@SuppressWarnings("deprecation")
	public static void loadItems() {
		elemental = EnumHelper.addToolMaterial("elemental", 6, 3568, 15.0F, 5.0F, 36);
		weakElemental = EnumHelper.addToolMaterial("weakElemental", 3, 754, 7.0F, 2.5F, 36);
		windElemental = EnumHelper.addToolMaterial("windElemental", 11, 15684, 15.0F, 6F, 42);
		shade = EnumHelper.addToolMaterial("shade", 32, 0, 1.0F, 17.0F, 12);
		testingItem = new TestItem_EC().setUnlocalizedName("essentialcraft.testItem").setMaxStackSize(1).setFull3D();
		registerItemSimple(testingItem,"testItem");
		mruMover1 = new ItemMRUMover().setTextureName("essentialcraft:tools/mru_mover_t1").setUnlocalizedName("essentialcraft.mruMover1").setMaxStackSize(1).setFull3D();
		registerItemSimple(mruMover1,"mruMover1");
		drops = new ItemDrop().setUnlocalizedName("essentialcraft.drops_").setMaxStackSize(64);
		registerItemSimple(drops, "drops");
		elemental_pick = new ItemPickaxe_Mod(elemental).setTextureName("essentialcraft:tools/elemental_pickaxe").setUnlocalizedName("essentialcraft.elemental_pick").setMaxStackSize(1).setFull3D();
		registerItemSimple(elemental_pick, "elemental_pick");
		elemental_axe = new ItemAxe_Mod(elemental).setTextureName("essentialcraft:tools/elemental_axe").setUnlocalizedName("essentialcraft.elemental_axe").setMaxStackSize(1).setFull3D();
		registerItemSimple(elemental_axe, "elemental_axe");
		elemental_hoe = new ItemHoe_Mod(elemental).setTextureName("essentialcraft:tools/elemental_hoe").setUnlocalizedName("essentialcraft.elemental_hoe").setMaxStackSize(1).setFull3D();
		registerItemSimple(elemental_hoe, "elemental_hoe");
		elemental_shovel = new ItemShovel_Mod(elemental).setTextureName("essentialcraft:tools/elemental_shovel").setUnlocalizedName("essentialcraft.elemental_shovel").setMaxStackSize(1).setFull3D();
		registerItemSimple(elemental_shovel, "elemental_shovel");
		elemental_sword = new ItemSword_Mod(elemental).setTextureName("essentialcraft:tools/elemental_sword").setUnlocalizedName("essentialcraft.elemental_sword").setMaxStackSize(1).setFull3D();
		registerItemSimple(elemental_sword, "elemental_sword");
		bound_gem = new ItemBoundGem().setUnlocalizedName("essentialcraft.bound_gem");
		registerItemSimple(bound_gem, "bound_gem");
		magicMonocle = registerItemSimple(magicMonocle,ItemMonocle.class,"magicMonocle","tools/magicMonocle",16,true,1);
		
		record_everlastingSummer = new ItemRecord_Mod("letsbefriends", SoundRegistry.recordLetsBeFriends).setTextureName("record_blocks").setUnlocalizedName("essentialcraft.record");
		registerItemSimple(record_everlastingSummer, "record");
		
		record_papersPlease = new ItemRecord_Mod("arstotzkan", SoundRegistry.recordArstotzkan).setTextureName("record_far").setUnlocalizedName("essentialcraft.record_a");
		registerItemSimple(record_papersPlease, "record_a");
		
		record_secret = new ItemRecord_Mod("ecsecret", SoundRegistry.recordSecret).setTextureName("record_13").setUnlocalizedName("essentialcraft.record_secret");
		registerItemSimple(record_secret, "record_secret");
		
		soulStone = registerItemSimple(soulStone,ItemSoulStone.class,"soulStone","matrix/soulStone",0,false,1);
		matrixProj = registerItemSimple(matrixProj,ItemMRUMatrixProjection.class,"mruMatrixProjection","mruMatrix_empty",0,false,1);
		titanite = registerItemSimple(titanite,ItemMod.class,"titanite","misc/titanite",0,false,64);
		twinkling_titanite = registerItemSimple(twinkling_titanite,ItemMod.class,"twinkling_titanite","misc/twinkling_titanite",0,false,64);
		secret = registerItemSimple(secret,ItemSecret.class,"secret","soulStone",0,false,1);
		magicalSlag = registerItemSimple(magicalSlag,ItemMod.class,"magicalSlag","misc/magicalSlag",0,false,64);
		genericItem = registerItemSimple(genericItem,ItemGenericEC3.class,"genItem","magicalSlag",0,false,64);
		magicalAlloy = registerItemSimple(magicalAlloy,ItemMagicalAlloy.class,"magicalAlloy","magicalSlag",0,false,64);
		essence = registerItemSimple(essence,ItemEssence.class,"essence","magicalSlag",0,false,16);
		
		storage = new ItemMRUStorageNBTTag(new int[]{500,5000,30000,120000,1000000}).setUnlocalizedName("essentialcraft.storage");
		registerItemSimple(storage, "storage");
		
		magicalDigger = registerItemSimple(magicalDigger,ItemMagicalDigger.class,"magicalDigger","tools/magicalDigger",0,true,1);
		spawnerCollector = registerItemSimple(spawnerCollector,ItemSpawnerCollector.class,"spawnerCollector","tools/spawnerCollector",0,true,1);
		staffOfLife = registerItemSimple(staffOfLife,ItemLifeStaff.class,"staffOfLife","tools/staffOfLife",0,true,1);
		biomeWand = registerItemSimple(biomeWand,ItemBiomeWand.class,"biomeWand","tools/biomeWand",0,true,1);
		
		emeraldHeart = registerItemSimple(emeraldHeart,ItemEmeraldHeart.class,"emeraldHeart","tools/emeraldHeart",0,true,1);
		magicalShield = registerItemSimple(magicalShield,ItemMagicalShield.class,"magicalShield","tools/magicalShield",0,true,1);
		spikyShield = registerItemSimple(spikyShield,ItemSpikyShield.class,"spikyShield","tools/spikyShield",0,true,1);
		magicWaterBottle = registerItemSimple(magicWaterBottle,ItemMagicalWater.class,"magicWaterBottle","tools/magicWaterBottle",0,false,1);
		magicalPorkchop = registerItemSimple(magicalPorkchop,ItemMagicalPorkchop.class,"magicalPorkchop","tools/magicalPorkchop",0,false,1);
		charm = registerItemSimple(charm,ItemCharm.class,"charm","tools/charmFire",0,false,1);
		magicalWings = registerItemSimple(magicalWings,ItemMagicalWings.class,"magicalWings","tools/magicalWings",0,false,1);
		
		chaosFork = registerItemSimple(chaosFork,ItemChaosFork.class,"chaosFork","tools/chaosFork",0,true,1);
		frozenMace = registerItemSimple(frozenMace,ItemFrostMace.class,"frozenMace","tools/frozenMace",0,true,1);
		holyMace = registerItemSimple(holyMace,ItemHolyMace.class,"holyMace","tools/holyMace",0,true,1);
		
		elementalFuel = registerItemSimple(elementalFuel,ItemEssentialFuel.class,"elementalFuel","tools/emeraldHeart",0,false,16);
		
		fFocus = new ItemElementalFocus().setTextureName("essentialcraft:elemental/eFocusFire").setUnlocalizedName("essentialcraft.fFocus");
		registerItemSimple(fFocus, "fFocus");
		wFocus = new ItemElementalFocus().setTextureName("essentialcraft:elemental/eFocusWater").setUnlocalizedName("essentialcraft.wFocus");
		registerItemSimple(wFocus, "wFocus");
		eFocus = new ItemElementalFocus().setTextureName("essentialcraft:elemental/eFocusEarth").setUnlocalizedName("essentialcraft.eFocus");
		registerItemSimple(eFocus, "eFocus");
		aFocus = new ItemElementalFocus().setTextureName("essentialcraft:elemental/eFocusAir").setUnlocalizedName("essentialcraft.aFocus");
		registerItemSimple(aFocus, "aFocus");
		
		fruit = new ItemFruit(8, 10F, false).setUnlocalizedName("essentialcraft.fruit_Item");
		registerItemSimple(fruit, "fruit_Item");
		
		bottledWind = new ItemWindKeeper(10).setTextureName("essentialcraft:bottledWind").setUnlocalizedName("essentialcraft.bottledWind");
		registerItemSimple(bottledWind, "bottledWind");
		
		imprisonedWind = new ItemWindKeeper(25).setTextureName("essentialcraft:imprisonedWind").setUnlocalizedName("essentialcraft.imprisonedWind");
		registerItemSimple(imprisonedWind, "imprisonedWind");
		
		windKeeper = new ItemWindKeeper(100).setTextureName("essentialcraft:windkeeper").setUnlocalizedName("essentialcraft.windKeeper");
		registerItemSimple(windKeeper, "windKeeper");
		
		windTablet = registerItemSimple(windTablet,ItemWindTablet.class,"windTablet","windTablet",0,false,1);
		
		elementalSword = registerItemSimple(elementalSword,ItemElementalSword.class,"elementalSword","tools/elementalSword",0,true,1);
		ember = registerItemSimple(ember,ItemEmber.class,"ember","windTablet",0,false,16);
		
		
		
		magicArmorItems[0] = new ItemArmorMod(magicArmorMaterial, 4, 0, 0).setArmorTexture("armorEC3Magic").setUnlocalizedName("essentialcraft.helm_magic").setMaxDamage(512);
		registerItemSimple(magicArmorItems[0], "helm_magic");
		ApiCore.allowItemToSeeMRU(magicArmorItems[0]);
		ApiCore.setArmorProperties(magicArmorItems[0], 0.25F, 0.125F, 0.10F);
		
		magicArmorItems[1] = new ItemArmorMod(magicArmorMaterial, 4, 1, 0).setArmorTexture("armorEC3Magic").setUnlocalizedName("essentialcraft.chest_magic").setMaxDamage(1536);
		registerItemSimple(magicArmorItems[1], "chest_magic");
		ApiCore.setArmorProperties(magicArmorItems[1], 0.25F, 0.125F, 0.10F);
		
		magicArmorItems[2] = new ItemArmorMod(magicArmorMaterial, 4, 2, 0).setArmorTexture("armorEC3Magic").setUnlocalizedName("essentialcraft.legs_magic").setMaxDamage(1024);
		registerItemSimple(magicArmorItems[2], "legs_magic");
		ApiCore.setArmorProperties(magicArmorItems[2], 0.25F, 0.125F, 0.10F);
		
		magicArmorItems[3] = new ItemArmorMod(magicArmorMaterial, 4, 3, 0).setArmorTexture("armorEC3Magic").setUnlocalizedName("essentialcraft.boots_magic").setMaxDamage(512);
		registerItemSimple(magicArmorItems[3], "boots_magic");
		ApiCore.setArmorProperties(magicArmorItems[3], 0.25F, 0.125F, 0.10F);
		
		
		
		magicArmorItems[4] = new ItemArmorMod(voidArmorMaterial, 4, 0, 1).setArmorTexture("armorEC3Void").setDescription(TextFormatting.ITALIC+"Converts life energy of fallen foes to your hunger").setUnlocalizedName("essentialcraft.helm_void").setMaxDamage(2048);
		registerItemSimple(magicArmorItems[4], "helm_void");
		ApiCore.allowItemToSeeMRU(magicArmorItems[4]);
		ApiCore.setArmorProperties(magicArmorItems[4], 1F, 0.5F, 0.75F);
		
		magicArmorItems[5] = new ItemArmorMod(voidArmorMaterial, 4, 1, 1).setArmorTexture("armorEC3Void").setDescription(TextFormatting.ITALIC+"Can dodge 20% of attacks").setUnlocalizedName("essentialcraft.chest_void").setMaxDamage(6144);
		registerItemSimple(magicArmorItems[5], "chest_void");
		ApiCore.setArmorProperties(magicArmorItems[5], 1F, 0.5F, 0.75F);
		
		magicArmorItems[6] = new ItemArmorMod(voidArmorMaterial, 4, 2, 1).setArmorTexture("armorEC3Void").setDescription(TextFormatting.ITALIC+"Can phase-shift through monsters").setUnlocalizedName("essentialcraft.legs_void").setMaxDamage(4096);
		registerItemSimple(magicArmorItems[6], "legs_void");
		ApiCore.setArmorProperties(magicArmorItems[6], 1F, 0.5F, 0.75F);
		
		magicArmorItems[7] = new ItemArmorMod(voidArmorMaterial, 4, 3, 1).setArmorTexture("armorEC3Void").setDescription(TextFormatting.ITALIC+"Negates 90% of falldamage").setUnlocalizedName("essentialcraft.boots_void").setMaxDamage(2048);
		registerItemSimple(magicArmorItems[7], "boots_void");
		ApiCore.setArmorProperties(magicArmorItems[7], 1F, 0.5F, 0.75F);
		
		
		
		magicArmorItems[8] = new ItemArmorMod(inquisArmorMaterial, 4, 0, 2).setArmorTexture("armorEC3Inquisitorium").setDescription(TextFormatting.ITALIC+"Linses created from the moonlight metal to see unpurity").setUnlocalizedName("essentialcraft.helm_inq").setMaxDamage(5120);
		registerItemSimple(magicArmorItems[8], "helm_inq");
		ApiCore.allowItemToSeeMRU(magicArmorItems[8]);
		ApiCore.setArmorProperties(magicArmorItems[8], 1F, 1F, 1F);
		
		magicArmorItems[9] = new ItemArmorMod(inquisArmorMaterial, 4, 1, 2).setArmorTexture("armorEC3Inquisitorium").setDescription(TextFormatting.ITALIC+"Be quiet, Dominique!").setUnlocalizedName("essentialcraft.chest_inq").setMaxDamage(15360);
		registerItemSimple(magicArmorItems[9], "chest_inq");
		ApiCore.setArmorProperties(magicArmorItems[9], 1F, 1F, 1F);
		
		magicArmorItems[10] = new ItemArmorMod(inquisArmorMaterial, 4, 2, 2).setArmorTexture("armorEC3Inquisitorium").setDescription(TextFormatting.ITALIC+"Magic is a parasite. It must be wiped out!").setUnlocalizedName("essentialcraft.legs_inq").setMaxDamage(10240);
		registerItemSimple(magicArmorItems[10], "legs_inq");
		ApiCore.setArmorProperties(magicArmorItems[10], 1F, 1F, 1F);
		
		magicArmorItems[11] = new ItemArmorMod(inquisArmorMaterial, 4, 3, 2).setArmorTexture("armorEC3Inquisitorium").setDescription(TextFormatting.ITALIC+"Magic shall not prevail!").setUnlocalizedName("essentialcraft.boots_inq").setMaxDamage(5120);
		registerItemSimple(magicArmorItems[11], "boots_inq");
		ApiCore.setArmorProperties(magicArmorItems[11], 1F, 1F, 1F);
		
		
		
		magicArmorItems[12] = new ItemArmorMod(ArmorMaterial.LEATHER, 4, 0, 3).setArmorTexture("armorEC3Wind").setUnlocalizedName("essentialcraft.helm_wind").setMaxDamage(128);
		registerItemSimple(magicArmorItems[12], "helm_wind");
		ApiCore.allowItemToSeeMRU(magicArmorItems[12]);
		
		magicArmorItems[13] = new ItemArmorMod(ArmorMaterial.LEATHER, 4, 1, 3).setArmorTexture("armorEC3Wind").setUnlocalizedName("essentialcraft.chest_wind").setMaxDamage(384);
		registerItemSimple(magicArmorItems[13], "chest_wind");
		
		magicArmorItems[14] = new ItemArmorMod(ArmorMaterial.LEATHER, 4, 2, 3).setArmorTexture("armorEC3Wind").setUnlocalizedName("essentialcraft.legs_wind").setMaxDamage(256);
		registerItemSimple(magicArmorItems[14], "legs_wind");
		
		magicArmorItems[15] = new ItemArmorMod(ArmorMaterial.LEATHER, 4, 3, 3).setArmorTexture("armorEC3Wind").setUnlocalizedName("essentialcraft.boots_wind").setMaxDamage(128);
		registerItemSimple(magicArmorItems[15], "boots_wind");
		
		
		
		bauble = registerItemSimple(bauble,ItemBaublesWearable.class,"bauble","windTablet",0,false,1);
		
		magmaticStaff = registerItemSimple(magmaticStaff,ItemMagmaticWand.class,"magmaticStaff","tools/magmaticWand",0,true,1);
		magicalLantern = registerItemSimple(magicalLantern,ItemMagicLantern.class,"magicalLantern","tools/elementalPurifier",0,true,1);
		magnetizingStaff = registerItemSimple(magnetizingStaff,ItemMagnetizingStaff.class,"magnetizingStaff","tools/magnetizingStaff",0,true,1);
		
		research_book = registerItemSimple(research_book,ItemKnowledgeBook.class,"research_book","book_knowledge",0,false,1);
	
		air_potion = registerItemSimple(air_potion,ItemLiquidAir.class,"air_potion","fluidAir",0,false,64);
		mruMover_t2 = new ItemMRUMover().setTextureName("essentialcraft:tools/mru_mover_t2").setUnlocalizedName("essentialcraft.mruMover_t2").setMaxDamage(256*6).setMaxStackSize(1).setFull3D();
		registerItemSimple(mruMover_t2, "mruMover_t2");
		playerList = registerItemSimple(playerList,ItemPlayerList.class,"playerList","minecraft:paper",0,false,1);
		magicalChisel = registerItemSimple(magicalChisel,ItemMagicalChisel.class,"magicalChisel","tools/magicalChisel",256,true,1);
		filter = registerItemSimple(filter,ItemFilter.class,"itemFilter","itemFilter",0,false,1);
		controlRod = registerItemSimple(controlRod,ItemControlRod.class,"controlRod","tools/controlRod",0,true,1);
		baublesCore = registerItemSimple(baublesCore,BaublesModifier.class,"baublesCore","???",0,false,1);
		ApiCore.allowsSeeingMRU.add(mruMover_t2);
		
		shadeSword = registerItemSimple(shadeSword,ItemShadeSword.class,"shadeSword","tools/swordOfTheSHADE",0,true,1);
		shadeSlasher = registerItemSimple(shadeSlasher,ItemShadeSlasher.class,"shadeSlasher","tools/slasherOfTheSHADE",0,true,1);
		shadeKnife = registerItemSimple(shadeKnife,ItemShadowKnife.class,"shadeKnife","tools/shadowKnife",0,true,32);
		entityEgg = registerItemSimple(entityEgg,ItemSpawnEGGEC3.class,"entityEgg","fruit",0,false,64);
		
		pistol = new ItemGun("pistol").setUnlocalizedName("ec3.gun.pistol");
		registerItemSimple(pistol, "gun.pistol");
		rifle = new ItemGun("rifle").setUnlocalizedName("ec3.gun.rifle");
		registerItemSimple(rifle, "gun.rifle");
		sniper = new ItemGun("sniper").setUnlocalizedName("ec3.gun.sniper");
		registerItemSimple(sniper, "gun.sniper");
		gatling = new ItemGun("gatling").setUnlocalizedName("ec3.gun.gatling");
		registerItemSimple(gatling, "gun.gatling");
		
		weak_elemental_pick = new ItemPickaxe_Mod(weakElemental).setTextureName("essentialcraft:tools/weak_pickaxe").setUnlocalizedName("essentialcraft.weak_elemental_pick").setMaxStackSize(1).setFull3D();
		registerItemSimple(weak_elemental_pick, "weak_elemental_pick");
		weak_elemental_axe = new ItemAxe_Mod(weakElemental).setTextureName("essentialcraft:tools/weak_axe").setUnlocalizedName("essentialcraft.weak_elemental_axe").setMaxStackSize(1).setFull3D();
		registerItemSimple(weak_elemental_axe,"weak_elemental_axe");
		weak_elemental_hoe = new ItemHoe_Mod(weakElemental).setTextureName("essentialcraft:tools/weak_hoe").setUnlocalizedName("essentialcraft.weak_elemental_hoe").setMaxStackSize(1).setFull3D();
		registerItemSimple(weak_elemental_hoe,"weak_elemental_hoe");
		weak_elemental_shovel = new ItemShovel_Mod(weakElemental).setTextureName("essentialcraft:tools/weak_shovel").setUnlocalizedName("essentialcraft.weak_elemental_shovel").setMaxStackSize(1).setFull3D();
		registerItemSimple(weak_elemental_shovel, "weak_elemental_shovel");
		weak_elemental_sword = new ItemSword_Mod(weakElemental).setTextureName("essentialcraft:tools/weak_sword").setUnlocalizedName("essentialcraft.weak_elemental_sword").setMaxStackSize(1).setFull3D();
		registerItemSimple(weak_elemental_sword, "weak_elemental_sword");
		
		wind_elemental_pick = new ItemWindPickaxe(windElemental).setTextureName("essentialcraft:tools/wind_pickaxe").setUnlocalizedName("essentialcraft.wind_elemental_pick").setMaxStackSize(1).setFull3D();
		registerItemSimple(wind_elemental_pick,"wind_elemental_pick");
		wind_elemental_shovel = new ItemWindShovel(windElemental).setTextureName("essentialcraft:tools/wind_shovel").setUnlocalizedName("essentialcraft.wind_elemental_shovel").setMaxStackSize(1).setFull3D();
		registerItemSimple(wind_elemental_shovel,"wind_elemental_shovel");
		wind_elemental_axe = new ItemWindAxe(windElemental).setTextureName("essentialcraft:tools/wind_axe").setUnlocalizedName("essentialcraft.wind_elemental_axe").setMaxStackSize(1).setFull3D();
		registerItemSimple(wind_elemental_axe,"wind_elemental_axe");
		wind_elemental_hoe = new ItemWindHoe(windElemental).setTextureName("essentialcraft:tools/wind_hoe").setUnlocalizedName("essentialcraft.wind_elemental_hoe").setMaxStackSize(1).setFull3D();
		registerItemSimple(wind_elemental_hoe,"wind_elemental_hoe");
		wind_elemental_sword = new ItemWindSword(windElemental).setTextureName("essentialcraft:tools/wind_sword").setUnlocalizedName("essentialcraft.wind_elemental_sword").setMaxStackSize(1).setFull3D();
		registerItemSimple(wind_elemental_sword,"wind_elemental_sword");
		
		wind_helmet = new ItemGenericArmor(windArmorMaterial, 0, 0).setTextureName("essentialcraft:armor/wind_helmet").setUnlocalizedName("essentialcraft.wind_helmet").setMaxDamage(624);
		registerItemSimple(wind_helmet,"wind_helmet");
		ApiCore.allowItemToSeeMRU(wind_helmet);
		ApiCore.setArmorProperties(wind_helmet, 0.5F, 0.75F, 0.5F);
		
		wind_chestplate = new ItemGenericArmor(windArmorMaterial, 0, 1).setTextureName("essentialcraft:armor/wind_chestplate").setUnlocalizedName("essentialcraft.wind_chestplate").setMaxDamage(1248);
		registerItemSimple(wind_chestplate,"wind_chestplate");
		ApiCore.setArmorProperties(wind_chestplate, 0.5F, 0.75F, 0.5F);

		wind_leggings = new ItemGenericArmor(windArmorMaterial, 0, 2).setTextureName("essentialcraft:armor/wind_leggings").setUnlocalizedName("essentialcraft.wind_leggings").setMaxDamage(936);
		registerItemSimple(wind_leggings,"wind_leggings");
		ApiCore.setArmorProperties(wind_leggings, 0.5F, 0.75F, 0.5F);
		
		wind_boots = new ItemGenericArmor(windArmorMaterial, 0, 3).setTextureName("essentialcraft:armor/wind_boots").setUnlocalizedName("essentialcraft.wind_boots").setMaxDamage(624);
		registerItemSimple(wind_boots,"wind_boots");
		ApiCore.setArmorProperties(wind_boots, 0.5F, 0.75F, 0.5F);
		
		soul = registerItemSimple(soul,ItemCapturedSoul.class,"soul","creatureSoul",0,false,64);
		soulScriber = registerItemSimple(soulScriber,ItemSoulScriber.class,"soulScriber","tools/soulScriber",32,true,1);

		magicalBuilder = registerItemSimple(magicalBuilder,ItemMagicalBuilder.class,"magicalBuilder","spells/staff_void",0,true,1);
		
		computer_helmet = new ItemComputerArmor(computerArmorMaterial, 0, 0).setTextureName("essentialcraft:armor/computer_helmet").setUnlocalizedName("essentialcraft.computer_helmet").setMaxDamage(16384);
		registerItemSimple(computer_helmet,"computer_helmet");
		ApiCore.allowItemToSeeMRU(computer_helmet);
		ApiCore.setArmorProperties(computer_helmet, 1F, 1F, 1F);
		
		computer_chestplate = new ItemComputerArmor(computerArmorMaterial, 0, 1).setTextureName("essentialcraft:armor/computer_chestplate").setUnlocalizedName("essentialcraft.computer_chestplate").setMaxDamage(16384);
		registerItemSimple(computer_chestplate,"computer_chestplate");
		ApiCore.setArmorProperties(computer_chestplate, 1F, 1F, 1F);
		
		computer_leggings = new ItemComputerArmor(computerArmorMaterial, 0, 2).setTextureName("essentialcraft:armor/computer_leggings").setUnlocalizedName("essentialcraft.computer_leggings").setMaxDamage(16384);
		registerItemSimple(computer_leggings,"computer_leggings");
		ApiCore.setArmorProperties(computer_leggings, 1F, 1F, 1F);
		
		computer_boots = new ItemComputerArmor(computerArmorMaterial, 0, 3).setTextureName("essentialcraft:armor/computer_boots").setUnlocalizedName("essentialcraft.computer_boots").setMaxDamage(16384);
		registerItemSimple(computer_boots,"computer_boots");
		ApiCore.setArmorProperties(computer_boots, 1F, 1F, 1F);
		
		computerBoard = registerItemSimple(computerBoard,ItemComputerBoard.class,"computerBoard","antigravity_board",0,true,1);
		
		orbitalRemote = registerItemSimple(orbitalRemote,ItemOrbitalRemote.class,"orbitalRemote","tools/orbitalStrikeRemote",0,true,1);
		
		record_robocalypse = new ItemRecord_Mod("hologram", SoundRegistry.recordRopocalypse).setTextureName("essentialcraft:record_holobattle").setUnlocalizedName("essentialcraft.hologramRec");
		
		dividingGun = registerItemSimple(dividingGun,ItemDividerGun.class,"dividerGun","tools/dividingGun",0,true,1);
		
		registerItemSimple(record_robocalypse, "robocalypse");
		
		inventoryGem = registerItemSimple(inventoryGem,ItemInventoryGem.class,"inventoryGem","null",0,false,64);
		craftingFrame = registerItemSimple(craftingFrame,ItemCraftingFrame.class,"craftingFrame","itemCrafter",0,false,1);
		collectedSpawner = registerItemSimple(collectedSpawner,ItemCollectedMonsterSpawner.class,"collectedSpawner","collectedSpawner",0,false,64);
		
		MagicianTableUpgrades.addUpgrade(new ItemStack(genericItem,1,1), 1.2F, loc("elementalCore"));
		MagicianTableUpgrades.addUpgrade(new ItemStack(genericItem,1,0), 5F, loc("combinedMagicalAlloys"));
		MagicianTableUpgrades.addUpgrade(new ItemStack(genericItem,1,8), 2.5F, loc("enderAlloy"));
		MagicianTableUpgrades.addUpgrade(new ItemStack(genericItem,1,23), 3F, loc("eyeOfAbsorbtion"));
		MagicianTableUpgrades.addUpgrade(new ItemStack(genericItem,1,35), 6F, loc("voidPlating"));
		MagicianTableUpgrades.addUpgrade(new ItemStack(genericItem,1,36), 7.5F, loc("voidCore"));
		MagicianTableUpgrades.addUpgrade(new ItemStack(genericItem,1,37), 10F, loc("voidReactor"));
		MagicianTableUpgrades.addUpgrade(new ItemStack(genericItem,1,44),4F, loc("crystal"));
		MagicianTableUpgrades.addUpgrade(new ItemStack(genericItem,1,53),16F, loc("demonicCore"));
	}
	
	public static ResourceLocation loc(String name)
	{
		return new ResourceLocation("essentialcraft","textures/models/"+name+".png");
	}

	public static Item registerItemSimple(Item i, Class<?extends Item> itemClass, String name, String textureName, int damage, boolean full3D, int stackSize)
	{
		try
		{
			if(textureName.indexOf(":") != -1)
				i = itemClass.newInstance().setUnlocalizedName("essentialcraft."+name).setMaxDamage(damage).setMaxStackSize(stackSize);
			else
				i = itemClass.newInstance().setUnlocalizedName("essentialcraft."+name).setMaxDamage(damage).setMaxStackSize(stackSize);
			if(full3D)
				i.setFull3D();
			ItemRegistry.registerItem(i, name, EssentialCraftCore.class);
			EssentialCraftCore.proxy.handleItemRegister(i);
			return i;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Item registerItemSimple(Item i, String name) {
		ItemRegistry.registerItem(i, name, EssentialCraftCore.class);
		EssentialCraftCore.proxy.handleItemRegister(i);
		return i;
	}
	
	public static Item testingItem;
	public static Item mruMover1;
	public static Item fancyHat;
	public static Item drops;
	public static Item elemental_pick;
	public static Item elemental_axe;
	public static Item elemental_hoe;
	public static Item elemental_shovel;
	public static Item elemental_sword;
	public static Item bound_gem;
	public static Item magicMonocle;
	public static Item record_everlastingSummer;
	public static Item record_papersPlease;
	public static Item soulStone;
	public static Item matrixProj;
	public static Item titanite;
	public static Item twinkling_titanite;
	public static Item secret;
	public static Item magicalSlag;
	public static Item genericItem;
	public static Item magicalAlloy;
	public static Item record_secret;
	public static Item essence;
	public static Item storage;
	
	public static Item magicalDigger;
	public static Item spawnerCollector;
	public static Item staffOfLife;
	public static Item biomeWand;
	
	public static Item emeraldHeart;
	public static Item magicalShield;
	public static Item spikyShield;
	public static Item magicWaterBottle;
	public static Item magicalPorkchop;
	public static Item charm;
	public static Item magicalWings;
	public static Item magmaticStaff;
	public static Item magicalLantern;
	public static Item magnetizingStaff;
	
	public static Item chaosFork;
	public static Item frozenMace;
	public static Item holyMace;
	
	public static Item elementalFuel;
	
	public static Item fFocus;
	public static Item wFocus;
	public static Item eFocus;
	public static Item aFocus;
	
	public static Item fruit;
	
	public static Item bottledWind;
	public static Item windKeeper;
	public static Item imprisonedWind;
	
	public static Item windTablet;
	
	public static Item elementalSword;
	public static Item ember;
	
	public static Item[] magicArmorItems = new Item[16];
	
	public static Item bauble;
	
	public static Item research_book;
	
	public static Item air_potion;
	
	public static Item mruMover_t2;
	
	public static Item playerList;
	
	public static Item magicalChisel;
	
	public static Item filter;
	public static Item controlRod;
	public static Item baublesCore;
	
	public static Item shadeSword;
	public static Item shadeSlasher;
	public static Item shadeKnife;
	public static Item entityEgg;
	
	public static Item pistol;
	public static Item rifle;
	public static Item sniper;
	public static Item gatling;
	
	public static Item weak_elemental_pick;
	public static Item weak_elemental_axe;
	public static Item weak_elemental_hoe;
	public static Item weak_elemental_shovel;
	public static Item weak_elemental_sword;
	
	public static Item wind_elemental_pick;
	public static Item wind_elemental_axe;
	public static Item wind_elemental_hoe;
	public static Item wind_elemental_shovel;
	public static Item wind_elemental_sword;
	
	public static Item wind_helmet;
	public static Item wind_chestplate;
	public static Item wind_leggings;
	public static Item wind_boots;
	
	public static Item computer_helmet;
	public static Item computer_chestplate;
	public static Item computer_leggings;
	public static Item computer_boots;
	
	public static Item soul;
	public static Item soulScriber;
	
	public static Item magicalBuilder;
	
	public static Item computerBoard;
	
	public static Item orbitalRemote;
	
	public static Item record_robocalypse;
	
	public static Item dividingGun;
	
	public static Item inventoryGem;
	public static Item craftingFrame;
	public static Item collectedSpawner;
	
	public static ArmorMaterial magicArmorMaterial = EnumHelper.addArmorMaterial("MRUFortified", "armorEC3Magic", 33, new int[]{3, 8, 6, 3}, 25, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 0);
	public static ArmorMaterial voidArmorMaterial = EnumHelper.addArmorMaterial("VoidFortified", "armorEC3Void", 52, new int[]{3, 8, 6, 3}, 40, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 0);
	public static ArmorMaterial inquisArmorMaterial = EnumHelper.addArmorMaterial("Inquisitorium", "armorEC3Inquisitorium", 64, new int[]{10, 10, 10, 10}, 60, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 0);
	public static ArmorMaterial windArmorMaterial = EnumHelper.addArmorMaterial("WindElemental", "armorEC3Wind", 42, new int[]{4, 10, 7, 4}, 40, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 0);
	public static ArmorMaterial computerArmorMaterial = EnumHelper.addArmorMaterial("Computeric", "armorEC3Computer", 256, new int[]{10, 30, 20, 10}, 127, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 0);
	
	public static ToolMaterial elemental;
	public static ToolMaterial weakElemental;
	public static ToolMaterial windElemental;
	public static ToolMaterial shade;
}
