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
	private StatusPet status = StatusPet.DEFAULT;
	private Entity target;
	private long lastDamage;
	
	public PetStatus(Pet pet) {
		this.pet = pet;
	}
	
	public void attack(LivingEntity entity) {
		if (status == StatusPet.PASSIVE) return;
		this.lastDamage = System.currentTimeMillis();
		this.target = entity;
	}
	
}
