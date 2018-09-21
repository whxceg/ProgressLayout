package com.sam.lib.progresslayout.simple;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.sam.lib.progresslayout.ProgressLayout;

public class MainActivity extends AppCompatActivity {

    private ProgressLayout mProgressLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgressLayout = findViewById(R.id.pl);
    }


    public void start(View view){
        ObjectAnimator oa = ObjectAnimator.ofInt(mProgressLayout,"progress",1,100);
        oa.setDuration(10000);
        oa.setInterpolator(new AccelerateDecelerateInterpolator());
        oa.start();
    }
}
