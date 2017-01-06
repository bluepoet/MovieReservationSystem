package net.bluepoet.moviereservation.model;

/**
 * Created by bluepoet on 2017. 1. 6..
 */
public class Reservation {
    private Customer customer;
    private Showing showing;
    private int audienceCount;

    private Money fee;

    public Reservation(Customer customer, Showing showing, int audienceCount) {
        this.customer = customer;
        this.showing = showing;
        this.audienceCount = audienceCount;
        this.fee = showing.calculateFee().times(audienceCount);
    }

    public double getFee() {
        return fee.getAmount();
    }

}
