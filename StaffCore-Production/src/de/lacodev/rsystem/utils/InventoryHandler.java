package de.lacodev.rsystem.utils;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class InventoryHandler {
	
	public static ArrayList<Player> filter = new ArrayList<>();

	public static void openChatFilterSettings(Player p) {
		if(Bukkit.getVersion().contains("(MC: 1.8") || Bukkit.getVersion().contains("(MC: 1.7")) {
			Inventory inv = p.getServer().createInventory(null, 27, "§7Settings §8- §eChatFilter");
			
			if(!filter.contains(p)) {
				inv.setItem(13, Data.buildItemStack(Material.getMaterial("WOOL"), 1, 8, "§3Add Cursed-Words §8| §7Deactivated", "§7Click to activate the chat input!", "§7When activated, just write the cursed words in chat!"));
			} else {
				inv.setItem(13, Data.buildItemStack(Material.getMaterial("WOOL"), 1, 5, "§3Add Cursed-Words §8| §aActivated", "§7Click to activate the chat input!", "§7When activated, just write the cursed words in chat!"));
			}
			
			p.openInventory(inv);
		} else {
			Inventory inv = p.getServer().createInventory(null, 27, "§7Settings §8- §eChatFilter");
			
			for(int i = 0; i < 27; i++) {
				inv.setItem(i, Data.buildPlace());
			}
			
			if(!filter.contains(p)) {
				inv.setItem(13, Data.buildItemStack(Material.GRAY_DYE, 1, 0, "§3Add Cursed-Words §8| §7Deactivated", "§7Click to activate the chat input!", "§7When activated, just write the cursed words in chat!"));
			} else {
				inv.setItem(13, Data.buildItemStack(Material.LIME_DYE, 1, 5, "§3Add Cursed-Words §8| §aActivated", "§7Click to activate the chat input!", "§7When activated, just write the cursed words in chat!"));
			}
			
			p.openInventory(inv);
		}
	}
}
