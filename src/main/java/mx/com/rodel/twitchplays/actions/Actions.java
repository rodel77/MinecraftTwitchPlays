package mx.com.rodel.twitchplays.actions;

import java.awt.Color;

public enum Actions {
	POISON("Poison", 0x00FF00),
	WHITER("Spawn wither", Color.GRAY),
	THUNDER("A thunder", Color.YELLOW),
	DIAMOND("Get a diamond", Color.CYAN);
	
	public String name;
	public int color;
	public int x = 0;
	public int y = 0;
	
	private Actions(String name, int color){
		this.name = name;
		this.color = color;
		int x = 0;
		int y = ordinal()/10;
		int c = 0;
		
		for (int i = y*10; i < (y*10)+10; i++) {
			if(i==ordinal()){
				x = c;
				break;
			}
			c++;
		}
		
		this.x = x*24;
		this.y = y*24;
	}
	
	private Actions(String name, Color color){
		this(name, color.getRGB());
	}
}
