package net.bluepoet.junit;

import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by bluepoet on 2017. 1. 5..
 */
public class MovieReservationServiceTest {
    private Movie movie;
    private Showing showing;
    private Customer customer;
    private List<Rule> rules;

    @Before
    public void setUp() throws Exception {
        movie = new Movie(new Money(10000));
        showing = new Showing();
        showing.setMovie(movie);
        customer = new Customer();
        rules = createRules();
    }

    private List<Rule> createRules() {
        return Lists.emptyList();
    }

    @Test
    public void calculateNoDiscountMovieFee() throws Exception {
        // Given
        movie.setDiscountStrategy(new NonDiscountStrategy(rules));
        int audienceCount = 1;

        // When
        Reservation reservation = showing.reserve(customer, audienceCount);

        // Then
        assertThat(reservation.getFee()).isEqualTo(10000);
    }

    @Test
    public void calculateNoDiscountMovieFee_manyAudience() throws Exception {
        // Given
        movie.setDiscountStrategy(new NonDiscountStrategy(rules));
        int audienceCount = 5;

        // When
        Reservation reservation = showing.reserve(customer, audienceCount);

        // Then
        assertThat(reservation.getFee()).isEqualTo(50000);
    }

    @Test
    public void calculateAmountDiscountMovieFee() throws Exception {
        // Given
        movie.setDiscountStrategy(new AmountDiscountStrategy(rules));
        int audienceCount = 1;

        // When
        Reservation reservation = showing.reserve(customer, audienceCount);

        // Then
        assertThat(reservation.getFee()).isEqualTo(7000);
    }
}

class Movie {
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

abstract class DiscountStrategy {
    private List<Rule> rules;

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

interface Rule {
    boolean isSatisfiedBy(Showing showing);
}

class SequenceRule implements Rule {

    public boolean isSatisfiedBy(Showing showing) {
        return false;
    }
}

class TimeOfDayRule implements Rule {
    public boolean isSatisfiedBy(Showing showing) {
        return false;
    }
}

class NonDiscountStrategy extends DiscountStrategy {
    public NonDiscountStrategy(List<Rule> rules) {
        super(rules);
    }

    protected Money getDiscountFee(Showing showing) {
        return Money.ZERO;
    }
}

class AmountDiscountStrategy extends DiscountStrategy {
    public AmountDiscountStrategy(List<Rule> rules) {
        super(rules);
    }

    protected Money getDiscountFee(Showing showing) {
        return new Money(3000);
    }
}

class PercentDiscountStrategy extends DiscountStrategy {
    private double percent = 0.2D;

    public PercentDiscountStrategy(List<Rule> rules) {
        super(rules);
    }

    protected Money getDiscountFee(Showing showing) {
        return showing.getFixedFee().times(percent);
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

    public Money getFixedFee() {
        return movie.getFee();
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

    public double getFee() {
        return fee.getAmount();
    }

}

class Money {
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
