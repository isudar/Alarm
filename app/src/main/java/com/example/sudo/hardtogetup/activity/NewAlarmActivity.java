package com.example.sudo.hardtogetup.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.example.sudo.hardtogetup.R;
import com.example.sudo.hardtogetup.models.Alarm;
import com.example.sudo.hardtogetup.utils.AlarmJobService;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmList;

public class NewAlarmActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.ivDismiss)
    ImageView ivDismiss;
    @BindView(R.id.npHourPicker)
    NumberPicker npHourPicker;
    @BindView(R.id.npMinutesPicker)
    NumberPicker npMinutesPicker;
    @BindView(R.id.bSaveAlarm)
    Button bSaveAlarm;

    FirebaseJobDispatcher dispatcher;
    @BindView(R.id.cbMonday)
    AppCompatCheckBox cbMonday;
    @BindView(R.id.cbTuesday)
    AppCompatCheckBox cbTuesday;
    @BindView(R.id.cbWednesday)
    AppCompatCheckBox cbWednesday;
    @BindView(R.id.cbThursday)
    AppCompatCheckBox cbThursday;
    @BindView(R.id.cbFriday)
    AppCompatCheckBox cbFriday;
    @BindView(R.id.cbSaturday)
    AppCompatCheckBox cbSaturday;
    @BindView(R.id.cbSunday)
    AppCompatCheckBox cbSunday;

    long difference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_alarm);
        ButterKnife.bind(this);
        dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
        setUi();
    }

    private void setUi() {
        ivDismiss.setOnClickListener(this);
        bSaveAlarm.setOnClickListener(this);
        npHourPicker.setMinValue(0);
        npHourPicker.setMaxValue(23);
        npMinutesPicker.setMinValue(0);
        npMinutesPicker.setMaxValue(59);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivDismiss:
                finish();
                break;
            case R.id.bSaveAlarm:
                saveAlarmAndExit();
                break;
        }
    }

    private void saveAlarmAndExit() {
        int hour = npHourPicker.getValue();
        int minutes = npMinutesPicker.getValue();
        RealmList<Boolean> alarmDays = new RealmList<>();

        alarmDays.add(cbMonday.isChecked());
        alarmDays.add(cbTuesday.isChecked());
        alarmDays.add(cbWednesday.isChecked());
        alarmDays.add(cbThursday.isChecked());
        alarmDays.add(cbFriday.isChecked());
        alarmDays.add(cbSaturday.isChecked());
        alarmDays.add(cbSunday.isChecked());

        Long tagMillis = System.currentTimeMillis();
        String  tagMillisString = String.valueOf(System.currentTimeMillis());
        if (cbMonday.isChecked())
            createAlarmByDay(hour, minutes, tagMillisString, 2);
        if (cbTuesday.isChecked())
            createAlarmByDay(hour, minutes, tagMillisString, 3);
        if (cbWednesday.isChecked())
            createAlarmByDay(hour, minutes, tagMillisString, 4);
        if (cbThursday.isChecked())
            createAlarmByDay(hour, minutes, tagMillisString, 5);
        if (cbFriday.isChecked())
            createAlarmByDay(hour, minutes, tagMillisString, 6);
        if (cbSaturday.isChecked())
            createAlarmByDay(hour, minutes, tagMillisString, 7);
        if (cbSunday.isChecked())
            createAlarmByDay(hour, minutes, tagMillisString, 1);

        if (!cbMonday.isChecked() && !cbTuesday.isChecked() && !cbWednesday.isChecked() && !cbThursday.isChecked() && !cbFriday.isChecked() && !cbSaturday.isChecked() && !cbSunday.isChecked())
            createAlarm(hour, minutes, tagMillisString);
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        Alarm alarm = new Alarm();
        alarm.setHour(hour);
        alarm.setMinutes(minutes);
        alarm.setTagMillis(String.valueOf(tagMillis + difference));
        alarm.setAlarmDays(alarmDays);
        realm.copyToRealm(alarm);
        realm.commitTransaction();
        realm.close();
        long second = (difference / 1000) % 60;
        long minute = (difference / (1000 * 60)) % 60;
        long hours = (difference / (1000 * 60 * 60)) % 24;
        long days = (difference / (1000 * 60 * 60 * 24)) % 7;
        String time = String.format("%02d days %02d hours %02d  minutes %02d seconds ",days, hours, minute, second);
        Toast.makeText(this, "Alarm will start for: " + time, Toast.LENGTH_LONG).show();
        finish();
    }

    private void createAlarmByDay(int hour, int minutes, String tagMillis, int day) {
        Calendar now = Calendar.getInstance();
        Calendar alarmTime = Calendar.getInstance();
        if (day < now.get(Calendar.DAY_OF_WEEK) || now.get(Calendar.DAY_OF_WEEK) == 1)
            alarmTime.add(Calendar.WEEK_OF_YEAR, +1);

        alarmTime.set(Calendar.DAY_OF_WEEK, day);
        alarmTime.set(Calendar.HOUR_OF_DAY, hour);
        alarmTime.set(Calendar.MINUTE, minutes);
        alarmTime.set(Calendar.SECOND, 0);
        alarmTime.set(Calendar.MILLISECOND, 0);

        difference = alarmTime.getTimeInMillis() - now.getTimeInMillis();
        int startSeconds = (int) (difference / 1000); // tell the start seconds
        int endSeconds = startSeconds + 1; // within one seconds

        Job bJob = dispatcher.newJobBuilder()
                .setService(AlarmJobService.class)
                .setTag(tagMillis)
                .setReplaceCurrent(true)
                .setRecurring(false)
                .setRetryStrategy(RetryStrategy.DEFAULT_LINEAR)
                .setLifetime(Lifetime.FOREVER)
                .setTrigger(Trigger.executionWindow(startSeconds, endSeconds))
                .build();
        dispatcher.mustSchedule(bJob);

    }

    private void createAlarm(int hour, int minutes, String tagMillis) {

        Calendar now = Calendar.getInstance();
        Calendar alarmTime = Calendar.getInstance();
        alarmTime.set(Calendar.HOUR_OF_DAY, hour);
        alarmTime.set(Calendar.MINUTE, minutes);
        alarmTime.clear(Calendar.SECOND);
        alarmTime.clear(Calendar.MILLISECOND);


        difference = alarmTime.getTimeInMillis() - now.getTimeInMillis();

        if (difference < 0) {
            alarmTime.add(Calendar.DAY_OF_MONTH, 1);
            difference = alarmTime.getTimeInMillis() - now.getTimeInMillis();
        }

        int startSeconds = (int) (difference / 1000); // tell the start seconds
        int endSeconds = startSeconds + 1; // within one seconds

        Job bJob = dispatcher.newJobBuilder()
                .setService(AlarmJobService.class)
                .setTag(tagMillis)
                .setReplaceCurrent(true)
                .setRecurring(false)
                .setRetryStrategy(RetryStrategy.DEFAULT_LINEAR)
                .setLifetime(Lifetime.FOREVER)
                .setTrigger(Trigger.executionWindow(startSeconds, endSeconds))
                .build();
        dispatcher.mustSchedule(bJob);
    }
}
