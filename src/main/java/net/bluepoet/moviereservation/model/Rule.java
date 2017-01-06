package net.bluepoet.moviereservation.model;

/**
 * Created by bluepoet on 2017. 1. 6..
 */
public interface Rule {
    boolean isSatisfiedBy(Showing showing);
}
