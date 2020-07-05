package de.lacodev.rsystem.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.lacodev.rsystem.Main;
import de.lacodev.rsystem.utils.WebVerifier;

public class CMD_WebVerify implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			
			Player p = (Player)sender;
			
			if(args.length == 1) {
				String code = args[0];
				
				if(WebVerifier.validateToken(p,code)) {
					WebVerifier.verifyAccount(p, code);
					p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.WebVerify.Successfully-Verified"));	
				} else {
					p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.WebVerify.Invalid-Token"));	
				}
			} else {
				p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.WebVerify.Usage"));
			}
			
		}
		return true;
	}

}
