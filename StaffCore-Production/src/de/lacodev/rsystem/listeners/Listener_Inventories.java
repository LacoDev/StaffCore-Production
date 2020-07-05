package de.lacodev.rsystem.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.connorlinfoot.actionbarapi.ActionBarAPI;

import de.lacodev.rsystem.Main;
import de.lacodev.rsystem.utils.BanManager;
import de.lacodev.rsystem.utils.InventoryHandler;
import de.lacodev.rsystem.utils.PageManager;
import de.lacodev.rsystem.utils.ReportManager;
import de.lacodev.rsystem.utils.SystemManager;

public class Listener_Inventories implements Listener {
	
	@EventHandler
	public void onInv(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		
		if(e.getView().getTitle().startsWith(Main.getMSG("Messages.Report.Inventory.Title"))) {

			e.setCancelled(true);

			

			if(e.getCurrentItem() != null) {

				if (e.getSlot() > 8 && e.getSlot() < 45) {

					if(e.getCurrentItem().hasItemMeta()) {

						String s = e.getView().getTitle().replace(Main.getMSG("Messages.Report.Inventory.Title"), "");

						String f = ChatColor.stripColor(s);



						Player target = Bukkit.getPlayer(f);



						if(target != null) {

							if(target != p) {

								ReportManager.createReport(p.getName(), target, ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()));

								p.closeInventory();

							} else {

								p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Report.Cannot-report-self"));

							}

						} else {

							p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Report.Target-offline"));

						}

					}

				} else {

					int page = PageManager.page.get(((Player) e.getWhoClicked()).getPlayer());

					String s = e.getView().getTitle().replace(Main.getMSG("Messages.Report.Inventory.Title"), "");

					String f = ChatColor.stripColor(s);



					Player target = Bukkit.getPlayer(f);


					if (e.getSlot() == 46) {

						if (e.getCurrentItem().getItemMeta().getDisplayName().equals(Main.getMSG("Messages.Report.Inventory.PreviousPage.NotAvailable"))) {

							return;

						} else {

							ReportManager.openPagedReportInv(p, target.getName(), page-1);

						}

					} else if (e.getSlot() == 52) {

						if (e.getCurrentItem().getItemMeta().getDisplayName().equals(Main.getMSG("Messages.Report.Inventory.NextPage.NotAvailable"))) {

							return;

						} else {

							ReportManager.openPagedReportInv(p, target.getName(), page+1);

						}

					}



				}

			}

		}
		
		if(e.getView().getTitle().startsWith(Main.getMSG("Messages.Ban.Inventory.Title"))) {
			e.setCancelled(true);

			if(e.getCurrentItem() != null) {
				if (e.getSlot() > 8 && e.getSlot() < 45) {
					if(e.getCurrentItem().hasItemMeta()) {
						String s = e.getView().getTitle().replace(Main.getMSG("Messages.Ban.Inventory.Title"), "");
						String reason = e.getCurrentItem().getItemMeta().getDisplayName();
						reason = ChatColor.stripColor(reason);
						String target = ChatColor.stripColor(s);

						if (!BanManager.isBanned(SystemManager.getUUIDByName(target))) {
							if(target != p.getName()) {
								BanManager.submitBan(SystemManager.getUUIDByName(target), reason, p.getUniqueId().toString());
								if(Main.getInstance().actionbar) {
									ActionBarAPI.sendActionBar(p, Main.getPrefix() + Main.getMSG("Messages.Ban.Created"));
								} else {
									p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Ban.Created"));
								}
								p.closeInventory();
							} else {
								p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Ban.Cannot-ban-self"));
							}
						} else {
							if (Main.getInstance().actionbar) {
								ActionBarAPI.sendActionBar(p, Main.getPrefix() + Main.getMSG("Messages.Ban.Already-Banned"));
							} else {
								p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Ban.Already-Banned"));
							}
						}
					}
				} else {
					int page = PageManager.page.get(((Player) e.getWhoClicked()).getPlayer());
					String s = e.getView().getTitle().replace(Main.getMSG("Messages.Ban.Inventory.Title"), "");
					String target = ChatColor.stripColor(s);
					if (e.getSlot() == 46) {
						if (e.getCurrentItem().getItemMeta().getDisplayName().equals(Main.getMSG("Messages.Ban.Inventory.PreviousPage.NotAvailable"))) {
							return;
						} else {
							BanManager.openPagedBanInv(p, target, page-1);
						}
					} else if (e.getSlot() == 52) {
						if (e.getCurrentItem().getItemMeta().getDisplayName().equals(Main.getMSG("Messages.Ban.Inventory.NextPage.NotAvailable"))) {
							return;
						} else {
							BanManager.openPagedBanInv(p, target, page+1);
						}
					}

				}
			}
		}

		if(e.getView().getTitle().startsWith(Main.getMSG("Messages.Mute.Inventory.Title"))) {
			e.setCancelled(true);

			if(e.getCurrentItem() != null) {
				if (e.getSlot() > 8 && e.getSlot() < 45) {
					if(e.getCurrentItem().hasItemMeta()) {
						String s = e.getView().getTitle().replace(Main.getMSG("Messages.Mute.Inventory.Title"), "");
						String reason = e.getCurrentItem().getItemMeta().getDisplayName();
						reason = ChatColor.stripColor(reason);
						String target = ChatColor.stripColor(s);

						if (!BanManager.isMuted(SystemManager.getUUIDByName(target))) {
							if(target != p.getName()) {
								BanManager.submitMute(SystemManager.getUUIDByName(target), reason, p.getUniqueId().toString());
								if(Main.getInstance().actionbar) {
									ActionBarAPI.sendActionBar(p, Main.getPrefix() + Main.getMSG("Messages.Mute.Created"));
								} else {
									p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Mute.Created"));
								}
								p.closeInventory();
							} else {
								p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Mute.Cannot-mute-self"));
							}
						} else {
							if (Main.getInstance().actionbar) {
								ActionBarAPI.sendActionBar(p, Main.getPrefix() + Main.getMSG("Messages.Mute.Already-Muted"));
							} else {
								p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Mute.Already-Muted"));
							}
						}
					}
				} else {
					int page = PageManager.page.get(((Player) e.getWhoClicked()).getPlayer());
					String s = e.getView().getTitle().replace(Main.getMSG("Messages.Mute.Inventory.Title"), "");
					String target = ChatColor.stripColor(s);
					if (e.getSlot() == 46) {
						if (e.getCurrentItem().getItemMeta().getDisplayName().equals(Main.getMSG("Messages.Mute.Inventory.PreviousPage.NotAvailable"))) {
							return;
						} else {
							BanManager.openPagedMuteInv(p, target, page-1);
						}
					} else if (e.getSlot() == 52) {
						if (e.getCurrentItem().getItemMeta().getDisplayName().equals(Main.getMSG("Messages.Mute.Inventory.NextPage.NotAvailable"))) {
							return;
						} else {
							BanManager.openPagedMuteInv(p, target, page+1);
						}
					}

				}
			}
		}
		
		if(e.getView().getTitle().startsWith("§7Settings §8- §eChatFilter")) {
			e.setCancelled(true);
			
			if(e.getCurrentItem() != null) {
				if(e.getCurrentItem().hasItemMeta()) {
					if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§3Add Cursed-Words §8| §7Deactivated")) {
						InventoryHandler.filter.add(p);
						p.closeInventory();
						p.sendMessage(Main.getPrefix() + "§7You are now able to write your curse words which will be blocked afterwards!");
					}
					if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§3Add Cursed-Words §8| §aActivated")) {
						InventoryHandler.filter.remove(p);
						p.closeInventory();
						p.sendMessage(Main.getPrefix() + "§7You can now use the chat as before!");
					}
				}
			}
		}
	}

}
