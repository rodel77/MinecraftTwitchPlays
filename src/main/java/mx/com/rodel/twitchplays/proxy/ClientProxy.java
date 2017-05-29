package mx.com.rodel.twitchplays.proxy;

import mx.com.rodel.twitchplays.events.JoinEvent;
import mx.com.rodel.twitchplays.events.KeyHandler;
import mx.com.rodel.twitchplays.events.OverlayHandler;
import mx.com.rodel.twitchplays.events.TickHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

public class ClientProxy extends CommonProxy{
	@Override
	public void init(FMLInitializationEvent e) {
		MinecraftForge.EVENT_BUS.register(new KeyHandler());
		MinecraftForge.EVENT_BUS.register(new OverlayHandler());
		MinecraftForge.EVENT_BUS.register(new TickHandler());
		MinecraftForge.EVENT_BUS.register(new JoinEvent());
	}
}
