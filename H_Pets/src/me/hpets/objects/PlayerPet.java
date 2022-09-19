package me.hpets.objects;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import lombok.Getter;
import me.hawkcore.utils.Save;
import me.hpets.Core;
import me.hpets.managers.Manager;

@Getter
public class PlayerPet {

	private String name;
	private File file;
	
	public PlayerPet(String name) {
		this.name = name;
		File folder = new File(Core.getInstance().getDataFolder() + "/players");
		if (!folder.exists()) folder.mkdir();
		file = new File(folder + "/" + name + ".json");
		if (!file.exists()) {
			try {
				file.createNewFile();
				save();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		load();
		Manager.get().getPlayers().add(this);
	}
	
	public void save() {
		List<Object> lista = new ArrayList<>();
		new Save(file, lista);
	}
	
	public void load() {
		List<Object> lista = Save.load(file);
		if (lista == null) return;
		if (lista.isEmpty()) return;
	}
	
	public Player getPlayer() {
		return Bukkit.getPlayerExact(name);
	}
	
	public static PlayerPet check(Player p) {
		if (p.hasMetadata("playerpet")) return (PlayerPet) p.getMetadata("playerpet").get(0).value();
		PlayerPet pp = Manager.get().getPlayer(p.getName());
		if (pp == null) pp = new PlayerPet(p.getName());
		p.setMetadata("playerpet", new FixedMetadataValue(Core.getInstance(), pp));
		return pp;
	}
	
	public static void checkAll() {
		for(Player all : Bukkit.getOnlinePlayers()) {
			if (all.hasMetadata("playerpet")) all.removeMetadata("playerpet", Core.getInstance());
			PlayerPet.check(all);
		}
	}
	
}
