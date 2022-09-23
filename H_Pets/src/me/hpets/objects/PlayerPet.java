package me.hpets.objects;

import java.io.File;



import java.util.ArrayList;
import java.util.List;


import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import lombok.Getter;
import me.hawkcore.tasks.Task;
import me.hawkcore.utils.API;
import me.hawkcore.utils.Save;
import me.hpets.Core;
import me.hpets.managers.Manager;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.NBTTagCompound;

@Getter
public class PlayerPet {

	private String name;
	private File file;
	private Pet pet;
	
	public PlayerPet(String name) {
		this.name = name;
		this.pet = new Pet(null, this);
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
		Manager.get().getPlayers().add(this);
	}
	
	public void saveAsync() {
		Task.runAsync(()-> save());
	}
	
	public Boolean containsPet() {
		return pet == null ? false : pet.getType() != null;
	}
	
	public void save() {
		List<Object> lista = new ArrayList<>();
		if (pet.getEntity() != null) {
			Entity entity = pet.getEntity();
			lista.add(entity.getType().toString());
			lista.add(pet.getTag().toString());
			lista.add(pet.getName());
			lista.add(String.valueOf(pet.getSpeed().getValue()));
			lista.add(API.get().serializeItems(pet.getInventory().getContents().clone()));
		}
		new Save(file, lista);
	}
	
	public void showPet() {
		List<Object> lista = Save.load(file);
		if (lista == null) return;
		if (lista.isEmpty()) return;
		Player p = getPlayer();
		if (p == null) return;
		if (pet.isValid()) return;
		Location loc = p.getLocation().clone();
		Entity entity = loc.getWorld().spawnEntity(loc, EntityType.valueOf((String)lista.get(0)));
		pet.setEntity(entity);
		pet.setName((String)lista.get(2));
		pet.getSpeed().setValue(Double.valueOf((String)lista.get(3)));
		pet.getInventory().setContents(API.get().unserializeItems((String)lista.get(4)).clone());
		
		NBTTagCompound tag = pet.getNbtUnserialize((String)lista.get(1));
		net.minecraft.server.v1_8_R3.Entity ce = ((CraftEntity)entity).getHandle();
		pet.setTag(tag);
		((EntityLiving)ce).a(tag);
		pet.update();
		pet.setType(EntityType.valueOf((String)lista.get(0)));
		
	}
	
	public void hidePet() {
		if (pet.getEntity() == null) return;
		pet.remove();
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
