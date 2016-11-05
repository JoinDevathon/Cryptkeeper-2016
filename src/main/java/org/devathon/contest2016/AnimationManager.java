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
        private int blinkState;

        private State(UUID uuid) {
            this.uuid = uuid;

            // Add out initial Scene, bit of a hack.
            this.scenes.add(new Scene());
        }

        public void deleteScene() {
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

            currentSceneIndex = Math.max(currentSceneIndex + mod, 0);

            if (currentSceneIndex >= scenes.size()) {
                scenes.add(new Scene());
            }
        }

        public void setRecording(boolean recording) {
            isRecording = recording;
        }

        public boolean isRecording() {
            return isRecording;
        }

        public Scene getCurrentScene() {
            return scenes.get(currentSceneIndex);
        }

        public String getMessage() {
            String message = "Current Scene: " +
                    (currentSceneIndex + 1) +
                    "/" +
                    scenes.size();

            if (isRecording) {
                message += " - ";
                message += (blinkState++ % 5 == 0 ? ChatColor.WHITE : ChatColor.RED) + Characters.CIRCLE_STAR + " RECORDING!";
            }

            return message;
        }
    }
}
