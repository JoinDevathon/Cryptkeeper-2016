package org.devathon.contest2016;

import org.bukkit.plugin.java.JavaPlugin;
import org.devathon.contest2016.command.TestCommand;
import org.devathon.contest2016.npc.NPCListener;
import org.devathon.contest2016.npc.NPCRegistry;

public class DevathonPlugin extends JavaPlugin {

    private static DevathonPlugin instance;

    private final NPCRegistry registry = new NPCRegistry();

    @Override
    public void onEnable() {
        instance = this;

        registry.start();

        getCommand("test").setExecutor(new TestCommand());

        getServer().getPluginManager().registerEvents(new NPCListener(), this);
        getServer().getPluginManager().registerEvents(new WorldListener(), this);
    }

    public NPCRegistry getNPCRegistry() {
        return registry;
    }

    public static DevathonPlugin getInstance() {
        return instance;
    }
}
