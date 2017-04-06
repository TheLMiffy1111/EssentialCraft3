package ec3.common.tile;

import DummyCore.Utils.DataStorage;
import DummyCore.Utils.DummyData;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.config.Configuration;
import ec3.common.entity.EntitySolarBeam;

public class TileSolarPrism extends TileEntity implements ITickable {
	
	public static float solarBeamChance = 0.025F;
	public static boolean requiresUnabstructedSky = true;
	public static boolean requiresMidday = true;
	public static boolean ignoreRain = false;
	
	@Override
	public void update() {
		if(!getWorld().isRemote) {
			if(getWorld().rand.nextFloat() <= solarBeamChance && (getWorld().canBlockSeeSky(pos) || !requiresUnabstructedSky) && ((getWorld().getWorldTime() % 24000 >= 5000 && getWorld().getWorldTime() % 24000 <= 7000) || !requiresMidday) && (!getWorld().isRaining() || ignoreRain)) {
				int y = pos.getY()-1;
				BlockPos.MutableBlockPos p = new BlockPos.MutableBlockPos(pos.down());
				while(y > 0 && getWorld().isAirBlock(p)) {
					--y;
					p.setY(y);
					if(!getWorld().isAirBlock(p)) {
						EntitySolarBeam beam = new EntitySolarBeam(getWorld(),pos.getX(),y,pos.getZ());
						getWorld().spawnEntity(beam);
					}
				}
			}
			if(getWorld().isAirBlock(pos.east(2)) || getWorld().isAirBlock(pos.west(2)) || getWorld().isAirBlock(pos.north(2)) || getWorld().isAirBlock(pos.south(2))) {
				getWorld().getBlockState(pos).getBlock().dropBlockAsItem(getWorld(), pos, getWorld().getBlockState(pos), 0);
				getWorld().setBlockToAir(pos);
			}
			if(!getWorld().isAirBlock(pos.east()) || !getWorld().isAirBlock(pos.west()) || !getWorld().isAirBlock(pos.north()) || !getWorld().isAirBlock(pos.south())) {
				getWorld().getBlockState(pos).getBlock().dropBlockAsItem(getWorld(), pos, getWorld().getBlockState(pos), 0);
				getWorld().setBlockToAir(pos);
			}
			if(!getWorld().isAirBlock(pos.add(1, 0, 1)) || !getWorld().isAirBlock(pos.add(1, 0, -1)) || !getWorld().isAirBlock(pos.add(-1, 0, -1)) || !getWorld().isAirBlock(pos.add(-1, 0, 1))) {
				getWorld().getBlockState(pos).getBlock().dropBlockAsItem(getWorld(), pos, getWorld().getBlockState(pos), 0);
				getWorld().setBlockToAir(pos);
			}
		}
	}
	
	public static void setupConfig(Configuration cfg) {
		try {
			cfg.load();
			String[] cfgArrayString = cfg.getStringList("SolarPrismSettings", "tileentities", new String[] {
					"Chance each tick to create a solar beam:0.025",
					"Requires unobstructed sky:true",
					"Requires midday:true",
					"Is rain ignored:false"
			}, "");
			String dataString = "";
			
			for(int i = 0; i < cfgArrayString.length; ++i)
				dataString += "||" + cfgArrayString[i];
			
			DummyData[] data = DataStorage.parseData(dataString);
			
			solarBeamChance = Float.parseFloat(data[0].fieldValue);
			requiresUnabstructedSky = Boolean.parseBoolean(data[1].fieldValue);
			requiresMidday = Boolean.parseBoolean(data[2].fieldValue);
			ignoreRain = Boolean.parseBoolean(data[3].fieldValue);
			
			cfg.save();
		}
		catch(Exception e) {
			return;
		}
	}
}
