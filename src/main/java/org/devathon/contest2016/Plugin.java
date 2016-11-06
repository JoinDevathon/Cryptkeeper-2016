package org.devathon.contest2016;

import org.bukkit.plugin.java.JavaPlugin;
import org.devathon.contest2016.learning.PatternListener;
import org.devathon.contest2016.npc.NPCListener;
import org.devathon.contest2016.npc.NPCRegistry;

public class Plugin extends JavaPlugin {

    private static Plugin instance;

    @Override
    public void onEnable() {
        instance = this;

        NPCRegistry.getInstance().start();

        getServer().getPluginManager().registerEvents(new PatternListener(), this);
        getServer().getPluginManager().registerEvents(new NPCListener(), this);
        getServer().getPluginManager().registerEvents(new GeneralListener(), this);
    }

    public static Plugin getInstance() {
        return instance;
    }
}
