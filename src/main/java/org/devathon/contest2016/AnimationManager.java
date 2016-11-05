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
package org.devathon.contest2016;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * @author Cryptkeeper
 * @since 05.11.2016
 */
public class AnimationManager {

    private static final AnimationManager INSTANCE = new AnimationManager();

    public static AnimationManager getInstance() {
        return INSTANCE;
    }

    private final Map<UUID, State> states = new HashMap<>();

    private AnimationManager() {
    }

    public State get(UUID uuid) {
        return states.computeIfAbsent(uuid, State::new);
    }

    public State get(Player player) {
        return get(player.getUniqueId());
    }

    public class State {

        private final List<Scene> scenes = new ArrayList<>();

        private final UUID uuid;

        private int currentSceneIndex;
        private boolean isRecording;
        private long tickRate = 5L;

        private int blinkState;

        private int runningTaskId = -1;
        private int runningSceneId;

        private State(UUID uuid) {
            this.uuid = uuid;

            // Add out initial Scene, bit of a hack.
            this.scenes.add(new Scene());
        }

        public void start() {
            if (!isPlaying()) {
                runningTaskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(DevathonPlugin.getInstance(), () -> {
                    System.out.println("runningSceneId = " + runningSceneId);

                    runningSceneId += 1;

                    if (runningSceneId >= scenes.size()) {
                        System.out.println("reset " + runningSceneId);

                        stop();

                        return;
                    }

                    /*
                    if (runningSceneId > 0) {
                        Scene lastScene = scenes.get(runningSceneId - 1);

                        //lastScene.getChanges().forEach(Change::apply);
                    }
                     */

                    Scene scene = scenes.get(runningSceneId);

                    scene.getChanges().forEach(Change::apply);
                }, tickRate / 2, tickRate);
            }
        }

        public void stop() {
            if (isPlaying()) {
                Bukkit.getScheduler().cancelTask(runningTaskId);

                runningTaskId = -1;

                if (runningSceneId < scenes.size()) {
                    Scene currentScene = scenes.get(runningSceneId);

                    currentScene.getChanges().forEach(Change::revert);
                }

                runningSceneId = 0; //
            }
        }

        public void deleteScene() {
            if (isPlaying()) {
                return;
            }

            if (scenes.size() > 1) {
                scenes.remove(currentSceneIndex);

                currentSceneIndex = Math.max(currentSceneIndex--, 0);

                // TODO: Fix delete on highs
            }
        }

        public void modScene(int mod) {
            if (mod < -1 || mod > 1 || mod == 0) {
                throw new IllegalArgumentException("mod can only be -1, 1");
            }

            if (isPlaying()) {
                return;
            }

            currentSceneIndex = Math.max(currentSceneIndex + mod, 0);

            if (currentSceneIndex >= scenes.size()) {
                scenes.add(new Scene());
            }
        }

        public void modTickRate(long mod) {
            if (!isPlaying()) {
                tickRate = Math.max(tickRate += mod, 1L);
                tickRate = Math.min(tickRate, 20L);
            }
        }

        public void setRecording(boolean recording) {
            isRecording = recording;
        }

        public long getTickRate() {
            return tickRate;
        }

        public boolean isRecording() {
            return isRecording;
        }

        public boolean isPlaying() {
            return runningTaskId != -1;
        }

        public Scene getCurrentScene() {
            return scenes.get(currentSceneIndex);
        }

        public String getMessage() {
            String message = "";

            message += "Current Scene: " + (currentSceneIndex + 1) + "/" + scenes.size();
            message += " - ";
            message += "Tick Rate: " + tickRate + "/20";

            if (isRecording) {
                message += " - ";
                message += (blinkState++ % 5 == 0 ? ChatColor.WHITE : ChatColor.RED) + Characters.CIRCLE_STAR + " RECORDING!";
            }

            return message;
        }
    }
}
