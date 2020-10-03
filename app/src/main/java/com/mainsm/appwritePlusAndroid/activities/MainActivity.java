package com.mainsm.appwritePlusAndroid.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.mainsm.appwritePlusAndroid.utils.Constants;
import com.mainsm.appwritePlusAndroid.R;

public class MainActivity extends AppCompatActivity {
    private boolean isLoggedIn;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = this.getSharedPreferences(Constants.PROJECT_ID, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        isLoggedIn = sharedPreferences.getBoolean(Constants.IS_LOGGED_IN, false);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(isLoggedIn)
                    startActivity(new Intent(MainActivity.this, HomeActivity.class));
                else
                    startActivity(new Intent(MainActivity.this, SignInActivity.class));

                finish();
            }
        }, 1500);
    }
}