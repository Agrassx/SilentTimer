package ru.agrass.silenttimer.utils;

import org.junit.Test;

import java.util.Calendar;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

public class CalendarUtilTest {

    private static final long DAY_MILLISECOND = 86400000;
    private static final long HOUR_MILLISECOND = 3600000;

    @Test
    public void getCalendar() {
    }

    @Test
    public void getCalendar1() {
    }

    @Test
    public void getCalendar2() {
    }

    @Test
    public void getCalendarOfNextWeek() {
        Calendar current = CalendarUtil.getCalendar(10, 0);
        Calendar nextWeek = CalendarUtil.getCalendarOfNextWeek(
                10,
                0,
                CalendarUtil.getCurrentDayOfWeek()
        );
        assertTrue(current.getTimeInMillis() < nextWeek.getTimeInMillis());
    }

    @Test
    public void getCurrentTimeCalendar() {
    }

    @Test
    public void getCurrentDayOfWeek() {
    }

    @Test
    public void addDay() {
        Calendar currentCalendar = CalendarUtil.getCurrentTimeCalendar();
        assertThat(
                currentCalendar.getTimeInMillis() + DAY_MILLISECOND,
                is(CalendarUtil.addDay(currentCalendar).getTimeInMillis())
        );

        assertThat(
                currentCalendar.getTimeInMillis() + DAY_MILLISECOND + 1500,
                not(CalendarUtil.addDay(currentCalendar).getTimeInMillis())
        );
    }

    @Test
    public void isCurrentLessThenTimeFrom() {
        Calendar calendarFrom = CalendarUtil.getCurrentTimeCalendar();
        long currentTime = System.currentTimeMillis();
        calendarFrom.setTimeInMillis(currentTime + HOUR_MILLISECOND * 2);

        assertTrue(CalendarUtil.isCurrentLessThenTimeFrom(calendarFrom));

        calendarFrom.setTimeInMillis(currentTime - HOUR_MILLISECOND * 3);

        assertFalse(CalendarUtil.isCurrentLessThenTimeFrom(calendarFrom));
    }

    @Test
    public void isCurrentTimeBetweenFromAndTo() {
        Calendar from = CalendarUtil.getCurrentTimeCalendar();
        Calendar to = CalendarUtil.getCurrentTimeCalendar();
        long currentTime = System.currentTimeMillis();

        from.setTimeInMillis(currentTime - HOUR_MILLISECOND * 2);
        to.setTimeInMillis(currentTime + HOUR_MILLISECOND * 2);

        assertTrue(CalendarUtil.isCurrentTimeBetweenFromAndTo(from, to));

        from.setTimeInMillis(currentTime - HOUR_MILLISECOND * 4);
        to.setTimeInMillis(currentTime - HOUR_MILLISECOND * 2);

        assertFalse(CalendarUtil.isCurrentTimeBetweenFromAndTo(from, to));

        from.setTimeInMillis(currentTime + HOUR_MILLISECOND * 2);
        to.setTimeInMillis(currentTime + HOUR_MILLISECOND * 5);

        assertFalse(CalendarUtil.isCurrentTimeBetweenFromAndTo(from, to));
    }

    @Test
    public void isCurrentMoreThenTimeTo() {
        Calendar to = CalendarUtil.getCurrentTimeCalendar();
        long currentTime = System.currentTimeMillis();
        to.setTimeInMillis(currentTime - HOUR_MILLISECOND * 2);

        assertTrue(CalendarUtil.isCurrentMoreThenTimeTo(to));

        to.setTimeInMillis(currentTime + HOUR_MILLISECOND * 3);

        assertFalse(CalendarUtil.isCurrentMoreThenTimeTo(to));
    }
}