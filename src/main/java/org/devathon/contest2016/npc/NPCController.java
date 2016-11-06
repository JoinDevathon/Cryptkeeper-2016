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
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.devathon.contest2016.Options;
import org.devathon.contest2016.Plugin;
import org.devathon.contest2016.npc.data.SpawnControl;
import org.devathon.contest2016.learning.PatternMatrix;
import org.devathon.contest2016.npc.data.ArmorCategory;
import org.devathon.contest2016.npc.entity.FakeZombie;
import org.devathon.contest2016.npc.logic.AttackLogic;
import org.devathon.contest2016.npc.logic.GoldenAppleLogic;
import org.devathon.contest2016.npc.logic.Logic;
import org.devathon.contest2016.npc.logic.ThrowPotionLogic;
import org.devathon.contest2016.util.EntityUtil;
import org.devathon.contest2016.util.NMSUtil;
import org.devathon.contest2016.util.SelectUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.devathon.contest2016.util.ItemStackUtil.getGenericAttackDamage;
import static org.devathon.contest2016.util.ItemStackUtil.getGenericDefense;

/**
 * @author Cryptkeeper
 * @since 05.11.2016
 */
public class NPCController {

    private final List<ItemStack> itemStacks = new ArrayList<>();
    private final List<ItemStack> pendingPickups = new ArrayList<>();

    private final List<Logic> logicHandlers = Arrays.asList(new ThrowPotionLogic(this), new AttackLogic(this), new GoldenAppleLogic(this));

    private final UUID target;
    private final SpawnControl point;

    private int ticksTilOverride;
    private FakeZombie entity;

    public NPCController(UUID target, SpawnControl point) {
        this.target = target;
        this.point = point;
    }

    public void tick() {
        getBukkitEntity().setTarget(getTarget());

        executeLogics();

        updateDroppedItems();
        updateSpeed();
        updateNameTag();
    }

    public void attemptSpawn() {
        if (point.attemptSpawn()) {
            Player target = getTarget();

            Location location = point.toLocation(target.getWorld());

            entity = new FakeZombie(location);

            Zombie entity = getBukkitEntity();

            entity.setCanPickupItems(false);
            entity.setCustomNameVisible(true);

            EntityUtil.reset(entity);

            Options.KIT_ITEMS.forEach(itemStack -> itemStacks.add(itemStack.clone()));

            // TODO: Messages

            updateEquipment();
            tick();
        }
    }

    public void destroy() {
        entity.world.removeEntity(entity);

        PatternMatrix.of(target).end();
    }

    private void executeLogics() {
        logicHandlers.forEach(Logic::tick);

        PatternMatrix.Event event = PatternMatrix.of(target).getExpectedEvent();

        if (event != null) {
            event = event.flip();
        }

        TObjectDoubleMap<Logic> byWeight = new TObjectDoubleHashMap<>();

        for (Logic logic : logicHandlers) {
            double weight = logic.getWeight(event);

            if (weight > 0) {
                byWeight.put(logic, weight);
            }
        }

        Logic logic = SelectUtil.select(byWeight);

        if (logic != null) {
            logic.execute();
        }
    }

    private void updateDroppedItems() {
        getBukkitEntity().getNearbyEntities(2.5, 1.5, 2.5).stream().filter(entity -> entity instanceof Item).forEach(entity -> {
            Item item = (Item) entity;

            if (item.getPickupDelay() <= 10) {
                pendingPickups.add(item.getItemStack());

                item.remove();
            }
        });

        if (pendingPickups.size() > 0) {
            if (isWithinToTarget(4 * 4)) {
                ticksTilOverride--;

                if (ticksTilOverride <= 0) {
                    ticksTilOverride = 100;

                    itemStacks.add(pendingPickups.remove(0));
                }
            } else {
                setFrozen(true);

                long delay = Math.min(pendingPickups.size(), 3) * 20;

                itemStacks.addAll(pendingPickups);
                pendingPickups.clear();

                Bukkit.getScheduler().scheduleSyncDelayedTask(Plugin.getInstance(), () -> {
                    setFrozen(false);
                }, delay);
            }

            updateEquipment();
        }
    }

    private void updateSpeed() {
        if (!isWithinToTarget(Math.pow(Options.SPRINT_DISTANCE, 2))) {
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
        double speed = sprinting ? Options.SPRINT_SPEED : Options.WALK_SPEED;

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
        return Bukkit.getPlayer(target);
    }

    private void updateNameTag() {
        Zombie entity = getBukkitEntity();

        entity.setCustomName(((int) getBukkitEntity().getHealth()) + "/" + ((int) getBukkitEntity().getMaxHealth()));
    }

    public void updateEquipment() {
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

        List<Pair<ItemStack, Double>> weighted = itemStacks.stream()
                .map(itemStack -> Pair.of(itemStack, getGenericAttackDamage(itemStack.getType())))
                .filter(pair -> pair.getRight() > 0)
                .collect(Collectors.toList());

        weighted.sort((a, b) -> -Double.compare(a.getRight(), b.getRight()));

        if (weighted.size() > 0) {
            getBukkitEntity().getEquipment().setItemInMainHand(weighted.get(0).getLeft());
        }
    }

    public List<ItemStack> getInventory() {
        return itemStacks;
    }

    public boolean isAlive() {
        return entity != null && entity.isAlive();
    }

    public boolean isWithinToTarget(double distanceSq) {
        Player target = getTarget();

        if (target != null) {
            double currentDistanceSq = getLocation().distanceSquared(target.getLocation());

            return currentDistanceSq < distanceSq;
        }

        return false;
    }
}
