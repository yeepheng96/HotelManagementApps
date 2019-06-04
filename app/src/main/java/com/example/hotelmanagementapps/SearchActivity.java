package com.example.hotelmanagementapps;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class SearchActivity extends AppCompatActivity {

    Button bckmainbtn;
    ListView alllvhotel;
    Spinner sploc;
    ArrayList<HashMap<String, String>> allhotellist;
    String email, name, phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        alllvhotel = findViewById(R.id.allList);
        sploc = findViewById(R.id.locationSpn);
        bckmainbtn = findViewById(R.id.backMainBtn);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        email = bundle.getString("email");
        name = bundle.getString("name");
        phone = bundle.getString("phone");
        loadAllHotel(sploc.getSelectedItem().toString());

        bckmainbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToMain();
            }
        });

        alllvhotel.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SearchActivity.this,RoomActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("hotelid",allhotellist.get(position).get("hotelid"));
                bundle.putString("hotelname",allhotellist.get(position).get("hotelname"));
                bundle.putString("location",allhotellist.get(position).get("location"));
                bundle.putString("city",allhotellist.get(position).get("city"));
                bundle.putString("rate",allhotellist.get(position).get("rate"));
                bundle.putString("email",email);
                bundle.putString("name",name);
                bundle.putString("phone",phone);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        sploc.setSelection(0,false);
        sploc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadAllHotel(sploc.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void loadAllHotel(final String loc){
        class LoadAllHotel extends AsyncTask<Void,Void,String>{

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("location",loc);
                RequestHandler rh = new RequestHandler();
                allhotellist = new ArrayList<>();
                String s = rh.sendPostRequest
                        ("http://supersensitive-dabs.000webhostapp.com/load_all_hotel.php",hashMap);
                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                // Toast.makeText(MainActivity.this, s, Toast.LENGTH_LONG).show();
                allhotellist.clear();
                try{
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray hotelarray = jsonObject.getJSONArray("allhotel");
                    for (int i=0;i<hotelarray.length();i++){
                        JSONObject c = hotelarray.getJSONObject(i);
                        String hid = c.getString("hotelid");
                        String hname = c.getString("hotelname");
                        String hloc = c.getString("location");
                        String hcity = c.getString("city");
                        String hrate = c.getString("rate");
                        HashMap<String,String> allhotellisthash = new HashMap<>();
                        allhotellisthash.put("hotelid",hid);
                        allhotellisthash.put("hotelname",hname);
                        allhotellisthash.put("location",hloc);
                        allhotellisthash.put("city",hcity);
                        allhotellisthash.put("rate",hrate);
                        allhotellist.add(allhotellisthash);
                    }
                }catch (final JSONException e){
                    Log.e("JSONERROR",e.toString());
                }

                ListAdapter adapter = new CustomAdapterHotel(
                        SearchActivity.this, allhotellist,
                        R.layout.list_hotel, new String[]
                        {"hotelname","location","city","rate"}, new int[]
                        {R.id.textView70,R.id.textView71,R.id.textView72,R.id.textView31});
                alllvhotel.setAdapter(adapter);
            }
        }
        LoadAllHotel loadAllHotel = new LoadAllHotel();
        loadAllHotel.execute();
    }

    public void backToMain(){
        Intent intent = new Intent(this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("email",email);
        bundle.putString("name",name);
        bundle.putString("phone",phone);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
