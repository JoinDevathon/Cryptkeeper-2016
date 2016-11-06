package org.devathon.contest2016;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.devathon.contest2016.command.TestCommand;
import org.devathon.contest2016.learning.PatternListener;
import org.devathon.contest2016.npc.NPCListener;
import org.devathon.contest2016.npc.NPCRegistry;
import org.devathon.contest2016.util.ItemStackUtil;

import java.util.Arrays;
import java.util.List;

public class Plugin extends JavaPlugin {

    private static Plugin instance;

    private final List<ItemStack> kitItems = Arrays.asList(
            new ItemStack(Material.DIAMOND_SWORD),

            new ItemStack(Material.GOLDEN_APPLE),
            new ItemStack(Material.GOLDEN_APPLE),
            new ItemStack(Material.GOLDEN_APPLE),

            ItemStackUtil.makeSplashPotion(Material.SPLASH_POTION, Arrays.asList(new PotionEffect(PotionEffectType.SLOW, 20 * 10, 1))),
            ItemStackUtil.makeSplashPotion(Material.SPLASH_POTION, Arrays.asList(new PotionEffect(PotionEffectType.WITHER, 20 * 10, 0))),
            ItemStackUtil.makeSplashPotion(Material.SPLASH_POTION, Arrays.asList(new PotionEffect(PotionEffectType.HARM, 20 * 10, 0))),

            new ItemStack(Material.IRON_CHESTPLATE),
            new ItemStack(Material.DIAMOND_HELMET)
    );

    @Override
    public void onEnable() {
        instance = this;

        NPCRegistry.getInstance().start();

        getCommand("test").setExecutor(new TestCommand());

        getServer().getPluginManager().registerEvents(new PatternListener(), this);
        getServer().getPluginManager().registerEvents(new NPCListener(), this);
        getServer().getPluginManager().registerEvents(new GeneralListener(), this);

        getServer().getScheduler().scheduleSyncDelayedTask(Plugin.getInstance(), () -> {
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

    public List<ItemStack> getKitItems() {
        return kitItems;
    }

    public static Plugin getInstance() {
        return instance;
    }
}
