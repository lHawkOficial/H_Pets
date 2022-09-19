package me.hpets.managers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import lombok.Getter;
import me.hpets.Core;
import me.hpets.objects.PlayerPet;

@Getter
public class Manager {

	private List<PlayerPet> players = new ArrayList<>();
	
	public PlayerPet getPlayer(String name) {
		Iterator<PlayerPet> it = players.iterator();
		while(it.hasNext()) {
			PlayerPet pp = it.next();
			if (pp.getName().equalsIgnoreCase(name)) return pp;
		}
		return null;
	}
	
	public static Manager get() {
		return Core.getInstance().getManager();
	}
}
