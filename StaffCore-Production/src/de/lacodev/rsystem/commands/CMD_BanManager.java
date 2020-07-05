package de.lacodev.rsystem.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.connorlinfoot.actionbarapi.ActionBarAPI;

import de.lacodev.rsystem.Main;
import de.lacodev.rsystem.mysql.MySQL;
import de.lacodev.rsystem.utils.BanManager;

public class CMD_BanManager implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {

			

			Player p = (Player)sender;

			

			if(MySQL.isConnected()) {

				if(args.length >= 4) {

					if(args[0].toLowerCase().equalsIgnoreCase("addreason")) {

						if(p.hasPermission(Main.getPermissionNotice("Permissions.BanManager.addreason")) || p.hasPermission(Main.getPermissionNotice("Permissions.Everything"))) {

							String timeunit = args[2].substring(args[2].length() - 1);

							String reason = "";
							for(int i = 3; i < args.length; i++) {
								reason = reason + " " + args[i];
							}

							if(timeunit.toLowerCase().matches("d") || timeunit.toLowerCase().matches("h") || timeunit.toLowerCase().matches("m") || args[2].toLowerCase().equalsIgnoreCase("perma")) {

								if(args[1].toLowerCase().matches("ban")) {

									if(args[2].toLowerCase().equalsIgnoreCase("perma")) {



										if(!BanManager.existsBanReason(reason)) {

											BanManager.createBanReason(reason, "Perma", -1);

											if(Main.getInstance().actionbar) {

												ActionBarAPI.sendActionBar(p, Main.getPrefix() + Main.getMSG("Messages.BanManager.Add-Reason.Successful"));

											} else {

												p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.BanManager.Add-Reason.Successful"));

											}

										} else {

											if(Main.getInstance().actionbar) {

												ActionBarAPI.sendActionBar(p, Main.getPrefix() + Main.getMSG("Messages.BanManager.Add-Reason.Already-Existing"));

											} else {

												p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.BanManager.Add-Reason.Already-Existing"));

											}

										}

									} else {

										if(!BanManager.existsBanReason(reason)) {

											BanManager.createBanReason(reason, timeunit, Integer.valueOf(args[2].substring(0, args[2].length() - 1)));

											if(Main.getInstance().actionbar) {

												ActionBarAPI.sendActionBar(p, Main.getPrefix() + Main.getMSG("Messages.BanManager.Add-Reason.Successful"));

											} else {

												p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.BanManager.Add-Reason.Successful"));

											}

										} else {

											if(Main.getInstance().actionbar) {

												ActionBarAPI.sendActionBar(p, Main.getPrefix() + Main.getMSG("Messages.BanManager.Add-Reason.Already-Existing"));

											} else {

												p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.BanManager.Add-Reason.Already-Existing"));

											}

										}

									}

								} else if(args[1].toLowerCase().matches("mute")) {

									if(!BanManager.existsMuteReason(reason)) {

										BanManager.createMuteReason(reason, timeunit, Integer.valueOf(args[2].substring(0, args[2].length() - 1)));

										if(Main.getInstance().actionbar) {

											ActionBarAPI.sendActionBar(p, Main.getPrefix() + Main.getMSG("Messages.BanManager.Add-Reason.Successful"));

										} else {

											p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.BanManager.Add-Reason.Successful"));

										}

									} else {

										if(Main.getInstance().actionbar) {

											ActionBarAPI.sendActionBar(p, Main.getPrefix() + Main.getMSG("Messages.BanManager.Add-Reason.Already-Existing"));

										} else {

											p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.BanManager.Add-Reason.Already-Existing"));

										}

									}

								} else {

									if(Main.getInstance().actionbar) {

										ActionBarAPI.sendActionBar(p, Main.getPrefix() + Main.getMSG("Messages.BanManager.Add-Reason.Invalid-ReasonType"));

									} else {

										p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.BanManager.Add-Reason.Invalid-ReasonType"));

									}

								}

							} else {

								if(Main.getInstance().actionbar) {

									ActionBarAPI.sendActionBar(p, Main.getPrefix() + Main.getMSG("Messages.BanManager.Add-Reason.Invalid-TimeUnit"));

								} else {

									p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.BanManager.Add-Reason.Invalid-TimeUnit"));

								}

							}

						}

					} else {

						p.sendMessage("");

						p.sendMessage(Main.getPrefix() + "§8[§eBanManager§8]");

						p.sendMessage("");

						p.sendMessage(Main.getPrefix() + "§7/banmanager addreason <Ban/Mute> <Length: 30d / Perma> <Reason>");

						p.sendMessage(Main.getPrefix() + "§7/banmanager removereason <Ban/Mute> <ID>");

						p.sendMessage("");

					}

				} else if(args.length == 3) {

					if(args[0].toLowerCase().equalsIgnoreCase("removereason")) {

						int id;

						if(p.hasPermission(Main.getPermissionNotice("Permissions.BanManager.removereason")) || p.hasPermission(Main.getPermissionNotice("Permissions.Everything"))) {

							try {

								id = Integer.parseInt(args[2]);

							} catch(NumberFormatException e) {

								p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Ban.NotValidID"));

								return false;

							}



							if(args[1].toLowerCase().matches("ban")) {

								String reason = BanManager.getBanReasonFromID(id);

								if(BanManager.existsBanReason(reason)) {

									BanManager.deleteBanReason(reason);

									if(Main.getInstance().actionbar) {

										ActionBarAPI.sendActionBar(p, Main.getPrefix() + Main.getMSG("Messages.BanManager.Remove-Reason.Successful"));

									} else {

										p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.BanManager.Remove-Reason.Successful"));

									}

								} else {

									if(Main.getInstance().actionbar) {

										ActionBarAPI.sendActionBar(p, Main.getPrefix() + Main.getMSG("Messages.BanManager.Remove-Reason.Not-Exists"));

									} else {

										p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.BanManager.Remove-Reason.Not-Exists"));

									}

								}

							} else if(args[1].toLowerCase().matches("mute")) {

								String reason = BanManager.getMuteReasonFromID(id);

								if(BanManager.existsMuteReason(reason)) {

									BanManager.deleteMuteReason(reason);

									if(Main.getInstance().actionbar) {

										ActionBarAPI.sendActionBar(p, Main.getPrefix() + Main.getMSG("Messages.BanManager.Remove-Reason.Successful"));

									} else {

										p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.BanManager.Remove-Reason.Successful"));

									}

								} else {

									if(Main.getInstance().actionbar) {

										ActionBarAPI.sendActionBar(p, Main.getPrefix() + Main.getMSG("Messages.BanManager.Remove-Reason.Not-Exists"));

									} else {

										p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.BanManager.Remove-Reason.Not-Exists"));

									}

								}

							} else {

								if(Main.getInstance().actionbar) {

									ActionBarAPI.sendActionBar(p, Main.getPrefix() + Main.getMSG("Messages.BanManager.Add-Reason.Invalid-ReasonType"));

								} else {

									p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.BanManager.Add-Reason.Invalid-ReasonType"));

								}

							}

						}

					} else {

						p.sendMessage("");

						p.sendMessage(Main.getPrefix() + "§8[§eBanManager§8]");

						p.sendMessage("");

						p.sendMessage(Main.getPrefix() + "§7/banmanager addreason <Ban/Mute> <Length: 30d / Perma> <Reason>");

						p.sendMessage(Main.getPrefix() + "§7/banmanager removereason <Ban/Mute> <ID>");

						p.sendMessage("");

					}

				} else {

					p.sendMessage("");

					p.sendMessage(Main.getPrefix() + "§8[§eBanManager§8]");

					p.sendMessage("");

					p.sendMessage(Main.getPrefix() + "§7/banmanager addreason <Ban/Mute> <Length: 30d / Perma> <Reason>");

					p.sendMessage(Main.getPrefix() + "§7/banmanager removereason <Ban/Mute> <ID>");

					p.sendMessage("");

				}

			} else {

				p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.No-Connection.Notify"));

			}

			

		} else {

			Bukkit.getConsoleSender().sendMessage("");

			Bukkit.getConsoleSender().sendMessage("§cSystem §8» §c§lFAILED §8(§7Console-Input§8)");

			Bukkit.getConsoleSender().sendMessage("");

		}
		return true;
	}

}
