package essentialcraft.client.gui.element;

import DummyCore.Utils.MathUtils;
import essentialcraft.common.capabilities.mru.CapabilityMRUHandler;
import essentialcraft.common.item.ItemBoundGem;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

public class GuiBoundGemState extends GuiTextElement {

	public TileEntity tile;
	public int slotNum;

	public GuiBoundGemState(int i, int j, TileEntity t, int slot) {
		super(i,j);
		tile = t;
		slotNum = slot;
	}

	@Override
	public ResourceLocation getElementTexture() {
		return super.getElementTexture();
	}

	@Override
	public void draw(int posX, int posY, int mouseX, int mouseY) {
		super.draw(posX, posY, mouseX, mouseY);
	}

	@Override
	public int getX() {
		return super.getX();
	}

	@Override
	public int getY() {
		return super.getY();
	}

	@Override
	public void drawText(int posX, int posY) {
		IInventory inventory = (IInventory) tile;
		if(!(inventory.getStackInSlot(slotNum).getItem() instanceof ItemBoundGem)) {
			Minecraft.getMinecraft().fontRenderer.drawString("No Bound Gem!", posX+6, posY+5, 0xff0000, true);
		}
		else if(inventory.getStackInSlot(slotNum).getTagCompound() == null) {
			Minecraft.getMinecraft().fontRenderer.drawString("Gem Not Bound!", posX+4, posY+5, 0xff0000, true);
		}
		else {
			int o[] = ItemBoundGem.getCoords(inventory.getStackInSlot(slotNum));
			BlockPos pos = new BlockPos(o[0], o[1], o[2]);
			if(this.tile.getWorld().getTileEntity(pos) == null) {
				Minecraft.getMinecraft().fontRenderer.drawString("No Tile At Pos!", posX+5, posY+5, 0xff0000, true);
			}
			else if(!this.tile.getWorld().getTileEntity(pos).hasCapability(CapabilityMRUHandler.MRU_HANDLER_CAPABILITY, null)) {
				Minecraft.getMinecraft().fontRenderer.drawString("Not Magical!", posX+12, posY+5, 0xff0000, true);
			}
			else if(MathUtils.getDifference(tile.getPos().getX(), o[0]) > 16 || MathUtils.getDifference(tile.getPos().getY(), o[1]) > 16 || MathUtils.getDifference(tile.getPos().getZ(), o[2]) > 16) {
				Minecraft.getMinecraft().fontRenderer.drawString("Not In Range!", posX+8, posY+5, 0xff0000, true);
			}
			else if(tile.getPos().equals(pos)) {
				Minecraft.getMinecraft().fontRenderer.drawString("Bound To Self!", posX+5, posY+5, 0xff0000, true);
			}
			else if(tile.getWorld().getTileEntity(pos).getCapability(CapabilityMRUHandler.MRU_HANDLER_CAPABILITY, null).getMRU() <= 0) {
				Minecraft.getMinecraft().fontRenderer.drawString("No MRU In Tile!", posX+6, posY+5, 0xffff00, true);
			}
			else {
				Minecraft.getMinecraft().fontRenderer.drawString("Working", posX+22, posY+5, 0x00ff00, true);
			}
		}
	}
}
