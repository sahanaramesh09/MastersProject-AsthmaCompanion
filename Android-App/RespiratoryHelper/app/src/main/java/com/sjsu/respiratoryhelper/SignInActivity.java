package com.sjsu.respiratoryhelper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sjsu.respiratoryhelper.appconfig.BaseHelper;
import com.sjsu.respiratoryhelper.model.UserDetails;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignInActivity extends AppCompatActivity {

    EditText etEmail, etPassword;
    Button btSignin;
    TextView tvSignup;

    Retrofit retrofit;
    DataSourceApi dataSourceApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        btSignin = findViewById(R.id.btSignin);
        tvSignup = findViewById(R.id.tvSignup);


        btSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, password;

                email = etEmail.getText().toString();
                password = etPassword.getText().toString();

                if (email.equals("")) {
                    Toast.makeText(SignInActivity.this, "Email Required", Toast.LENGTH_SHORT).show();
                } else if (password.equals("")) {
                    Toast.makeText(SignInActivity.this, "Password required", Toast.LENGTH_SHORT).show();
                } else {
                    retrofit = new Retrofit.Builder()
                            .baseUrl(BaseHelper.SERVER_PREFIX)
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    dataSourceApi = retrofit.create(DataSourceApi.class);
                    UserDetails userDetails = new UserDetails();
                    userDetails.setEmail(email);
                    userDetails.setPassword(password);
                    Call<UserDetails> call = dataSourceApi.login(userDetails);
                    call.enqueue(new Callback<UserDetails>() {
                        @Override
                        public void onResponse(Call<UserDetails> call, Response<UserDetails> response) {
                            Toast.makeText(SignInActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                            Intent loginIntent = new Intent(SignInActivity.this, MainActivity.class);
                            startActivity(loginIntent);
                            finish();
                        }

                        @Override
                        public void onFailure(Call<UserDetails> call, Throwable t) {
                            Toast.makeText(SignInActivity.this, "Incorrect Email Id or Password. Login Failed!", Toast.LENGTH_SHORT).show();
                            Intent loginIntent = new Intent(SignInActivity.this, SignInActivity.class);
                            startActivity(loginIntent);
                            finish();
                        }
                    });
                }
            }
        });

        tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(i);
                finish();

            }
        });

    }
}
