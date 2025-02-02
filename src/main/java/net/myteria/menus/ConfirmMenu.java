package net.myteria.menus;

import java.util.List;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import net.myteria.HousingAPI.Action;
import net.myteria.HousingAPI;
import net.myteria.PlayerHousing;

public class ConfirmMenu implements InventoryHolder {
	HousingAPI api = PlayerHousing.getAPI();
	Inventory inv;
	int[] backSlots = {0,1,2,9,10,11,18,19,20};
	int[] confirmSlots = {6,7,8,15,16,17,24,25,26};
	int[] glassSlots = {3,4,5,12,14,21,22,23};
	int[] headSlot = {13};
	
	public ConfirmMenu(Action action, OfflinePlayer target) {
		String actionName = getActionName(action);
		
		inv = Bukkit.createInventory(this, 3*9, actionName + " " + target.getName());
		setSlot(backSlots, setMeta(new ItemStack(Material.RED_WOOL), "Back", null));
		setSlot(confirmSlots, setMeta(new ItemStack(Material.GREEN_WOOL), "Confirm", action, null));
		setSlot(glassSlots, setMeta(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), " ", null));
		setSlot(headSlot, setSkullMeta(new ItemStack(Material.PLAYER_HEAD), target));
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
	public ItemStack setMeta(ItemStack item, String display, Action action, String rank, String permission, List<String> lore) {
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(display);
		meta.getPersistentDataContainer().set(new NamespacedKey(PlayerHousing.getInstance(), "action"), PersistentDataType.STRING, action.name());
		meta.getPersistentDataContainer().set(new NamespacedKey(PlayerHousing.getInstance(), "rank"), PersistentDataType.STRING, rank);
		meta.getPersistentDataContainer().set(new NamespacedKey(PlayerHousing.getInstance(), "permission"), PersistentDataType.STRING, permission);
		if (lore != null) {
			meta.setLore(lore);
		}
		item.setItemMeta(meta);
		return item;
		
	}
	public ItemStack setSkullMeta(ItemStack item, OfflinePlayer target) {
		SkullMeta meta = (SkullMeta) item.getItemMeta();
		meta.setDisplayName(target.getName());
		meta.setOwningPlayer(target);
		item.setItemMeta(meta);
		return item;
		
	}
	
	public void setSlot(int[] slot, ItemStack item) {
		for (int invSlot: slot) {
			inv.setItem(invSlot, item);
		}
	}
	
	public String getActionName(Action action) {
		String actionName;
		switch(action) {
		  case addWhitelist: { 
			actionName = "Whitelist";
			break;
		  }
		case Ban: { 
			actionName = "Ban";
			break;
		  }
		case Kick: { 
			actionName = "Kick";
			break;
		  }
		case Unban: { 
			actionName = "Unban";
			break;
		  }
		case removeWhitelist: { 
			actionName = "Un-whitelist";
			break;
		  }
		default: { 
			actionName = action.name();
			break;
		  }
		}
		return actionName;
	}

}
