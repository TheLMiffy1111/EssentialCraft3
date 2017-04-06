package ec3.common.tile;

import java.util.List;

import ec3.common.block.BlockCorruption_Light;
import ec3.common.block.BlocksCore;
import ec3.common.registry.BiomeRegistry;
import ec3.common.registry.PotionRegistry;
import ec3.utils.common.ECUtils;
import DummyCore.Utils.DataStorage;
import DummyCore.Utils.DummyData;
import DummyCore.Utils.MiscUtils;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.INetHandlerPlayClient;
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
	
	@SuppressWarnings("unchecked")
	public void update() {
		try {
			Potion potionToAdd = PotionRegistry.mruCorruptionPotion;
			Block blk = getWorld().getBlockState(pos).getBlock();
			if(blk == BlocksCore.lightCorruption[0]) {
				potionToAdd = PotionRegistry.chaosInfluence;
			}
			if(blk == BlocksCore.lightCorruption[1]) {
				potionToAdd = PotionRegistry.frozenMind;
			}
			List<EntityPlayer> players = getWorld().getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(pos));
			for(int i = 0; i < players.size(); ++i) {
				if(!getWorld().isRemote) {
					float increasement = 5;
					increasement *= ECUtils.getGenResistance(1, players.get(i));
					ECUtils.calculateAndAddPE(players.get(i), potionToAdd, 2000, (int)increasement);
				}
			}
			if(getWorld().rand.nextFloat() <= 0.0001F) {
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
				if(!getWorld().isRemote)
					changeBiomeAtPos(biomeId);
			}
			int metadata = getWorld().getBlockState(pos).getValue(BlockCorruption_Light.LEVEL);
			if(metadata >= 7 && canDestroyBlocks) {
				for(int i = 0; i < 6; ++i) {
					EnumFacing dir = EnumFacing.getFront(i);
					if(getWorld().getTileEntity(pos.offset(dir)) == null && getWorld().getBlockState(pos.offset(dir)).getBlock().isBlockSolid(getWorld(), pos.offset(dir), dir.getOpposite())) {
						getWorld().setBlockState(pos.offset(dir), blk.getStateFromMeta(0), 3);
					}
				}
				getWorld().setBlockToAir(pos);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}
	
	public static void setupConfig(Configuration cfg) {
		try {
			cfg.load();
			String[] cfgArrayString = cfg.getStringList("CorruptionSettings", "tileentities", new String[] {
					"Change Biome:true",
					"Destroy Blocks if grown:true"
			}, "Settings of the given Device.");
			String dataString = "";
			
			for(int i = 0; i < cfgArrayString.length; ++i)
				dataString += "||" + cfgArrayString[i];
			
			DummyData[] data = DataStorage.parseData(dataString);
			canChangeBiome = Boolean.parseBoolean(data[0].fieldValue);
			canDestroyBlocks = Boolean.parseBoolean(data[1].fieldValue);
			cfg.save();
		}
		catch(Exception e) {
			return;
		}
	}
}
