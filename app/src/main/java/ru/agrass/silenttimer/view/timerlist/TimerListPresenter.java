package ru.agrass.silenttimer.view.timerlist;


import android.content.Context;
import android.util.Log;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;
import ru.agrass.silenttimer.model.database.DataBaseImpl;
import ru.agrass.silenttimer.model.entity.Timer;
import ru.agrass.silenttimer.model.TimerScheduler;
import ru.agrass.silenttimer.view.base.BasePresenter;

public class TimerListPresenter<V extends TimerListView> extends BasePresenter<V>
        implements ITimerListPresenter<V> {
    private static final String TAG = TimerListPresenter.class.getSimpleName();


    private DataBaseImpl dataBase;
    private TimerScheduler timerScheduler;
    private PublishSubject<Timer> publishSubject = PublishSubject.create();
    private Disposable getAllTimersDisposable;
    private Disposable addTimerDisposable;
    private Disposable deleteTimerDisposable;
    private Disposable updateDisposable;

    TimerListPresenter(Context context) {
        this.dataBase = DataBaseImpl.getInstance(context);
        this.timerScheduler = new TimerScheduler();
        initDisposable();
    }

    private void initDisposable() {
        updateDisposable = publishSubject
                .subscribeOn(getSchedulerProvider().io())
                .map(timer -> dataBase.insertOrUpdateTimer(timer))
                .observeOn(getSchedulerProvider().ui())
                .subscribe(
                        this::onTimerUpdate,
                        Throwable::printStackTrace
                );
        getCompositeDisposable().add(updateDisposable);
    }

    @Override
    public void getTimers() {
        Log.e(TAG, "get Timers");
        getAllTimersDisposable = Observable.fromCallable(() -> dataBase.getAllTimers())
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(this::onGetTimers, Throwable::printStackTrace);
        getCompositeDisposable().add(getAllTimersDisposable);
    }

    @Override
    public void updateTimer(Timer timer) {
        Log.e(TAG, "Update Timer");
        publishSubject.onNext(timer);
        timerScheduler.updateTimer(timer);
    }

    @Override
    public void addTimer() {
        Log.e(TAG, "Add Timer");
        addTimerDisposable = Observable
                .fromCallable(() -> dataBase.insertOrUpdateTimer(Timer.generateTimer()))
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(this::onTimerCreated, Throwable::printStackTrace);
        getCompositeDisposable().add(addTimerDisposable);
    }

    @Override
    public void deleteTimer(Timer timer) {
        Log.e(TAG, "Delete Timer");
        deleteTimerDisposable = Observable.fromCallable(() -> dataBase.delete(timer))
                        .subscribeOn(getSchedulerProvider().io())
                        .observeOn(getSchedulerProvider().ui())
                        .subscribe(this::onTimerCreated, Throwable::printStackTrace);
        getCompositeDisposable().add(deleteTimerDisposable);
    }

    private void onGetTimers(List<Timer> list) {
        Log.i(TAG, "list size: " + list.size());
        getAllTimersDisposable.dispose();
        getView().showTimers(list);
    }

    private void onTimerCreated(long uid) {
        addTimerDisposable.dispose();
        if (uid > 0) {
            getTimers();
            return;
        }
        getView().showMessage("Some error, try again");
    }

    private void onTimerDeleted(int uid) {
        deleteTimerDisposable.dispose();
        if (uid > 0) {
            getTimers();
        }
    }

    private void onTimerUpdate(long uid) {
        if (uid > 0) return;
        getView().showMessage("Some error, try again");
    }

    @Override
    public void resume() {
        if (updateDisposable.isDisposed()) {
            getCompositeDisposable().add(updateDisposable);
        }
    }

    @Override
    public void pause() {
        updateDisposable.dispose();
        getCompositeDisposable().clear();
    }

    @Override
    public void destroy() {
        super.onDetach();
    }

    public void startTimer(Timer timer) {
        timerScheduler.setUpTimer(timer);
    }

    public void stopTimer(Timer timer) {
        timerScheduler.cancelTimer(timer);
    }
}
