package me.hpets;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;
import me.hpets.listeners.ListenerPlayer;
import me.hpets.managers.Manager;
import me.hpets.objects.PlayerPet;

@Getter
public class Core extends JavaPlugin {

	@Getter
	private static Core instance;
	private String tag, version = "§dv" + getDescription().getVersion();
	private Manager manager;
	
	@Override
	public void onEnable() {
		instance = this;
		saveDefaultConfig();
		reloadPlugin();
		
		List<Listener> events = new ArrayList<>();
		events.add(new ListenerPlayer());
		events.forEach(event -> Bukkit.getPluginManager().registerEvents(event, this));
		
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
		manager = new Manager();
		PlayerPet.checkAll();
	}
	
	public void sendConsole(String msg) {Bukkit.getConsoleSender().sendMessage(msg.replace("&", "§"));}
	
}
