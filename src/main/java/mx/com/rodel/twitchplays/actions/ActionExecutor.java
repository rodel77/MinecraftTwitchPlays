package mx.com.rodel.twitchplays.actions;

import java.util.HashMap;

import mx.com.rodel.twitchplays.utils.Helper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;

public class ActionExecutor {

	public static HashMap<Actions, Executor> executors = new HashMap<Actions, ActionExecutor.Executor>();
	
	public static Executor currentExecutor = null;
	public static Actions currentAction = null;
	
	public static void registerEvents(){
		executors.clear();
		if(executors.isEmpty()){
			executor(Actions.POISON, new Executor() {
				@Override
				public void onStart(EntityPlayer player, World world) {
					world.getPlayerEntityByUUID(player.getUniqueID()).addPotionEffect(new PotionEffect(MobEffects.POISON, 20*15, 10, false, false));
				}
				
				@Override
				public void onEnd(EntityPlayer player, World world) {
					EntityPlayer p = world.getPlayerEntityByUUID(player.getUniqueID());
					if(p.getActivePotionEffect(MobEffects.POISON) != null){
						p.removeActivePotionEffect(MobEffects.POISON);
					}
				}
			});
			
			executor(Actions.THUNDER, new Executor() {
				@Override
				public void onStart(EntityPlayer player, World world) {
					world.getWorldInfo().setRaining(true);
				}
				
				@Override
				public void onEnd(EntityPlayer player, World world){
					world.getWorldInfo().setRaining(false);
				}
			});
			
			executor(Actions.WHITER, new Executor(){
				Entity wither;
				
				@Override
				public void onStart(EntityPlayer player, World world) {
					Entity sentity = EntityList.createEntityByID(64, world);
					wither = sentity;
					sentity.setCustomNameTag("§9Twitchither");
					sentity.setLocationAndAngles(player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ(), world.rand.nextFloat() * 360F, 0.0f);
					world.spawnEntity(sentity);
				}
				
				@Override
				public void onEnd(EntityPlayer player, World world) {
					if(!wither.isDead){
						wither.setDead();
					}
				}
			});
			
			executor(Actions.DIAMOND, new Executor(){
				@Override
				public void onStart(EntityPlayer player, World world) {
					EntityItem diamond = new EntityItem(world, player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ(), new ItemStack(Items.DIAMOND, 1, 0).setStackDisplayName("§9Twichamond"));
					diamond.setPickupDelay(10);
					world.spawnEntity(diamond);
				}
				
				@Override
				public void onEnd(EntityPlayer player, World world) {}
			});
			
			executor(Actions.MOUSE, new Executor() {
				@Override
				public void onStart(EntityPlayer player, World world) {
					Minecraft.getMinecraft().gameSettings.invertMouse = !Minecraft.getMinecraft().gameSettings.invertMouse; 
				}
				
				@Override
				public void onEnd(EntityPlayer player, World world) {
					onStart(player, world);
				}
			});
			
			executor(Actions.CAMERA, new Executor() {
				@Override
				public void onStart(EntityPlayer player, World world) {
					EntityRenderer renderer = Minecraft.getMinecraft().entityRenderer;
					if(renderer.isShaderActive()){
						renderer.stopUseShader();
					}
					renderer.loadShader(new ResourceLocation("shaders/post/flip.json"));
				}
				
				@Override
				public void onEnd(EntityPlayer player, World world) {
					EntityRenderer renderer = Minecraft.getMinecraft().entityRenderer;
					if(renderer.isShaderActive()){
						renderer.stopUseShader();
					}
				}
			});
			
			executor(Actions.FIREWORK_POWERED, new Executor() {
				@Override
				public void onStart(EntityPlayer player, World world) {
					player.playSound(SoundEvents.ENTITY_FIREWORK_LAUNCH, 1, 1);
					player.motionY = 2;
				}
				
				@Override
				public void onEnd(EntityPlayer player, World world) {
				}
			});
		}
	}
	
	private static void executor(Actions action, Executor executor){
		System.out.println("Registring executor for "+action.toString());
		executors.put(action, executor);
	}
	
	public static void executeAction(Actions action){
		registerEvents();
		EntityPlayer player = FMLClientHandler.instance().getClientPlayerEntity();
		World world = Helper.findWorld(player);
		world.playSound(player, player.getPosition(), SoundEvents.ENTITY_ARROW_HIT_PLAYER, SoundCategory.BLOCKS, 1f, 1f);
		
		Helper.sendMessage("&aYou followers select &6"+action.name);
		
		currentExecutor = executors.get(action);
		currentAction = action;
		currentExecutor.onStart(player, world);
	}
	
	public interface Executor{
		public void onStart(EntityPlayer player, World world);
		public void onEnd(EntityPlayer player, World world);
	}
}
