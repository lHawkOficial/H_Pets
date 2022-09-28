package me.hpets.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import lombok.Getter;
import me.hawkcore.utils.itemcreator.ItemCreator;
import me.hpets.Core;

@Getter
public class ConfigGeral {

	private ItemStack tameItem;
	
	private Boolean use_regions;
	
	private List<String> regions = new ArrayList<>();
	private List<World> worlds = new ArrayList<>();
	
	private double attack_max_distance,
	attack_delay,
	attack_distance;
	
	public ConfigGeral() {
		FileConfiguration config = Core.getInstance().getConfig();
		ConfigurationSection section = config.getConfigurationSection("Config");
		tameItem = ItemCreator.get().getItem(section.getString("tameItem")) == null ? null : ItemCreator.get().getItem(section.getString("tameItem")).getItem().clone();
		use_regions = section.getBoolean("use_regions");
		for(String line : section.getStringList("regions")) {
			String[] args = line.split(":");
			World world = Bukkit.getWorld(args[0]);
			if (world == null) continue;
			worlds.add(world);
			regions.add(args[1]);
		}
		attack_max_distance = section.getDouble("attack_max_distance");
		attack_delay = section.getDouble("attack_delay");
		attack_distance = section.getDouble("attack_distance");
	}
	
	public static ConfigGeral get() {
		return Core.getInstance().getConfiggeral();
	}
	
}
