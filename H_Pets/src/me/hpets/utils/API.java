package me.hpets.utils;

import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

import me.hawkcore.tasks.Task;
import me.hpets.Core;
import me.hpets.objects.Pet;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;

public class API {

	public static API get() {
		return Core.getInstance().getApi();
	}
	
	public void playEffectUp(Pet pet) {
		if (!pet.isValid()) return;
		Entity entity = pet.getEntity();
		Location loc = entity.getLocation().clone();
		World world = loc.getWorld();
		Firework fire = (Firework) world.spawnEntity(loc, EntityType.FIREWORK);
		FireworkMeta meta = fire.getFireworkMeta();
		meta.setPower(1);
		meta.addEffect(FireworkEffect.builder().flicker(true).withColor(Color.fromBGR(new Random().nextInt(255), new Random().nextInt(255), new Random().nextInt(255))).build());
		fire.setFireworkMeta(meta);
		Task task = new Task();
		task.setRunnable(new Runnable() {
			int total = 0;
			double y = 0; 
			float raio = 0.5f;
			Location location = loc.clone();
			@Override
			public void run() {
				if (total < 10) {
					List<Location> locs = me.hawkcore.utils.API.get().getLocsAround(location, raio);
					for (int i = 0; i < locs.size(); i++) {
						if (i%2!=0)continue;
						Location loc = locs.get(i);
						PacketPlayOutWorldParticles a = new PacketPlayOutWorldParticles(EnumParticle.FLAME, false, (float)loc.getX(), (float)(loc.getY()+y), (float)loc.getZ(), 0, 0, 0, 0, 1);
						for(Player p : Bukkit.getOnlinePlayers()) {
							if (p.getWorld().equals(loc.getWorld())) {
								((CraftPlayer)p).getHandle().playerConnection.sendPacket(a);
							}
						}
					}
					y+=0.25;
					raio+=0.3;
					total+=1;
				} else task.cancel();
			}
		}).run(1);
	}
	
}
