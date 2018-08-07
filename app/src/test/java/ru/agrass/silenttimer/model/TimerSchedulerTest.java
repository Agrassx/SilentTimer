package ru.agrass.silenttimer.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;

import static org.junit.Assert.*;

public class TimerSchedulerTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }


    @Test
    public void checkFindNearestDay() {
        Class[] classes = {Timer.class, String.class};
//        Method findNearestDay = TimerScheduler.class.getDeclaredMethod("findNearestDay", classes);
//        findNearestDay.setAccessible(true);
//
//        TimerScheduler scheduler = new TimerScheduler();
//
//        findNearestDay.invoke(Timer.generateTimer(), Week.LONG_MONDAY);
    }

    @Test
    public void startTimer() {
    }

    @Test
    public void setUpTimer() {
    }

    @Test
    public void updateTimer() {
    }

    @Test
    public void cancelTimer() {
    }
}