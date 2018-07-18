package ru.agrass.silenttimer.view.activity;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;
import ru.agrass.silenttimer.R;
import ru.agrass.silenttimer.receivers.SoundSwitchReceiver;
import ru.agrass.silenttimer.view.timerlist.TimerListFragment;


public class MainActivity extends AppCompatActivity implements MainActivityView {

    private static final int REQUEST_ACCESS_NOTIFICATION_POLICY = 213;
    private static final int REQUEST_MODIFY_AUDIO_SETTINGS = 232;
    private SoundSwitchReceiver receiver = new SoundSwitchReceiver();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);
        setSupportActionBar(findViewById(R.id.toolbar));

        NotificationManager n = (NotificationManager)
                getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(!n.isNotificationPolicyAccessGranted()) {
                // Ask the user to grant access
//                TODO: Add user dialog
//                TODO: Test on api 21
                Intent intent = new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
                startActivityForResult(intent, REQUEST_ACCESS_NOTIFICATION_POLICY);
                return;
            }
        }
        startFragment();
    }

    private void startFragment()  {
        TimerListFragment fragment = TimerListFragment.newInstance(this);
        String tag = fragment.getClass().getSimpleName();
        if (getSupportFragmentManager().findFragmentByTag(tag) == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, fragment, tag)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unregisterReceiver(receiver);
//        RefWatcher refWatcher = SilentTimerApplication.getRefWatcher(getApplicationContext());
//        refWatcher.watch(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_ACCESS_NOTIFICATION_POLICY: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startFragment();
                } else {
                    this.onDestroy();
                }
                return;
            }
        }
    }
}
