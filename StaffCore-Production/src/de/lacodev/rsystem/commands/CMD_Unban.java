package de.lacodev.rsystem.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.lacodev.rsystem.Main;
import de.lacodev.rsystem.mysql.MySQL;
import de.lacodev.rsystem.utils.BanManager;
import de.lacodev.rsystem.utils.ReportManager;
import de.lacodev.rsystem.utils.SystemManager;

public class CMD_Unban implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			
			Player p = (Player)sender;
			
			if(MySQL.isConnected()) {
				if( p.hasPermission(Main.getPermissionNotice("Permissions.Everything")) || p.hasPermission(Main.getPermissionNotice("Permissions.UnBan.Use")) ) {
					if( args.length == 1 ) {
						if(SystemManager.getUUIDByName(args[0]) != null) {
							if(BanManager.isBanned(SystemManager.getUUIDByName(args[0]))) {
								if(BanManager.unban(SystemManager.getUUIDByName(args[0]))) {
									ReportManager.sendNotify("UNBAN", p.getName(), args[0], null);
									p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.UnBan.Success"));
								} else {
									p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.UnBan.Error"));
								}
							} else {
								p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.UnBan.Not-Banned"));
							}
						} else {
							p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.UnBan.Cannot-find-player"));
						}
					} else {
						p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.UnBan.Usage"));
					}
				}
			} else {
				p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.No-Connection.Notify"));
			}
			
		} else {
			if(MySQL.isConnected()) {
				if( args.length == 1 ) {
					if(SystemManager.getUUIDByName(args[0]) != null) {
						if(BanManager.isBanned(SystemManager.getUUIDByName(args[0]))) {
							if(BanManager.unban(SystemManager.getUUIDByName(args[0]))) {
								ReportManager.sendNotify("UNBAN", "Console", args[0], null);
								sender.sendMessage(Main.getPrefix() + Main.getMSG("Messages.UnBan.Success"));
							} else {
								sender.sendMessage(Main.getPrefix() + Main.getMSG("Messages.UnBan.Error"));
							}
						} else {
							sender.sendMessage(Main.getPrefix() + Main.getMSG("Messages.UnBan.Not-Banned"));
						}
					} else {
						sender.sendMessage(Main.getPrefix() + Main.getMSG("Messages.UnBan.Cannot-find-player"));
					}
				} else {
					sender.sendMessage(Main.getPrefix() + Main.getMSG("Messages.UnBan.Usage"));
				}
			} else {
				sender.sendMessage(Main.getPrefix() + Main.getMSG("Messages.No-Connection.Notify"));
			}
		}
		return true;
	}

}
