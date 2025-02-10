package net.myteria.menus;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
import net.myteria.objects.PlayerWorld;

public class SettingsMenu implements InventoryHolder {
	HousingAPI api = PlayerHousing.getAPI();
	Inventory inv = null;
	int[] purpleSlots = {0, 2, 3, 4, 5, 6, 8, 10, 16, 18, 26, 28, 34, 38, 39, 41, 42, 45, 48, 50, 53};
	int[] graySlots = {1, 7, 9, 17, 27, 35, 36, 37, 43, 44, 46, 47, 51, 52};
	int[] magentaSlots = {12, 14, 19, 20, 21, 22, 23, 24, 25, 30, 32, 40};
	int[] difficultySlots = {11};
	int[] timeSlots = {13};
	int[] pvpSlots = {15};
	int[] gamemodeSlots = {29};
	int[] gameruleSlots = {31};
	int[] statusSlots = {33};
	int[] deleteSlots = {49};
	List<String> difficultyLore = new ArrayList<>();
	List<String> timeLore = new ArrayList<>();
	List<String> pvpLore = new ArrayList<>();
	List<String> gamemodeLore = new ArrayList<>();
	List<String> gamerulesLore = new ArrayList<>();
	List<String> statusLore = new ArrayList<>();
	List<String> deleteLore = new ArrayList<>();
	
	public void setupInventory(Player player) {
		difficultyLore.clear();
		timeLore.clear();
		pvpLore.clear();
		gamemodeLore.clear();
		gamerulesLore.clear();
		statusLore.clear();
		deleteLore.clear();
		
		
		OfflinePlayer owner = api.getWorldOwner(player.getWorld());
		UUID uuid = owner.getUniqueId();
		PlayerWorld world = api.getWorldInstance(uuid);
		String worldName =world.getWorldName();

		inv = Bukkit.createInventory(this, 6*9, "Settings Menu");
		
		difficultyLore.add(world.getDifficulty().name().toUpperCase());
		timeLore.add((world.getWorld().isDayTime() ? "Day" : "Night"));
		pvpLore.add((world.getPVP() ? "PvP Enabled" : "PvP Disabled"));
		gamemodeLore.add(world.getGamemode().name().toUpperCase());
		gamerulesLore.add("GameRules Menu");
		statusLore.add(world.getStatus().name().toUpperCase());
		deleteLore.add("Coming Soon!");
		
		ItemStack purple = setMeta(new ItemStack(Material.PURPLE_STAINED_GLASS_PANE), " ", null);
		ItemStack gray = setMeta(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), " ", null);
		ItemStack magenta = setMeta(new ItemStack(Material.MAGENTA_STAINED_GLASS_PANE), " ", null);
		ItemStack difficultyBtn = setMeta(new ItemStack(Material.SPAWNER), "Difficulty", difficultyLore);
		ItemStack timeBtn = setMeta(new ItemStack(Material.CLOCK), "World Time", timeLore);
		ItemStack pvpBtn = setMeta(new ItemStack(Material.DIAMOND_SWORD), "PvP", pvpLore);
		ItemStack gamemodeBtn = setMeta(new ItemStack(Material.CRAFTING_TABLE), "Gamemode", gamemodeLore);
		ItemStack gamerulesBtn = setMeta(new ItemStack(Material.BOOK), "GameRules", gamerulesLore);
		ItemStack statusBtn = setMeta(new ItemStack(Material.OAK_DOOR), "Status", statusLore);
		ItemStack deleteBtn = setMeta(new ItemStack(Material.TNT), "Delete World", deleteLore);
		
		
		setSlot(purpleSlots, purple);
		setSlot(graySlots, gray);
		setSlot(magentaSlots, magenta);
		setSlot(difficultySlots, difficultyBtn);
		setSlot(timeSlots, timeBtn);
		setSlot(pvpSlots, pvpBtn);
		setSlot(gamemodeSlots, gamemodeBtn);
		setSlot(gameruleSlots, gamerulesBtn);
		setSlot(statusSlots, statusBtn);
		setSlot(deleteSlots, deleteBtn);
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
