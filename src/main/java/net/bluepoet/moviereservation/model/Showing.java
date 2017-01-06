package net.bluepoet.moviereservation.model;

import java.time.DayOfWeek;
import java.time.LocalTime;

/**
 * Created by bluepoet on 2017. 1. 6..
 */
public class Showing {
    private Movie movie;
    private int sequence;
    private DayOfWeek dayOfWeek;
    private String startTime;
    private String endTime;

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

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public void createShowingTime(DayOfWeek dayOfWeek, String startTime, String endTime) {
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public boolean isSequence(int sequence) {
        if (this.sequence == sequence) {
            return true;
        }
        return false;
    }

    public boolean isPlayingOn(DayOfWeek dayOfWeek) {
        if (this.dayOfWeek.equals(dayOfWeek)) {
            return true;
        }
        return false;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public boolean isContainTime(String startTime, String endTime) {
        LocalTime showingStartTime = LocalTime.parse(this.startTime);
        LocalTime showingEndTime = LocalTime.parse(this.endTime);
        LocalTime ruleStartTime = LocalTime.parse(startTime);
        LocalTime ruleEndTime = LocalTime.parse(endTime);

        if (ruleStartTime.isBefore(showingStartTime) && ruleEndTime.isAfter(showingEndTime)) {
            return true;
        }

        return false;
    }
}
