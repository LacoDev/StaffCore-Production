package de.lacodev.rsystem.commands;

import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.connorlinfoot.actionbarapi.ActionBarAPI;

import de.lacodev.rsystem.Main;
import de.lacodev.rsystem.mysql.MySQL;
import de.lacodev.rsystem.objects.BanReasons;
import de.lacodev.rsystem.utils.BanManager;
import de.lacodev.rsystem.utils.SystemManager;

public class CMD_Ban implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
	       if (sender instanceof Player) {

	            Player p = (Player) sender;

	            if (MySQL.isConnected()) {
	                if (p.hasPermission(Main.getPermissionNotice("Permissions.Everything")) || p.hasPermission(Main.getPermissionNotice("Permissions.Ban.Use"))) {
	                    if (args.length == 2) {
	                        int id;
	                        if (SystemManager.getUUIDByName(args[0]) != null) {
	                            try {
	                                id = Integer.parseInt(args[1]);
	                            } catch (NumberFormatException e) {
	                                if (Main.getInstance().actionbar) {
	                                    ActionBarAPI.sendActionBar(p, Main.getPrefix() + Main.getMSG("Messages.Ban.NotValidID"));
	                                } else {
	                                    p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Ban.NotValidID"));
	                                }
	                                return false;
	                            }
	                            if (BanManager.existsBanID(id)) {
	                                if (!BanManager.isBanned(SystemManager.getUUIDByName(args[0]))) {
	                                    if (!args[1].matches(p.getName())) {
	                                        BanManager.submitBan(SystemManager.getUUIDByName(args[0]), BanManager.getBanReasonFromID(id), p.getUniqueId().toString());

	                                        if (Main.getInstance().actionbar) {
	                                            ActionBarAPI.sendActionBar(p, Main.getPrefix() + Main.getMSG("Messages.Ban.Created"));
	                                        } else {
	                                            p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Ban.Created"));
	                                        }
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
	                            } else {
	                                if (Main.getInstance().actionbar) {
	                                    ActionBarAPI.sendActionBar(p, Main.getPrefix() + Main.getMSG("Messages.Ban.Reason-Not-Exists"));
	                                } else {
	                                    p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Ban.Reason-Not-Exists"));
	                                }
	                                p.sendMessage("");

	                                ArrayList<BanReasons> reasons = BanManager.getBanReasons();

	                                if (reasons.size() > 0) {
	                                    for (int i = 0; i < reasons.size(); i++) {
	                                        p.sendMessage(Main.getPrefix() + "§8- §e" + reasons.get(i).getName() + " §8(§e" + reasons.get(i).getID() + "§8)");
	                                    }
	                                } else {
	                                    p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Ban.No-Reasons"));
	                                }

	                                p.sendMessage("");
	                            }
	                        } else {
	                            if (Main.getInstance().actionbar) {
	                                ActionBarAPI.sendActionBar(p, Main.getPrefix() + Main.getMSG("Messages.Ban.Cannot-find-player"));
	                            } else {
	                                p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Ban.Cannot-find-player"));
	                            }
	                        }

	                    } else {
	                        if (args.length == 1) {
	                            if (SystemManager.getUUIDByName(args[0]) != null) {
	                                if (!BanManager.isBanned(SystemManager.getUUIDByName(args[0]))) {
	                                    BanManager.openPagedBanInv(p, args[0], 1);
	                                } else {
	                                    if (Main.getInstance().actionbar) {
	                                        ActionBarAPI.sendActionBar(p, Main.getPrefix() + Main.getMSG("Messages.Ban.Already-Banned"));
	                                    } else {
	                                        p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Ban.Already-Banned"));
	                                    }
	                                }

	                            } else {
	                                if (Main.getInstance().actionbar) {
	                                    ActionBarAPI.sendActionBar(p, Main.getPrefix() + Main.getMSG("Messages.Ban.Cannot-find-player"));
	                                } else {
	                                    p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Ban.Cannot-find-player"));
	                                }
	                            }

	                        } else {
	                            if (Main.getInstance().actionbar) {
	                                ActionBarAPI.sendActionBar(p, Main.getPrefix() + Main.getMSG("Messages.Ban.Usage"));
	                            } else {
	                                p.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Ban.Usage"));
	                            }
	                            p.sendMessage("");
	                            ArrayList<BanReasons> reasons = BanManager.getBanReasons();

	                            if (reasons.size() > 0) {
	                                for (int i = 0; i < reasons.size(); i++) {
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
	            if (MySQL.isConnected()) {
	                if (args.length == 2) {
	                    int id;
	                    if (SystemManager.getUUIDByName(args[0]) != null) {
	                        try {
	                            id = Integer.parseInt(args[1]);
	                        } catch (NumberFormatException e) {
	                            sender.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Ban.NotValidID"));
	                            return false;
	                        }
	                        if (BanManager.existsBanID(id)) {
	                            if (!BanManager.isBanned(SystemManager.getUUIDByName(args[0]))) {
	                                BanManager.submitBan(SystemManager.getUUIDByName(args[0]), BanManager.getBanReasonFromID(id), "Console");

	                                sender.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Ban.Created"));
	                            } else {
	                                sender.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Ban.Already-Banned"));
	                            }
	                        } else {

	                            sender.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Ban.Reason-Not-Exists"));

	                            sender.sendMessage("");

	                            ArrayList<BanReasons> reasons = BanManager.getBanReasons();
	                            if (reasons.size() > 0) {
	                                for (int i = 0; i < reasons.size(); i++) {
	                                    sender.sendMessage(Main.getPrefix() + "§8- §e" + reasons.get(i).getName() + " §8(§e" + reasons.get(i).getID() + "§8)");
	                                }
	                            } else {
	                                sender.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Ban.No-Reasons"));
	                            }

	                            sender.sendMessage("");
	                        }
	                    } else {
	                        sender.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Ban.Cannot-find-player"));
	                    }

	                } else {

	                    sender.sendMessage(Main.getPrefix() + Main.getMSG("Messages.Ban.Usage"));

	                    sender.sendMessage("");
	                    ArrayList<BanReasons> reasons = BanManager.getBanReasons();

	                    if (reasons.size() > 0) {
	                        for (int i = 0; i < reasons.size(); i++) {
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
