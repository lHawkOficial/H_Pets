package me.hpets.objects.petutils.functions;

import org.bukkit.Bukkit;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import lombok.Getter;
import me.hawkcore.tasks.Task;
import me.hawkcore.utils.API;
import me.hawkcore.utils.locations.Distance;
import me.hpets.objects.Pet;
import me.hpets.objects.PetStatus;
import me.hpets.objects.PlayerPet;
import me.hpets.objects.Status;
import me.hpets.objects.petutils.PetFunctions;

@Getter
public class PetMove extends PetFunctions implements Runnable {
	
	private API api = API.get();
	
	public PetMove(PlayerPet player, Pet pet) {
		super(player, pet);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void run() {
	
		PlayerPet player = getPlayer();
		Pet pet = getPet();
		Entity entity = pet.getEntity();
		Player p = player.getPlayer();
		if (p == null) return;
		
		PetStatus mode = pet.getMode();
		Status status = mode.getStatus();
		if (status == Status.PASSIVE) {
			Location loc = p.getLocation();
			Distance distance = new Distance(loc, entity.getLocation());
			double value = distance.value();
			if (value <= 5) return;
			if (value > 20) {
				Task.run(()-> entity.teleport(p.getLocation()));
				return;
			}
			api.makeEntityMoveTo(entity, p.getLocation(), 1.5);
		}else if (System.currentTimeMillis() - mode.getLastDamage() >= 600) {
			Entity target = mode.getTarget();
			if (target != null && !target.isDead() && target.isValid() && target instanceof LivingEntity && !target.equals(entity)) {
				api.makeEntityMoveTo(entity, target.getLocation(), 2.5);
				Distance distance = new Distance(target.getLocation(), entity.getLocation());
				double value = distance.value();
				if (value > 20) {
					Task.run(()-> entity.teleport(target.getLocation()));
					return;
				}
				if (value > 1) return;
				mode.setLastDamage(System.currentTimeMillis());
				((LivingEntity)target).damage(2, entity);
				EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(entity, target, DamageCause.ENTITY_ATTACK, 2);
				target.setLastDamageCause(event);
				Bukkit.getPluginManager().callEvent(event);
			} else mode.setStatus(Status.PASSIVE);
		}
		
	}
	
}
