package de.lacodev.rsystem.commands;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.lacodev.rsystem.Main;
import de.lacodev.rsystem.listeners.Listener_Chat;
import de.lacodev.rsystem.mysql.MySQL;
import de.lacodev.rsystem.utils.ReportManager;

public class CMD_Report implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			
			Player p = (Player)sender;
			
			if(MySQL.isConnected()) {
				if(args.length == 0) {
					sendHelp(p);
				} else if(args.length == 1) {
					if(args[0].toLowerCase().equalsIgnoreCase("templates")) {
						p.sendMessage("");
						p.sendMessage(Main.getPrefix() + "§eReport Templates");
						p.sendMessage("");
						ArrayList<String> reasons = ReportManager.getReportReasons();
						
						if(reasons.size() > 0) {
							for(int i = 0; i < reasons.size(); i++) {
								p.sendMessage(Main.getPrefix() + "§8- §e" + reasons.get(i));
							}
						} else {
							p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Report.No-Reportreasons"));
						}
						p.sendMessage("");
					} else {
						Player target = Bukkit.getPlayer(args[0]);
						
						if(target != null) {
							if(target != p) {
								ReportManager.openPagedReportInv(p, target.getName(), 1);
							} else {
								p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Report.Cannot-report-self"));
							}
						} else {
							p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Report.Target-offline"));
						}
					}
				} else if(args.length == 2) {
					if(args[0].toLowerCase().equalsIgnoreCase("claim")) {

						if(p.hasPermission(Main.getPermissionNotice("Permissions.Report.Claim")) || p.hasPermission(Main.getPermissionNotice("Permissions.Everything"))) {
							ReportManager.claimReport(p, args[1]);
						}
						
					} else {
						ArrayList<String> reasons = ReportManager.getReportReasons();
						
						if(reasons.contains(args[1].toLowerCase())) {
							Player target = Bukkit.getPlayer(args[0]);
							
							if(target != null) {
								if(target != p) {
									ReportManager.createReport(p.getName(), target, args[1]);
									if(!p.hasPermission(Main.getPermissionNotice("Permissions.Everything")) || !p.hasPermission(Main.getPermissionNotice("Permissions.Report.Spam.Bypass"))) {
										Listener_Chat.spam.put(p, System.currentTimeMillis() + Main.getInstance().getConfig().getInt("Report-Spam.Duration-in-Seconds") * 1000);
									}
								} else {
									p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Report.Cannot-report-self"));
								}
							} else {
								p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Report.Target-offline"));
							}
						} else {
							sendHelp(p);
						}
					}
				} else {
					sendHelp(p);
				}
			} else {
				p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.No-Connection.Notify"));
			}
			
		} else {
			Bukkit.getConsoleSender().sendMessage("");
			Bukkit.getConsoleSender().sendMessage("§cSystem §8» §c§lFAILED §8(§7Console-Input§8)");
			Bukkit.getConsoleSender().sendMessage("");
		}
		return true;
	}
	
	public static void sendHelp(Player p) {
		p.sendMessage("");
		p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Report.Usage.Title"));
		p.sendMessage("");
		p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Report.Usage.Command"));
		p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Report.Usage.Inventory"));
		p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Report.Usage.Template"));
		p.sendMessage("");
	}

}
