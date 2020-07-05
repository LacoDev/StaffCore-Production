package de.lacodev.rsystem.completer;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class Completer_BanManager implements TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {

		ArrayList<String> manager = new ArrayList<>();
		
		manager.add("addreason");
		manager.add("removereason");
		
		if(args.length == 1) {
			return manager;
		} else {
			manager.clear();
			return manager;
		}
		
	}

}
