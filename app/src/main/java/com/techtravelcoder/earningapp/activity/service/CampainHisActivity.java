package com.techtravelcoder.earningapp.activity.service;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.techtravelcoder.earningapp.R;
import com.techtravelcoder.earningapp.adapter.CampHisAdapter;
import com.techtravelcoder.earningapp.model.HomeModel;

import java.util.ArrayList;

public class CampainHisActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    CampHisAdapter campHisAdapter;
    HomeModel specificUserModel;
    ArrayList<HomeModel> list;
    DatabaseReference mbase ;
    private ImageView imageView;
    private TextView text ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campain_his);
        int color=0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            color = getColor(R.color.red);
        }
        getWindow().setStatusBarColor(color);

        imageView=findViewById(R.id.nothing_image);
        text=findViewById(R.id.nothing_text_id);
        //shimmerRecyclerView=findViewById(R.id.shimmer_view_container);
        recyclerView=findViewById(R.id.campain_recycler_id);


        mbase = FirebaseDatabase.getInstance().getReference("Link Details");

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list=new ArrayList<>();
        campHisAdapter=new CampHisAdapter(this,list);
        recyclerView.setAdapter(campHisAdapter);

        
        
        mbase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();

                if(snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        specificUserModel = dataSnapshot.getValue(HomeModel.class);

                        if(specificUserModel.getStatus().equals("Approve") && specificUserModel != null && specificUserModel.getUid().equals(FirebaseAuth.getInstance().getUid())){
                            list.add(specificUserModel);
                        }

                    }
                    if(list.size()==0){
                        imageView.setVisibility(View.VISIBLE);
                        text.setVisibility(View.VISIBLE);
                    }
                }
                campHisAdapter.notifyDataSetChanged();
            //    shimmerRecyclerView.hideShimmer();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled if needed
            }
        });
    }
}