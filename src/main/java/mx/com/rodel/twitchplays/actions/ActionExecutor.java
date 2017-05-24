package mx.com.rodel.twitchplays.actions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.client.FMLClientHandler;

public class ActionExecutor {

	public static HashMap<Actions, Executor> executors = new HashMap<Actions, ActionExecutor.Executor>();
	
	public static void registerEvents(){
		if(executors.isEmpty()){
			executor(Actions.POISON, new Executor() {
				@Override
				public void execute(EntityPlayerSP player) {
					System.out.println("asd");
					player.addPotionEffect(new PotionEffect(Potion.getPotionById(19), 20*5));
				}
			});
			
			executor(Actions.THUNDER, new Executor() {
				@Override
				public void execute(EntityPlayerSP player) {
					player.world.setThunderStrength(1F);
				}
			});
			
			executor(Actions.WHITER, new Executor(){
				@Override
				public void execute(EntityPlayerSP player) {
					EntityWither wither = new EntityWither(player.world);
					wither.setPosition(player.posX, player.posY, player.posZ);
					player.world.spawnEntity(wither);
				}
			});
		}
	}
	
	private static void executor(Actions action, Executor executor){
		System.out.println("Registring executor for "+action.toString());
		executors.put(action, executor);
	}
	
	public static void executeAction(Actions action){
		executors
		.get(action)
		.execute(FMLClientHandler
				.instance()
				.getClient()
				.player);
	}
	
	public interface Executor{
		public void execute(EntityPlayerSP player);
	}
}
