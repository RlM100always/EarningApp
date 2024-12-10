package com.techtravelcoder.earningapp.activity.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class VisibilityAlarmReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        String specificUserUID=intent.getStringExtra("userUid");
        //String keyVal=intent.getStringExtra("keys");
        ArrayList<String> keysVal = intent.getStringArrayListExtra("keysList");
        Toast.makeText(context, ""+keysVal.size(), Toast.LENGTH_SHORT).show();


        // Update the visibility for all items in the list
        updateVisibilityForList(context,specificUserUID, keysVal);

      //  Toast.makeText(context, "123456", Toast.LENGTH_SHORT).show();


    }

    private void updateVisibilityForList(Context context,String userUid, ArrayList<String> keysList) {
        for (String key : keysList) {
            // Update visibility for each key
            Toast.makeText(context, ""+key, Toast.LENGTH_SHORT).show();

            updateVisibility(userUid, key);
        }
    }

    private void updateVisibility(String userUid, String keyVal) {

        FirebaseDatabase.getInstance().getReference("Link Details")
                .child(keyVal)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Assuming you have a field named "visibility" to update
                        dataSnapshot.getRef().child("visibility").child(userUid).setValue("visible");
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle error if needed
                    }
                });

    }
}
