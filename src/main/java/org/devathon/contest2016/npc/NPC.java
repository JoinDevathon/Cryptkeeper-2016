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
package org.devathon.contest2016.npc;

import gnu.trove.map.TObjectDoubleMap;
import gnu.trove.map.hash.TObjectDoubleHashMap;
import net.minecraft.server.v1_10_R1.GenericAttributes;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.devathon.contest2016.DevathonPlugin;
import org.devathon.contest2016.ItemSet;
import org.devathon.contest2016.data.ArmorCategory;
import org.devathon.contest2016.entity.FakeZombie;
import org.devathon.contest2016.logic.AttackLogic;
import org.devathon.contest2016.logic.ConsumeItemLogic;
import org.devathon.contest2016.logic.Logic;
import org.devathon.contest2016.logic.ThrowPotionLogic;
import org.devathon.contest2016.util.NMSUtil;
import org.devathon.contest2016.util.SelectUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static org.devathon.contest2016.util.ItemStackUtil.getGenericAttackDamage;
import static org.devathon.contest2016.util.ItemStackUtil.getGenericDefense;

/**
 * @author Cryptkeeper
 * @since 05.11.2016
 */
public class NPC {

    private final List<ItemStack> itemStacks = new ArrayList<>();
    private final List<ItemStack> pendingPickups = new ArrayList<>();

    private final Supplier<Player> target;
    private final List<Logic> logics;
    private final NPCOptions options;

    private int ticksTilOverride = 10;

    private FakeZombie entity;

    public NPC(Supplier<Player> target, NPCOptions options) {
        this.target = target;
        this.options = options;

        this.logics = Arrays.asList(new ThrowPotionLogic(this),
                new AttackLogic(this),
                new ConsumeItemLogic(this));
    }

    public void tick() {
        if (!entity.isAlive()) {
            return;
        }

        getBukkitEntity().setTarget(getTarget());

        logics.forEach(Logic::tick);

        TObjectDoubleMap<Logic> byWeight = new TObjectDoubleHashMap<>();

        for (Logic logic : logics) {
            double weight = logic.getWeight();

            if (weight > 0) {
                byWeight.put(logic, logic.getWeight());
            }
        }

        Logic logic = SelectUtil.select(byWeight);

        if (logic != null) {
            logic.execute();
        }

        for (Entity entity : getBukkitEntity().getNearbyEntities(2.5, 1.5, 2.5)) {
            if (entity instanceof Item) {
                Item item = (Item) entity;

                if (item.getPickupDelay() <= 10) {
                    pendingPickups.add(item.getItemStack());

                    item.remove();
                }
            }
        }

        if (pendingPickups.size() > 0) {
            if (!isWithinToTarget(4 * 4)) {
                setFrozen(true);

                long delay = Math.min(pendingPickups.size(), 3) * 20;

                pendingPickups.forEach(this::pickupItem);

                pendingPickups.clear();

                Bukkit.getScheduler().scheduleSyncDelayedTask(DevathonPlugin.getInstance(), () -> {
                    setFrozen(false);
                }, delay);
            } else {
                ticksTilOverride--;

                if (ticksTilOverride <= 0) {
                    ticksTilOverride = 100;

                    pickupItem(pendingPickups.remove(0));
                }
            }
        }

        updateSpeed();
    }

    public void spawn(Location location) {
        entity = new FakeZombie(location);

        Zombie entity = getBukkitEntity();

        entity.setCanPickupItems(false);

        entity.setCustomNameVisible(true);
        entity.setCustomName(options.getDisplayName());

        entity.getEquipment().setArmorContents(new ItemStack[4]);
        entity.getEquipment().setItemInMainHand(null);
        entity.getEquipment().setItemInOffHand(null);

        for (ItemStack itemStack : ItemSet.STANDARD_ITEMS) {
            pickupItem(itemStack.clone());
        }
    }

    private void pickupItem(ItemStack itemStack) {
        itemStacks.add(itemStack);

        updateWeapon();
        updateArmor();
    }

    private void updateSpeed() {
        if (!isWithinToTarget(Math.pow(options.getSprintDistance(), 2))) {
            setSprinting(true);
        } else {
            Player target = getTarget();

            setSprinting(target.isSprinting());
        }
    }

    public void setFrozen(boolean frozen) {
        if (frozen) {
            getBukkitEntity().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 9), true);
        } else {
            getBukkitEntity().removePotionEffect(PotionEffectType.SLOW);
        }
    }

    public void setSprinting(boolean sprinting) {
        double speed = sprinting ? options.getSprintSpeed() : options.getWalkSpeed();

        speed *= NMSUtil.PLAYER_ABILITIES.walkSpeed;

        entity.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(speed);
    }

    public FakeZombie getEntity() {
        return entity;
    }

    public Zombie getBukkitEntity() {
        return (Zombie) entity.getBukkitEntity();
    }

    public Location getLocation() {
        return getBukkitEntity().getLocation();
    }

    public Player getTarget() {
        return target.get();
    }

    private void updateWeapon() {
        List<Pair<ItemStack, Double>> weighted = itemStacks.stream()
                .map(itemStack -> Pair.of(itemStack, getGenericAttackDamage(itemStack.getType())))
                .filter(pair -> pair.getRight() > 0)
                .collect(Collectors.toList());

        weighted.sort((a, b) -> -Double.compare(a.getRight(), b.getRight()));

        if (weighted.size() > 0) {
            getBukkitEntity().getEquipment().setItemInMainHand(weighted.get(0).getLeft());
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
                category.applyTo(getBukkitEntity(), weighted.get(0).getLeft());
            }
        }
    }

    public List<ItemStack> getInventory() {
        return itemStacks;
    }

    public NPCOptions getOptions() {
        return options;
    }

    public boolean isAlive() {
        return entity.isAlive();
    }

    private boolean isWithinToTarget(double distanceSq) {
        Player target = getTarget();

        if (target != null) {
            double currentDistanceSq = getLocation().distanceSquared(target.getLocation());

            return currentDistanceSq < distanceSq;
        }

        return false;
    }
}
