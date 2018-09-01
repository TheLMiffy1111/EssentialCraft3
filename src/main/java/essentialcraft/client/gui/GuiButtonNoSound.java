package essentialcraft.client.gui;

import essentialcraft.common.registry.SoundRegistry;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.GuiButton;

public class GuiButtonNoSound extends GuiButton {

	public GuiButtonNoSound(int id, int x, int y, int width, int height, String buttonText) {
		super(id, x, y, width, height, buttonText);
	}

	@Override
	public void playPressSound(SoundHandler soundHandler) {
		soundHandler.playSound(PositionedSoundRecord.getMasterRecord(SoundRegistry.bookPageTurn, 1.0F));
	}
}
