package com.example.chatapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;

import Api.ApiClient;

public class SettingsPage extends AppCompatActivity {

    private SwitchCompat nightModeSwitch;
    SharedPreferences sharedPreferences = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_page);

        EditText txtServerAddress = findViewById(R.id.txtServerAddress);
        txtServerAddress.setHint("Current Server Address:\n"+ ApiClient.getBaseUrl());

        @SuppressLint("UseButton")
        Button btnServerAdd = findViewById(R.id.btnServerAdd);
        btnServerAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiClient.setBaseUrl(txtServerAddress.getText().toString());
                txtServerAddress.setHint("Current Server Address:\n"+ ApiClient.getBaseUrl());
                txtServerAddress.setText("");
                // Create a LayoutInflater object
                LayoutInflater inflater = getLayoutInflater();

// Inflate the custom toast layout
                View layout = inflater.inflate(R.layout.toast_loading,
                findViewById(R.id.custom_toast_container));

// Create a new Toast and set the custom layout
                Toast toast = new Toast(getApplicationContext());
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(layout);

// Show the toast
                toast.show();


                Intent intent = new Intent(SettingsPage.this, SignInActivity.class);

                // Start the target activity
                startActivity(intent);


            }
        });

        nightModeSwitch = findViewById(R.id.switch1);

        sharedPreferences = getSharedPreferences("night",0);
        Boolean bool = sharedPreferences.getBoolean("night_mode",true);
        if (bool){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            nightModeSwitch.setChecked(true);
        }

        nightModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    nightModeSwitch.setChecked(true);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("night_mode",true);
                    editor.commit();
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    nightModeSwitch.setChecked(false);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("night_mode",false);
                    editor.commit();
                }
            }
        });
    }

}


