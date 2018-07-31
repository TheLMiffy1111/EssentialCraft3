package essentialcraft.client.gui.element;

import DummyCore.Utils.DrawUtils;
import DummyCore.Utils.MathUtils;
import essentialcraft.api.IHotBlock;
import essentialcraft.common.tile.TileColdDistillator;
import essentialcraft.common.tile.TileEnderGenerator;
import essentialcraft.common.tile.TileFlowerBurner;
import essentialcraft.common.tile.TileHeatGenerator;
import essentialcraft.common.tile.TileMatrixAbsorber;
import essentialcraft.common.tile.TileMoonWell;
import essentialcraft.common.tile.TileSunRayAbsorber;
import essentialcraft.common.tile.TileUltraFlowerBurner;
import essentialcraft.common.tile.TileUltraHeatGenerator;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

public class GuiMRUGenerated extends GuiTextElement{

	public TileEntity tile;
	public String tileValue;

	public GuiMRUGenerated(int i, int j, TileEntity t, String tileType) {
		super(i,j);
		tile = t;
		tileValue = tileType;
	}

	@Override
	public void drawText(int posX, int posY) {
		if(tileValue.equals("matrixAbsorber")) {
			Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(TileMatrixAbsorber.mruUsage+"UBMRU > "+TileMatrixAbsorber.mruGenerated+"MRU", posX+2, posY+5, 0xffffff);
		}
		if(tileValue.equals("heatGenerator")) {
			if(tile instanceof TileHeatGenerator) {
				TileHeatGenerator furnace = (TileHeatGenerator) tile;
				DrawUtils.bindTexture("minecraft", "textures/gui/container/furnace.png");
				this.drawTexturedModalRect(posX+100, posY+2, 55, 36, 15, 15);
				if(furnace.currentBurnTime > 0) {
					int scaledSize = MathUtils.pixelatedTextureSize(furnace.currentBurnTime, furnace.currentMaxBurnTime, 14)+1;
					this.drawTexturedModalRect(posX+101, posY+2+15-scaledSize, 176, 15-scaledSize, 15, scaledSize);
				}
				float mruGenerated = TileHeatGenerator.mruGenerated;
				float mruFactor = 1.0F;
				Block[] b = new Block[4];
				b[0] = furnace.getWorld().getBlockState(furnace.getPos().add(2, 0, 0)).getBlock();
				b[1] = furnace.getWorld().getBlockState(furnace.getPos().add(-2, 0, 0)).getBlock();
				b[2] = furnace.getWorld().getBlockState(furnace.getPos().add(0, 0, 2)).getBlock();
				b[3] = furnace.getWorld().getBlockState(furnace.getPos().add(0, 0, -2)).getBlock();
				int[] ox = {2,-2,0,0};
				int[] oz = {0,0,2,-2};
				for(int i = 0; i < 4; ++i) {
					if(b[i] == Blocks.AIR) {
						mruFactor*=0;
					}
					else if(b[i] == Blocks.NETHERRACK) {
						mruFactor*=0.75F;
					}
					else if(b[i] == Blocks.LAVA) {
						mruFactor*=0.95F;
					}
					else if(b[i] == Blocks.FIRE) {
						mruFactor*=0.7F;
					}
					else if(b[i] instanceof IHotBlock) {
						mruFactor*=((IHotBlock)b[i]).getHeatModifier(tile.getWorld(), tile.getPos().add(ox[i], 0, oz[i]));
					}
					else {
						mruFactor*=0.5F;
					}
				}
				mruGenerated*=mruFactor;
				Minecraft.getMinecraft().fontRenderer.drawStringWithShadow((int)mruGenerated+" MRU/t", posX+2, posY+5, 0xffffff);
			}
		}
		if(tileValue.equals("ultraHeatGenerator")) {
			if(tile instanceof TileUltraHeatGenerator) {
				TileUltraHeatGenerator furnace = (TileUltraHeatGenerator) tile;
				DrawUtils.bindTexture("minecraft", "textures/gui/container/furnace.png");
				this.drawTexturedModalRect(posX+100, posY+2, 55, 36, 15, 15);
				if(furnace.currentBurnTime > 0) {
					int scaledSize = MathUtils.pixelatedTextureSize(furnace.currentBurnTime, furnace.currentMaxBurnTime, 14)+1;
					this.drawTexturedModalRect(posX+101, posY+2+15-scaledSize, 176, 15-scaledSize, 15, scaledSize);
				}
				Block[] b = new Block[4];
				b[0] = furnace.getWorld().getBlockState(furnace.getPos().add(2, 0, 0)).getBlock();
				b[1] = furnace.getWorld().getBlockState(furnace.getPos().add(-2, 0, 0)).getBlock();
				b[2] = furnace.getWorld().getBlockState(furnace.getPos().add(0, 0, 2)).getBlock();
				b[3] = furnace.getWorld().getBlockState(furnace.getPos().add(0, 0, -2)).getBlock();
				for(int i = 0; i < 4; ++i) {
					if(b[i] == Blocks.AIR) {}
					else if(b[i] == Blocks.NETHERRACK) {}
					else if(b[i] == Blocks.LAVA) {}
					else if(b[i] == Blocks.FIRE) {}
					else if(b[i] instanceof IHotBlock) {}
					else {}
				}
				double heat = furnace.heat;
				double mruGenerated;
				if(heat < 1000)
					mruGenerated = heat/100;
				else if(heat > 10000)
					mruGenerated = 80+heat/1000;
				else
					mruGenerated = heat/124;
				Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(mruGenerated+" MRU/t", posX+2, posY+5, 0xffffff);
				Minecraft.getMinecraft().fontRenderer.drawStringWithShadow("Heat: "+(int)furnace.heat+"C", posX+82, posY-10, 0xffffff);
			}
		}
		if(tileValue.equals("naturalFurnace")) {
			if(tile instanceof TileFlowerBurner) {
				TileFlowerBurner furnace = (TileFlowerBurner) tile;
				Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(TileFlowerBurner.mruGenerated+" MRU/t", posX+2, posY+5, 0xffffff);
				DrawUtils.bindTexture("essentialcraft", "textures/gui/slot_common.png");
				this.drawTexturedModalRect(posX+82, posY, 0, 0, 18, 18);
				RenderItem renderitem = Minecraft.getMinecraft().getRenderItem();
				if(furnace.burnedFlower != null) {
					BlockPos pos = new BlockPos(furnace.burnedFlower.getX(),furnace.burnedFlower.getY(), furnace.burnedFlower.getZ());
					IBlockState b = furnace.getWorld().getBlockState(pos);
					if(b != null && b.getBlock() != Blocks.AIR && Item.getItemFromBlock(b.getBlock()) != null) {
						renderitem.renderItemIntoGUI(b.getBlock().getPickBlock(b, new RayTraceResult(Vec3d.ZERO, EnumFacing.DOWN, pos), furnace.getWorld(), furnace.burnedFlower, Minecraft.getMinecraft().player), posX+83, posY+1);
					}
				}
				DrawUtils.bindTexture("minecraft", "textures/gui/container/furnace.png");
				this.drawTexturedModalRect(posX+82, posY-15, 55, 36, 15, 15);
				if(furnace.burnTime > 0) {
					int scaledSize = MathUtils.pixelatedTextureSize(furnace.burnTime, 30*20, 14)+1;
					this.drawTexturedModalRect(posX+83, posY-scaledSize, 176, 15-scaledSize, 15, scaledSize);
				}
			}
		}
		if(tileValue.equals("ultraNaturalFurnace")) {
			if(tile instanceof TileUltraFlowerBurner) {
				TileUltraFlowerBurner furnace = (TileUltraFlowerBurner) tile;
				Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(furnace.mruProduced+" MRU/t", posX+2, posY+5, 0xffffff);
				DrawUtils.bindTexture("essentialcraft", "textures/gui/slot_common.png");
				this.drawTexturedModalRect(posX+82, posY, 0, 0, 18, 18);
				RenderItem renderitem = Minecraft.getMinecraft().getRenderItem();
				if(furnace.burnedFlower != null) {
					BlockPos pos = furnace.burnedFlower;
					IBlockState b = furnace.getWorld().getBlockState(pos);
					if(b != null && Item.getItemFromBlock(b.getBlock()) != null) {
						renderitem.renderItemIntoGUI(b.getBlock().getPickBlock(b, new RayTraceResult(Vec3d.ZERO, EnumFacing.DOWN, pos), furnace.getWorld(), furnace.burnedFlower, Minecraft.getMinecraft().player), posX+83, posY+1);
					}
				}
				DrawUtils.bindTexture("minecraft", "textures/gui/container/furnace.png");
				this.drawTexturedModalRect(posX+82, posY-15, 55, 36, 15, 15);
				if(furnace.burnTime > 0) {
					int scaledSize = MathUtils.pixelatedTextureSize(furnace.burnTime, 30*20, 14)+1;
					this.drawTexturedModalRect(posX+83, posY-scaledSize, 176, 15-scaledSize, 15, scaledSize);
				}
			}
		}
		if(tileValue.equals("cold")) {
			if(tile instanceof TileColdDistillator) {
				TileColdDistillator cold = (TileColdDistillator) tile;
				Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(cold.getMRU()+" MRU/t", posX+2, posY+5, 0xffffff);
			}
		}
		if(tileValue.equals("enderGenerator")) {
			Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(TileEnderGenerator.mruGenerated+" MRU/hit", posX+2, posY+5, 0xffffff);
		}
		if(tileValue.equals("sunray")) {
			Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(TileSunRayAbsorber.mruGenerated+" MRU/t", posX+2, posY+5, 0xffffff);
		}
		if(tileValue.equals("moonwell")) {
			double mruGenerated = TileMoonWell.mruGenerated;
			int moonPhase = tile.getWorld().provider.getMoonPhase(tile.getWorld().getWorldTime());
			double moonFactor = Math.abs(1D - moonPhase*0.25D);
			mruGenerated *= moonFactor;
			double heightFactor = 1.0F;
			if(tile.getPos().getY() > TileMoonWell.maxHeight) {
				heightFactor = 0D;
			}
			else {
				heightFactor = 1D - tile.getPos().getY()/TileMoonWell.maxHeight;
				mruGenerated *= heightFactor;
			}
			Minecraft.getMinecraft().fontRenderer.drawStringWithShadow((int)mruGenerated+" MRU/t", posX+2, posY+5, 0xffffff);
		}
	}

}
