package net.myteria.menus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import net.myteria.HousingAPI;
import net.myteria.PlayerHousing;
import net.myteria.HousingAPI.Status;
import net.myteria.objects.PlayerWorld;

public class WorldsMenu implements InventoryHolder {
	private List<ItemStack> items = new ArrayList<>();
	private Inventory inv;
	
	public void setupMenu(boolean clear) {
		if (clear) {
			items.clear();
		}
		HousingAPI api = PlayerHousing.getAPI();
		
		
		if (items.isEmpty()) {
			for (Entry<OfflinePlayer, PlayerWorld> world : api.getWorlds().entrySet()) {
				OfflinePlayer owner = world.getKey();
				Status status = world.getValue().getStatus();
				if (status != Status.PUBLIC) {
					continue;
				}
				
				List<String> lore = new ArrayList<>();
				ItemStack head = new ItemStack(Material.PLAYER_HEAD);
				
				SkullMeta meta = (SkullMeta) head.getItemMeta();
				meta.setOwningPlayer(owner);
				meta.setDisplayName(owner.getName() + "'s World");
				lore.add(world.getValue().getDescription());
				lore.add(" ");
				lore.add("Online: " + world.getValue().getPlayersOnline());
				lore.add(" ");
				meta.setLore(lore);
				head.setItemMeta(meta);
				items.add(head);
			}
		}
		
	}

	private void setPageItems(int page) {
		inv.clear();
		setupMenu(true);
		if (items.isEmpty()) {
			return;
		}
		ItemStack back = new ItemStack(Material.ARROW);
		ItemMeta meta = back.getItemMeta();
		meta.setDisplayName("Back");
		back.setItemMeta(meta);
		
		ItemStack next = new ItemStack(Material.ARROW);
		ItemMeta meta2 = next.getItemMeta();
		meta2.setDisplayName("Next");
		next.setItemMeta(meta2);
		
		int pages = PlayerHousing.getAPI().listToPages(items, 36).size();
		if (pages >= 2 && page != pages - 1) {
			inv.setItem(44, next);
		}
		if (page >= 1) {
			inv.setItem(36, back);
		}
		PlayerHousing.getAPI().listToPages(items, 36).get(page).forEach(item -> {
			inv.addItem(item);
		});
		
		//44 next page
		//36 last page
		
	}

	@Override
	public Inventory getInventory() {
		// TODO Auto-generated method stub
		return inv;
	}
	
	public void setInventory(Inventory inv, int page) {
		// TODO Auto-generated method stub
		this.inv = inv;
		setPageItems(page);
	}
	
	public List<ItemStack> getItems() {
		// TODO Auto-generated method stub
		return items;
	}

}
