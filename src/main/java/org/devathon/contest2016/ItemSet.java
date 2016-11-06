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
public class ItemSet {

    public static final List<ItemStack> STANDARD_ITEMS = Arrays.asList(
            new ItemStack(Material.DIAMOND_SWORD),

            new ItemStack(Material.GOLDEN_APPLE),
            new ItemStack(Material.GOLDEN_APPLE),
            new ItemStack(Material.GOLDEN_APPLE),

            ItemStackUtil.makeSplashPotion(Material.SPLASH_POTION, Arrays.asList(new PotionEffect(PotionEffectType.SLOW, 20 * 10, 0))),
            ItemStackUtil.makeSplashPotion(Material.SPLASH_POTION, Arrays.asList(new PotionEffect(PotionEffectType.WITHER, 20 * 10, 0))),
            ItemStackUtil.makeSplashPotion(Material.SPLASH_POTION, Arrays.asList(new PotionEffect(PotionEffectType.HUNGER, 20 * 10, 0))),

            new ItemStack(Material.IRON_CHESTPLATE),
            new ItemStack(Material.DIAMOND_HELMET)
    );
}
