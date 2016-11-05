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

import org.bukkit.entity.LivingEntity;
import org.devathon.contest2016.entity.npc.NPC;

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
        System.out.println("npc = " + npc);
    }

    @Override
    public double getWeight() {
        if (ticksSinceAttack < 3) { // 7 CPS = 20/7 = ~2.8
            //return 0;
        }

        LivingEntity target = npc.getTarget();

        if (target == null) {
            return 0;
        }

        if (target.getLocation().distanceSquared(npc.getLocation()) < Math.pow(1.5, 2)) {
            return Double.MAX_VALUE;
        }

        return 0;
    }
}
