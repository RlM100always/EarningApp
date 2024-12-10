package com.techtravelcoder.earningapp.activity.service;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.techtravelcoder.earningapp.R;
import com.techtravelcoder.earningapp.adapter.NotificationAdapter;
import com.techtravelcoder.earningapp.model.NotificationModel;

import java.util.ArrayList;

public class NotificationActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NotificationModel notificationModel;
    private NotificationAdapter notificationAdapter;
    private ArrayList<NotificationModel>list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        recyclerView=findViewById(R.id.notification_recycler_view);
        list=new ArrayList<>();
        notificationAdapter=new NotificationAdapter(NotificationActivity.this,list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(notificationAdapter);

        FirebaseDatabase.getInstance().getReference("Control").child("Notification")
                .child("Main Notification")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        if(snapshot.exists()){
                            for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                                notificationModel=dataSnapshot.getValue(NotificationModel.class);
                                list.add(notificationModel);
                            }
                        }
                        notificationAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}