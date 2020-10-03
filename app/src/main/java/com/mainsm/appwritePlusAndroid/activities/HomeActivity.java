package com.mainsm.appwritePlusAndroid.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mainsm.appwritePlusAndroid.utils.Constants;
import com.mainsm.appwritePlusAndroid.R;
import com.mainsm.appwritePlusAndroid.retrofit.RetrofitClient;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = HomeActivity.class.getSimpleName();
    private Button logout, logoutAll;
    private ProgressBar progressBar;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initUi();
        initLogic();
        showPopup();

    }

    private void showPopup() {
        Dialog dialog = new Dialog(HomeActivity.this);
        dialog.setContentView(R.layout.congratrs_dialog);
        ImageView close = dialog.findViewById(R.id.close);
        close.setOnClickListener(v -> dialog.dismiss());
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        dialog.setCancelable(false);
    }

    private void initLogic() {
        logout.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            editor.clear();
            editor.apply();
            startActivity(new Intent(HomeActivity.this, MainActivity.class));
            Toast.makeText(getApplicationContext(), R.string.logged_out, Toast.LENGTH_LONG).show();
            progressBar.setVisibility(View.INVISIBLE);

            finish();
        });

        logoutAll.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            logoutFromAllDevice();
        });
    }

    private void logoutFromAllDevice() {
        String token = PreferenceManager.getDefaultSharedPreferences(HomeActivity.this).getString(Constants.LOGIN_TOKEN, "");
        String url = Constants.BASE_URL + "account/sessions/" + "current";
        RetrofitClient.getInstance(HomeActivity.this).getApi().deleteUserSession(url)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            progressBar.setVisibility(View.INVISIBLE);
                            Log.e(TAG, "onResponse: "+ response.body() + response.message() + response.toString() );
                            editor.clear();
                            editor.apply();
                            startActivity(new Intent(HomeActivity.this, MainActivity.class));
                            Toast.makeText(getApplicationContext(), R.string.logged_out, Toast.LENGTH_LONG).show();
                            finish();
                        }else {
                            Toast.makeText(getApplicationContext(), R.string.went_wrong, Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.INVISIBLE);
                            try {
                                if (response.errorBody() != null) {
                                    Log.e(TAG, "onResponse: " + response.errorBody().string() );
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                    }

                    @Override
                    public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                        Log.e(TAG, "onFailure: " + t.getMessage() );
                        Toast.makeText(getApplicationContext(), R.string.went_wrong, Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
    }

    private void initUi() {
        sharedPreferences = getSharedPreferences(Constants.PROJECT_ID, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        logout = findViewById(R.id.logout);
        logoutAll = findViewById(R.id.logout_all);
        progressBar = findViewById(R.id.progress_bar);
    }
}