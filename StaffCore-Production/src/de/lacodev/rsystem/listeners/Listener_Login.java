package de.lacodev.rsystem.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

import de.lacodev.rsystem.Main;
import de.lacodev.rsystem.utils.BanManager;

public class Listener_Login implements Listener {

	@EventHandler
	public void onLogin(PlayerLoginEvent e) {
		Player p = e.getPlayer();
		
		if(BanManager.isBanned(p.getUniqueId().toString())) {
			
			String listString = "";
			if(BanManager.getBanEnd(p.getUniqueId().toString()) != -1) {
				for(String s : Main.getInstance().getConfig().getStringList("Layout.Ban.LAYOUT")) {
					listString += ChatColor.translateAlternateColorCodes('&', s).replace("%reason%", BanManager.getBanReason(p.getUniqueId().toString())).replace("%remaining%", BanManager.getBanFinalEnd(p.getUniqueId().toString())).replace("%lengthvalue%", Main.getMSG("Layout.Ban.Length-Values.Temporarly")) + "\n";
				}
			} else {
				for(String s : Main.getInstance().getConfig().getStringList("Layout.Ban.LAYOUT")) {
					listString += ChatColor.translateAlternateColorCodes('&', s).replace("%reason%", BanManager.getBanReason(p.getUniqueId().toString())).replace("%remaining%", Main.getMSG("Layout.Ban.Length-Values.Permanently")).replace("%lengthvalue%", Main.getMSG("Layout.Ban.Length-Values.Permanently")) + "\n";
				}
			}
			
			e.disallow(Result.KICK_BANNED, listString);
		} else {
			if(!BanManager.isIPBanned(e.getRealAddress().toString())) {
				if(p.isOp()) {
					if(!p.hasPermission(Main.getPermissionNotice("Permissions.Allow-OP.Join"))) {
						e.disallow(Result.KICK_OTHER, Main.getMSG("Unpermitted-OP.Kick-Player.Message"));
						for(Player all : Bukkit.getOnlinePlayers()) {
							if(all.hasPermission(Main.getPermissionNotice("Permissions.Allow-OP.Notify"))) {
								all.sendMessage(Main.getPrefix() + Main.getMSG("Unpermitted-OP.Staff-Notify").replace("%target%", p.getName()));
							}
						}
					}
				}
			} else {
				e.disallow(Result.KICK_BANNED, Main.getMSG("IP-Ban.Kick-Message"));
			}
		}
	}
	
}
