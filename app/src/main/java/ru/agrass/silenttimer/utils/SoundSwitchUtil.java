package ru.agrass.silenttimer.utils;

import android.content.Intent;
import android.media.AudioManager;
import android.util.Log;

import ru.agrass.silenttimer.SilentTimerApplication;

import static android.content.Context.AUDIO_SERVICE;

public class SoundSwitchUtil {
    private static final String TAG = SoundSwitchUtil.class.getSimpleName();

    public static void soundTurnOn() {
        Log.e(TAG, "Sound on");
        AudioManager audioManager = (AudioManager) SilentTimerApplication
                .getInstance().getSystemService(AUDIO_SERVICE);
        assert audioManager != null;
        audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
    }

    public static void soundTurnOff() {
        Log.e(TAG, "Sound off");
        AudioManager audioManager = (AudioManager) SilentTimerApplication
                .getInstance().getSystemService(AUDIO_SERVICE);
        assert audioManager != null;
        audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
    }

}
