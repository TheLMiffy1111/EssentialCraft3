package essentialcraft.common.tile;

import essentialcraft.common.entity.EntitySolarBeam;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.config.Configuration;

public class TileSolarPrism extends TileEntity implements ITickable {

	public static double solarBeamChance = 0.025D;
	public static boolean requiresUnabstructedSky = true;
	public static boolean requiresMidday = true;
	public static boolean ignoreRain = false;

	@Override
	public void update() {
		if(getWorld().isAirBlock(pos.east(2)) || getWorld().isAirBlock(pos.west(2)) || getWorld().isAirBlock(pos.north(2)) || getWorld().isAirBlock(pos.south(2))) {
			getWorld().getBlockState(pos).getBlock().dropBlockAsItem(getWorld(), pos, getWorld().getBlockState(pos), 0);
			getWorld().setBlockToAir(pos);
			return;
		}
		if(!getWorld().isAirBlock(pos.east()) || !getWorld().isAirBlock(pos.west()) || !getWorld().isAirBlock(pos.north()) || !getWorld().isAirBlock(pos.south())) {
			getWorld().getBlockState(pos).getBlock().dropBlockAsItem(getWorld(), pos, getWorld().getBlockState(pos), 0);
			getWorld().setBlockToAir(pos);
			return;
		}
		if(!getWorld().isAirBlock(pos.add(1, 0, 1)) || !getWorld().isAirBlock(pos.add(1, 0, -1)) || !getWorld().isAirBlock(pos.add(-1, 0, -1)) || !getWorld().isAirBlock(pos.add(-1, 0, 1))) {
			getWorld().getBlockState(pos).getBlock().dropBlockAsItem(getWorld(), pos, getWorld().getBlockState(pos), 0);
			getWorld().setBlockToAir(pos);
			return;
		}
		if(!getWorld().isRemote) {
			if(getWorld().rand.nextDouble() <= solarBeamChance && (getWorld().canBlockSeeSky(pos) || !requiresUnabstructedSky) && (getWorld().getWorldTime() % 24000 >= 5000 && getWorld().getWorldTime() % 24000 <= 7000 || !requiresMidday) && (!getWorld().isRaining() || ignoreRain)) {
				int y = pos.getY()-1;
				BlockPos.MutableBlockPos p = new BlockPos.MutableBlockPos(pos.down());
				while(y > 0 && getWorld().isAirBlock(p)) {
					--y;
					p.setY(y);
					if(!getWorld().isAirBlock(p)) {
						EntitySolarBeam beam = new EntitySolarBeam(getWorld(),pos.getX()+0.5,y,pos.getZ()+0.5);
						getWorld().spawnEntity(beam);
					}
				}
			}
		}
	}

	public static void setupConfig(Configuration cfg) {
		try {
			String category = "tileentities.solarprism";
			solarBeamChance = cfg.get(category, "SolarBeamChance", 0.025D, "Chance each tick to create a solar beam").setMinValue(0D).getDouble();
			requiresUnabstructedSky = cfg.get(category, "RequiresUnobstructedSky", true).getBoolean();
			requiresMidday = cfg.get(category, "RequiresMidday", true).getBoolean();
			ignoreRain = cfg.get(category, "IgnoreRain", false).getBoolean();
		}
		catch(Exception e) {
			return;
		}
	}
}
