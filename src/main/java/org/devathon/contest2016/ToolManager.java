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
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Wool;

import java.util.Arrays;
import java.util.List;

/**
 * @author Cryptkeeper
 * @since 05.11.2016
 */
public class ToolManager {

    private static final ToolManager INSTANCE = new ToolManager();

    public static ToolManager getInstance() {
        return INSTANCE;
    }

    private final List<Tool> tools = Arrays.asList(
            new ConsumerTool(ChatColor.GREEN + "Start Recording", new Wool(DyeColor.LIME).toItemStack(1), state -> state.setRecording(true)),
            new ConsumerTool(ChatColor.RED + "Stop Recording", new Wool(DyeColor.RED).toItemStack(1), state -> state.setRecording(false)),
            new ConsumerTool(ChatColor.RED + "< Previous Scene", new Wool(DyeColor.LIGHT_BLUE).toItemStack(1), state -> state.modScene(-1)),
            new ConsumerTool(ChatColor.GREEN + "Next Scene >", new Wool(DyeColor.LIGHT_BLUE).toItemStack(1), state -> state.modScene(1)),
            new ConsumerTool(ChatColor.RED + "X Delete Scene", new Wool(DyeColor.BLACK).toItemStack(1), AnimationManager.State::deleteScene),
            new ConsumerTool(ChatColor.GREEN + "+ Increase Tick Rate", new Wool(DyeColor.LIME).toItemStack(1), state -> state.modTickRate(1)),
            new ConsumerTool(ChatColor.RED + "- Decrease Tick Rate", new Wool(DyeColor.RED).toItemStack(1), state -> state.modTickRate(-1)),
            new ConsumerTool(ChatColor.GREEN + "Start Playing", new ItemStack(Material.STICK), AnimationManager.State::start),
            new ConsumerTool(ChatColor.RED + "Stop Playing", new ItemStack(Material.STICK), AnimationManager.State::stop)
    );

    private ToolManager() {
    }

    public List<Tool> getTools() {
        return tools;
    }

    public Tool getByDisplayName(String displayName) {
        for (Tool tool : tools) {
            if (tool.getDisplayName().equals(displayName)) {
                return tool;
            }
        }

        return null;
    }
}
