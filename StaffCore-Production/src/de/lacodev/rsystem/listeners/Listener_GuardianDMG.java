package de.lacodev.rsystem.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Guardian;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import de.lacodev.rsystem.utils.BanManager;

public class Listener_GuardianDMG implements Listener {
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e) {
		Entity p = e.getEntity();
		Entity d = e.getDamager();
		
		if(p instanceof Player) {
			if(d instanceof Guardian) {
				Guardian g = (Guardian) d;
				
				if(g.getCustomName() != null) {
					if(g.getCustomName().equalsIgnoreCase("G1") || g.getCustomName().equalsIgnoreCase("G2") || g.getCustomName().equalsIgnoreCase("G3")) {
						e.setCancelled(true);
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		Entity en = e.getEntity();
		
		if(en instanceof Zombie) {
			if(en.getCustomName() != null) {
				if(e.getEntity().getCustomName().startsWith("§cBan §8» §7")) {
					e.setCancelled(true);
				}
			}
				
		}
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		
		if(BanManager.freezed.contains(p)) {
			p.teleport(e.getFrom());
		}
	}
	
	@EventHandler
	public void onZombie(EntityTargetLivingEntityEvent e) {
		if(e.getEntity().getCustomName() != null) {
			if(e.getEntity().getCustomName().startsWith("§cBan §8» §7")) {
				e.setCancelled(true);
			}
		}
	}

}
