package mx.com.rodel.twitchplays.bot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.jibble.pircbot.PircBot;

import mx.com.rodel.twitchplays.TwitchPlays;
import mx.com.rodel.twitchplays.actions.ActionExecutor;
import mx.com.rodel.twitchplays.actions.Actions;
import mx.com.rodel.twitchplays.actions.State;
import mx.com.rodel.twitchplays.events.OverlayHandler;
import mx.com.rodel.twitchplays.utils.Helper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.client.FMLClientHandler;

public class TwitchBot extends PircBot{
	
	public static boolean isConnected = false;
	public static TwitchBot instance = null;

	public String channel = "";
	public State state = State.IDLE;
//	public Scheduler currentTimer;
	public List<String> voters = new ArrayList<String>();
	
	public void changeState(State state){
		this.state = state;
		switch(state){
			case IDLE:
				OverlayHandler.state = 0;
				break;
			case SELECTING:
				EntityPlayer player = FMLClientHandler.instance().getClientPlayerEntity();
				voters.clear();
				OverlayHandler.createActions();
				OverlayHandler.state = 1;
				if(ActionExecutor.currentExecutor != null){
					ActionExecutor.currentExecutor.onEnd(player, Helper.findWorld(player));
					ActionExecutor.currentExecutor = null;
					ActionExecutor.currentAction = null;
				}

				TwitchPlays.setTask(1000*35, new Runnable() {
					public void run() {
						Actions bestAction = null;
						int bestVotes = -1;
						for(Entry<Actions, Integer> vote : OverlayHandler.actions.values()){
							if(vote.getValue() > bestVotes){
								bestAction = vote.getKey();
								bestVotes = vote.getValue();
							}
						}
						
						ActionExecutor.executeAction(bestAction);
						
						changeState(State.PRE_SELECTING);
					}
				});
				break;
			case PRE_SELECTING:
				OverlayHandler.state = -1;
				TwitchPlays.setTask(1000*40, new Runnable() {
					public void run() {
						changeState(State.SELECTING);
					}
				});
				break;
			default:
				break;
		}
	}
	
	public TwitchBot() throws Exception {
		if(instance==null){
			instance = this;
		}else{
			throw new Exception("Sorry but you cannot create other instance of TwitchBot use TwitchBot.instance instead");
		}
	}
	
	@Override
	protected void onJoin(String channel, String sender, String login, String hostname) {
		this.channel = channel;
		Helper.sendMessage("Connected to "+channel+" channel!");
		isConnected = true;
		changeState(State.PRE_SELECTING);
	}
	
	@Override
	protected void onMessage(String channel, String sender, String login, String hostname, String message) {
		if(!voters.contains(sender) && state == State.SELECTING){
			if(message.equalsIgnoreCase("!1")){
				OverlayHandler.vote(0);
			}else if(message.equalsIgnoreCase("!2")){
				OverlayHandler.vote(1);
			}else if(message.equalsIgnoreCase("!3")){
				OverlayHandler.vote(2);
			}
			voters.add(sender);
		}
	}
	
	public String getChannel(){
		return channel.replace("#", "");
	}
	
	
	@Override
	protected void onConnect() {
		System.out.println("Connected to twitch irc server");
	}
	
	@Override
	protected void onDisconnect() {
		Helper.sendMessage("Disconnected from "+channel+" channel!");
		channel = "";
		isConnected = false;
		System.out.println("Disconnected from the channel");
		TwitchPlays.removeAllTasks();
		changeState(State.IDLE);
		OverlayHandler.state = -1;
	}
}
