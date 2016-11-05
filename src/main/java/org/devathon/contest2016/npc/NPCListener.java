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

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.devathon.contest2016.DevathonPlugin;

/**
 * @author Cryptkeeper
 * @since 05.11.2016
 */
public class NPCListener implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPotionSplash(EntityDamageByEntityEvent event) {
        if (DevathonPlugin.getInstance().getNPCRegistry().isNPC(event.getEntity())) {
            if (event.getDamager() instanceof ThrownPotion) {
                event.setCancelled(true);

                LivingEntity entity = ((LivingEntity) event.getEntity());

                entity.setHealth(Math.min(entity.getMaxHealth(), entity.getHealth() + event.getDamage()));
            }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onEntityRegainHealth(EntityRegainHealthEvent event) {
        if (DevathonPlugin.getInstance().getNPCRegistry().isNPC(event.getEntity())) {
            if (event.getRegainReason() == EntityRegainHealthEvent.RegainReason.MAGIC) {
                event.setCancelled(true);

                ((LivingEntity) event.getEntity()).damage(event.getAmount());
            }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onEntityCombust(EntityCombustEvent event) {
        if (DevathonPlugin.getInstance().getNPCRegistry().isNPC(event.getEntity()) && event.getDuration() == 8) {
            event.setCancelled(true);
        }
    }
}
