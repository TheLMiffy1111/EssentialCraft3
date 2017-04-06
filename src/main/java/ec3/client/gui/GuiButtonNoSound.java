package ec3.client.gui;

import DummyCore.Utils.MathUtils;
import ec3.common.registry.SoundRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.GuiButton;

public class GuiButtonNoSound extends GuiButton {

	public GuiButtonNoSound(int p_i1021_1_, int p_i1021_2_, int p_i1021_3_, int p_i1021_4_, int p_i1021_5_, String p_i1021_6_) {
		super(p_i1021_1_, p_i1021_2_, p_i1021_3_, p_i1021_4_, p_i1021_5_, p_i1021_6_);
	}

	@Override
    public void playPressSound(SoundHandler p_146113_1_)
    {
		p_146113_1_.playSound(PositionedSoundRecord.getMasterRecord(SoundRegistry.bookPageTurn, 1.0F));
    }
}
