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
package org.devathon.contest2016.learning;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.devathon.contest2016.Plugin;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author Cryptkeeper
 * @since 05.11.2016
 */
public class LearnManager {

    private static final LearnManager INSTANCE = new LearnManager();

    public static LearnManager getInstance() {
        return INSTANCE;
    }

    private LearnManager() {
    }

    private final LoadingCache<UUID, PatternMatrix> matrixLookup = CacheBuilder.newBuilder()
            .expireAfterAccess(30, TimeUnit.MINUTES)
            .build(new CacheLoader<UUID, PatternMatrix>() {

                @Override
                public PatternMatrix load(UUID uuid) throws Exception {
                    return new PatternMatrix();
                }
            });

    public void start() {
        Bukkit.getPluginManager().registerEvents(new EventListener(), Plugin.getInstance());
    }

    public PatternMatrix get(UUID uuid) {
        return matrixLookup.getUnchecked(uuid);
    }

    public PatternMatrix get(Player player) {
        return get(player.getUniqueId());
    }
}
