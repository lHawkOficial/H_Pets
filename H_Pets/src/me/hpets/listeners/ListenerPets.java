package me.hpets.listeners;

import org.bukkit.Material;

import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.hawkcore.utils.items.Item;
import me.hpets.objects.Pet;
import me.hpets.objects.PetStatus;
import me.hpets.objects.PlayerPet;
import me.hpets.objects.Status;
import me.hpets.utils.ConfigGeral;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.NBTTagCompound;

public class ListenerPets implements Listener {

	@EventHandler
	public void tame(EntityDeathEvent e) {
		Entity entity = e.getEntity();
		Entity killer = e.getEntity().getKiller();
		if (e.getEntity().getKiller() instanceof Player) {
			Player p = e.getEntity().getKiller();
			PlayerPet pp = PlayerPet.check(p);
			if (!entity.hasMetadata("pet")) {
				if (p.getItemInHand() == null || !Item.isSimilar(p.getItemInHand(), ConfigGeral.get().getTameItem().clone())) return;
				if (pp.containsPet()) return;
				e.getDrops().clear();
				Pet pet = pp.getPet();
				LivingEntity et = (LivingEntity) entity;
				et.setHealth(et.getMaxHealth());
				pet.setEntity(entity);
				pet.setType(entity.getType());
				pet.setName("§ePet do jogador §f" + pp.getName());
				pet.update();
				
				NBTTagCompound tag = new NBTTagCompound();
				net.minecraft.server.v1_8_R3.Entity ce = ((CraftEntity)entity).getHandle();
				ce.c(tag);
				pet.setTag(tag);
				((EntityLiving)ce).a(tag);
				
				pp.save();
				pp.hidePet();
				pp.showPet();
				p.playSound(p.getLocation(), Sound.BLAZE_HIT, 0.5f, 0.5f);
			}
		}
		if (entity.hasMetadata("pet")) {
			e.getDrops().clear();
			Pet pet = (Pet) entity.getMetadata("pet").get(0).value();
			for(ItemStack item : pet.getInventory().getContents()) {
				if (item != null && item.getType() != Material.AIR) e.getDrops().add(item.clone());
			}
			pet.getInventory().clear();
			pet.getPlayer().hidePet();
		}
		if (killer != null && killer.hasMetadata("pet")) {
			Pet pet = (Pet) killer.getMetadata("pet").get(0).value();
			e.getDrops().forEach(item -> {
				Inventory inv = pet.getInventory();
				if (inv.firstEmpty() != -1) {
					inv.addItem(item.clone());
					e.getDrops().remove(item);
				}
			});
			pet.getPlayer().getPlayer().sendMessage("Seu pet matou o mob.");
		}
	}
	
	@EventHandler
	public void interact(PlayerInteractEntityEvent e) {
		Player p = e.getPlayer();
		Entity entity = e.getRightClicked();
		if (!entity.hasMetadata("pet")) return;
		Pet pet = (Pet) entity.getMetadata("pet").get(0).value();
		if (!pet.getPlayer().getName().equals(p.getName())) return;
		e.setCancelled(true);
		p.openInventory(pet.getInventory());
		p.updateInventory();
		p.playSound(p.getLocation(), Sound.CHEST_OPEN, 0.5f, 10);
	}
	
	@EventHandler
	public void target(EntityTargetEvent e) {
		Entity entity = e.getEntity();
		if (!entity.hasMetadata("pet")) return;
		e.setCancelled(true);
	}
	
	@EventHandler
	public void close(InventoryCloseEvent e) {
		Player p = (Player) e.getPlayer();
		PlayerPet pp = PlayerPet.check(p);
		Pet pet = pp.getPet();
		if (pet.getInventory().equals(e.getInventory())) {
			p.playSound(p.getLocation(), Sound.CHEST_CLOSE, 0.5f, 10);
			pp.saveAsync();
		}
	}
	
	@EventHandler
	public void click(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		PlayerPet pp = PlayerPet.check(p);
		Pet pet = pp.getPet();
		if (pet.getInventory().equals(e.getInventory())) {
			if (e.getCurrentItem() == null) return;
			if (e.getCurrentItem().getType() == Material.AIR) return;
			p.playSound(p.getLocation(), Sound.CLICK, 0.5f, 10);
		}
	}
	
	@EventHandler
	public void damage(EntityDamageByEntityEvent e) {
		Entity attacker = e.getDamager();
		if (attacker instanceof Projectile) {
			Projectile tile = (Projectile) attacker;
			if (tile.getShooter() instanceof Player) attacker = (Player) tile.getShooter();
		}
		if (!(attacker instanceof Player)) {
			Entity entity = e.getEntity();
			if (entity instanceof Projectile) {
				Projectile tile = (Projectile) entity;
				entity = (Entity) tile.getShooter();
			}
			if (!(entity instanceof Player)) {
				if (!entity.hasMetadata("pet")) return;
				Pet pet = (Pet) entity.getMetadata("pet").get(0).value();
				pet.getMode().setTarget(attacker);
				pet.getMode().setStatus(Status.AGRESSIVE);
				return;
			}
			Player p = (Player) entity;
			PlayerPet pp = PlayerPet.check(p);
			Pet pet = pp.getPet();
			if (pet.getEntity() == null) return;
			PetStatus mode = pet.getMode();
			mode.setTarget(attacker);
			mode.setStatus(Status.AGRESSIVE);
			return;
		}
		Player p = (Player) attacker;
		PlayerPet pp = PlayerPet.check(p);
		Pet pet = pp.getPet();
		if (pet.getEntity() == null) return;
		PetStatus mode = pet.getMode();
		mode.setTarget(e.getEntity());
		mode.setStatus(Status.AGRESSIVE);
	}
	
}
