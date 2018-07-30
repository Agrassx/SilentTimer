package ru.agrass.silenttimer.view.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;
import ru.agrass.silenttimer.R;
import ru.agrass.silenttimer.view.timerlist.TimerListFragment;


public class MainActivity extends AppCompatActivity implements MainActivityView {

    private static final int REQUEST_MODIFY_AUDIO_SETTINGS = 232;

    private View mLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);
        mLayout = findViewById(R.id.mainLayout);
        setSupportActionBar(findViewById(R.id.toolbar));
        checkPermissions();
    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.MODIFY_AUDIO_SETTINGS)
                != PackageManager.PERMISSION_GRANTED) {
            askModifyAudioSettings();
            return;
        }
        startFragment();
    }

    private void askModifyAudioSettings() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.MODIFY_AUDIO_SETTINGS)) {
            // Show an explanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.

            Snackbar.make(mLayout, "Need audio permission",
                    Snackbar.LENGTH_INDEFINITE).setAction(R.string.ok, view -> {
                            // Request the permission
                            ActivityCompat.requestPermissions(
                                    MainActivity.this,
                                    new String[]{Manifest.permission.MODIFY_AUDIO_SETTINGS},
                                    REQUEST_MODIFY_AUDIO_SETTINGS
                            );
                    })
                    .show();

        } else {
            // No explanation needed; request the permission
            ActivityCompat.requestPermissions(this,
                    new String[]{ Manifest.permission.MODIFY_AUDIO_SETTINGS },
                    REQUEST_MODIFY_AUDIO_SETTINGS);

            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        }
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
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_MODIFY_AUDIO_SETTINGS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkPermissions();
                } else {
                    this.onDestroy();
                }
            }
        }
    }
}
