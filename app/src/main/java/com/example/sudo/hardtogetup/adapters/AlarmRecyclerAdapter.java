package com.example.sudo.hardtogetup.adapters;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.sudo.hardtogetup.R;
import com.example.sudo.hardtogetup.models.Alarm;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

public class AlarmRecyclerAdapter extends RecyclerView.Adapter<AlarmRecyclerAdapter.ViewHolder> {

    private List<Alarm> alarmList = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_alarm_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        String hour = String.valueOf(alarmList.get(position).getHour());
        String minutes = String.valueOf(alarmList.get(position).getMinutes());
        //pišemo 0 ako je broj od 0-9
        if (hour.length() == 1) {
            hour = "0" + hour;
        }
        if (minutes.length() == 1) {
            minutes = "0" + minutes;
        }
        viewHolder.tvHour.setText(hour);
        viewHolder.tvMinutes.setText(minutes);
        long tagMillis =Long.valueOf(alarmList.get(position).getTagMillis());
        if (tagMillis < System.currentTimeMillis()){
            viewHolder.rlTime.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_ATOP);
        }

        //ponavljajuci alarm stavljamo vidljivost na dane kojima se ponavlja
        if (alarmList.get(position).getAlarmDays().get(0))
            viewHolder.tvMonday.setVisibility(View.VISIBLE);
        if (alarmList.get(position).getAlarmDays().get(1))
            viewHolder.tvTuesday.setVisibility(View.VISIBLE);
        if (alarmList.get(position).getAlarmDays().get(2))
            viewHolder.tvWednsday.setVisibility(View.VISIBLE);
        if (alarmList.get(position).getAlarmDays().get(3))
            viewHolder.tvThursday.setVisibility(View.VISIBLE);
        if (alarmList.get(position).getAlarmDays().get(4))
            viewHolder.tFriday.setVisibility(View.VISIBLE);
        if (alarmList.get(position).getAlarmDays().get(5))
            viewHolder.tvSaturday.setVisibility(View.VISIBLE);
        if (alarmList.get(position).getAlarmDays().get(6))
            viewHolder.tvSunday.setVisibility(View.VISIBLE);
        if (!alarmList.get(position).getAlarmDays().get(0) && !alarmList.get(position).getAlarmDays().get(1) && !alarmList.get(position).getAlarmDays().get(2)&& !alarmList.get(position).getAlarmDays().get(3)&& !alarmList.get(position).getAlarmDays().get(4)&& !alarmList.get(position).getAlarmDays().get(5)&& !alarmList.get(position).getAlarmDays().get(6))
            viewHolder.llRepeatingDays.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {
        return alarmList.size();
    }

    public void setAlarmList(List<Alarm> alarmList) {
        this.alarmList.clear();
        this.alarmList.addAll(alarmList);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        @BindView(R.id.tvHour)
        TextView tvHour;
        @BindView(R.id.tvMinutes)
        TextView tvMinutes;
        @BindView(R.id.rlAlarm)
        RelativeLayout rlAlarm;
        @BindView(R.id.tvMonday)
        TextView tvMonday;
        @BindView(R.id.tvTuesday)
        TextView tvTuesday;
        @BindView(R.id.tvWednsday)
        TextView tvWednsday;
        @BindView(R.id.tvThursday)
        TextView tvThursday;
        @BindView(R.id.tFriday)
        TextView tFriday;
        @BindView(R.id.tvSaturday)
        TextView tvSaturday;
        @BindView(R.id.tvSunday)
        TextView tvSunday;
        @BindView(R.id.llRepeatingDays)
        LinearLayout llRepeatingDays;
        @BindView(R.id.rlTime)
        RelativeLayout rlTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            rlAlarm.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View view) {

            //brisanje alarma iz liste moramo obrisati i iz realma
            String tagMillis = alarmList.get(getAdapterPosition()).getTagMillis();
            alarmList.remove(getAdapterPosition());
            notifyItemRemoved(getAdapterPosition());
            notifyItemRangeChanged(getAdapterPosition(), alarmList.size());
            Realm realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    RealmResults<Alarm> alarm = realm.where(Alarm.class).equalTo("tagMillis", tagMillis).findAll();
                    alarm.deleteAllFromRealm();
                }
            });

            FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(view.getContext()));
            dispatcher.cancel(tagMillis);

            return false;
        }
    }
}
