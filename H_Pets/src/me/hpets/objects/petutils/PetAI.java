package me.hpets.objects.petutils;

import java.util.ArrayList;

import java.util.List;

import lombok.Getter;
import me.hpets.objects.Pet;
import me.hpets.objects.PlayerPet;
import me.hpets.objects.petutils.functions.PetMove;

@Getter
public class PetAI implements Runnable {
	
	private PlayerPet player;
	private Pet pet;
	private List<Runnable> runnables = new ArrayList<>();
	
	public PetAI(PlayerPet player, Pet pet) {
		this.player = player;
		this.pet = pet;
		runnables.add(new PetMove(player, pet));
	}
	
	@Override
	public void run() {
		
		if (runnables.isEmpty()) return;
		if (!pet.isValid()) return;
		for (int i = 0; i < runnables.size(); i++) {
			try {
				runnables.get(i).run();
			} catch (Exception e) {
				continue;
			}
		}
		
	}
	
}
