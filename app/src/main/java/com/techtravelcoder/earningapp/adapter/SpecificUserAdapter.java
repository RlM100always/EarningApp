package com.techtravelcoder.earningapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.techtravelcoder.earningapp.R;
import com.techtravelcoder.earningapp.model.SpecificUserModel;

import java.util.ArrayList;

public class SpecificUserAdapter extends RecyclerView.Adapter<SpecificUserAdapter.SpViewHolder> {
    Context context;
    ArrayList<SpecificUserModel> list ;

    public SpecificUserAdapter(Context context, ArrayList<SpecificUserModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public SpecificUserAdapter.SpViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.specific_user_design,parent,false);

        return new SpViewHolder(view) ;
    }

    @Override
    public void onBindViewHolder(@NonNull SpecificUserAdapter.SpViewHolder holder, int position) {

        SpecificUserModel specificUserModel=list.get(position);
        holder.all.setText("⚫️ Campaign Number : "+specificUserModel.getCount()+"\n⚫️ Cost : "+specificUserModel.getPoints()+" points "+"\n⚫️ Need : "+specificUserModel.getWorkerNumber()
        +" traffic / visitor  "+"\n⚫️ Date : "+specificUserModel.getDate());
        holder.stat.setText(specificUserModel.getStatus());
        holder.stat.setBackgroundColor(Color.parseColor(specificUserModel.getBackColor()));


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class SpViewHolder extends RecyclerView.ViewHolder {
        TextView all,stat;
        public SpViewHolder(@NonNull View itemView) {
            super(itemView);

            all=itemView.findViewById(R.id.all_text_set);
            stat=itemView.findViewById(R.id.status_check_id);
        }
    }
}
