package me.hpets.utils;


import lombok.Getter;
import me.hpets.Core;

@Getter
public class Mensagens {

	private String petTamed,
	petDeath,
	petCooldown,
	petMode,
	petModeSelected,
	petName,
	petHide,
	petSpawn,
	petSpawned;
	
	public Mensagens() {
		petSpawned = replace("petSpawned");
		petSpawn = replace("petSpawn");
		petHide = replace("petHide");
		petName = replace("petName");
		petModeSelected = replace("petModeSelected");
		petMode = replace("petMode");
		petCooldown = replace("petCooldown");
		petDeath = replace("petDeath");
		petTamed = replace("petTamed");
	}
	
	private String replace(String msg) {
		return Core.getInstance().getConfig().getString("Mensagens." + msg).replace("&", "§").replace("{tag}", Core.getInstance().getTag());
	}
	
	public static Mensagens get() {
		return Core.getInstance().getMensagens();
	}
	
}
