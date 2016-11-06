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
package org.devathon.contest2016.entity;

import net.minecraft.server.v1_10_R1.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_10_R1.CraftWorld;
import org.bukkit.entity.EntityType;
import org.devathon.contest2016.util.EntityUtil;
import org.devathon.contest2016.util.NMSUtil;

/**
 * @author Cryptkeeper
 * @since 05.11.2016
 */
public class FakeZombie extends EntityZombie {

    static {
        EntityUtil.register(EntityType.ZOMBIE, FakeZombie.class);
    }

    public FakeZombie(Location location) {
        super(((CraftWorld) location.getWorld()).getHandle());

        setPositionRotation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        setBaby(false);
        setVillagerType(EnumZombieType.NORMAL);

        world.addEntity(this);
    }

    @Override
    public void r() {
        goalSelector = new PathfinderGoalSelector(NMSUtil.METHOD_PROFILER);

        goalSelector.a(0, new PathfinderGoalFloat(this));
        goalSelector.a(1, new PathfinderGoalHarmlessAttack(this, 1D, false));
    }

    @Override
    public boolean d(MobEffect mobeffect) {
        return true;
    }
}
