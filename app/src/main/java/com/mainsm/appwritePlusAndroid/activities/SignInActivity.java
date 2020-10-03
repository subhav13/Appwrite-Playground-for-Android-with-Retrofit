package com.mainsm.appwritePlusAndroid.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mainsm.appwritePlusAndroid.utils.Constants;
import com.mainsm.appwritePlusAndroid.R;
import com.mainsm.appwritePlusAndroid.model.requestModel.SignInRequestBody;
import com.mainsm.appwritePlusAndroid.model.responseModel.SignInResponse;
import com.mainsm.appwritePlusAndroid.retrofit.RetrofitClient;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity {
    private static final String TAG = SignInActivity.class.getSimpleName();
    EditText email, password;
    Button login;
    TextView createAcc;
    private ProgressBar progressBar;
    private String errorCode;

    @SuppressLint("ObsoleteSdkInt")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        initUi();
        initLogic();
        registerUser();
    }

    private void registerUser() {
        SpannableString spannableString = new SpannableString("Don't have an Appwrite Account yet? Sign Up now!");
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View textView) {
                startActivity(new Intent(SignInActivity.this, RegisterActivity.class));
            }
        };
        spannableString.setSpan(clickableSpan, 36, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(Color.MAGENTA), 36, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        SpannableStringBuilder builder = new SpannableStringBuilder(spannableString);
        createAcc.setText(builder);
        createAcc.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void initLogic() {
        login.setOnClickListener(v -> {
            String e = email.getText().toString().trim();
            String p = password.getText().toString().trim();
            if(!e.isEmpty() && isValidEmail(e)){
                if(!p.isEmpty()) {
                    progressBar.setVisibility(View.VISIBLE);
                    SignInRequestBody requestBody = new SignInRequestBody(e, p);
                    RetrofitClient.getInstance(SignInActivity.this).getApi().loginUser(requestBody)
                            .enqueue(new Callback<SignInResponse>() {
                                @Override
                                public void onResponse(@NotNull Call<SignInResponse> call, @NotNull Response<SignInResponse> response) {
                                    if(response.isSuccessful() && null == response.errorBody() && null != response.body()){
                                        progressBar.setVisibility(View.INVISIBLE);
                                        loginUserAndMoveToNextActivity(response.body());
                                    }else if( null != response.errorBody())
                                        getCodeToShowError(response);
                                }

                                @Override
                                public void onFailure(@NotNull Call<SignInResponse> call, @NotNull Throwable t) {
                                    progressBar.setVisibility(View.INVISIBLE);

                                }
                            });


                }else
                    Toast.makeText(getApplicationContext(), R.string.enter_password, Toast.LENGTH_LONG).show();


            }else
                Toast.makeText(getApplicationContext(), R.string.enter_valid_email, Toast.LENGTH_LONG).show();

        });



    }

    private void getCodeToShowError(Response<SignInResponse> response) {
        progressBar.setVisibility(View.INVISIBLE);
        try {
            assert response.errorBody() != null;
            String responseCode = response.errorBody().string();
            Log.e(TAG, "getCode: RESPONSE STRING " + responseCode);
            JSONObject errorBody = new JSONObject(responseCode);
            errorCode = errorBody.getString("code");
            Log.e(TAG, "getCode: ERROR CODE " + errorCode );
        } catch (IOException | JSONException ex) {
            ex.printStackTrace();
        }
        if(!errorCode.isEmpty())
            handleToastForErrors(Integer.parseInt(errorCode));
    }

    private void handleToastForErrors(int code) {
        progressBar.setVisibility(View.INVISIBLE);
        if( code == 401)
            Toast.makeText(getApplicationContext(), R.string.invalid_cred, Toast.LENGTH_LONG).show();
        else
            Toast.makeText(getApplicationContext(), R.string.went_wrong, Toast.LENGTH_LONG).show();

    }

    private void loginUserAndMoveToNextActivity(SignInResponse body) {
        SharedPreferences sharedPreferences;
        SharedPreferences.Editor editor;
        sharedPreferences = SignInActivity.this.getSharedPreferences(Constants.PROJECT_ID, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString(Constants.LOGIN_TOKEN, body.getId()).apply();
        editor.putInt(Constants.EXPIRE, body.getExpire()).apply();
        editor.putBoolean(Constants.IS_LOGGED_IN, true).apply();
        startActivity(new Intent(SignInActivity.this, HomeActivity.class));
        finish();

    }

    private boolean isValidEmail(String e) {
        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                                       + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                                       + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                                       + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                                       + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                                       + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(e).matches();
    }

    private void initUi() {
        email = findViewById(R.id.enterEmail);
        password = findViewById(R.id.enterPassword);
        login = findViewById(R.id.loginBtn);
        progressBar = findViewById(R.id.progress_bar);
        createAcc = findViewById(R.id.sign_up);

    }
}