package com.example.chatapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        TextView txtAlreadyHave = findViewById(R.id.txtClickSignUp);
        txtAlreadyHave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Define the intent to launch the target activity
                Intent intent = new Intent(SignInActivity.this, MainActivity.class);

                // Start the target activity
                startActivity(intent);

            }
        });

        EditText txtUsername = findViewById(R.id.txtUsername);
        EditText txtPassword= findViewById(R.id.txtPassword);
        Button btnSignin = findViewById(R.id.btnSignin);

        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtUsername.getText().toString().isEmpty() ||
                        txtPassword.getText().toString().isEmpty()) {
                    Toast.makeText(SignInActivity.this, "invalid input", Toast.LENGTH_SHORT).show();
                }

            }

        });

    }
}