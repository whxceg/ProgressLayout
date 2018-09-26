package com.sam.lib.progresslayout.simple;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Toast;

import com.sam.lib.progresslayout.ProgressLayout;
import com.sam.lib.progresslayout.DownloadProgressButton;
import com.sam.lib.progresslayout.SimpleProgressClickListener;

public class MainActivity extends AppCompatActivity {

    private ProgressLayout mProgressLayout;
    private DownloadProgressButton mProgressStateLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgressLayout = (ProgressLayout) findViewById(R.id.pl);
        mProgressStateLayout = (DownloadProgressButton) findViewById(R.id.psl);
        mProgressStateLayout.setOnProgressClickListener(new DownloadProgressButton.OnProgressClickListener() {
            @Override
            public void onStartClick(View view) {
                startDownload();
            }

            @Override
            public void onDownloadClick(View view) {
                Toast.makeText(MainActivity.this, "正在下载", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFinishClick(View view) {
                Toast.makeText(MainActivity.this, "打开", Toast.LENGTH_SHORT).show();
            }
        });

//        mProgressStateLayout.setOnProgressClickListener(new SimpleProgressClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        }, new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        }, new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        }));
    }


    public void start(View view) {
        ObjectAnimator oa = ObjectAnimator.ofInt(mProgressLayout, "progress", 1, 100);
        oa.setDuration(10000);
        oa.setInterpolator(new AccelerateDecelerateInterpolator());
        oa.start();
    }

    private void startDownload() {
        ValueAnimator va = ValueAnimator.ofInt(1, 100);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mProgressStateLayout.setProgress((Integer) animation.getAnimatedValue());
            }
        });
        va.setDuration(10000);
        va.setInterpolator(new AccelerateDecelerateInterpolator());
        va.start();
        va.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressStateLayout.setState(DownloadProgressButton.State.STATE_FINISH);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

    }


}
