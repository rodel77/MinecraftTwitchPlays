package mx.com.rodel.twitchplays.actions;

import java.util.HashMap;

import com.mojang.realmsclient.gui.ChatFormatting;

import mx.com.rodel.twitchplays.utils.Helper;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class ActionExecutor {

	public static HashMap<Actions, Executor> executors = new HashMap<Actions, ActionExecutor.Executor>();
	
	public static void registerEvents(){
		executors.clear();
		if(executors.isEmpty()){
			executor(Actions.POISON, new Executor() {
				@Override
				public void execute(EntityPlayer player) {
					findWorld(player).getPlayerEntityByUUID(player.getUniqueID()).addPotionEffect(new PotionEffect(MobEffects.POISON, 20*15, 10, false, false));
				}
			});
			
			executor(Actions.THUNDER, new Executor() {
				@Override
				public void execute(EntityPlayer player) {
					findWorld(player).getWorldInfo().setRaining(true);
				}
			});
			
			executor(Actions.WHITER, new Executor(){
				@Override
				public void execute(EntityPlayer player) {
					World world = findWorld(player);
					Entity sentity = EntityList.createEntityByID(64, world);
					sentity.setCustomNameTag("§9Twitchither");
					sentity.setLocationAndAngles(player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ(), world.rand.nextFloat() * 360F, 0.0f);
					world.spawnEntity(sentity);
					((EntityLiving) sentity).playLivingSound();
				}
			});
			
			executor(Actions.DIAMOND, new Executor(){
				@Override
				public void execute(EntityPlayer player) {
					World world = findWorld(player);
					EntityItem diamond = new EntityItem(world, player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ(), new ItemStack(Items.DIAMOND, 1, 0));
					diamond.setPickupDelay(10);
					world.spawnEntity(diamond);
				}
			});
		}
	}
	
	private static World findWorld(EntityPlayer player){
		for(WorldServer world : FMLCommonHandler.instance().getMinecraftServerInstance().worlds){
			if(world.provider.getDimension()==player.world.provider.getDimension() && !world.isRemote){
				return world;
			}
		}
		return null;
	}
	
	private static void executor(Actions action, Executor executor){
		System.out.println("Registring executor for "+action.toString());
		executors.put(action, executor);
	}
	
	public static void executeAction(Actions action){
		EntityPlayer player = FMLClientHandler.instance().getClientPlayerEntity();
		findWorld(player).playSound(player, player.getPosition(), SoundEvents.ENTITY_ARROW_HIT_PLAYER, SoundCategory.BLOCKS, 1f, 1f);
		Helper.sendMessage("&aYou followers select &6"+action.name);
		registerEvents();
		executors
		.get(action)
		.execute(FMLClientHandler.instance().getClientPlayerEntity());
		
	}
	
	public interface Executor{
		public void execute(EntityPlayer player);
	}
}
