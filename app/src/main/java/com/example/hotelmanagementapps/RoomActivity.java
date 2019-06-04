package com.example.hotelmanagementapps;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RoomActivity extends AppCompatActivity {

    TextView tvhotelname, tvlocation, tvcity, tvrate;
    ImageView imgHotel;
    ListView lvroom;
    ArrayList<HashMap<String,String>> roomlist;
    Dialog myDialogWindow;
    String email, name, phone, hotelid, hotelname, location;
    Button bcksearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        hotelid = bundle.getString("hotelid");
        hotelname = bundle.getString("hotelname");
        location = bundle.getString("location");
        String hcity = bundle.getString("city");
        String hrate = bundle.getString("rate");
        email = bundle.getString("email");
        name = bundle.getString("name");
        phone = bundle.getString("phone");
        initView();
        tvhotelname.setText(hotelname);
        tvlocation.setText(location);
        tvcity.setText(hcity);
        tvrate.setText(hrate);
        Picasso.with(this).load("http://supersensitive-dabs.000webhostapp.com/hotelpicture/"+hotelid+".jpg")
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .fit().into(imgHotel);
        lvroom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showRoomDetail(position);
            }
        });
        bcksearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backHotel();
            }
        });
        loadRooms(hotelid);
    }

    private void initView(){
        roomlist = new ArrayList<>();
        imgHotel = findViewById(R.id.imageView5);
        tvhotelname = findViewById(R.id.textView22);
        tvlocation = findViewById(R.id.textView23);
        tvcity = findViewById(R.id.textView24);
        tvrate = findViewById(R.id.textView25);
        lvroom = findViewById(R.id.listviewroom);
        bcksearch = findViewById(R.id.backhotel);
    }

    private void showRoomDetail(int p) {
        myDialogWindow = new Dialog(this, android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth);//Theme_DeviceDefault_Dialog_NoActionBar
        myDialogWindow.setContentView(R.layout.dialog_window);
        myDialogWindow.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        TextView tvrname,tvrtype,tvrquan;
        final ImageView imgroom = myDialogWindow.findViewById(R.id.imageViewRoom);
        final EditText sdated = myDialogWindow.findViewById(R.id.sdate);
        final EditText edated = myDialogWindow.findViewById(R.id.edate);
        final Spinner spstime = myDialogWindow.findViewById(R.id.stimespn);
        final Spinner spetime = myDialogWindow.findViewById(R.id.etimespn);
        Button btnbook = myDialogWindow.findViewById(R.id.confirmbook);
        tvrname= myDialogWindow.findViewById(R.id.textView51);
        tvrtype = myDialogWindow.findViewById(R.id.textView53);
        tvrquan = myDialogWindow.findViewById(R.id.textView55);
        tvrname.setText(roomlist.get(p).get("roomname"));
        tvrtype.setText(roomlist.get(p).get("roomtype"));
        tvrquan.setText(roomlist.get(p).get("availability"));
        final String rid = roomlist.get(p).get("roomid");
        final String roomname = roomlist.get(p).get("roomname");
        final String roomtype = roomlist.get(p).get("roomtype");
        final String quanroom = roomlist.get(p).get("availability");
        btnbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sdate = sdated.getText().toString();
                String edate = edated.getText().toString();
                String stime = spstime.getSelectedItem().toString();
                String etime = spetime.getSelectedItem().toString();
                dialogBook(email,hotelid,hotelname,location,rid,roomname,roomtype,quanroom,sdate,edate,stime,etime);
            }
        });

        Picasso.with(this).load("http://supersensitive-dabs.000webhostapp.com/roompicture/"+rid+".jpg")
                .memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE)
                .fit().into(imgroom);
        myDialogWindow.show();
    }

    private void dialogBook(final String email, final String hotelid, final String hotelname, final String location, final String rid, final String roomname,
                            final String roomtype, final String quanroom,
                            final String sdate, final String edate, final String stime, final String etime) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(hotelname+" ("+roomname+")");

        alertDialogBuilder
                .setMessage("Confirm to book?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mathBooked(email,hotelid,hotelname,location,rid,quanroom,roomname,roomtype,sdate,edate,stime,etime);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void mathBooked(final String email, final String hotelid, final String hotelname, final String location, final String rid, final String quanroom,
                            final String roomname, final String roomtype,
                            final String sdate, final String edate, final String stime, final String etime){
        class MathBooked extends AsyncTask<Void,Void,String>{
            @Override
            protected String doInBackground(Void...voids){
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("roomid",rid);
                hashMap.put("availability",quanroom);
                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest("http://supersensitive-dabs.000webhostapp.com/substraction.php",hashMap);
                return s;
            }
            @Override
            protected void onPostExecute(String s){
                super.onPostExecute(s);
                if (s.equalsIgnoreCase("success")){
                    insertBooked(email,hotelid,hotelname,location,rid,roomname,roomtype,sdate,edate,stime,etime);
                }
                else{
                    Toast.makeText(RoomActivity.this, "Failed to book the room", Toast.LENGTH_SHORT).show();
                }
            }
        }
        MathBooked mathBooked = new MathBooked();
        mathBooked.execute();
    }

    private void insertBooked(final String email, final String hotelid, final String hotelname, final String location, final String rid, final String roomname, final String roomtype,
                              final String sdate, final String edate, final String stime, final String etime) {
        class InsertBooked extends AsyncTask<Void,Void,String> {

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("email",email);
                hashMap.put("hotelid",hotelid);
                hashMap.put("hotelname",hotelname);
                hashMap.put("location",location);
                hashMap.put("roomid",rid);
                hashMap.put("roomname",roomname);
                hashMap.put("roomtype",roomtype);
                hashMap.put("startdate",sdate);
                hashMap.put("enddate",edate);
                hashMap.put("starttime",stime);
                hashMap.put("endtime",etime);
                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest("http://supersensitive-dabs.000webhostapp.com/insert_booked.php",hashMap);
                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //Toast.makeText(RoomActivity.this,s, Toast.LENGTH_SHORT).show();
                if (s.equalsIgnoreCase("success")){
                    Toast.makeText(RoomActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    myDialogWindow.dismiss();
                    loadRooms(hotelid);
                }else{
                    Toast.makeText(RoomActivity.this, s, Toast.LENGTH_SHORT).show();
                }
            }

        }
        InsertBooked insertBooked = new InsertBooked();
        insertBooked.execute();
    }

    private void loadRooms(final String hotelid) {
        class LoadRoom extends AsyncTask<Void,Void,String>{

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("hotelid",hotelid);
                RequestHandler requestHandler = new RequestHandler();
                String s = requestHandler.sendPostRequest("http://supersensitive-dabs.000webhostapp.com/load_room.php",hashMap);
                return s;
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                roomlist.clear();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray roomarray = jsonObject.getJSONArray("room");
                    for (int i = 0; i < roomarray.length(); i++) {
                        JSONObject c = roomarray.getJSONObject(i);
                        String rid = c.getString("roomid");
                        String rname = c.getString("roomname");
                        String rsize = c.getString("roomtype");
                        String ravail = c.getString("availability");
                        HashMap<String,String> roomlisthash = new HashMap<>();
                        roomlisthash.put("roomid",rid);
                        roomlisthash.put("roomname",rname);
                        roomlisthash.put("roomtype",rsize);
                        roomlisthash.put("availability",ravail);
                        roomlist.add(roomlisthash);
                    }
                }catch(JSONException e){

                }
                ListAdapter adapter = new CustomAdapterRoom(
                        RoomActivity.this, roomlist,
                        R.layout.cust_list_room, new String[]
                        {"roomname","roomtype","availability"}, new int[]
                        {R.id.textView70,R.id.textView71,R.id.textView72});
                lvroom.setAdapter(adapter);
            }
        }
        LoadRoom loadRoom = new LoadRoom();
        loadRoom.execute();
    }

    public void backHotel(){
        Intent intent = new Intent(this, SearchActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("email",email);
        bundle.putString("name",name);
        bundle.putString("phone",phone);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
