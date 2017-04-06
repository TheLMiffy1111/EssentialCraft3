package ec3.common.tile;

import DummyCore.Utils.MathUtils;
import ec3.api.WindImbueRecipe;
import ec3.common.block.BlockMithrilineCrystal;
import ec3.common.block.BlocksCore;
import ec3.common.item.ItemSoulStone;
import ec3.common.mod.EssentialCraftCore;
import ec3.utils.common.ECUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class TileWindRune extends TileEntity implements ITickable {
	public int tier = -1;
	public int sCheckTick = 0;
	public int energy;
	public int energyCheck = 0;

	public int getEnderstarEnergy() {
		if(tier == -1)
			return 0;

		int[][] coords = new int[][]{
			{2, 0, 2}, {2, 0, -2}, {-2, 0, 2}, {-2, 0, -2},
			{1, 2, 1}, {-1, 2, 1}, {1, 2, -1}, {-1, 2, -1}
		};
		int energy = 0;

		for(int i = 0; i < 8; ++i) {
			TileEntity tile = getWorld().getTileEntity(pos.add(coords[i][0], coords[i][1], coords[i][2]));
			if(tile instanceof TileMithrilineCrystal) {
				energy += MathHelper.floor(TileMithrilineCrystal.class.cast(tile).energy);
			}
		}

		return energy;
	}

	public boolean action(EntityPlayer player) {
		if(player.getHeldItemMainhand() != null) {
			ItemStack item = player.getHeldItemMainhand();
			WindImbueRecipe rec = WindImbueRecipe.findRecipeByComponent(item);
			if(rec != null) {
				int energy = this.energy;
				boolean hasEnergy = energy >= rec.enderEnergy;
				boolean creative = player.capabilities.isCreativeMode;
				if(hasEnergy || creative) {
					int[][] coords = new int[][]{
						{2, 0, 2}, {2, 0, -2}, {-2, 0, 2}, {-2, 0, -2},
						{1, 2, 1}, {-1, 2, 1}, {1, 2, -1}, {-1, 2, -1}
					};
					int cenergy = 0;

					for(int i = 0; i < 8; ++i) {
						TileEntity tile = getWorld().getTileEntity(pos.add(coords[i][0], coords[i][1], coords[i][2]));
						if(tile instanceof TileMithrilineCrystal) {
							cenergy += MathHelper.floor(TileMithrilineCrystal.class.cast(tile).energy);
							if(hasEnergy)
								TileMithrilineCrystal.class.cast(tile).energy = 0;

							if(cenergy >= energy) {
								int left = cenergy - energy;
								if(hasEnergy)
									TileMithrilineCrystal.class.cast(tile).energy = left;

								if(rec.transforming.getItem() instanceof ItemSoulStone && !ECUtils.getData(player).isWindbound()) {
									ECUtils.getData(player).modifyWindbound(true);
									if(!player.world.isRemote) {
										player.sendMessage(new TextComponentString(I18n.translateToLocal("ec3.txt.windImbue")).setStyle(new Style().setColor(TextFormatting.AQUA)));
										ECUtils.requestSync(player);
									}
								}

								player.inventory.decrStackSize(player.inventory.currentItem, 1);
								if(player.inventory.getStackInSlot(player.inventory.currentItem) == null)
									player.inventory.setInventorySlotContents(player.inventory.currentItem, rec.result.copy());
								else
									if(!player.inventory.addItemStackToInventory(rec.result.copy()))
										player.dropItem(rec.result.copy(), true);

								for(int j = 0; j < 300; ++j) 
									EssentialCraftCore.proxy.SmokeFX(pos.getX()+0.5D + MathUtils.randomDouble(getWorld().rand)*1.6D, pos.getY(), pos.getZ()+0.5D + MathUtils.randomDouble(getWorld().rand)*1.6D, 0, getWorld().rand.nextDouble()*0.3D, 0, 1, 0.7D, 1.0D, 0.85D);

								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}

	@Override
	public void update() {
		if(energyCheck == 0) {
			energyCheck = 100;
			energy = getEnderstarEnergy();
		}
		else
			--energyCheck;

		if(sCheckTick == 0) {
			checkStructureAndTier();
			sCheckTick = 200;
		}
		else
			--sCheckTick;

		float movement = getWorld().getWorldTime() % 60;
		if(movement > 30)
			movement = 60F - movement;
		int[][] coords = new int[][]{
			{2, 0, 2}, {2, 0, -2}, {-2, 0, 2}, {-2, 0, -2},
			{1, 2, 1}, {-1, 2, 1}, {1, 2, -1}, {-1, 2, -1}
		};
		if(tier >= 0) {
			EssentialCraftCore.proxy.SmokeFX(pos.getX()+0.5D + MathUtils.randomDouble(getWorld().rand)*1.6D, pos.getY(), pos.getZ()+0.5D + MathUtils.randomDouble(getWorld().rand)*1.6D, 0, getWorld().rand.nextDouble()*0.3D, 0, 1, 0.2D, 1.0D, 0.45D);

			for(int i = 0; i < 8; ++i) {
				getWorld().spawnParticle(EnumParticleTypes.REDSTONE, pos.getX()+coords[i][0]+0.5D, pos.getY()+coords[i][1]+movement/30, pos.getZ()+coords[i][2]+0.5D, -1, 1, 0);
				getWorld().spawnParticle(EnumParticleTypes.REDSTONE, pos.getX()+coords[i][0]+0.5D, pos.getY()+coords[i][1]+2+movement/30, pos.getZ()+coords[i][2]+0.5D, -1, 1, 0);
			}
		}
	}

	public void checkStructureAndTier() {
		World w = getWorld();
		BlockPos cp = pos.down();
		tier = -1;
		if(
				w.getBlockState(cp.add(0, 0, 0)).getBlock() == BlocksCore.invertedBlock &&
				w.getBlockState(cp.add(1, 0, 0)).getBlock() == BlocksCore.invertedBlock &&
				w.getBlockState(cp.add(-1, 0, 0)).getBlock() == BlocksCore.invertedBlock &&
				w.getBlockState(cp.add(0, 0, 1)).getBlock() == BlocksCore.invertedBlock &&
				w.getBlockState(cp.add(0, 0, -1)).getBlock() == BlocksCore.invertedBlock &&
				w.getBlockState(cp.add(1, 1, -1)).getBlock() == BlocksCore.invertedBlock &&
				w.getBlockState(cp.add(1, 1, 1)).getBlock() == BlocksCore.invertedBlock &&
				w.getBlockState(cp.add(-1, 1, -1)).getBlock() == BlocksCore.invertedBlock &&
				w.getBlockState(cp.add(-1, 1, 1)).getBlock() == BlocksCore.invertedBlock &&
				w.getBlockState(cp.add(1, 2, -1)).getBlock() == BlocksCore.invertedBlock &&
				w.getBlockState(cp.add(1, 2, 1)).getBlock() == BlocksCore.invertedBlock &&
				w.getBlockState(cp.add(-1, 2, -1)).getBlock() == BlocksCore.invertedBlock &&
				w.getBlockState(cp.add(-1, 2, 1)).getBlock() == BlocksCore.invertedBlock &&
				w.getBlockState(cp.add(-1, 3, 1)).getBlock() == BlocksCore.mithrilineCrystal &&
				w.getBlockState(cp.add(1, 3, 1)).getBlock() == BlocksCore.mithrilineCrystal &&
				w.getBlockState(cp.add(-1, 3, -1)).getBlock() == BlocksCore.mithrilineCrystal &&
				w.getBlockState(cp.add(1, 3, -1)).getBlock() == BlocksCore.mithrilineCrystal &&
				w.getBlockState(cp.add(-2, 1, 2)).getBlock() == BlocksCore.mithrilineCrystal &&
				w.getBlockState(cp.add(2, 1, 2)).getBlock() == BlocksCore.mithrilineCrystal &&
				w.getBlockState(cp.add(-2, 1, -2)).getBlock() == BlocksCore.mithrilineCrystal &&
				w.getBlockState(cp.add(2, 1, -2)).getBlock() == BlocksCore.mithrilineCrystal &&
				w.getBlockState(cp.add(1, 0, -1)).getBlock() == BlocksCore.platingPale &&
				w.getBlockState(cp.add(2, 0, -1)).getBlock() == BlocksCore.platingPale &&
				w.getBlockState(cp.add(-2, 0, -1)).getBlock() == BlocksCore.platingPale &&
				w.getBlockState(cp.add(2, 0, 1)).getBlock() == BlocksCore.platingPale &&
				w.getBlockState(cp.add(-2, 0, 1)).getBlock() == BlocksCore.platingPale &&
				w.getBlockState(cp.add(1, 0, -2)).getBlock() == BlocksCore.platingPale &&
				w.getBlockState(cp.add(-1, 0, -2)).getBlock() == BlocksCore.platingPale &&
				w.getBlockState(cp.add(1, 0, 2)).getBlock() == BlocksCore.platingPale &&
				w.getBlockState(cp.add(-1, 0, 2)).getBlock() == BlocksCore.platingPale &&
				w.getBlockState(cp.add(-2, 0, 0)).getBlock() == BlocksCore.magicPlating &&
				w.getBlockState(cp.add(-2, 1, 0)).getBlock() == BlocksCore.magicPlating &&
				w.getBlockState(cp.add(2, 0, 0)).getBlock() == BlocksCore.magicPlating &&
				w.getBlockState(cp.add(2, 1, 0)).getBlock() == BlocksCore.magicPlating &&
				w.getBlockState(cp.add(0, 0, -2)).getBlock() == BlocksCore.magicPlating &&
				w.getBlockState(cp.add(0, 1, -2)).getBlock() == BlocksCore.magicPlating &&
				w.getBlockState(cp.add(0, 0, 2)).getBlock() == BlocksCore.magicPlating &&
				w.getBlockState(cp.add(0, 1, 2)).getBlock() == BlocksCore.magicPlating &&
				w.getBlockState(cp.add(1, 0, 1)).getBlock() == BlocksCore.platingPale &&
				w.getBlockState(cp.add(-1, 0, -1)).getBlock() == BlocksCore.platingPale &&
				w.getBlockState(cp.add(-1, 0, 1)).getBlock() == BlocksCore.platingPale &&
				w.getBlockState(cp.add(1, 0, -1)).getBlock() == BlocksCore.platingPale &&
				w.getBlockState(cp.add(-1, 3, 1)).getValue(BlockMithrilineCrystal.TYPE).getIndex() == 0 &&
				w.getBlockState(cp.add(1, 3, 1)).getValue(BlockMithrilineCrystal.TYPE).getIndex() == 0 &&
				w.getBlockState(cp.add(-1, 3, -1)).getValue(BlockMithrilineCrystal.TYPE).getIndex() == 0 &&
				w.getBlockState(cp.add(1, 3, -1)).getValue(BlockMithrilineCrystal.TYPE).getIndex() == 0 &&
				w.getBlockState(cp.add(-2, 1, 2)).getValue(BlockMithrilineCrystal.TYPE).getIndex() == 0 &&
				w.getBlockState(cp.add(2, 1, 2)).getValue(BlockMithrilineCrystal.TYPE).getIndex() == 0 &&
				w.getBlockState(cp.add(-2, 1, -2)).getValue(BlockMithrilineCrystal.TYPE).getIndex() == 0 &&
				w.getBlockState(cp.add(2, 1, -2)).getValue(BlockMithrilineCrystal.TYPE).getIndex() == 0
				) {
			tier = 0;
		}
	}
}
