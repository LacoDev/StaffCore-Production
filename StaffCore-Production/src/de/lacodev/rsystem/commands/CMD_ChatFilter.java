package de.lacodev.rsystem.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.lacodev.rsystem.Main;
import de.lacodev.rsystem.utils.InventoryHandler;

public class CMD_ChatFilter implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			
			Player p = (Player)sender;
			
			if(p.hasPermission(Main.getPermissionNotice("Permissions.Chatfilter.Manage"))) {
				if(args.length == 0) {
					InventoryHandler.openChatFilterSettings(p);
				} else {
					p.sendMessage(Main.getPrefix() + "§cToo many arguments");
				}
			}
			
		} else {
			Bukkit.getConsoleSender().sendMessage("");
			Bukkit.getConsoleSender().sendMessage("§cSystem §8» §c§lFAILED §8(§7Console-Input§8)");
			Bukkit.getConsoleSender().sendMessage("");
		}
		return true;
	}

}
