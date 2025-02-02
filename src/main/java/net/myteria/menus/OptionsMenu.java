package net.myteria.menus;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import net.myteria.HousingAPI;
import net.myteria.PlayerHousing;
import net.myteria.HousingAPI.Action;

public class OptionsMenu implements InventoryHolder {
	HousingAPI api = PlayerHousing.getAPI();
	Inventory inv = null;
	int[] purpleSlots = {3, 4, 5, 21, 22, 23};
	int[] graySlots = {0, 1, 7, 8, 9, 17, 18, 19, 25, 26};
	int[] magentaSlots = {2, 6, 10, 12, 14, 16, 20, 24};
	int[] kickSlots = {11};
	int[] rankSlot = {13};
	int[] headSlot = {4};
	int[] banSlot = {15};
	List<String> rankLore;
	public void setupMenu(OfflinePlayer player, World world) {
		inv = Bukkit.createInventory(this, 3*9, "Options");
		
		ItemStack purple = setMeta(new ItemStack(Material.PURPLE_STAINED_GLASS_PANE), " ", null);
		ItemStack gray = setMeta(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), " ", null);
		ItemStack magenta = setMeta(new ItemStack(Material.MAGENTA_STAINED_GLASS_PANE), " ", null);
		ItemStack kickBtn = setMeta(new ItemStack(Material.OAK_DOOR), "Kick", null);
		ItemStack headBtn = setSkullMeta(new ItemStack(Material.PLAYER_HEAD), player);
		ItemStack banBtn = setMeta(new ItemStack(Material.TNT), "Ban", null);
		
		
		setSlot(purpleSlots, purple);
		setSlot(graySlots, gray);
		setSlot(magentaSlots, magenta);
		setSlot(kickSlots, kickBtn);
		setSlot(headSlot, headBtn);
		setSlot(banSlot, banBtn);
	}

	@Override
	public @NotNull Inventory getInventory() {
		// TODO Auto-generated method stub
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
	public ItemStack setMeta(ItemStack item, String display, Action action, List<String> lore) {
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(display);
		meta.getPersistentDataContainer().set(new NamespacedKey(PlayerHousing.getInstance(), "action"), PersistentDataType.STRING, action.name());
		if (lore != null) {
			meta.setLore(lore);
		}
		item.setItemMeta(meta);
		return item;
		
	}
	public ItemStack setSkullMeta(ItemStack item, OfflinePlayer player) {
		SkullMeta meta = (SkullMeta) item.getItemMeta();
		meta.setDisplayName(player.getName());
		meta.setOwningPlayer(player);
		item.setItemMeta(meta);
		return item;
		
	}
	
	public void setSlot(int[] slot, ItemStack item) {
		for (int invSlot: slot) {
			inv.setItem(invSlot, item);
		}
	}

}
