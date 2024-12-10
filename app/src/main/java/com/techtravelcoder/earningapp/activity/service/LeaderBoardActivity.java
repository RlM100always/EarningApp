package com.techtravelcoder.earningapp.activity.service;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.techtravelcoder.earningapp.R;
import com.techtravelcoder.earningapp.adapter.LeaderBoardAdapter;
import com.techtravelcoder.earningapp.adapter.PaymentBuyAdapter;
import com.techtravelcoder.earningapp.model.TrnxModelMain;
import com.techtravelcoder.earningapp.model.UserModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class LeaderBoardActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private UserModel userModel;
    private LeaderBoardAdapter leaderBoardAdapter;
    private ArrayList<UserModel> list;
    private DatabaseReference mbase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);

        recyclerView=findViewById(R.id.leader_recycler_id);


        list = new ArrayList<>();
        mbase = FirebaseDatabase.getInstance().getReference("User Information");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        leaderBoardAdapter = new LeaderBoardAdapter(this, list);
        recyclerView.setAdapter(leaderBoardAdapter);

        mbase.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                 // list.clear();

                if (snapshot.exists()) {

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        userModel = dataSnapshot.getValue(UserModel.class);


                        if(userModel!=null){
                            list.add(userModel);
                        }
                    }

                    Collections.sort(list, new Comparator<UserModel>() {
                        @Override
                        public int compare(UserModel user1, UserModel user2) {
                            // Assuming balance is a double or float
                            return Double.compare(user2.getBalance(), user1.getBalance());
                        }
                    });


                    leaderBoardAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }
}