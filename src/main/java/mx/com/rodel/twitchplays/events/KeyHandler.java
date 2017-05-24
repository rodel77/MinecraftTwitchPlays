package mx.com.rodel.twitchplays.events;

import org.lwjgl.input.Keyboard;

import mx.com.rodel.twitchplays.gui.ConnectGUI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

public class KeyHandler {
	KeyBinding b;
	public KeyHandler() {
		ClientRegistry.registerKeyBinding(b = new KeyBinding("twitchplays.key.open.description", Keyboard.KEY_P, "twitchplays.key.category"));
	}
	
	@SubscribeEvent
	public void keyHandler(InputEvent.KeyInputEvent e){
		if(b.isPressed()){
			FMLClientHandler.instance().displayGuiScreen(FMLClientHandler.instance().getClient().player, new ConnectGUI());
		}
	}
}
