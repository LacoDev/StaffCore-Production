package de.lacodev.rsystem.commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.connorlinfoot.actionbarapi.ActionBarAPI;

import de.lacodev.rsystem.Main;
import de.lacodev.rsystem.mysql.MySQL;
import de.lacodev.rsystem.utils.ReportManager;

public class CMD_ReportManager implements CommandExecutor {

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			
			Player p = (Player)sender;
			
			if(MySQL.isConnected()) {
				if(args.length == 2) {
					if(args[0].toLowerCase().equalsIgnoreCase("addreason")) {
						if(p.hasPermission(Main.getPermissionNotice("Permissions.ReportManager.addreason")) || p.hasPermission(Main.getPermissionNotice("Permissions.Everything"))) {
							if(Bukkit.getVersion().contains("(MC: 1.8.8)")) {
								if(p.getItemInHand().getType() != Material.AIR) {
									if(!ReportManager.existsReportReason(args[1])) {
										ReportManager.createReportReason(args[1], p.getItemInHand());
										if(Main.getInstance().actionbar) {
											ActionBarAPI.sendActionBar(p, Main.getPrefix() + Main.getMSG("Messages.ReportManager.Add-Reason.Successful"));
										} else {
											p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.ReportManager.Add-Reason.Successful"));
										}
									} else {
										if(Main.getInstance().actionbar) {
											ActionBarAPI.sendActionBar(p, Main.getPrefix() + Main.getMSG("Messages.ReportManager.Add-Reason.Already-Exists"));
										} else {
											p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.ReportManager.Add-Reason.Already-Exists"));
										}
									}
								} else {
									if(Main.getInstance().actionbar) {
										ActionBarAPI.sendActionBar(p, Main.getPrefix() + Main.getMSG("Messages.ReportManager.Add-Reason.No-Item-In-Hand"));
									} else {
										p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.ReportManager.Add-Reason.No-Item-In-Hand"));
									}
								}
							} else {
								if(p.getInventory().getItemInMainHand().getType() != Material.AIR) {
									if(!ReportManager.existsReportReason(args[1])) {
										ReportManager.createReportReason(args[1], p.getInventory().getItemInMainHand());
										if(Main.getInstance().actionbar) {
											ActionBarAPI.sendActionBar(p, Main.getPrefix() + Main.getMSG("Messages.ReportManager.Add-Reason.Successful"));
										} else {
											p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.ReportManager.Add-Reason.Successful"));
										}
									} else {
										if(Main.getInstance().actionbar) {
											ActionBarAPI.sendActionBar(p, Main.getPrefix() + Main.getMSG("Messages.ReportManager.Add-Reason.Already-Exists"));
										} else {
											p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.ReportManager.Add-Reason.Already-Exists"));
										}
									}
								} else {
									if(Main.getInstance().actionbar) {
										ActionBarAPI.sendActionBar(p, Main.getPrefix() + Main.getMSG("Messages.ReportManager.Add-Reason.No-Item-In-Hand"));
									} else {
										p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.ReportManager.Add-Reason.No-Item-In-Hand"));
									}
								}
							}
						}
					} else if(args[0].toLowerCase().equalsIgnoreCase("removereason")) {
						if(p.hasPermission(Main.getPermissionNotice("Permissions.ReportManager.removereason")) || p.hasPermission(Main.getPermissionNotice("Permissions.Everything"))) {
							if(ReportManager.existsReportReason(args[1])) {
								ReportManager.deleteReportReason(args[1]);
								if(Main.getInstance().actionbar) {
									ActionBarAPI.sendActionBar(p, Main.getPrefix() + Main.getMSG("Messages.ReportManager.Remove-Reason.Successful"));
								} else {
									p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.ReportManager.Remove-Reason.Successful"));
								}
							} else {
								if(Main.getInstance().actionbar) {
									ActionBarAPI.sendActionBar(p, Main.getPrefix() + Main.getMSG("Messages.ReportManager.Remove-Reason.Not-Exists"));
								} else {
									p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.ReportManager.Remove-Reason.Not-Exists"));
								}
							}
						}
					} else {
						p.sendMessage("");
						p.sendMessage(Main.getPrefix() + "§8[§eReportManager§8]");
						p.sendMessage("");
						p.sendMessage(Main.getPrefix() + "§7/reportmanager addreason <Reason>");
						p.sendMessage(Main.getPrefix() + "§7/reportmanager removereason <Reason>");
						p.sendMessage("");
					}
				} else {
					p.sendMessage("");
					p.sendMessage(Main.getPrefix() + "§8[§eReportManager§8]");
					p.sendMessage("");
					p.sendMessage(Main.getPrefix() + "§7/reportmanager addreason <Reason>");
					p.sendMessage(Main.getPrefix() + "§7/reportmanager removereason <Reason>");
					p.sendMessage("");
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

}
