package de.lacodev.rsystem.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.lacodev.rsystem.Main;
import de.lacodev.staffcore.api.StaffCoreAPI;

public class CMD_StaffCore implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if(sender instanceof Player) {
			
			Player p = (Player)sender;
			
			if(args.length == 1) {
				if(args[0].toLowerCase().equalsIgnoreCase("reload")) {
					if(p.hasPermission(Main.getPermissionNotice("Permissions.Everything")) || p.hasPermission(Main.getPermissionNotice("Permissions.System.Reload"))) {
						if(Main.getInstance().getConfig().getBoolean("General.Restart-Plugin-On-Reload")) {
							try {
								Class.forName("de.lacodev.staffcore.api.StaffCoreAPI");
								
								StaffCoreAPI.restartStaffCore();
								
								p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Reload.Success"));
							} catch (ClassNotFoundException e) {
								p.sendMessage(Main.getPrefix() + "§cYou need the §eStaffCore-Restarter §cto reload the plugin properly");
							}
						} else {
							Main.getInstance().reloadConfig();
							p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Reload.Success"));
						}
					}
				}
			} else {
				p.sendMessage(Main.getPrefix() + "§7" + Main.getInstance().getDescription().getName() + " §8(§7LacoDev and Esmaybe§8)");
				if(Main.getInstance().latest) {
					p.sendMessage(Main.getPrefix() + "§7Plugin Version §8» " + Main.getInstance().getDescription().getVersion() + " §8(§aUp to date§8)");
				} else {
					p.sendMessage(Main.getPrefix() + "§7Plugin Version §8» " + Main.getInstance().getDescription().getVersion() + " §8(§cOutdated§8)");
				}
			}
			
		} else {
			if(args.length == 1) {
				if(args[0].toLowerCase().equalsIgnoreCase("reload")) {
					if(Main.getInstance().getConfig().getBoolean("General.Restart-Plugin-On-Reload")) {
						try {
							Class.forName("de.lacodev.staffcore.api.StaffCoreAPI");
							
							StaffCoreAPI.restartStaffCore();
							
							sender.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Reload.Success"));
						} catch (ClassNotFoundException e) {
							sender.sendMessage(Main.getPrefix() + "§cYou need the §eStaffCore-Restarter §cto reload the plugin properly");
						}
					} else {
						Main.getInstance().reloadConfig();
						sender.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Reload.Success"));
					}
				}
			} else {
				sender.sendMessage(Main.getPrefix() + "§7" + Main.getInstance().getDescription().getName() + " §8(§7LacoDev and Esmaybe§8)");
				if(Main.getInstance().latest) {
					sender.sendMessage(Main.getPrefix() + "§7Plugin Version §8» " + Main.getInstance().getDescription().getVersion() + " §8(§aUp to date§8)");
				} else {
					sender.sendMessage(Main.getPrefix() + "§7Plugin Version §8» " + Main.getInstance().getDescription().getVersion() + " §8(§cOutdated§8)");
				}
			}
		}
		
		return true;
	}

}
