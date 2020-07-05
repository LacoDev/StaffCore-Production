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

public class CMD_Unmute implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			
			Player p = (Player)sender;
			
			if(MySQL.isConnected()) {
				if( p.hasPermission(Main.getPermissionNotice("Permissions.Everything")) || p.hasPermission(Main.getPermissionNotice("Permissions.UnMute.Use")) ) {
					if( args.length == 1 ) {
						if(SystemManager.getUUIDByName(args[0]) != null) {
							if(BanManager.isMuted(SystemManager.getUUIDByName(args[0]))) {
								if(BanManager.unmute(SystemManager.getUUIDByName(args[0]))) {
									ReportManager.sendNotify("UNMUTE", p.getName(), SystemManager.getUUIDByName(args[0]), null);
									p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.UnMute.Success"));
								} else {
									p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.UnMute.Error"));
								}
							} else {
								p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.UnMute.Not-Muted"));
							}
						} else {
							p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.UnMute.Cannot-find-player"));
						}
					} else {
						p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.UnMute.Usage"));
					}
				}
			} else {
				p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.No-Connection.Notify"));
			}
			
		} else {
			if(MySQL.isConnected()) {
				if( args.length == 1 ) {
					if(SystemManager.getUUIDByName(args[0]) != null) {
						if(BanManager.isMuted(SystemManager.getUUIDByName(args[0]))) {
							if(BanManager.unmute(SystemManager.getUUIDByName(args[0]))) {
								ReportManager.sendNotify("UNMUTE", "Console", SystemManager.getUUIDByName(args[0]), null);
								sender.sendMessage(Main.getPrefix() + Main.getMSG("Messages.UnMute.Success"));
							} else {
								sender.sendMessage(Main.getPrefix() + Main.getMSG("Messages.UnMute.Error"));
							}
						} else {
							sender.sendMessage(Main.getPrefix() + Main.getMSG("Messages.UnMute.Not-Muted"));
						}
					} else {
						sender.sendMessage(Main.getPrefix() + Main.getMSG("Messages.UnMute.Cannot-find-player"));
					}
				} else {
					sender.sendMessage(Main.getPrefix() + Main.getMSG("Messages.UnMute.Usage"));
				}
			} else {
				sender.sendMessage(Main.getPrefix() + Main.getMSG("Messages.No-Connection.Notify"));
			}
		}
		return true;
	}

}
