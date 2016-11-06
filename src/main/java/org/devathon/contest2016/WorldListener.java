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

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;

/**
 * @author Cryptkeeper
 * @since 05.11.2016
 */
public class WorldListener implements Listener {

    @EventHandler
    public void onWorldLoad(WorldLoadEvent event) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(DevathonPlugin.getInstance(), () -> {
            int size = 50;

            for (int x = 0; x < size; x++) {
                for (int z = 0; z < size; z++) {
                    int maxHeight = (x == 0 || z == 0 || x == size - 1 || z == size - 1) ? 5 : 1;

                    for (int y = 0; y < maxHeight; y++) {
                        Location location = new Location(event.getWorld(), x, 200 + y, z);

                        location.getBlock().setType(maxHeight > 1 ? Material.STONE : Material.GRASS);
                    }
                }
            }

            event.getWorld().setSpawnLocation(size / 2, 205, size / 2);
        });
    }
}
