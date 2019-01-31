package com.example.sudo.hardtogetup.utils;

import android.app.PendingIntent;
import android.content.Intent;

import com.example.sudo.hardtogetup.activity.AlarmActivity;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

public class AlarmJobService extends JobService {
    @Override
    public boolean onStartJob(JobParameters jobParameters) {
//ovo je servis koji se izvršava kada se pali alarm
        Intent intent = new Intent(getApplicationContext(), AlarmActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
        try {
            pi.send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }
}
