package com.example.hotelmanagementapps;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity {

    Button search;
    String email, name, phone;
    ListView lvhotels;
    Spinner sloc;
    ArrayList<HashMap<String, String>> hotellist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        search = findViewById(R.id.searchBtn);
        lvhotels = findViewById(R.id.lvhotel);
        sloc = findViewById(R.id.sploc);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        email = bundle.getString("email");
        name = bundle.getString("name");
        phone = bundle.getString("phone");
        loadHotel(sloc.getSelectedItem().toString(),email);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSearch();
            }
        });

        lvhotels.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this,HotelActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("invoiceid",hotellist.get(position).get("invoiceid"));
                bundle.putString("roomid",hotellist.get(position).get("roomid"));
                bundle.putString("hotelid",hotellist.get(position).get("hotelid"));
                bundle.putString("hotelname",hotellist.get(position).get("hotelname"));
                bundle.putString("roomname",hotellist.get(position).get("roomname"));
                bundle.putString("roomtype",hotellist.get(position).get("roomtype"));
                bundle.putString("location",hotellist.get(position).get("location"));
                bundle.putString("startdate",hotellist.get(position).get("startdate"));
                bundle.putString("enddate",hotellist.get(position).get("enddate"));
                bundle.putString("starttime",hotellist.get(position).get("starttime"));
                bundle.putString("endtime",hotellist.get(position).get("endtime"));
                bundle.putString("email",email);
                bundle.putString("name",name);
                bundle.putString("phone",phone);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        sloc.setSelection(0,false);
        sloc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadHotel(sloc.getSelectedItem().toString(),email);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_things,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.myprofile:
                Toast.makeText(this,"My Profile", Toast.LENGTH_SHORT).show();
                openProfile();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void openProfile(){
        Intent intent = new Intent(this, ProfileActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("email",email);
        bundle.putString("name",name);
        bundle.putString("phone",phone);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void openSearch(){
        Intent intent = new Intent(this, SearchActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("email",email);
        bundle.putString("name",name);
        bundle.putString("phone",phone);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void loadHotel(final String loc, final String mail) {
        class LoadHotel extends AsyncTask<Void,Void,String> {

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("location",loc);
                hashMap.put("email",mail);
                RequestHandler rh = new RequestHandler();
                hotellist = new ArrayList<>();
                String s = rh.sendPostRequest
                        ("http://supersensitive-dabs.000webhostapp.com/load_hotel.php",hashMap);
                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                // Toast.makeText(MainActivity.this, s, Toast.LENGTH_LONG).show()
                hotellist.clear();
                try{
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray hotelarray = jsonObject.getJSONArray("hotel");
                    for (int i=0;i<hotelarray.length();i++){
                        JSONObject c = hotelarray.getJSONObject(i);
                        String inid = c.getString("invoiceid");
                        String hid = c.getString("hotelid");
                        String rid = c.getString("roomid");
                        String hname = c.getString("hotelname");
                        String rname = c.getString("roomname");
                        String rtype = c.getString("roomtype");
                        String hloc = c.getString("location");
                        String hdate = c.getString("startdate");
                        String hdated = c.getString("enddate");
                        String htime = c.getString("starttime");
                        String htimed = c.getString("endtime");
                        HashMap<String,String> hotellisthash = new HashMap<>();
                        hotellisthash.put("invoiceid",inid);
                        hotellisthash.put("hotelid",hid);
                        hotellisthash.put("roomid",rid);
                        hotellisthash.put("hotelname",hname);
                        hotellisthash.put("roomname",rname);
                        hotellisthash.put("roomtype",rtype);
                        hotellisthash.put("location",hloc);
                        hotellisthash.put("startdate",hdate);
                        hotellisthash.put("enddate",hdated);
                        hotellisthash.put("starttime",htime);
                        hotellisthash.put("endtime",htimed);
                        hotellist.add(hotellisthash);
                    }
                }catch (final JSONException e){
                    Log.e("JSONERROR",e.toString());
                }

                ListAdapter adapter = new CustomAdapter(
                        MainActivity.this, hotellist,
                        R.layout.cust_list_hotel, new String[]
                        {"hotelname","roomtype","location","startdate","starttime"}, new int[]
                        {R.id.textView8,R.id.textView9,R.id.textView10,R.id.textView50,R.id.textView51});
                lvhotels.setAdapter(adapter);
            }

        }
        LoadHotel loadHotel = new LoadHotel();
        loadHotel.execute();
    }
}
