package me.hpets;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;
import me.hpets.commands.CommandPet;
import me.hpets.listeners.ListenerPets;
import me.hpets.listeners.ListenerPlayer;
import me.hpets.managers.Manager;
import me.hpets.objects.Pet;
import me.hpets.objects.PlayerPet;
import me.hpets.utils.API;
import me.hpets.utils.ConfigGeral;
import me.hpets.utils.Mensagens;

@Getter
public class Core extends JavaPlugin {

	@Getter
	private static Core instance;
	private String tag, version = "§dv" + getDescription().getVersion();
	private Manager manager;
	private ConfigGeral configgeral;
	private Mensagens mensagens;
	private API api;
	
	@Override
	public void onEnable() {
		instance = this;
		saveDefaultConfig();
		reloadPlugin();
		
		List<Listener> events = new ArrayList<>();
		events.add(new ListenerPlayer());
		events.add(new ListenerPets());
		events.forEach(event -> Bukkit.getPluginManager().registerEvents(event, this));
		new CommandPet();
		
		sendConsole(" ");
		sendConsole(tag + " &aH_Pets iniciado com sucesso! &6[Author lHawk_] " + version);
		sendConsole(" ");
	}
	
	@Override
	public void onDisable() {
		manager.getPlayers().forEach(p -> {
			Pet pet = p.getPet();
			pet.remove();
			pet.getTask().cancel();
		});
		for(World world : Bukkit.getWorlds()) {
			for(Entity entity : world.getEntities()) {
				if (entity.hasMetadata("pet")) entity.remove();
			}
		}
		sendConsole(" ");
		sendConsole(tag + " &cH_Pets desligado com sucesso! &6[Author lHawk_] " + version);
		sendConsole(" ");
	}
	
	public void reloadPlugin() {
		reloadConfig();
		tag = getConfig().getString("Config.tag").replace("&", "§");
		api = new API();
		if (manager!=null) {
			manager.getPlayers().forEach(p -> {
				Pet pet = p.getPet();
				pet.remove();
				pet.getTask().cancel();
			});
		}
		manager = new Manager();
		configgeral = new ConfigGeral();
		mensagens = new Mensagens();
		
		File folder = new File(getDataFolder() + "/players");
		if (folder.exists()) {
			for(File file : folder.listFiles()) {
				if (!file.getName().endsWith(".json")) continue;
				new PlayerPet(file.getName().replace(".json", new String()));
			}
		}
		
		PlayerPet.checkAll();
	}
	
	public void sendConsole(String msg) {Bukkit.getConsoleSender().sendMessage(msg.replace("&", "§"));}
	
}
