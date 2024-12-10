package com.techtravelcoder.earningapp.activity.service;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.techtravelcoder.earningapp.R;
import com.techtravelcoder.earningapp.adapter.HomeAdapter;
import com.techtravelcoder.earningapp.model.HomeModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity    {

    private ImageView settings ;
    private ImageView profile;
    RecyclerView recyclerView;
    HomeAdapter homeAdapter;
    HomeModel homeModel;
    ArrayList<Object> list;

    DatabaseReference mbase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int color=0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            color = getColor(R.color.red);
        }
        getWindow().setStatusBarColor(color);


       // Toast.makeText(MainActivity.this, "ggggggg", Toast.LENGTH_SHORT).show();

        handleDataRetrive();

    }



    public void handleDataRetrive() {
        recyclerView = findViewById(R.id.main_recycler_view_id);
        mbase = FirebaseDatabase.getInstance().getReference("Link Details");

        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        list = new ArrayList<>();
        homeAdapter = new HomeAdapter(this, list);
        recyclerView.setAdapter(homeAdapter);


        mbase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Clear the list before adding items
                list.clear();

                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        HomeModel homeModel = dataSnapshot.getValue(HomeModel.class);
                        if (homeModel != null) {
                            retrieveVisibilityAndAddToList(homeModel);
                        }
                    }
                }
                homeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled if needed
            }
        });


    }
    private void retrieveVisibilityAndAddToList(HomeModel model) {
        FirebaseDatabase.getInstance().getReference("Link Details").child(model.getPostKey())
                .child("visibility").child(FirebaseAuth.getInstance().getUid()).
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String s_visibility = snapshot.getValue(String.class);
                        processVisibility(s_visibility, model);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle onCancelled if needed
                    }
                });
    }
    private void processVisibility(String s_visibility, HomeModel model) {
        if (model.getStatus().equals("Approve")&& model.getWorkingHistory().equals("Running") && (s_visibility==null || s_visibility.equals("visible"))) {
            // Toast.makeText(MainActivity.this, "11"+s_visibility, Toast.LENGTH_SHORT).show();
            list.add(model);
            homeAdapter.notifyDataSetChanged();
        }
    }





}