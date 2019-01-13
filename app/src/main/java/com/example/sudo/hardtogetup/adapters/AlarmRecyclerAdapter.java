package com.example.sudo.hardtogetup.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sudo.hardtogetup.R;
import com.example.sudo.hardtogetup.models.Alarm;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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
            if (hour.length() == 1){
                hour = "0" + hour;
            }
            if (minutes.length() == 1){
                minutes = "0" + minutes;
            }
            viewHolder.tvHour.setText(hour);
            viewHolder.tvMinutes.setText(minutes);

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

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvHour)
        TextView tvHour;
        @BindView(R.id.tvMinutes)
        TextView tvMinutes;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
