package net.bluepoet.junit;

import static org.assertj.core.api.Assertions.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by bluepoet on 2017. 1. 5..
 */
public class MovieReservationServiceTest {
    private Movie movie;
    private Showing showing;
    private Customer customer;

    @Before
    public void setUp() throws Exception {
        movie = new Movie(new Money(10000), new NonDiscountStrategy());
        showing = new Showing();
        showing.setMovie(movie);
        customer = new Customer();
    }

    @Test
    public void calculateNoDiscountMovieFee() throws Exception {
        // Given
        int audienceCount = 1;

        // When
        Reservation reservation = showing.reserve(customer, audienceCount);

        // Then
        assertThat(reservation.getFee()).isEqualTo(10000);
    }

    @Test
    public void calculateNoDiscountMovieFee_manyAudience() throws Exception {
        // Given
        int audienceCount = 5;

        // When
        Reservation reservation = showing.reserve(customer, audienceCount);

        // Then
        assertThat(reservation.getFee()).isEqualTo(50000);
    }
}

class Movie {
    private Money fee;
    private DiscountStrategy discountStrategy;

    public Movie() {
    }

    public Movie(Money fee, DiscountStrategy discountStrategy) {
        this.fee = fee;
        this.discountStrategy = discountStrategy;
    }

    public void setDiscountStrategy(DiscountStrategy discountStrategy) {
        this.discountStrategy = discountStrategy;
    }

    public Money calculateFee(Showing showing) {
        return fee.minus(discountStrategy.calculateDiscountFee(showing));
    }
}

abstract class DiscountStrategy {

    protected Money calculateDiscountFee(Showing showing) {
        return null;
    }
}

class NonDiscountStrategy extends DiscountStrategy {
    protected Money calculateDiscountFee(Showing showing) {
        return Money.ZERO;
    }
}

class Showing {
    private Movie movie;

    public Reservation reserve(Customer customer, int audienceCount) {
        return new Reservation(customer, this, audienceCount);
    }

    public Money calculateFee() {
        return movie.calculateFee(this);
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }
}

class Customer {

}

class Reservation {
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

    public int getFee() {
        return fee.getAmount();
    }

}

class Money {
    public static Money ZERO = new Money(0);
    private int amount;

    public Money(int amount) {
        this.amount = amount;
    }

    public Money times(int audienceCount) {
        return new Money(this.getAmount() * audienceCount);
    }

    public Money minus(Money money) {
        return new Money(this.getAmount() - money.getAmount());
    }

    public int getAmount() {
        return amount;
    }
}
