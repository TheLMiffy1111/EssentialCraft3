package essentialcraft.common.tile;

import java.util.List;

import DummyCore.Utils.MathUtils;
import essentialcraft.api.ApiCore;
import essentialcraft.common.item.ItemsCore;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemRecord;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.util.text.event.HoverEvent.Action;
import net.minecraftforge.common.config.Configuration;

public class TileMagicalJukebox extends TileMRUGeneric {

	public static int cfgMaxMRU = ApiCore.DEVICE_MAX_MRU_GENERIC;

	public int recordCooldownTime, recordPlayed;
	String[] achievementNames = {"Top Secret!", "Secret Advancement 2017!", "Never Give Up On Your Secrets!", "Go, Go Secret Rangers!", "Such Secrets!", "The Secret Is A Lie!", "Secret Bro!", "Secrets, Secrets Everywhere!", "Secrets, Secrets, Secrets!", "Never Give Up!", "Unachievable!", "Too Much Secrets!", "Very Secret!", "Top Secret Of 2164!", "The Secret Paradize!"};
	String[] monsterNames = {"jeb_", "Bob", "Jordan", "Monstro", "Isaac's Fork", "Bucker", "Arthem", "Cube", "2XStuffed", "Rainbow!", "StringFormatException", "java", "Secret!!!", "Ronny", "Clementine", "Jack", "Roland", "The Tower", "Judjment", "Jorji", "EZIC", "Roshan", "Ursa", "Enderbro", "Zombie", "poodiepie", "Cartman", "Markiplier", "The Game Theorist!", "Direwolf20", "Pahimar", "Eloraam", "CoTH", "[CoTH]", "ForgeDevName", "Player000", "Herobrine", "[DATA REMOVED]", "SCP-126", "No More!", "The longest list!", "Much Text", "Thanks!", "Time", "Moon", "Sun", "Earth", "Mars", "Theory", "Bang!", "Poo", "Lol 69", "146%", "2000", "1337", "OVER 9000!", "++i + ++i", "Brain", "<--->>---<<-", "Hello World!", "Doge", "The Wurm", "Spice!", "Happy", ":3", "^_^"};

	public TileMagicalJukebox() {
		super(ApiCore.DEVICE_MAX_MRU_GENERIC);
		setSlotsNum(2);
	}

	@Override
	public void update() {
		super.update();
		mruStorage.update(getPos(), getWorld(), getStackInSlot(0));

		if(!getStackInSlot(1).isEmpty() && getStackInSlot(1).getItem() == ItemsCore.record_secret && recordCooldownTime <= 0) {
			recordPlayed = 1;
			recordCooldownTime = 257*20;
			if(!getWorld().isRemote) {
				getWorld().playEvent(null, 1010, pos, Item.getIdFromItem(getStackInSlot(1).getItem()));
			}
			return;
		}

		if(getStackInSlot(1).isEmpty() && recordCooldownTime > 100) {
			recordCooldownTime = 0;
			recordPlayed = 0;
			stopRecord();
			return;
		}

		if(!getStackInSlot(1).isEmpty() && getStackInSlot(1).getItem() == ItemsCore.record_secret && recordCooldownTime > 0) {
			--recordCooldownTime;
			if(world.isRemote) {
				getWorld().spawnParticle(EnumParticleTypes.NOTE, pos.getX() + 0.5D+MathUtils.randomDouble(getWorld().rand)*3, pos.getY() + 1.2D, pos.getZ() + 0.5D+MathUtils.randomDouble(getWorld().rand)*3, MathUtils.randomDouble(getWorld().rand)*24 / 24.0D, 0.0D, 0.0D);

				double randomX = MathUtils.randomDouble(getWorld().rand)*12;
				double randomY = MathUtils.randomDouble(getWorld().rand)*12;
				double randomZ = MathUtils.randomDouble(getWorld().rand)*12;
				double randomColorR = getWorld().rand.nextDouble();
				double randomColorG = getWorld().rand.nextDouble();
				double randomColorB = getWorld().rand.nextDouble();
				boolean randomBool = getWorld().rand.nextBoolean();
				for(int i = 0; i < 50; ++i)
					getWorld().spawnParticle(EnumParticleTypes.SPELL_MOB, pos.getX()+randomX, pos.getY()+randomY, pos.getZ()+randomZ, randomColorR, randomColorG, randomColorB);
				for(int i = 0; i < 100; ++i) {
					if(randomBool)
						getWorld().spawnParticle(EnumParticleTypes.CRIT, pos.getX()+randomX, pos.getY()+randomY, pos.getZ()+randomZ, MathUtils.randomDouble(getWorld().rand), 1.1D+MathUtils.randomDouble(getWorld().rand), MathUtils.randomDouble(getWorld().rand));
					else
						getWorld().spawnParticle(EnumParticleTypes.CRIT_MAGIC, pos.getX()+randomX, pos.getY()+randomY, pos.getZ()+randomZ, MathUtils.randomDouble(getWorld().rand), 1.1D+MathUtils.randomDouble(getWorld().rand), MathUtils.randomDouble(getWorld().rand));
				}
			}
			else if(!getWorld().isRemote) {
				int achievementID = getWorld().rand.nextInt(achievementNames.length);
				Style greenItalicAchievement = new Style().setColor(TextFormatting.GREEN);
				TextComponentString achName = new TextComponentString(achievementNames[achievementID]+"\n");
				achName.setStyle(greenItalicAchievement);
				achName.appendSibling(new TextComponentString("Achievable!").setStyle(new Style().setColor(TextFormatting.WHITE)));
				HoverEvent achievementEvent = new HoverEvent(Action.SHOW_TEXT, achName);
				Style achievementStyle = new Style().setColor(TextFormatting.GREEN);
				achievementStyle.setHoverEvent(achievementEvent);

				TextComponentString achievementText = new TextComponentString("["+achievementNames[achievementID]+"]");
				achievementText.setStyle(achievementStyle);
				List<EntityPlayer> playerLst = getWorld().getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(pos.getX()-6, pos.getY()-6, pos.getZ()-6, pos.getX()+7, pos.getY()+7, pos.getZ()+7));
				if(!playerLst.isEmpty() && getWorld().getWorldTime() % 60 == 0) {
					EntityPlayer player = playerLst.get(getWorld().rand.nextInt(playerLst.size()));
					player.sendMessage(new TextComponentTranslation("chat.type.advancement.task", player.getDisplayName(), achievementText));
				}
				List<EntitySheep> sheepLst = getWorld().getEntitiesWithinAABB(EntitySheep.class, new AxisAlignedBB(pos.getX()-16, pos.getY()-16, pos.getZ()-16, pos.getX()+17, pos.getY()+17, pos.getZ()+17));
				if(!sheepLst.isEmpty() && getWorld().getWorldTime() % 20 == 0) {
					for(int t = 0; t < sheepLst.size(); ++t) {
						EntitySheep sheep = sheepLst.get(t);
						sheep.setFleeceColor(EnumDyeColor.byMetadata(getWorld().rand.nextInt(16)));
						sheep.setCustomNameTag(monsterNames[getWorld().rand.nextInt(monsterNames.length)]);
					}
				}
				List<EntityLiving> baseLst = getWorld().getEntitiesWithinAABB(EntityLiving.class, new AxisAlignedBB(pos.getX()-16, pos.getY()-16, pos.getZ()-16, pos.getX()+17, pos.getY()+17, pos.getZ()+17));
				if(!baseLst.isEmpty() && getWorld().getWorldTime() % 10 == 0) {
					for(int t = 0; t < baseLst.size(); ++t) {
						EntityLiving sheep = baseLst.get(t);
						sheep.setCustomNameTag(monsterNames[getWorld().rand.nextInt(monsterNames.length)]);
					}
				}

				if(getWorld().getWorldTime()%20 == 0) {
					int randomBlockID = getWorld().rand.nextInt(15);

					IBlockState fallingBlock;

					switch(randomBlockID) {
					default:
					case 0:
					case 1:
					case 2:
					case 3:
					case 4:
						fallingBlock = Blocks.YELLOW_FLOWER.getDefaultState();
						break;
					case 5:
					case 6:
					case 7:
					case 8:
					case 9:
						fallingBlock = Blocks.RED_FLOWER.getStateFromMeta(randomBlockID-1);
						break;
					case 10:
						fallingBlock = Blocks.RED_MUSHROOM.getDefaultState();
						break;
					case 11:
						fallingBlock = Blocks.BROWN_MUSHROOM.getDefaultState();
						break;
					case 12:
					case 13:
						fallingBlock = Blocks.TALLGRASS.getStateFromMeta(randomBlockID-11);
						break;
					case 14:
						fallingBlock = Blocks.WATERLILY.getDefaultState();
						break;
					}
					EntityFallingBlock flower = new EntityFallingBlock(getWorld(), pos.getX()+(int)(MathUtils.randomDouble(getWorld().rand)*16)+0.5D, 255, pos.getZ()+(int)(MathUtils.randomDouble(getWorld().rand)*16)+0.5D, fallingBlock);
					flower.fallTime = 3;
					getWorld().spawnEntity(flower);
				}
			}
			return;
		}

		if(!getStackInSlot(1).isEmpty() && getStackInSlot(1).getItem() instanceof ItemRecord && recordPlayed == 0 && recordCooldownTime == 0 && mruStorage.getMRU() >= 500) {
			mruStorage.extractMRU(500, true);
			recordPlayed = 1;
			recordCooldownTime = 100;
			if(!getWorld().isRemote) {
				getWorld().playEvent(null, 1010, pos, Item.getIdFromItem(getStackInSlot(1).getItem()));
			}
		}

		if(getWorld().isRemote && !getStackInSlot(1).isEmpty() && recordPlayed == 1) {
			if(getWorld().rand.nextFloat() <= 0.33F) {
				getWorld().spawnParticle(EnumParticleTypes.NOTE, pos.getX() + 0.5D, pos.getY() + 1.2D, pos.getZ() + 0.5D+MathUtils.randomDouble(getWorld().rand)/2, MathUtils.randomDouble(getWorld().rand)*24 / 24.0D, 0.0D, 0.0D);
			}
		}

		if(recordCooldownTime > 0) {
			--recordCooldownTime;
		}

		if(!getStackInSlot(1).isEmpty() && getStackInSlot(1).getItem() instanceof ItemRecord && recordPlayed == 1 && recordCooldownTime == 0 && getWorld().isBlockIndirectlyGettingPowered(pos) > 0 && mruStorage.getMRU() >= 500) {
			stopRecord();
			mruStorage.extractMRU(500, true);
			recordCooldownTime = 100;
			if(!getWorld().isRemote) {
				getWorld().playEvent(null, 1010, pos, Item.getIdFromItem(getStackInSlot(1).getItem()));
			}
		}
		if(getStackInSlot(1).isEmpty() || !(getStackInSlot(1).getItem() instanceof ItemRecord) && recordPlayed == 1) {
			recordPlayed = 0;
			stopRecord();
		}
	}

	public void stopRecord() {
		getWorld().playEvent(1010, pos, 0);
		getWorld().playRecord(pos, null);
	}

	@Override
	public int[] getOutputSlots() {
		return new int[] {1};
	}

	public static void setupConfig(Configuration cfg) {
		try {
			String category = "tileentities.magicaljukebox";
			cfgMaxMRU = cfg.get(category, "MaxMRU", ApiCore.DEVICE_MAX_MRU_GENERIC).setMinValue(1).getInt();
		}
		catch(Exception e) {
			return;
		}
	}
}
