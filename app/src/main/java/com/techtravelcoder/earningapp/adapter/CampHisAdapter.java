package com.techtravelcoder.earningapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.techtravelcoder.earningapp.R;
import com.techtravelcoder.earningapp.model.HomeModel;
import com.techtravelcoder.earningapp.model.SpecificUserModel;

import java.util.ArrayList;

public class CampHisAdapter extends RecyclerView.Adapter< CampHisAdapter.MViewHolder> {

    Context context;
    ArrayList<HomeModel>list;

    public CampHisAdapter(Context context, ArrayList<HomeModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public CampHisAdapter.MViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.campain_his_design,parent,false);
        return new MViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CampHisAdapter.MViewHolder holder, int position) {

        HomeModel specificUserModel=list.get(position);


        holder.count.setText("⚫️ Campaign Number : "+(position+1));
        holder.total_worker.setText("⚫️ Need : "+specificUserModel.getWorkerNumber()+" Organic Traffic");
        holder.current_complete.setText(String.valueOf(specificUserModel.getWorkerComplete()));
        holder.stat.setText(specificUserModel.getWorkingHistory());
        holder.stat.setBackgroundColor(Color.parseColor(specificUserModel.getWorkingHistoryColor()));



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MViewHolder extends RecyclerView.ViewHolder {
        TextView count,total_worker,current_complete,stat ;
        public MViewHolder(@NonNull View itemView) {
            super(itemView);
            count=itemView.findViewById(R.id.count_id);

            total_worker=itemView.findViewById(R.id.total_worker_id);
            current_complete=itemView.findViewById(R.id.complete_id);
            stat=itemView.findViewById(R.id.status_id);

        }
    }
}
