package de.lacodev.rsystem.commands;

import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.connorlinfoot.actionbarapi.ActionBarAPI;

import de.lacodev.rsystem.Main;
import de.lacodev.rsystem.mysql.MySQL;
import de.lacodev.rsystem.objects.MuteReasons;
import de.lacodev.rsystem.utils.BanManager;
import de.lacodev.rsystem.utils.SystemManager;

public class CMD_Mute implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {

			Player p = (Player)sender;

			if(MySQL.isConnected()) {
				if( p.hasPermission(Main.getPermissionNotice("Permissions.Everything")) || p.hasPermission(Main.getPermissionNotice("Permissions.Mute.Use")) ) {
					if( args.length == 2 ) {
						int id;
						if(SystemManager.getUUIDByName(args[0]) != null) {
							try {
								id = Integer.parseInt(args[1]);
							} catch (NumberFormatException e) {
								if(Main.getInstance().actionbar) {
									ActionBarAPI.sendActionBar(p, Main.getPrefix() + Main.getMSG("Messages.Ban.NotValidID"));
								} else {
									p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Ban.NotValidID"));
								}
								return false;
							}
							if(BanManager.existsMuteID(id)) {
								if(!BanManager.isMuted(SystemManager.getUUIDByName(args[0]))) {
									if(!args[1].matches(p.getName())) {
										if(BanManager.submitMute(SystemManager.getUUIDByName(args[0]), BanManager.getMuteReasonFromID(id), p.getUniqueId().toString())) {
											if(Main.getInstance().actionbar) {
												ActionBarAPI.sendActionBar(p, Main.getPrefix() + Main.getMSG("Messages.Mute.Created"));
											} else {
												p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Mute.Created"));
											}
										} else {
											if(Main.getInstance().actionbar) {
												ActionBarAPI.sendActionBar(p, Main.getPrefix() + Main.getMSG("Messages.Mute.Error"));
											} else {
												p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Mute.Error"));
											}
										}
									} else {
										p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Mute.Cannot-mute-self"));
									}
								} else {
									if(Main.getInstance().actionbar) {
										ActionBarAPI.sendActionBar(p, Main.getPrefix() + Main.getMSG("Messages.Mute.Already-Muted"));
									} else {
										p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Mute.Already-Muted"));
									}
								}
							} else {
								if(Main.getInstance().actionbar) {
									ActionBarAPI.sendActionBar(p, Main.getPrefix() + Main.getMSG("Messages.Mute.Reason-Not-Exists"));
								} else {
									p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Mute.Reason-Not-Exists"));
								}
								p.sendMessage("");

								ArrayList<MuteReasons> reasons = BanManager.getMuteReasons();

								if(reasons.size() > 0) {
									for(int i = 0; i < reasons.size(); i++) {
										p.sendMessage(Main.getPrefix() + "§8- §e" + reasons.get(i).getName() + " §8(§e" + reasons.get(i).getID() + "§8)");
									}
								} else {
									p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Ban.No-Reasons"));
								}
								p.sendMessage("");
							}
						} else {
							if(Main.getInstance().actionbar) {
								ActionBarAPI.sendActionBar(p, Main.getPrefix() + Main.getMSG("Messages.Mute.Cannot-find-player"));
							} else {
								p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Mute.Cannot-find-player"));
							}
						}

					} else {
						if (args.length == 1) {
							if (SystemManager.getUUIDByName(args[0]) != null) {
								if (!BanManager.isMuted(SystemManager.getUUIDByName(args[0]))) {
									BanManager.openPagedMuteInv(p, args[0], 1);
								} else {
									if (Main.getInstance().actionbar) {
										ActionBarAPI.sendActionBar(p, Main.getPrefix() + Main.getMSG("Messages.Mute.Already-Muted"));
									} else {
										p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Mute.Already-Muted"));
									}
								}

							} else {
								if (Main.getInstance().actionbar) {
									ActionBarAPI.sendActionBar(p, Main.getPrefix() + Main.getMSG("Messages.Mute.Cannot-find-player"));
								} else {
									p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Mute.Cannot-find-player"));
								}
							}
						} else {
							p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Mute.Usage"));
							p.sendMessage("");
							ArrayList<MuteReasons> reasons = BanManager.getMuteReasons();

							if(reasons.size() > 0) {
								for(int i = 0; i < reasons.size(); i++) {
									p.sendMessage(Main.getPrefix() + "§8- §e" + reasons.get(i).getName() + " §8(§e" + reasons.get(i).getID() + "§8)");
								}
							} else {
								p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Ban.No-Reasons"));
							}
							p.sendMessage("");
						}
					}
				}
			} else {
				p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.No-Connection.Notify"));
			}

		} else {
			if(MySQL.isConnected()) {
				if( args.length == 2 ) {
					int id;
					
					try {
						id = Integer.parseInt(args[1]);
					} catch (NumberFormatException e) {
						sender.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Ban.NotValidID"));
						return false;
					}

					if(SystemManager.getUUIDByName(args[0]) != null) {
						if(BanManager.existsMuteID(id)) {
							if(!BanManager.isMuted(SystemManager.getUUIDByName(args[0]))) {
								if(BanManager.submitMute(SystemManager.getUUIDByName(args[0]), BanManager.getMuteReasonFromID(id), "Console")) {
									sender.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Mute.Created"));
								} else {
									sender.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Mute.Error"));
								}
							} else {
								sender.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Mute.Already-Muted"));
							}
						} else {
							sender.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Mute.Reason-Not-Exists"));

							sender.sendMessage("");
							ArrayList<MuteReasons> reasons = BanManager.getMuteReasons();

							if(reasons.size() > 0) {
								for(int i = 0; i < reasons.size(); i++) {
									sender.sendMessage(Main.getPrefix() + "§8- §e" + reasons.get(i).getName() + " §8(§e" + reasons.get(i).getID() + "§8)");
								}
							} else {
								sender.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Ban.No-Reasons"));
							}
							sender.sendMessage("");
						}
					} else {
						sender.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Mute.Cannot-find-player"));
					}

				} else {
					sender.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Mute.Usage"));
					sender.sendMessage("");
					ArrayList<MuteReasons> reasons = BanManager.getMuteReasons();

					if(reasons.size() > 0) {
						for(int i = 0; i < reasons.size(); i++) {
							sender.sendMessage(Main.getPrefix() + "§8- §e" + reasons.get(i).getName() + " §8(§e" + reasons.get(i).getID() + "§8)");
						}
					} else {
						sender.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Ban.No-Reasons"));
					}
					sender.sendMessage("");
				}
			} else {
				sender.sendMessage(Main.getPrefix() + Main.getMSG("Messages.No-Connection.Notify"));
			}
		}		
		return true;
	}
}
