package me.hpets.utils;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import lombok.Getter;
import me.hawkcore.utils.itemcreator.ItemCreator;
import me.hpets.Core;

@Getter
public class ConfigGeral {

	private ItemStack tameItem;
	
	public ConfigGeral() {
		FileConfiguration config = Core.getInstance().getConfig();
		ConfigurationSection section = config.getConfigurationSection("Config");
		this.tameItem = ItemCreator.get().getItem(section.getString("tameItem")) == null ? null : ItemCreator.get().getItem(section.getString("tameItem")).getItem().clone();
	}
	
	public static ConfigGeral get() {
		return Core.getInstance().getConfiggeral();
	}
	
}
