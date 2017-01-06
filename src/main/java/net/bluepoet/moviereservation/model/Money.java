package net.bluepoet.moviereservation.model;

/**
 * Created by bluepoet on 2017. 1. 6..
 */
public class Money {
    public static Money ZERO = new Money(0);
    private double amount;

    public Money(double amount) {
        this.amount = amount;
    }

    public Money times(int audienceCount) {
        return new Money(this.getAmount() * audienceCount);
    }

    public Money times(double percent) {
        return new Money(this.getAmount() * percent);
    }

    public Money minus(Money money) {
        return new Money(this.getAmount() - money.getAmount());
    }

    public double getAmount() {
        return amount;
    }
}
