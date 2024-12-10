package com.techtravelcoder.earningapp.activity.service;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.techtravelcoder.earningapp.R;

public class ImageActivity extends AppCompatActivity {

    private ImageView imageView ;
    private ProgressBar progressBar;
    PhotoView photoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        progressBar=findViewById(R.id.progress_bar_id);
        photoView=findViewById(R.id.photo_view);
        progressBar.setVisibility(View.VISIBLE);

        imageView=findViewById(R.id.imageview_id);
        String img=getIntent().getStringExtra("imageString");


        Glide.with(this)
                .load(img)
                .into(photoView);

        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);

            }
        },2000);

    }
}