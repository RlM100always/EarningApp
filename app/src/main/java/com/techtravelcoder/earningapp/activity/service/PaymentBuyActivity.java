package com.techtravelcoder.earningapp.activity.service;

import android.annotation.SuppressLint;
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
import com.techtravelcoder.earningapp.adapter.PaymentBuyAdapter;
import com.techtravelcoder.earningapp.model.TrnxModelMain;

import java.util.ArrayList;

public class PaymentBuyActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TrnxModelMain trnxModelMain;
    private PaymentBuyAdapter paymentBuyAdapter;
    private ArrayList<TrnxModelMain> list;
    private DatabaseReference mbase;
    private ImageView imageView;
    private TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_buy);

        int color = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            color = getColor(R.color.red);
        }
        getWindow().setStatusBarColor(color);

        imageView=findViewById(R.id.payment_image_id);
        textView=findViewById(R.id.payment_image_text_id);


        list = new ArrayList<>();
        recyclerView = findViewById(R.id.trnx_recycler_view);
        mbase = FirebaseDatabase.getInstance().getReference("Payment Details");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        paymentBuyAdapter = new PaymentBuyAdapter(this, list);
        recyclerView.setAdapter(paymentBuyAdapter);



        mbase.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SuspiciousIndentation")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               list.clear();

                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        trnxModelMain = dataSnapshot.getValue(TrnxModelMain.class);

                         if(FirebaseAuth.getInstance().getUid().equals(trnxModelMain.getUid()) && trnxModelMain!=null){
                             list.add(trnxModelMain);
                         }
                    }
                    // Move setAdapter inside onDataChange
                    paymentBuyAdapter.notifyDataSetChanged();

                    if(list.size()==0){
                        imageView.setVisibility(View.VISIBLE);
                        textView.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }
}
