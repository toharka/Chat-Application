package com.example.chatapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import Api.ApiClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;




public class SignInActivity extends AppCompatActivity {


    SharedPreferences sharedPreferences = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);


        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
//                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

//                        Log.d(TAG, msg);
                        Toast.makeText(SignInActivity.this, token, Toast.LENGTH_LONG).show();
                    }
                });

            sharedPreferences = getSharedPreferences("night",0);
        Boolean bool = sharedPreferences.getBoolean("night_mode",true);
        if (bool){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }


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
                // Inside the onResponse() of the sign-in call
                Call<ResponseBody> call = ApiClient.getInstance().getApiInterface().
                        connection(credentials);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                if (response.code()==200){

                                    sendRegistrationToServer(txtUsername.getText().toString());

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
                                    // Define the intent to launch the target activity
                                    Intent intent = new Intent(SignInActivity.this, Contact.class);

                                    // Start the target activity
                                    startActivity(intent);
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

        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu settings) {
        getMenuInflater().inflate(R.menu.settings, settings);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            // Start the SettingsActivity
            Intent intent = new Intent(this, SettingsPage.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void sendRegistrationToServer(String username) {
        Map<String, String> deviceDetails = new HashMap<>();
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
//                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("tokenDevice", token);
                        editor.apply();
                        deviceDetails.put("username", username);  // replace with the actual username
                        deviceDetails.put("token", token);

                        Call<ResponseBody> call = ApiClient.getInstance().getApiInterface().sentDeviceToken(deviceDetails);
                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                Log.d("FCM", "Token sent to server");
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Log.e("FCM", "Failed to send token to server", t);
                            }
                        });

                    }
                });








    }

}