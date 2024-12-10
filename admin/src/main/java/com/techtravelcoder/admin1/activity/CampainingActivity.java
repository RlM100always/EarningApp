package com.techtravelcoder.admin1.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.techtravelcoder.admin1.R;
import com.techtravelcoder.admin1.adapter.CampainAdapter;
import com.techtravelcoder.admin1.model.CampainModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CampainingActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private CampainAdapter campainAdapter;
    private List<CampainModel> list;
    private DatabaseReference mbase;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campaining);


        int color=0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            color = getColor(R.color.red);
        }
        getWindow().setStatusBarColor(color);

        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Loading...");
        progressDialog.setMessage("Wait User Campaigning Details is Loading...");
        progressDialog.show();

        recyclerView=findViewById(R.id.recycler_id);

        mbase = FirebaseDatabase.getInstance().getReference("Link Details");

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list=new ArrayList<>();
        campainAdapter=new CampainAdapter(this,list);
        recyclerView.setAdapter(campainAdapter );

        mbase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                list.clear();
                CampainModel campainModel;
                if(snapshot.exists()){
                    progressDialog.dismiss();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        campainModel = dataSnapshot.getValue(CampainModel.class);

                        if(campainModel != null){

                            list.add(campainModel);

                        }

                    }

                }
                campainAdapter.notifyDataSetChanged();

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}