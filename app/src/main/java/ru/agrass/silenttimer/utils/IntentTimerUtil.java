package ru.agrass.silenttimer.utils;

import android.content.Intent;

import ru.agrass.silenttimer.SilentTimerApplication;
import ru.agrass.silenttimer.model.Timer;
import ru.agrass.silenttimer.receivers.SoundSwitchReceiver;

public class IntentTimerUtil {

    public static final String TAG_UID = "uid";
    public static final String TAG_TIME_TO = "time_to";
    public static final String TAG_TIME_FROM = "time_from";
    public static final String TAG_SOUND_TURN_OFF = "turn_off";

    public static Intent getIntentFromTimer(Timer timer, boolean isSoundOff) {

        Intent intent = new Intent(
                SilentTimerApplication.getInstance(),
                SoundSwitchReceiver.class
        );

        intent.setAction(SoundSwitchReceiver.SWITCH_TIMER);
        intent.putExtra(TAG_UID, timer.getUid());
        intent.putExtra(TAG_SOUND_TURN_OFF, isSoundOff);
//        intent.putExtra(TAG_TIME_TO, timer.getTimeTo());
//        intent.putExtra(TAG_TIME_FROM, timer.getTimeFrom());

        return intent;
    }



}
