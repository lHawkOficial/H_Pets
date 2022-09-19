package me.hpets;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;

@Getter
public class Core extends JavaPlugin {

	@Getter
	private static Core instance;
	private String tag, version = "§dv" + getDescription().getVersion();
	
	@Override
	public void onEnable() {
		instance = this;
		saveDefaultConfig();
		reloadPlugin();
		
		sendConsole(" ");
		sendConsole(tag + " &aH_Pets iniciado com sucesso! &6[Author lHawk_] " + version);
		sendConsole(" ");
	}
	
	@Override
	public void onDisable() {
		sendConsole(" ");
		sendConsole(tag + " &cH_Pets desligado com sucesso! &6[Author lHawk_] " + version);
		sendConsole(" ");
	}
	
	public void reloadPlugin() {
		reloadConfig();
		tag = getConfig().getString("Config.tag").replace("&", "§");
	}
	
	public void sendConsole(String msg) {Bukkit.getConsoleSender().sendMessage(msg.replace("&", "§"));}
	
}
