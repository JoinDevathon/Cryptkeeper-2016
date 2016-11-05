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

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Cryptkeeper
 * @since 05.11.2016
 */
public class LivingEntityNPC<T extends LivingEntity> implements NPC {

    private final Class<T> clazz;

    private final List<ItemStack> itemStacks = new ArrayList<>();

    private T entity;

    public LivingEntityNPC(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public void spawn(Location location) {
        entity = location.getWorld().spawn(location, clazz);

        entity.setCanPickupItems(false);

        entity.setCustomNameVisible(true);
        entity.setCustomName(ChatColor.RED + "Combat Trainer");

        entity.getEquipment().setArmorContents(new ItemStack[4]);
        entity.getEquipment().setItemInMainHand(null);
        entity.getEquipment().setItemInOffHand(null);

        entity.getEquipment().setHelmetDropChance(1F);
        entity.getEquipment().setChestplateDropChance(1F);
        entity.getEquipment().setLeggingsDropChance(1F);
        entity.getEquipment().setBootsDropChance(1F);

        pickupItem(new ItemStack(Material.GOLD_SWORD));
        pickupItem(new ItemStack(Material.DIAMOND_SWORD));
    }

    @Override
    public void pickupItem(ItemStack itemStack) {
        itemStacks.add(itemStack);

        updateWeapon();

        // TODO: Compare weapon
        // TODO: Compare armor
        // TODO: Blocking
    }

    private void updateWeapon() {
        List<ItemStack> weighted = new ArrayList<>(itemStacks);

        weighted.sort((a, b) -> -Double.compare(ItemStackUtil.getGenericAttackDamage(a.getType()), ItemStackUtil.getGenericAttackDamage(b.getType())));

        if (weighted.size() > 0) {
            System.out.println("weighted = " + weighted);
            entity.getEquipment().setItemInMainHand(weighted.get(0));
        }
    }

    @Override
    public List<ItemStack> getInventory() {
        return itemStacks;
    }
}
