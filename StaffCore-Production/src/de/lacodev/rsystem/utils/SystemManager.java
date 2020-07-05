package de.lacodev.rsystem.utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.entity.Player;

import de.lacodev.rsystem.mysql.MySQL;

public class SystemManager {

	public static String getUsernameByUUID(String targetuuid) {
		if(MySQL.isConnected()) {
			ResultSet rs = MySQL.getResult("SELECT PLAYERNAME FROM ReportSystem_playerdb WHERE UUID = '"+ targetuuid +"'");
			try {
				if(rs != null) {
					if(rs.next()) {
						return rs.getString("PLAYERNAME");
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return "UNKNOWN";
	}
	
	public static String getUUIDByName(String targetname) {
		if(MySQL.isConnected()) {
			ResultSet rs = MySQL.getResult("SELECT UUID FROM ReportSystem_playerdb WHERE PLAYERNAME = '"+ targetname +"'");
			try {
				if(rs != null) {
					if(rs.next()) {
						return rs.getString("UUID");
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return "UNKNOWN";
	}
	
	public static void createPlayerData(Player p) {
		if(MySQL.isConnected()) {
			if(!existsPlayerData(p.getUniqueId().toString())) {
				try {
					PreparedStatement st = MySQL.getCon().prepareStatement("INSERT INTO ReportSystem_playerdb(UUID,PLAYERNAME,BANS,MUTES,REPORTS,LAST_KNOWN_IP) VALUES ('"+ p.getUniqueId().toString() +"','"+ p.getName() +"','0','0','0','"+ p.getAddress().getAddress() +"')");
					st.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} else {
				updateName(p);
			}
		}
	}

	private static void updateName(Player p) {
		if(MySQL.isConnected()) {
			try {
				PreparedStatement st = MySQL.getCon().prepareStatement("UPDATE ReportSystem_playerdb SET PLAYERNAME = '"+ p.getName() +"' WHERE UUID = '"+ p.getUniqueId().toString() +"'");
				st.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static boolean existsPlayerData(String uuid) {
		if(MySQL.isConnected()) {
			ResultSet rs = MySQL.getResult("SELECT UUID FROM ReportSystem_playerdb WHERE UUID = '"+ uuid +"'");
			try {
				if(rs != null) {
					if(rs.next()) {
						return rs.getString("UUID") != null;
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public static String getLastKnownIP(String uuidByName) {
		if(MySQL.isConnected()) {
			if(existsPlayerData(uuidByName)) {
				ResultSet rs = MySQL.getResult("SELECT LAST_KNOWN_IP FROM ReportSystem_playerdb WHERE UUID = '"+ uuidByName +"'");
				try {
					if(rs != null) {
						if(rs.next()) {
							return rs.getString("LAST_KNOWN_IP");
						}
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return "§cUNKNOWN";
	}

}
