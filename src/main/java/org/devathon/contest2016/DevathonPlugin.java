package org.devathon.contest2016;

import org.bukkit.plugin.java.JavaPlugin;

public class DevathonPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new GenericListener(), this);

        getServer().getScheduler().scheduleSyncRepeatingTask(this, new MessageTask(), 2L, 2L);
    }
}
