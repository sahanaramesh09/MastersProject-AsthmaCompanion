package com.sjsu.respiratoryhelper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sjsu.respiratoryhelper.appconfig.BaseHelper;

public class SignInActivity extends AppCompatActivity {

    EditText etEmail, etPassword;
    Button btSignin;
    TextView tvSignup;

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
                    String checkValidity = checkForUsers(email);
                    if (checkValidity.equals(password)){
                        Toast.makeText(SignInActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                        //Authentication
                        Intent hp = new Intent(SignInActivity.this, MainActivity.class);
                        startActivity(hp);
                        finish();
                    } else {
                        Toast.makeText(SignInActivity.this, "Incorrect email id or password. Login Failed!", Toast.LENGTH_SHORT).show();
                        Intent ma = new Intent(SignInActivity.this, SignInActivity.class);
                        startActivity(ma);
                        finish();
                    }
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

    private String checkForUsers(String email) {
        SharedPreferences mSharedPreferences = getSharedPreferences(BaseHelper.APP_SHARED_PREF, MODE_PRIVATE);
        return mSharedPreferences.getString(email, "");
    }
}
