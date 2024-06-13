package umg.student.project;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import umg.student.project.AdminActivity.AdminMainActivity;
import umg.student.project.Utils.SessionManager;


public class SplashScreenActivity extends AppCompatActivity {
    private static final int splashTimeOut = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        setSplashScreen();
    }



    private void setSplashScreen()
    {
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        if (SessionManager.isOnline(this))
        {
            new Handler().postDelayed(() -> {
                Intent loginIntent = new Intent(SplashScreenActivity.this, WelcomeActivity.class);
                startActivity(loginIntent);
                finish();
            }, splashTimeOut);
        }
        else {
            Toast.makeText(SplashScreenActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}