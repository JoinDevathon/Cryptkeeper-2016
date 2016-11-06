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

    private String displayName = "Dummy";
    private int simulatedCPS = 5;
    private int potionThrowDelay = 15;
    private double attackDamage = 3;
    private double lowReachDistance = 1.5;
    private double highReachDistance = 3;
    private double sprintDistance = 5;

    private NPCOptions() {
    }

    public NPCOptions lowReachDistance(double lowReachDistance) {
        this.lowReachDistance = lowReachDistance;

        return this;
    }

    public NPCOptions highReachDistance(double highReachDistance) {
        this.highReachDistance = highReachDistance;

        return this;
    }

    public NPCOptions attackDamage(double attackDamage) {
        this.attackDamage = attackDamage;

        return this;
    }

    public NPCOptions displayName(String displayName) {
        this.displayName = displayName;

        return this;
    }

    public NPCOptions simulatedCPS(int simulatedCPS) {
        this.simulatedCPS = simulatedCPS;

        return this;
    }

    public NPCOptions potionThrowDelay(int potionThrowDelay) {
        this.potionThrowDelay = potionThrowDelay;

        return this;
    }

    public NPCOptions sprintDistance(double sprintDistance) {
        this.sprintDistance = sprintDistance;

        return this;
    }

    public double getSprintDistance() {
        return sprintDistance;
    }

    public double getAttackDamage() {
        return attackDamage;
    }

    public int getPotionThrowDelay() {
        return potionThrowDelay;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getSimulatedCPS() {
        return simulatedCPS;
    }

    public double getLowReachDistance() {
        return lowReachDistance;
    }

    public double getHighReachDistance() {
        return highReachDistance;
    }
}
