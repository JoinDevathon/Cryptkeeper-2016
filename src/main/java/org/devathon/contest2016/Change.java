package org.devathon.contest2016;

/**
 * @author Cryptkeeper
 * @since 05.11.2016
 */
public interface Change {

    void apply();

    void revert();
}
