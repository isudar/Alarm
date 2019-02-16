package com.example.sudo.hardtogetup.adapters;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sudo.hardtogetup.R;
import com.example.sudo.hardtogetup.models.Alarm;
import com.example.sudo.hardtogetup.models.MathProblem;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

public class DrawerRecyclerAdapter extends RecyclerView.Adapter<DrawerRecyclerAdapter.ViewHolder> {

    private List<MathProblem> mathProblemList = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_drawer_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.tvMathProblem.setText(mathProblemList.get(position).getProblem());
    }

    @Override
    public int getItemCount() {
        return mathProblemList.size();
    }

    public void setAdapterList(List<MathProblem> mathProblemList) {
        this.mathProblemList.clear();
        this.mathProblemList.addAll(mathProblemList);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        @BindView(R.id.tvMathProblem)
        TextView tvMathProblem;
        @BindView(R.id.clDrawerItem)
        ConstraintLayout clDrawerItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            clDrawerItem.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View view) {
            //brisanje alarma iz liste moramo obrisati i iz realma
            if (getAdapterPosition() > 3) {
                String problem = mathProblemList.get(getAdapterPosition()).getProblem();
                mathProblemList.remove(getAdapterPosition());
                notifyItemRemoved(getAdapterPosition());
                notifyItemRangeChanged(getAdapterPosition(), mathProblemList.size());
                Realm realm = Realm.getDefaultInstance();
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        RealmResults<MathProblem> alarm = realm.where(MathProblem.class).equalTo("problem", problem).findAll();
                        alarm.deleteAllFromRealm();
                    }
                });
            }
            return false;
        }
    }
}
