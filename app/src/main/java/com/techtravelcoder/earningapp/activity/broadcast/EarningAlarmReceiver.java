package com.techtravelcoder.earningapp.activity.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

public class EarningAlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context, "2222", Toast.LENGTH_SHORT).show();

        String specificUserUID=intent.getStringExtra("userUid");

        FirebaseDatabase.getInstance().getReference("Control").child("StartIo").
                child("user").child(specificUserUID).
                child("visibility").setValue(true);
    }
}
