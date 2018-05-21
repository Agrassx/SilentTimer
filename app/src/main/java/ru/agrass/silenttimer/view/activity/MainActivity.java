package ru.agrass.silenttimer.view.activity;

import android.app.AlarmManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.AlarmManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.widget.Toast;

import ru.agrass.silenttimer.R;
import ru.agrass.silenttimer.receivers.SoundSwitchReceiver;
import ru.agrass.silenttimer.services.SoundSwitchIntentService;
import ru.agrass.silenttimer.view.timerlist.TimerListFragment;


public class MainActivity extends AppCompatActivity implements MainActivityView {

    private SoundSwitchReceiver receiver = new SoundSwitchReceiver();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(findViewById(R.id.toolbar));

        TimerListFragment fragment = TimerListFragment.newInstance(this);
        String tag = fragment.getClass().getSimpleName();
        if (getSupportFragmentManager().findFragmentByTag(tag) == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, fragment, tag)
                    .commit();
        }

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, SoundSwitchIntentService.class);
        startService(intent);

        registerReceiver(receiver, new IntentFilter(SoundSwitchReceiver.SWITCH_TIMER));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
//        RefWatcher refWatcher = SilentTimerApplication.getRefWatcher(getApplicationContext());
//        refWatcher.watch(this);
    }
}
