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

import gnu.trove.map.TObjectDoubleMap;
import gnu.trove.map.hash.TObjectDoubleHashMap;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.devathon.contest2016.ItemStackUtil.getGenericAttackDamage;
import static org.devathon.contest2016.ItemStackUtil.getGenericDefense;

/**
 * @author Cryptkeeper
 * @since 05.11.2016
 */
public class CreatureNPC implements NPC {

    private final Class<? extends Creature> clazz;
    private final List<Logic> logics;

    private final List<ItemStack> itemStacks = new ArrayList<>();

    private Creature entity;

    public CreatureNPC(Class<? extends Creature> clazz) {
        this.clazz = clazz;

        this.logics = Arrays.asList(new ThrowPotionLogic(this), new AttackLogic(this));
    }

    @Override
    public void tick() {
        if (entity.isDead() || !entity.isValid()) {
            return;
        }

        Player target = EntityUtil.getClosestEntity(entity, 10, filter -> filter instanceof Player);

        entity.setTarget(target);

        System.out.println("target = " + target);

        logics.forEach(Logic::tick);

        TObjectDoubleMap<Logic> byWeight = new TObjectDoubleHashMap<>();

        for (Logic logic : logics) {
            double weight = logic.getWeight();

            if (weight > 0) {
                byWeight.put(logic, logic.getWeight());
            }
        }

        Logic logic = SelectUtil.select(byWeight);

        System.out.println("byWeight = " + byWeight);
        System.out.println("logic = " + logic);

        if (logic != null) {
            logic.execute();
        }
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

        for (int i = 0; i < 36; i++) {
            itemStacks.add(ItemStackUtil.makeSplashPotion(Material.SPLASH_POTION, Arrays.asList(new PotionEffect(PotionEffectType.SPEED, 20 * 10, 0))));
        }
    }

    @Override
    public void pickupItem(ItemStack itemStack) {
        itemStacks.add(itemStack);

        updateWeapon();
        updateArmor();
    }

    @Override
    public Creature getEntity() {
        return entity;
    }

    @Override
    public Location getLocation() {
        return entity.getLocation();
    }

    private void updateWeapon() {
        List<Pair<ItemStack, Double>> weighted = itemStacks.stream()
                .map(itemStack -> Pair.of(itemStack, getGenericAttackDamage(itemStack.getType())))
                .filter(pair -> pair.getRight() > 0)
                .collect(Collectors.toList());

        weighted.sort((a, b) -> -Double.compare(a.getRight(), b.getRight()));

        if (weighted.size() > 0) {
            entity.getEquipment().setItemInMainHand(weighted.get(0).getLeft());
        }
    }

    private void updateArmor() {
        for (ArmorCategory category : ArmorCategory.values()) {
            List<Pair<ItemStack, Double>> weighted = itemStacks.stream()
                    .map(itemStack -> Pair.of(itemStack, getGenericDefense(category, itemStack.getType())))
                    .filter(pair -> pair.getRight() > 0)
                    .collect(Collectors.toList());

            weighted.sort((a, b) -> -Double.compare(a.getRight(), b.getRight()));

            if (weighted.size() > 0) {
                category.applyTo(entity, weighted.get(0).getLeft());
            }
        }
    }

    @Override
    public List<ItemStack> getInventory() {
        return itemStacks;
    }
}
