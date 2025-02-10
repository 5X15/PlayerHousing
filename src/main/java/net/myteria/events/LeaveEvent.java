package net.myteria.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import net.myteria.HousingAPI;
import net.myteria.PlayerHousing;

public class LeaveEvent implements Listener {
	HousingAPI api = PlayerHousing.getAPI();
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event) {
		api.gameRulesPage.remove(event.getPlayer());
		api.playersPage.remove(event.getPlayer());
		api.worldsMenuPage.remove(event.getPlayer());
		api.permissionsPage.remove(event.getPlayer());
		
	}
	
}
