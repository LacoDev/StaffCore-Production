package de.lacodev.rsystem.commands;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import de.lacodev.rsystem.Main;
import de.lacodev.rsystem.mysql.MySQL;
import de.lacodev.rsystem.utils.SystemManager;

public class CMD_BanList implements CommandExecutor {
	
	public ArrayList<String> banned_players = new ArrayList<>();
	public HashMap<String, String> ban_reasons = new HashMap<>();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			
			Player p = (Player)sender;
			
			if(p.hasPermission(Main.getPermissionNotice("Permissions.Everything")) || p.hasPermission(Main.getPermissionNotice("Permissions.Ban.List"))) {
					
				if(MySQL.isConnected()) {
					new BukkitRunnable() {

						@Override
						public void run() {
							ResultSet rs = MySQL.getResult("SELECT * FROM ReportSystem_bansdb");
							try {
								if(rs.next()) {
									if(rs.getFetchSize() > 1) {
										while(rs.next()) {
											if(!banned_players.contains(SystemManager.getUsernameByUUID(rs.getString("BANNED_UUID")))) {
												banned_players.add(SystemManager.getUsernameByUUID(rs.getString("BANNED_UUID")));
												ban_reasons.put(SystemManager.getUsernameByUUID(rs.getString("BANNED_UUID")), rs.getString("REASON"));
											}
										}
									} else {
										if(!banned_players.contains(SystemManager.getUsernameByUUID(rs.getString("BANNED_UUID")))) {
											banned_players.add(SystemManager.getUsernameByUUID(rs.getString("BANNED_UUID")));
											ban_reasons.put(SystemManager.getUsernameByUUID(rs.getString("BANNED_UUID")), rs.getString("REASON"));	
										}
									}
									p.sendMessage("");
									for(int i = 0; i < banned_players.size(); i++) {
										p.sendMessage(Main.getPrefix() + "§8- §e" + banned_players.get(i) + " §8» §7" + ban_reasons.get(banned_players.get(i)));
									}
									p.sendMessage("");
								} else {
									p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Ban.List.Empty"));	
								}
							} catch (SQLException e) {
								Bukkit.getConsoleSender().sendMessage("");
								Bukkit.getConsoleSender().sendMessage("§cSystem §8» §c§lFAILED §8(§7Fetch BanList§8)");
								Bukkit.getConsoleSender().sendMessage("");
							}
						}
					}.runTaskAsynchronously(Main.getInstance());
				} else {
					p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.No-Connection.Notify"));
				}
			}
			
		} else {
			if(MySQL.isConnected()) {
				new BukkitRunnable() {

					@Override
					public void run() {
						ResultSet rs = MySQL.getResult("SELECT * FROM ReportSystem_bansdb");
						try {
							if(rs.next()) {
								if(rs.getFetchSize() > 1) {
									while(rs.next()) {
										if(!banned_players.contains(SystemManager.getUsernameByUUID(rs.getString("BANNED_UUID")))) {
											banned_players.add(SystemManager.getUsernameByUUID(rs.getString("BANNED_UUID")));
											ban_reasons.put(SystemManager.getUsernameByUUID(rs.getString("BANNED_UUID")), rs.getString("REASON"));
										}
									}
								} else {
									if(!banned_players.contains(SystemManager.getUsernameByUUID(rs.getString("BANNED_UUID")))) {
										banned_players.add(SystemManager.getUsernameByUUID(rs.getString("BANNED_UUID")));
										ban_reasons.put(SystemManager.getUsernameByUUID(rs.getString("BANNED_UUID")), rs.getString("REASON"));	
									}
								}
								sender.sendMessage("");
								for(int i = 0; i < banned_players.size(); i++) {
									sender.sendMessage(Main.getPrefix() + "§8- §e" + banned_players.get(i) + " §8» §7" + ban_reasons.get(banned_players.get(i)));
								}
								sender.sendMessage("");
							} else {
								sender.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Ban.List.Empty"));	
							}
						} catch (SQLException e) {
							Bukkit.getConsoleSender().sendMessage("");
							Bukkit.getConsoleSender().sendMessage("§cSystem §8» §c§lFAILED §8(§7Fetch BanList§8)");
							Bukkit.getConsoleSender().sendMessage("");
						}
					}
				}.runTaskAsynchronously(Main.getInstance());
			} else {
				sender.sendMessage(Main.getPrefix() + Main.getMSG("Messages.No-Connection.Notify"));
			}
		}
		return true;
	}

}
