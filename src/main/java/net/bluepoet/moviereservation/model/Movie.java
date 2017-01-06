package net.bluepoet.moviereservation.model;

/**
 * Created by bluepoet on 2017. 1. 6..
 */
public class Movie {
    private Money fee;
    private DiscountStrategy discountStrategy;

    public Movie() {
    }

    public Movie(Money fee) {
        this.fee = fee;
    }

    public void setDiscountStrategy(DiscountStrategy discountStrategy) {
        this.discountStrategy = discountStrategy;
    }

    public Money calculateFee(Showing showing) {
        return fee.minus(discountStrategy.calculateDiscountFee(showing));
    }

    public Money getFee() {
        return fee;
    }
}
