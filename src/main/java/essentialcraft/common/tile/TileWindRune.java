package essentialcraft.common.tile;

import java.util.function.BiPredicate;

import DummyCore.Utils.MathUtils;
import essentialcraft.api.IESPEHandler;
import essentialcraft.api.WindImbueRecipe;
import essentialcraft.common.block.BlocksCore;
import essentialcraft.common.capabilities.espe.CapabilityESPEHandler;
import essentialcraft.common.item.ItemSoulStone;
import essentialcraft.common.mod.EssentialCraftCore;
import essentialcraft.utils.common.ECUtils;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class TileWindRune extends TileEntity implements ITickable {
	public int tier = -1;
	public int sCheckTick = 0;
	public int energyCheck = 0;
	protected static Vec3i[] coords = {
			new Vec3i(2, 0, 2), new Vec3i(2, 0, -2), new Vec3i(-2, 0, 2), new Vec3i(-2, 0, -2),
			new Vec3i(1, 2, 1), new Vec3i(-1, 2, 1), new Vec3i(1, 2, -1), new Vec3i(-1, 2, -1)
	};

	public int getEnderstarEnergy() {
		if(tier == -1) {
			return 0;
		}

		double energy = 0;

		for(int i = 0; i < coords.length; ++i) {
			TileEntity tile = getWorld().getTileEntity(pos.add(coords[i]));
			if(tile == null) {
				continue;
			}
			if(tile.hasCapability(CapabilityESPEHandler.ESPE_HANDLER_CAPABILITY, null)) {
				IESPEHandler handler = tile.getCapability(CapabilityESPEHandler.ESPE_HANDLER_CAPABILITY, null);
				energy += handler.getESPE();
			}
		}

		return MathHelper.floor(energy);
	}

	public boolean action(EntityPlayer player) {
		if(!player.getHeldItemMainhand().isEmpty()) {
			ItemStack item = player.getHeldItemMainhand();
			WindImbueRecipe rec = WindImbueRecipe.findRecipeByComponent(item);
			if(rec != null) {
				int energy = getEnderstarEnergy();
				boolean hasEnergy = energy >= rec.enderEnergy;
				boolean creative = player.capabilities.isCreativeMode;
				if(hasEnergy || creative) {
					double cenergy = 0;

					for(int i = 0; i < 8; ++i) {
						TileEntity tile = getWorld().getTileEntity(pos.add(coords[i]));
						if(tile == null) {
							continue;
						}
						if(tile.hasCapability(CapabilityESPEHandler.ESPE_HANDLER_CAPABILITY, null)) {
							IESPEHandler handler = tile.getCapability(CapabilityESPEHandler.ESPE_HANDLER_CAPABILITY, null);
							double req = rec.enderEnergy - cenergy;
							double extracted = handler.extractESPE(req, !creative);
							cenergy += extracted;

							if(cenergy >= rec.enderEnergy || creative) {
								if(rec.transforming.getItem() instanceof ItemSoulStone && !ECUtils.getData(player).isWindbound()) {
									ECUtils.getData(player).modifyWindbound(true);
									if(!player.world.isRemote) {
										player.sendMessage(new TextComponentTranslation("essentialcraft.txt.windImbue").setStyle(new Style().setColor(TextFormatting.AQUA)));
										ECUtils.requestSync(player);
									}
									if(player.world.isRemote)
										for(int j = 0; j < 300; ++j)
											EssentialCraftCore.proxy.SmokeFX(pos.getX()+0.5D + MathUtils.randomDouble(getWorld().rand)*1.6D, pos.getY(), pos.getZ()+0.5D + MathUtils.randomDouble(getWorld().rand)*1.6D, 0, getWorld().rand.nextDouble()*0.3D, 0, 1, 0.7D, 1.0D, 0.85D);

									return true;
								}

								player.inventory.decrStackSize(player.inventory.currentItem, 1);
								if(player.inventory.getStackInSlot(player.inventory.currentItem).isEmpty())
									player.inventory.setInventorySlotContents(player.inventory.currentItem, rec.result.copy());
								else if(!player.inventory.addItemStackToInventory(rec.result.copy()))
									player.dropItem(rec.result.copy(), true);

								if(player.world.isRemote)
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
		if(sCheckTick == 0) {
			checkStructureAndTier();
			sCheckTick = 40;
		}
		else {
			--sCheckTick;
		}

		if(getWorld().isRemote && tier >= 0) {
			int movement = (int)(getWorld().getWorldTime() % 60);
			if(movement > 30)
				movement = 60 - movement;
			EssentialCraftCore.proxy.SmokeFX(pos.getX()+0.5D + MathUtils.randomDouble(getWorld().rand)*1.6D, pos.getY(), pos.getZ()+0.5D + MathUtils.randomDouble(getWorld().rand)*1.6D, 0, getWorld().rand.nextDouble()*0.3D, 0, 1, 0.2D, 1.0D, 0.45D);

			for(int i = 0; i < 8; ++i) {
				getWorld().spawnParticle(EnumParticleTypes.REDSTONE, pos.getX()+coords[i].getX()+0.5D, pos.getY()+coords[i].getY()+movement/30D, pos.getZ()+coords[i].getZ()+0.5D, -1, 1, 0);
				getWorld().spawnParticle(EnumParticleTypes.REDSTONE, pos.getX()+coords[i].getX()+0.5D, pos.getY()+coords[i].getY()+2+movement/30D, pos.getZ()+coords[i].getZ()+0.5D, -1, 1, 0);
			}
		}
	}

	protected static boolean testBlock(IBlockAccess world, BlockPos pos, Block block) {
		return world.getBlockState(pos).getBlock() == block;
	}

	protected static boolean testESPEHandler(IBlockAccess world, BlockPos pos, int minTier) {
		TileEntity tile = world.getTileEntity(pos);
		return tile != null &&
				tile.hasCapability(CapabilityESPEHandler.ESPE_HANDLER_CAPABILITY, null) &&
				tile.getCapability(CapabilityESPEHandler.ESPE_HANDLER_CAPABILITY, null).getTier() >= minTier;
	}

	protected static BiPredicate<IBlockAccess, BlockPos> tier0Checker = (w, cp)->
	testBlock(w, cp.add( 0, 0, 0), BlocksCore.invertedBlock) &&
	testBlock(w, cp.add( 1, 0, 0), BlocksCore.invertedBlock) &&
	testBlock(w, cp.add(-1, 0, 0), BlocksCore.invertedBlock) &&
	testBlock(w, cp.add( 0, 0, 1), BlocksCore.invertedBlock) &&
	testBlock(w, cp.add( 0, 0,-1), BlocksCore.invertedBlock) &&
	testBlock(w, cp.add( 1, 1,-1), BlocksCore.invertedBlock) &&
	testBlock(w, cp.add( 1, 1, 1), BlocksCore.invertedBlock) &&
	testBlock(w, cp.add(-1, 1,-1), BlocksCore.invertedBlock) &&
	testBlock(w, cp.add(-1, 1, 1), BlocksCore.invertedBlock) &&
	testBlock(w, cp.add( 1, 2,-1), BlocksCore.invertedBlock) &&
	testBlock(w, cp.add( 1, 2, 1), BlocksCore.invertedBlock) &&
	testBlock(w, cp.add(-1, 2,-1), BlocksCore.invertedBlock) &&
	testBlock(w, cp.add(-1, 2, 1), BlocksCore.invertedBlock) &&
	testBlock(w, cp.add( 1, 0,-1), BlocksCore.platingPale) &&
	testBlock(w, cp.add( 2, 0,-1), BlocksCore.platingPale) &&
	testBlock(w, cp.add(-2, 0,-1), BlocksCore.platingPale) &&
	testBlock(w, cp.add( 2, 0, 1), BlocksCore.platingPale) &&
	testBlock(w, cp.add(-2, 0, 1), BlocksCore.platingPale) &&
	testBlock(w, cp.add( 1, 0,-2), BlocksCore.platingPale) &&
	testBlock(w, cp.add(-1, 0,-2), BlocksCore.platingPale) &&
	testBlock(w, cp.add( 1, 0, 2), BlocksCore.platingPale) &&
	testBlock(w, cp.add(-1, 0, 2), BlocksCore.platingPale) &&
	testBlock(w, cp.add(-2, 0, 0), BlocksCore.magicPlating) &&
	testBlock(w, cp.add(-2, 1, 0), BlocksCore.magicPlating) &&
	testBlock(w, cp.add( 2, 0, 0), BlocksCore.magicPlating) &&
	testBlock(w, cp.add( 2, 1, 0), BlocksCore.magicPlating) &&
	testBlock(w, cp.add( 0, 0,-2), BlocksCore.magicPlating) &&
	testBlock(w, cp.add( 0, 1,-2), BlocksCore.magicPlating) &&
	testBlock(w, cp.add( 0, 0, 2), BlocksCore.magicPlating) &&
	testBlock(w, cp.add( 0, 1, 2), BlocksCore.magicPlating) &&
	testBlock(w, cp.add( 1, 0, 1), BlocksCore.platingPale) &&
	testBlock(w, cp.add(-1, 0,-1), BlocksCore.platingPale) &&
	testBlock(w, cp.add(-1, 0, 1), BlocksCore.platingPale) &&
	testBlock(w, cp.add( 1, 0,-1), BlocksCore.platingPale) &&
	testESPEHandler(w, cp.add(-1, 3, 1), 0) &&
	testESPEHandler(w, cp.add( 1, 3, 1), 0) &&
	testESPEHandler(w, cp.add(-1, 3,-1), 0) &&
	testESPEHandler(w, cp.add( 1, 3,-1), 0) &&
	testESPEHandler(w, cp.add(-2, 1, 2), 0) &&
	testESPEHandler(w, cp.add( 2, 1, 2), 0) &&
	testESPEHandler(w, cp.add(-2, 1,-2), 0) &&
	testESPEHandler(w, cp.add( 2, 1,-2), 0);

	public void checkStructureAndTier() {
		World w = getWorld();
		BlockPos cp = pos.down();
		tier = -1;
		if(tier0Checker.test(w, cp)) {
			tier = 0;
		}
	}
	
	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return new AxisAlignedBB(pos.add(-1, 0, -1), pos.add(2, 1, 2));
	}
}
