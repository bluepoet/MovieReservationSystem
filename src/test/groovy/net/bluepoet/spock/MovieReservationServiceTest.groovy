package net.bluepoet.spock

import net.bluepoet.moviereservation.model.*
import org.assertj.core.util.Lists
import spock.lang.Specification

import java.time.DayOfWeek

/**
 * Created by bluepoet on 2017. 1. 7..
 */
class MovieReservationServiceTest extends Specification {
    static final DEFAULT_AUDIENCE_COUNT = 1

    Movie movie
    Showing showing
    Customer customer

    void setup() {
        movie = new Movie(new Money(10000))
        showing = new Showing()
        showing.setMovie(movie)
        customer = new Customer()
    }

    def "할인룰이 아무것도 없을 때, 영화의 상영가격을 계산해 확인한다."() {
        given:
        movie.setDiscountStrategy(new NonDiscountStrategy(Lists.emptyList()))

        when:
        def reservation = showing.reserve(customer, DEFAULT_AUDIENCE_COUNT)

        then:
        reservation.getFee() == 10000
    }

    def "시퀀스룰이 적용되었지만 할인전략이 없는 영화의 상영가격을 계산해 확인한다."() {
        given:
        showing.setSequence(1)
        setNotMatchTimeRuleShowingTime()
        movie.setDiscountStrategy(new NonDiscountStrategy())

        when:
        def reservation = showing.reserve(customer, DEFAULT_AUDIENCE_COUNT)

        then:
        reservation.getFee() == 10000
    }

    def "시퀀스가 맞지 않아 할인이 되지 않았을 때, 영화의 상영가격을 계산해 확인한다."() {
        given:
        showing.setSequence(2)
        setNotMatchTimeRuleShowingTime()
        movie.setDiscountStrategy(new AmountDiscountStrategy())

        when:
        def reservation = showing.reserve(customer, DEFAULT_AUDIENCE_COUNT)

        then:
        reservation.getFee() == 10000
    }

    def "시퀀스룰 1회차가 상영정보와 맞아 가격할인전략이 적용되었을 때, 영화의 상영가격을 계산해 확인한다."() {
        given:
        showing.setSequence(1)
        setNotMatchTimeRuleShowingTime()
        movie.setDiscountStrategy(new AmountDiscountStrategy())

        when:
        def reservation = showing.reserve(customer, DEFAULT_AUDIENCE_COUNT)

        then:
        reservation.getFee() == 7000
    }

    def "시퀀스룰이 맞아 가격할인전략이 적용된 5명의 영화 상영가격을 계산해 확인한다."() {
        showing.setSequence(1)
        setNotMatchTimeRuleShowingTime()
        movie.setDiscountStrategy(new AmountDiscountStrategy())
        def audienceCount = 5

        when:
        def reservation = showing.reserve(customer, audienceCount)

        then:
        reservation.getFee() == 35000
    }

    def "타임룰의 요일이 맞지 않아 할인이 되지 않았을 때, 영화 상영가격을 계산해 확인한다."() {
        given:
        givenAmountDiscountStrategyAndTimeOfRule(DayOfWeek.FRIDAY, "09:00", "11:40")

        when:
        def reservation = showing.reserve(customer, DEFAULT_AUDIENCE_COUNT)

        then:
        reservation.getFee() == 10000
    }

    def "타임룰의 시간이 맞지 않아 할인이 되지 않았을 때, 영화 상영가격을 계산해 확인한다."() {
        given:
        givenAmountDiscountStrategyAndTimeOfRule(DayOfWeek.SUNDAY, "09:00", "11:40")

        when:
        def reservation = showing.reserve(customer, DEFAULT_AUDIENCE_COUNT)

        then:
        reservation.getFee() == 10000
    }

    def "타임룰이 적용되어 가격할인전략이 적용되었을 때, 영화 상영가격을 계산해 확인한다."() {
        given:
        givenAmountDiscountStrategyAndTimeOfRule(DayOfWeek.SUNDAY, "09:01", "11:29")

        when:
        def reservation = showing.reserve(customer, DEFAULT_AUDIENCE_COUNT)

        then:
        reservation.getFee() == 7000
    }

    def "타임룰이 적용되어 가격할인전략이 적용되었을 때, 5명의 영화 상영가격을 계산해 확인한다."() {
        given:
        givenAmountDiscountStrategyAndTimeOfRule(DayOfWeek.SUNDAY, "09:01", "11:29")
        def audienceCount = 5

        when:
        def reservation = showing.reserve(customer, audienceCount)

        then:
        reservation.getFee() == 35000
    }

    def "시퀀스룰이 적용되어 퍼센트할인전략이 적용되었을 때, 5명의 영화 상영가격을 계산해 확인한다."() {
        given:
        showing.setSequence(1)
        setNotMatchTimeRuleShowingTime()
        movie.setDiscountStrategy(new PercentDiscountStrategy())

        when:
        def reservation = showing.reserve(customer, DEFAULT_AUDIENCE_COUNT)

        then:
        reservation.getFee() == 8000
    }

    def "타임룰이 적용되어 퍼센트할인전략이 적용되었을 때, 영화 상영가격을 계산해 확인한다."() {
        given:
        showing.setSequence(2)
        setMatchTimeRuleShowingTime()
        movie.setDiscountStrategy(new PercentDiscountStrategy())

        when:
        def reservation = showing.reserve(customer, DEFAULT_AUDIENCE_COUNT)

        then:
        reservation.getFee() == 8000
    }

    def "시퀀스룰이 적용되어 오버랩할인전략이 적용되었을 때, 영화 상영가격을 계산해 확인한다."() {
        given:
        showing.setSequence(1)
        setNotMatchTimeRuleShowingTime()
        movie.setDiscountStrategy(createOverlappedDiscountStrategy())

        when:
        def reservation = showing.reserve(customer, DEFAULT_AUDIENCE_COUNT)

        then:
        reservation.getFee() == 5000
    }

    def "타임룰이 적용되어 오버랩할인전략이 적용되었을 때, 영화 상영가격을 계산해 확인한다."() {
        given:
        showing.setSequence(2)
        setMatchTimeRuleShowingTime()
        movie.setDiscountStrategy(createOverlappedDiscountStrategy())

        when:
        def reservation = showing.reserve(customer, DEFAULT_AUDIENCE_COUNT)

        then:
        reservation.getFee() == 5000
    }

    def "타임룰이 적용되어 오버랩할인전략이 적용되었을 때, 4명의 영화 상영가격을 계산해 확인한다."() {
        given:
        showing.setSequence(2)
        setMatchTimeRuleShowingTime()
        movie.setDiscountStrategy(createOverlappedDiscountStrategy())
        def audienceCount = 4

        when:
        def reservation = showing.reserve(customer, audienceCount)

        then:
        reservation.getFee() == 20000
    }

    def givenAmountDiscountStrategyAndTimeOfRule(dayOfWeek, startTime, endTime) {
        showing.setSequence(2)
        showing.createShowingTime(dayOfWeek, startTime, endTime)
        movie.setDiscountStrategy(new AmountDiscountStrategy())
    }

    def createOverlappedDiscountStrategy() {
        def overlappedDiscountStrategy = new OverlappedDiscountStrategy()
        overlappedDiscountStrategy.setStrategies(Arrays.asList(new AmountDiscountStrategy(), new PercentDiscountStrategy()))
        overlappedDiscountStrategy
    }

    def setNotMatchTimeRuleShowingTime() {
        showing.createShowingTime(DayOfWeek.MONDAY, "09:01", "11:01")
    }

    def setMatchTimeRuleShowingTime() {
        showing.createShowingTime(DayOfWeek.SUNDAY, "09:01", "11:01")
    }
}