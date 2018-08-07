package ru.agrass.silenttimer.model;

import android.support.v4.util.ArrayMap;

import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import ru.agrass.silenttimer.model.exceptions.DayNotFoundException;
import ru.agrass.silenttimer.model.exceptions.WeekException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class WeekTest {

    Map<String, Boolean> daysMap;

    @Before
    public void setUp() {
        daysMap = new ArrayMap<>(Week.COUNT_OF_DAYS);
        daysMap.put(Week.MONDAY, false);
        daysMap.put(Week.TUESDAY, false);
        daysMap.put(Week.WEDNESDAY, false);
        daysMap.put(Week.THURSDAY, false);
        daysMap.put(Week.FRIDAY, false);
        daysMap.put(Week.SATURDAY, false);
        daysMap.put(Week.SUNDAY, false);
    }

    @Test
    public void initWeek() {
        Map<String, Boolean> week = Week.initWeek();
        assertNotNull("Map is null;", week);
        assertEquals("Map not equals with test map;", daysMap, week);
        assertThat("Size mismatch for map;", week.size(), is(Week.COUNT_OF_DAYS));
        assertTrue("Missing keys in received map;", week.keySet().containsAll(daysMap.keySet()));
    }

    @Test
    public void getIntFromString() {
        assertThat("Wrong Mo value;", Week.INT_MONDAY, is(Week.getIntFromString(Week.MONDAY)));
        assertThat("Wrong Monday value;", Week.INT_MONDAY, is(Week.getIntFromString(Week.LONG_MONDAY)));

        assertThat("Wrong Tu value;", Week.INT_TUESDAY, is(Week.getIntFromString(Week.TUESDAY)));
        assertThat("Wrong Tuesday value;", Week.INT_TUESDAY, is(Week.getIntFromString(Week.LONG_TUESDAY)));

        assertThat("Wrong We value;", Week.INT_WEDNESDAY, is(Week.getIntFromString(Week.WEDNESDAY)));
        assertThat("Wrong Wednesday value;", Week.INT_WEDNESDAY, is(Week.getIntFromString(Week.LONG_WEDNESDAY)));

        assertThat("Wrong Th value;", Week.INT_THURSDAY, is(Week.getIntFromString(Week.THURSDAY)));
        assertThat("Wrong Thursday value;", Week.INT_THURSDAY, is(Week.getIntFromString(Week.LONG_THURSDAY)));

        assertThat("Wrong Fr value;", Week.INT_FRIDAY, is(Week.getIntFromString(Week.FRIDAY)));
        assertThat("Wrong Friday value;", Week.INT_FRIDAY, is(Week.getIntFromString(Week.LONG_FRIDAY)));

        assertThat("Wrong Sa value;", Week.INT_SATURDAY, is(Week.getIntFromString(Week.SATURDAY)));
        assertThat("Wrong Saturday value;", Week.INT_SATURDAY, is(Week.getIntFromString(Week.LONG_SATURDAY)));

        assertThat("Wrong Su value;", Week.INT_SUNDAY, is(Week.getIntFromString(Week.SUNDAY)));
        assertThat("Wrong Sunday value;", Week.INT_SUNDAY, is(Week.getIntFromString(Week.LONG_SUNDAY)));

    }

    @Test
    public void getShortStringFromInt() {
        assertThat(Week.getShortStringFromInt(Week.INT_MONDAY),    is(Week.MONDAY));
        assertThat(Week.getShortStringFromInt(Week.INT_TUESDAY),   is(Week.TUESDAY));
        assertThat(Week.getShortStringFromInt(Week.INT_WEDNESDAY), is(Week.WEDNESDAY));
        assertThat(Week.getShortStringFromInt(Week.INT_THURSDAY),  is(Week.THURSDAY));
        assertThat(Week.getShortStringFromInt(Week.INT_FRIDAY),    is(Week.FRIDAY));
        assertThat(Week.getShortStringFromInt(Week.INT_SATURDAY),  is(Week.SATURDAY));
        assertThat(Week.getShortStringFromInt(Week.INT_SUNDAY),    is(Week.SUNDAY));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testWeekIndexOutOfBoundsException() {
        Week.getShortStringFromInt(7);
        Week.getShortStringFromInt(-1);
    }

    @Test
    public void isDayThisWeek() {
//        TODO: What if current day = near day
        String currentDay = Week.LONG_MONDAY;
        assertTrue(" ", Week.isDayOnThisWeek(currentDay, Week.LONG_TUESDAY));
        assertTrue(" ", Week.isDayOnThisWeek(currentDay, Week.LONG_WEDNESDAY));
        assertTrue(" ", Week.isDayOnThisWeek(currentDay, Week.LONG_THURSDAY));
        assertTrue(" ", Week.isDayOnThisWeek(currentDay, Week.LONG_FRIDAY));
        assertTrue(" ", Week.isDayOnThisWeek(currentDay, Week.LONG_SATURDAY));
        assertTrue(" ", Week.isDayOnThisWeek(currentDay, Week.LONG_SUNDAY));

        currentDay = Week.LONG_TUESDAY;
        assertFalse(" ", Week.isDayOnThisWeek(currentDay, Week.LONG_MONDAY));
        assertTrue(" ", Week.isDayOnThisWeek(currentDay, Week.LONG_WEDNESDAY));
        assertTrue(" ", Week.isDayOnThisWeek(currentDay, Week.LONG_THURSDAY));
        assertTrue(" ", Week.isDayOnThisWeek(currentDay, Week.LONG_FRIDAY));
        assertTrue(" ", Week.isDayOnThisWeek(currentDay, Week.LONG_SATURDAY));
        assertTrue(" ", Week.isDayOnThisWeek(currentDay, Week.LONG_SUNDAY));


        currentDay = Week.LONG_WEDNESDAY;
        assertFalse(" ", Week.isDayOnThisWeek(currentDay, Week.LONG_MONDAY));
        assertFalse(" ", Week.isDayOnThisWeek(currentDay, Week.LONG_TUESDAY));
        assertTrue(" ", Week.isDayOnThisWeek(currentDay, Week.LONG_THURSDAY));
        assertTrue(" ", Week.isDayOnThisWeek(currentDay, Week.LONG_FRIDAY));
        assertTrue(" ", Week.isDayOnThisWeek(currentDay, Week.LONG_SATURDAY));
        assertTrue(" ", Week.isDayOnThisWeek(currentDay, Week.LONG_SUNDAY));

        currentDay = Week.LONG_THURSDAY;
        assertFalse(" ", Week.isDayOnThisWeek(currentDay, Week.LONG_MONDAY));
        assertFalse(" ", Week.isDayOnThisWeek(currentDay, Week.LONG_TUESDAY));
        assertFalse(" ", Week.isDayOnThisWeek(currentDay, Week.LONG_WEDNESDAY));
        assertTrue(" ", Week.isDayOnThisWeek(currentDay, Week.LONG_FRIDAY));
        assertTrue(" ", Week.isDayOnThisWeek(currentDay, Week.LONG_SATURDAY));
        assertTrue(" ", Week.isDayOnThisWeek(currentDay, Week.LONG_SUNDAY));

        currentDay = Week.LONG_FRIDAY;
        assertFalse(" ", Week.isDayOnThisWeek(currentDay, Week.LONG_MONDAY));
        assertFalse(" ", Week.isDayOnThisWeek(currentDay, Week.LONG_TUESDAY));
        assertFalse(" ", Week.isDayOnThisWeek(currentDay, Week.LONG_WEDNESDAY));
        assertFalse(" ", Week.isDayOnThisWeek(currentDay, Week.LONG_THURSDAY));
        assertTrue(" ", Week.isDayOnThisWeek(currentDay, Week.LONG_SATURDAY));
        assertTrue(" ", Week.isDayOnThisWeek(currentDay, Week.LONG_SUNDAY));

        currentDay = Week.LONG_SATURDAY;
        assertFalse(" ", Week.isDayOnThisWeek(currentDay, Week.LONG_MONDAY));
        assertFalse(" ", Week.isDayOnThisWeek(currentDay, Week.LONG_TUESDAY));
        assertFalse(" ", Week.isDayOnThisWeek(currentDay, Week.LONG_WEDNESDAY));
        assertFalse(" ", Week.isDayOnThisWeek(currentDay, Week.LONG_THURSDAY));
        assertFalse(" ", Week.isDayOnThisWeek(currentDay, Week.LONG_FRIDAY));
        assertTrue(" ", Week.isDayOnThisWeek(currentDay, Week.LONG_SUNDAY));

        currentDay = Week.SUNDAY;
        assertFalse(" ", Week.isDayOnThisWeek(currentDay, Week.LONG_MONDAY));
        assertFalse(" ", Week.isDayOnThisWeek(currentDay, Week.LONG_TUESDAY));
        assertFalse(" ", Week.isDayOnThisWeek(currentDay, Week.LONG_WEDNESDAY));
        assertFalse(" ", Week.isDayOnThisWeek(currentDay, Week.LONG_THURSDAY));
        assertFalse(" ", Week.isDayOnThisWeek(currentDay, Week.LONG_FRIDAY));
        assertFalse(" ", Week.isDayOnThisWeek(currentDay, Week.LONG_SATURDAY));

    }

    @Test
    public void findNearestDay() throws WeekException {

        /**
         * Test in case when
         *
         **/

        Map<String, Boolean> week = new ArrayMap<>(Week.COUNT_OF_DAYS);
        week.put(Week.MONDAY,    false);
        week.put(Week.TUESDAY,   false);
        week.put(Week.WEDNESDAY, true);
        week.put(Week.THURSDAY,  false);
        week.put(Week.FRIDAY,    true);
        week.put(Week.SATURDAY,  false);
        week.put(Week.SUNDAY,    false);
        String currentDay = Week.LONG_MONDAY;
        assertThat(Week.findNearestDay(week, currentDay), is(Week.WEDNESDAY));




        /**
        * Test in case when
        *
        **/

        week = new ArrayMap<>(Week.COUNT_OF_DAYS);
        week.put(Week.MONDAY,    true);
        week.put(Week.TUESDAY,   false);
        week.put(Week.WEDNESDAY, true);
        week.put(Week.THURSDAY,  false);
        week.put(Week.FRIDAY,    false);
        week.put(Week.SATURDAY,  false);
        week.put(Week.SUNDAY,    false);
        currentDay = Week.LONG_FRIDAY;
        assertThat(Week.findNearestDay(week, currentDay), is(Week.MONDAY));

    }

    @Test(expected = WeekException.class)
    public void findNearestDayWeekException() throws WeekException {
        Week.findNearestDay(daysMap, Week.LONG_FRIDAY);
    }


    @Test
    public void isInCurrentDay() throws DayNotFoundException {
        Map<String, Boolean> week = new ArrayMap<>(Week.COUNT_OF_DAYS);
        week.put(Week.MONDAY,    false);
        week.put(Week.TUESDAY,   false);
        week.put(Week.WEDNESDAY, true);
        week.put(Week.THURSDAY,  false);
        week.put(Week.FRIDAY,    true);
        week.put(Week.SATURDAY,  false);
        week.put(Week.SUNDAY,    false);

        String currentDay = Week.WEDNESDAY;
        assertTrue(Week.isInCurrentDay(week, currentDay));

        currentDay = Week.SUNDAY;
        assertFalse(Week.isInCurrentDay(week, currentDay));
    }

    @Test(expected = DayNotFoundException.class)
    public void isInCurrentDayException() throws DayNotFoundException {
        Map<String, Boolean> week = new ArrayMap<>(Week.COUNT_OF_DAYS);
        week.put(Week.MONDAY,    false);
        week.put(Week.TUESDAY,   false);
        week.put(Week.WEDNESDAY, true);
        week.put(Week.THURSDAY,  false);
        week.put(Week.FRIDAY,    true);
        week.put(Week.SATURDAY,  false);
        week.put(Week.SUNDAY,    false);

        String currentDay = "some day";
        assertTrue(Week.isInCurrentDay(week, currentDay));
    }
}