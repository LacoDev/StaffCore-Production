package de.lacodev.rsystem.completer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import de.lacodev.rsystem.mysql.MySQL;

public class Completer_Mute implements TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		
		

		if(args.length == 2) {

			ArrayList<String> reasons = new ArrayList<>();



			ResultSet rs = MySQL.getResult("SELECT * FROM ReportSystem_reasonsdb WHERE TYPE = 'MUTE'");



			try {

				while(rs.next()) {

					reasons.add(rs.getString("NAME").toLowerCase());

				}

				return reasons;

			} catch (SQLException e) {

				e.printStackTrace();

			}

		} else {

			return null;

		}

		return null;
		
	}

}
