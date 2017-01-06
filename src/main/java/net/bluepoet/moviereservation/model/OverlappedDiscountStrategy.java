package net.bluepoet.moviereservation.model;

import java.util.List;

/**
 * Created by bluepoet on 2017. 1. 6..
 */
public class OverlappedDiscountStrategy extends DiscountStrategy {
    List<DiscountStrategy> strategies;

    public OverlappedDiscountStrategy() {
        super();
    }

    public void setStrategies(List<DiscountStrategy> strategies) {
        this.strategies = strategies;
    }

    public OverlappedDiscountStrategy(List<Rule> rules) {
        super(rules);
    }

    @Override
    protected Money getDiscountFee(Showing showing) {
        double discountAmount = 0.0D;

        for (DiscountStrategy strategy : strategies) {
            discountAmount += strategy.getDiscountFee(showing).getAmount();
        }

        return new Money(discountAmount);
    }
}
