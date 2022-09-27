package me.hpets.commands;

import org.bukkit.Sound;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.hpets.Core;
import me.hpets.objects.PlayerPet;
import me.hpets.objects.petutils.enums.StatusPet;

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
				p.sendMessage("deletado");
				return false;
			}
		}
		if (args.length == 2) {
			if (args[0].equalsIgnoreCase("status")) {
				try {
					pp.getPet().getMode().setStatus(StatusPet.valueOf(args[1].toUpperCase()));
					p.sendMessage("setado para " + args[1]);
				} catch (Exception e) {
					p.sendMessage("erro");
				}
				return false;
			}
		}
		pp.showPet();
		
		return false;
	}
	
}
