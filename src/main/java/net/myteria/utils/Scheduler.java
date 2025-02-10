package net.myteria.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import net.myteria.PlayerHousing;


public class Scheduler {

    public static void runTask(PlayerHousing plugin, Runnable runnable) {
        if (plugin.isFolia()) {
            Bukkit.getGlobalRegionScheduler().run(plugin, (task) -> runnable.run());
            return;
        }
        Bukkit.getScheduler().runTask(plugin, runnable);
    }
    
    public static void runTaskLater(PlayerHousing plugin, Location loc, Runnable runnable, long delayedTicks) {
        if (plugin.isFolia()) {
        	Bukkit.getRegionScheduler().runDelayed(plugin, loc, (task) ->runnable.run(), delayedTicks);
            return;
        }
        Bukkit.getScheduler().runTaskLater(plugin, runnable, delayedTicks);
    }
    
    public static void runTaskLater(Entity entity, PlayerHousing plugin, Runnable runnable, Runnable callback, long delayedTicks) {
        if (plugin.isFolia()) {
        	entity.getScheduler().runDelayed(plugin, (task) -> runnable.run(), callback, delayedTicks);
            return;
        }
        Bukkit.getScheduler().runTaskLater(plugin, runnable, delayedTicks);
    }
    
    public static void runTask(PlayerHousing plugin, Location loc, Runnable runnable) {
        if (plugin.isFolia()) {
        	Bukkit.getRegionScheduler().run(plugin, loc, (task) ->runnable.run());
            return;
        }
        Bukkit.getScheduler().runTask(plugin, runnable);
    }
}