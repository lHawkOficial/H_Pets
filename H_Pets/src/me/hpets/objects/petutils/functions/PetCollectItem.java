package me.hpets.objects.petutils.functions;

import org.bukkit.Sound;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import lombok.Getter;
import me.hawkcore.tasks.Task;
import me.hawkcore.utils.API;
import me.hawkcore.utils.locations.Distance;
import me.hpets.objects.Pet;
import me.hpets.objects.PetStatus;
import me.hpets.objects.PlayerPet;
import me.hpets.objects.petutils.PetFunctions;

public class PetCollectItem extends PetFunctions implements Runnable {

@Getter
private API api = API.get();
	
	private long delayCollect;

	public PetCollectItem(PlayerPet player, Pet pet) {
		super(player, pet);
	}
	
	@Override
	public void run() {
		PlayerPet player = getPlayer();
		Pet pet = getPet();
		Entity entity = pet.getEntity();
		Player p = player.getPlayer();
		Inventory inv = pet.getInventory();
		PetStatus mode = pet.getMode();
		Entity target = mode.getTarget();
		double speed = pet.getSpeed().getValue();
		if (p == null) return;
		if (inv.firstEmpty() == -1) return;
		if (System.currentTimeMillis() - delayCollect < 200) return;
		if (target == null) {
			mode.setTarget(pet.getItemNext());
			delayCollect = System.currentTimeMillis();
			return;
		}
		if (target != null && !(target instanceof Item)) return;
		Item item = (Item) target;
		if (item == null || !item.isValid()) {
			mode.setTarget(null);
			return;
		}
		api.makeEntityMoveTo(entity, item.getLocation().clone(), speed);
		Distance distance = new Distance(item.getLocation(), entity.getLocation());
		double value = distance.value();
		if (value > 1.25) return;
		delayCollect = System.currentTimeMillis();
		mode.setTarget(null);
		Task.run(()-> {
			inv.addItem(item.getItemStack().clone());
			item.remove();
			pet.getPlayerNext().forEach(all -> all.playSound(entity.getLocation(), Sound.ITEM_PICKUP, 0.5f, 1.25f));
		});
	}
	
}
