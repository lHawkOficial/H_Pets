package me.hpets.objects.petutils.functions;

import org.bukkit.Bukkit;


import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
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
import me.hpets.objects.petutils.PetFunctions;
import me.hpets.objects.petutils.enums.StatusPet;

@Getter
public class PetMove extends PetFunctions implements Runnable {
	
	private API api = API.get();
	private double attackDelay = 350;
	private double attackDamage = 2;
	
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
		StatusPet status = mode.getStatus();
		double speed = pet.getSpeed().getValue();
		
		if (entity.getPassenger() != null && entity.getPassenger().equals(p)) {
			api.makeEntityMoveTo(entity, api.getLocationInFrontEntity(p, 3.5), speed/2);
			return;
		}
		if (mode.getTarget() != null && mode.getTarget() instanceof Item) return;
		if (mode.getTarget() != null && new Distance(p.getLocation(), entity.getLocation()).value() > 16) {
			mode.setTarget(null);
			return;
		}
		if (status == StatusPet.PASSIVE || mode.getTarget() == null) {
			if (mode.getTarget() != null) mode.setTarget(null);
			Location loc = p.getLocation();
			Distance distance = new Distance(loc, entity.getLocation());
			double value = distance.value();
			if (value <= 5) return;
			if (value > 16) {
				Task.run(()-> entity.teleport(p.getLocation()));
				return;
			}
			api.makeEntityMoveTo(entity, p.getLocation(), speed);
		}else if (System.currentTimeMillis() - mode.getLastDamage() >= attackDelay) {
			Entity target = mode.getTarget();
			if (target != null && !target.isDead() && target.isValid() && target instanceof LivingEntity && !target.getUniqueId().equals(entity.getUniqueId()) && !target.getName().equals(player.getName())) {
				api.makeEntityMoveTo(entity, target.getLocation(), speed);
				Distance distance = new Distance(target.getLocation(), entity.getLocation());
				double value = distance.value();
				if (value > 16) {
					Task.run(()-> entity.teleport(target.getLocation()));
					return;
				}
				if (value > 1.25) return;
				mode.setLastDamage(System.currentTimeMillis());
				Task.run(()->{
					((LivingEntity)target).damage(attackDamage, entity);
					EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(entity, target, DamageCause.ENTITY_ATTACK, attackDamage);
					target.setLastDamageCause(event);
					Bukkit.getPluginManager().callEvent(event);
				});
			} else {
				mode.setTarget(null);
			}
		}
		
	}
	
}
