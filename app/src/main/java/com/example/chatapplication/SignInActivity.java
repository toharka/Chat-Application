package com.example.chatapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import Api.ApiClient;
import models.AppDB;
import models.User;
import models.UserDao;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SignInActivity extends AppCompatActivity {

    AppDB db;
    UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        db = Room.databaseBuilder(getApplicationContext(), AppDB.class, "AppDB").build();
        userDao= db.userDao();

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
                Map<String, String> credentials = new HashMap<>();
                credentials.put("username", txtUsername.getText().toString());
                credentials.put("password", txtPassword.getText().toString());


                new Thread(() -> {
                    User user = userDao.findUser(txtUsername.getText().toString(), txtPassword.getText().toString());


                    if (user == null) {
                        runOnUiThread(()->{

                            // Toast or intent navigation here
                            Toast.makeText(SignInActivity.this, "User info incorrect", Toast.LENGTH_SHORT).show();

                        });
                    } else {
                        // Toast for error here
                        // Inside the onResponse() of the sign-in call
                        Call<ResponseBody> call = ApiClient.getInstance().getApiInterface().
                                connection(credentials);
                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if (response.isSuccessful()) {
                                    try {
                                        if (response.code()==200){

                                            // Assume that your server returns the token as a string in the response body
                                            String token = response.body().string();
                                            System.out.printf(token);

                                            // Save the token in SharedPreferences
                                            SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putString("token", token);
                                            editor.apply();
                                            editor.putString("username", txtUsername.getText().toString());
                                            editor.apply();
                                            String tak = sharedPreferences.getString("token", "");
                                            System.out.println(tak);

                                            runOnUiThread(()->{
                                                // Define the intent to launch the target activity
                                                Intent intent = new Intent(SignInActivity.this, Contact.class);

                                                // Start the target activity
                                                startActivity(intent);
                                            });

                                        }
                                        else {
                                            Toast.makeText(SignInActivity.this, "User info incorrect", Toast.LENGTH_SHORT).show();
                                        }

                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    Toast.makeText(SignInActivity.this, "User info incorrect", Toast.LENGTH_SHORT).show();
                                    // Handle unsuccessful response, for example, print the response code
                                    System.out.println("Response Code: " + response.code());
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                // Handle failure
                                t.printStackTrace();
                            }
                        });
                    }
                }).start();

            }

        });

    }
}