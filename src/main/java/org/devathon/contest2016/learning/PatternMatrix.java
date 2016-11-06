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

import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import org.devathon.contest2016.util.SelectUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Cryptkeeper
 * @since 05.11.2016
 */
public class PatternMatrix {

    private final List<Row> byRows = new ArrayList<>();

    private Row currentRow;

    PatternMatrix() {
    }

    public void push(Event event) {
        if (currentRow == null)
            currentRow = new Row();

        currentRow.events.add(event);
    }

    public void end() {
        if (currentRow != null) {
            if (byRows.size() >= 10) {
                byRows.remove(0);
            }

            byRows.add(currentRow);

            currentRow = new Row();
        }
    }

    public Event flipCurrent() {
        Event current = getCurrentAverage();

        if (current != null) {
            switch (current) {
                case ATTACK:
                case THROW_POTION:
                    return Event.CONSUME_GOLDEN_APPLE;
                case CONSUME_GOLDEN_APPLE:
                    return Event.ATTACK;

                default:
                    throw new IllegalArgumentException(current.name());
            }
        } else {
            return null;
        }
    }

    private Event getCurrentAverage() {
        int totalLength = 0;

        for (Row row : byRows) {
            totalLength += row.events.size();
        }

        int averageLength = (int) (totalLength / (double) byRows.size());

        List<TObjectIntMap<Event>> byIndex = new ArrayList<>();

        for (int i = 0; i < averageLength; i++) {
            TObjectIntMap<Event> entry = new TObjectIntHashMap<>();

            for (Row row : byRows) {
                if (i < row.events.size()) {
                    Event event = row.events.get(i);

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

        if (currentRow == null) {
            return null;
        }

        int index = currentRow.events.size();

        if (index < averages.size()) {
            return averages.get(index);
        } else {
            return null;
        }
    }

    public enum Event {

        THROW_POTION,
        CONSUME_GOLDEN_APPLE,
        ATTACK
    }

    private class Row {

        private final List<Event> events = new ArrayList<>();
    }
}
