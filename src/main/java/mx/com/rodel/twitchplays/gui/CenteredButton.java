package mx.com.rodel.twitchplays.gui;

import net.minecraft.client.gui.GuiButton;

public class CenteredButton extends GuiButton{

	public CenteredButton(int buttonId, int x, int y, String buttonText) {
		this(buttonId, x, y, 200, 20, buttonText);
	}
	
	public CenteredButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
		super(buttonId, x-widthIn/2, y-heightIn/2, widthIn, heightIn, buttonText);
	}

}
