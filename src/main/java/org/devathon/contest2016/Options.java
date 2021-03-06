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

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.devathon.contest2016.util.ItemStackUtil;

import java.util.Arrays;
import java.util.List;

/**
 * @author Cryptkeeper
 * @since 05.11.2016
 */
public class Options {

    public static final int MIN_CPS = 1;
    public static final int MAX_CPS = 4;

    public static final int POTION_THROW_DELAY = 60;
    public static final int CONSUME_ITEM_DELAY = 100;
    public static final int FIREBALL_USE_DELAY = 100;

    public static final double LOW_REACH_DISTANCE = 1;
    public static final double HIGH_REACH_DISTANCE = 2.5;

    public static final double SPRINT_DISTANCE = 5;

    public static final double WALK_SPEED = 2.75;
    public static final double SPRINT_SPEED = 3.75;

    public static final double JUMP_CHANCE = 0.65;

    public static final int ARENA_SIZE = 20;

    public static final List<ItemStack> NPC_KIT_ITEMS = Arrays.asList(
            new ItemStack(Material.IRON_SWORD),

            new ItemStack(Material.GOLDEN_APPLE, 3),

            ItemStackUtil.makeSplashPotion(Material.SPLASH_POTION, Arrays.asList(new PotionEffect(PotionEffectType.SLOW, 20 * 10, 1))),
            ItemStackUtil.makeSplashPotion(Material.SPLASH_POTION, Arrays.asList(new PotionEffect(PotionEffectType.WITHER, 20 * 10, 0))),
            ItemStackUtil.makeSplashPotion(Material.SPLASH_POTION, Arrays.asList(new PotionEffect(PotionEffectType.HARM, 20 * 10, 0))),

            new ItemStack(Material.FIREBALL, 2),

            new ItemStack(Material.LEATHER_HELMET),
            new ItemStack(Material.IRON_CHESTPLATE)
    );

    public static final List<ItemStack> KIT_ITEMS = Arrays.asList(
            new ItemStack(Material.DIAMOND_SWORD),

            new ItemStack(Material.GOLDEN_APPLE, 3),

            ItemStackUtil.makeSplashPotion(Material.SPLASH_POTION, Arrays.asList(new PotionEffect(PotionEffectType.SLOW, 20 * 10, 1))),
            ItemStackUtil.makeSplashPotion(Material.SPLASH_POTION, Arrays.asList(new PotionEffect(PotionEffectType.WITHER, 20 * 10, 0))),
            ItemStackUtil.makeSplashPotion(Material.SPLASH_POTION, Arrays.asList(new PotionEffect(PotionEffectType.HARM, 20 * 10, 0)))
    );

    private Options() {
    }
}
