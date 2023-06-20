package com.example.chatapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import Api.ApiClient;
import models.AppDB;
import models.User;
import models.UserDao;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    AppDB db;
    UserDao userDao;
    private static final int GALLERY_REQ_CODE = 1;
    ImageView  ProfilePic;
    Uri profilePicUrl = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = Room.databaseBuilder(getApplicationContext(), AppDB.class, "AppDB").build();
        userDao= db.userDao();

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

        ProfilePic = findViewById(R.id.ProfilePic);
        Button selectButton = findViewById(R.id.selectButton);

        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK);
                gallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery,GALLERY_REQ_CODE);
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
                    Toast.makeText(MainActivity.this, "user information incomplete", Toast.LENGTH_SHORT).show();
                } else if (!txtPassword.getText().toString()
                        .equals(txtPasswordConf.getText().toString())) {
                    Toast.makeText(MainActivity.this, "password confirmation unsuccessful", Toast.LENGTH_SHORT).show();
                } else if (profilePicUrl == null) {
                    Toast.makeText(MainActivity.this, "please upload profile picture", Toast.LENGTH_SHORT).show();
                }
                else {
                    User user = new User(txtUsername.getText().toString(), txtPassword.getText().toString()
                            , txtNicName.getText().toString(),toBase64(profilePicUrl)) ;
                    Call<ResponseBody> call = ApiClient.getInstance().getApiInterface().createUser(user);

                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()) {


                                new Thread(()->{
                                    userDao.insert(user);
                                    runOnUiThread(()->{

                                        Intent intent = new Intent(MainActivity.this, SignInActivity.class);

                                        // Start the target activity
                                        startActivity(intent);

                                    });
                                }).start();
                                // The request was successful, handle the response
//                                System.out.println("Response received");
//                                try {
//                                    if (response.body() != null) {
//                                        if (response.code()==200){
//                                            // Define the intent to launch the target activity
//
//
//                                        }
//                                        else {
//                                            Toast.makeText(MainActivity.this, "username is taken", Toast.LENGTH_SHORT).show();
//                                        }
//                                        System.out.println("Response body: " + response.body().string());
//                                    }
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
                            } else {
                                Toast.makeText(MainActivity.this, "username is taken", Toast.LENGTH_SHORT).show();
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


            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){

        super.onActivityResult(requestCode,resultCode,data);

        if (requestCode == GALLERY_REQ_CODE && resultCode==RESULT_OK){
            profilePicUrl = data.getData();
            ProfilePic.setImageURI(profilePicUrl);
            Log.i("photourl", String.valueOf(profilePicUrl));
        }

    }

    private String toBase64(Uri profilePicUrl){
        InputStream inputStream = null;
        try {
            inputStream = getContentResolver().openInputStream(profilePicUrl);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while (true) {
            try {
                if (!((bytesRead = inputStream.read(buffer)) != -1)) break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            byteArrayOutputStream.write(buffer, 0, bytesRead);
        }
        byte[] imageBytes = byteArrayOutputStream.toByteArray();

        String base64Image = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return base64Image;
    }
}

















