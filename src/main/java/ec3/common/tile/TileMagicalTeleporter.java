package ec3.common.tile;

import java.util.List;

import DummyCore.Utils.DataStorage;
import DummyCore.Utils.DummyData;
import DummyCore.Utils.DummyPortalGenerator;
import DummyCore.Utils.DummyPortalHandler;
import DummyCore.Utils.DummyTeleporter;
import DummyCore.Utils.MathUtils;
import DummyCore.Utils.MiscUtils;
import ec3.api.ApiCore;
import ec3.common.block.BlocksCore;
import ec3.common.item.ItemBoundGem;
import ec3.common.mod.EssentialCraftCore;
import ec3.utils.common.ECUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.config.Configuration;

public class TileMagicalTeleporter extends TileMRUGeneric {
	public int progressLevel;
	public static float cfgMaxMRU = ApiCore.DEVICE_MAX_MRU_GENERIC*10;
	public static boolean generatesCorruption = false;
	public static int genCorruption = 5;
	public static int mruUsage = 500;
	public static int teleportTime = 250;
	public static boolean allowDimensionalTeleportation = true;

	public TileMagicalTeleporter() {
		super();
		setSlotsNum(2);
	}

	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
		super.readFromNBT(par1NBTTagCompound);
		progressLevel = par1NBTTagCompound.getInteger("progress");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound par1NBTTagCompound) {
		super.writeToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setInteger("progress", progressLevel);
		return par1NBTTagCompound;
	}

	@Override
	public void update()  {
		maxMRU = (int)cfgMaxMRU;
		super.update();
		spawnParticles();
		if(getWorld().isBlockIndirectlyGettingPowered(pos) == 0)
			tryTeleport();
		ECUtils.manage(this, 0);
	}

	public boolean isStructureCorrect() {
		boolean flag = true;
		for(int x = -2; x <= 2; ++x) {
			for(int z = -2; z <= 2; ++z) {
				if(((x == 2 || x == -2) && z == 0) || ((z == 2 || z == -2) && x == 0)) {
					flag = getWorld().getBlockState(pos.add(x, 0, z)).getBlock() == BlocksCore.magicPlating;
					if(!flag)
						return false;
				}
				else if(x != 0 || z != 0) {
					flag = getWorld().getBlockState(pos.add(x, 0, z)).getBlock() == BlocksCore.voidStone;
					if(!flag)
						return false;	
				}
			}
		}
		flag =
				getWorld().getBlockState(pos.add(1, 1, 2)).getBlock() == BlocksCore.voidStone &&
				getWorld().getBlockState(pos.add(1, 2, 2)).getBlock() == BlocksCore.voidStone &&
				getWorld().getBlockState(pos.add(2, 1, 1)).getBlock() == BlocksCore.voidStone &&
				getWorld().getBlockState(pos.add(2, 2, 1)).getBlock() == BlocksCore.voidStone &&
				getWorld().getBlockState(pos.add(-2, 1, 1)).getBlock() == BlocksCore.voidStone &&
				getWorld().getBlockState(pos.add(-2, 2, 1)).getBlock() == BlocksCore.voidStone &&
				getWorld().getBlockState(pos.add(-1, 1, 2)).getBlock() == BlocksCore.voidStone &&
				getWorld().getBlockState(pos.add(-1, 2, 2)).getBlock() == BlocksCore.voidStone &&
				getWorld().getBlockState(pos.add(2, 1, -1)).getBlock() == BlocksCore.voidStone &&
				getWorld().getBlockState(pos.add(2, 2, -1)).getBlock() == BlocksCore.voidStone &&
				getWorld().getBlockState(pos.add(1, 1, -2)).getBlock() == BlocksCore.voidStone &&
				getWorld().getBlockState(pos.add(1, 2, -2)).getBlock() == BlocksCore.voidStone &&
				getWorld().getBlockState(pos.add(-1, 1, -2)).getBlock() == BlocksCore.voidStone &&
				getWorld().getBlockState(pos.add(-1, 2, -2)).getBlock() == BlocksCore.voidStone &&
				getWorld().getBlockState(pos.add(-2, 1, -1)).getBlock() == BlocksCore.voidStone &&
				getWorld().getBlockState(pos.add(-2, 2, -1)).getBlock() == BlocksCore.voidStone;
		return flag;
	}

	public void tryTeleport() {
		if(hasRequiredItemToTeleport() && hasPlayer() && getMRU() >= getTPCost() && isStructureCorrect()) {
			EntityPlayer player = getPlayer();
			++progressLevel;
			getWorld().playSound(player, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_MINECART_RIDING, SoundCategory.BLOCKS, 0.1F, 0.8F + ((float)progressLevel/teleportTime));
			if(progressLevel >= teleportTime) {
				if(generatesCorruption)
					ECUtils.increaseCorruptionAt(getWorld(), pos.getX(), pos.getY(), pos.getZ(), getWorld().rand.nextInt(genCorruption));
				int[] tpCoords = getCoordsToTP();
				if(!getWorld().isRemote) {
					setMRU(getMRU()-getTPCost());
					for(int i = 0; i < 20; ++i) {
						getWorld().playSound(player, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_FIREWORK_LARGE_BLAST_FAR, SoundCategory.BLOCKS, 1.0F, 0.5F+MathUtils.randomFloat(getWorld().rand));
					}
					int currentPlayerDim = player.dimension;
					int newDim = getDimensionToTP();
					if(currentPlayerDim != newDim && !player.world.isRemote && allowDimensionalTeleportation) {
						MinecraftServer mcServer = getWorld().getMinecraftServer();
						EntityPlayerMP p_72356_1_ = (EntityPlayerMP)player;
						WorldServer transferTo = mcServer.worldServerForDimension(newDim);
						DummyTeleporter teleporter = new DummyTeleporter(transferTo, tpCoords[0]+0.5, tpCoords[1]+1.5, tpCoords[2]+0.5,DummyPortalGenerator.TELEPORT_ONLY, false);
						DummyPortalHandler.transferPlayerToDimension(p_72356_1_, newDim, teleporter);
					}
					player.setPositionAndUpdate(tpCoords[0]+0.5, tpCoords[1]+1.5, tpCoords[2]+0.5);
					for(int i = 0; i < 20; ++i) {
						player.world.playSound(player, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_FIREWORK_LARGE_BLAST, SoundCategory.BLOCKS, 1.0F, 0.5F + MathUtils.randomFloat(getWorld().rand));
					}
				}
				progressLevel = 0;
			}
		}
		else
			progressLevel = 0;
	}

	public int getTPCost() {
		int[] tpCoords = getCoordsToTP();
		int dim = getDimensionToTP();
		int diffX = (int)MathUtils.getDifference(pos.getX(), tpCoords[0]);
		int diffY = (int)MathUtils.getDifference(pos.getY(), tpCoords[1]);
		int diffZ = (int)MathUtils.getDifference(pos.getZ(), tpCoords[2]);
		float mainDiff = (diffX+diffY+diffZ)/3;
		int ret = (int)(mruUsage * mainDiff);
		if(ret > cfgMaxMRU)
			ret = (int)cfgMaxMRU;
		if(getWorld().provider.getDimension() != dim)
			ret = (int)cfgMaxMRU;
		return ret;
	}

	public EntityPlayer getPlayer() {
		List<EntityPlayer> l = getWorld().<EntityPlayer>getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX()+1, pos.getY()+2, pos.getZ()+1));
		if(!l.isEmpty())
			return l.get(0);
		return null;
	}

	public boolean hasPlayer() {
		List<EntityPlayer> l = getWorld().<EntityPlayer>getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX()+1, pos.getY()+2, pos.getZ()+1));
		if(!l.isEmpty())
			return true;
		return false;
	}

	public int getDimensionToTP() {
		ItemStack s = getStackInSlot(1);
		return MiscUtils.getStackTag(s).getInteger("dim");
	}

	public int[] getCoordsToTP() {
		ItemStack s = getStackInSlot(1);
		return MiscUtils.getStackTag(s).getIntArray("pos");
	}

	public boolean hasRequiredItemToTeleport() {
		return getStackInSlot(1) != null && getStackInSlot(1).getItem() instanceof ItemBoundGem && getStackInSlot(1).getTagCompound() != null;
	}

	public void spawnParticles() {
		if(hasPlayer()) {
			EntityPlayer p = getPlayer();
			for(int i = 0; i < (progressLevel/5); ++i) {
				getWorld().spawnParticle(EnumParticleTypes.REDSTONE, p.posX+MathUtils.randomFloat(getWorld().rand)/2, p.posY+MathUtils.randomFloat(getWorld().rand)*2-1, p.posZ+MathUtils.randomFloat(getWorld().rand)/2, 0, 0, 1);
				getWorld().spawnParticle(EnumParticleTypes.REDSTONE, pos.getX()+0.5+MathUtils.randomFloat(getWorld().rand)*2, pos.getY()+3,pos.getZ()+0.5+MathUtils.randomFloat(getWorld().rand)*2, 0, 0, 1);
				getWorld().spawnParticle(EnumParticleTypes.REDSTONE, pos.getX()+0.5+2, pos.getY()+2+MathUtils.randomFloat(getWorld().rand),pos.getZ()+0.5+MathUtils.randomFloat(getWorld().rand)*2, 0, 0, 1);
				getWorld().spawnParticle(EnumParticleTypes.REDSTONE, pos.getX()+0.5-2, pos.getY()+2+MathUtils.randomFloat(getWorld().rand),pos.getZ()+0.5+MathUtils.randomFloat(getWorld().rand)*2, 0, 0, 1);
				getWorld().spawnParticle(EnumParticleTypes.REDSTONE, pos.getX()+0.5+MathUtils.randomFloat(getWorld().rand)*2, pos.getY()+2+MathUtils.randomFloat(getWorld().rand),pos.getZ()+0.5-2, 0, 0, 1);
				getWorld().spawnParticle(EnumParticleTypes.REDSTONE, pos.getX()+0.5+MathUtils.randomFloat(getWorld().rand)*2, pos.getY()+2+MathUtils.randomFloat(getWorld().rand),pos.getZ()+0.5+2, 0, 0, 1);
			}
			for(int i = 0; i < 4; ++i) {}
		}
		if(isStructureCorrect()) {
			/*for(int i = 0; i < 100; ++i)*/ {
				EssentialCraftCore.proxy.spawnParticle("cSpellFX", pos.getX()+0.5F+MathUtils.randomFloat(getWorld().rand)*3, pos.getY()+1, pos.getZ()+0.5F+MathUtils.randomFloat(getWorld().rand)*3, 0,2, 0);
			}
		}
	}

	public static void setupConfig(Configuration cfg) {
		try {
			cfg.load();
			String[] cfgArrayString = cfg.getStringList("MagicalTeleporterSettings", "tileentities", new String[]{
					"Max MRU:" + ApiCore.DEVICE_MAX_MRU_GENERIC*10,
					"MRU Usage per block:500",
					"Can this device actually generate corruption:false",
					"The amount of corruption generated each tick(do not set to 0!):5",
					"Ticks required for the teleporter to teleport an entity:250",
					"Allow teleportation between dimensions:true"
			}, "");
			String dataString = "";

			for(int i = 0; i < cfgArrayString.length; ++i)
				dataString += "||" + cfgArrayString[i];

			DummyData[] data = DataStorage.parseData(dataString);

			mruUsage = Integer.parseInt(data[1].fieldValue);
			cfgMaxMRU = Float.parseFloat(data[0].fieldValue);
			generatesCorruption = Boolean.parseBoolean(data[2].fieldValue);
			genCorruption = Integer.parseInt(data[3].fieldValue);
			teleportTime = Integer.parseInt(data[4].fieldValue);
			allowDimensionalTeleportation = Boolean.parseBoolean(data[5].fieldValue);

			cfg.save();
		}
		catch(Exception e) {
			return;
		}
	}

	@Override
	public int[] getOutputSlots() {
		return new int[] {1};
	}
}
