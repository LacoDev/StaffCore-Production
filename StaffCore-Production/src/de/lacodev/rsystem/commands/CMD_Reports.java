package de.lacodev.rsystem.commands;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.lacodev.rsystem.Main;
import de.lacodev.rsystem.mysql.MySQL;
import de.lacodev.rsystem.utils.SystemManager;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class CMD_Reports implements CommandExecutor {
	
	public ArrayList<String> reported = new ArrayList<>();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			
			Player p = (Player)sender;
			
			if(MySQL.isConnected()) {
				if( p.hasPermission(Main.getPermissionNotice("Permissions.Reports.See")) || p.hasPermission(Main.getPermissionNotice("Permissions.Everything")) ) {
					
					if( args.length == 0 ) {
						
						// Get all Reported Players
						try {
							ResultSet rs = MySQL.getResult("SELECT REPORTED_UUID FROM ReportSystem_reportsdb WHERE STATUS = '0'");
							if(rs.next()) {
								if(!reported.contains(rs.getString("REPORTED_UUID"))) {
									reported.add(rs.getString("REPORTED_UUID"));
								}
								while(rs.next()) {
									if(!reported.contains(rs.getString("REPORTED_UUID"))) {
										reported.add(rs.getString("REPORTED_UUID"));
									}
								}
								
								p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Reports.Title"));
								p.sendMessage("");
								
								for(int i = 0; i < reported.size(); i++) {
									TextComponent tc = new TextComponent();
									tc.setText(Main.getPrefix() + Main.getMSG("Messages.Reports.Layout").replace("%target%", SystemManager.getUsernameByUUID(reported.get(i))).replace("%status%", getStatusByUUID(reported.get(i))));
									tc.setClickEvent(new ClickEvent(Action.RUN_COMMAND,"/report claim " + reported.get(i)));
												
									String listString = Main.getMSG("Messages.Reports.Layout-Hover-Title") + "\n\n";
									for(String s : getReportReasonsFromPlayer(reported.get(i))) {
										listString += "§8- §e"+ s + "\n";
									}
									
									tc.setHoverEvent(new HoverEvent(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(listString).create()));
									
									p.spigot().sendMessage(tc);
								}
								
								p.sendMessage("");
							} else {
								p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Reports.No-Open"));
							}
						} catch (SQLException e) {
							e.printStackTrace();
						}
					} else {
						p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Reports.Usage"));
					}
				}
			} else {
				p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.No-Connection.Notify"));
			}
		}
		return true;
	}
	
	private ArrayList<String> getReportReasonsFromPlayer(String uuid) {
		ArrayList<String> r = new ArrayList<>();
		
		ResultSet rs = MySQL.getResult("SELECT REASON FROM ReportSystem_reportsdb WHERE STATUS = '0' AND REPORTED_UUID = '"+ uuid +"'");
		try {
			while(rs.next()) {
				if( !r.contains(rs.getString("REASON")) ) {
					r.add(rs.getString("REASON"));
				}
			}
			return r;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return r;
	}

	private String getStatusByUUID(String string) {
		Player target = Bukkit.getPlayer(SystemManager.getUsernameByUUID(string));
		
		if(target != null) {
			return "§aOnline";
		} else {
			return "§cOffline";
		}
	}

}
