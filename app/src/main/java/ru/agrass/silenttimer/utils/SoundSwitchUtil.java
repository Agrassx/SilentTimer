package ru.agrass.silenttimer.utils;

import android.media.AudioManager;
import android.util.Log;

import ru.agrass.silenttimer.SilentTimerApplication;

import static android.content.Context.AUDIO_SERVICE;

public class SoundSwitchUtil {
    private static final String TAG = SoundSwitchUtil.class.getSimpleName();

    public static void soundTurnOn() {
        Log.i(TAG, "Sound turn on");
        AudioManager audioManager = (AudioManager) SilentTimerApplication
                .getInstance().getSystemService(AUDIO_SERVICE);
        assert audioManager != null;
        audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
    }

    public static void soundTurnOff() {
        Log.i(TAG, "Sound turn off");
        AudioManager audioManager = (AudioManager) SilentTimerApplication
                .getInstance().getSystemService(AUDIO_SERVICE);
        assert audioManager != null;
        audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
    }

}
