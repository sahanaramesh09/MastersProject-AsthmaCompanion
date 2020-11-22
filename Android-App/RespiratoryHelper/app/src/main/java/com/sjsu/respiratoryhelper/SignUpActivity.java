package com.sjsu.respiratoryhelper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.sjsu.respiratoryhelper.appconfig.BaseHelper;

public class SignUpActivity extends AppCompatActivity {

    EditText etName, etEmail, etPassword, etConfPass;
    Button btSignup;

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
                    Toast.makeText(SignUpActivity.this, "Account Created Successfully! Please Login with your email and password ", Toast.LENGTH_LONG).show();
                    setSampleUsers();

                    Intent i = new Intent(SignUpActivity.this, SignInActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });
    }

    private void setSampleUsers() {
        SharedPreferences mSharedPreferences = getSharedPreferences(BaseHelper.APP_SHARED_PREF, MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putString("sahana@gmail.com", "sahana");
        mEditor.putString("nithya@gmail.com", "nithya");
        mEditor.putString("kavya@gmail.com", "kavya");
        mEditor.putString("chandra@gmail.com", "chandra");
        mEditor.apply();
    }
}
