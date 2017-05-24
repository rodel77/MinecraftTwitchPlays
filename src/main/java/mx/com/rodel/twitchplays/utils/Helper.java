package mx.com.rodel.twitchplays.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map.Entry;

import jline.internal.InputStreamReader;
import json.JSONObject;
import json.parser.JSONParser;
import mx.com.rodel.twitchplays.TwitchPlays;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.client.FMLClientHandler;

public class Helper {
	public static void sendMessage(String message){
		FMLClientHandler.instance().getClient().player.sendMessage(new TextComponentString(("&f[&9TwitchPlays&f]&7 "+message).replace("&", "�")));
	}
	
	public static boolean channelExists(String channel){
		try {
			URL url = new URL("https://api.twitch.tv/kraken/users/"+channel);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Client-ID", "ga9f94swxixv7gks6n5o8utvvtcjdw8");
			connection.setUseCaches(false);
			connection.setDoOutput(true);
			connection.connect();
			
			return connection.getResponseCode()!=404;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static int colorFromCountdown(long number, int max){
		int middle = max/2;
		if(number == middle){
			return 0xFFFF00; 
		}else if(number > middle){
			return 0x00FF00;
		}else if(number < middle){
			return 0xFF0000;
		}
		return 0xFFFFFF;
	}
	
	public static <T, E> Entry<T, E> entry(final T key, final E value){
		return new Entry<T, E>() {
			@Override
			public T getKey() {
				return key;
			}
			
			public E getValue() {
				return value;
			}

			@Override
			public E setValue(E value) {
				return value;
			};
		};
	}
	
	public float lerp(float a, float b, float f){
	    return a + f * (b - a);
	}
}