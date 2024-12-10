package com.techtravelcoder.earningapp.activity.service;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.techtravelcoder.earningapp.R;
import com.techtravelcoder.earningapp.adapter.HomeAdapter;
import com.techtravelcoder.earningapp.adapter.SpecificUserAdapter;
import com.techtravelcoder.earningapp.model.HomeModel;
import com.techtravelcoder.earningapp.model.SpecificUserModel;

import java.util.ArrayList;

public class SpecificUserActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    SpecificUserModel specificUserModel;
    SpecificUserAdapter specificUserAdapter;
    DatabaseReference mbase;
    ArrayList<SpecificUserModel> list ;
    private ImageView img;
    private TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_user);
        int color=0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            color = getColor(R.color.red);
        }
        getWindow().setStatusBarColor(color);

        img=findViewById(R.id.nothing_image_specific);
        tv=findViewById(R.id.nothing_text_id_specific);


        recyclerView=findViewById(R.id.specific_recycler_id);

        mbase = FirebaseDatabase.getInstance().getReference("Specefic User Details").child(FirebaseAuth.getInstance().getUid());

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list=new ArrayList<>();
        specificUserAdapter=new SpecificUserAdapter(this,list);
        recyclerView.setAdapter(specificUserAdapter );

        mbase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();

                if(snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        specificUserModel = dataSnapshot.getValue(SpecificUserModel.class);
                        if(specificUserModel != null){
                            list.add(specificUserModel);
                        }

                    }
                }
                if(list.size()==0){
                    img.setVisibility(View.VISIBLE);
                    tv.setVisibility(View.VISIBLE);
                }

                specificUserAdapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled if needed
            }
        });
    }
}