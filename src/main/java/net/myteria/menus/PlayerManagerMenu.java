package net.myteria.menus;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import net.myteria.HousingAPI;
import net.myteria.PlayerHousing;

public class PlayerManagerMenu implements InventoryHolder {
	HousingAPI api = PlayerHousing.getAPI();
	Inventory inv = null;
	int[] purpleSlots = {3, 4, 5, 12, 14, 21, 22, 23};
	int[] graySlots = {0, 1, 7, 8, 9, 17, 18, 19, 25, 26};
	int[] magentaSlots = {2, 6, 10, 16, 20, 24};
	int[] whitelistSlots = {11};
	int[] playersSlots = {13};
	int[] bannedSlots = {15};
	public PlayerManagerMenu() {
		inv = Bukkit.createInventory(this, 3*9, "Player Manager");
		ItemStack purple = setMeta(new ItemStack(Material.PURPLE_STAINED_GLASS_PANE), " ", null);
		ItemStack gray = setMeta(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), " ", null);
		ItemStack magenta = setMeta(new ItemStack(Material.MAGENTA_STAINED_GLASS_PANE), " ", null);
		ItemStack whitelistBtn = setMeta(new ItemStack(Material.WRITABLE_BOOK), "Whitelist Manager", null);
		ItemStack playersBtn = setMeta(new ItemStack(Material.BUCKET), "Online Players", null);
		ItemStack bannedBtn = setMeta(new ItemStack(Material.BARRIER), "Ban Manager", null);
		
		setSlot(purpleSlots, purple);
		setSlot(graySlots, gray);
		setSlot(magentaSlots, magenta);
		setSlot(whitelistSlots, whitelistBtn);
		setSlot(playersSlots, playersBtn);
		setSlot(bannedSlots, bannedBtn);
	}
	
	@Override
	public @NotNull Inventory getInventory() {
		return inv;
	}
	
	public ItemStack setMeta(ItemStack item, String display, List<String> lore) {
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(display);
		if (lore != null) {
			meta.setLore(lore);
		}
		item.setItemMeta(meta);
		return item;
		
	}
	
	public void setSlot(int[] slot, ItemStack item) {
		for (int invSlot: slot) {
			inv.setItem(invSlot, item);
		}
	}
}
