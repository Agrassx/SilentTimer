package ru.agrass.silenttimer.model.database;

import android.content.Context;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import ru.agrass.silenttimer.model.entity.Timer;
import ru.agrass.silenttimer.model.database.timer.TimerDataSource;
import ru.agrass.silenttimer.model.database.timer.TimerDatabase;


@Singleton
public class DataBaseImpl implements TimerDataSource {

    private final TimerDatabase db;
    private static DataBaseImpl instance;


    public static DataBaseImpl getInstance(Context context) {
        if (instance == null) {
            instance = new DataBaseImpl(context);
        }
        return instance;
    }

    @Inject
    private DataBaseImpl(Context context) {
        this.db = TimerDatabase.getTimerDatabase(context);
        instance = this;
    }

    @Override
    public Timer getTimer(long uid) {
        return db.timerDao().getTimerByUid(uid);
    }

    @Override
    public List<Timer> getAllTimers() {
        return db.timerDao().getAll();
    }

    @Override
    public Long insertOrUpdateTimer(Timer timer) {
        return db.timerDao().insert(timer);
    }

    @Override
    public int delete(Timer timer) {
        return db.timerDao().delete(timer);
    }

    @Override
    public void deleteAllUsersTimers() {

    }
}
