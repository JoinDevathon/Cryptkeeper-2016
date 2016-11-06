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
package org.devathon.contest2016.npc.logic;

import org.bukkit.Material;
import org.bukkit.entity.LargeFireball;
import org.bukkit.inventory.ItemStack;
import org.devathon.contest2016.Options;
import org.devathon.contest2016.learning.PatternMatrix;
import org.devathon.contest2016.npc.NPCController;

import java.util.List;

/**
 * @author Cryptkeeper
 * @since 05.11.2016
 */
public class FireballLogic extends ConsumeLogic {

    private int sinceFireballUse = Options.FIREBALL_USE_DELAY;

    public FireballLogic(NPCController npc) {
        super(npc);
    }

    @Override
    public void tick() {
        sinceFireballUse = Math.max(sinceFireballUse - 1, 0);
    }

    @Override
    protected void _execute(ItemStack itemStack) {
        sinceFireballUse = Options.FIREBALL_USE_DELAY;

        npc.getBukkitEntity().launchProjectile(LargeFireball.class);
    }

    @Override
    public double getWeight(PatternMatrix.Event event) {
        if (sinceFireballUse > 0) {
            return 0;
        }

        if (npc.isWithinToTarget(5.5 * 5.5)) {
            return 0;
        }

        List<ItemStack> itemStacks = getRelevantItemStacks();

        if (itemStacks == null) {
            return 0;
        }

        if (event == PatternMatrix.Event.USE_FIREBALL) {
            return 1;
        }

        double base = itemStacks.size() / (4 * 9);

        if (npc.getTarget().isSprinting()) {
            base += 0.5;
        }

        return base;
    }

    @Override
    protected boolean isRelevant(ItemStack itemStack) {
        return itemStack.getType() == Material.FIREBALL;
    }
}
