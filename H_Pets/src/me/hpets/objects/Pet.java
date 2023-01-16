package me.hpets.objects;

import java.util.ArrayList;


import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.metadata.FixedMetadataValue;

import lombok.Getter;
import lombok.Setter;
import me.hawkcore.Core;
import me.hawkcore.tasks.Task;
import me.hawkcore.utils.locations.Distance;
import me.hpets.objects.petutils.PetAI;
import me.hpets.objects.petutils.functions.levels.Speed;
import me.hpets.utils.ConfigGeral;
import net.minecraft.server.v1_8_R3.MojangsonParseException;
import net.minecraft.server.v1_8_R3.MojangsonParser;
import net.minecraft.server.v1_8_R3.NBTTagCompound;

@Getter
@Setter
public class Pet {
	
	private String name;
	private EntityType type;
	private Entity entity;
	private PlayerPet player;
	private Inventory inventory;
	private Task task;
	private PetAI petAi;
	private Speed speed;
	private NBTTagCompound tag;
	private PetStatus mode;
	
	public Pet(EntityType type, PlayerPet player) {
		this.player = player;
		this.inventory = Bukkit.createInventory(null, 9*3, "§8● Inventário do seu pet");
		this.petAi = new PetAI(player, this);
		this.task = new Task(petAi).run(1);
		this.speed = new Speed(this, 10);
		this.mode = new PetStatus(this);
	}
	
	public NBTTagCompound getNbtUnserialize(String txt) {
		try {
			return MojangsonParser.parse(txt);
		} catch (MojangsonParseException e) {
			return null;
		}
	}
	
	public Item getItemNext() {
		if (entity == null) return null;
		World world = entity.getWorld();
		Location loc = entity.getLocation().clone();
		double value = 16;
		Item item = null;
		for(Entity entity : world.getEntitiesByClass(Item.class)) {
			Distance distance = new Distance(entity.getLocation(), loc);
			Double v = distance.value();
			if (v > 16) continue;
			if (v <= value) {
				value = v;
				item = (Item) entity;
				continue;
			}
		}
		return item;
	}
	
	public LivingEntity getEntityNext() {
		if (entity == null) return null;
		World world = entity.getWorld();
		Location loc = entity.getLocation().clone();
		ConfigGeral config = ConfigGeral.get();
		Player p = player.getPlayer();
		if (p == null) return null;
		double value = config.getAttack_max_distance();
		LivingEntity et = null;
		for(Entity entity : world.getEntities()) {
			if (!(entity instanceof LivingEntity)) continue;
			if (entity instanceof ArmorStand) continue;
			if (entity.getUniqueId().equals(p.getUniqueId())) continue;
			if (entity.getUniqueId().equals(this.entity.getUniqueId())) continue;
			Distance distance = new Distance(entity.getLocation(), loc);
			Double v = distance.value();
			if (v > config.getAttack_max_distance()) continue;
			if (v <= value) {
				value = v;
				et = (LivingEntity) entity;
				continue;
			}
		}
		return et;
	}
	
	public List<Player> getPlayerNext() {
		List<Player> list = new ArrayList<>();
		if (entity == null) return list;
		for(Player p : Bukkit.getOnlinePlayers()) {
			if (!p.getWorld().equals(entity.getWorld())) continue;
			if (new Distance(p.getLocation(), entity.getLocation()).value() > 10) continue;
			list.add(p);
		}
		return list;
	}
	
	public NBTTagCompound getNbt() {
		return entity == null ? null : ((CraftEntity)entity).getHandle().getNBTTag();
	}
	
	public boolean isValid() {
		if (type == null) return false;
		if (entity == null) return false;
		return !entity.isDead() && entity.isValid();
	}
	
	public void update() {
		if (entity == null) return;
		this.entity.setCustomNameVisible(true);
		this.entity.setCustomName(name.replace("&", "§"));
		this.entity.setMetadata("pet", new FixedMetadataValue(Core.getInstance(), this));
	}
	
	public void remove() {
		if (entity == null) return;
		entity.remove();
		player.save();
	}
	
	public void delete() {
		type = null;
		remove();
		player.save();
	}
	
	public static Pet get(Player p) {
		return PlayerPet.check(p).getPet();
	}
	
}
