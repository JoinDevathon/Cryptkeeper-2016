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
import org.bukkit.entity.ThrownPotion;
import org.bukkit.inventory.ItemStack;
import org.devathon.contest2016.Options;
import org.devathon.contest2016.learning.PatternMatrix;
import org.devathon.contest2016.npc.NPCController;

import java.util.List;

/**
 * @author Cryptkeeper
 * @since 05.11.2016
 */
public class ThrowPotionLogic extends ConsumeLogic {

    private int sincePotionThrown;

    public ThrowPotionLogic(NPCController npc) {
        super(npc);
    }

    @Override
    public void tick() {
        sincePotionThrown = Math.max(sincePotionThrown - 1, 0);
    }

    @Override
    protected void _execute(ItemStack itemStack) {
        sincePotionThrown = Options.POTION_THROW_DELAY;

        ThrownPotion thrownPotion = npc.getBukkitEntity().launchProjectile(ThrownPotion.class);

        thrownPotion.setItem(itemStack);
    }

    @Override
    public double getWeight(PatternMatrix.Event event) {
        if (sincePotionThrown > 0) {
            return 0;
        }

        if (!npc.isWithinToTarget(5.5 * 5.5)) {
            return 0;
        }

        List<ItemStack> itemStacks = getRelevantItemStacks();

        if (itemStacks == null) {
            return 0;
        }

        if (event == PatternMatrix.Event.THROW_POTION) {
            return 1;
        }

        return itemStacks.size() / (4 * 9);
    }

    @Override
    protected boolean isRelevant(ItemStack itemStack) {
        return itemStack.getType() == Material.SPLASH_POTION;
    }
}
