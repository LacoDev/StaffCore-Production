package de.lacodev.rsystem.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.entity.Player;

import de.lacodev.rsystem.mysql.MySQL;

public class WebVerifier {

	public static boolean validateToken(Player p, String code) {
		if(MySQL.isConnected()) {
			ResultSet rs = MySQL.getResult("SELECT * FROM ReportSystem_webaccounts WHERE VerifyToken = '"+ code +"' AND PLAYERNAME = '"+ p.getName() +"'");
			try {
				if(rs != null) {
					if(rs.next()) {
						if(rs.getInt("USERRANK") == 0) {
							return true;
						} else {
							return false;
						}
					}
				}
			} catch (SQLException e) {
				return false;
			}
		} else {
			return false;
		}
		return false;
	}
	
	public static void verifyAccount(Player p, String code) {
		if(MySQL.isConnected()) {
			MySQL.update("UPDATE ReportSystem_webaccounts SET UUID = '"+ p.getUniqueId().toString() +"',USERRANK = '1' WHERE PLAYERNAME = '"+ p.getName() +"' AND VerifyToken = '"+ code +"'");
		}
	}

}
