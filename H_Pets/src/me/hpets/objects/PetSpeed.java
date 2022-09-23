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
	}
	
	public double getValue() {
		return 1.95;
	}
	
}
