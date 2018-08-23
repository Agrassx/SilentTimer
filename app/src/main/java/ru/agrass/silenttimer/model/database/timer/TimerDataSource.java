package ru.agrass.silenttimer.model.database.timer;

import java.util.List;

import ru.agrass.silenttimer.model.entity.Timer;

public interface TimerDataSource {

    Timer getTimer(long uid);

    List<Timer> getAllTimers();

    Long insertOrUpdateTimer(Timer timer);

    int delete(Timer timer);

    void deleteAllUsersTimers();

}
