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
import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import org.devathon.contest2016.util.SelectUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * @author Cryptkeeper
 * @since 05.11.2016
 */
public class PatternMatrix {

    private static final LoadingCache<UUID, PatternMatrix> MATRIX_MAP = CacheBuilder.newBuilder()
            .expireAfterAccess(30, TimeUnit.MINUTES)
            .build(new CacheLoader<UUID, PatternMatrix>() {

                @Override
                public PatternMatrix load(UUID uuid) throws Exception {
                    return new PatternMatrix();
                }
            });

    public static PatternMatrix of(UUID uuid) {
        return MATRIX_MAP.getUnchecked(uuid);
    }

    private final List<List<Event>> byRows = new ArrayList<>();
    private final List<Event> currentRow = new ArrayList<>();

    private PatternMatrix() {
    }

    public void push(Event event) {
        currentRow.add(event);
    }

    public void end() {
        if (currentRow.size() > 0) {
            if (byRows.size() >= 10) {
                byRows.remove(0);
            }

            byRows.add(new ArrayList<>(currentRow));

            currentRow.clear();
        }
    }

    public Event getExpectedEvent() {
        int totalLength = 0;

        for (List<Event> row : byRows) {
            totalLength += row.size();
        }

        int averageLength = (int) (totalLength / (double) byRows.size());

        List<TObjectIntMap<Event>> byIndex = new ArrayList<>();

        for (int i = 0; i < averageLength; i++) {
            TObjectIntMap<Event> entry = new TObjectIntHashMap<>();

            for (List<Event> row : byRows) {
                if (i < row.size()) {
                    Event event = row.get(i);

                    if (!entry.increment(event)) {
                        entry.put(event, 1);
                    }
                }
            }

            byIndex.add(entry);
        }

        List<Event> averages = new ArrayList<>();

        for (TObjectIntMap<Event> entry : byIndex) {
            Event event = SelectUtil.select(entry);

            averages.add(event);
        }

        if (currentRow.size() == 0) {
            return null;
        }

        int index = currentRow.size();

        if (index < averages.size()) {
            return averages.get(index);
        }

        return null;
    }

    public enum Event {

        THROW_POTION,
        CONSUME_GOLDEN_APPLE,
        ATTACK,
        USE_FIREBALL;

        public Event flip() {
            ThreadLocalRandom random = ThreadLocalRandom.current();

            switch (this) {
                case ATTACK: {
                    return random.nextBoolean() ? THROW_POTION : USE_FIREBALL;
                }

                case THROW_POTION:
                    return CONSUME_GOLDEN_APPLE;

                case CONSUME_GOLDEN_APPLE: {
                    return random.nextBoolean() ? ATTACK : THROW_POTION;
                }

                default:
                    throw new IllegalArgumentException(name());
            }
        }
    }
}
