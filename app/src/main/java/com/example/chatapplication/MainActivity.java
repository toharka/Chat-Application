package com.example.chatapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import Api.ApiClient;
import models.User;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
                    System.out.println("blop1");
                }
//                else if () {
//                    //user exist
//                }
                User user = new User("aaa", "1234", "mydisplayName", "myprofilepic");
                Call<ResponseBody> call = ApiClient.getInstance().getApiInterface().createUser(user);

                System.out.println("blop");
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        // Handle response
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        // Handle failure
                    }

                });
            }

        });
    }
}















