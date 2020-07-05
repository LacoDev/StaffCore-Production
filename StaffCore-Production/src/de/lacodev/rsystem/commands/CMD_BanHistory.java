package de.lacodev.rsystem.commands;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import de.lacodev.rsystem.Main;
import de.lacodev.rsystem.mysql.MySQL;
import de.lacodev.rsystem.utils.BanManager;
import de.lacodev.rsystem.utils.SystemManager;

public class CMD_BanHistory implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			
			Player p = (Player)sender;
			
			if(p.hasPermission(Main.getPermissionNotice("Permissions.Everything")) || p.hasPermission(Main.getPermissionNotice("Permissions.BanHistory.See"))) {
				if(MySQL.isConnected()) {
					
					if(args.length == 1) {
						
						if(SystemManager.existsPlayerData(SystemManager.getUUIDByName(args[0]))) {
							
							new BukkitRunnable() {

								@SuppressWarnings("deprecation")
								@Override
								public void run() {
									
									ResultSet rs = MySQL.getResult("SELECT * FROM ReportSystem_banhistory WHERE BANNED_UUID = '"+ SystemManager.getUUIDByName(args[0]) +"'");
									
									try {
										if(rs.next()) {
											p.sendMessage("");
											p.sendMessage(Main.getPrefix() + "§cBanHistory §8» §7" + args[0]);
											p.sendMessage("");
											
											while(rs.next()) {
												p.sendMessage(Main.getPrefix() + "§cBanID §8» §7" + rs.getInt("id"));
												p.sendMessage(Main.getPrefix() + "§cReason §8» §7" + rs.getString("REASON"));
												p.sendMessage(Main.getPrefix() + "§cBanned by §8» §7" + SystemManager.getUsernameByUUID(rs.getString("TEAM_UUID")));
												p.sendMessage(Main.getPrefix() + "§cBanned since §8» §7" + new Date(rs.getLong("BAN_START")).toGMTString() + " " + checkForExpiration(rs.getString("BANNED_UUID"), rs.getString("REASON"), rs.getLong("BAN_END")));
												p.sendMessage("");
											}
													
										} else {
											p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.BanHistory.No-History-Available"));
										}
										p.sendMessage("");
									} catch (SQLException e) {
										Bukkit.getConsoleSender().sendMessage("");
										Bukkit.getConsoleSender().sendMessage("§cSystem §8» §c§lFAILED §8(§7Fetch BanHistory§8)");
										Bukkit.getConsoleSender().sendMessage("");
									}
								}					
							}.runTaskAsynchronously(Main.getInstance());
							
						} else {
							p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.BanHistory.Target-Never-Joined"));
						}
						
					} else if(args.length == 2) {
						
						if(SystemManager.existsPlayerData(SystemManager.getUUIDByName(args[0]))) {
							
							if(args[1].toLowerCase().equalsIgnoreCase("clear")) {
								
								new BukkitRunnable() {

									@Override
									public void run() {
										MySQL.update("DELETE FROM ReportSystem_banhistory WHERE BANNED_UUID = '"+ SystemManager.getUUIDByName(args[0]) +"'");
									}
									
								}.runTaskAsynchronously(Main.getInstance());
								
								p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.BanHistory.Cleared"));
								
							}
							
						} else {
							p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.BanHistory.Target-Never-Joined"));
						}
						
					} else {
						p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.BanHistory.Usage"));
					}
					
				} else {
					p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.No-Connection.Notify"));
				}
			}
			
		} else {
			
		}
		
		return false;
	}

	protected String checkForExpiration(String uuid, String reason, long long1) {
		if(System.currentTimeMillis() > long1) {
			return "§8(§cExpired§8)";
		} else {
			if(BanManager.isBanned(uuid)) {
				if(BanManager.getBanReason(uuid).matches(reason)) {
					if(BanManager.getBanEnd(uuid) == -1) {
						return "§8("+ Main.getMSG("Layout.Ban.Length-Values.Permanently") +"§8)";
					} else {
						return "§8("+ BanManager.getBanFinalEnd(uuid) + "§8)";
					}
				} else {
					return "§8(§cUnbanned§8)";
				}
			} else {
				return "§8(§cUnbanned§8)";
			}
		}
	}

}
