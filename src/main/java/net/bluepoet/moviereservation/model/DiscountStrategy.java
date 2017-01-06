package net.bluepoet.moviereservation.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by bluepoet on 2017. 1. 6..
 */
public abstract class DiscountStrategy {
    private List<Rule> rules;

    public DiscountStrategy() {
        this.rules = new ArrayList<>(Arrays.asList(new SequenceRule(), new TimeOfDayRule()));
    }

    public DiscountStrategy(List<Rule> rules) {
        this.rules = rules;
    }

    public Money calculateDiscountFee(Showing showing) {
        for (Rule rule : rules) {
            if (rule.isSatisfiedBy(showing)) {
                return getDiscountFee(showing);
            }
        }

        return Money.ZERO;
    }

    protected abstract Money getDiscountFee(Showing showing);

    public void setRules(List<Rule> rules) {
        this.rules = rules;
    }
}
