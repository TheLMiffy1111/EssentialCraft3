package essentialcraft.common.registry;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootEntryTable;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class LootTableRegistry {

	public static final ResourceLocation CHEST_CATACOMBS = LootTableList.register(new ResourceLocation("essentialcraft:chests/catacombs"));
	public static final ResourceLocation CHEST_TOWN_TOWER = LootTableList.register(new ResourceLocation("essentialcraft:chests/town_tower"));
	public static final ResourceLocation CHEST_HOLOGRAM = LootTableList.register(new ResourceLocation("essentialcraft:chests/hologram"));
	public static final ResourceLocation ENTITY_WINDMAGE_APPRENTICE = LootTableList.register(new ResourceLocation("essentialcraft:entities/windmage_apprentice"));
	public static final ResourceLocation ENTITY_WINDMAGE_NORMAL = LootTableList.register(new ResourceLocation("essentialcraft:entities/windmage_normal"));
	public static final ResourceLocation ENTITY_WINDMAGE_ARCHMAGE = LootTableList.register(new ResourceLocation("essentialcraft:entities/windmage_archmage"));
	public static final ResourceLocation ENTITY_HOLOGRAM_ADDITION = LootTableList.register(new ResourceLocation("essentialcraft:entities/hologram_addition"));
	public static final ResourceLocation ENTITY_HOLOGRAM_DIVISION = LootTableList.register(new ResourceLocation("essentialcraft:entities/hologram_division"));
	public static final ResourceLocation ENTITY_HOLOGRAM_MULTIPLICATION = LootTableList.register(new ResourceLocation("essentialcraft:entities/hologram_multiplication"));
	public static final ResourceLocation ENTITY_HOLOGRAM_SUBTRACTION = LootTableList.register(new ResourceLocation("essentialcraft:entities/hologram_subtraction"));
	public static final ResourceLocation INJECT_CHEST_SIMPLE_DUNGEON = LootTableList.register(new ResourceLocation("essentialcraft:inject/chests/simple_dungeon"));

	public static void init() {
		MinecraftForge.EVENT_BUS.register(new LootTableRegistry());
	}

	@SubscribeEvent
	public void lootTableEvent(LootTableLoadEvent event) {
		if(event.getName().equals(LootTableList.CHESTS_SIMPLE_DUNGEON)) {
			event.getTable().addPool(getInjectPool(INJECT_CHEST_SIMPLE_DUNGEON));
		}
	}

	public static LootPool getInjectPool(ResourceLocation entryName) {
		return new LootPool(new LootEntry[] {getInjectEntry(entryName, 1)}, new LootCondition[0], new RandomValueRange(1), new RandomValueRange(0), "essentialcraft_inject");
	}

	public static LootEntryTable getInjectEntry(ResourceLocation name, int weight) {
		return new LootEntryTable(name, weight, 0, new LootCondition[0], "essentialcraft_inject");
	}
}
