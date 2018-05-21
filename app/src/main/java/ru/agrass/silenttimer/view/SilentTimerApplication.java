package ru.agrass.silenttimer.view;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

public class SilentTimerApplication extends Application {

    public static RefWatcher getRefWatcher(Context context) {
        SilentTimerApplication application = (SilentTimerApplication) context.getApplicationContext();
        return application.refWatcher;
    }

    private RefWatcher refWatcher;

    @Override
    public void onCreate() {
        super.onCreate();
        this.initializeLeakDetection();
    }


    private void initializeLeakDetection() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);
    }

}
