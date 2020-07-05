package de.lacodev.rsystem.commands;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import de.lacodev.rsystem.Main;
import de.lacodev.rsystem.listeners.Listener_ChatLog;
import de.lacodev.rsystem.mysql.MySQL;
import de.lacodev.rsystem.utils.ReportManager;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;

public class CMD_ChatLog implements CommandExecutor {
	
	public static HashMap<Player, Long> antispam = new HashMap<>();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			
			Player p = (Player)sender;
			
			if(args.length == 1) {
				Player target = Bukkit.getPlayer(args[0]);
				
				if(antispam.containsKey(target)) {
					if(antispam.get(target) < System.currentTimeMillis()) {
						antispam.remove(target);
						if(target != null) {
							if(target != p) {
								if(Main.getInstance().getConfig().getBoolean("ChatLog.Save-On-Reload")) {
									File file = new File("plugins//" + Main.getInstance().getDescription().getName() + "//chatlog.yml");
									YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
									
									if(cfg.contains("ChatLogs." + target.getUniqueId().toString() + ".Messages")) {
										ArrayList<String> msgs = (ArrayList<String>) cfg.getStringList("ChatLogs." + target.getUniqueId().toString() + ".Messages");
										
										if(msgs.size() != 0) {
											new BukkitRunnable() {

												@Override
												public void run() {
										            String buchstaben = "QWERTZUIOPASDFGHJKLYXCVBNMqwertzuiopasdfghjklyxcvbnm123456789";
										            String code = "";
										            int codelength = 25;
										            
										            Random rand = new Random();
										            char[] text = new char[codelength];
										            for (int i = 0; i < codelength; i++) {
										              text[i] = buchstaben.charAt(rand.nextInt(buchstaben.length()));
										            }
										            for (int i = 0; i < text.length; i++) {
										              code = code + text[i];
										            }
													
													for(int i = 0; i < msgs.size(); i++) {
														MySQL.update("INSERT INTO ReportSystem_chatlogs(CHATLOG_ID,REPORTED_UUID,MESSAGE,STATUS) VALUES ('"+ code +"','"+ target.getUniqueId().toString() +"','"+ msgs.get(i) +"','0')");
													}
													ReportManager.sendNotify("CHATLOG", p.getName(), target.getName(), code);
													p.sendMessage("");
													p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.ChatLog.Notify.User.Creator").replace("%target%", target.getName()));
													TextComponent tc = new TextComponent();
													
													tc.setText(Main.getPrefix() + Main.getMSG("Messages.ChatLog.Notify.User.Link-Text"));
													tc.setClickEvent(new ClickEvent(Action.OPEN_URL, Main.getPermissionNotice("ChatLog.WebInterface-URL") + code));
													
													p.spigot().sendMessage(tc);
													p.sendMessage("");
													
													msgs.clear();
													cfg.set("ChatLogs." + target.getUniqueId().toString() + ".Messages", msgs);
													try {
														cfg.save(file);
													} catch (IOException e) {
														Bukkit.getConsoleSender().sendMessage(Main.getPrefix() + "§4ERROR §7(§cClear ChatLog-MSG§7)");
													}
													antispam.put(target, System.currentTimeMillis() + 1000 * Main.getInstance().getConfig().getInt("ChatLog.Cooldown-In-Seconds"));
												}
											}.runTaskAsynchronously(Main.getInstance());
										} else {
											p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.ChatLog.Player-Wrote-No-Message"));
										}
									} else {
										p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.ChatLog.Player-Wrote-No-Message"));
									}
								} else {
									if(Listener_ChatLog.chatlog.containsKey(p)) {
										new BukkitRunnable() {

											@Override
											public void run() {
												ArrayList<String> chat = Listener_ChatLog.chatlog.get(p);
												
												String buchstaben = "QWERTZUIOPASDFGHJKLYXCVBNMqwertzuiopasdfghjklyxcvbnm123456789";
									            String code = "";
									            int codelength = 25;
									            
									            Random rand = new Random();
									            char[] text = new char[codelength];
									            for (int i = 0; i < codelength; i++) {
									              text[i] = buchstaben.charAt(rand.nextInt(buchstaben.length()));
									            }
									            for (int i = 0; i < text.length; i++) {
									              code = code + text[i];
									            }
												
												for(int i = 0; i < chat.size(); i++) {
													MySQL.update("INSERT INTO ReportSystem_chatlogs(CHATLOG_ID,REPORTED_UUID,MESSAGE,STATUS) VALUES ('"+ code +"','"+ target.getUniqueId().toString() +"','"+ chat.get(i) +"','0')");
												}
												ReportManager.sendNotify("CHATLOG", p.getName(), target.getName(), code);
												p.sendMessage("");
												p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.ChatLog.Notify.User.Creator").replace("%target%", target.getName()));
												TextComponent tc = new TextComponent();
												
												tc.setText(Main.getPrefix() + Main.getMSG("Messages.ChatLog.Notify.User.Link-Text"));
												tc.setClickEvent(new ClickEvent(Action.OPEN_URL, Main.getPermissionNotice("ChatLog.WebInterface-URL") + code));
												
												p.spigot().sendMessage(tc);
												p.sendMessage("");
												antispam.put(target, System.currentTimeMillis() + 1000 * Main.getInstance().getConfig().getInt("ChatLog.Cooldown-In-Seconds"));
											}
										}.runTaskAsynchronously(Main.getInstance());
									} else {
										p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.ChatLog.Player-Wrote-No-Message"));
									}
								}
							} else {
								p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.ChatLog.Cannot-Chatlog-Self"));
							}
						} else {
							p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.ChatLog.Player-Offline"));
						}
					} else {
						p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.ChatLog.Cooldown"));
					}
				} else {
					if(target != null) {
						if(target != p) {
							if(Main.getInstance().getConfig().getBoolean("ChatLog.Save-On-Reload")) {
								File file = new File("plugins//" + Main.getInstance().getDescription().getName() + "//chatlog.yml");
								YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
								
								if(cfg.contains("ChatLogs." + target.getUniqueId().toString() + ".Messages")) {
									ArrayList<String> msgs = (ArrayList<String>) cfg.getStringList("ChatLogs." + target.getUniqueId().toString() + ".Messages");
									
									if(msgs.size() != 0) {
										new BukkitRunnable() {

											@Override
											public void run() {
									            String buchstaben = "QWERTZUIOPASDFGHJKLYXCVBNMqwertzuiopasdfghjklyxcvbnm123456789";
									            String code = "";
									            int codelength = 25;
									            
									            Random rand = new Random();
									            char[] text = new char[codelength];
									            for (int i = 0; i < codelength; i++) {
									              text[i] = buchstaben.charAt(rand.nextInt(buchstaben.length()));
									            }
									            for (int i = 0; i < text.length; i++) {
									              code = code + text[i];
									            }
												
												for(int i = 0; i < msgs.size(); i++) {
													MySQL.update("INSERT INTO ReportSystem_chatlogs(CHATLOG_ID,REPORTED_UUID,MESSAGE,STATUS) VALUES ('"+ code +"','"+ target.getUniqueId().toString() +"','"+ msgs.get(i) +"','0')");
												}
												ReportManager.sendNotify("CHATLOG", p.getName(), target.getName(), code);
												p.sendMessage("");
												p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.ChatLog.Notify.User.Creator").replace("%target%", target.getName()));
												TextComponent tc = new TextComponent();
												
												tc.setText(Main.getPrefix() + Main.getMSG("Messages.ChatLog.Notify.User.Link-Text"));
												tc.setClickEvent(new ClickEvent(Action.OPEN_URL, Main.getPermissionNotice("ChatLog.WebInterface-URL") + code));
												
												p.spigot().sendMessage(tc);
												p.sendMessage("");
												
												msgs.clear();
												cfg.set("ChatLogs." + target.getUniqueId().toString() + ".Messages", msgs);
												try {
													cfg.save(file);
												} catch (IOException e) {
													Bukkit.getConsoleSender().sendMessage(Main.getPrefix() + "§4ERROR §7(§cClear ChatLog-MSG§7)");
												}
												antispam.put(target, System.currentTimeMillis() + 1000 * Main.getInstance().getConfig().getInt("ChatLog.Cooldown-In-Seconds"));
											}
										}.runTaskAsynchronously(Main.getInstance());
									} else {
										p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.ChatLog.Player-Wrote-No-Message"));
									}
								} else {
									p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.ChatLog.Player-Wrote-No-Message"));
								}
							} else {
								if(Listener_ChatLog.chatlog.containsKey(target)) {
									new BukkitRunnable() {

										@Override
										public void run() {
											ArrayList<String> chat = Listener_ChatLog.chatlog.get(target);
											
											String buchstaben = "QWERTZUIOPASDFGHJKLYXCVBNMqwertzuiopasdfghjklyxcvbnm123456789";
								            String code = "";
								            int codelength = 25;
								            
								            Random rand = new Random();
								            char[] text = new char[codelength];
								            for (int i = 0; i < codelength; i++) {
								              text[i] = buchstaben.charAt(rand.nextInt(buchstaben.length()));
								            }
								            for (int i = 0; i < text.length; i++) {
								              code = code + text[i];
								            }
											
											for(int i = 0; i < chat.size(); i++) {
												MySQL.update("INSERT INTO ReportSystem_chatlogs(CHATLOG_ID,REPORTED_UUID,MESSAGE,STATUS) VALUES ('"+ code +"','"+ target.getUniqueId().toString() +"','"+ chat.get(i) +"','0')");
											}
											ReportManager.sendNotify("CHATLOG", p.getName(), target.getName(), code);
											p.sendMessage("");
											p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.ChatLog.Notify.User.Creator").replace("%target%", target.getName()));
											TextComponent tc = new TextComponent();
											
											tc.setText(Main.getPrefix() + Main.getMSG("Messages.ChatLog.Notify.User.Link-Text"));
											tc.setClickEvent(new ClickEvent(Action.OPEN_URL, Main.getPermissionNotice("ChatLog.WebInterface-URL") + code));
											
											p.spigot().sendMessage(tc);
											p.sendMessage("");
											
											Listener_ChatLog.chatlog.remove(target);
											
											antispam.put(target, System.currentTimeMillis() + 1000 * Main.getInstance().getConfig().getInt("ChatLog.Cooldown-In-Seconds"));
										}	
									}.runTaskAsynchronously(Main.getInstance());
								} else {
									p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.ChatLog.Player-Wrote-No-Message"));
								}
							}
						} else {
							p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.ChatLog.Cannot-Chatlog-Self"));
						}
					} else {
						p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.ChatLog.Player-Offline"));
					}
				}
				
			} else {
				p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.ChatLog.Usage"));
			}
			
		} else {
			Bukkit.getConsoleSender().sendMessage("");
			Bukkit.getConsoleSender().sendMessage("§cSystem §8» §c§lFAILED §8(§7Console-Input§8)");
			Bukkit.getConsoleSender().sendMessage("");
		}
		return true;
	}

}
