package com.techtravelcoder.earningapp.activity.ads;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.startapp.sdk.ads.banner.Banner;
import com.techtravelcoder.earningapp.R;

public class AnsActivity extends AppCompatActivity {

    Banner banner;
    private ImageView img;
    private TextView tv1,tv2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ans);

        banner=findViewById(R.id.startAppBannerAns);
        img=findViewById(R.id.ans_imgview_id);
        tv1=findViewById(R.id.ans_cing_id);
        tv2=findViewById(R.id.ans_point_id);

        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tv1.setVisibility(View.VISIBLE);
                img.setVisibility(View.VISIBLE);
                tv2.setVisibility(View.VISIBLE);

            }
        },4000);

        banner.loadAd();
    }
}