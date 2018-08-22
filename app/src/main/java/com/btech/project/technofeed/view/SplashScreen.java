package com.btech.project.technofeed.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.btech.project.technofeed.R;

public class SplashScreen extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
                                      @Override
                                      public void run() {
                                          Intent i = new Intent(getBaseContext(), LoginActivity.class);
                                          startActivity(i);
                                          finish();
                                      }
                                  }
                , 1500);
    }
}