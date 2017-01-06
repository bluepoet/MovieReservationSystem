package net.bluepoet.moviereservation.model;

/**
 * Created by bluepoet on 2017. 1. 6..
 */
public class SequenceRule implements Rule {
    private int sequence = 1;

    public boolean isSatisfiedBy(Showing showing) {
        return showing.isSequence(sequence);
    }
}
