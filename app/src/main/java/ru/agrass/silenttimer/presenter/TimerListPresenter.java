package ru.agrass.silenttimer.presenter;


import android.app.TimePickerDialog;
import android.content.Context;
import android.util.Log;

import java.util.List;

import io.reactivex.disposables.Disposable;
import ru.agrass.silenttimer.model.DataBaseImpl;
import ru.agrass.silenttimer.model.Timer;
import ru.agrass.silenttimer.model.TimerScheduler;
import ru.agrass.silenttimer.presenter.Base.BasePresenter;
import ru.agrass.silenttimer.view.timerlist.TimerListView;

public class TimerListPresenter extends BasePresenter {
    private static final String TAG = TimerListPresenter.class.getSimpleName();


    private DataBaseImpl dataBase;
    private final TimerListView view;
    private Disposable addDisposable;
    private TimePickerDialog dialogs;
    private TimerScheduler timerScheduler;
    private Context context;

    public TimerListPresenter(Context context, TimerListView view) {
        this.dataBase = DataBaseImpl.getInstance(context);
        this.timerScheduler = new TimerScheduler();
        this.context = context;
        this.view = view;
    }

    public void getTimers() {
        compositeDisposable.add(
                dataBase.getAllTimers()
                        .subscribeOn(mSchedulerProvider.io())
                        .observeOn(mSchedulerProvider.ui())
                        .doOnNext(this::onGetTimers)
                        .doOnError(Throwable::printStackTrace)
                        .subscribe()
        );
    }

    public void updateTimer(Timer timer) {
        addDisposable = dataBase.insertOrUpdateTimer(timer)
                .subscribeOn(mSchedulerProvider.io())
                .doOnSuccess(this::onTimerUpdate)
                .doOnError(Throwable::printStackTrace)
                .observeOn(mSchedulerProvider.ui())
                .subscribe();
        compositeDisposable.add(addDisposable);
    }

    public void addTimer() {
        addDisposable = dataBase.insertOrUpdateTimer(Timer.generateTimer())
                .subscribeOn(mSchedulerProvider.io())
                .doOnSuccess(this::onTimerCreate)
                .doOnError(Throwable::printStackTrace)
                .observeOn(mSchedulerProvider.ui())
                .subscribe();
        compositeDisposable.add(addDisposable);
    }

    public void deleteTimer(Timer timer) {
        compositeDisposable.add(
                dataBase.delete(timer)
                        .subscribeOn(mSchedulerProvider.io())
                        .doOnSuccess(this::onTimerCreate)
                        .doOnError(Throwable::printStackTrace)
                        .observeOn(mSchedulerProvider.ui())
                        .subscribe()

        );
    }

    private void onGetTimers(List<Timer> list) {
        Log.e(TAG, "list size: " + list.size());
        Log.e(TAG, "compositeDisposable: " + compositeDisposable.size());
        compositeDisposable.clear();
        view.showTimers(list);
    }

    private void onTimerCreate(boolean isSuccess) {
        if (isSuccess) {
            getTimers();
            return;
        }
        view.showMessage("Some error, try again");
    }

    private void onTimerUpdate(boolean isSuccess) {
        if (isSuccess) return;
        view.showMessage("Some error, try again");
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        compositeDisposable.clear();
    }

    public void startTimer(Timer timer) {
        timerScheduler.startTimer(timer);
    }

    public void stopTimer(Timer timer) {
        timerScheduler.cancelTimer(timer);
    }
}
