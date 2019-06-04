package com.example.hotelmanagementapps;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;


public class LoginActivity extends AppCompatActivity {
    TextView tvforgot;
    EditText emails,passwords;
    Button login, register;
    SharedPreferences sharedPreferences;
    CheckBox cbrem;
    Dialog dialogforgotpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emails = findViewById(R.id.emailEdit);
        passwords = findViewById(R.id.passEdit);
        login = findViewById(R.id.loginBtn);
        register = findViewById(R.id.registerBtn);
        cbrem = findViewById(R.id.checkBox);
        tvforgot = findViewById(R.id.fgtpass);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegister();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emails.getText().toString();
                String password = passwords.getText().toString();
                loginUser(email,password);
                savePref(email,password);
            }
        });
        cbrem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cbrem.isChecked()){
                    String email = emails.getText().toString();
                    String password = passwords.getText().toString();
                }
            }
        });
        tvforgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotPassDialog();
            }
        });
        loadPref();
    }

    private void savePref(String email, String password){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email",email);
        editor.putString("password",password);
        editor.commit();
        Toast.makeText(this,"Your email and password has been saved",Toast.LENGTH_SHORT).show();
    }

    private void loadPref() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String preuser = sharedPreferences.getString("email", "");
        String prepass = sharedPreferences.getString("password", "");
        if (preuser.length()>0){
            cbrem.setChecked(true);
            emails.setText(preuser);
            passwords.setText(prepass);
        }
    }

    private void loginUser(final String email, final String password) {
        class LoginUser extends AsyncTask<Void,Void,String>{
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(LoginActivity.this,
                        "Login user","...",false,false);
            }
            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("email",email);
                hashMap.put("password",password);
                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest
                        ("http://supersensitive-dabs.000webhostapp.com/logins.php",hashMap);
                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                if(s.length()>7){
                    Toast.makeText(LoginActivity.this, s, Toast.LENGTH_LONG).show();
                    String[] val = s.split(",");
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("email",email);
                    bundle.putString("name", val[0]);
                    bundle.putString("phone", val[1]);
                    intent.putExtras(bundle);
                    verifySQLite(email,password);
                    startActivity(intent);
                }else{
                    Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_LONG).show();
                }
            }
        }
        LoginUser loginUser = new LoginUser();
        loginUser.execute();
    }

    public void openRegister(){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void verifySQLite(String email, String password){
        SharedPreferenceConfig.saveEmail(email, this);
        SharedPreferenceConfig.savePassword(password, this);
        finish();
    }

    void forgotPassDialog(){
        dialogforgotpass = new Dialog(this,android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth);
        dialogforgotpass.setContentView(R.layout.forgot_password);
        dialogforgotpass.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        final EditText edmail = dialogforgotpass.findViewById(R.id.edtfgtpass);
        Button btnsend = dialogforgotpass.findViewById(R.id.btnfgtpass);
        btnsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String forgotemail = edmail.getText().toString();
                sendPassword(forgotemail);
            }
        });
        dialogforgotpass.show();
    }

    private void sendPassword(final String forgotemail){
        class SendPassword extends AsyncTask<Void,String,String>{
            @Override
            protected String doInBackground(Void...voids){
                HashMap<String,String> hashMap = new HashMap();
                hashMap.put("email",forgotemail);
                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest("http://supersensitive-dabs.000webhostapp.com/verification_email.php",hashMap);
                return s;
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (s.equalsIgnoreCase("success")){
                    Toast.makeText(LoginActivity.this, "Success. Check your email", Toast.LENGTH_LONG).show();
                    dialogforgotpass.dismiss();
                }else{
                    Toast.makeText(LoginActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        }
        SendPassword sendPassword = new SendPassword();
        sendPassword.execute();
    }
}