package com.example.hotelmanagementapps;

import android.app.AlertDialog;
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
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class EditProfileActivity extends AppCompatActivity {

    EditText names, phones;
    TextView emails;
    Button btnback;
    ImageView image;
    private String emailed, named, phoned;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        initView();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        emailed = bundle.getString("email");
        phoned = bundle.getString("phone");
        named = bundle.getString("name");
        btnback = findViewById(R.id.backMainBtn);
        emails.setText(emailed);
        Picasso.with(this)
                .load(R.mipmap.profile)
                .resize(400, 400)
                .into(image);
        btnback.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                backProfile();
            }
        });
    }

    public void updateUserInput(View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder
                .setMessage("Are you sure to update?")
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        check();
                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void check() {
        String name, phone;
        name = names.getText().toString();
        phone = phones.getText().toString();
        if (name.equals("")||phone.equals("")){
            Toast.makeText(this, "Please fill in all column", Toast.LENGTH_SHORT).show();
        }else {
            try{
                updateUser(emailed,name,phone);
            }catch (Exception ex){
                Toast.makeText(this, "Update Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updateUser(final String email, final String name, final String phone) {
        class UpdateUser extends AsyncTask<Void,Void,String> {

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("email",email);
                hashMap.put("name",name);
                hashMap.put("phone",phone);
                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest
                        ("http://supersensitive-dabs.000webhostapp.com/editprofile.php",hashMap);
                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (s.equalsIgnoreCase("success")) {
                    Toast.makeText(EditProfileActivity.this, "Update Success", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
                    EditProfileActivity.this.finish();
                    Bundle bundle = new Bundle();
                    bundle.putString("email",email);
                    bundle.putString("name",name);
                    bundle.putString("phone",phone);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }else{
                    Toast.makeText(EditProfileActivity.this, "Update Failed", Toast.LENGTH_SHORT).show();
                }
            }
        }

        UpdateUser updateUser = new UpdateUser();
        updateUser.execute();
    }

    public void backProfile(){
        Intent intent = new Intent(this, ProfileActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("email",emailed);
        bundle.putString("name",named);
        bundle.putString("phone",phoned);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void initView(){
        names = findViewById(R.id.edname);
        phones = findViewById(R.id.edphone);
        emails = findViewById(R.id.fixedEmail);
        image = findViewById(R.id.updateprofile);
    }
}
