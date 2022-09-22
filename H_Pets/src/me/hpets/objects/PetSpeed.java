package me.hpets.objects;

import lombok.Getter;
import lombok.Setter;

@Getter
public class PetSpeed {

	private final double maxSpeed;
	@Setter
	private double value;
	private Pet pet;
	
	public PetSpeed(Pet pet, double maxSpeed) {
		this.pet = pet;
		this.maxSpeed = maxSpeed;
		this.value = 1.1;
		System.out.println("Valor speed pet: " + getValue());
	}
	
}
