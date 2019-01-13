package com.example.sudo.hardtogetup.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;

import com.example.sudo.hardtogetup.R;
import com.example.sudo.hardtogetup.models.Alarm;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class NewAlarmActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.ivDismiss)
    ImageView ivDismiss;
    @BindView(R.id.npHourPicker)
    NumberPicker npHourPicker;
    @BindView(R.id.npMinutesPicker)
    NumberPicker npMinutesPicker;
    @BindView(R.id.bSaveAlarm)
    Button bSaveAlarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_alarm);
        ButterKnife.bind(this);

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
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        Alarm alarm = new Alarm();
        alarm.setHour(npHourPicker.getValue());
        alarm.setMinutes(npMinutesPicker.getValue());
        realm.copyToRealm(alarm);
        realm.commitTransaction();
        realm.close();
        finish();
    }
}
