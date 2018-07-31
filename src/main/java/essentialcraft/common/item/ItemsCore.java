package essentialcraft.common.item;

import DummyCore.Registries.ItemRegistry;
import essentialcraft.api.ApiCore;
import essentialcraft.api.MagicianTableUpgrades;
import essentialcraft.common.mod.EssentialCraftCore;
import essentialcraft.common.registry.SoundRegistry;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.util.EnumHelper;

public class ItemsCore {

	public static void loadItems() {
		elemental = EnumHelper.addToolMaterial("elemental", 6, 3568, 15.0F, 5.0F, 36);
		weakElemental = EnumHelper.addToolMaterial("weakElemental", 3, 754, 7.0F, 2.5F, 36);
		windElemental = EnumHelper.addToolMaterial("windElemental", 11, 15684, 15.0F, 6F, 42);
		shade = EnumHelper.addToolMaterial("shade", 32, 754, 1.0F, 17.0F, 12);

		testingItem = new ItemTestEC().setUnlocalizedName("essentialcraft.testItem").setMaxStackSize(1).setFull3D();
		registerItemSimple(testingItem, "testItem");
		mruMover1 = new ItemMRUMover().setUnlocalizedName("essentialcraft.mruMover1").setMaxStackSize(1).setFull3D();
		registerItemSimple(mruMover1, "mruMover1");
		drops = new ItemDrop().setUnlocalizedName("essentialcraft.drops_").setMaxStackSize(64);
		registerItemSimple(drops,  "gem_elemental");
		elemental_pick = new ItemPickaxeEC(elemental).setUnlocalizedName("essentialcraft.elemental_pick").setMaxStackSize(1).setFull3D();
		registerItemSimple(elemental_pick, "elemental_pick");
		elemental_axe = new ItemAxeEC(elemental).setUnlocalizedName("essentialcraft.elemental_axe").setMaxStackSize(1).setFull3D();
		registerItemSimple(elemental_axe, "elemental_axe");
		elemental_hoe = new ItemHoeEC(elemental).setUnlocalizedName("essentialcraft.elemental_hoe").setMaxStackSize(1).setFull3D();
		registerItemSimple(elemental_hoe, "elemental_hoe");
		elemental_shovel = new ItemShovelEC(elemental).setUnlocalizedName("essentialcraft.elemental_shovel").setMaxStackSize(1).setFull3D();
		registerItemSimple(elemental_shovel, "elemental_shovel");
		elemental_sword = new ItemSwordEC(elemental).setUnlocalizedName("essentialcraft.elemental_sword").setMaxStackSize(1).setFull3D();
		registerItemSimple(elemental_sword, "elemental_sword");
		bound_gem = new ItemBoundGem().setUnlocalizedName("essentialcraft.bound_gem");
		registerItemSimple(bound_gem, "bound_gem");
		magicMonocle = registerItemSimple(new ItemMonocle(), "magicMonocle", 16, true, 1);

		record_everlastingSummer = new ItemRecordEC("letsbefriends", SoundRegistry.recordLetsBeFriends).setUnlocalizedName("essentialcraft.record");
		registerItemSimple(record_everlastingSummer, "record");

		record_papersPlease = new ItemRecordEC("arstotzkan", SoundRegistry.recordArstotzkan).setUnlocalizedName("essentialcraft.record_a");
		registerItemSimple(record_papersPlease, "record_a");

		record_secret = new ItemRecordEC("ecsecret", SoundRegistry.recordSecret).setUnlocalizedName("essentialcraft.record_secret");
		registerItemSimple(record_secret, "record_secret");

		soulStone = registerItemSimple(new ItemSoulStone(), "soulStone", 0, false, 1);
		matrixProj = registerItemSimple(new ItemMRUMatrixProjection(), "mruMatrixProjection", 0, false, 1);
		titanite = registerItemSimple(new ItemEC(), "titanite", 0, false, 64);
		twinkling_titanite = registerItemSimple(new ItemEC(), "twinkling_titanite", 0, false, 64);
		secret = registerItemSimple(new ItemSecret(), "secret", 0, false, 1);
		magicalSlag = registerItemSimple(new ItemEC(), "magicalSlag", 0, false, 64);
		genericItem = registerItemSimple(new ItemGenericEC(), "genItem", 0, false, 64);
		magicalAlloy = registerItemSimple(new ItemMagicalAlloy(), "magicalAlloy", 0, false, 64);
		essence = registerItemSimple(new ItemEssence(), "essence", 0, false, 16);

		storage = new ItemMRUStorageEC(new int[]{500, 5000, 30000, 120000, 1000000}).setUnlocalizedName("essentialcraft.storage");
		registerItemSimple(storage, "storage");

		magicalDigger = registerItemSimple(new ItemMagicalDigger(), "magicalDigger", 0, true, 1);
		spawnerCollector = registerItemSimple(new ItemSpawnerCollector(), "spawnerCollector", 0, true, 1);
		staffOfLife = registerItemSimple(new ItemLifeStaff(), "staffOfLife", 0, true, 1);
		biomeWand = registerItemSimple(new ItemBiomeWand(), "biomeWand", 0, true, 1);

		emeraldHeart = registerItemSimple(new ItemEmeraldHeart(), "emeraldHeart", 0, true, 1);
		magicalShield = registerItemSimple(new ItemMagicalShield(), "magicalShield", 0, true, 1);
		spikyShield = registerItemSimple(new ItemSpikyShield(), "spikyShield", 0, true, 1);
		magicWaterBottle = registerItemSimple(new ItemMagicalWater(), "magicWaterBottle", 0, false, 1);
		magicalPorkchop = registerItemSimple(new ItemMagicalPorkchop(), "magicalPorkchop", 0, false, 1);
		charm = registerItemSimple(new ItemCharm(), "charm", 0, false, 1);
		magicalWings = registerItemSimple(new ItemMagicalWings(), "magicalWings", 0, false, 1);

		chaosFork = registerItemSimple(new ItemChaosFork(), "chaosFork", 0, true, 1);
		frozenMace = registerItemSimple(new ItemFrostMace(), "frozenMace", 0, true, 1);
		holyMace = registerItemSimple(new ItemHolyMace(), "holyMace", 0, true, 1);

		elementalFuel = registerItemSimple(new ItemEssentialFuel(), "elementalFuel", 0, false, 16);

		fFocus = new ItemElementalFocus().setUnlocalizedName("essentialcraft.fFocus");
		registerItemSimple(fFocus, "fFocus");
		wFocus = new ItemElementalFocus().setUnlocalizedName("essentialcraft.wFocus");
		registerItemSimple(wFocus, "wFocus");
		eFocus = new ItemElementalFocus().setUnlocalizedName("essentialcraft.eFocus");
		registerItemSimple(eFocus, "eFocus");
		aFocus = new ItemElementalFocus().setUnlocalizedName("essentialcraft.aFocus");
		registerItemSimple(aFocus, "aFocus");

		fruit = new ItemFruit(8, 10F, false).setUnlocalizedName("essentialcraft.fruit_Item");
		registerItemSimple(fruit, "fruit_Item");

		bottledWind = new ItemWindKeeper(10).setUnlocalizedName("essentialcraft.bottledWind");
		registerItemSimple(bottledWind, "bottledWind");

		imprisonedWind = new ItemWindKeeper(25).setUnlocalizedName("essentialcraft.imprisonedWind");
		registerItemSimple(imprisonedWind, "imprisonedWind");

		windKeeper = new ItemWindKeeper(100).setUnlocalizedName("essentialcraft.windKeeper");
		registerItemSimple(windKeeper, "windKeeper");

		windTablet = registerItemSimple(new ItemWindTablet(), "windTablet", 0, false, 1);

		elementalSword = registerItemSimple(new ItemElementalSword(), "elementalSword", 0, true, 1);
		ember = registerItemSimple(new ItemEmber(), "ember", 0, false, 16);



		magicArmorItems[0] = new ItemArmorEC(magicArmorMaterial, 4, 0, 0).setArmorTexture("armorEC3Magic").setUnlocalizedName("essentialcraft.helm_magic").setMaxDamage(512);
		registerItemSimple(magicArmorItems[0], "helm_magic");
		ApiCore.allowItemToSeeMRU(magicArmorItems[0]);
		ApiCore.setItemResistances(magicArmorItems[0], 0.25F, 0.125F, 0.10F);

		magicArmorItems[1] = new ItemArmorEC(magicArmorMaterial, 4, 1, 0).setArmorTexture("armorEC3Magic").setUnlocalizedName("essentialcraft.chest_magic").setMaxDamage(1536);
		registerItemSimple(magicArmorItems[1], "chest_magic");
		ApiCore.setItemResistances(magicArmorItems[1], 0.25F, 0.125F, 0.10F);

		magicArmorItems[2] = new ItemArmorEC(magicArmorMaterial, 4, 2, 0).setArmorTexture("armorEC3Magic").setUnlocalizedName("essentialcraft.legs_magic").setMaxDamage(1024);
		registerItemSimple(magicArmorItems[2], "legs_magic");
		ApiCore.setItemResistances(magicArmorItems[2], 0.25F, 0.125F, 0.10F);

		magicArmorItems[3] = new ItemArmorEC(magicArmorMaterial, 4, 3, 0).setArmorTexture("armorEC3Magic").setUnlocalizedName("essentialcraft.boots_magic").setMaxDamage(512);
		registerItemSimple(magicArmorItems[3], "boots_magic");
		ApiCore.setItemResistances(magicArmorItems[3], 0.25F, 0.125F, 0.10F);



		magicArmorItems[4] = new ItemArmorEC(voidArmorMaterial, 4, 0, 1).setArmorTexture("armorEC3Void").setDescription(TextFormatting.ITALIC+"Converts life energy of fallen foes to your hunger").setUnlocalizedName("essentialcraft.helm_void").setMaxDamage(2048);
		registerItemSimple(magicArmorItems[4], "helm_void");
		ApiCore.allowItemToSeeMRU(magicArmorItems[4]);
		ApiCore.setItemResistances(magicArmorItems[4], 1F, 0.5F, 0.75F);

		magicArmorItems[5] = new ItemArmorEC(voidArmorMaterial, 4, 1, 1).setArmorTexture("armorEC3Void").setDescription(TextFormatting.ITALIC+"Can dodge 20% of attacks").setUnlocalizedName("essentialcraft.chest_void").setMaxDamage(6144);
		registerItemSimple(magicArmorItems[5], "chest_void");
		ApiCore.setItemResistances(magicArmorItems[5], 1F, 0.5F, 0.75F);

		magicArmorItems[6] = new ItemArmorEC(voidArmorMaterial, 4, 2, 1).setArmorTexture("armorEC3Void").setDescription(TextFormatting.ITALIC+"Can phase-shift through monsters").setUnlocalizedName("essentialcraft.legs_void").setMaxDamage(4096);
		registerItemSimple(magicArmorItems[6], "legs_void");
		ApiCore.setItemResistances(magicArmorItems[6], 1F, 0.5F, 0.75F);

		magicArmorItems[7] = new ItemArmorEC(voidArmorMaterial, 4, 3, 1).setArmorTexture("armorEC3Void").setDescription(TextFormatting.ITALIC+"Negates 90% of falldamage").setUnlocalizedName("essentialcraft.boots_void").setMaxDamage(2048);
		registerItemSimple(magicArmorItems[7], "boots_void");
		ApiCore.setItemResistances(magicArmorItems[7], 1F, 0.5F, 0.75F);



		magicArmorItems[8] = new ItemArmorEC(inquisArmorMaterial, 4, 0, 2).setArmorTexture("armorEC3Inquisitorium").setDescription(TextFormatting.ITALIC+"Linses created from the moonlight metal to see unpurity").setUnlocalizedName("essentialcraft.helm_inq").setMaxDamage(5120);
		registerItemSimple(magicArmorItems[8], "helm_inq");
		ApiCore.allowItemToSeeMRU(magicArmorItems[8]);
		ApiCore.setItemResistances(magicArmorItems[8], 1F, 1F, 1F);

		magicArmorItems[9] = new ItemArmorEC(inquisArmorMaterial, 4, 1, 2).setArmorTexture("armorEC3Inquisitorium").setDescription(TextFormatting.ITALIC+"Be quiet, Dominique!").setUnlocalizedName("essentialcraft.chest_inq").setMaxDamage(15360);
		registerItemSimple(magicArmorItems[9], "chest_inq");
		ApiCore.setItemResistances(magicArmorItems[9], 1F, 1F, 1F);

		magicArmorItems[10] = new ItemArmorEC(inquisArmorMaterial, 4, 2, 2).setArmorTexture("armorEC3Inquisitorium").setDescription(TextFormatting.ITALIC+"Magic is a parasite. It must be wiped out!").setUnlocalizedName("essentialcraft.legs_inq").setMaxDamage(10240);
		registerItemSimple(magicArmorItems[10], "legs_inq");
		ApiCore.setItemResistances(magicArmorItems[10], 1F, 1F, 1F);

		magicArmorItems[11] = new ItemArmorEC(inquisArmorMaterial, 4, 3, 2).setArmorTexture("armorEC3Inquisitorium").setDescription(TextFormatting.ITALIC+"Magic shall not prevail!").setUnlocalizedName("essentialcraft.boots_inq").setMaxDamage(5120);
		registerItemSimple(magicArmorItems[11], "boots_inq");
		ApiCore.setItemResistances(magicArmorItems[11], 1F, 1F, 1F);



		magicArmorItems[12] = new ItemArmorEC(ArmorMaterial.LEATHER, 4, 0, 3).setArmorTexture("armorEC3Wind").setUnlocalizedName("essentialcraft.helm_wind").setMaxDamage(128);
		registerItemSimple(magicArmorItems[12], "helm_wind");
		ApiCore.allowItemToSeeMRU(magicArmorItems[12]);

		magicArmorItems[13] = new ItemArmorEC(ArmorMaterial.LEATHER, 4, 1, 3).setArmorTexture("armorEC3Wind").setUnlocalizedName("essentialcraft.chest_wind").setMaxDamage(384);
		registerItemSimple(magicArmorItems[13], "chest_wind");

		magicArmorItems[14] = new ItemArmorEC(ArmorMaterial.LEATHER, 4, 2, 3).setArmorTexture("armorEC3Wind").setUnlocalizedName("essentialcraft.legs_wind").setMaxDamage(256);
		registerItemSimple(magicArmorItems[14], "legs_wind");

		magicArmorItems[15] = new ItemArmorEC(ArmorMaterial.LEATHER, 4, 3, 3).setArmorTexture("armorEC3Wind").setUnlocalizedName("essentialcraft.boots_wind").setMaxDamage(128);
		registerItemSimple(magicArmorItems[15], "boots_wind");



		bauble = registerItemSimple(new ItemBaublesResistance(), "bauble", 0, false, 1);

		magmaticStaff = registerItemSimple(new ItemMagmaticWand(), "magmaticStaff", 0, true, 1);
		magicalLantern = registerItemSimple(new ItemMagicLantern(), "magicalLantern", 0, true, 1);
		magnetizingStaff = registerItemSimple(new ItemMagnetizingStaff(), "magnetizingStaff", 0, true, 1);

		research_book = registerItemSimple(new ItemKnowledgeBook(), "research_book", 0, false, 1);

		air_potion = registerItemSimple(new ItemLiquidAir(), "air_potion", 0, false, 64);
		mruMover_t2 = new ItemMRUMover().setUnlocalizedName("essentialcraft.mruMover_t2").setMaxDamage(256*6).setMaxStackSize(1).setFull3D();
		registerItemSimple(mruMover_t2, "mruMover_t2");
		playerList = registerItemSimple(new ItemPlayerList(), "playerList", 0, false, 1);
		magicalChisel = registerItemSimple(new ItemMagicalChisel(), "magicalChisel", 256, true, 1);
		filter = registerItemSimple(new ItemFilter(), "itemFilter", 0, false, 1);
		controlRod = registerItemSimple(new ItemControlRod(), "controlRod", 0, true, 1);
		baublesCore = registerItemSimple(new ItemBaublesSpecial(), "baublesCore", 0, false, 1);
		ApiCore.MRU_VISIBLE_LIST.add(mruMover_t2);

		shadeSword = registerItemSimple(new ItemShadeSword(), "shadeSword", 0, true, 1);
		shadeSlasher = registerItemSimple(new ItemShadeSlasher(), "shadeSlasher", 0, true, 1);
		shadeKnife = registerItemSimple(new ItemShadowKnife(), "shadeKnife", 0, true, 32);
		entityEgg = registerItemSimple(new ItemSpawnEggEC(), "entityEgg", 0, false, 64);

		pistol = new ItemGun("pistol").setUnlocalizedName("essentialcraft.gun.pistol");
		registerItemSimple(pistol, "gun.pistol");
		rifle = new ItemGun("rifle").setUnlocalizedName("essentialcraft.gun.rifle");
		registerItemSimple(rifle, "gun.rifle");
		sniper = new ItemGun("sniper").setUnlocalizedName("essentialcraft.gun.sniper");
		registerItemSimple(sniper, "gun.sniper");
		gatling = new ItemGun("gatling").setUnlocalizedName("essentialcraft.gun.gatling");
		registerItemSimple(gatling, "gun.gatling");

		weak_elemental_pick = new ItemPickaxeEC(weakElemental).setUnlocalizedName("essentialcraft.weak_elemental_pick").setMaxStackSize(1).setFull3D();
		registerItemSimple(weak_elemental_pick, "weak_elemental_pick");
		weak_elemental_axe = new ItemAxeEC(weakElemental).setUnlocalizedName("essentialcraft.weak_elemental_axe").setMaxStackSize(1).setFull3D();
		registerItemSimple(weak_elemental_axe, "weak_elemental_axe");
		weak_elemental_hoe = new ItemHoeEC(weakElemental).setUnlocalizedName("essentialcraft.weak_elemental_hoe").setMaxStackSize(1).setFull3D();
		registerItemSimple(weak_elemental_hoe, "weak_elemental_hoe");
		weak_elemental_shovel = new ItemShovelEC(weakElemental).setUnlocalizedName("essentialcraft.weak_elemental_shovel").setMaxStackSize(1).setFull3D();
		registerItemSimple(weak_elemental_shovel, "weak_elemental_shovel");
		weak_elemental_sword = new ItemSwordEC(weakElemental).setUnlocalizedName("essentialcraft.weak_elemental_sword").setMaxStackSize(1).setFull3D();
		registerItemSimple(weak_elemental_sword, "weak_elemental_sword");

		wind_elemental_pick = new ItemWindPickaxe(windElemental).setUnlocalizedName("essentialcraft.wind_elemental_pick").setMaxStackSize(1).setFull3D();
		registerItemSimple(wind_elemental_pick, "wind_elemental_pick");
		wind_elemental_shovel = new ItemWindShovel(windElemental).setUnlocalizedName("essentialcraft.wind_elemental_shovel").setMaxStackSize(1).setFull3D();
		registerItemSimple(wind_elemental_shovel, "wind_elemental_shovel");
		wind_elemental_axe = new ItemWindAxe(windElemental).setUnlocalizedName("essentialcraft.wind_elemental_axe").setMaxStackSize(1).setFull3D();
		registerItemSimple(wind_elemental_axe, "wind_elemental_axe");
		wind_elemental_hoe = new ItemWindHoe(windElemental).setUnlocalizedName("essentialcraft.wind_elemental_hoe").setMaxStackSize(1).setFull3D();
		registerItemSimple(wind_elemental_hoe, "wind_elemental_hoe");
		wind_elemental_sword = new ItemWindSword(windElemental).setUnlocalizedName("essentialcraft.wind_elemental_sword").setMaxStackSize(1).setFull3D();
		registerItemSimple(wind_elemental_sword, "wind_elemental_sword");

		wind_helmet = new ItemGenericArmor(windArmorMaterial, 0, 0).setUnlocalizedName("essentialcraft.wind_helmet").setMaxDamage(624);
		registerItemSimple(wind_helmet, "wind_helmet");
		ApiCore.allowItemToSeeMRU(wind_helmet);
		ApiCore.setItemResistances(wind_helmet, 0.5F, 0.75F, 0.5F);

		wind_chestplate = new ItemGenericArmor(windArmorMaterial, 0, 1).setUnlocalizedName("essentialcraft.wind_chestplate").setMaxDamage(1248);
		registerItemSimple(wind_chestplate, "wind_chestplate");
		ApiCore.setItemResistances(wind_chestplate, 0.5F, 0.75F, 0.5F);

		wind_leggings = new ItemGenericArmor(windArmorMaterial, 0, 2).setUnlocalizedName("essentialcraft.wind_leggings").setMaxDamage(936);
		registerItemSimple(wind_leggings, "wind_leggings");
		ApiCore.setItemResistances(wind_leggings, 0.5F, 0.75F, 0.5F);

		wind_boots = new ItemGenericArmor(windArmorMaterial, 0, 3).setUnlocalizedName("essentialcraft.wind_boots").setMaxDamage(624);
		registerItemSimple(wind_boots, "wind_boots");
		ApiCore.setItemResistances(wind_boots, 0.5F, 0.75F, 0.5F);

		soul = registerItemSimple(new ItemCapturedSoul(), "soul", 0, false, 64);
		soulScriber = registerItemSimple(new ItemSoulScriber(), "soulScriber", 32, true, 1);

		magicalBuilder = registerItemSimple(new ItemMagicalBuilder(), "magicalBuilder", 0, true, 1);

		computer_helmet = new ItemComputerArmor(computerArmorMaterial, 0, 0).setTextureName("essentialcraft:armor/computer_helmet").setUnlocalizedName("essentialcraft.computer_helmet").setMaxDamage(16384);
		registerItemSimple(computer_helmet, "computer_helmet");
		ApiCore.allowItemToSeeMRU(computer_helmet);
		ApiCore.setItemResistances(computer_helmet, 1F, 1F, 1F);

		computer_chestplate = new ItemComputerArmor(computerArmorMaterial, 0, 1).setTextureName("essentialcraft:armor/computer_chestplate").setUnlocalizedName("essentialcraft.computer_chestplate").setMaxDamage(16384);
		registerItemSimple(computer_chestplate, "computer_chestplate");
		ApiCore.setItemResistances(computer_chestplate, 1F, 1F, 1F);

		computer_leggings = new ItemComputerArmor(computerArmorMaterial, 0, 2).setTextureName("essentialcraft:armor/computer_leggings").setUnlocalizedName("essentialcraft.computer_leggings").setMaxDamage(16384);
		registerItemSimple(computer_leggings, "computer_leggings");
		ApiCore.setItemResistances(computer_leggings, 1F, 1F, 1F);

		computer_boots = new ItemComputerArmor(computerArmorMaterial, 0, 3).setTextureName("essentialcraft:armor/computer_boots").setUnlocalizedName("essentialcraft.computer_boots").setMaxDamage(16384);
		registerItemSimple(computer_boots, "computer_boots");
		ApiCore.setItemResistances(computer_boots, 1F, 1F, 1F);

		computerBoard = registerItemSimple(new ItemComputerBoard(), "computerBoard", 0, true, 1);

		orbitalRemote = registerItemSimple(new ItemOrbitalRemote(), "orbitalRemote", 0, true, 1);

		record_robocalypse = new ItemRecordEC("hologram", SoundRegistry.recordRopocalypse).setUnlocalizedName("essentialcraft.hologramRec");

		dividingGun = registerItemSimple(new ItemDividerGun(), "dividerGun", 0, true, 1);

		registerItemSimple(record_robocalypse, "robocalypse");

		inventoryGem = registerItemSimple(new ItemInventoryGem(), "inventoryGem", 0, false, 64);
		craftingFrame = registerItemSimple(new ItemCraftingFrame(), "craftingFrame", 0, false, 1);
		collectedSpawner = registerItemSimple(new ItemCollectedMonsterSpawner(), "collectedSpawner", 0, false, 64);

		balance_setter = registerItemSimple(new ItemBalanceSetter(), "balance_setter",0,true,1);

		clearing_catalyst = registerItemSimple(new ItemEC(), "clearing_catalyst", 0, false, 64);
		raining_catalyst = registerItemSimple(new ItemEC(), "raining_catalyst", 0, false, 64);
		thundering_catalyst = registerItemSimple(new ItemEC(), "thundering_catalyst", 0, false, 64);

		MagicianTableUpgrades.addUpgrade(new ItemStack(genericItem, 1, 1), 1.2D, loc("elementalcore"));
		MagicianTableUpgrades.addUpgrade(new ItemStack(genericItem, 1, 0), 5D, loc("combinedmagicalalloys"));
		MagicianTableUpgrades.addUpgrade(new ItemStack(genericItem, 1,8), 2.5D, loc("enderalloy"));
		MagicianTableUpgrades.addUpgrade(new ItemStack(genericItem, 1, 23), 3D, loc("eyeofabsorbtion"));
		MagicianTableUpgrades.addUpgrade(new ItemStack(genericItem, 1, 35), 6D, loc("voidplating"));
		MagicianTableUpgrades.addUpgrade(new ItemStack(genericItem, 1, 36), 7.5D, loc("voidcore"));
		MagicianTableUpgrades.addUpgrade(new ItemStack(genericItem, 1, 37), 10D, loc("voidreactor"));
		MagicianTableUpgrades.addUpgrade(new ItemStack(genericItem, 1, 44), 4D, loc("crystal"));
		MagicianTableUpgrades.addUpgrade(new ItemStack(genericItem, 1, 53), 16D, loc("demoniccore"));
	}

	public static ResourceLocation loc(String name) {
		return new ResourceLocation("essentialcraft", "textures/models/"+name+".png");
	}

	public static <T extends Item> T registerItemSimple(T item, String name, int damage, boolean full3D, int stackSize) {
		item.setUnlocalizedName("essentialcraft."+name).setMaxDamage(damage).setMaxStackSize(stackSize);
		if(full3D)
			item.setFull3D();
		ItemRegistry.registerItem(item, name, EssentialCraftCore.class);
		EssentialCraftCore.proxy.handleItemRegister(item);
		return item;
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

	public static Item balance_setter;

	public static Item clearing_catalyst;
	public static Item raining_catalyst;
	public static Item thundering_catalyst;

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
