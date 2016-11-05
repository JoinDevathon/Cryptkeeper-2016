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

/**
 * @author Cryptkeeper
 * @since 05.11.2016
 */
public class ItemStackUtil {

    public static double getGenericDefense(ArmorCategory category, Material material) {
        switch (category) {
            case HELMET:
                switch (material) {
                    case LEATHER_HELMET:
                        return 1;
                    case GOLD_HELMET:
                    case CHAINMAIL_HELMET:
                    case IRON_HELMET:
                        return 2;
                    case DIAMOND_HELMET:
                        return 3;

                    default:
                        return 0;
                }

            case CHESTPLATE:
                switch (material) {
                    case LEATHER_CHESTPLATE:
                        return 3;
                    case GOLD_CHESTPLATE:
                    case CHAINMAIL_CHESTPLATE:
                        return 5;
                    case IRON_CHESTPLATE:
                        return 6;
                    case DIAMOND_CHESTPLATE:
                        return 8;

                    default:
                        return 0;
                }

            case LEGGINGS:
                switch (material) {
                    case LEATHER_LEGGINGS:
                        return 2;
                    case GOLD_LEGGINGS:
                        return 3;
                    case CHAINMAIL_LEGGINGS:
                        return 4;
                    case IRON_LEGGINGS:
                        return 5;
                    case DIAMOND_LEGGINGS:
                        return 6;

                    default:
                        return 0;
                }

            case BOOTS:
                switch (material) {
                    case LEATHER_BOOTS:
                    case CHAINMAIL_BOOTS:
                    case GOLD_BOOTS:
                        return 1;
                    case IRON_BOOTS:
                        return 2;
                    case DIAMOND_BOOTS:
                        return 3;

                    default:
                        return 0;
                }

            case SHIELD:
                switch (material) {
                    case SHIELD:
                        return 1;

                    default:
                        return 0;
                }

            default:
                return 0;
        }
    }

    public static double getGenericAttackDamage(Material material) {
        switch (material) {
            case WOOD_SWORD:
                return 6.4;
            case STONE_SWORD:
                return 8;
            case IRON_SWORD:
                return 9.6;
            case DIAMOND_SWORD:
                return 11.2;
            case GOLD_SWORD:
                return 6.4;

            case WOOD_SPADE:
                return 2.5;
            case STONE_SPADE:
                return 3.5;
            case IRON_SPADE:
                return 4.5;
            case DIAMOND_SPADE:
                return 5.5;
            case GOLD_SPADE:
                return 2.5;

            case WOOD_PICKAXE:
                return 2.4;
            case STONE_PICKAXE:
                return 3.6;
            case IRON_PICKAXE:
                return 4.8;
            case DIAMOND_PICKAXE:
                return 6;
            case GOLD_PICKAXE:
                return 2.4;

            case WOOD_AXE:
                return 5.6;
            case STONE_AXE:
                return 7.2;
            case IRON_AXE:
                return 8.1;
            case DIAMOND_AXE:
                return 9;
            case GOLD_AXE:
                return 7.1;

            default:
                return 0;
        }
    }
}
