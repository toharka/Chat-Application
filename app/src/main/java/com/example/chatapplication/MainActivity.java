package com.example.chatapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView txtAlreadyHave = findViewById(R.id.txtAlreadyHave);
        txtAlreadyHave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Define the intent to launch the target activity
                Intent intent = new Intent(MainActivity.this, SignInActivity.class);

                // Start the target activity
                startActivity(intent);
            }
        });

        EditText txtUsername = findViewById(R.id.txtUsername);
        EditText txtPassword = findViewById(R.id.txtPassword);
        EditText txtPasswordConf = findViewById(R.id.txtPasswordConf);
        EditText txtNicName = findViewById(R.id.txtNicName);
        Button btnSignup = findViewById(R.id.btnSignup);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtUsername.getText().toString().isEmpty() ||
                        txtNicName.getText().toString().isEmpty() ||
                        txtPassword.getText().toString().isEmpty() ||
                        txtPasswordConf.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "invalid input", Toast.LENGTH_SHORT).show();
                } else if (!txtPassword.getText().toString()
                        .equals(txtPasswordConf.getText().toString())) {
                    Toast.makeText(MainActivity.this, "password confirmation unsuccessful", Toast.LENGTH_SHORT).show();
                }
//                else if () {
//                    //user exist
//                }
            }
        });

    }


}
















