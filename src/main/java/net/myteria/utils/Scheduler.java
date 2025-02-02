package net.myteria.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

import net.myteria.PlayerHousing;

import java.util.function.Consumer;

public class Scheduler {

    private static Object GLOBAL_REGION_SCHEDULER = null;

    public static <T> T callMethod(Class<?> clazz, Object object, String methodName, Class<?>[] parameterTypes, Object... args) {
        try {
            return (T) clazz.getDeclaredMethod(methodName, parameterTypes).invoke(object, args);
        } catch (Throwable t) {
            throw new IllegalStateException(t);
        }
    }

    public static <T> T callMethod(Object object, String methodName, Class<?>[] parameterTypes, Object... args) {
        return callMethod(object.getClass(), object, methodName, parameterTypes, args);
    }

    public static <T> T callMethod(Class<?> clazz, String methodName) {
        return callMethod(clazz, null, methodName, new Class[]{});
    }

    private static boolean methodExist(Class<?> clazz, String methodName, Class<?>... parameterTypes) {
        try {
            clazz.getDeclaredMethod(methodName, parameterTypes);
            return true;
        } catch (Throwable ignored) {
        }
        return false;
    }

    public static Object getGlobalRegionScheduler() {
        if (GLOBAL_REGION_SCHEDULER == null) {
            GLOBAL_REGION_SCHEDULER = callMethod(Bukkit.class, "getGlobalRegionScheduler");
        }
        return GLOBAL_REGION_SCHEDULER;
    }

    public static void runTask(PlayerHousing plugin, Runnable runnable) {
        if (plugin.isFolia()) {
            Object RegionScheduler = getGlobalRegionScheduler();
            callMethod(RegionScheduler, "run", new Class[]{Plugin.class, Consumer.class}, plugin, (Consumer<?>) (task) -> runnable.run());
            return;
        }
        Bukkit.getScheduler().runTask(plugin, runnable);
    }
    
    public static void runTaskAsynchronously(PlayerHousing plugin, Runnable runnable) {
        if (plugin.isFolia()) {
            Object RegionScheduler = getGlobalRegionScheduler();
            callMethod(RegionScheduler, "run", new Class[]{Plugin.class, Consumer.class}, plugin, (Consumer<?>) (task) -> runnable.run());
            return;
        }
        Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable);
    }

    public static void runTaskTimer(PlayerHousing plugin, Runnable runnable, long initialDelayTicks, long periodTicks) {
        if (plugin.isFolia()) {
            Object RegionScheduler = getGlobalRegionScheduler();
            callMethod(RegionScheduler, "runAtFixedRate", new Class[]{Plugin.class, Consumer.class, long.class, long.class},
                plugin, (Consumer<?>) (task) -> runnable.run(), initialDelayTicks, periodTicks);
            return;
        }
        Bukkit.getScheduler().runTaskTimer(plugin, runnable, initialDelayTicks, periodTicks);
    }

    public static void runTaskLater(PlayerHousing plugin, Runnable runnable, long delayedTicks) {
        if (plugin.isFolia()) {
            Object RegionScheduler = getGlobalRegionScheduler();
            callMethod(RegionScheduler, "runDelayed", new Class[]{Plugin.class, Consumer.class, long.class},
                plugin, (Consumer<?>) (task) -> runnable.run(), delayedTicks);
            return;
        }
        Bukkit.getScheduler().runTaskLater(plugin, runnable, delayedTicks);
    }
    
    public static void runTaskLater(PlayerHousing plugin, Location loc, Runnable runnable, long delayedTicks) {
        if (plugin.isFolia()) {
            Object RegionScheduler = getGlobalRegionScheduler();
            callMethod(RegionScheduler, "runDelayed", new Class[]{Plugin.class, Location.class, Consumer.class, long.class},
                plugin, (Consumer<?>) (task) -> runnable.run(), delayedTicks);
            return;
        }
        Bukkit.getScheduler().runTaskLater(plugin, runnable, delayedTicks);
    }
    
    public static void runTaskLater(Entity entity, PlayerHousing plugin, Runnable runnable, Runnable callback, long delayedTicks) {
        if (plugin.isFolia()) {
            Object RegionScheduler = entity.getScheduler();
            callMethod(RegionScheduler, "runDelayed", new Class[]{Plugin.class, Consumer.class, long.class},
                plugin, (Consumer<?>) (task) -> runnable.run(), (callback == null ? null :  (Consumer<?>) (task) -> runnable.run()), delayedTicks);
            return;
        }
        Bukkit.getScheduler().runTaskLater(plugin, runnable, delayedTicks);
    }
    
    public static void runTask(PlayerHousing plugin, Location loc, Runnable runnable) {
        if (plugin.isFolia()) {
            Object RegionScheduler = Bukkit.getRegionScheduler();
            callMethod(RegionScheduler, "runDelayed", new Class[]{Plugin.class, Location.class, Consumer.class},
                plugin, (Consumer<?>) (task) -> runnable.run());
            return;
        }
        Bukkit.getScheduler().runTask(plugin, runnable);
    }
}