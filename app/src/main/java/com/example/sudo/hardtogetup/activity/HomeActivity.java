package com.example.sudo.hardtogetup.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.sudo.hardtogetup.R;
import com.example.sudo.hardtogetup.adapters.AlarmRecyclerAdapter;
import com.example.sudo.hardtogetup.adapters.DrawerRecyclerAdapter;
import com.example.sudo.hardtogetup.models.Alarm;
import com.example.sudo.hardtogetup.models.MathProblem;
import com.example.sudo.hardtogetup.utils.AlarmJobService;
import com.example.sudo.hardtogetup.utils.UIUtils;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.internal.Utils;
import io.realm.Realm;
import io.realm.RealmResults;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.ivHamburger)
    ImageView ivHamburger;
    @BindView(R.id.ivNewAlarm)
    ImageView ivNewAlarm;
    @BindView(R.id.rvAlarms)
    RecyclerView rvAlarms;
    @BindView(R.id.ivAddNewProblem)
    ImageView ivAddNewProblem;
    @BindView(R.id.rvMathProblems)
    RecyclerView rvMathProblems;
    @BindView(R.id.rlToolbar)
    RelativeLayout rlToolbar;
    @BindView(R.id.rlBottom)
    RelativeLayout rlBottom;
    @BindView(R.id.rlDrawer)
    RelativeLayout rlDrawer;
    @BindView(R.id.dlDrawer)
    DrawerLayout dlDrawer;


    private DrawerRecyclerAdapter drawerRecyclerAdapter;
    private AlarmRecyclerAdapter alarmRecyclerAdapter;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    Realm realm;
    FirebaseJobDispatcher dispatcher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        realm = Realm.getDefaultInstance();
        dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
        setUi();
    }

    private void setUi() {
        ivNewAlarm.setOnClickListener(this);
        ivAddNewProblem.setOnClickListener(this);
        ivHamburger.setOnClickListener(this);

        RecyclerView.LayoutManager mLayoutManger = new LinearLayoutManager(this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvMathProblems.setLayoutManager(mLayoutManger);
        drawerRecyclerAdapter = new DrawerRecyclerAdapter();
        rvMathProblems.setAdapter(drawerRecyclerAdapter);

        rvAlarms.setLayoutManager(layoutManager);
        alarmRecyclerAdapter = new AlarmRecyclerAdapter();
        rvAlarms.setAdapter(alarmRecyclerAdapter);

        setupDrawer();

    }

    private void getAlarmsFromRealmAndSetAlarmList() {
        if (realm.isClosed()) {
            realm = Realm.getDefaultInstance();
        }
        realm.beginTransaction();
        RealmResults<Alarm> alarms = realm.where(Alarm.class).findAll();
        realm.commitTransaction();
        if (!alarms.isEmpty()) {
            alarmRecyclerAdapter.setAlarmList(alarms);
        }
    }

    private void getProblemsFromRealmAndSetItToDrawer() {
        if (realm.isClosed()) {
            realm = Realm.getDefaultInstance();
        }
        realm.beginTransaction();
        RealmResults<MathProblem> mathProblems = realm.where(MathProblem.class).findAll();
        realm.commitTransaction();
        if (!mathProblems.isEmpty()) {
            drawerRecyclerAdapter.setAdapterList(mathProblems);
        }

    }

    private void setupDrawer() {
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, dlDrawer, R.string.drawer_open, R.string.drawer_close) {
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        dlDrawer.setDrawerListener(actionBarDrawerToggle);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivNewAlarm:
                openNewAlarmActivtiy();
                break;
            case R.id.ivAddNewProblem:
                openNewMathProblemActivity();
                break;
            case R.id.ivHamburger:
                dlDrawer.openDrawer(Gravity.START);
                break;
        }
    }

    private void openNewMathProblemActivity() {
        Intent openNewMathProblemActivity = new Intent(this, NewMathProblemActivity.class);
        startActivity(openNewMathProblemActivity);
    }

    private void openNewAlarmActivtiy() {
        Intent openNewAlarmActivity = new Intent(this, NewAlarmActivity.class);
        startActivity(openNewAlarmActivity);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getProblemsFromRealmAndSetItToDrawer();
        getAlarmsFromRealmAndSetAlarmList();
    }
}
