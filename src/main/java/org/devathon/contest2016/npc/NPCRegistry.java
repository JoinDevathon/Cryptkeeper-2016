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

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.devathon.contest2016.Plugin;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Cryptkeeper
 * @since 05.11.2016
 */
public class NPCRegistry {

    private static final NPCRegistry INSTANCE = new NPCRegistry();

    public static NPCRegistry getInstance() {
        return INSTANCE;
    }

    private NPCRegistry() {
    }

    private final List<NPC> npcs = new ArrayList<>();

    public void start() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Plugin.getInstance(), this::tick, 1L, 1L);
    }

    public void register(NPC npc) {
        npcs.add(npc);
    }

    private void tick() {
        List<NPC> toRemove = null;

        for (NPC npc : npcs) {
            if (!npc.isAlive() || (npc.getTarget() == null || npc.getTarget().isDead())) {
                if (toRemove == null) {
                    toRemove = new ArrayList<>();
                }

                toRemove.add(npc);

                npc.destroy();

                continue;
            }

            npc.tick();
        }

        if (toRemove != null) {
            npcs.removeAll(toRemove);
        }
    }

    public boolean isNPC(int entityId) {
        for (NPC npc : npcs) {
            if (npc.getBukkitEntity().getEntityId() == entityId) {
                return true;
            }
        }

        return false;
    }

    public boolean isNPC(Entity entity) {
        return isNPC(entity.getEntityId());
    }
}
