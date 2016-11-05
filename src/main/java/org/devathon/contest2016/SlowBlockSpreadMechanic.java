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
import org.bukkit.block.Block;

import java.util.Random;

/**
 * @author Cryptkeeper
 * @since 05.11.2016
 */
public class SlowBlockSpreadMechanic implements SpreadMechanic {

    private static final Random RANDOM = new Random();

    private final int rate;

    private final Selector<Material> material;
    private final byte data;
    private final Location source;

    private int maxRadius = 1;
    private int tickAttempts;
    private int modSteps = 2;

    public SlowBlockSpreadMechanic(int rate, Selector<Material> material, byte data, Location source) {
        this.rate = rate;
        this.material = material;
        this.data = data;
        this.source = source;
    }

    @Override
    public void attempt() {
        tickAttempts += 1;

        if (tickAttempts % Math.min(Math.pow(modSteps, 2), 4 * 4) == 0) {
            maxRadius += 1;
            modSteps += 1;

            System.out.println("tickAttempts = " + tickAttempts);
            System.out.println("maxRadius = " + maxRadius);
            System.out.println("modSteps = " + modSteps);
        }

        // TODO: expo curve, max toSpawn

        int toSpawn = RANDOM.nextInt(rate) + rate / 2;

        for (int i = 0; i < toSpawn; i++) {
            int xOffset = nextWithinRadius();
            int zOffset = nextWithinRadius();

            Location newLocation = source.clone().add(xOffset, 0, zOffset); // TODO: Y shapes?

            double distanceFromMid = newLocation.distance(source);
            int maxHeight = (int) Math.max((maxRadius / 4) - distanceFromMid, 0);

            maxHeight = Math.max(maxHeight, newLocation.getWorld().getMaxHeight() - 1);

            for (int y = 0; y < maxHeight; y++) {
                Location aboveLocation = newLocation.clone().add(0, y, 0);

                if (!material.options().contains(aboveLocation.getBlock().getType())) {
                    updateBlock(aboveLocation);

                    break;
                }
            }

            for (int y = 0; y > -10; y--) {
                Location belowLocation = newLocation.clone().add(0, y, 0);

                if (!material.options().contains(belowLocation.getBlock().getType())) {
                    updateBlock(belowLocation);

                    break;
                }
            }

            updateBlock(newLocation);
        }
    }

    private void updateBlock(Location location) {
        Block block = location.getBlock();

        if (!material.options().contains(block.getType())) {
            Material selectedMaterial = material.select();

            if (block.getType() != selectedMaterial) {
                block.setType(selectedMaterial);
            }
        }

        if (block.getData() != data) {
            block.setData(data);
        }
    }

    private int nextWithinRadius() {
        int base = RANDOM.nextInt(maxRadius) - (maxRadius / 4) - RANDOM.nextInt(maxRadius) / 2;

        return (RANDOM.nextBoolean() ? -1 : 1) * base;
    }
}
