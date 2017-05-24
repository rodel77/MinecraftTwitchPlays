package mx.com.rodel.twitchplays;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mx.com.rodel.twitchplays.actions.ActionExecutor;
import mx.com.rodel.twitchplays.bot.TwitchBot;
import mx.com.rodel.twitchplays.proxy.CommonProxy;
import mx.com.rodel.twitchplays.utils.Scheduler;
import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = TwitchPlays.MODID, version = TwitchPlays.VERSION, clientSideOnly=true)
public class TwitchPlays{
    public static final String MODID = "twitchplays";
    public static final String VERSION = "1.0";

    public static List<Scheduler> timerManager = new ArrayList<Scheduler>();
    
    @SidedProxy(clientSide="mx.com.rodel.twitchplays.proxy.ClientProxy", serverSide="mx.com.rodel.twitchplays.proxy.SeverProxy")
    public static CommonProxy proxy;
    
    
    public static Scheduler createTask(long delay, Runnable runnable){
    	Scheduler ret;
    	timerManager.add(ret = new Scheduler(System.currentTimeMillis(), delay, runnable));
    	System.out.println("Timer created for "+delay+" milliseconds...");
    	return ret;
    }
    
    public static void removeAllTasks(){
    	timerManager.clear();
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
