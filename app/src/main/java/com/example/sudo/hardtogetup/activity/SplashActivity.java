package com.example.sudo.hardtogetup.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;

import com.example.sudo.hardtogetup.R;
import com.example.sudo.hardtogetup.models.MathProblem;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmConfiguration;


public class SplashActivity extends AppCompatActivity {

    @BindView(R.id.rlRootActivity)
    RelativeLayout rlRootActivity;
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        Realm.init(this);
        configureLocale();
        sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);
        if (!sharedPreferences.getBoolean("isRun", false)) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isRun", true);
            editor.apply();
            saveDefaultProblemToRealm();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                openHomeIntent();
            }
        }, 2000);
    }

    private void configureLocale() {
        Locale locale = new Locale("hr", "HR");
        Locale.setDefault(locale);
        Configuration configuration = this.getResources().getConfiguration();
        configuration.setLocale(locale);
        this.getResources().updateConfiguration(configuration,this.getResources().getDisplayMetrics());
    }

    private void saveDefaultProblemToRealm() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        MathProblem mathProblem = new MathProblem("2 + 2 =", "4", "2", "3", "5");
        MathProblem mathProblem1 = new MathProblem("5 * 5 =", "25", "26", "24", "30");
        MathProblem mathProblem2 = new MathProblem("2 * 2 - 2 =", "2", "4", "3", "5");
        MathProblem mathProblem3 = new MathProblem("3 * 3 - 3 / 3   =", "8", "5", "6", "10");

        realm.copyToRealm(mathProblem);
        realm.copyToRealm(mathProblem1);
        realm.copyToRealm(mathProblem2);
        realm.copyToRealm(mathProblem3);
        realm.commitTransaction();
        realm.close();
    }

    private void openHomeIntent() {
        Intent openHome = new Intent(this, HomeActivity.class);
        startActivity(openHome);
        finish();
    }
}
