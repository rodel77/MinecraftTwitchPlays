package mx.com.rodel.twitchplays;

import java.util.ArrayList;
import java.util.List;

import mx.com.rodel.twitchplays.actions.ActionExecutor;
import mx.com.rodel.twitchplays.bot.TwitchBot;
import mx.com.rodel.twitchplays.proxy.CommonProxy;
import mx.com.rodel.twitchplays.utils.Scheduler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = TwitchPlays.MODID, version = TwitchPlays.VERSION, clientSideOnly=true)
public class TwitchPlays{
    public static final String MODID = "twitchplays";
    public static final String VERSION = "0.0.1";

    public static List<Scheduler> timerManager = new ArrayList<Scheduler>();
    
    @SidedProxy(clientSide="mx.com.rodel.twitchplays.proxy.ClientProxy", serverSide="mx.com.rodel.twitchplays.proxy.SeverProxy")
    public static CommonProxy proxy;

    public static long timerStart = -1;
    public static long timerDelay = -1;
    public static Runnable currentRunnable = null;
    
    public static long getRemaining(){
    	return (timerStart + timerDelay) - System.currentTimeMillis();
    }
    
    public static void setTask(long delay, Runnable runnable){
    	System.out.println("SETTING TIMER");
    	timerStart = System.currentTimeMillis();
    	timerDelay = delay;
    	currentRunnable = runnable;
    	System.out.println(timerStart+" "+timerDelay+" "+System.currentTimeMillis());
    }
    
    public static void removeAllTasks(){
    	currentRunnable = null;
    	timerStart = -1;
    	timerDelay = -1;
    }
    
    @EventHandler
    public void init(FMLInitializationEvent e){
    	ActionExecutor.registerEvents();
    	
    	try {
			new TwitchBot();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
    	
    	proxy.init(e);
    }
}
