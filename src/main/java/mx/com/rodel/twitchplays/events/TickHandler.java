package mx.com.rodel.twitchplays.events;

import java.util.ArrayList;
import java.util.List;

import mx.com.rodel.twitchplays.TwitchPlays;
import mx.com.rodel.twitchplays.utils.Scheduler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class TickHandler {
	@SubscribeEvent
	public void tick(TickEvent e){
		if(TwitchPlays.timerStart != -1 && TwitchPlays.getRemaining() < 0){
			TwitchPlays.timerStart = -1;
			TwitchPlays.timerDelay = -1;
			TwitchPlays.currentRunnable.run();
		}
	}
}
