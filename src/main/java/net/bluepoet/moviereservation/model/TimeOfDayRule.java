package net.bluepoet.moviereservation.model;

import java.time.DayOfWeek;

/**
 * Created by bluepoet on 2017. 1. 6..
 */
public class TimeOfDayRule implements Rule {
    private DayOfWeek dayOfWeek = DayOfWeek.SUNDAY;
    private String startTime = "09:00";
    private String endTime = "11:30";

    public boolean isSatisfiedBy(Showing showing) {
        return showing.isPlayingOn(dayOfWeek) && showing.isContainTime(startTime, endTime);
    }
}
