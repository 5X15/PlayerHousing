package net.myteria.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import net.myteria.HousingAPI;
import net.myteria.PlayerHousing;

public class visitCmd implements CommandExecutor {
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
			@NotNull String[] args) {
		Player player = (Player) sender;
		HousingAPI api = PlayerHousing.getAPI();
		
		if (args.length >= 1) {
			OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
			if (target == null || !target.hasPlayedBefore()) {
				player.sendMessage(args[0] + " could not be found!");
				return true;
			}
			
			api.joinWorld(player, target.getUniqueId());
		}
		
		api.worldsMenuInv.put(player, Bukkit.createInventory(api.getWorldsMenu(), 5*9, "Player Worlds"));
		api.getWorldsMenu().setInventory(api.worldsMenuInv.get(player), api.worldsMenuPage.get(player));
		player.openInventory(api.getWorldsMenu().getInventory());
		return true;
	}

}
