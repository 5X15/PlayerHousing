package net.myteria.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import net.myteria.HousingAPI;
import net.myteria.PlayerHousing;

public class JoinEvent implements Listener {
	HousingAPI api = PlayerHousing.getAPI();

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		api.gameRulesPage.put(player, 0);
		api.playersPage.put(player, 0);
		api.worldsMenuPage.put(player, 0);
		api.permissionsPage.put(player, 0);
		
		api.loadWorld(player.getUniqueId());
		
	}
	
}
