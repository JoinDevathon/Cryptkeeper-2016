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
package org.devathon.contest2016.util;

import net.minecraft.server.v1_10_R1.EntityInsentient;
import net.minecraft.server.v1_10_R1.EntityTypes;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * @author Cryptkeeper
 * @since 05.11.2016
 */
public class EntityUtil {

    private static Map<Class<? extends net.minecraft.server.v1_10_R1.Entity>, Integer> ENTITY_ID_BY_CLASS;

    static {
        try {
            Field field = EntityTypes.class.getDeclaredField("f");

            field.setAccessible(true);

            ENTITY_ID_BY_CLASS = (Map<Class<? extends net.minecraft.server.v1_10_R1.Entity>, Integer>) field.get(null);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void reset(LivingEntity entity) {
        entity.getEquipment().setArmorContents(new ItemStack[4]);

        entity.getEquipment().setItemInMainHand(null);
        entity.getEquipment().setItemInOffHand(null);
    }

    public static void register(EntityType entityType, Class<? extends net.minecraft.server.v1_10_R1.Entity> clazz) {
        ENTITY_ID_BY_CLASS.put(clazz, (int) entityType.getTypeId());
    }

    public static void look(Entity entity, Entity other) {
        net.minecraft.server.v1_10_R1.Entity entityOther = ((CraftEntity) other).getHandle();

        if (entity instanceof EntityInsentient) {
            ((EntityInsentient) entity).getControllerLook().a(entityOther, 10.F, ((EntityInsentient) entity).N());
        }
    }
}
