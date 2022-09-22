package me.hpets.commands;

import org.bukkit.Sound;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.hpets.Core;
import me.hpets.objects.PlayerPet;

public class CommandPet implements CommandExecutor {

	public CommandPet() {
		Core.getInstance().getCommand("pet").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender s, Command c, String lb, String[] args) {
		if (!(s instanceof Player)) return false;
		Player p = (Player) s;
		p.playSound(p.getLocation(), Sound.NOTE_BASS_GUITAR, 0.5f, 10);
		String tag = Core.getInstance().getTag();
		PlayerPet pp = PlayerPet.check(p);
		if (args.length == 1) {
			if (p.hasPermission("H_Pets.Adm")) {
				if (args[0].equalsIgnoreCase("reload")) {
					Core.getInstance().reloadPlugin();
					tag = Core.getInstance().getTag();
					p.sendMessage(tag + " §aPlugin recarregado com sucesso!");
					return false;
				}
			}
			if (args[0].equalsIgnoreCase("delete")) {
				pp.getPet().delete();
				return false;
			}
		}
		pp.showPet();
		
		return false;
	}
	
}
