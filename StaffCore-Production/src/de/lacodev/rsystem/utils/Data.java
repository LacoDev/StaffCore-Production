package de.lacodev.rsystem.utils;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Data {
	
	@SuppressWarnings("deprecation")
	public static ItemStack buildItemStack(Material mat, int size, int sh, String name, String lore1, String lore2) {
		ItemStack stack = new ItemStack(mat, size, (short)sh);
		ItemMeta meta = stack.getItemMeta();
		ArrayList<String> lore = new ArrayList<>();
		lore.add("");
		lore.add(lore1);
		lore.add(lore2);
		lore.add("");
		meta.setLore(lore);
		meta.setDisplayName(name);
		stack.setItemMeta(meta);
		
		return stack;
	}
	
	@SuppressWarnings("deprecation")
	public static ItemStack buildItemStackLore(Material mat, int size, int sh, String name, String lore1, String lore2, String lore3) {
		ItemStack stack = new ItemStack(mat, size, (short)sh);
		ItemMeta meta = stack.getItemMeta();
		ArrayList<String> lore = new ArrayList<>();
		lore.add("");
		lore.add(lore1);
		lore.add(lore2);
		lore.add(lore3);
		lore.add("");
		meta.setLore(lore);
		meta.setDisplayName(name);
		stack.setItemMeta(meta);
		
		return stack;
	}
	
	public static ItemStack buildPlace() {
		ItemStack stack = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName("§8...");
		stack.setItemMeta(meta);
		
		return stack;
	}
	
	@SuppressWarnings("deprecation")
	public static ItemStack buildItem(Material mat, int size, int sh, String name) {
		ItemStack stack = new ItemStack(mat, size, (short)sh);
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName(name);
		stack.setItemMeta(meta);
		
		return stack;
	}

}
