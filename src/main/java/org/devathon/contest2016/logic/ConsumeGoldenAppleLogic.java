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

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.devathon.contest2016.Plugin;
import org.devathon.contest2016.learning.PatternMatrix;
import org.devathon.contest2016.npc.NPC;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Cryptkeeper
 * @since 05.11.2016
 */
public class ConsumeGoldenAppleLogic implements Logic {

    private final NPC npc;

    private int sinceConsumeItem;

    public ConsumeGoldenAppleLogic(NPC npc) {
        this.npc = npc;
    }

    @Override
    public void tick() {
        sinceConsumeItem = Math.max(sinceConsumeItem - 1, 0);
    }

    @Override
    public void execute() {
        sinceConsumeItem = npc.getOptions().getConsumeItemDelay();

        List<ItemStack> items = getConsumables();

        Collections.shuffle(items);

        ItemStack itemStack = items.remove(0);

        npc.getInventory().remove(itemStack);

        npc.getBukkitEntity().getEquipment().setItemInMainHand(itemStack);

        npc.getBukkitEntity().addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 2 * 60 * 20, 0));
        npc.getBukkitEntity().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 5 * 20, 1));

        npc.setFrozen(true);

        Bukkit.getScheduler().scheduleSyncDelayedTask(Plugin.getInstance(), () -> {
            npc.updateWeapon();
            npc.setFrozen(false);
        }, 30L);
    }

    @Override
    public double getWeight(PatternMatrix.Event event) {
        if (sinceConsumeItem > 0) {
            return 0;
        }

        if (npc.isWithinToTarget(5 * 5)) {
            return 0;
        }

        double downHealth = npc.getBukkitEntity().getMaxHealth() - npc.getBukkitEntity().getHealth();

        if (downHealth < 4 * 2) {
            return 0;
        }

        List<ItemStack> items = getConsumables();

        if (items.size() == 0) {
            return 0;
        }

        if (event == PatternMatrix.Event.CONSUME_GOLDEN_APPLE) {
            return 1D;
        } else {
            return Math.max(items.size(), 9) / (4D * 9D);
        }
    }

    private List<ItemStack> getConsumables() {
        return npc.getInventory().stream()
                .filter(itemStack -> itemStack.getType() == Material.GOLDEN_APPLE)
                .collect(Collectors.toList());
    }
}
