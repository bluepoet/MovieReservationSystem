package net.bluepoet.moviereservation.model;

import java.util.List;

/**
 * Created by bluepoet on 2017. 1. 6..
 */
public class NonDiscountStrategy extends DiscountStrategy {
    public NonDiscountStrategy() {
        super();
    }

    public NonDiscountStrategy(List<Rule> rules) {
        super(rules);
    }

    protected Money getDiscountFee(Showing showing) {
        return Money.ZERO;
    }
}
