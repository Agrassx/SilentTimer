package ru.agrass.silenttimer.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class SoundSwitchReceiver extends BroadcastReceiver {
    private static final String TAG = SoundSwitchReceiver.class.getSimpleName();
    public static final String SWITCH_TIMER = "ru.agrass.silenttimer.action.switch";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e(TAG, "onReceive");
    }
}
