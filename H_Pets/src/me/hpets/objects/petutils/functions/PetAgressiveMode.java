package me.hpets.objects.petutils.functions;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import lombok.Getter;
import me.hawkcore.tasks.Task;
import me.hawkcore.utils.API;
import me.hpets.objects.Pet;
import me.hpets.objects.PetStatus;
import me.hpets.objects.PlayerPet;
import me.hpets.objects.petutils.PetFunctions;
import me.hpets.objects.petutils.enums.StatusPet;
import me.hpets.utils.ConfigGeral;

@Getter
public class PetAgressiveMode extends PetFunctions implements Runnable {

	private API api = API.get();
	private ConfigGeral config = ConfigGeral.get();
	
	public PetAgressiveMode(PlayerPet player, Pet pet) {
		super(player, pet);
	}
	
	@Override
	public void run() {
		PlayerPet player = getPlayer();
		Pet pet = getPet();
		Entity entity = pet.getEntity();
		Player p = player.getPlayer();
		PetStatus mode = pet.getMode();
		Entity target = mode.getTarget();
		if (p == null) return;
		if (config.getUse_regions()) {
			if (!api.containsRegion(entity.getLocation(), config.getRegions())) {
				Task.run(()-> player.hidePet());
				return;
			}
		}
		if (mode.getStatus() != StatusPet.AGRESSIVE) return;
		if (target != null) return;
		mode.attack(pet.getEntityNext());
	}
	
}
