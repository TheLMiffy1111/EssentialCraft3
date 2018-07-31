package essentialcraft.common.tile;

import java.util.List;

import DummyCore.Utils.MiscUtils;
import essentialcraft.common.block.BlockCorruption;
import essentialcraft.common.block.BlocksCore;
import essentialcraft.common.registry.BiomeRegistry;
import essentialcraft.common.registry.PotionRegistry;
import essentialcraft.utils.common.ECUtils;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.config.Configuration;

public class TileCorruption extends TileEntity implements ITickable {

	public static boolean canChangeBiome = true, canDestroyBlocks = true;

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		if(pkt.getTileEntityType() == -11) {
			NBTTagCompound packetTag = pkt.getNbtCompound();
			int biomeID = packetTag.getInteger("biomeID");
			MiscUtils.changeBiome(getWorld(), Biome.getBiome(biomeID), pos.getX(), pos.getZ());
		}
	}

	public void changeBiomeAtPos(int biomeID) {
		if(canChangeBiome) {
			MiscUtils.changeBiome(getWorld(), Biome.getBiome(biomeID), pos.getX(), pos.getZ());
			NBTTagCompound nbttagcompound = new NBTTagCompound();
			nbttagcompound.setInteger("biomeID", biomeID);
			SPacketUpdateTileEntity pkt = new SPacketUpdateTileEntity(pos, -11, nbttagcompound);
			MiscUtils.sendPacketToAllAround(getWorld(), pkt, pos.getX(), pos.getY(), pos.getZ(), getWorld().provider.getDimension(), 32);
		}
	}

	public void addPotionEffectAtEntity(EntityLivingBase e, PotionEffect p) {
		if(!e.isPotionActive(p.getPotion())) {
			e.addPotionEffect(p);
		}
	}

	@Override
	public void update() {
		try {
			Potion potionToAdd = PotionRegistry.mruCorruption;
			Block blk = getWorld().getBlockState(pos).getBlock();
			if(blk == BlocksCore.lightCorruption[0]) {
				potionToAdd = PotionRegistry.chaosInfluence;
			}
			if(blk == BlocksCore.lightCorruption[1]) {
				potionToAdd = PotionRegistry.frozenMind;
			}
			List<EntityPlayer> players = getWorld().getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(pos));
			for(EntityPlayer player : players) {
				if(!player.isCreative() && !player.isSpectator()) {
					float increasement = 5;
					increasement *= ECUtils.getGenResistance(1, player);
					ECUtils.calculateAndAddPE(player, potionToAdd, 2000, (int)increasement);
				}
			}
			if(!getWorld().isRemote && getWorld().rand.nextFloat() <= 0.0001F) {
				int biomeId = 0;
				if(blk == BlocksCore.lightCorruption[0]) {
					biomeId = Biome.getIdForBiome(BiomeRegistry.chaosCorruption);
				}
				if(blk == BlocksCore.lightCorruption[1]) {
					biomeId = Biome.getIdForBiome(BiomeRegistry.frozenCorruption);
				}
				if(blk == BlocksCore.lightCorruption[2]) {
					biomeId = Biome.getIdForBiome(BiomeRegistry.shadowCorruption);
				}
				if(blk == BlocksCore.lightCorruption[3]) {
					biomeId = Biome.getIdForBiome(BiomeRegistry.magicCorruption);
				}
				changeBiomeAtPos(biomeId);
			}
			int metadata = getWorld().getBlockState(pos).getValue(BlockCorruption.LEVEL);
			if(metadata >= 7 && canDestroyBlocks) {
				for(int i = 0; i < 6; ++i) {
					EnumFacing dir = EnumFacing.getFront(i);
					if(getWorld().getTileEntity(pos.offset(dir)) == null && getWorld().getBlockState(pos.offset(dir)).getBlock().isSideSolid(getWorld().getBlockState(pos.offset(dir)), getWorld(), pos.offset(dir), dir.getOpposite())) {
						getWorld().setBlockState(pos.offset(dir), blk.getStateFromMeta(0), 3);
					}
				}
				getWorld().setBlockToAir(pos);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			return;
		}
	}

	public static void setupConfig(Configuration cfg) {
		try {
			String category = "tileentities.corruption";
			canChangeBiome = cfg.get(category, "ChangeBiome", true).getBoolean();
			canDestroyBlocks = cfg.get(category, "DestroyBlocks", true, "Destroy blocks if grown").getBoolean();
		}
		catch(Exception e) {
			return;
		}
	}
}
