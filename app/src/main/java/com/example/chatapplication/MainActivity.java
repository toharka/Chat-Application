package com.example.chatapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

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
                User user = new User(txtUsername.getText().toString(), txtPassword.getText().toString()
                        , txtNicName.getText().toString(), "myprofilepic");
                Call<ResponseBody> call = ApiClient.getInstance().getApiInterface().createUser(user);

                System.out.println("blop");
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            // The request was successful, handle the response
                            System.out.println("Response received");
                            try {
                                if (response.body() != null) {
                                    System.out.println("Response body: " + response.body().string());
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            // The request failed, handle the error
                            System.out.println("Request failed, HTTP status code: " + response.code());
                            try {
                                if (response.errorBody() != null) {
                                    System.out.println("Error body: " + response.errorBody().string());
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        // The request did not even reach the server, handle the failure
                        System.out.println("Request did not reach the server");
                        t.printStackTrace();
                    }
                });
            }
        });

    }
}















