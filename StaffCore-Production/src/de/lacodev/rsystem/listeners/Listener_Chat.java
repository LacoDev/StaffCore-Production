package de.lacodev.rsystem.listeners;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import de.lacodev.rsystem.Main;
import de.lacodev.rsystem.utils.BanManager;
import de.lacodev.rsystem.utils.InventoryHandler;
import de.lacodev.rsystem.utils.ReportManager;

public class Listener_Chat implements Listener {
	
	public static HashMap<Player, Long> spam = new HashMap<>();
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		
		if(BanManager.isMuted(p.getUniqueId().toString())) {
			
			String listString = "";
			for(String s : Main.getInstance().getConfig().getStringList("Layout.Mute.LAYOUT")) {
				listString += ChatColor.translateAlternateColorCodes('&', s).replace("%reason%", BanManager.getMuteReason(p.getUniqueId().toString())).replace("%remaining%", BanManager.getMuteFinalEnd(p.getUniqueId().toString())) + "\n";
			}
			
			p.sendMessage(listString);
			e.setCancelled(true);
			
		}
		
		File file = new File("plugins//" + Main.getInstance().getDescription().getName() + "//chatfilter.yml");
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		
		if(cfg.contains("Cursed-Words")) {
			if(wordIsBlocked(e.getMessage())) {
				if(Main.getInstance().getConfig().getBoolean("Chatfilter.Cursed-Words.AutoReport.Enable")) {
					if(Main.getInstance().getConfig().getBoolean("Chatfilter.Cursed-Words.Block-Message.Enable")) {
						ReportManager.createReport(Main.getPermissionNotice("Chatfilter.ReporterName"), p, Main.getPermissionNotice("Chatfilter.Cursed-Words.AutoReport.Reason"));
						e.setCancelled(true);
						p.sendMessage(Main.getPrefix() + Main.getMSG("Chatfilter.Cursed-Words.Block-Message.Message"));
					} else {
						ReportManager.createReport(Main.getPermissionNotice("Chatfilter.ReporterName"), p, Main.getPermissionNotice("Chatfilter.Cursed-Words.AutoReport.Reason"));
					}
				} else {
					if(Main.getInstance().getConfig().getBoolean("Chatfilter.Cursed-Words.Block-Message")) {
						e.setCancelled(true);
						p.sendMessage(Main.getPrefix() + Main.getMSG("Chatfilter.Cursed-Words.Block-Message.Message"));
					}
				}
			}
		}
		
		if(InventoryHandler.filter.contains(p)) {
			
			ArrayList<String> cursed = (ArrayList<String>) cfg.getStringList("Cursed-Words");
			
			if(!cursed.contains(e.getMessage())) {
				e.setCancelled(true);
				cursed.add(e.getMessage());
				cfg.set("Cursed-Words", cursed);
				try {
					cfg.save(file);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				p.sendMessage(Main.getPrefix() + "§7Word §aadded §7to the Cursed-Words");
			} else {
				e.setCancelled(true);
			}
			
		}
		
		ArrayList<String> ad = (ArrayList<String>) Main.getInstance().getConfig().getStringList("Chatfilter.Advertisment.Whitelist");
		ArrayList<String> blocked = (ArrayList<String>) Main.getInstance().getConfig().getStringList("Chatfilter.Advertisment.Blocked-Domains");
		
		String[] adtest = e.getMessage().split(" ");
		
		for(String ads : adtest) {
			if(!ad.contains(ads)) {
				if(blocked.contains(ads)) {
					if(Main.getInstance().getConfig().getBoolean("Chatfilter.Advertisment.AutoReport.Enable")) {
						if(Main.getInstance().getConfig().getBoolean("Chatfilter.Advertisment.Block-Message.Enable")) {
							ReportManager.createReport(Main.getPermissionNotice("Chatfilter.ReporterName"), p, Main.getPermissionNotice("Chatfilter.Advertisment.AutoReport.Reason"));
							e.setCancelled(true);
							p.sendMessage(Main.getPrefix() + Main.getMSG("Chatfilter.Advertisment.Block-Message.Message"));
						} else {
							ReportManager.createReport(Main.getPermissionNotice("Chatfilter.ReporterName"), p, Main.getPermissionNotice("Chatfilter.Advertisment.AutoReport.Reason"));
						}
					} else {
						if(Main.getInstance().getConfig().getBoolean("Chatfilter.Advertisment.Block-Message")) {
							e.setCancelled(true);
							p.sendMessage(Main.getPrefix() + Main.getMSG("Chatfilter.Advertisment.Block-Message.Message"));
						}
					}
				}
			}
		}
	}

	private boolean wordIsBlocked(String message) {
		File file = new File("plugins//" + Main.getInstance().getDescription().getName() + "//chatfilter.yml");
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		
		ArrayList<String> cursed = (ArrayList<String>) cfg.getStringList("Cursed-Words");
		
		for(int i = 0; i < cursed.size(); i++) {
			int minpercent = Main.getInstance().getConfig().getInt("Chatfilter.Cursed-Words.Match-for-action-in-Percentage");
			String[] msg = message.split(" ");
			for(String m : msg) {
				if(cursed.get(i).toLowerCase().contains(m.toLowerCase())) {
					double total = cursed.get(i).length();
					double score = m.length();
					float percentage = (float) ((score * 100) / total);
					if(percentage > minpercent) {
						return true;
					}
				}
			}
		}
		return false;
	}

	@EventHandler
	public void onReport(PlayerCommandPreprocessEvent e) {
		Player p = e.getPlayer();
		
		if(!e.getMessage().startsWith("/report templates")) {
			if(e.getMessage().startsWith("/report ")) {
				if(spam.containsKey(p)) {
					if(spam.get(p) > System.currentTimeMillis()) {
						p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Report.AntiSpam"));
						e.setCancelled(true);
					}
				}
			}
		}
	}
	
}
