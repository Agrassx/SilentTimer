package ru.agrass.silenttimer.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;

import ru.agrass.silenttimer.model.DataBaseImpl;
import ru.agrass.silenttimer.model.Timer;
import ru.agrass.silenttimer.utils.rx.AppSchedulerProvider;
import ru.agrass.silenttimer.utils.rx.SchedulerProvider;

import static ru.agrass.silenttimer.receivers.SoundSwitchReceiver.SWITCH_TIMER;

public class SoundSwitchIntentService extends IntentService {
    private static final String TAG = SoundSwitchIntentService.class.getSimpleName();

//    private final String name;
    private final SchedulerProvider mSchedulerProvider = new AppSchedulerProvider();
    private DataBaseImpl db;


    public SoundSwitchIntentService() {
        super("serviceTest");
    }

    public SoundSwitchIntentService(String name) {
        super(name);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "Service Destroyed");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.e(TAG, "Service Running");
        db = DataBaseImpl.getInstance(getApplicationContext());
        db.getAllTimers()
                .subscribeOn(mSchedulerProvider.io())
                .doOnNext(this::startTimer)
                .doOnError(Throwable::printStackTrace)
                .observeOn(mSchedulerProvider.io())
                .subscribe();

        sendBroadcast(new Intent(SWITCH_TIMER));
    }

    private void startTimer(List<Timer> list) {

    }
}
