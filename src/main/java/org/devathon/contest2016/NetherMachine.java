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

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * @author Cryptkeeper
 * @since 05.11.2016
 */
public class NetherMachine implements Machine {

    private final Location location;

    private final MaterialSelector materialSelector = new MaterialSelector()
            .pair(Material.NETHERRACK, 0.85D)
            .pair(Material.SOUL_SAND, 0.15D);

    private final SpreadMechanic spreadMechanic;

    public NetherMachine(Location location) {
        this.location = location;
        this.spreadMechanic = new SlowBlockSpreadMechanic(10, materialSelector, (byte) 0, location.clone().subtract(0, 1, 0));
    }

    @Override
    public void tick() {
        spreadMechanic.attempt();
    }

    @Override
    public void update(Player player, double distanceSq) {
        // TODO: Night vision, potion effects
        // TODO: Mobs
    }

    @Override
    public Location getOriginLocation() {
        return location;
    }
}
