package mx.com.rodel.twitchplays.gui;

import java.io.IOException;

import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;
import org.lwjgl.input.Keyboard;

import mx.com.rodel.twitchplays.bot.TwitchBot;
import mx.com.rodel.twitchplays.utils.Helper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;

public class ConnectGUI extends GuiScreen{
	
	private GuiTextField text;
	private CenteredButton dsBtn;
	
	@Override
	public void initGui() {
		int centerX = width/2-37;	
		int centerY = height/2-10;
		
		buttonList.add(new CenteredButton(0, width/2, (height/2)+30, "Connect"));
		buttonList.add(dsBtn = new CenteredButton(1, width/2, (height/2)+60, "Discconnect"));

		dsBtn.visible = TwitchBot.isConnected;
		
		text = new GuiTextField(1, fontRendererObj, (width/2)-100, (height/2)-10, 200, 20);
		text.setFocused(true);
		text.setMaxStringLength(50);
		
		text.setText(TwitchBot.instance.getChannel());
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		
		int centerX = width/2-37;
		int centerY = height/2-10;
		
		drawCenteredString(fontRendererObj, TwitchBot.isConnected ? "Connected" : "Disconnected", width/2, (height/2)-40, TwitchBot.isConnected ? 0x00FF00 : 0xFF0000);
		drawCenteredString(fontRendererObj, "Insert your name twitch name here", width/2, (height/2)-25, 0xFFFFFF);
		
		dsBtn.visible = TwitchBot.isConnected;
		
		text.drawTextBox();
		
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	public void updateScreen() {
		super.updateScreen();
		text.updateCursorCounter();
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		text.mouseClicked(mouseX, mouseY, mouseButton);
		
		if(buttonList.get(0).mousePressed(mc, mouseX, mouseY)){
			connect();
		}else if(buttonList.get(1).mousePressed(mc, mouseX, mouseY)){
			TwitchBot.instance.disconnect();
		}
	}
	
	public void connect(){
		if(TwitchBot.isConnected){
			TwitchBot.instance.disconnect();
		}
		if(text.getText().isEmpty() || !Helper.channelExists(text.getText())){
			FMLClientHandler.instance().getClient().displayGuiScreen(null);
			Helper.sendMessage("&cSorry but this channel does not exists!");
			return;
		}
		FMLClientHandler.instance().getClient().displayGuiScreen(null);
		TwitchBot bot = TwitchBot.instance;
		bot.setVerbose(true);
		try {
			bot.connect("irc.twitch.tv", 6667, "oauth:js6ui5rvnuvk5z53twqgxuntmlf2v9");
			bot.joinChannel("#"+text.getText());
		} catch (NickAlreadyInUseException e) {
			e.printStackTrace();
		} catch (IrcException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		super.keyTyped(typedChar, keyCode);
		text.textboxKeyTyped(typedChar, keyCode);
		if(keyCode==Keyboard.KEY_RETURN){
			connect();
		}
	}
}
