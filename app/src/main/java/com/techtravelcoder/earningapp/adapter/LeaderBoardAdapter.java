package com.techtravelcoder.earningapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.techtravelcoder.earningapp.R;
import com.techtravelcoder.earningapp.model.UserModel;

import java.util.ArrayList;

public class LeaderBoardAdapter extends RecyclerView.Adapter<LeaderBoardAdapter.LeaderViewHolder> {
    Context context;
    ArrayList<UserModel>list;

    public LeaderBoardAdapter(Context context, ArrayList<UserModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public LeaderBoardAdapter.LeaderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.leaderboard_design,parent,false);
        return new LeaderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaderBoardAdapter.LeaderViewHolder holder, int position) {
        UserModel userModel=list.get(position);
        Toast.makeText(context, "111111", Toast.LENGTH_SHORT).show();

        holder.serial_no.setText(position+1+".");
        holder.name.setText(userModel.getName());
        holder.points.setText(String.valueOf(userModel.getBalance()));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class LeaderViewHolder extends RecyclerView.ViewHolder {

        TextView serial_no,name,points;
        public LeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            serial_no=itemView.findViewById(R.id.serial_no_leader_id);
            name=itemView.findViewById(R.id.leader_name_id);
            points=itemView.findViewById(R.id.leader_points_id);

        }
    }
}
