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
		List<Scheduler> scheluder = new ArrayList<Scheduler>();
		if((scheluder = TwitchPlays.timerManager).size()!=0){
			for (int i = 0; i < scheluder.size(); i++) {
				Scheduler sc = scheluder.get(i);
				if(sc.canExecute()){
					try {
						sc.runnable.run();
					} catch (Exception e2) {
						e2.printStackTrace();
					}
					TwitchPlays.timerManager.remove(i);
				}
			}
		}
	}
}
