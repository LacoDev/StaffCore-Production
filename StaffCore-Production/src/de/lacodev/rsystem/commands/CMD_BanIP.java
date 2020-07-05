package de.lacodev.rsystem.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.lacodev.rsystem.Main;
import de.lacodev.rsystem.utils.BanManager;

public class CMD_BanIP implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			
			Player p = (Player)sender;
			
			if(p.hasPermission(Main.getPermissionNotice("Permissions.Everything")) || p.hasPermission(Main.getPermissionNotice("Permissions.IpBan.Use"))) {
				if(args.length == 1) {
					Player target = Bukkit.getPlayer(args[0]);
					if(target != null) {
						if(!BanManager.isIPBanned(target.getAddress().getAddress().toString())) {
							BanManager.banIPAddress(target);
							target.kickPlayer(Main.getMSG("IP-Ban.Kick-Message"));
							for(Player all : Bukkit.getOnlinePlayers()) {
								if(all.hasPermission(Main.getPermissionNotice("Permissions.IpBan.Notify"))) {
									all.sendMessage(Main.getPrefix() + Main.getMSG("IP-Ban.Notify").replace("%player%", p.getName()).replace("%target%", target.getName()).replace("%duration%", 
											Main.getInstance().getConfig().getInt("IP-Ban.Duration-in-Hours") + " " + Main.getMSG("Layout.Ban.Remaining.Hours")));
								}
							}
						} else {
							p.sendMessage(Main.getPrefix() + Main.getMSG("IP-Ban.Already-banned").replace("%target%", target.getName()));
						}
					} else {
						p.sendMessage(Main.getPrefix() + Main.getMSG("IP-Ban.Target-Offline"));
					}
				} else {
					p.sendMessage(Main.getPrefix() + Main.getMSG("IP-Ban.Usage"));
				}
			}
			
		}
		return true;
	}

}
