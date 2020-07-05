package de.lacodev.rsystem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import de.lacodev.rsystem.commands.CMD_Ban;
import de.lacodev.rsystem.commands.CMD_BanHistory;
import de.lacodev.rsystem.commands.CMD_BanIP;
import de.lacodev.rsystem.commands.CMD_BanList;
import de.lacodev.rsystem.commands.CMD_BanManager;
import de.lacodev.rsystem.commands.CMD_ChatFilter;
import de.lacodev.rsystem.commands.CMD_ChatLog;
import de.lacodev.rsystem.commands.CMD_Check;
import de.lacodev.rsystem.commands.CMD_Mute;
import de.lacodev.rsystem.commands.CMD_Report;
import de.lacodev.rsystem.commands.CMD_ReportManager;
import de.lacodev.rsystem.commands.CMD_Reports;
import de.lacodev.rsystem.commands.CMD_StaffCore;
import de.lacodev.rsystem.commands.CMD_Unban;
import de.lacodev.rsystem.commands.CMD_Unmute;
import de.lacodev.rsystem.commands.CMD_WebVerify;
import de.lacodev.rsystem.completer.Completer_Ban;
import de.lacodev.rsystem.completer.Completer_BanManager;
import de.lacodev.rsystem.completer.Completer_Mute;
import de.lacodev.rsystem.completer.Completer_Report;
import de.lacodev.rsystem.completer.Completer_ReportManager;
import de.lacodev.rsystem.listeners.Listener_Chat;
import de.lacodev.rsystem.listeners.Listener_ChatLog;
import de.lacodev.rsystem.listeners.Listener_GuardianDMG;
import de.lacodev.rsystem.listeners.Listener_Inventories;
import de.lacodev.rsystem.listeners.Listener_JoinQuit;
import de.lacodev.rsystem.listeners.Listener_Login;
import de.lacodev.rsystem.listeners.Listener_Matrix;
import de.lacodev.rsystem.listeners.Listener_Spartan;
import de.lacodev.rsystem.mysql.MySQL;
import de.lacodev.rsystem.utils.BanManager;
import me.vagdedes.spartan.api.API;
import me.vagdedes.spartan.system.Enums.HackType;
import net.milkbowl.vault.economy.Economy;

public class Main extends JavaPlugin{

	public static Main instance;
	private static Economy econ = null;
	
	public static String prefix;
	
	public static String lizenz;
	
	public boolean latest = false;
	public boolean actionbar = false;
	
	public String host;
	public String port;
	public String database;
	public String username;
	public String password;
	
	public PluginManager pm = Bukkit.getPluginManager();
	public API api = null;
	
	public void onEnable() {
		instance = this;
		
		loadConfigs();
		applyConfigs();
		
		try {
			Bukkit.getConsoleSender().sendMessage("");
			Bukkit.getConsoleSender().sendMessage("§cSystem §8» §7Checking version... §8(§7"+ this.getDescription().getVersion() +"§8)");
			Bukkit.getConsoleSender().sendMessage("");
			checkLizenz(new URL("https://api.spigotmc.org/legacy/update.php?resource=48655"));
		} catch (MalformedURLException e) {
			Bukkit.getConsoleSender().sendMessage("");
			Bukkit.getConsoleSender().sendMessage("§cSystem §8» §c§lFAILED §8(§7Versioncheck§8)");
			Bukkit.getConsoleSender().sendMessage("");
		} catch (IOException e) {
			Bukkit.getConsoleSender().sendMessage("");
			Bukkit.getConsoleSender().sendMessage("§cSystem §8» §c§lFAILED §8(§7Versioncheck§8)");
			Bukkit.getConsoleSender().sendMessage("");
		}
		
		if(lizenz.contains(this.getDescription().getVersion())) {
			latest = true;
			Bukkit.getConsoleSender().sendMessage("");
			Bukkit.getConsoleSender().sendMessage("§cSystem §8» §a§lSUCCESS §8(§7Versioncheck§8)");
			Bukkit.getConsoleSender().sendMessage("");
			Bukkit.getConsoleSender().sendMessage("§aYou are using the latest version!");
			Bukkit.getConsoleSender().sendMessage("");
		} else {
			Bukkit.getConsoleSender().sendMessage("");
			Bukkit.getConsoleSender().sendMessage("§cSystem §8» §a§lSUCCESS §8(§7Versioncheck§8)");
			Bukkit.getConsoleSender().sendMessage("");
			Bukkit.getConsoleSender().sendMessage("§cThere is an update available!");
			Bukkit.getConsoleSender().sendMessage("§8» §ehttps://www.spigotmc.org/resources/staffcore-1-7-1-15.48655/updates");
			Bukkit.getConsoleSender().sendMessage("");
		}
		
		if(getConfig().getBoolean("General.Include-Vault")) {
			if(setupEconomy()) {
				Bukkit.getConsoleSender().sendMessage("");
				Bukkit.getConsoleSender().sendMessage(prefix + "§aSuccessfully §7added Vault to our system");
				Bukkit.getConsoleSender().sendMessage("");
			} else {
				Bukkit.getConsoleSender().sendMessage("");
				Bukkit.getConsoleSender().sendMessage(prefix + "§cFailed §7to add Vault to our system! Please make sure you have Vault installed!");
				Bukkit.getConsoleSender().sendMessage("");
			}
		}
		
		if(getConfig().getBoolean("General.Include-ActionBarAPI")) {
			if(setupActionBar()) {
				Bukkit.getConsoleSender().sendMessage("");
				Bukkit.getConsoleSender().sendMessage(prefix + "§aSuccessfully §7added ActionBarAPI to our system");
				Bukkit.getConsoleSender().sendMessage("");
			} else {
				Bukkit.getConsoleSender().sendMessage("");
				Bukkit.getConsoleSender().sendMessage(prefix + "§cFailed §7to add ActionBarAPI to our system! Please make sure you have ActionBarAPI installed!");
				Bukkit.getConsoleSender().sendMessage("");
			}
		}
		
		if(getConfig().getBoolean("General.Include-MatrixAntiCheat")) {
			if(setupMatrix()) {
				Bukkit.getConsoleSender().sendMessage("");
				Bukkit.getConsoleSender().sendMessage(prefix + "§aSuccessfully §7added Matrix to our system");
				Bukkit.getConsoleSender().sendMessage("");
			} else {
				Bukkit.getConsoleSender().sendMessage("");
				Bukkit.getConsoleSender().sendMessage(prefix + "§cFailed §7to add Matrix to our system! Please make sure you have Matrix installed and set up properly!");
				Bukkit.getConsoleSender().sendMessage("");
			}
		}
		
		if(getConfig().getBoolean("General.Include-SpartanAntiCheat")) {
			if(setupSpartanAC()) {
				api = new API();
				Bukkit.getConsoleSender().sendMessage("");
				Bukkit.getConsoleSender().sendMessage(prefix + "§aSuccessfully §7added SpartanAntiCheat to our system");
				Bukkit.getConsoleSender().sendMessage("");
			} else {
				Bukkit.getConsoleSender().sendMessage("");
				Bukkit.getConsoleSender().sendMessage(prefix + "§cFailed §7to add SpartanAntiCheat to our system! Please make sure you have SpartanAntiCheat installed and set up properly!");
				Bukkit.getConsoleSender().sendMessage("");
			}
		}
		
		MySQL.connect();
		MySQL.createTable();
		
		if(MySQL.isConnected()) {
			registerCommands();
			registerEvents();
			
			for(Sound s : Sound.values()) {
				BanManager.sound_enums.add(s.name().toUpperCase());
			}
			
			Bukkit.getConsoleSender().sendMessage("");
			Bukkit.getConsoleSender().sendMessage("§cSystem §8» §7Environment: " + Bukkit.getVersion());
			Bukkit.getConsoleSender().sendMessage("§cSystem §8» §a§lSUCCESS §8(§aPLUGIN STARTED§8)");
			Bukkit.getConsoleSender().sendMessage("");
		} else {
			Bukkit.getConsoleSender().sendMessage("");
			Bukkit.getConsoleSender().sendMessage("§cSystem §8» §7Environment: " + Bukkit.getVersion());
			Bukkit.getConsoleSender().sendMessage("§cSystem §8» §c§lERROR §8(§cNO CONNECTION§8)");
			Bukkit.getConsoleSender().sendMessage("");
		}
	}
	
	public boolean setupMatrix() {
        if (getServer().getPluginManager().getPlugin("Matrix") == null) {
            return false;
        } else {
        	return true;
        }
	}

	public boolean setupActionBar() {
        if (getServer().getPluginManager().getPlugin("ActionBarAPI") == null) {
            return false;
        } else {
        	actionbar = true;
        	return true;
        }
	}
	
	public boolean setupSpartanAC() {
        if (getServer().getPluginManager().getPlugin("SpartanAPI") == null && getServer().getPluginManager().getPlugin("Spartan") == null) {
            return false;
        } else {
        	return true;
        }
	}

	private void registerEvents() {
		pm.registerEvents(new Listener_JoinQuit(), this);
		pm.registerEvents(new Listener_Inventories(), this);
		pm.registerEvents(new Listener_Login(), this);
		pm.registerEvents(new Listener_Chat(), this);
		pm.registerEvents(new Listener_ChatLog(), this);
		pm.registerEvents(new Listener_GuardianDMG(), this);
		if(setupMatrix()) {
			pm.registerEvents(new Listener_Matrix(), this);
		}
		if(setupSpartanAC()) {
			pm.registerEvents(new Listener_Spartan(), this);
		}
	}

	private void registerCommands() {
		// StaffCore Command
		getCommand("staffcore").setExecutor(new CMD_StaffCore());
		
		// Report Command
		getCommand("report").setExecutor(new CMD_Report());
		getCommand("report").setTabCompleter(new Completer_Report());
		
		// Reports Command
		getCommand("reports").setExecutor(new CMD_Reports());
		
		// Check Command
		getCommand("check").setExecutor(new CMD_Check());
		
		// ReportManager Command
		getCommand("reportmanager").setExecutor(new CMD_ReportManager());
		getCommand("reportmanager").setTabCompleter(new Completer_ReportManager());
		
		// BanManager Command
		getCommand("banmanager").setExecutor(new CMD_BanManager());
		getCommand("banmanager").setTabCompleter(new Completer_BanManager());
		
		// Ban Command
		getCommand("ban").setExecutor(new CMD_Ban());
		getCommand("ban").setTabCompleter(new Completer_Ban());
		
		// Mute Command
		getCommand("mute").setExecutor(new CMD_Mute());
		getCommand("mute").setTabCompleter(new Completer_Mute());
		
		// Unban Command
		getCommand("unban").setExecutor(new CMD_Unban());
		
		// Unmute Command
		getCommand("unmute").setExecutor(new CMD_Unmute());
		
		// Chatfilter Command
		getCommand("chatfilter").setExecutor(new CMD_ChatFilter());
		
		// WebVerify Command
		getCommand("webverify").setExecutor(new CMD_WebVerify());
		
		// ChatLog Command
		getCommand("chatlog").setExecutor(new CMD_ChatLog());
		
		// BanList Command
		getCommand("banlist").setExecutor(new CMD_BanList());
		
		// BanIP Command
		getCommand("banip").setExecutor(new CMD_BanIP());
		
		// BanHistory Command
		getCommand("banhistory").setExecutor(new CMD_BanHistory());
	}

	public void onDisable() {
		MySQL.disconnect();
	}

	private void loadConfigs() {
		Bukkit.getConsoleSender().sendMessage("");
		Bukkit.getConsoleSender().sendMessage("§cSystem §8» §7Generating... §8(§econfig.yml§8)");
		Bukkit.getConsoleSender().sendMessage("");
		
		getConfig().options().copyDefaults(true);
		
		getConfig().addDefault("MySQL.HOST", "host");
		getConfig().addDefault("MySQL.PORT", "3306");
		getConfig().addDefault("MySQL.DATABASE", "database");
		getConfig().addDefault("MySQL.USERNAME", "username");
		getConfig().addDefault("MySQL.PASSWORD", "password");
		
		getConfig().addDefault("General.System-Prefix", "&cSystem &8%double_arrow% ");
		getConfig().addDefault("General.Include-Worldname", false);
		getConfig().addDefault("General.Include-Vault", false);
		getConfig().addDefault("General.Include-ActionBarAPI", false);
		getConfig().addDefault("General.Include-MatrixAntiCheat", false);
		getConfig().addDefault("General.Include-SpartanAntiCheat", false);
		getConfig().addDefault("General.Force-SpectatorMode", true);
		getConfig().addDefault("General.Restart-Plugin-On-Reload", true);
		
		getConfig().addDefault("Ban-Animation.Enable", true);
		getConfig().addDefault("Ban-Animation.Type", "GUARDIAN");
		getConfig().addDefault("Ban-Animation.Sound", "ENTITY_ENDER_DRAGON_DEATH");
		
		getConfig().addDefault("Vault.Rewards.Report.MIN", 50);
		getConfig().addDefault("Vault.Rewards.Report.MAX", 150);
		
		getConfig().addDefault("Report-Spam.Duration-in-Seconds", 20);
		
		getConfig().addDefault("ChatLog.Reason", "ChatLog");
		getConfig().addDefault("ChatLog.Max-Message-Count", 200);
		getConfig().addDefault("ChatLog.Save-On-Reload", true);
		getConfig().addDefault("ChatLog.Cooldown-In-Seconds", 120);
		getConfig().addDefault("ChatLog.WebInterface-URL", "https://yourserver.com/WebInterface/chatlog/?id=");
		
		getConfig().addDefault("MatrixAntiCheat.Autoreport.Enable", false);
		getConfig().addDefault("MatrixAntiCheat.Autoreport.Name", "MatrixAC");
		getConfig().addDefault("MatrixAntiCheat.Autoreport.Log.Reset-Violations-On-Join", true);
		if(setupMatrix()) {
			for(me.rerere.matrix.api.HackType matrix : me.rerere.matrix.api.HackType.values()) {
				getConfig().addDefault("MatrixAntiCheat.Autoreport." + matrix.toString() + ".Displayname", "" + matrix.toString().substring(0, 1).toUpperCase() + matrix.toString().substring(1).toLowerCase());
				getConfig().addDefault("MatrixAntiCheat.Autoreport." + matrix.toString() + ".Violationslevel", 20);
			}
		}
		
		getConfig().addDefault("SpartanAntiCheat.Autoreport.Enable", false);
		getConfig().addDefault("SpartanAntiCheat.Autoreport.Name", "SpartanAC");
		getConfig().addDefault("SpartanAntiCheat.Autoreport.Log.Reset-Violations-On-Join", true);
		if(setupSpartanAC()) {
			for(HackType h : HackType.values()) {
				getConfig().addDefault("SpartanAntiCheat.Autoreport."+ h.toString() + ".Displayname", "" + h.toString().substring(0, 1).toUpperCase() + h.toString().substring(1).toLowerCase());
				getConfig().addDefault("SpartanAntiCheat.Autoreport."+ h.toString() + ".Violationslevel", 20);
			}
		}
		
		getConfig().addDefault("Unpermitted-OP.Kick-Player.Enable", true);
		getConfig().addDefault("Unpermitted-OP.Kick-Player.Message", "\n &cYou got kicked from the server! \n \n &7We have detected that you are not allowed to be "
				+ "\n an operator on this server! \n \n &aPlease contact an administrator");
		getConfig().addDefault("Unpermitted-OP.Staff-Notify", "&c%target% &7tries to join as operator, but does not have the permission to do so!");
		
		getConfig().addDefault("IP-Ban.Ban-IP-When-Player-Gets-Banned", false);
		getConfig().addDefault("IP-Ban.Duration-in-Hours", 24);
		getConfig().addDefault("IP-Ban.Usage", "&cUse: &7/banip <Player>");
		getConfig().addDefault("IP-Ban.Kick-Message", "&cYour IP was banned from this server! \n \n &cIf you think this is an mistake, please contact an admin!");
		getConfig().addDefault("IP-Ban.Notify", "&a%player% &7banned &c%target%'s &7IP-Address for &e%duration%");
		getConfig().addDefault("IP-Ban.Already-banned", "&cThe IP Address of %target% is already banned");
		getConfig().addDefault("IP-Ban.Target-Offline", "&cThe player is offline");
		
		getConfig().addDefault("Messages.No-Connection.Notify", "&cWe are currently having some difficulties with our systems! Please try again later!");
		getConfig().addDefault("Messages.Reload.Success", "&7StaffCore was &asuccessfully &7reloaded");
		getConfig().addDefault("Messages.Report.Usage.Title", "&8[&eReport-Help&8]");
		getConfig().addDefault("Messages.Report.Usage.Command", "&e/report <Player> <Template> &8- §7Report a player");
		getConfig().addDefault("Messages.Report.Usage.Inventory", "&e/report <Player> &8- &7Report a player with GUI");
		getConfig().addDefault("Messages.Report.Usage.Template", "&e/report templates &8- &7Display all Reportreasons");
		getConfig().addDefault("Messages.Report.No-Reportreasons", "&cWe couldnt find any Reasons to Report any player");
		getConfig().addDefault("Messages.Report.Target-offline", "&cThe player is offline");
		getConfig().addDefault("Messages.Report.Cannot-report-self", "&cYou cannot report yourself");
		getConfig().addDefault("Messages.Report.Inventory.Title", "&7Report &8- ");
		getConfig().addDefault("Messages.Report.Inventory.Itemname", "&e%reasonname%");
		getConfig().addDefault("Messages.Report.Notify.Team.Reported", "&7The player &a%player% &7reported &c%target%");
		getConfig().addDefault("Messages.Report.Notify.Team.Worldname", "&7World: &e%worldname%");
		getConfig().addDefault("Messages.Report.Notify.Team.Teleport-Button", "&eClick here to teleport!");
		getConfig().addDefault("Messages.Report.Notify.Team.Already-Claimed", "&cThis report already got claimed!");
		getConfig().addDefault("Messages.Report.Notify.Team.Claimed", "&7You claimed the report of &c%target%");
		getConfig().addDefault("Messages.Report.Notify.User.Report-Created", "&aYou created a report! Thanks for your help");
		getConfig().addDefault("Messages.Report.Notify.User.Team-Claimed-Report", "&7An staffmember is &anow &7taking care of your report!");
		getConfig().addDefault("Messages.Report.Inventory.ReportItem-Name.Color", "YELLOW");
		getConfig().addDefault("Messages.Report.Inventory.ReportItem-Lore.1", "&7Report &c%target%");
		getConfig().addDefault("Messages.Report.Inventory.ReportItem-Lore.2", "&7for &e%reason%");
		getConfig().addDefault("Messages.Report.Inventory.PreviousPage.Available", "&aGo to the previous page!");
		getConfig().addDefault("Messages.Report.Inventory.PreviousPage.NotAvailable", "&cNo previous page available!");
		getConfig().addDefault("Messages.Report.Inventory.NextPage.Available", "&aGo to the next page!");
		getConfig().addDefault("Messages.Report.Inventory.NextPage.NotAvailable", "&cNo next page available!");
		getConfig().addDefault("Messages.Report.AntiSpam", "&cPlease wait a few seconds before reporting again!");
		getConfig().addDefault("Messages.Reports.Title", "&8[&eAll Reports&8]");
		getConfig().addDefault("Messages.Reports.Usage", "&cUse: &7/reports");
		getConfig().addDefault("Messages.Reports.Layout", "&c%target% &8- %status%");
		getConfig().addDefault("Messages.Reports.Layout-Hover-Title", "&7The player was reported for the following:");
		getConfig().addDefault("Messages.Reports.No-Open", "&cAt the moment there are no reports open");
		getConfig().addDefault("Messages.ReportManager.Add-Reason.No-Item-In-Hand", "&cYou need to have an item in your hand!");
		getConfig().addDefault("Messages.ReportManager.Add-Reason.Already-Exists", "&cThis reason already exists");
		getConfig().addDefault("Messages.ReportManager.Add-Reason.Successful", "&7You &asuccessfully &7created the reason!");
		getConfig().addDefault("Messages.ReportManager.Remove-Reason.Not-Exists", "&cThis reason does not exist");
		getConfig().addDefault("Messages.ReportManager.Remove-Reason.Successful", "&7You &asuccessfully &7removed the reason!");
		getConfig().addDefault("Messages.Check.Title", "&8[&eCheck Player&8]");
		getConfig().addDefault("Messages.Check.No-Player-Found", "&cThis player could not be found");
		getConfig().addDefault("Messages.BanManager.Add-Reason.Invalid-TimeUnit", "&cInvalid TimeUnit! Please use d/h/m");
		getConfig().addDefault("Messages.BanManager.Add-Reason.Invalid-ReasonType", "&cInvalid Reasontype! Please use ban/mute");
		getConfig().addDefault("Messages.BanManager.Add-Reason.Already-Existing", "&cThis reason already exists");
		getConfig().addDefault("Messages.BanManager.Add-Reason.Successful", "&7You &asuccessfully &7created the reason");
		getConfig().addDefault("Messages.BanManager.Remove-Reason.Not-Exists", "&cThis reason does not exist");
		getConfig().addDefault("Messages.BanManager.Remove-Reason.Successful", "&7You &asuccessfully &7removed the reason!");
		getConfig().addDefault("Messages.Ban.Usage", "&cUse: &7/ban <Player> <Reason>");
		getConfig().addDefault("Messages.Ban.No-Reasons", "&cThere are no Ban-Reasons");
		getConfig().addDefault("Messages.Ban.Cannot-find-player", "&cThis player never joined the server!");
		getConfig().addDefault("Messages.Ban.Cannot-ban-self", "&cYou can not ban yourself!");
		getConfig().addDefault("Messages.Ban.Already-Banned", "&cThis player is already banned!");
		getConfig().addDefault("Messages.Ban.Created", "&aBan created");
		getConfig().addDefault("Messages.Ban.NotValidID", "&cThe given ID is not a number!");
		getConfig().addDefault("Messages.Ban.List.Empty", "&cNo active bans found!");
		getConfig().addDefault("Messages.Ban.List.Error", "&cBanlist could not be loaded");
		getConfig().addDefault("Messages.Ban.Reason-Not-Exists", "&cPlease use an valid reason to ban this player!");
		getConfig().addDefault("Messages.Ban.Kick-Player-If-Banned", "&cYou were banned from this server! \n \n Reason &8> &e%reason%");
		getConfig().addDefault("Messages.Ban.Notify.Team.Banned", "&c%target% &7was banned by &a%player%");
		getConfig().addDefault("Messages.Ban.Inventory.Title", "&7Ban &8- ");
		getConfig().addDefault("Messages.Ban.Inventory.Itemname", "&e%reasonname%");
		getConfig().addDefault("Messages.Ban.Inventory.BanItem-Name.Color", "YELLOW");
		getConfig().addDefault("Messages.Ban.Inventory.BanItem-Lore.1", "&7Ban &c%target%");
		getConfig().addDefault("Messages.Ban.Inventory.BanItem-Lore.2", "&7for &e%reason%");
		getConfig().addDefault("Messages.Ban.Inventory.PreviousPage.Available", "&aGo to the previous page!");
		getConfig().addDefault("Messages.Ban.Inventory.PreviousPage.NotAvailable", "&cNo previous page available!");
		getConfig().addDefault("Messages.Ban.Inventory.NextPage.Available", "&aGo to the next page!");
		getConfig().addDefault("Messages.Ban.Inventory.NextPage.NotAvailable", "&cNo next page available!");
		getConfig().addDefault("Messages.Mute.Usage", "&cUse: &7/mute <Player> <Reason>");
		getConfig().addDefault("Messages.Mute.No-Reasons", "&cThere are no Mute-Reasons");
		getConfig().addDefault("Messages.Mute.Cannot-find-player", "&cThis player never joined the server!");
		getConfig().addDefault("Messages.Mute.Cannot-mute-self", "&cYou can not mute yourself!");
		getConfig().addDefault("Messages.Mute.Already-Muted", "&cThis player is already muted!");
		getConfig().addDefault("Messages.Mute.Created", "&aMute created");
		getConfig().addDefault("Messages.Mute.Error", "&cMute could not be created");
		getConfig().addDefault("Messages.Mute.Reason-Not-Exists", "&cPlease use an valid reason to mute this player!");
		getConfig().addDefault("Messages.Mute.Message-If-Player-Muted", "&cYou were muted from the chat! \n \n Reason &8> &e%reason%");
		getConfig().addDefault("Messages.Mute.Notify.Team.Muted", "&c%target% &7was muted by &a%player%");
		getConfig().addDefault("Messages.Mute.Inventory.Title", "&7Mute &8- ");
		getConfig().addDefault("Messages.Mute.Inventory.Itemname", "&e%reasonname%");
		getConfig().addDefault("Messages.Mute.Inventory.MuteItem-Name.Color", "YELLOW");
		getConfig().addDefault("Messages.Mute.Inventory.MuteItem-Lore.1", "&7Mute &c%target%");
		getConfig().addDefault("Messages.Mute.Inventory.MuteItem-Lore.2", "&7for &e%reason%");
		getConfig().addDefault("Messages.Mute.Inventory.PreviousPage.Available", "&aGo to the previous page!");
		getConfig().addDefault("Messages.Mute.Inventory.PreviousPage.NotAvailable", "&cNo previous page available!");
		getConfig().addDefault("Messages.Mute.Inventory.NextPage.Available", "&aGo to the next page!");
		getConfig().addDefault("Messages.Mute.Inventory.NextPage.NotAvailable", "&cNo next page available!");
		getConfig().addDefault("Messages.UnBan.Usage", "&cUse: &7/unban <Player>");
		getConfig().addDefault("Messages.UnBan.Cannot-find-player", "&cThis player never joined the server!");
		getConfig().addDefault("Messages.UnBan.Not-Banned", "&cThis player is not banned!");
		getConfig().addDefault("Messages.UnBan.Success", "&7Player was &asuccessfully &7unbanned!");
		getConfig().addDefault("Messages.UnBan.Error", "&cPlayer could not be unbanned");
		getConfig().addDefault("Messages.UnBan.Notify.Team.Unban", "&c%target% &7was unbanned by &a%player%");
		getConfig().addDefault("Messages.UnBan.Notify.Team.ConsoleName", "Automatic Cloud");
		getConfig().addDefault("Messages.UnMute.Usage", "&cUse: &7/unmute <Player>");
		getConfig().addDefault("Messages.UnMute.Cannot-find-player", "&cThis player never joined the server!");
		getConfig().addDefault("Messages.UnMute.Not-Muted", "&cThis player is not muted!");
		getConfig().addDefault("Messages.UnMute.Success", "&7Player was &asuccessfully &7unmuted!");
		getConfig().addDefault("Messages.UnMute.Error", "&cPlayer could not be unbanned");
		getConfig().addDefault("Messages.UnMute.Notify.Team.Unmute", "&c%target% &7was unmuted by &a%player%");
		getConfig().addDefault("Messages.UnMute.Notify.Team.ConsoleName", "Automatic Cloud");
		getConfig().addDefault("Messages.Vault.Rewards.Report.Success", "&7Due to your report, we could punish an rulebreaker thanks for your help! &eReward: %reward%");
		getConfig().addDefault("Messages.Vault.Rewards.Report.Success-While-Offline", "&7While you were offline, our staff has been taking care of %count% Reports submitted by you!");
		getConfig().addDefault("Messages.WebVerify.Usage", "&cUse: &7/webverify <Token>");
		getConfig().addDefault("Messages.WebVerify.Invalid-Token", "&cThis token is invalid or could not be validated by the server!");
		getConfig().addDefault("Messages.WebVerify.Successfully-Verified", "&7Your account is &anow &7verified! You can now log in the dashboard");
		getConfig().addDefault("Messages.ChatLog.Usage", "&cUse &7/chatlog <Player>");
		getConfig().addDefault("Messages.ChatLog.Player-Offline", "&cThis player is currently offline");
		getConfig().addDefault("Messages.ChatLog.Cannot-Chatlog-Self", "&cYou cant chatlog yourself!");
		getConfig().addDefault("Messages.ChatLog.Player-Wrote-No-Message", "&cThis player never sent a message!");
		getConfig().addDefault("Messages.ChatLog.Notify.Team.Creator", "&a%player% &7has created a chatlog for &c%target%");
		getConfig().addDefault("Messages.ChatLog.Notify.User.Creator", "&aYou have created a chatlog for &7%target%");
		getConfig().addDefault("Messages.ChatLog.Notify.User.Link-Text", "&7You can view this chatlog: &eHERE");
		getConfig().addDefault("Messages.ChatLog.Cooldown", "&cThere is already a recent chatlog for this player");
		getConfig().addDefault("Messages.BanHistory.Usage", "&cUse: &7/banhistory <Player>");
		getConfig().addDefault("Messages.BanHistory.Target-Never-Joined", "&cThis player never joined the server!");
		getConfig().addDefault("Messages.BanHistory.Cleared", "&7The banhistory was &acleared");
		getConfig().addDefault("Messages.BanHistory.No-History-Available", "&cThis player has no banhistory!");
		getConfig().addDefault("Messages.BanHistory.History-too-long", "&cThe BanHistory is too long for the chat! You only see 10 entries");
		
		getConfig().addDefault("Layout.Ban.Length-Values.Temporarly", "&ctemporarly");
		getConfig().addDefault("Layout.Ban.Length-Values.Permanently", "&cPERMANENTLY");
		getConfig().addDefault("Layout.Ban.Remaining.Seconds", "&eSecond(s)");
		getConfig().addDefault("Layout.Ban.Remaining.Minutes", "&eMinute(s)");
		getConfig().addDefault("Layout.Ban.Remaining.Hours", "&eHour(s)");
		getConfig().addDefault("Layout.Ban.Remaining.Days", "&eDay(s)");
		
		ArrayList<String> ban_layout = new ArrayList<>();
		
		ban_layout.add("");
		ban_layout.add("&cYou were %lengthvalue% banned from this server!");
		ban_layout.add("");
		ban_layout.add("&7Reason &8> &e%reason%");
		ban_layout.add("");
		ban_layout.add("&7Remaining &8> &e%remaining%");
		ban_layout.add("");
		ban_layout.add("&aYou think this was an mistake? Please let us know!");
		ban_layout.add("&7Our Forum: &eyourforum.com");
		ban_layout.add("");
		
		getConfig().addDefault("Layout.Ban.LAYOUT", ban_layout);
		
		
		getConfig().addDefault("Layout.Mute.Remaining.Seconds", "&eSecond(s)");
		getConfig().addDefault("Layout.Mute.Remaining.Minutes", "&eMinute(s)");
		getConfig().addDefault("Layout.Mute.Remaining.Hours", "&eHour(s)");
		getConfig().addDefault("Layout.Mute.Remaining.Days", "&eDay(s)");
		
		ArrayList<String> mute_layout = new ArrayList<>();
		
		mute_layout.add("");
		mute_layout.add("&cYou were muted from the chat");
		mute_layout.add("");
		mute_layout.add("&7Reason &8> &e%reason%");
		mute_layout.add("");
		mute_layout.add("&7Remaining &8> &e%remaining%");
		mute_layout.add("");
		mute_layout.add("&aYou think this was an mistake? Please let us know!");
		mute_layout.add("&7Our Forum: &eyourforum.com");
		mute_layout.add("");
		
		getConfig().addDefault("Layout.Mute.LAYOUT", mute_layout);
		
		getConfig().addDefault("Permissions.Everything", "rsystem.*");
		getConfig().addDefault("Permissions.System.Reload", "rsystem.reload");
		getConfig().addDefault("Permissions.Allow-OP.Join", "rsystem.op.allow");
		getConfig().addDefault("Permissions.Allow-OP.Notify", "rsystem.op.notify");
		getConfig().addDefault("Permissions.IpBan.Use", "rsystem.ipban.use");
		getConfig().addDefault("Permissions.IpBan.Notify", "rsystem.ipban.notify");
		getConfig().addDefault("Permissions.Report.Notify", "rsystem.report.notify");
		getConfig().addDefault("Permissions.Report.Claim", "rsystem.report.claim");
		getConfig().addDefault("Permissions.Report.Spam.Bypass", "rsystem.report.spam.bypass");
		getConfig().addDefault("Permissions.ReportManager.addreason", "rsystem.reportmanager.addreason");
		getConfig().addDefault("Permissions.ReportManager.removereason", "rsystem.reportmanager.removereason");
		getConfig().addDefault("Permissions.Reports.See", "rsystem.reports.see");
		getConfig().addDefault("Permissions.Check.Use", "rsystem.check.use");
		getConfig().addDefault("Permissions.BanManager.addreason", "rsystem.banmanager.addreason");
		getConfig().addDefault("Permissions.BanManager.removereason", "rsystem.banmanager.removereason");
		getConfig().addDefault("Permissions.Ban.Use", "rsystem.ban.use");
		getConfig().addDefault("Permissions.Ban.Notify", "rsystem.ban.notify");
		getConfig().addDefault("Permissions.Ban.List", "rsystem.ban.list");
		getConfig().addDefault("Permissions.Mute.Use", "rsystem.mute.use");
		getConfig().addDefault("Permissions.Mute.Notify", "rsystem.mute.notify");
		getConfig().addDefault("Permissions.UnBan.Use", "rsystem.unban.use");
		getConfig().addDefault("Permissions.UnBan.Notify", "rsystem.unban.notify");
		getConfig().addDefault("Permissions.UnMute.Use", "rsystem.unmute.use");
		getConfig().addDefault("Permissions.UnMute.Notify", "rsystem.unmute.notify");
		getConfig().addDefault("Permissions.Chatfilter.Manage", "rsystem.chatfilter.manage");
		getConfig().addDefault("Permissions.ChatLog.Notify", "rsystem.chatlog.notify");
		getConfig().addDefault("Permissions.BanHistory.See", "rsystem.banhistory.see");
		
		getConfig().addDefault("Chatfilter.ReporterName", "ChatController");
		getConfig().addDefault("Chatfilter.Cursed-Words.Match-for-action-in-Percentage", 50);
		getConfig().addDefault("Chatfilter.Cursed-Words.AutoReport.Enable", true);
		getConfig().addDefault("Chatfilter.Cursed-Words.AutoReport.Reason", "Insult");
		getConfig().addDefault("Chatfilter.Cursed-Words.Block-Message.Enable", true);
		getConfig().addDefault("Chatfilter.Cursed-Words.Block-Message.Message", "&cPlease mind your language");
		getConfig().addDefault("Chatfilter.Advertisment.AutoReport.Enable", true);
		getConfig().addDefault("Chatfilter.Advertisment.AutoReport.Reason", "Advertisment");
		getConfig().addDefault("Chatfilter.Advertisment.Block-Message.Enable", true);
		getConfig().addDefault("Chatfilter.Advertisment.Block-Message.Message", "&cPlease dont advertise.");
		
		ArrayList<String> whitelist = new ArrayList<>();
		
		whitelist.add("yourserver.de");
		whitelist.add("forum.yourserver.de");
		whitelist.add("ts.yourserver.de");
		whitelist.add("shop.yourserver.de");
		
		getConfig().addDefault("Chatfilter.Advertisment.Whitelist", whitelist);
		
		ArrayList<String> blocked = new ArrayList<>();
		
		blocked.add(".ac");
		blocked.add(".ad");
		blocked.add(".ae");
		blocked.add(".aero");
		blocked.add(".af");
		blocked.add(".ag");
		blocked.add(".ai");
		blocked.add(".al");
		blocked.add(".am");
		blocked.add(".an");
		blocked.add(".am");
		blocked.add(".an");
		blocked.add(".ao");
		blocked.add(".aq");
		blocked.add(".ar");
		blocked.add(".as");
		blocked.add(".asia");
		blocked.add(".at");
		blocked.add(".au");
		blocked.add(".aw");
		blocked.add(".ax");
		blocked.add(".az");
		blocked.add(".de");
		blocked.add(".com");
		blocked.add(".net");
		blocked.add(".org");
		blocked.add(".eu");
		blocked.add(".be");
		blocked.add(".nl");
		blocked.add(".me");
		blocked.add(".ch");
		blocked.add(".info");
		blocked.add(".dev");
		
		getConfig().addDefault("Chatfilter.Advertisment.Blocked-Domains", blocked);
		
		saveConfig();
		
		Bukkit.getConsoleSender().sendMessage("");
		Bukkit.getConsoleSender().sendMessage("§cSystem §8» §7Generated §8(§econfig.yml§8)");
		Bukkit.getConsoleSender().sendMessage("");
	}

	private void applyConfigs() {
		Bukkit.getConsoleSender().sendMessage("");
		Bukkit.getConsoleSender().sendMessage("§cSystem §8» §7Applying values... §8(§econfig.yml§8)");
		Bukkit.getConsoleSender().sendMessage("");
		
		host = getConfig().getString("MySQL.HOST");
		port = getConfig().getString("MySQL.PORT");
		database = getConfig().getString("MySQL.DATABASE");
		username = getConfig().getString("MySQL.USERNAME");
		password = getConfig().getString("MySQL.PASSWORD");
		
		if(getConfig().getString("General.System-Prefix").contains("%double_arrow%")) {
			prefix = ChatColor.translateAlternateColorCodes('&', getConfig().getString("General.System-Prefix").replace("%double_arrow%", "»"));
		} else {
			prefix = ChatColor.translateAlternateColorCodes('&', getConfig().getString("General.System-Prefix"));
		}
		
		Bukkit.getConsoleSender().sendMessage("");
		Bukkit.getConsoleSender().sendMessage("§cSystem §8» §7Applied values §8(§econfig.yml§8)");
		Bukkit.getConsoleSender().sendMessage("");
		
	}
	
	public static String getMSG(String config) {
		if(getInstance().getConfig().getString(config) != null) {
			return ChatColor.translateAlternateColorCodes('&', getInstance().getConfig().getString(config));
		} else {
			Bukkit.getConsoleSender().sendMessage("");
			Bukkit.getConsoleSender().sendMessage("§cSystem §8» §cFAILED §8(§7Message Output§8)");
			Bukkit.getConsoleSender().sendMessage("");
			return null;
		}
	}
	
	public static String getPermissionNotice(String config) {
		if(getInstance().getConfig().getString(config) != null) {
			return ChatColor.stripColor(getInstance().getConfig().getString(config));
		} else {
			Bukkit.getConsoleSender().sendMessage("");
			Bukkit.getConsoleSender().sendMessage("§cSystem §8» §cFAILED §8(§7Message Output§8)");
			Bukkit.getConsoleSender().sendMessage("");
			return null;
		}
	}

	public void checkLizenz(URL url) throws IOException{
		try {
            String parseLine; /* variable definition *//* create objects */           
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));

            while ((parseLine = br.readLine()) != null) {
                lizenz = parseLine;
            }
            br.close();

        } catch (MalformedURLException me) {
            System.out.println(me);

        } catch (IOException ioe) {
            System.out.println(ioe);
        }
	}
	
	public static String getPrefix() {
		return prefix;
	}
	
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
    
	public static Main getInstance() {
		return instance;
	}

	public static Economy getEconomy() {
		return econ;
	}
	
}
