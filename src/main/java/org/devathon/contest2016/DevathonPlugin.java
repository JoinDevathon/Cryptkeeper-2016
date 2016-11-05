package org.devathon.contest2016;

import org.bukkit.plugin.java.JavaPlugin;

public class DevathonPlugin extends JavaPlugin {

    private static DevathonPlugin instance;

    @Override
    public void onEnable() {
        instance = this;

        getServer().getPluginManager().registerEvents(new GenericListener(), this);
        getServer().getPluginManager().registerEvents(new AnimationListener(), this);

        getServer().getScheduler().scheduleSyncRepeatingTask(this, new MessageTask(), 2L, 2L);
    }

    public static DevathonPlugin getInstance() {
        return instance;
    }
}
