package com.techtravelcoder.earningapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.techtravelcoder.earningapp.R;
import com.techtravelcoder.earningapp.model.NotificationModel;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    Context context;
    ArrayList<NotificationModel>list;

    public NotificationAdapter(Context context, ArrayList<NotificationModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public NotificationAdapter.NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.notification_design,parent,false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.NotificationViewHolder holder, int position) {
        NotificationModel notificationModel=list.get(position);
        holder.text.setText(notificationModel.getText());
        holder.date.setText(notificationModel.getDate());
        Glide.with(context).load(notificationModel.getImage()).into(holder.img);

        //holder.photoView.setImageResource(R.drawable.ic_launcher_foreground);  // Set a placeholder image if needed
        Glide.with(context).load(notificationModel.getImage()).into(holder.photoView);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class NotificationViewHolder extends RecyclerView.ViewHolder {
        TextView text,date;
        ImageView img;
        PhotoView photoView;
        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);

            text=itemView.findViewById(R.id.notification_text_id);
            img=itemView.findViewById(R.id.notification_image_id);
            date=itemView.findViewById(R.id.notification_date_id);
            photoView=itemView.findViewById(R.id.notification_photo_view);


        }
    }
}
