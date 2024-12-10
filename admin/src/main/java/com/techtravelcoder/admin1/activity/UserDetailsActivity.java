package com.techtravelcoder.admin1.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.techtravelcoder.admin1.R;
import com.techtravelcoder.admin1.adapter.CampainAdapter;
import com.techtravelcoder.admin1.adapter.UserAdapter;
import com.techtravelcoder.admin1.model.CampainModel;
import com.techtravelcoder.admin1.model.UserModel;

import java.util.ArrayList;
import java.util.List;

public class UserDetailsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    UserAdapter userAdapter;
    UserModel userModel;
    ArrayList<UserModel> list;
    DatabaseReference mbase;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        int color=0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            color = getColor(R.color.red);
        }
        getWindow().setStatusBarColor(color);

        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Loading...");
        progressDialog.setMessage("Wait User Information is Loading...");
        progressDialog.show();


        recyclerView=findViewById(R.id.user_details_recycler_id);

        mbase = FirebaseDatabase.getInstance().getReference("User Information");

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list=new ArrayList<>();
        userAdapter=new UserAdapter(this,list);
        recyclerView.setAdapter(userAdapter );
        mbase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                list.clear();

                if(snapshot.exists()){
                    progressDialog.dismiss();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        userModel = dataSnapshot.getValue(UserModel.class);
                      //  Toast.makeText(getApplicationContext(), "Rakib", Toast.LENGTH_SHORT).show();

                        if(userModel != null){
                            list.add(0,userModel);

                        }
                    }

                }
                userAdapter.notifyDataSetChanged();

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
}