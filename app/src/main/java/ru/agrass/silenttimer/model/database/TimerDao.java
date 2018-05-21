package ru.agrass.silenttimer.model.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import ru.agrass.silenttimer.model.Timer;


@Dao
public interface TimerDao {

    @Query("SELECT * FROM timers")
    Flowable<List<Timer>> getAll();

    @Insert
    void insertAll(Timer... timers);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insert(Timer timer);

    @Delete
    int delete(Timer timer);

}
