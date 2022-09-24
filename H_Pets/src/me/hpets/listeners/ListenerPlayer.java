package me.hpets.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.hpets.Core;
import me.hpets.managers.Manager;
import me.hpets.objects.Pet;
import me.hpets.objects.PlayerPet;

public class ListenerPlayer implements Listener {

	@EventHandler
	public void join(PlayerJoinEvent e) {
		PlayerPet.check(e.getPlayer());
	}
	
	@EventHandler
	public void quit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		PlayerPet pp = PlayerPet.check(p);
		Pet pet = pp.getPet();
		pet.remove();
		pet.getTask().cancel();
		Manager.get().getPlayers().remove(pp);
		if (p.hasMetadata("playerpet")) p.removeMetadata("playerpet", Core.getInstance());
	}
	
}
