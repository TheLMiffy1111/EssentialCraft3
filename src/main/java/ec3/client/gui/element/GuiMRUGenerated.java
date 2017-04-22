package ec3.client.gui.element;

import DummyCore.Utils.DrawUtils;
import DummyCore.Utils.MathUtils;
import ec3.api.IHotBlock;
import ec3.common.tile.TileColdDistillator;
import ec3.common.tile.TileEnderGenerator;
import ec3.common.tile.TileFlowerBurner;
import ec3.common.tile.TileHeatGenerator;
import ec3.common.tile.TileMatrixAbsorber;
import ec3.common.tile.TileMoonWell;
import ec3.common.tile.TileSunRayAbsorber;
import ec3.common.tile.TileUltraFlowerBurner;
import ec3.common.tile.TileUltraHeatGenerator;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

public class GuiMRUGenerated extends GuiTextField{

	public TileEntity tile;
	public String tileValue;

	public GuiMRUGenerated(int i, int j, TileEntity t, String tileType)
	{
		super(i,j);
		tile = t;
		tileValue = tileType;
	}

	@Override
	public void drawText(int posX, int posY) {
		if(tileValue.equals("matrixAbsorber")) {
			Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(TileMatrixAbsorber.mruUsage+"UBMRU > "+TileMatrixAbsorber.mruGenerated+"MRU", posX+2, posY+5, 0xffffff);
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
				int[] ox = new int[]{2,-2,0,0};
				int[] oz = new int[]{0,0,2,-2};
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
						mruFactor*=(((IHotBlock)b[i]).getHeatModifier(tile.getWorld(), tile.getPos().add(ox[i], 0, oz[i])));
					}
					else {
						mruFactor*=0.5F;
					}
				}
				mruGenerated*=mruFactor;
				Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow((int)mruGenerated+" MRU/t", posX+2, posY+5, 0xffffff);
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
				float mruGenerated = 20;
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
				float heat = furnace.heat;
				if(heat < 1000)
					mruGenerated = heat/100;
				else
					if(heat > 10000)
						mruGenerated = 80+heat/1000;
					else
						mruGenerated = heat/124;
				Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow((int)mruGenerated+" MRU/t", posX+2, posY+5, 0xffffff);
				Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow("Heat: "+(int)furnace.heat+"C", posX+82, posY-10, 0xffffff);
			}
		}
		if(tileValue.equals("naturalFurnace"))
		{
			if(tile instanceof TileFlowerBurner)
			{
				TileFlowerBurner furnace = (TileFlowerBurner) tile;
				Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow((int)TileFlowerBurner.mruGenerated+" MRU/t", posX+2, posY+5, 0xffffff);
				DrawUtils.bindTexture("essentialcraft", "textures/gui/slot_common.png");
				this.drawTexturedModalRect(posX+82, posY, 0, 0, 18, 18);
				RenderItem renderitem = Minecraft.getMinecraft().getRenderItem();
				if(furnace.burnedFlower != null)
				{
					BlockPos pos = new BlockPos((int)furnace.burnedFlower.x,(int)furnace.burnedFlower.y, (int)furnace.burnedFlower.z);
					IBlockState b = furnace.getWorld().getBlockState(pos);
					if(b != null && Item.getItemFromBlock(b.getBlock()) != null)
					{
						renderitem.renderItemIntoGUI(b.getBlock().getPickBlock(b, new RayTraceResult(Vec3d.ZERO, EnumFacing.DOWN, pos), furnace.getWorld(), new BlockPos((int)furnace.burnedFlower.x,(int)furnace.burnedFlower.y, (int)furnace.burnedFlower.z), Minecraft.getMinecraft().player), posX+83, posY+1);
					}
				}
				DrawUtils.bindTexture("minecraft", "textures/gui/container/furnace.png");
				this.drawTexturedModalRect(posX+82, posY-15, 55, 36, 15, 15);
				if(furnace.burnTime > 0)
				{
					int scaledSize = MathUtils.pixelatedTextureSize(furnace.burnTime, 30*20, 14)+1;
					this.drawTexturedModalRect(posX+83, posY-scaledSize, 176, 15-scaledSize, 15, scaledSize);
				}
			}
		}
		if(tileValue.equals("ultraNaturalFurnace"))
		{
			if(tile instanceof TileUltraFlowerBurner)
			{
				TileUltraFlowerBurner furnace = (TileUltraFlowerBurner) tile;
				Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow((int)furnace.mruProduced+" MRU/t", posX+2, posY+5, 0xffffff);
				DrawUtils.bindTexture("essentialcraft", "textures/gui/slot_common.png");
				this.drawTexturedModalRect(posX+82, posY, 0, 0, 18, 18);
				RenderItem renderitem = Minecraft.getMinecraft().getRenderItem();
				if(furnace.burnedFlower != null)
				{
					BlockPos pos = new BlockPos((int)furnace.burnedFlower.x,(int)furnace.burnedFlower.y, (int)furnace.burnedFlower.z);
					IBlockState b = furnace.getWorld().getBlockState(pos);
					if(b != null && Item.getItemFromBlock(b.getBlock()) != null)
					{
						renderitem.renderItemIntoGUI(b.getBlock().getPickBlock(b, new RayTraceResult(Vec3d.ZERO, EnumFacing.DOWN, pos), furnace.getWorld(), new BlockPos((int)furnace.burnedFlower.x,(int)furnace.burnedFlower.y, (int)furnace.burnedFlower.z), Minecraft.getMinecraft().player), posX+83, posY+1);
					}
				}
				DrawUtils.bindTexture("minecraft", "textures/gui/container/furnace.png");
				this.drawTexturedModalRect(posX+82, posY-15, 55, 36, 15, 15);
				if(furnace.burnTime > 0)
				{
					int scaledSize = MathUtils.pixelatedTextureSize(furnace.burnTime, 30*20, 14)+1;
					this.drawTexturedModalRect(posX+83, posY-scaledSize, 176, 15-scaledSize, 15, scaledSize);
				}
			}
		}
		if(tileValue.equals("cold"))
		{
			if(tile instanceof TileColdDistillator)
			{
				TileColdDistillator cold = (TileColdDistillator) tile;
				Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow((int)cold.CgetMru()+" MRU/t", posX+2, posY+5, 0xffffff);
			}
		}
		if(tileValue.equals("enderGenerator"))
		{
			Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow((int)TileEnderGenerator.mruGenerated+" MRU/hit", posX+2, posY+5, 0xffffff);
		}
		if(tileValue.equals("sunray"))
		{
			Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow((int)TileSunRayAbsorber.mruGenerated+" MRU/t", posX+2, posY+5, 0xffffff);
		}
		if(tileValue.equals("moonwell"))
		{
			float mruGenerated = TileMoonWell.mruGenerated;
			int moonPhase = tile.getWorld().provider.getMoonPhase(tile.getWorld().getWorldTime());
			float moonFactor = 1.0F;
			switch(moonPhase)
			{
			case 0:
			{
				moonFactor = 1.0F;
				break;
			}
			case 1:
			{
				moonFactor = 0.75F;
				break;
			}
			case 7:
			{
				moonFactor = 0.75F;
				break;
			}
			case 2:
			{
				moonFactor = 0.5F;
				break;
			}
			case 6:
			{
				moonFactor = 0.5F;
				break;
			}
			case 3:
			{
				moonFactor = 0.25F;
				break;
			}
			case 5:
			{
				moonFactor = 0.25F;
				break;
			}
			case 4:
			{
				moonFactor = 0.0F;
				break;
			}
			}
			mruGenerated *= moonFactor;
			float heightFactor = 1.0F;
			if(tile.getPos().getY() > 80)
				heightFactor = 0F;
			else
			{
				heightFactor = 1.0F - (float)((float)tile.getPos().getY()/80F);
				mruGenerated *= heightFactor;
			}
			Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow((int)mruGenerated+" MRU/t", posX+2, posY+5, 0xffffff);
		}
	}

}
