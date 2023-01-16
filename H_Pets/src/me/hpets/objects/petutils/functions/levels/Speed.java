package me.hpets.objects.petutils.functions.levels;

import lombok.Getter;
import lombok.Setter;
import me.hpets.objects.Pet;

@Getter
public class Speed {

	private final double maxSpeed;
	@Setter
	private double value;
	private Pet pet;
	
	public Speed(Pet pet, double maxSpeed) {
		this.pet = pet;
		this.maxSpeed = maxSpeed;
	}
	
	public double getValue() {
		return 2.5;
	}
	
}
