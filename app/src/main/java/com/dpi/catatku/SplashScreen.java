package com.dpi.catatku;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //untuk menyembunyikan panel aplikasi
        getSupportActionBar().hide();
        setContentView(R.layout.activity_splash_screen);
        long delayMillis;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i =new Intent(SplashScreen.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        }, 3000);//berjalan 3detik
    }
}