package ru.agrass.silenttimer.model;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;

public interface TimerDataSource {

    /**
     * Gets the user from the data source.
     *
     * @return the user from the data source.
     */
    Flowable<Timer> getTimer();

    Flowable<List<Timer>> getAllTimers();

    Single<Boolean> insertOrUpdateTimer(Timer timer);

    Single<Boolean> delete(Timer timer);

    void deleteAllUsersTimers();

}
