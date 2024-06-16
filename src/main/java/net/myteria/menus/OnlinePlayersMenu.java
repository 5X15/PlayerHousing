package net.myteria.menus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_20_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.nbt.NBTTagCompound;
import net.myteria.HousingAPI.Action;
import net.myteria.HousingAPI;
import net.myteria.PlayerHousing;

public class OnlinePlayersMenu implements InventoryHolder {
	private List<ItemStack> items = new ArrayList<>();
	private Inventory inv;
	
	public void setupMenu(Player player, Action action) {
		items.clear();
		HousingAPI api = PlayerHousing.getAPI();
		World world = player.getWorld();
		OfflinePlayer owner = api.getWorldOwner(world);
		YamlConfiguration config = api.getWorldConfig(owner.getUniqueId());
		
		if (items.isEmpty()) {
			
			switch(action) {
				case Manage: {
					for (Player user: world.getPlayers()) {
						ItemStack item = new ItemStack(Material.PLAYER_HEAD);
						SkullMeta meta = (SkullMeta) item.getItemMeta();
						meta.setOwningPlayer(user);
						meta.setDisplayName(user.getName());
						meta.getPersistentDataContainer().set(new NamespacedKey(PlayerHousing.getInstance(), "action"), PersistentDataType.STRING, action.name());
						item.setItemMeta(meta);
						items.add(item);
						
					}
					break;
				}
				case Whitelist: {
					String worldName = config.getString("default-world");
					for (String uuid: (List<String>)config.getList(worldName + ".ranks.default.members")) {
						ItemStack item = new ItemStack(Material.PLAYER_HEAD);
						SkullMeta meta = (SkullMeta) item.getItemMeta();
						meta.setOwningPlayer(Bukkit.getOfflinePlayer(UUID.fromString(uuid.toString())));
						meta.setDisplayName(Bukkit.getOfflinePlayer(UUID.fromString(uuid.toString())).getName());
						meta.getPersistentDataContainer().set(new NamespacedKey(PlayerHousing.getInstance(), "action"), PersistentDataType.STRING, action.name());
						item.setItemMeta(meta);
						items.add(item);
					}
					for (String uuid: (List<String>)config.getList(worldName + ".ranks.trusted.members")) {
						ItemStack item = new ItemStack(Material.PLAYER_HEAD);
						SkullMeta meta = (SkullMeta) item.getItemMeta();
						meta.setOwningPlayer(Bukkit.getOfflinePlayer(UUID.fromString(uuid.toString())));
						meta.setDisplayName(Bukkit.getOfflinePlayer(UUID.fromString(uuid.toString())).getName());
						meta.getPersistentDataContainer().set(new NamespacedKey(PlayerHousing.getInstance(), "action"), PersistentDataType.STRING, action.name());
						item.setItemMeta(meta);
						items.add(item);
					}
					break;
				}
				case Banned: {
					String worldName = config.getString("default-world");
					for (String uuid: (List<String>)config.getList(worldName + ".ranks.default.members")) {
						ItemStack item = new ItemStack(Material.PLAYER_HEAD);
						SkullMeta meta = (SkullMeta) item.getItemMeta();
						meta.setOwningPlayer(Bukkit.getOfflinePlayer(UUID.fromString(uuid.toString())));
						meta.setDisplayName(Bukkit.getOfflinePlayer(UUID.fromString(uuid.toString())).getName());
						meta.getPersistentDataContainer().set(new NamespacedKey(PlayerHousing.getInstance(), "action"), PersistentDataType.STRING, action.name());
						item.setItemMeta(meta);
						items.add(item);
					}
					for (String uuid: (List<String>)config.getList(worldName + ".ranks.trusted.members")) {
						ItemStack item = new ItemStack(Material.PLAYER_HEAD);
						SkullMeta meta = (SkullMeta) item.getItemMeta();
						meta.setOwningPlayer(Bukkit.getOfflinePlayer(UUID.fromString(uuid.toString())));
						meta.setDisplayName(Bukkit.getOfflinePlayer(UUID.fromString(uuid.toString())).getName());
						meta.getPersistentDataContainer().set(new NamespacedKey(PlayerHousing.getInstance(), "action"), PersistentDataType.STRING, action.name());
						item.setItemMeta(meta);
						items.add(item);
					}
					break;
				}
			default:
				break;
			}
			
		}
		
	}

	private void setPageItems(int page) {
		inv.clear();
		ItemStack back = new ItemStack(Material.ARROW);
		ItemMeta meta = back.getItemMeta();
		meta.setDisplayName("Back");
		back.setItemMeta(meta);
		
		ItemStack next = new ItemStack(Material.ARROW);
		ItemMeta meta2 = next.getItemMeta();
		meta2.setDisplayName("Next");
		next.setItemMeta(meta2);
		
		if (!items.isEmpty()) {
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
		}
		
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
