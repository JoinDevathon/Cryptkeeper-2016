package org.devathon.contest2016;

import java.util.Collection;

/**
 * @author Cryptkeeper
 * @since 05.11.2016
 */
public interface Selector<T> {

    T select();

    Collection<T> options();
}
