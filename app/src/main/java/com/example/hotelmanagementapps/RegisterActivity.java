package com.example.hotelmanagementapps;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    EditText emails,passwords,phones,names;
    Button btnReg, btnBack;
    HotelUser hotel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUserInput();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backLogin();
            }
        });
    }

    private void initView() {
        names = findViewById(R.id.edName);
        passwords = findViewById(R.id.edPass);
        emails = findViewById(R.id.edEmail);
        phones =findViewById(R.id.edPhone);
        btnReg = findViewById(R.id.registerBtn);
        btnBack = findViewById(R.id.backMainBtn);
    }

    private void registerUserInput() {
        String email,password,phone,name;
        name = names.getText().toString();
        password = passwords.getText().toString();
        email = emails.getText().toString();
        phone = phones.getText().toString();
        hotel = new HotelUser(name,password,email,phone);
        insertData();
    }

    private void insertData() {
        class RegisterUser extends AsyncTask<Void,Void,String>{

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(RegisterActivity.this,
                        "Registration","...",false,false);
            }

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("name",hotel.name);
                hashMap.put("password",hotel.password);
                hashMap.put("email",hotel.email);
                hashMap.put("phone",hotel.phone);
                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest
                        ("http://supersensitive-dabs.000webhostapp.com/register.php",hashMap);
                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                if (s.equalsIgnoreCase("success")){
                    Toast.makeText(RegisterActivity.this, "Registration Success", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                    RegisterActivity.this.finish();
                    startActivity(intent);

                }else if (s.equalsIgnoreCase("nodata")){
                    Toast.makeText(RegisterActivity.this, "Please fill in data first", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(RegisterActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                }

            }
        }
        RegisterUser registerUser = new RegisterUser();
        registerUser.execute();
    }

    public void backLogin(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}