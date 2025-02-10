package net.myteria.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import net.myteria.HousingAPI;
import net.myteria.PlayerHousing;

public class housingCmd implements CommandExecutor {
	HousingAPI api = PlayerHousing.getAPI();

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
			@NotNull String[] args) {
		Player player = (Player) sender;
		
		if (player.getWorld() == api.getWorld(player.getUniqueId())) {

			player.openInventory(api.getHousingMenu().getInventory());
			
		} else {
			if (api.getConfigManager().hasWorld(player.getUniqueId())) {
				api.loadWorld(player.getUniqueId());
				api.joinWorld(player, player.getUniqueId());
				return true;
			}
			api.worldsMenuInv.put(player, Bukkit.createInventory(api.getWorldsMenu(), 5*9, "Worlds Menu"));
			api.getWorldsMenu().setInventory(api.worldsMenuInv.get(player), api.worldsMenuPage.get(player));
			player.openInventory(api.getWorldsMenu().getInventory());
		}
				
		return true;
	}

}
