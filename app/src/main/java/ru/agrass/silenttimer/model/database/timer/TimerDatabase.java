package ru.agrass.silenttimer.model.database.timer;

import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import ru.agrass.silenttimer.model.entity.Timer;


@Database(entities = {Timer.class}, version = 1)
public abstract class TimerDatabase extends RoomDatabase {

    private static final String NAME_DATABASE = "timer-database";
    private static TimerDatabase mInstance;
    private final MutableLiveData<Boolean> mIsDatabaseCreated = new MutableLiveData<>();

    public abstract TimerDao timerDao();


    public static TimerDatabase getTimerDatabase(final Context context) {
        if (mInstance == null) {
            synchronized (TimerDatabase.class) {
                mInstance = Room.databaseBuilder(
                        context.getApplicationContext(),
                        TimerDatabase.class,
                        NAME_DATABASE
                ).build();
            }
        }
        return mInstance;

    }
}
