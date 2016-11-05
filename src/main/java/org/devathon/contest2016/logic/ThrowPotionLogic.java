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
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.inventory.ItemStack;
import org.devathon.contest2016.entity.npc.NPC;
import org.devathon.contest2016.util.EntityUtil;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * @author Cryptkeeper
 * @since 05.11.2016
 */
public class ThrowPotionLogic implements Logic {

    private final NPC npc;

    private int sincePotionThrown;

    public ThrowPotionLogic(NPC npc) {
        this.npc = npc;
    }

    @Override
    public void tick() {
        sincePotionThrown = Math.max(sincePotionThrown--, 0);
    }

    @Override
    public void execute() {
        sincePotionThrown = (2 + ThreadLocalRandom.current().nextInt(3)) * 5;

        if (npc.getTarget() != null) {
            EntityUtil.look(npc.getBukkitEntity(), npc.getTarget());
        }

        List<ItemStack> potions = getPotions();

        ItemStack itemStack = potions.remove(0);

        npc.getInventory().remove(itemStack);

        ThrownPotion thrownPotion = npc.getBukkitEntity().launchProjectile(ThrownPotion.class);

        thrownPotion.setItem(itemStack);
    }

    @Override
    public double getWeight() {
        if (sincePotionThrown > 0) {
            return 0;
        }

        LivingEntity target = npc.getTarget();

        if (target == null) {
            return 0;
        }

        double distanceSq = target.getLocation().distanceSquared(npc.getLocation());

        if (distanceSq > Math.pow(5.5, 2)) {
            return 0;
        }

        List<ItemStack> potions = getPotions();

        if (potions.size() == 0) {
            return 0;
        }

        return potions.size() / (4D * 9D);
    }

    private List<ItemStack> getPotions() {
        return npc.getInventory().stream()
                .filter(itemStack -> itemStack.getType() == Material.SPLASH_POTION)
                .collect(Collectors.toList());
    }
}
