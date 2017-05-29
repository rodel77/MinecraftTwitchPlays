package mx.com.rodel.twitchplays.events;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;

import mx.com.rodel.twitchplays.TwitchPlays;
import mx.com.rodel.twitchplays.actions.ActionExecutor;
import mx.com.rodel.twitchplays.actions.Actions;
import mx.com.rodel.twitchplays.actions.State;
import mx.com.rodel.twitchplays.bot.TwitchBot;
import mx.com.rodel.twitchplays.utils.Helper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class OverlayHandler extends Gui{
	
	private final Minecraft mc;
	
	public static int minX = -200;
	public static int x = minX;
	public static int state = 0;
	public static Random random = new Random();
	
	
	public static HashMap<Integer, Entry<Actions, Integer>> actions = new HashMap<Integer, Entry<Actions,Integer>>();
	
	private static final ResourceLocation texture = new ResourceLocation("twitchplays", "textures/gui/overlay.png");
	
	public OverlayHandler() {
		mc = Minecraft.getMinecraft();
	}
	
	public static void vote(int index){
		Entry<Actions, Integer> ac = actions.get(index);
		actions.put(index, Helper.entry(ac.getKey(), ac.getValue()+1));
	}
	
	public static void createActions(){
		actions = new HashMap<Integer, Entry<Actions,Integer>>();
		int i = 0;
		while(actions.size()!=3){
			Actions action = Actions.values()[random.nextInt(Actions.values().length)];
			if(!contains(action)){
				System.out.println(action.name);
				actions.put(i, Helper.entry(action, 0));
				i++;
			}
		}
	}

	public static boolean contains(Actions action){
		for (int i = 0; i < actions.size(); i++) {
			System.out.println("Executing actions contains... "+i);
			if(actions.get(i).getKey()==action){
				return true;
			}
		}
		return false;
	}
	
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public void onOve(RenderGameOverlayEvent.Post e){
		if(e.getType()==ElementType.ALL){
			ScaledResolution res = new ScaledResolution(mc);
			FontRenderer fontRenderer = mc.fontRendererObj;
			
			int width = res.getScaledWidth();
			int height = res.getScaledHeight();
			
			mc.entityRenderer.setupOverlayRendering();
			mc.getTextureManager().bindTexture(texture);
			
			GlStateManager.pushAttrib();
			GlStateManager.color(1f, 1f, 1f, 1f);
			GlStateManager.disableLighting();
			GlStateManager.enableAlpha();
			
			if(state==0){
				x=minX;
			}else if(state==1){
				if(x<=0){
					x++;
				}
			}else if(state==-1){
				if(x>=minX){
					x--;
				}
			}
			
//			drawCenteredTexture(100, 100, 48, 246, 30, 5);
//			drawTexturedModalRect(width/2 - (60/2) + 2, height/2 - (10/2), 48, 236, 56, 10);
//			drawCenteredTexture(width/2, height/2, 48, 246, 60, 10);
//			drawScaledCustomSizeModalRect(100, 100, 48, 246, 30, 5, 30, 5, 1, 1);
//			drawModalRectWithCustomSizedTexture(100, 100, 0, 259, 30, 5, 2, 2);
			
			if(actions.size()== 3){
				drawCenteredTexture(x+15, (height/2)-25, 0, 232, 24, 24);
				drawCenteredTexture(x+15, (height/2)-25, actions.get(0).getKey().x, actions.get(0).getKey().y, 24, 24);
				
				drawCenteredTexture(x+15, height/2, 0, 232, 24, 24);
				drawCenteredTexture(x+15, height/2, actions.get(1).getKey().x, actions.get(1).getKey().y, 24, 24);
				
				
				drawCenteredTexture(x+15, (height/2)+25, 0, 232, 24, 24);
				drawCenteredTexture(x+15, (height/2)+25, actions.get(2).getKey().x, actions.get(2).getKey().y, 24, 24);
				
				if(ActionExecutor.currentAction!=null){
					drawCenteredTexture(16, (height/2)-60, 24, 232, 24, 24);
					drawCenteredTexture(16, (height/2)-60, ActionExecutor.currentAction.x, ActionExecutor.currentAction.y, 24, 24);
				}
				
				drawString(fontRenderer, actions.get(0).getKey().name+" ("+actions.get(0).getValue()+" votes)", x+15+22, ((height/2)-25)-(fontRenderer.FONT_HEIGHT/2), actions.get(0).getKey().color);
				drawString(fontRenderer, actions.get(1).getKey().name+" ("+actions.get(1).getValue()+" votes)", x+15+22, (height/2)-(fontRenderer.FONT_HEIGHT/2), actions.get(1).getKey().color);
				drawString(fontRenderer, actions.get(2).getKey().name+" ("+actions.get(2).getValue()+" votes)", x+15+22, ((height/2)+25)-(fontRenderer.FONT_HEIGHT/2), actions.get(2).getKey().color);
				
				drawString(fontRenderer, "!1", x+22, ((height/2)-25)-(fontRenderer.FONT_HEIGHT/2)+5, 0xFFFFFF);
				drawString(fontRenderer, "!2", x+22, (height/2)-(fontRenderer.FONT_HEIGHT/2)+5, 0xFFFFFF);
				drawString(fontRenderer, "!3", x+22, ((height/2)+25)-(fontRenderer.FONT_HEIGHT/2)+5, 0xFFFFFF);
				
				if(ActionExecutor.currentAction!=null){
					drawString(fontRenderer, "Current: ", 35, ((height/2)-60)-(fontRenderer.FONT_HEIGHT/2), 0xFF33F2);
					drawString(fontRenderer, ActionExecutor.currentAction.name, 35+fontRenderer.getStringWidth("Current: "), ((height/2)-60)-(fontRenderer.FONT_HEIGHT/2), ActionExecutor.currentAction.color);
				}
			}
			
			if(TwitchBot.instance.state==State.PRE_SELECTING || TwitchBot.instance.state==State.SELECTING){
				long secs = TwitchPlays.getRemaining()/1000;
				drawCenteredString(fontRenderer, TwitchBot.instance.state==State.PRE_SELECTING ? "Next vote on "+secs+"..." : secs+" seconds for voting...", width/2, height-65, Helper.colorFromCountdown(secs, 10));
			}
			
			GlStateManager.popAttrib();
		}
	}
	
	public void drawCenteredTexture(int x, int y, int textureX, int textureY, int width, int height){
		drawTexturedModalRect(x - (width/2), y - (height / 2), textureX, textureY, width, height);
	}
	
	public void drawCenteredString(FontRenderer fontRenderer, String text, int x, int y, int color){
		fontRenderer.drawStringWithShadow(text, x - (fontRenderer.getStringWidth(text) /2), y, color);
	}
}
