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
package org.devathon.contest2016;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * @author Cryptkeeper
 * @since 05.11.2016
 */
public class MaterialSelector implements Selector<Material> {

    private final List<Pair> pairs = new ArrayList<>();

    public MaterialSelector pair(Material material, double weight) {
        pairs.add(new Pair(material, weight));

        return this;
    }

    @Override
    public Material select() {
        // TODO: Weighted random
        int index = ThreadLocalRandom.current().nextInt(pairs.size());

        return pairs.get(index).material;
    }

    @Override
    public Collection<Material> options() {
        return pairs.stream().map(pair -> pair.material).collect(Collectors.toList());
    }

    private class Pair {

        private final Material material;
        private final double weight;

        Pair(Material material, double weight) {
            this.material = material;
            this.weight = weight;
        }
    }
}
