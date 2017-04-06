package ec3.common.tile;

import DummyCore.Utils.Coord3D;
import DummyCore.Utils.Lightning;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ec3.common.block.BlocksCore;
import ec3.common.registry.SoundRegistry;

public class TileMRUCoil_Hardener extends TileEntity implements ITickable {
	
	@SideOnly(Side.CLIENT)
	public Lightning localLightning;
	
	@Override
	public void update() {
		if(getWorld().isRemote) {
			if(localLightning == null && getWorld().rand.nextFloat() <= 0.01F) {
				BlockPos.MutableBlockPos dp = new BlockPos.MutableBlockPos(pos);
				int dy = pos.getY();
				while(getWorld().getBlockState(dp).getBlock() == BlocksCore.mruCoilHardener) {
					++dy;
					dp.setY(dy);
				}
				if(getWorld().getBlockState(dp).getBlock() == BlocksCore.mruCoil) {
					TileMRUCoil tile = (TileMRUCoil)getWorld().getTileEntity(dp);
					if(tile.isStructureCorrect()) {
						EnumFacing fDir = EnumFacing.getHorizontal(getWorld().rand.nextInt(4));
						localLightning = new Lightning(getWorld().rand, new Coord3D(0.5F+(float)fDir.getFrontOffsetX()/2.3F, 0, 0.5F+(float)fDir.getFrontOffsetZ()/2.3F), new Coord3D(0.5F+(float)fDir.getFrontOffsetX()/2.3F, 1, 0.5F+(float)fDir.getFrontOffsetZ()/2.3F), 0.03F, 1.0F, 0.1F, 0.8F);
						getWorld().playSound(pos.getX()+0.5F+(float)fDir.getFrontOffsetX()/2.3F, pos.getY()+0.5F, pos.getZ()+0.5F+(float)fDir.getFrontOffsetZ()/2.3F, SoundRegistry.machineGenElectricity, SoundCategory.BLOCKS, 0.1F, 1F, false);
					}
				}
			}
			else if(localLightning != null && localLightning.renderTicksExisted >= 40)
				localLightning = null;
		}
	}
}
