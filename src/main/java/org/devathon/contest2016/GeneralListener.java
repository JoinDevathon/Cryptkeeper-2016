/*
 * MIT License
 * 
 * Copyright (c) 2016
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.devathon.contest2016;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.devathon.contest2016.npc.NPCController;
import org.devathon.contest2016.npc.NPCRegistry;
import org.devathon.contest2016.npc.data.SpawnControl;
import org.devathon.contest2016.util.ItemStackUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Cryptkeeper
 * @since 05.11.2016
 */
public class GeneralListener implements Listener {

    private final Map<UUID, Location> respawnLocations = new HashMap<>();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        reset(player);

        ItemStack machineItem = ItemStackUtil.makeCustomItemStack(Material.IRON_BLOCK, ChatColor.LIGHT_PURPLE + "The Hell Machine " + ChatColor.GRAY + "(Right Click to Place)");

        player.getInventory().addItem(machineItem);
        player.setGameMode(GameMode.SURVIVAL);
    }

    @EventHandler
    public void onFoodChange(FoodLevelChangeEvent event) {
        event.setCancelled(true);
        event.setFoodLevel(20);
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        event.setCancelled(event.toWeatherState());
    }

    @EventHandler
    public void onThunderChange(ThunderChangeEvent event) {
        event.setCancelled(event.toThunderState());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDeath(PlayerDeathEvent event) {
        respawnLocations.put(event.getEntity().getUniqueId(), event.getEntity().getLocation());

        event.setDeathMessage(null);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDeath(EntityDeathEvent event) {
        event.setDroppedExp(0);
        event.getDrops().clear();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();

        Location location = respawnLocations.remove(player.getUniqueId());

        if (location != null) {
            event.setRespawnLocation(location);

            reset(player);

            Options.KIT_ITEMS.forEach(itemStack -> player.getInventory().addItem(itemStack.clone()));

            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.YELLOW + "You breathe again..."));
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onBlockPlace(BlockPlaceEvent event) {
        // We'll just assume it's our custom ItemStack.
        if (event.getBlock().getType() == Material.IRON_BLOCK) {
            int size = Options.ARENA_SIZE;

            Location location = event.getBlock().getLocation();

            // Set our Y to where we won't collide with anything.
            location.setY(location.getWorld().getHighestBlockYAt(location));

            for (int x = -size / 2; x <= size / 2; x++) {
                for (int z = -size / 2; z <= size / 2; z++) {
                    int maxY = (x == -size / 2 || z == -size / 2 || x == size / 2 || z == size / 2) ? 5 : 1;

                    for (int y = 0; y < maxY; y++) {
                        Block block = location.clone().add(x, y, z).getBlock();

                        if (y == 0) {
                            block.setType(Material.NETHERRACK);
                        } else {
                            block.setType(Material.IRON_FENCE);
                        }
                    }
                }
            }

            Player player = event.getPlayer();

            player.getInventory().clear();
            player.setGameMode(GameMode.ADVENTURE);
            player.teleport(location.clone().add(5, 1, 0));

            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.RED + "An arena appears..."));

            Options.KIT_ITEMS.forEach(itemStack -> player.getInventory().addItem(itemStack.clone()));

            Bukkit.getScheduler().scheduleSyncDelayedTask(Plugin.getInstance(), () -> {
                Location spawnLocation = location.clone().add(0, 1, 0);

                SpawnControl point = new SpawnControl(spawnLocation);
                NPCController controller = new NPCController(player.getUniqueId(), point);

                NPCRegistry.getInstance().register(controller);
            });

            event.setBuild(false);
        }
    }

    private void reset(Player player) {
        player.setHealth(player.getMaxHealth());
        player.setFoodLevel(20);
        player.getInventory().clear();
        player.getEquipment().setArmorContents(new ItemStack[4]);
    }
}
