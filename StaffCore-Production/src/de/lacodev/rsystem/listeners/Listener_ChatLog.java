package de.lacodev.rsystem.listeners;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import de.lacodev.rsystem.Main;

public class Listener_ChatLog implements Listener {
	
	public static HashMap<Player, ArrayList<String>> chatlog = new HashMap<>();
	public File file = new File("plugins//" + Main.getInstance().getDescription().getName() + "//chatlog.yml");
	public YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
	
	@EventHandler
	public void onChatLog(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		
		if(Main.getInstance().getConfig().getBoolean("ChatLog.Save-On-Reload")) {
			
			if(cfg.contains("ChatLogs." + p.getUniqueId().toString() + ".Messages")) {
				ArrayList<String> saved = (ArrayList<String>) cfg.getStringList("ChatLogs." + p.getUniqueId().toString() + ".Messages");
				
				if(saved.size() >= Main.getInstance().getConfig().getInt("ChatLog.Max-Message-Count")) {
					saved.remove(0);
					
					saved.add(e.getMessage());
					
					cfg.set("ChatLogs." + p.getUniqueId().toString() + ".Messages", saved);
					saveFile();
				} else {
					saved.add(e.getMessage());
					
					cfg.set("ChatLogs." + p.getUniqueId().toString() + ".Messages", saved);
					saveFile();
				}
				
			} else {
				ArrayList<String> newsave = new ArrayList<>();
				
				newsave.clear();
				
				newsave.add(e.getMessage());
				
				cfg.set("ChatLogs." + p.getUniqueId().toString() + ".Messages", newsave);
				saveFile();
			}
			
		} else {
			if(chatlog.containsKey(p)) {
				ArrayList<String> chat = chatlog.get(p);
				
				if(chat.size() >= Main.getInstance().getConfig().getInt("ChatLog.Max-Message-Count")) {
					chat.remove(0);
					
					chat.add(e.getMessage());
					
					chatlog.put(p, chat);
				} else {
					chat.add(e.getMessage());
					
					chatlog.put(p, chat);
				}
			} else {
				ArrayList<String> newchat = new ArrayList<>();
				newchat.clear();
				
				newchat.add(e.getMessage());
				
				chatlog.put(p, newchat);
			}
		}
	}

	private void saveFile() {

		try {
			cfg.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
