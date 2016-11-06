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

    public static NPCOptions create() {
        return new NPCOptions();
    }

    private int minCPS = 2;
    private int maxCPS = 8;
    private int potionThrowDelay = 60;
    private double lowReachDistance = 1.5;
    private double highReachDistance = 3;
    private double sprintDistance = 5;
    private double walkSpeed = 2.75;
    private double sprintSpeed = 3.75;
    private int consumeItemDelay = 100;

    private NPCOptions() {
    }

    public int getConsumeItemDelay() {
        return consumeItemDelay;
    }

    public double getSprintDistance() {
        return sprintDistance;
    }

    public int getPotionThrowDelay() {
        return potionThrowDelay;
    }

    public int getMinCPS() {
        return minCPS;
    }

    public int getMaxCPS() {
        return maxCPS;
    }

    public double getLowReachDistance() {
        return lowReachDistance;
    }

    public double getHighReachDistance() {
        return highReachDistance;
    }

    public double getWalkSpeed() {
        return walkSpeed;
    }

    public double getSprintSpeed() {
        return sprintSpeed;
    }
}
