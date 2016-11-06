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

/**
 * @author Cryptkeeper
 * @since 05.11.2016
 */
public class NPCOptions {

    public static final int MIN_CPS = 2;
    public static final int MAX_CPS = 8;

    public static final int POTION_THROW_DELAY = 60;

    public static final double LOW_REACH_DISTANCE = 1.5;
    public static final double HIGH_REACH_DISTANCE = 3;

    public static final double SPRINT_DISTANCE = 5;

    public static final double WALK_SPEED = 2.75;
    public static final double SPRINT_SPEED = 3.75;

    public static final int CONSUME_ITEM_DELAY = 100;

    public static final double JUMP_CHANCE = 0.75;

    private NPCOptions() {
    }
}
