package essentialcraft.common.tile;

import java.util.List;
import java.util.function.BiPredicate;

import DummyCore.Utils.BiPredicates;
import DummyCore.Utils.DummyPortalGenerator;
import DummyCore.Utils.DummyPortalHandler;
import DummyCore.Utils.DummyTeleporter;
import DummyCore.Utils.MathUtils;
import DummyCore.Utils.MiscUtils;
import essentialcraft.api.ApiCore;
import essentialcraft.common.block.BlocksCore;
import essentialcraft.common.item.ItemBoundGem;
import essentialcraft.common.mod.EssentialCraftCore;
import essentialcraft.utils.common.ECUtils;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.config.Configuration;

public class TileMagicalTeleporter extends TileMRUGeneric {

	public int progressLevel;
	public static int cfgMaxMRU = ApiCore.DEVICE_MAX_MRU_GENERIC*10;
	public static boolean generatesCorruption = false;
	public static int genCorruption = 5;
	public static int mruUsage = 500;
	public static int teleportTime = 250;
	public static boolean allowDimensionalTeleportation = true;

	public TileMagicalTeleporter() {
		super(cfgMaxMRU);
		setSlotsNum(2);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		progressLevel = nbt.getInteger("progress");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setInteger("progress", progressLevel);
		return nbt;
	}

	@Override
	public void update()  {
		super.update();
		mruStorage.update(getPos(), getWorld(), getStackInSlot(0));
		spawnParticles();
		if(getWorld().isBlockIndirectlyGettingPowered(pos) == 0)
			tryTeleport();
	}

	protected static boolean testBlock(IBlockAccess world, BlockPos pos, Block block) {
		return world.getBlockState(pos).getBlock() == block;
	}

	protected static BiPredicate<IBlockAccess, BlockPos> structureChecker = BiPredicates.<IBlockAccess, BlockPos>and(
			(world, pos)->{
				for(int x = -2; x <= 2; ++x) {
					for(int z = -2; z <= 2; ++z) {
						if((x == 2 || x == -2) && z == 0 || (z == 2 || z == -2) && x == 0) {
							if(!testBlock(world, pos.add(x, 0, z), BlocksCore.magicPlating)) {
								return false;
							}
						}
						else if(x != 0 || z != 0) {
							if(!testBlock(world, pos.add(x, 0, z), BlocksCore.voidStone)) {
								return false;
							}
						}
					}
				}
				return true;
			}, (world, pos)->
			testBlock(world, pos.add( 1, 1, 2), BlocksCore.voidStone) &&
			testBlock(world, pos.add( 1, 2, 2), BlocksCore.voidStone) &&
			testBlock(world, pos.add( 2, 1, 1), BlocksCore.voidStone) &&
			testBlock(world, pos.add( 2, 2, 1), BlocksCore.voidStone) &&
			testBlock(world, pos.add(-2, 1, 1), BlocksCore.voidStone) &&
			testBlock(world, pos.add(-2, 2, 1), BlocksCore.voidStone) &&
			testBlock(world, pos.add(-1, 1, 2), BlocksCore.voidStone) &&
			testBlock(world, pos.add(-1, 2, 2), BlocksCore.voidStone) &&
			testBlock(world, pos.add( 2, 1,-1), BlocksCore.voidStone) &&
			testBlock(world, pos.add( 2, 2,-1), BlocksCore.voidStone) &&
			testBlock(world, pos.add( 1, 1,-2), BlocksCore.voidStone) &&
			testBlock(world, pos.add( 1, 2,-2), BlocksCore.voidStone) &&
			testBlock(world, pos.add(-1, 1,-2), BlocksCore.voidStone) &&
			testBlock(world, pos.add(-1, 2,-2), BlocksCore.voidStone) &&
			testBlock(world, pos.add(-2, 1,-1), BlocksCore.voidStone) &&
			testBlock(world, pos.add(-2, 2,-1), BlocksCore.voidStone)
			);

	public void tryTeleport() {
		if(getWorld().isBlockIndirectlyGettingPowered(pos) == 0) {
			if(hasRequiredItemToTeleport() && hasPlayer() && structureChecker.test(getWorld(), getPos())) {
				if(mruStorage.getMRU() >= getTPCost()) {
					EntityPlayer player = getPlayer();
					++progressLevel;
					getWorld().playSound(player.posX, player.posY, player.posZ, SoundEvents.ENTITY_MINECART_RIDING, SoundCategory.BLOCKS, 0.1F, 0.8F + (float)progressLevel/teleportTime, false);
					if(progressLevel >= teleportTime) {
						mruStorage.extractMRU(getTPCost(), true);
						if(generatesCorruption)
							ECUtils.randomIncreaseCorruptionAt(getWorld(), pos, getWorld().rand, (genCorruption));
						int[] tpCoords = getCoordsToTP();
						for(int i = 0; i < 20; ++i) {
							getWorld().playSound(player, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_FIREWORK_LARGE_BLAST_FAR, SoundCategory.BLOCKS, 1.0F, 0.5F+MathUtils.randomFloat(getWorld().rand));
						}
						int currentPlayerDim = player.dimension;
						int newDim = getDimensionToTP();
						if(currentPlayerDim != newDim && allowDimensionalTeleportation && !player.world.isRemote) {
							MinecraftServer mcServer = getWorld().getMinecraftServer();
							EntityPlayerMP playerMP = (EntityPlayerMP)player;
							WorldServer transferTo = mcServer.getWorld(newDim);
							DummyTeleporter teleporter = new DummyTeleporter(transferTo, tpCoords[0]+0.5, tpCoords[1]+1.5, tpCoords[2]+0.5,DummyPortalGenerator.TELEPORT_ONLY, false);
							DummyPortalHandler.transferPlayerToDimension(playerMP, newDim, teleporter);
						}
						if(!player.world.isRemote) {
							player.setPositionAndUpdate(tpCoords[0]+0.5D, tpCoords[1]+1D, tpCoords[2]+0.5D);
						}
						for(int i = 0; i < 20; ++i) {
							player.world.playSound(player, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_FIREWORK_LARGE_BLAST, SoundCategory.BLOCKS, 1.0F, 0.5F+MathUtils.randomFloat(getWorld().rand));
						}
						progressLevel = 0;
					}
				}
			}
			else
				progressLevel = 0;
		}
	}

	public int getTPCost() {
		int[] tpCoords = getCoordsToTP();
		int dim = getDimensionToTP();
		if(getWorld().provider.getDimension() != dim) {
			return cfgMaxMRU;
		}
		int diffX = (int)MathUtils.getDifference(pos.getX(), tpCoords[0]);
		int diffY = (int)MathUtils.getDifference(pos.getY(), tpCoords[1]);
		int diffZ = (int)MathUtils.getDifference(pos.getZ(), tpCoords[2]);
		double mainDiff = (diffX+diffY+diffZ)/3D;
		int ret = (int)(mruUsage * mainDiff);
		if(ret > cfgMaxMRU) {
			ret = cfgMaxMRU;
		}
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
		return !getStackInSlot(1).isEmpty() && getStackInSlot(1).getItem() instanceof ItemBoundGem && getStackInSlot(1).getTagCompound() != null;
	}

	public void spawnParticles() {
		if(world.isRemote) {
			if(hasPlayer()) {
				EntityPlayer p = getPlayer();
				for(int i = 0; i < progressLevel/5; ++i) {
					getWorld().spawnParticle(EnumParticleTypes.REDSTONE, p.posX+MathUtils.randomFloat(getWorld().rand)/2, p.posY+MathUtils.randomFloat(getWorld().rand)*2-1, p.posZ+MathUtils.randomFloat(getWorld().rand)/2, 0, 0, 1);
					getWorld().spawnParticle(EnumParticleTypes.REDSTONE, pos.getX()+0.5+MathUtils.randomFloat(getWorld().rand)*2, pos.getY()+3,pos.getZ()+0.5+MathUtils.randomFloat(getWorld().rand)*2, 0, 0, 1);
					getWorld().spawnParticle(EnumParticleTypes.REDSTONE, pos.getX()+0.5+2, pos.getY()+2+MathUtils.randomFloat(getWorld().rand),pos.getZ()+0.5+MathUtils.randomFloat(getWorld().rand)*2, 0, 0, 1);
					getWorld().spawnParticle(EnumParticleTypes.REDSTONE, pos.getX()+0.5-2, pos.getY()+2+MathUtils.randomFloat(getWorld().rand),pos.getZ()+0.5+MathUtils.randomFloat(getWorld().rand)*2, 0, 0, 1);
					getWorld().spawnParticle(EnumParticleTypes.REDSTONE, pos.getX()+0.5+MathUtils.randomFloat(getWorld().rand)*2, pos.getY()+2+MathUtils.randomFloat(getWorld().rand),pos.getZ()+0.5-2, 0, 0, 1);
					getWorld().spawnParticle(EnumParticleTypes.REDSTONE, pos.getX()+0.5+MathUtils.randomFloat(getWorld().rand)*2, pos.getY()+2+MathUtils.randomFloat(getWorld().rand),pos.getZ()+0.5+2, 0, 0, 1);
				}
			}
			if(structureChecker.test(getWorld(), getPos())) {
				/*for(int i = 0; i < 100; ++i)*/ {
					EssentialCraftCore.proxy.spawnParticle("cSpellFX", pos.getX()+0.5F+MathUtils.randomFloat(getWorld().rand)*3, pos.getY()+1, pos.getZ()+0.5F+MathUtils.randomFloat(getWorld().rand)*3, 0,2, 0);
				}
			}
		}
	}

	public static void setupConfig(Configuration cfg) {
		try {
			String category = "tileentities.magicalteleporter";
			cfgMaxMRU = cfg.get(category, "MaxMRU", ApiCore.DEVICE_MAX_MRU_GENERIC*10).setMinValue(1).getInt();
			mruUsage = cfg.get(category, "MRUUsage", 500).setMinValue(0).setMaxValue(cfgMaxMRU).getInt();
			generatesCorruption = cfg.get(category, "GenerateCorruption", false).getBoolean();
			genCorruption = cfg.get(category, "MaxCorruptionGen", 5, "Max amount of corruption generated per tick").setMinValue(0).getInt();
			teleportTime = cfg.get(category, "TicksRequired", 250).setMinValue(0).getInt();
			allowDimensionalTeleportation = cfg.get(category, "AllowDimensional", true).getBoolean();
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
