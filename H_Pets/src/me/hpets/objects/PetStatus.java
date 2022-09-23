package me.hpets.objects;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import lombok.Getter;
import lombok.Setter;
import me.hpets.objects.petutils.enums.StatusPet;

@Getter
@Setter
public class PetStatus {

	private Pet pet;
	private StatusPet status = StatusPet.PASSIVE;
	private Entity target;
	private long lastDamage;
	
	public PetStatus(Pet pet) {
		this.pet = pet;
	}
	
	public void attack(LivingEntity entity) {
		this.lastDamage = System.currentTimeMillis();
		this.target = entity;
		this.status = StatusPet.AGRESSIVE;
	}
	
}
