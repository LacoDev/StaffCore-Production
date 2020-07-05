package de.lacodev.rsystem.utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Guardian;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import com.connorlinfoot.actionbarapi.ActionBarAPI;

import de.lacodev.rsystem.Main;
import de.lacodev.rsystem.mysql.MySQL;
import de.lacodev.rsystem.objects.BanReasons;
import de.lacodev.rsystem.objects.MuteReasons;

public class BanManager {
	
	public static ArrayList<Player> freezed = new ArrayList<>();
	public static ArrayList<String> sound_enums = new ArrayList<>();
	
	public static Inventory baninv;
	public static Inventory muteinv;

	public static void openPagedBanInv(Player p, String name, int page) {
		String title = Main.getMSG("Messages.Ban.Inventory.Title") + "§e" + name;
		if(title.length() > 32) {
			title = title.substring(0, 32);
		}
		baninv = p.getServer().createInventory(null, 54, title);


		if(MySQL.isConnected()) {

			ArrayList<BanReasons> rows = new ArrayList<>();
			ResultSet rs = MySQL.getResult("SELECT * FROM ReportSystem_reasonsdb WHERE TYPE = 'BAN'");
			try {
				while(rs.next()) {
					rows.add(new BanReasons(rs.getString("NAME"), rs.getInt("id")));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

			for (int i2 = 0; i2 < 9; i2++) {
				if(Bukkit.getVersion().contains("(MC: 1.8") || Bukkit.getVersion().contains("(MC: 1.7")) {
					baninv.setItem(i2, Data.buildItem(Material.getMaterial("STAINED_GLASS_PANE"), 1, 15, "§c"));
				} else {
					baninv.setItem(i2, Data.buildItem(Material.BLACK_STAINED_GLASS_PANE, 1, 0, "§c"));
				}

			}
			if (PageManager.page.containsKey(p.getPlayer())) {
				PageManager.page.remove(p.getPlayer());
			}
			PageManager.page.put(p.getPlayer(), page);

			for (int i2 = 45; i2 < 54; i2++) {
				if(Bukkit.getVersion().contains("(MC: 1.8") || Bukkit.getVersion().contains("(MC: 1.7")) {
					baninv.setItem(i2, Data.buildItem(Material.getMaterial("STAINED_GLASS_PANE"), 1, 15, "§c"));
				} else {
					baninv.setItem(i2, Data.buildItem(Material.BLACK_STAINED_GLASS_PANE, 1, 0, "§c"));
				}
			}
			if(Bukkit.getVersion().contains("(MC: 1.8") || Bukkit.getVersion().contains("(MC: 1.7")) {
				if (PageManager.isPageValid2(rows, page - 1, 36)) {
					baninv.setItem(46, Data.buildItem(Material.getMaterial("STAINED_GLASS_PANE"), 1, 5, Main.getMSG("Messages.Ban.Inventory.PreviousPage.Available")));
				} else {
					baninv.setItem(46, Data.buildItem(Material.getMaterial("STAINED_GLASS_PANE"), 1, 14, Main.getMSG("Messages.Ban.Inventory.PreviousPage.NotAvailable")));
				}

				if (PageManager.isPageValid2(rows, page + 1, 36)) {
					baninv.setItem(52, Data.buildItem(Material.getMaterial("STAINED_GLASS_PANE"), 1, 5, Main.getMSG("Messages.Ban.Inventory.NextPage.Available")));
				} else {
					baninv.setItem(52, Data.buildItem(Material.getMaterial("STAINED_GLASS_PANE"), 1, 14, Main.getMSG("Messages.Ban.Inventory.NextPage.NotAvailable")));
				}
			} else {
				if (PageManager.isPageValid2(rows, page - 1, 36)) {
					baninv.setItem(46, Data.buildItem(Material.LIME_STAINED_GLASS_PANE, 1, 0, Main.getMSG("Messages.Ban.Inventory.PreviousPage.Available")));
				} else {
					baninv.setItem(46, Data.buildItem(Material.RED_STAINED_GLASS_PANE, 1, 0, Main.getMSG("Messages.Ban.Inventory.PreviousPage.NotAvailable")));
				}

				if (PageManager.isPageValid2(rows, page + 1, 36)) {
					baninv.setItem(52, Data.buildItem(Material.LIME_STAINED_GLASS_PANE, 1, 0, Main.getMSG("Messages.Ban.Inventory.NextPage.Available")));
				} else {
					baninv.setItem(52, Data.buildItem(Material.RED_STAINED_GLASS_PANE, 1, 0, Main.getMSG("Messages.Ban.Inventory.NextPage.NotAvailable")));
				}
			}


			for (BanReasons item : PageManager.getPageItems2(rows, page, 36)) {
				if(ChatColor.valueOf(Main.getInstance().getConfig().getString("Messages.Ban.Inventory.BanItem-Name.Color")).isColor()) {
					baninv.setItem(baninv.firstEmpty(), Data.buildItemStack(Material.PAPER, 1, 0, ChatColor.valueOf(Main.getInstance().getConfig().getString("Messages.Ban.Inventory.BanItem-Name.Color")) + item.getName(), Main.getMSG("Messages.Ban.Inventory.BanItem-Lore.1").replace("%target%", name), Main.getMSG("Messages.Ban.Inventory.BanItem-Lore.2").replace("%reason%", item.getName())) );
				} else {
					baninv.setItem(baninv.firstEmpty(), Data.buildItemStack(Material.PAPER, 1, 0, "§e" + item.getName(), Main.getMSG("Messages.Ban.Inventory.BanItem-Lore.1").replace("%target%", name), Main.getMSG("Messages.Ban.Inventory.BanItem-Lore.2").replace("%reason%", item.getName())));
				}
			}
		} else {
			baninv.setItem(13, Data.buildItem(Material.BARRIER, 1, 0, "§cNo Connection"));
		}

		p.openInventory(baninv);
	}

	public static void openPagedMuteInv(Player p, String name, int page) {
		String title = Main.getMSG("Messages.Mute.Inventory.Title") + "§e" + name;
		if(title.length() > 32) {
			title = title.substring(0, 32);
		}
		muteinv = p.getServer().createInventory(null, 54, title);


		if(MySQL.isConnected()) {

			ArrayList<MuteReasons> rows = new ArrayList<>();
			ResultSet rs = MySQL.getResult("SELECT * FROM ReportSystem_reasonsdb WHERE TYPE = 'MUTE'");
			try {
				while(rs.next()) {
					rows.add(new MuteReasons(rs.getString("NAME"), rs.getInt("id")));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

			for (int i2 = 0; i2 < 9; i2++) {
				if(Bukkit.getVersion().contains("(MC: 1.8") || Bukkit.getVersion().contains("(MC: 1.7")) {
					muteinv.setItem(i2, Data.buildItem(Material.getMaterial("STAINED_GLASS_PANE"), 1, 15, "§c"));
				} else {
					muteinv.setItem(i2, Data.buildItem(Material.BLACK_STAINED_GLASS_PANE, 1, 0, "§c"));
				}

			}
			if (PageManager.page.containsKey(p.getPlayer())) {
				PageManager.page.remove(p.getPlayer());
			}
			PageManager.page.put(p.getPlayer(), page);

			for (int i2 = 45; i2 < 54; i2++) {
				if(Bukkit.getVersion().contains("(MC: 1.8") || Bukkit.getVersion().contains("(MC: 1.7")) {
					muteinv.setItem(i2, Data.buildItem(Material.getMaterial("STAINED_GLASS_PANE"), 1, 15, "§c"));
				} else {
					muteinv.setItem(i2, Data.buildItem(Material.BLACK_STAINED_GLASS_PANE, 1, 0, "§c"));
				}
			}
			if(Bukkit.getVersion().contains("(MC: 1.8") || Bukkit.getVersion().contains("(MC: 1.7")) {
				if (PageManager.isPageValid3(rows, page - 1, 36)) {
					muteinv.setItem(46, Data.buildItem(Material.getMaterial("STAINED_GLASS_PANE"), 1, 5, Main.getMSG("Messages.Mute.Inventory.PreviousPage.Available")));
				} else {
					muteinv.setItem(46, Data.buildItem(Material.getMaterial("STAINED_GLASS_PANE"), 1, 14, Main.getMSG("Messages.Mute.Inventory.PreviousPage.NotAvailable")));
				}

				if (PageManager.isPageValid3(rows, page + 1, 36)) {
					muteinv.setItem(52, Data.buildItem(Material.getMaterial("STAINED_GLASS_PANE"), 1, 5, Main.getMSG("Messages.Mute.Inventory.NextPage.Available")));
				} else {
					muteinv.setItem(52, Data.buildItem(Material.getMaterial("STAINED_GLASS_PANE"), 1, 14, Main.getMSG("Messages.Mute.Inventory.NextPage.NotAvailable")));
				}
			} else {
				if (PageManager.isPageValid3(rows, page - 1, 36)) {
					muteinv.setItem(46, Data.buildItem(Material.LIME_STAINED_GLASS_PANE, 1, 0, Main.getMSG("Messages.Mute.Inventory.PreviousPage.Available")));
				} else {
					muteinv.setItem(46, Data.buildItem(Material.RED_STAINED_GLASS_PANE, 1, 0, Main.getMSG("Messages.Mute.Inventory.PreviousPage.NotAvailable")));
				}

				if (PageManager.isPageValid3(rows, page + 1, 36)) {
					muteinv.setItem(52, Data.buildItem(Material.LIME_STAINED_GLASS_PANE, 1, 0, Main.getMSG("Messages.Mute.Inventory.NextPage.Available")));
				} else {
					muteinv.setItem(52, Data.buildItem(Material.RED_STAINED_GLASS_PANE, 1, 0, Main.getMSG("Messages.Mute.Inventory.NextPage.NotAvailable")));
				}
			}


			for (MuteReasons item : PageManager.getPageItems3(rows, page, 36)) {
				if(ChatColor.valueOf(Main.getInstance().getConfig().getString("Messages.Mute.Inventory.MuteItem-Name.Color")).isColor()) {
					muteinv.setItem(muteinv.firstEmpty(), Data.buildItemStack(Material.PAPER, 1, 0, ChatColor.valueOf(Main.getInstance().getConfig().getString("Messages.Mute.Inventory.MuteItem-Name.Color")) + item.getName(), Main.getMSG("Messages.Mute.Inventory.MuteItem-Lore.1").replace("%target%", name), Main.getMSG("Messages.Mute.Inventory.MuteItem-Lore.2").replace("%reason%", item.getName())) );
				} else {
					muteinv.setItem(muteinv.firstEmpty(), Data.buildItemStack(Material.PAPER, 1, 0, "§e" + item.getName(), Main.getMSG("Messages.Mute.Inventory.MuteItem-Lore.1").replace("%target%", name), Main.getMSG("Messages.Mute.Inventory.MuteItem-Lore.2").replace("%reason%", item.getName())));
				}
			}
		} else {
			muteinv.setItem(13, Data.buildItem(Material.BARRIER, 1, 0, "§cNo Connection"));
		}

		p.openInventory(muteinv);

	}

	public static void createBanReason(String name, String unit, int length) {
		if(MySQL.isConnected()) {
			if(!existsBanReason(name)) {
				
				long time = 0;
				
				if(unit.toLowerCase().matches("d")) {
					time = 1000*60*60*24;
				} else if(unit.toLowerCase().matches("h")) {
					time = 1000*60*60;
				} else if(unit.toLowerCase().matches("m")) {
					time = 1000*60;
				} else if(unit.toLowerCase().matches("perma")) {
					time = 1;
				}
				
				try {
					PreparedStatement st = MySQL.getCon().prepareStatement("INSERT INTO ReportSystem_reasonsdb(TYPE,NAME,BAN_LENGTH) VALUES ('BAN','"+ name +"','"+ (time * length) +"')");
					st.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	

	public static boolean existsBanID(int id) {

		if(MySQL.isConnected()) {

			ResultSet rs = MySQL.getResult("SELECT id FROM ReportSystem_reasonsdb WHERE id = '"+ id +"' AND TYPE = 'BAN'");

			try {

				if(rs.next()) {

					return rs.getString("id") != null;

				}

			} catch (SQLException e) {

				e.printStackTrace();

			}

		}

		return false;

	}



	public static String getBanReasonFromID(int id) {

		if(MySQL.isConnected()) {

			ResultSet rs = MySQL.getResult("SELECT NAME FROM ReportSystem_reasonsdb WHERE id = '"+ id +"' AND TYPE = 'BAN'");

			try {

				if(rs.next()) {

					return rs.getString("NAME");

				}

			} catch (SQLException e) {

				e.printStackTrace();

			}

		}

		return "Unknown";

	}
	
	public static boolean existsBanReason(String name) {
		if(MySQL.isConnected()) {
			ResultSet rs = MySQL.getResult("SELECT NAME FROM ReportSystem_reasonsdb WHERE NAME = '"+ name +"' AND TYPE = 'BAN'");
			try {
				if(rs.next()) {
					return rs.getString("NAME") != null;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	public static void deleteBanReason(String name) {
		if(MySQL.isConnected()) {
			try {
				PreparedStatement st = MySQL.getCon().prepareStatement("DELETE FROM ReportSystem_reasonsdb WHERE NAME = '"+ name +"' AND TYPE = 'BAN'");
				st.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static boolean isBanned(String uuid) {
		if(MySQL.isConnected()) {
			ResultSet rs = MySQL.getResult("SELECT * FROM ReportSystem_bansdb WHERE BANNED_UUID = '"+ uuid +"'");
			try {
				if(rs != null) {
					if(rs.next()) {
						if(rs.getLong("BAN_END") == -1) {
							return true;
						} else {
							if(rs.getLong("BAN_END") > System.currentTimeMillis()) {
								return true;
							} else {
								if(unban(uuid)) {
									sendConsoleNotify("UNBAN", uuid);	
								}
							}
						}
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	public static boolean unban(String uuid) {
		if(MySQL.isConnected()) {
			try {
				PreparedStatement st = MySQL.getCon().prepareStatement("DELETE FROM ReportSystem_bansdb WHERE BANNED_UUID = '"+ uuid +"'");
				if(st.executeUpdate() > 0) {
					return true;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	private static void sendConsoleNotify(String string, String uuid) {
		if(string.matches("UNBAN")) {
			for(Player all : Bukkit.getOnlinePlayers()) {
				if(all.hasPermission(Main.getPermissionNotice("Permissions.UnBan.Notify")) || all.hasPermission(Main.getPermissionNotice("Permissions.Everything"))) {
					if(Main.getInstance().actionbar) {
						ActionBarAPI.sendActionBar(all, Main.getPrefix() + Main.getMSG("Messages.UnBan.Notify.Team.Unban").replace("%player%", Main.getMSG("Messages.UnBan.Notify.Team.ConsoleName")).replace("%target%", SystemManager.getUsernameByUUID(uuid)));
					} else {
						all.sendMessage("");
						all.sendMessage(Main.getPrefix() + Main.getMSG("Messages.UnBan.Notify.Team.Unban").replace("%player%", Main.getMSG("Messages.UnBan.Notify.Team.ConsoleName")).replace("%target%", SystemManager.getUsernameByUUID(uuid)));
						all.sendMessage("");
					}
				}
			}
		} else if(string.matches("UNMUTE")) {
			for(Player all : Bukkit.getOnlinePlayers()) {
				if(all.hasPermission(Main.getPermissionNotice("Permissions.UnMute.Notify")) || all.hasPermission(Main.getPermissionNotice("Permissions.Everything"))) {
					if(Main.getInstance().actionbar) {
						ActionBarAPI.sendActionBar(all, Main.getPrefix() + Main.getMSG("Messages.UnMute.Notify.Team.Unmute").replace("%player%", Main.getMSG("Messages.UnMute.Notify.Team.ConsoleName")).replace("%target%", SystemManager.getUsernameByUUID(uuid)));
					} else {
						all.sendMessage("");
						all.sendMessage(Main.getPrefix() + Main.getMSG("Messages.UnMute.Notify.Team.Unmute").replace("%player%", Main.getMSG("Messages.UnMute.Notify.Team.ConsoleName")).replace("%target%", SystemManager.getUsernameByUUID(uuid)));
						all.sendMessage("");
					}
				}
			}
		}
	}
	
	public static long getRawBanLength(String reason) {
		if(MySQL.isConnected()) {
			ResultSet rs = MySQL.getResult("SELECT BAN_LENGTH FROM ReportSystem_reasonsdb WHERE NAME = '"+ reason +"' AND TYPE = 'BAN'");
			try {
				if(rs.next()) {
					return rs.getLong("BAN_LENGTH");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return 0;
	}
	
	public static long getBanEnd(String uuid) {
		if(MySQL.isConnected()) {
			ResultSet rs = MySQL.getResult("SELECT BAN_END FROM ReportSystem_bansdb WHERE BANNED_UUID = '"+ uuid +"'");
			try {
				if(rs.next()) {
					return rs.getLong("BAN_END");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return 0;
	}
	
	public static String getBanReason(String uuid) {
		if(MySQL.isConnected()) {
			ResultSet rs = MySQL.getResult("SELECT REASON FROM ReportSystem_bansdb WHERE BANNED_UUID = '"+ uuid +"'");
			try {
				if(rs.next()) {
					return rs.getString("REASON");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return "Unknown";
	}
	

	public static ArrayList<BanReasons> getBanReasons() {

		ArrayList<BanReasons> reasons = new ArrayList<>();



		ResultSet rs = MySQL.getResult("SELECT * FROM ReportSystem_reasonsdb WHERE TYPE = 'BAN'");



		try {

			while(rs.next()) {

				reasons.add(new BanReasons(rs.getString("NAME"), rs.getInt("id")));

			}

			return reasons;

		} catch (SQLException e) {

			e.printStackTrace();

		}

		return reasons;

	}
	
	public static void addBan(String uuid) {
		try {
			PreparedStatement st = MySQL.getCon().prepareStatement("UPDATE ReportSystem_playerdb SET BANS = '"+ (getBans(uuid) + 1) +"' WHERE UUID = '"+ uuid +"'");
			st.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static int getBans(String uuid) {
		ResultSet rs = MySQL.getResult("SELECT BANS FROM ReportSystem_playerdb WHERE UUID = '"+ uuid +"'");
		try {
			if(rs.next()) {
				return rs.getInt("BANS");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public static String getBanFinalEnd(String UUID) {
		long uhrzeit = System.currentTimeMillis();
		long end = getBanEnd(UUID);
		
		long millis = end - uhrzeit;
		
		long sekunden = 0L;
		long minuten = 0L;
		long stunden = 0L;
		long tage = 0L;
		
		while(millis > 1000L) {
			millis -= 1000L;
			sekunden += 1L;
		}
		while(sekunden > 60L) {
			sekunden -= 60L;
			minuten += 1L;
		}
		while(minuten > 60L) {
			minuten -= 60L;
			stunden += 1L;
		}
		while(stunden > 24L) {
			stunden -= 24L;
			tage += 1L;
		}
		if (tage != 0L) {
			return "§a" + tage + " §7"+ Main.getMSG("Layout.Ban.Remaining.Days") +" §a" + stunden + " §7"+ Main.getMSG("Layout.Ban.Remaining.Hours") +" §a" + minuten + " §7" + Main.getMSG("Layout.Ban.Remaining.Minutes");
		}
		if ((tage == 0L) && (stunden != 0L)) {
			return "§a" + stunden + " §7"+ Main.getMSG("Layout.Ban.Remaining.Hours") +" §a" + minuten + " §7"+ Main.getMSG("Layout.Ban.Remaining.Minutes") +" §a" + sekunden + " §7" + Main.getMSG("Layout.Ban.Remaining.Seconds");
		}
		if ((tage == 0L) && (stunden == 0L) && (minuten != 0L)) {
			return "§a" + minuten + " §7"+ Main.getMSG("Layout.Ban.Remaining.Minutes") +" §a" + sekunden + " §7"+ Main.getMSG("Layout.Ban.Remaining.Seconds");
		}
		if ((tage == 0L) && (stunden == 0L) && (minuten == 0L) && (sekunden != 0L)) {
			return "§a" + sekunden + " §7"+ Main.getMSG("Layout.Ban.Remaining.Seconds");
		}
		return "§4Fehler in der Berechnung!";
	}
	
	public static String getMuteFinalEnd(String UUID) {
		long uhrzeit = System.currentTimeMillis();
		long end = getMuteEnd(UUID);
		
		long millis = end - uhrzeit;
		
		long sekunden = 0L;
		long minuten = 0L;
		long stunden = 0L;
		long tage = 0L;
		
		while(millis > 1000L) {
			millis -= 1000L;
			sekunden += 1L;
		}
		while(sekunden > 60L) {
			sekunden -= 60L;
			minuten += 1L;
		}
		while(minuten > 60L) {
			minuten -= 60L;
			stunden += 1L;
		}
		while(stunden > 24L) {
			stunden -= 24L;
			tage += 1L;
		}
		if (tage != 0L) {
			return "§a" + tage + " §7"+ Main.getMSG("Layout.Mute.Remaining.Days") +" §a" + stunden + " §7"+ Main.getMSG("Layout.Mute.Remaining.Hours") +" §a" + minuten + " §7" + Main.getMSG("Layout.Mute.Remaining.Minutes");
		}
		if ((tage == 0L) && (stunden != 0L)) {
			return "§a" + stunden + " §7"+ Main.getMSG("Layout.Mute.Remaining.Hours") +" §a" + minuten + " §7"+ Main.getMSG("Layout.Mute.Remaining.Minutes") +" §a" + sekunden + " §7" + Main.getMSG("Layout.Mute.Remaining.Seconds");
		}
		if ((tage == 0L) && (stunden == 0L) && (minuten != 0L)) {
			return "§a" + minuten + " §7"+ Main.getMSG("Layout.Mute.Remaining.Minutes") +" §a" + sekunden + " §7"+ Main.getMSG("Layout.Mute.Remaining.Seconds");
		}
		if ((tage == 0L) && (stunden == 0L) && (minuten == 0L) && (sekunden != 0L)) {
			return "§a" + sekunden + " §7"+ Main.getMSG("Layout.Mute.Remaining.Seconds");
		}
		return "§4Fehler in der Berechnung!";
	}
	
	public static void submitBan(String targetuuid, String reason, String team) {
		new BukkitRunnable() {

			@Override
			public void run() {
				if(MySQL.isConnected()) {
					if(existsBanReason(reason)) {
						
						long ban_end = 0;
						
						if(getRawBanLength(reason) == -1) {
							ban_end = getRawBanLength(reason);
						} else {
							ban_end = getRawBanLength(reason) + System.currentTimeMillis();
						}
						
						try {
							PreparedStatement st = MySQL.getCon().prepareStatement("INSERT INTO ReportSystem_bansdb(BANNED_UUID,TEAM_UUID,REASON,BAN_END) VALUES ('"+ targetuuid +"','"+ team +"','"+ reason +"','"+ ban_end +"')");
							if(st.executeUpdate() > 0) {
								MySQL.update("INSERT INTO ReportSystem_banhistory(BANNED_UUID,TEAM_UUID,REASON,BAN_START,BAN_END) VALUES ('"+ targetuuid +"','"+ team +"','"+ reason +"','"+ System.currentTimeMillis() +"','"+ ban_end +"')");
								Player target = Bukkit.getPlayer(SystemManager.getUsernameByUUID(targetuuid));
								if(target != null) {
									if(Main.getInstance().getConfig().getBoolean("Ban-Animation.Enable")) {
										playBanAnimation(target);
									} else {
										target.kickPlayer(Main.getMSG("Messages.Ban.Kick-Player-If-Banned").replace("%reason%", BanManager.getBanReason(target.getUniqueId().toString())));
									}
									
									if(Main.getInstance().getConfig().getBoolean("IP-Ban.Ban-IP-When-Player-Gets-Banned")) {
										banIPAddress(target);
									}
								}
								addBan(targetuuid);
								if(SystemManager.existsPlayerData(team)) {
									Player p = Bukkit.getPlayer(SystemManager.getUsernameByUUID(team));
									if(p != null) {
										ReportManager.sendNotify("BAN", p.getName(), SystemManager.getUsernameByUUID(targetuuid), reason);
									}
								} else {
									ReportManager.sendNotify("BAN", "Console", SystemManager.getUsernameByUUID(targetuuid), reason);
								}
								
								if(Main.getInstance().getConfig().getBoolean("General.Include-Vault")) {
									ArrayList<String> reporter_uuids = new ArrayList<>();
									
									ResultSet rs1 = MySQL.getResult("SELECT * FROM ReportSystem_reportsdb WHERE REPORTED_UUID = '"+ targetuuid +"'");
									if(rs1.next()) {
										while(rs1.next()) {
											if(!reporter_uuids.contains(rs1.getString("REPORTER_UUID"))) {
												reporter_uuids.add(rs1.getString("REPORTER_UUID"));
											}
										}
										for(int i = 0; i < reporter_uuids.size(); i++) {
											if(SystemManager.existsPlayerData(reporter_uuids.get(i))) {
												Player reporter = Bukkit.getPlayer(SystemManager.getUsernameByUUID(reporter_uuids.get(i)));
												
												if(reporter != null) {
													
													Random rdm = new Random();
													int rdmm = rdm.nextInt(Main.getInstance().getConfig().getInt("Vault.Rewards.Report.MAX"));
													
													Main.getEconomy().depositPlayer(reporter, rdmm);
													reporter.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Vault.Rewards.Report.Success").replace("%reward%", String.valueOf(rdmm)));
												} else {
													ActionManager.createAction("REPORT_SUCCESS", reporter_uuids.get(i), target.getName());
												}
											}
										}
									}
								}
							}
						} catch(Exception e) {
							Bukkit.getConsoleSender().sendMessage("");
							Bukkit.getConsoleSender().sendMessage("§cSystem §8» §c§lFAILED §8(§7Submit BAN§8)");
							Bukkit.getConsoleSender().sendMessage("");
						}
					}
				}
			}
		}.runTask(Main.getInstance());
	}
	
	@SuppressWarnings("deprecation")
	public static void playBanAnimation(Player t) {
		
		freeze(t);
		
		if(Main.getPermissionNotice("Ban-Animation.Type").toLowerCase().equalsIgnoreCase("guardian")) {
			if(Bukkit.getVersion().contains("(MC: 1.13") || Bukkit.getVersion().contains("(MC: 1.14") || Bukkit.getVersion().contains("(MC: 1.15") || Bukkit.getVersion().contains("(MC: 1.16")) {
				int a;
				
				Entity en = t.getLocation().getWorld().spawnEntity(t.getLocation().add(-2, 5, -2), EntityType.GUARDIAN);
				
				Guardian g = (Guardian) en;
				
				g.setGravity(false);
				g.setSwimming(true);
				g.setTarget(t);
				g.setCustomName("G1");
				g.setCustomNameVisible(false);
				
				Entity en2 = t.getLocation().getWorld().spawnEntity(t.getLocation().add(2, 5, -2), EntityType.GUARDIAN);
				
				Guardian g2 = (Guardian) en2;
				
				g2.setGravity(false);
				g2.setSwimming(true);
				g2.setTarget(t);
				g2.setCustomName("G2");
				g2.setCustomNameVisible(false);
				
				Entity en3 = t.getLocation().getWorld().spawnEntity(t.getLocation().add(0, 5, 2), EntityType.GUARDIAN);
				
				Guardian g3 = (Guardian) en3;
				
				g3.setInvulnerable(true);
				g3.setGravity(false);
				g3.setSwimming(true);
				g3.setTarget(t);
				g3.setCustomName("G3");
				g3.setCustomNameVisible(false);
				
				t.setGravity(false);
				
				if(sound_enums.contains(Main.getPermissionNotice("Ban-Animation.Sound").toUpperCase())) {
					for(Player all : Bukkit.getOnlinePlayers()) {
						all.playSound(all.getLocation(), Sound.valueOf(Main.getPermissionNotice("Ban-Animation.Sound")), 100, 100);
					}
				} else {
					Bukkit.getConsoleSender().sendMessage(Main.getPrefix() + "§cInvalid Sound §8- §7It seems like this sound is not available in this version!");
				}
				
				a = Bukkit.getScheduler().scheduleAsyncRepeatingTask(Main.getInstance(), new Runnable() {

					@Override
					public void run() {
						for(Player all : Bukkit.getOnlinePlayers()) {
							all.playEffect(t.getLocation().add(0, 1, 0), Effect.ZOMBIE_INFECT, 10);
						}
					}
				}, 0, 5);
				
				new BukkitRunnable() {

					@Override
					public void run() {
						g.setHealth(0);
						g2.setHealth(0);
						g3.setHealth(0);
						
						for(Player all : Bukkit.getOnlinePlayers()) {
							if(sound_enums.contains("ENTITY_LIGHTNING_BOLT_THUNDER")) {
								all.playSound(all.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 100, 100);
							} else {
								all.playSound(all.getLocation(), Sound.valueOf("AMBIENCE_THUNDER"), 100, 100);
							}
						}
						t.getLocation().getWorld().strikeLightning(t.getLocation());
						
						Bukkit.getScheduler().cancelTask(a);
						t.kickPlayer(Main.getMSG("Messages.Ban.Kick-Player-If-Banned").replace("%reason%", BanManager.getBanReason(t.getUniqueId().toString())));
					}
				}.runTaskLater(Main.getInstance(), 5*20);
			} else {
				Bukkit.getConsoleSender().sendMessage("");
				t.kickPlayer(Main.getMSG("Messages.Ban.Kick-Player-If-Banned").replace("%reason%", BanManager.getBanReason(t.getUniqueId().toString())));
				Bukkit.getConsoleSender().sendMessage(Main.getPrefix() + "§cInvalid Enviroment §8- §7" + Bukkit.getVersion());
				Bukkit.getConsoleSender().sendMessage("");
				Bukkit.getConsoleSender().sendMessage(Main.getPrefix() + "§7Choose one, which is available:");
				Bukkit.getConsoleSender().sendMessage(Main.getPrefix() + "§3Guardian §8(§71.13+§8)");
				Bukkit.getConsoleSender().sendMessage(Main.getPrefix() + "§3TNT §8(§71.7+§8)");
				Bukkit.getConsoleSender().sendMessage(Main.getPrefix() + "§3Zombie §8(§71.7+§8)");
			}
		} else if(Main.getPermissionNotice("Ban-Animation.Type").toLowerCase().equalsIgnoreCase("zombie")) {
			int a;
			
			Entity en = t.getLocation().getWorld().spawnEntity(t.getLocation(), EntityType.ZOMBIE);
			
			Zombie z = (Zombie) en;
			
			z.setTarget(null);
			z.setCustomName("§cBan §8» §7" + t.getName());
			z.setCustomNameVisible(false);
			z.setFireTicks(0);
			
			if(sound_enums.contains(Main.getPermissionNotice("Ban-Animation.Sound").toUpperCase())) {
				for(Player all : Bukkit.getOnlinePlayers()) {
					all.hidePlayer(t);
					all.playSound(all.getLocation(), Sound.valueOf(Main.getPermissionNotice("Ban-Animation.Sound")), 100, 100);
				}
			} else {
				Bukkit.getConsoleSender().sendMessage(Main.getPrefix() + "§cInvalid Sound §8- §7It seems like this sound is not available in this version!");
			}
			
			a = Bukkit.getScheduler().scheduleAsyncRepeatingTask(Main.getInstance(), new Runnable() {

				@Override
				public void run() {
					for(Player all : Bukkit.getOnlinePlayers()) {
						all.playEffect(t.getLocation().add(0, 1, 0), Effect.MOBSPAWNER_FLAMES, 10);
						z.teleport(t.getLocation());
						if(sound_enums.contains("ENTITY_LIGHTNING_BOLT_THUNDER")) {
							all.playSound(all.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 100, 100);
						} else {
							all.playSound(all.getLocation(), Sound.valueOf("AMBIENCE_THUNDER"), 100, 100);
						}
					}
				}
			}, 0, 5);
			
			new BukkitRunnable() {

				@Override
				public void run() {
					z.setHealth(0);
					
					for(Player all : Bukkit.getOnlinePlayers()) {
						if(sound_enums.contains("ENTITY_LIGHTNING_BOLT_THUNDER")) {
							all.playSound(all.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 100, 100);
						} else {
							all.playSound(all.getLocation(), Sound.valueOf("AMBIENCE_THUNDER"), 100, 100);
						}
					}
					t.getLocation().getWorld().strikeLightning(t.getLocation());
					
					Bukkit.getScheduler().cancelTask(a);
					t.kickPlayer(Main.getMSG("Messages.Ban.Kick-Player-If-Banned").replace("%reason%", BanManager.getBanReason(t.getUniqueId().toString())));
				}
			}.runTaskLater(Main.getInstance(), 5*20);
		} else if(Main.getPermissionNotice("Ban-Animation.Type").toLowerCase().equalsIgnoreCase("tnt")) {
			int a;
			
			if(sound_enums.contains(Main.getPermissionNotice("Ban-Animation.Sound").toUpperCase())) {
				for(Player all : Bukkit.getOnlinePlayers()) {
					all.playSound(all.getLocation(), Sound.valueOf(Main.getPermissionNotice("Ban-Animation.Sound")), 100, 100);
				}
			} else {
				Bukkit.getConsoleSender().sendMessage(Main.getPrefix() + "§cInvalid Sound §8- §7It seems like this sound is not available in this version!");
			}
			
			a = Bukkit.getScheduler().scheduleAsyncRepeatingTask(Main.getInstance(), new Runnable() {

				@Override
				public void run() {
					for(Player all : Bukkit.getOnlinePlayers()) {
						all.playEffect(t.getLocation().add(0, 1, 0), Effect.EXTINGUISH, 10);
						
						if(sound_enums.contains("ENTITY_TNT_PRIMED")) {
							all.playSound(all.getLocation(), Sound.ENTITY_TNT_PRIMED, 100, 100);
						} else {
							all.playSound(all.getLocation(), Sound.valueOf("EXPLODE"), 100, 100);
						}
					}
				}
			}, 0, 5);
			
			new BukkitRunnable() {

				@Override
				public void run() {
					
					for(Player all : Bukkit.getOnlinePlayers()) {
						if(sound_enums.contains("ENTITY_LIGHTNING_BOLT_THUNDER")) {
							all.playSound(all.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 100, 100);
						} else {
							all.playSound(all.getLocation(), Sound.valueOf("AMBIENCE_THUNDER"), 100, 100);
						}
					}
					t.getLocation().getWorld().strikeLightning(t.getLocation());
					
					Bukkit.getScheduler().cancelTask(a);
					t.kickPlayer(Main.getMSG("Messages.Ban.Kick-Player-If-Banned").replace("%reason%", BanManager.getBanReason(t.getUniqueId().toString())));
				}
			}.runTaskLater(Main.getInstance(), 2*20);
		} else {
			Bukkit.getConsoleSender().sendMessage("");
			t.kickPlayer(Main.getMSG("Messages.Ban.Kick-Player-If-Banned").replace("%reason%", BanManager.getBanReason(t.getUniqueId().toString())));
			Bukkit.getConsoleSender().sendMessage(Main.getPrefix() + "§cInvalid AnimationType!");
			Bukkit.getConsoleSender().sendMessage("");
			Bukkit.getConsoleSender().sendMessage(Main.getPrefix() + "§7Choose one, which is available:");
			Bukkit.getConsoleSender().sendMessage(Main.getPrefix() + "§3Guardian §8(§71.13+§8)");
			Bukkit.getConsoleSender().sendMessage(Main.getPrefix() + "§3TNT §8(§71.7+§8)");
			Bukkit.getConsoleSender().sendMessage(Main.getPrefix() + "§3Zombie §8(§71.7+§8)");
		}
		
	}


	public static void freeze(Player t) {
		freezed.add(t);
		
		if(t.getLocation().add(0, -1, 0).getBlock().getType() == Material.AIR) {
			t.getLocation().add(0, -1, 0).getBlock().setType(Material.BARRIER);
		}
	}
	
	public static void unfreeze(Player t) {
		freezed.remove(t);
		
		if(t.getLocation().add(0, -1, 0).getBlock().getType() == Material.BARRIER) {
			t.getLocation().add(0, -1, 0).getBlock().setType(Material.AIR);
		}
	}


	public static void banIPAddress(Player target) {
		if(MySQL.isConnected()) {
			if(!isIPBanned(target.getAddress().getAddress().toString())) {
				
				long current = System.currentTimeMillis();
				long hours = 1000*60*60*Main.getInstance().getConfig().getInt("IP-Ban.Duration-in-Hours");
				
				long end = current + hours;
				
				String ip = target.getAddress().getAddress().toString();
				
				MySQL.update("INSERT INTO ReportSystem_ipbans(IP_ADDRESS,END) VALUES ('"+ ip +"','"+ end +"')"); 
			}
		}
	}

	public static boolean isIPBanned(String address) {
		if(MySQL.isConnected()) {
			ResultSet rs = MySQL.getResult("SELECT * FROM ReportSystem_ipbans WHERE IP_ADDRESS = '"+ address +"'");
			try {
				if(rs != null) {
					if(rs.next()) {
						if(rs.getLong("END") > System.currentTimeMillis()) {
							return true;
						} else {
							return false;
						}
					} else {
						return false;
					}
				} else {
					return false;
				}
			} catch (SQLException e) {
				return false;
			}
		} else {
			return false;
		}
	}

	public static void createMuteReason(String name, String unit, int length) {
		if(MySQL.isConnected()) {
			if(!existsMuteReason(name)) {
				
				long time = 0;
				
				if(unit.toLowerCase().matches("d")) {
					time = 1000*60*60*24;
				} else if(unit.toLowerCase().matches("h")) {
					time = 1000*60*60;
				} else if(unit.toLowerCase().matches("m")) {
					time = 1000*60;
				}
				
				try {
					PreparedStatement st = MySQL.getCon().prepareStatement("INSERT INTO ReportSystem_reasonsdb(TYPE,NAME,BAN_LENGTH) VALUES ('MUTE','"+ name +"','"+ (time * length) +"')");
					st.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	

	public static boolean existsMuteID(int id) {

		if(MySQL.isConnected()) {

			ResultSet rs = MySQL.getResult("SELECT id FROM ReportSystem_reasonsdb WHERE id = '"+ id +"' AND TYPE = 'MUTE'");

			try {

				if(rs.next()) {

					return rs.getString("id") != null;

				}

			} catch (SQLException e) {

				e.printStackTrace();

			}

		}

		return false;

	}



	public static String getMuteReasonFromID(int id) {

		if(MySQL.isConnected()) {

			ResultSet rs = MySQL.getResult("SELECT NAME FROM ReportSystem_reasonsdb WHERE id = '"+ id +"' AND TYPE = 'MUTE'");

			try {

				if(rs.next()) {

					return rs.getString("NAME");

				}

			} catch (SQLException e) {

				e.printStackTrace();

			}

		}

		return "Unknown";

	}
	
	public static boolean existsMuteReason(String name) {
		if(MySQL.isConnected()) {
			ResultSet rs = MySQL.getResult("SELECT NAME FROM ReportSystem_reasonsdb WHERE NAME = '"+ name +"' AND TYPE = 'MUTE'");
			try {
				if(rs.next()) {
					return rs.getString("NAME") != null;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	public static void deleteMuteReason(String name) {
		if(MySQL.isConnected()) {
			if(existsMuteReason(name)) {
				try {
					PreparedStatement st = MySQL.getCon().prepareStatement("DELETE FROM ReportSystem_reasonsdb WHERE NAME = '"+ name +"' AND TYPE = 'MUTE'");
					st.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static boolean isMuted(String uuid) {
		if(MySQL.isConnected()) {
			ResultSet rs = MySQL.getResult("SELECT * FROM ReportSystem_mutesdb WHERE MUTED_UUID = '"+ uuid +"'");
			try {
				if(rs != null) {
					if(rs.next()) {
						if(rs.getLong("MUTE_END") > System.currentTimeMillis()) {
							return rs.getString("MUTED_UUID") != null;
						} else {
							if(unmute(uuid)) {
								sendConsoleNotify("UNMUTE", uuid);
							}
						}
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	public static boolean unmute(String uuid) {
		if(MySQL.isConnected()) {
			try {
				PreparedStatement st = MySQL.getCon().prepareStatement("DELETE FROM ReportSystem_mutesdb WHERE MUTED_UUID = '"+ uuid +"'");
				if(st.executeUpdate() > 0) {
					return true;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}


	public static ArrayList<MuteReasons> getMuteReasons() {

		ArrayList<MuteReasons> reasons = new ArrayList<>();



		ResultSet rs = MySQL.getResult("SELECT * FROM ReportSystem_reasonsdb WHERE TYPE = 'MUTE'");



		try {

			while(rs.next()) {

				reasons.add(new MuteReasons(rs.getString("NAME"), rs.getInt("id")));

			}

			return reasons;

		} catch (SQLException e) {

			e.printStackTrace();

		}

		return reasons;

	}
	
	public static long getRawMuteLength(String reason) {
		if(MySQL.isConnected()) {
			ResultSet rs = MySQL.getResult("SELECT BAN_LENGTH FROM ReportSystem_reasonsdb WHERE NAME = '"+ reason +"' AND TYPE = 'MUTE'");
			try {
				if(rs.next()) {
					return rs.getLong("BAN_LENGTH");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return 0;
	}
	
	public static long getMuteEnd(String uuid) {
		if(MySQL.isConnected()) {
			ResultSet rs = MySQL.getResult("SELECT MUTE_END FROM ReportSystem_mutesdb WHERE MUTED_UUID = '"+ uuid +"'");
			try {
				if(rs.next()) {
					return rs.getLong("MUTE_END");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return 0;
	}
	
	public static String getMuteReason(String uuid) {
		if(MySQL.isConnected()) {
			ResultSet rs = MySQL.getResult("SELECT REASON FROM ReportSystem_mutesdb WHERE MUTED_UUID = '"+ uuid +"'");
			try {
				if(rs.next()) {
					return rs.getString("REASON");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return "Unknown";
	}
	
	public static void addMute(String uuid) {
		try {
			PreparedStatement st = MySQL.getCon().prepareStatement("UPDATE ReportSystem_playerdb SET MUTES = '"+ (getMutes(uuid) + 1) +"' WHERE UUID = '"+ uuid +"'");
			st.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static int getMutes(String uuid) {
		ResultSet rs = MySQL.getResult("SELECT MUTES FROM ReportSystem_playerdb WHERE UUID = '"+ uuid +"'");
		try {
			if(rs.next()) {
				return rs.getInt("MUTES");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public static boolean submitMute(String targetuuid, String reason, String team) {
		if(MySQL.isConnected()) {
			if(existsMuteReason(reason)) {
				
				long ban_end = getRawMuteLength(reason) + System.currentTimeMillis();
				
				try {
					PreparedStatement st = MySQL.getCon().prepareStatement("INSERT INTO ReportSystem_mutesdb(MUTED_UUID,TEAM_UUID,REASON,MUTE_END) VALUES ('"+ targetuuid +"','"+ team +"','"+ reason +"','"+ ban_end +"')");
					if(st.executeUpdate() > 0) {
						addMute(targetuuid);
						if(SystemManager.existsPlayerData(team)) {
							Player p = Bukkit.getPlayer(SystemManager.getUsernameByUUID(team));
							if(p != null) {
								ReportManager.sendNotify("MUTE", p.getName(), SystemManager.getUsernameByUUID(targetuuid), reason);
							}
						} else {
							ReportManager.sendNotify("MUTE", "Console", SystemManager.getUsernameByUUID(targetuuid), reason);
						}
						return true;
					} else {
						return false;
					}
				} catch (SQLException e) {
					return false;
				}
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
}
