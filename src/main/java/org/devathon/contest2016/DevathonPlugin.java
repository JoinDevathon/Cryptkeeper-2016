package org.devathon.contest2016;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
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
        getServer().getPluginManager().registerEvents(new GeneralListener(), this);

        getServer().getScheduler().scheduleSyncDelayedTask(DevathonPlugin.getInstance(), () -> {
            World world = Bukkit.getWorlds().get(0);

            int size = 50;

            for (int x = 0; x < size; x++) {
                for (int z = 0; z < size; z++) {
                    int maxHeight = (x == 0 || z == 0 || x == size - 1 || z == size - 1) ? 5 : 1;

                    for (int y = 0; y < maxHeight; y++) {
                        Location location = new Location(world, x, 200 + y, z);

                        location.getBlock().setType(maxHeight > 1 ? Material.STONE : Material.GRASS);
                    }
                }
            }

            world.setSpawnLocation(size / 2, 205, size / 2);
        });
    }

    public NPCRegistry getNPCRegistry() {
        return registry;
    }

    public static DevathonPlugin getInstance() {
        return instance;
    }
}
