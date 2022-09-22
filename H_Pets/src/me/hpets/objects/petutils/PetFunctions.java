package me.hpets.objects.petutils;

import lombok.Getter;
import me.hpets.objects.Pet;
import me.hpets.objects.PlayerPet;

@Getter
public class PetFunctions {

	private PlayerPet player;
	private Pet pet;
	
	public PetFunctions(PlayerPet player, Pet pet) {
		this.player = player;
		this.pet = pet;
	}
	
}
