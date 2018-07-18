package ru.agrass.silenttimer;

import android.app.AlarmManager;
import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import io.fabric.sdk.android.Fabric;

public class SilentTimerApplication extends Application {

    private static SilentTimerApplication Instance;

    public static RefWatcher getRefWatcher(Context context) {
        SilentTimerApplication application = (SilentTimerApplication) context.getApplicationContext();
        return application.refWatcher;
    }

    public static synchronized SilentTimerApplication getInstance() {
        return Instance;
    }

    private RefWatcher refWatcher;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        this.initializeLeakDetection();
        Instance = this;
    }

    public static synchronized AlarmManager getAlarmManager() {
        return (AlarmManager) getInstance().getSystemService(ALARM_SERVICE);
    }

    private void initializeLeakDetection() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);
    }

}
