package me.hpets.objects;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PetStatus {

	private Pet pet;
	private Status status = Status.PASSIVE;
	private Entity target;
	private long lastDamage;
	
	public PetStatus(Pet pet) {
		this.pet = pet;
	}
	
	public void attack(LivingEntity entity) {
		this.lastDamage = System.currentTimeMillis();
		this.target = entity;
		this.status = Status.AGRESSIVE;
	}
	
}
