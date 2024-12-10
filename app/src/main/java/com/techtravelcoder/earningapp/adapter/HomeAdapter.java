package com.techtravelcoder.earningapp.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.CountDownTimer;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.techtravelcoder.earningapp.R;
import com.techtravelcoder.earningapp.activity.service.MainActivity;
import com.techtravelcoder.earningapp.activity.service.MainActivitySupport;
import com.techtravelcoder.earningapp.model.HomeModel;

import java.util.ArrayList;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.NewViewHolder> {

    Context context;
    ArrayList<Object> list;


    public HomeAdapter(Context context, ArrayList<Object> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public HomeAdapter.NewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.home_design,parent,false);
        return new NewViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull HomeAdapter.NewViewHolder holder, int position) {

        HomeModel homeModel= (HomeModel) list.get(position);
        holder.tit.setText(homeModel.getTitle());
        Glide.with(context).load(homeModel.getImage()).into(holder.img);




        //Toast.makeText(context, ""+homeModel.getNames(), Toast.LENGTH_SHORT).show();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, MainActivitySupport.class);
                intent.putExtra("link",homeModel.getLink());
                intent.putExtra("postKey",homeModel.getPostKey());
                Toast.makeText(context, ""+list.size(), Toast.LENGTH_SHORT).show();

                intent.putExtra("uid",homeModel.getUid());
                intent.putExtra("size",list.size());

                context.startActivity(intent);
                ((Activity)context).finish();
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    public class NewViewHolder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView tit;
        public NewViewHolder(@NonNull View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.image_id);
            tit=itemView.findViewById(R.id.title_id);
        }
    }
}
