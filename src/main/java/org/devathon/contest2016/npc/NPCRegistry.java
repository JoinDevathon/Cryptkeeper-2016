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

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

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

    private final List<NPCController> controllers = new ArrayList<>();

    public void register(NPCController npc) {
        controllers.add(npc);
    }

    public void tick() {
        List<NPCController> toRemove = null;

        for (NPCController npc : controllers) {
            Player target = npc.getTarget();

            if (target == null) {
                if (toRemove == null) {
                    toRemove = new ArrayList<>();
                }

                toRemove.add(npc);

                npc.destroy();
            } else if (target.isDead()) {
                npc.destroy();
            } else if (npc.isAlive()) {
                npc.tick();
            } else {
                npc.attemptSpawn();
            }
        }

        if (toRemove != null) {
            controllers.removeAll(toRemove);
        }
    }

    public boolean isController(int entityId) {
        for (NPCController npc : controllers) {
            if (npc.isAlive() && npc.getBukkitEntity().getEntityId() == entityId) {
                return true;
            }
        }

        return false;
    }

    public boolean isController(Entity entity) {
        return isController(entity.getEntityId());
    }
}
