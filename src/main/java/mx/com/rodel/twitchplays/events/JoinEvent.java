package mx.com.rodel.twitchplays.events;

import mx.com.rodel.twitchplays.utils.Helper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class JoinEvent {
	@SubscribeEvent
	public void playerJoin(PlayerEvent.PlayerLoggedInEvent e){
		String update = Helper.update();
		System.out.println(update != null ? update+" ready..." : "Mod up-to-date");
		if(update != null){
			Helper.sendMessage("TwitchPlays "+update+" is available, get it on MinecraftForums...");
		}
	}
}
