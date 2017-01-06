package net.bluepoet.moviereservation.model;

import java.util.List;

/**
 * Created by bluepoet on 2017. 1. 6..
 */
public class PercentDiscountStrategy extends DiscountStrategy {
    private double percent = 0.2D;

    public PercentDiscountStrategy() {
        super();
    }

    public PercentDiscountStrategy(List<Rule> rules) {
        super(rules);
    }

    protected Money getDiscountFee(Showing showing) {
        return showing.getFixedFee().times(percent);
    }
}
