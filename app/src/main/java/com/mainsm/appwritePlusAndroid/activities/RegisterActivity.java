package com.mainsm.appwritePlusAndroid.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mainsm.appwritePlusAndroid.R;
import com.mainsm.appwritePlusAndroid.model.requestModel.RegisterUserRequestBody;
import com.mainsm.appwritePlusAndroid.model.responseModel.RegisterUserResponse;
import com.mainsm.appwritePlusAndroid.retrofit.RetrofitClient;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = RegisterActivity.class.getSimpleName();

    private EditText name, email, password;
    private Button signUp;
    private ProgressBar progressBar;
    private String errorCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initUi();
        initLogic();


    }

    private void initLogic() {
        signUp.setOnClickListener(v -> {
            String n = name.getText().toString().trim();
            String e = email.getText().toString().trim();
            String p = password.getText().toString().trim();
            if(!e.isEmpty() && isValidEmail(e)){
                if(!p.isEmpty()) {
                    if(!n.isEmpty()){
                        progressBar.setVisibility(View.VISIBLE);
                        RegisterUserRequestBody requestBody = new RegisterUserRequestBody(n, e, p);
                        RetrofitClient.getInstance(RegisterActivity.this).getApi().registerUser(requestBody)
                                .enqueue(new Callback<RegisterUserResponse>() {
                                    @Override
                                    public void onResponse(@NotNull Call<RegisterUserResponse> call, @NotNull Response<RegisterUserResponse> response) {
                                        if(response.isSuccessful() && null == response.errorBody() && null != response.body()) {
                                            progressBar.setVisibility(View.INVISIBLE);
                                            startActivity(new Intent(RegisterActivity.this, SignInActivity.class));
                                            Toast.makeText(getApplicationContext(), R.string.acc_created, Toast.LENGTH_LONG).show();
                                            finish();
                                        }else if( null != response.errorBody())
                                            getCodeToShowError(response);

                                    }

                                    @Override
                                    public void onFailure(@NotNull Call<RegisterUserResponse> call, @NotNull Throwable t) {
                                        progressBar.setVisibility(View.INVISIBLE);
                                    }
                                });
                    }else
                        Toast.makeText(getApplicationContext(), R.string.enter_name, Toast.LENGTH_LONG).show();


                }else
                    Toast.makeText(getApplicationContext(), R.string.enter_password, Toast.LENGTH_LONG).show();


            }else
                Toast.makeText(getApplicationContext(), R.string.enter_valid_email, Toast.LENGTH_LONG).show();



        });
    }

    private void getCodeToShowError(Response<RegisterUserResponse> response) {
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
        if( code == 409)
            Toast.makeText(getApplicationContext(), R.string.acc_exist, Toast.LENGTH_LONG).show();
        else
            Toast.makeText(getApplicationContext(), R.string.went_wrong, Toast.LENGTH_LONG).show();


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
        name = findViewById(R.id.enterName);
        signUp = findViewById(R.id.signUpBtn);
        progressBar = findViewById(R.id.progress_bar);
    }
}