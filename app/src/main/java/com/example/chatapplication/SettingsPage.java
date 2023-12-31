package com.example.chatapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;

import java.net.MalformedURLException;
import java.net.URL;

import Api.ApiClient;

public class SettingsPage extends AppCompatActivity {


    SharedPreferences sharedPreferences = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_page);

        EditText txtServerAddress = findViewById(R.id.txtServerAddress);
        txtServerAddress.setHint("Current Server Address:\n" + ApiClient.getBaseUrl());


        Button btnServerAdd = findViewById(R.id.btnServerAdd);
        btnServerAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isValidBaseUrl(txtServerAddress.getText().toString())){
                    ApiClient.setBaseUrl(txtServerAddress.getText().toString());
                    txtServerAddress.setHint("Current Server Address:\n" + ApiClient.getBaseUrl());
                    txtServerAddress.setText("");
                    // Create a LayoutInflater object
                    LayoutInflater inflater = getLayoutInflater();

// Inflate the custom toast layout
                    View layout = inflater.inflate(R.layout.toast_loading,
                            findViewById(R.id.custom_toast_container));

// Create a new Toast and set the custom layout
                    Toast toast = new Toast(getApplicationContext());
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.setView(layout);

// Show the toast
                    toast.show();

                    // Create and start the intent after the delay
                    Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();

                }

                else {
                    Toast.makeText(SettingsPage.this, "Invalid Server Address!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        SwitchCompat nightModeSwitch;
        nightModeSwitch = findViewById(R.id.switch1);

        sharedPreferences = getSharedPreferences("night", 0);
        Boolean bool = sharedPreferences.getBoolean("night_mode", true);
        if (bool) {
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
                    editor.putBoolean("night_mode", true);
                    editor.commit();
                    return;
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    nightModeSwitch.setChecked(false);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("night_mode", false);
                    editor.commit();
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu settings) {
        getMenuInflater().inflate(R.menu.back, settings);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.last_act) {
            // Return to the previous activity without creating a new instance of SettingsPage

            // Call finish() to return to the previous activity
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean isValidBaseUrl(String baseUrl) {
        try {
            URL url = new URL(baseUrl);
            // Additional validation can be performed if needed
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }


}
