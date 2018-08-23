package ru.agrass.silenttimer.model.database.timer;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import ru.agrass.silenttimer.model.entity.Timer;


@Dao
public interface TimerDao {

    @Query("SELECT * FROM timers")
    List<Timer> getAll();

    @Query("SELECT * FROM timers WHERE uid = :uid")
    Timer getTimerByUid(long uid);

    @Insert
    void insertAll(Timer... timers);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insert(Timer timer);

    @Delete
    int delete(Timer timer);

}
