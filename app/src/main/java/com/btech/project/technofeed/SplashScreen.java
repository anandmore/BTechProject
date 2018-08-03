package com.btech.project.technofeed;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.btech.project.technofeed.view.MainActivity;

public class SplashScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final ImageView imageView = (ImageView) findViewById(R.id.splash_image);
        final Animation animation_1 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.fade_in);
        final Animation animation_2 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.rotate);
        final Animation animation_3 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.antirotate);

        imageView.startAnimation(animation_1);
        animation_1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                imageView.startAnimation(animation_2);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        animation_2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                imageView.startAnimation(animation_3);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        animation_3.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(getBaseContext(), MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                }, 0);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
