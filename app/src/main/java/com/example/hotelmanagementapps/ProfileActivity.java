package com.example.hotelmanagementapps;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity{
    private TextView tvname,tvemail,tvphone;
    private String email, name, phone;
    private ImageView image;
    Button edit, back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        initialView();
        email = bundle.getString("email");
        name = bundle.getString("name");
        phone = bundle.getString("phone");
        edit = findViewById(R.id.editBtn);
        back = findViewById(R.id.backMainBtn);
        tvemail.setText(email);
        Picasso.with(ProfileActivity.this)
                .load("http://supersensitive-dabs.000webhostapp.com/profilepicture/"+email+".jpg")
                .resize(400, 400)
                .error(R.mipmap.profile)
                .into(image);
        loadprofile(email);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEditProfile();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backMain();
            }
        });
    }

    private void loadprofile(final String email) {
        class LoadProfile extends AsyncTask<Void,Void,String>{

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("email",email);
                RequestHandler requestHandler = new RequestHandler();
                String s = requestHandler.sendPostRequest("http://supersensitive-dabs.000webhostapp.com/loaduser.php",hashMap);
                return s;
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray restarray = jsonObject.getJSONArray("hoteluser");
                    for(int i=0; i<restarray.length(); i++){
                        JSONObject c = restarray.getJSONObject(i);
                        name = c.getString("name");
                        phone = c.getString("phone");
                    }
                }catch(JSONException e){
                    Toast.makeText(ProfileActivity.this, "Retrieve data failed!", Toast.LENGTH_SHORT).show();
                }
            }
        }
        tvname.setText(name);
        tvphone.setText(phone);
        LoadProfile loadProfile = new LoadProfile();
        loadProfile.execute();
    }

    private void initialView() {
        image=findViewById(R.id.myprofile);
        tvname=findViewById(R.id.nameInfo);
        tvemail=findViewById(R.id.emailInfo);
        tvphone=findViewById(R.id.phoneInfo);
    }

    public void openEditProfile(){
        Intent intent = new Intent(this, EditProfileActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("email",email);
        bundle.putString("name",name);
        bundle.putString("phone",phone);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void backMain(){
        Intent intent = new Intent(this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("email",email);
        bundle.putString("name",name);
        bundle.putString("phone",phone);
        intent.putExtras(bundle);
        startActivity(intent);
    }

}