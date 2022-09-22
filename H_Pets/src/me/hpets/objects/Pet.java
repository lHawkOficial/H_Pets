package me.hpets.objects;

import org.bukkit.Bukkit;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.metadata.FixedMetadataValue;

import lombok.Getter;
import lombok.Setter;
import me.hawkcore.Core;
import me.hawkcore.tasks.Task;
import me.hpets.objects.petutils.PetAI;
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
	private PetSpeed speed;
	private NBTTagCompound tag;
	private PetStatus mode;
	
	public Pet(EntityType type, PlayerPet player) {
		this.player = player;
		this.inventory = Bukkit.createInventory(null, 9*3, "§8● Inventário do seu pet");
		this.petAi = new PetAI(player, this);
		this.task = new Task(petAi).run(1);
		this.speed = new PetSpeed(this, 10);
		this.mode = new PetStatus(this);
	}
	
	public NBTTagCompound getNbtUnserialize(String txt) {
		try {
			return MojangsonParser.parse(txt);
		} catch (MojangsonParseException e) {
			return null;
		}
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
		player.save();
		entity.remove();
	}
	
	public void delete() {
		entity.remove();
		type = null;
		player.save();
	}
	
	public static Pet get(Player p) {
		return PlayerPet.check(p).getPet();
	}
	
}
