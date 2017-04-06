package ec3.common.tile;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.util.text.event.HoverEvent.Action;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemRecord;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import DummyCore.Utils.MathUtils;
import ec3.api.ApiCore;
import ec3.common.item.ItemsCore;
import ec3.utils.common.ECUtils;

public class TileMagicalJukebox extends TileMRUGeneric {
	
	public int recordCooldownTime, recordPlayed;
	String[] achievementNames = new String[] {"Top Secret!", "Secret Achievement 2017!", "Never give up on your secrets!", "Go, Go secret rangers!", "Such Secrets!", "The Secret is a Lie!", "Secret Bro!", "Secrets, Secrets everywhere!", "Secrets,Secrets,Secrets!", "Never give up!", "Unachievable!", "Too much Secrets!", "Very Secret!", "Top Secret of 2164!", "The Secret Paradize!"};
	String[] monsterNames = new String[] {"jeb_", "Bob", "Jordan", "Monstro", "Isaac's Fork", "Bucker", "Arthem", "Cube", "2XStuffed", "Rainbow!", "StringFormatException", "java", "Secret!!!", "Ronny", "Clementine", "Jack", "Roland", "The Tower", "Judjment", "Jorji", "EZIC", "Roshan", "Ursa", "Enderbro", "Zombie", "poodiepie", "Cartman", "Markiplier", "The Game Theorist!", "Direwolf20", "Pahimar", "Eloraam", "CoTH", "[CoTH]", "ForgeDevName", "Player000", "Herobrine", "[DATA REMOVED]", "SCP-126", "No More!", "The longest list!", "Much Text", "Thanks!", "Time", "Moon", "Sun", "Earth", "Mars", "Theory", "Bang!", "Poo", "Lol 69", "146%", "2000", "1337", "OVER 9000!", "++i + ++i", "Brain", "<--->>---<<-", "Hello World!", "Doge", "The Wurm", "Spice!", "Happy", ":3", "^_^"};
	
	public TileMagicalJukebox() {
		super();
		maxMRU = (int)ApiCore.DEVICE_MAX_MRU_GENERIC;
		setSlotsNum(2);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void update() {
		
		super.update();
		ECUtils.manage(this, 0);
		
		if(getStackInSlot(1) != null && getStackInSlot(1).getItem() == ItemsCore.record_secret && recordCooldownTime <= 0) {
			recordPlayed = 1;
			recordCooldownTime = 257*20;
			if(!getWorld().isRemote)
				getWorld().playEvent((EntityPlayer)null, 1010, pos, Item.getIdFromItem(getStackInSlot(1).getItem()));
			return;
		}
		
		if(getStackInSlot(1) == null && recordCooldownTime > 100) {
			recordCooldownTime = 0;
			recordPlayed = 0;
			return;
		}
		
		if(getStackInSlot(1) != null && getStackInSlot(1).getItem() == ItemsCore.record_secret && recordCooldownTime > 0) {
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
			--recordCooldownTime;
			
			int achievementID = getWorld().rand.nextInt(achievementNames.length);
			Style greenItalicAchievement = new Style().setColor(TextFormatting.GREEN).setItalic(true);
			TextComponentString achName = new TextComponentString(achievementNames[achievementID]+"\n");
			achName.setStyle(greenItalicAchievement);
			TextComponentString achievement = new TextComponentString("Achievement");
			achievement.setStyle(new Style().setColor(TextFormatting.WHITE).setItalic(true));
			achName.appendSibling(achievement);
			achName.appendSibling(new TextComponentString("\nAchievable!"));
			HoverEvent achievementEvent = new HoverEvent(Action.SHOW_TEXT, achName);
			Style achievementStyle = new Style().setColor(TextFormatting.GREEN);
			achievementStyle.setHoverEvent(achievementEvent);
			
			TextComponentString achievementText = new TextComponentString("["+achievementNames[achievementID]+"]");
			achievementText.setStyle(achievementStyle);
			List<EntityPlayer> playerLst = getWorld().getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(pos.getX()-6, pos.getY()-6, pos.getZ()-6, pos.getX()+7, pos.getY()+7, pos.getZ()+7));
			if(!playerLst.isEmpty() && getWorld().getWorldTime() % 60 == 0 && !getWorld().isRemote) {
				EntityPlayer player = playerLst.get(getWorld().rand.nextInt(playerLst.size()));
				player.sendMessage(new TextComponentString(player.getDisplayName()+" has just earned the achievement ").appendSibling(achievementText));
			}
			List<EntitySheep> sheepLst = getWorld().getEntitiesWithinAABB(EntitySheep.class, new AxisAlignedBB(pos.getX()-16, pos.getY()-16, pos.getZ()-16, pos.getX()+17, pos.getY()+17, pos.getZ()+17));
			if(!sheepLst.isEmpty() && getWorld().getWorldTime() % 20 == 0 && !getWorld().isRemote) {
				for(int t = 0; t < sheepLst.size(); ++t) {
					EntitySheep sheep = sheepLst.get(t);
					sheep.setFleeceColor(EnumDyeColor.byMetadata(getWorld().rand.nextInt(16)));
					sheep.setCustomNameTag(monsterNames[getWorld().rand.nextInt(monsterNames.length)]);
				}
			}
			List<EntityLiving> baseLst = getWorld().getEntitiesWithinAABB(EntityLiving.class, new AxisAlignedBB(pos.getX()-16, pos.getY()-16, pos.getZ()-16, pos.getX()+17, pos.getY()+17, pos.getZ()+17));
			if(!baseLst.isEmpty() && getWorld().getWorldTime() % 10 == 0 && !getWorld().isRemote) {
				for(int t = 0; t < baseLst.size(); ++t) {
					EntityLiving sheep = baseLst.get(t);
					sheep.setCustomNameTag(monsterNames[getWorld().rand.nextInt(monsterNames.length)]);
				}
			}
			
			int randomBlockID = getWorld().rand.nextInt(6);
			
			IBlockState fallingBlock = Blocks.RED_FLOWER.getDefaultState();
			
			switch(randomBlockID) {
			case 0: {
				fallingBlock = Blocks.RED_FLOWER.getDefaultState();
				break;
			}
			case 1: {
				fallingBlock = Blocks.YELLOW_FLOWER.getDefaultState();
				break;
			}
			case 2: {
				fallingBlock = Blocks.RED_MUSHROOM.getDefaultState();
				break;
			}
			case 3: {
				fallingBlock = Blocks.BROWN_MUSHROOM.getDefaultState();
				break;
			}
			case 4: {
				fallingBlock = Blocks.TALLGRASS.getDefaultState();
				break;
			}
			case 5: {
				fallingBlock = Blocks.WATERLILY.getDefaultState();
				break;
			}
			}
			
			if(getWorld().getWorldTime()%20 == 0) {
				EntityFallingBlock flower = new EntityFallingBlock(getWorld(), pos.getX()+MathUtils.randomDouble(getWorld().rand)*16, 255, pos.getZ()+MathUtils.randomDouble(getWorld().rand)*16, fallingBlock);
				flower.fallTime = 3;
				if(!getWorld().isRemote)
					getWorld().spawnEntity(flower);
			}
			getWorld().spawnParticle(EnumParticleTypes.NOTE, (double)pos.getX() + 0.5D+MathUtils.randomDouble(getWorld().rand)*3, (double)pos.getY() + 1.2D, (double)pos.getZ() + 0.5D+MathUtils.randomDouble(getWorld().rand)*3, (double)MathUtils.randomDouble(getWorld().rand)*24 / 24.0D, 0.0D, 0.0D);
			return;
		}
		
		if(getStackInSlot(1) != null && getStackInSlot(1).getItem() instanceof ItemRecord && recordPlayed == 0 && recordCooldownTime == 0 && getMRU() > 500) {
			setMRU(getMRU() - 500);
			recordPlayed = 1;
			recordCooldownTime = 100;
			if(!getWorld().isRemote)
				getWorld().playEvent((EntityPlayer)null, 1010, pos, Item.getIdFromItem(getStackInSlot(1).getItem()));
		}
		
		if(getStackInSlot(1) != null) {
			if(getWorld().rand.nextFloat() <= 0.33F)
				getWorld().spawnParticle(EnumParticleTypes.NOTE, (double)pos.getX() + 0.5D, (double)pos.getY() + 1.2D, (double)pos.getZ() + 0.5D+MathUtils.randomDouble(getWorld().rand)/2, (double)MathUtils.randomDouble(getWorld().rand)*24 / 24.0D, 0.0D, 0.0D);
		}
		
		if(recordCooldownTime > 0)
			--recordCooldownTime;
		
		if(getStackInSlot(1) != null && getStackInSlot(1).getItem() instanceof ItemRecord && recordPlayed == 1 && recordCooldownTime == 0 && getWorld().isBlockIndirectlyGettingPowered(pos) > 0 && getMRU() > 500) {
			setMRU(getMRU() - 500);
			recordCooldownTime = 100;
			if(!getWorld().isRemote)
				getWorld().playEvent((EntityPlayer)null, 1010, pos, Item.getIdFromItem(getStackInSlot(1).getItem()));
		}
		if(getStackInSlot(1) == null || !(getStackInSlot(1).getItem() instanceof ItemRecord) && recordPlayed == 1) {
			recordPlayed = 0;
			getWorld().playEvent(1010, pos, 0);
			getWorld().playRecord(pos, null);
		}
	}
	
	@Override
	public int[] getOutputSlots() {
		return new int[] {1};
	}
}
