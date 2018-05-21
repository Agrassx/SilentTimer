package ru.agrass.silenttimer.model;

import android.content.Context;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.internal.operators.single.SingleCreate;
import io.reactivex.schedulers.Schedulers;
import ru.agrass.silenttimer.model.database.TimerDatabase;
import ru.agrass.silenttimer.utils.rx.AppSchedulerProvider;
import ru.agrass.silenttimer.utils.rx.SchedulerProvider;


@Singleton
public class DataBaseImpl implements TimerDataSource {

    private final TimerDatabase db;
    private final SchedulerProvider schedulerProvider;
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
        this.schedulerProvider = new AppSchedulerProvider();
        instance = this;
    }

    @Override
    public Flowable<Timer> getTimer() {
        return null;
    }

    @Override
    public Flowable<List<Timer>> getAllTimers() {
        return db.timerDao().getAll()
                .subscribeOn(schedulerProvider.io())
                .unsubscribeOn(schedulerProvider.io());
    }

    @Override
    public Single<Boolean> insertOrUpdateTimer(Timer timer) {
        return Single
                .just(db)
                .subscribeOn(schedulerProvider.io())
                .map(db -> db.timerDao().insert(timer) > 0)
                .doOnError(Throwable::printStackTrace)
                .observeOn(schedulerProvider.io());
    }

    @Override
    public Single<Boolean> delete(Timer timer) {
        return Single
                .just(db)
                .subscribeOn(schedulerProvider.io())
                .map(db -> db.timerDao().delete(timer) > 0)
                .doOnError(Throwable::printStackTrace)
                .observeOn(schedulerProvider.io());
    }

    @Override
    public void deleteAllUsersTimers() {

    }
}
