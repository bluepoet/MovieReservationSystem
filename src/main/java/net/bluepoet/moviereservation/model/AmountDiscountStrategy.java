package net.bluepoet.moviereservation.model;

import java.util.List;

/**
 * Created by bluepoet on 2017. 1. 6..
 */
public class AmountDiscountStrategy extends DiscountStrategy {
    public AmountDiscountStrategy() {
        super();
    }

    public AmountDiscountStrategy(List<Rule> rules) {
        super(rules);
    }

    protected Money getDiscountFee(Showing showing) {
        return new Money(3000);
    }
}
