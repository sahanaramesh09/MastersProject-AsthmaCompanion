package com.sjsu.respiratoryhelper;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.sjsu.respiratoryhelper.appconfig.BaseHelper;
import com.sjsu.respiratoryhelper.model.UserDetails;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "SignUpActivity";

    EditText etName, etEmail, etPassword, etConfPass;
    Button btSignup;

    Retrofit retrofit;
    DataSourceApi dataSourceApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfPass = findViewById(R.id.etConfPass);

        btSignup = findViewById(R.id.btSignup);

        btSignup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String name, email, password, confpass;
                email = etEmail.getText().toString();
                name = etName.getText().toString();
                password = etPassword.getText().toString();
                confpass = etConfPass.getText().toString();

                if (name.equals("")) {
                    Toast.makeText(SignUpActivity.this, "Name is required", Toast.LENGTH_SHORT).show();
                } else if (email.equals("")) {
                    Toast.makeText(SignUpActivity.this, "Email is required", Toast.LENGTH_SHORT).show();
                } else if (password.equals("")) {
                    Toast.makeText(SignUpActivity.this, "Password is required", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(confpass)) {
                    Toast.makeText(SignUpActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                } else {

                    //Call the api
                    retrofit = new Retrofit.Builder()
                            .baseUrl(BaseHelper.SERVER_PREFIX)
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    dataSourceApi = retrofit.create(DataSourceApi.class);

                    UserDetails userDetails = new UserDetails();
                    userDetails.setUserName(name);
                    userDetails.setEmail(email);
                    userDetails.setPassword(password);
                    Call<UserDetails> call = dataSourceApi.sendPosts(userDetails);
                    call.enqueue(new Callback<UserDetails>() {
                        @Override
                        public void onResponse(Call<UserDetails> call, Response<UserDetails> response) {
                            Toast.makeText(SignUpActivity.this, "Account Created Successfully! Please Login with your email and password ", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(SignUpActivity.this, SignInActivity.class);
                            startActivity(i);
                            finish();
                        }

                        @Override
                        public void onFailure(Call<UserDetails> call, Throwable t) {
                            Toast.makeText(SignUpActivity.this, "Error occurred during Sign Up! Try Again.", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "Error : " + t.toString());
                            Intent signUpIntent = new Intent(SignUpActivity.this, SignUpActivity.class);
                            startActivity(signUpIntent);
                            finish();
                        }
                    });
                }
            }
        });
    }
}
