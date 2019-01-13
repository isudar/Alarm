package com.example.sudo.hardtogetup.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;

import com.example.sudo.hardtogetup.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmConfiguration;

import static com.example.sudo.hardtogetup.utils.UIUtils.setGradientBackground;

public class SplashActivity extends AppCompatActivity {

    @BindView(R.id.rlRootActivity)
    RelativeLayout rlRootActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        Realm.init(this);
        RealmConfiguration config = new  RealmConfiguration
                .Builder()
                .build();
        config.shouldDeleteRealmIfMigrationNeeded();
        Realm.setDefaultConfiguration(config);

        setGradientBackground(this, R.color.green_light, R.color.blue_light, findViewById(R.id.rlRootActivity));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                openHomeIntent();
            }
        }, 2000);
    }

    private void openHomeIntent() {
        Intent openHome = new Intent(this, HomeActivity.class);
        startActivity(openHome);
    }
}
