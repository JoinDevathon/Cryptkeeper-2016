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

import net.minecraft.server.v1_10_R1.EntityLiving;
import net.minecraft.server.v1_10_R1.EntityZombie;
import net.minecraft.server.v1_10_R1.EnumHand;
import net.minecraft.server.v1_10_R1.PathfinderGoalMeleeAttack;

/**
 * @author Cryptkeeper
 * @since 05.11.2016
 */
public class PathfinderTest extends PathfinderGoalMeleeAttack {

    private final EntityZombie h;
    private int i;

    public PathfinderTest(EntityZombie var1, double var2, boolean var4) {
        super(var1, var2, var4);
        this.h = var1;
    }

    public void c() {
        super.c();
        this.i = 0;
    }

    public void d() {
        super.d();
        this.h.a(false);

    }

    public void e() {
        super.e();
        ++this.i;
        if(this.i >= 5 && this.c < 10) {
            this.h.a(true);
        } else {
            this.h.a(false);
        }

    }

    @Override
    protected void a(EntityLiving var1, double var2) {
        double var4 = this.a(var1);
        if(var2 <= var4 && this.c <= 0) {
            this.c = 20;
            this.b.a(EnumHand.MAIN_HAND);
            this.b.B(var1);
        }

    }
}
