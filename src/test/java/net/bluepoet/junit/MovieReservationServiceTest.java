package net.bluepoet.junit;

import net.bluepoet.moviereservation.model.*;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by bluepoet on 2017. 1. 5..
 */
public class MovieReservationServiceTest {
    public static final int DEFAULT_AUDIENCE_COUNT = 1;

    private Movie movie;
    private Showing showing;
    private Customer customer;

    @Before
    public void setUp() throws Exception {
        movie = new Movie(new Money(10000));
        showing = new Showing();
        showing.setMovie(movie);
        customer = new Customer();
    }

    @Test
    public void calculateNoRulesMovieFee() throws Exception {
        // Given
        movie.setDiscountStrategy(new NonDiscountStrategy(Lists.emptyList()));

        // When
        Reservation reservation = showing.reserve(customer, DEFAULT_AUDIENCE_COUNT);

        // Then
        assertThat(reservation.getFee()).isEqualTo(10000);
    }

    @Test
    public void calculateNonDiscountMovieFee() throws Exception {
        // Given
        showing.setSequence(1);
        showing.createShowingTime(DayOfWeek.MONDAY, "09:01", "11:01");
        movie.setDiscountStrategy(new NonDiscountStrategy());

        // When
        Reservation reservation = showing.reserve(customer, DEFAULT_AUDIENCE_COUNT);

        // Then
        assertThat(reservation.getFee()).isEqualTo(10000);
    }

    @Test
    public void calculateAmountDiscountNotMatchSequenceRuleMovieFee() throws Exception {
        // Given
        showing.setSequence(2);
        showing.createShowingTime(DayOfWeek.MONDAY, "09:01", "11:01");
        movie.setDiscountStrategy(new AmountDiscountStrategy());

        // When
        Reservation reservation = showing.reserve(customer, DEFAULT_AUDIENCE_COUNT);

        // Then
        assertThat(reservation.getFee()).isEqualTo(10000);
    }

    @Test
    public void calculateAmountDiscountMatchSequenceRuleMovieFee() throws Exception {
        // Given
        showing.setSequence(1);
        showing.createShowingTime(DayOfWeek.MONDAY, "09:01", "11:01");
        movie.setDiscountStrategy(new AmountDiscountStrategy());

        // When
        Reservation reservation = showing.reserve(customer, DEFAULT_AUDIENCE_COUNT);

        // Then
        assertThat(reservation.getFee()).isEqualTo(7000);
    }

    @Test
    public void calculateAmountDiscountMatchSequenceRuleMovieFeeByManyAudienceCount() throws Exception {
        // Given
        showing.setSequence(1);
        showing.createShowingTime(DayOfWeek.MONDAY, "09:01", "11:01");
        movie.setDiscountStrategy(new AmountDiscountStrategy());
        int audienceCount = 5;

        // When
        Reservation reservation = showing.reserve(customer, audienceCount);

        // Then
        assertThat(reservation.getFee()).isEqualTo(35000);
    }

    @Test
    public void calculateAmountDiscountNotMatchDayTimeOfDayRuleMovieFee() throws Exception {
        // Given
        givenAmountDiscountStrategyAndTimeOfRule(DayOfWeek.FRIDAY, "09:00", "11:40");

        // When
        Reservation reservation = showing.reserve(customer, DEFAULT_AUDIENCE_COUNT);

        // Then
        assertThat(reservation.getFee()).isEqualTo(10000);
    }

    @Test
    public void calculateAmountDiscountNotMatchTimeDayTimeOfDayRuleMovieFee() throws Exception {
        // Given
        givenAmountDiscountStrategyAndTimeOfRule(DayOfWeek.SUNDAY, "09:00", "11:40");

        // When
        Reservation reservation = showing.reserve(customer, DEFAULT_AUDIENCE_COUNT);

        // Then
        assertThat(reservation.getFee()).isEqualTo(10000);
    }

    @Test
    public void calculateAmountDiscountMatchTimeDayTimeOfDayRuleMovieFee() throws Exception {
        // Given
        givenAmountDiscountStrategyAndTimeOfRule(DayOfWeek.SUNDAY, "09:01", "11:29");

        // When
        Reservation reservation = showing.reserve(customer, DEFAULT_AUDIENCE_COUNT);

        // Then
        assertThat(reservation.getFee()).isEqualTo(7000);
    }

    @Test
    public void calculateAmountDiscountMatchTimeDayTimeOfDayRuleMovieFeeByManyAudienceCount() throws Exception {
        // Given
        givenAmountDiscountStrategyAndTimeOfRule(DayOfWeek.SUNDAY, "09:01", "11:29");
        int audienceCount = 5;

        // When
        Reservation reservation = showing.reserve(customer, audienceCount);

        // Then
        assertThat(reservation.getFee()).isEqualTo(35000);
    }

    @Test
    public void calculatePercentDiscountSequenceRuleMovieFee() throws Exception {
        // Given
        showing.setSequence(1);
        showing.createShowingTime(DayOfWeek.SUNDAY, "09:01", "11:00");
        movie.setDiscountStrategy(new PercentDiscountStrategy());

        // When
        Reservation reservation = showing.reserve(customer, DEFAULT_AUDIENCE_COUNT);

        // Then
        assertThat(reservation.getFee()).isEqualTo(8000);
    }

    @Test
    public void calculatePercentDiscountTimeOfDayRuleMovieFee() throws Exception {
        // Given
        showing.setSequence(2);
        showing.createShowingTime(DayOfWeek.SUNDAY, "09:01", "11:00");
        movie.setDiscountStrategy(new PercentDiscountStrategy());

        // When
        Reservation reservation = showing.reserve(customer, DEFAULT_AUDIENCE_COUNT);

        // Then
        assertThat(reservation.getFee()).isEqualTo(8000);
    }

    @Test
    public void calculateOverrapedDiscountSequenceRuleMovieFee() throws Exception {
        // Given
        showing.setSequence(1);
        showing.createShowingTime(DayOfWeek.TUESDAY, "09:01", "11:00");
        movie.setDiscountStrategy(createOverlappedDiscountStrategy());

        // When
        Reservation reservation = showing.reserve(customer, DEFAULT_AUDIENCE_COUNT);

        // Then
        assertThat(reservation.getFee()).isEqualTo(5000);
    }

    @Test
    public void calculateOverrapedDiscountTimeOfDayRuleMovieFee() throws Exception {
        // Given
        showing.setSequence(2);
        showing.createShowingTime(DayOfWeek.SUNDAY, "09:01", "11:01");
        movie.setDiscountStrategy(createOverlappedDiscountStrategy());

        // When
        Reservation reservation = showing.reserve(customer, DEFAULT_AUDIENCE_COUNT);

        // Then
        assertThat(reservation.getFee()).isEqualTo(5000);
    }

    @Test
    public void calculateOverrapedDiscountTimeOfDayRuleMovieFeeByManyAudienceCount() throws Exception {
        // Given
        showing.setSequence(2);
        showing.createShowingTime(DayOfWeek.SUNDAY, "09:01", "11:01");
        movie.setDiscountStrategy(createOverlappedDiscountStrategy());
        int defaultAudienceCount = 4;

        // When
        Reservation reservation = showing.reserve(customer, defaultAudienceCount);

        // Then
        assertThat(reservation.getFee()).isEqualTo(20000);
    }

    private OverlappedDiscountStrategy createOverlappedDiscountStrategy() {
        OverlappedDiscountStrategy overlappedDiscountStrategy = new OverlappedDiscountStrategy();
        overlappedDiscountStrategy.setStrategies(createStrategies());
        return overlappedDiscountStrategy;
    }

    private List<DiscountStrategy> createStrategies() {
        return new ArrayList<>(Arrays.asList(new AmountDiscountStrategy(), new PercentDiscountStrategy()));
    }

    private void givenAmountDiscountStrategyAndTimeOfRule(DayOfWeek dayOfWeek, String startTime, String endTime) {
        showing.setSequence(2);
        showing.createShowingTime(dayOfWeek, startTime, endTime);
        movie.setDiscountStrategy(new AmountDiscountStrategy());
    }
}

