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
package org.devathon.contest2016.logic;

import net.minecraft.server.v1_10_R1.EnumHand;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;
import org.devathon.contest2016.npc.NPC;
import org.devathon.contest2016.npc.NPCOptions;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Cryptkeeper
 * @since 05.11.2016
 */
public class AttackLogic implements Logic {

    private final NPC npc;

    private int ticksSinceAttack;

    public AttackLogic(NPC npc) {
        this.npc = npc;
    }

    @Override
    public void tick() {
        ticksSinceAttack++;
    }

    @Override
    public void execute() {
        npc.getEntity().a(EnumHand.MAIN_HAND);
        npc.getEntity().B(((CraftLivingEntity) npc.getTarget()).getHandle());
    }

    @Override
    public double getWeight() {
        if (ticksSinceAttack < (20 / (double) npc.getOptions().getSimulatedCPS())) {
            return 0;
        }

        LivingEntity target = npc.getTarget();

        if (target == null) {
            return 0;
        }

        NPCOptions options = npc.getOptions();

        double diff = ThreadLocalRandom.current().nextDouble(options.getHighReachDistance() - options.getLowReachDistance()) + options.getLowReachDistance();

        if (target.getLocation().distanceSquared(npc.getLocation()) < Math.pow(diff, 2)) {
            return 1;
        }

        return 0;
    }
}
