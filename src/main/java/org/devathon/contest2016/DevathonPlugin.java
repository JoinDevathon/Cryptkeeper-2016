package org.devathon.contest2016;

import org.bukkit.plugin.java.JavaPlugin;

public class DevathonPlugin extends JavaPlugin {

    private static DevathonPlugin instance;

    @Override
    public void onEnable() {
        instance = this;
    }

    public static DevathonPlugin getInstance() {
        return instance;
    }
}
