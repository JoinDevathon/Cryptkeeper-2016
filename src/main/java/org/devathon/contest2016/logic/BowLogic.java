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
package org.devathon.contest2016.logic;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.devathon.contest2016.npc.NPC;
import org.devathon.contest2016.util.EntityUtil;

/**
 * @author Cryptkeeper
 * @since 05.11.2016
 */
public class BowLogic implements Logic {

    private final NPC npc;

    public BowLogic(NPC npc) {
        this.npc = npc;
    }

    @Override
    public void tick() {

    }

    @Override
    public void execute() {
        EntityUtil.look(npc.getBukkitEntity(), npc.getTarget());
    }

    @Override
    public double getWeight() {
        if (npc.isWithinToTarget(10 * 10)) {
            return 0;
        }

        if (!hasArrows()) {
            return 0;
        }

        return 0.5;
    }

    private boolean hasArrows() {
        for (ItemStack itemStack : npc.getInventory()) {
            if (itemStack.getType() == Material.ARROW) {
                return true;
            }
        }

        return false;
    }
}
