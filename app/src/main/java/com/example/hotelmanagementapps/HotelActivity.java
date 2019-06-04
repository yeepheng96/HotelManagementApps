package com.example.hotelmanagementapps;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class HotelActivity extends AppCompatActivity {

    TextView hotelview, roomnames, roomview, locationview, startdate, enddate, starttime, endtime;
    ImageView imghotel, imgroom;
    String invoiceid, emailid, nameid, phoneid, hotelid, roomid;
    Button dltbook, bckmain;
    ArrayList<HashMap<String,String>> availroom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        invoiceid = bundle.getString("invoiceid");
        hotelid = bundle.getString("hotelid");
        roomid = bundle.getString("roomid");
        final String hotel = bundle.getString("hotelname");
        final String rm = bundle.getString("roomname");
        String room = bundle.getString("roomtype");
        String location = bundle.getString("location");
        String sdated = bundle.getString("startdate");
        String edated = bundle.getString("enddate");
        String stimed = bundle.getString("starttime");
        String etimed = bundle.getString("endtime");
        emailid = bundle.getString("email");
        nameid = bundle.getString("name");
        phoneid = bundle.getString("phone");
        initView();
        hotelview.setText(hotel);
        roomnames.setText(rm);
        roomview.setText(room);
        locationview.setText(location);
        startdate.setText(sdated);
        enddate.setText(edated);
        starttime.setText(stimed);
        endtime.setText(etimed);
        Picasso.with(HotelActivity.this)
                .load("http://supersensitive-dabs.000webhostapp.com/hotelpicture/"+hotelid+".jpg")
                .resize(400, 400)
                .into(imghotel);
        Picasso.with(HotelActivity.this)
                .load("http://supersensitive-dabs.000webhostapp.com/roompicture/"+roomid+".jpg")
                .resize(400, 400)
                .into(imgroom);
        loadAvail(roomid);
        dltbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDelete(invoiceid, roomid, hotel, rm);
            }
        });
        bckmain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToMain();
            }
        });
    }

    private void initView(){
        imghotel = findViewById(R.id.himg);
        imgroom = findViewById(R.id.rimg);
        hotelview = findViewById(R.id.hname);
        roomnames = findViewById(R.id.rname);
        roomview = findViewById(R.id.rtype);
        locationview = findViewById(R.id.loc);
        startdate = findViewById(R.id.sdate);
        enddate = findViewById(R.id.edate);
        starttime = findViewById(R.id.stime);
        endtime = findViewById(R.id.etime);
        dltbook = findViewById(R.id.delbtn);
        bckmain = findViewById(R.id.backbtn);
    }

    private void alertDelete(final String invoiceid, final String roomid, final String hotelname, final String roomname){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Delete hotel: "+hotelname+", room: "+roomname);

        alertDialogBuilder
                .setMessage("Confirm to delete?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mathAvail(invoiceid,roomid);
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

    private void loadAvail(final String roomid) {
        class LoadAvail extends AsyncTask<Void,Void,String>{

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("roomid",roomid);
                RequestHandler requestHandler = new RequestHandler();
                availroom = new ArrayList<>();
                String s = requestHandler.sendPostRequest("http://supersensitive-dabs.000webhostapp.com/load_availability.php",hashMap);
                return s;
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                availroom.clear();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray roomarray = jsonObject.getJSONArray("room");
                    for (int i = 0; i < roomarray.length(); i++) {
                        JSONObject c = roomarray.getJSONObject(i);
                        String ravail = c.getString("availability");
                        HashMap<String,String> roomlisthash = new HashMap<>();
                        roomlisthash.put("availability",ravail);
                        availroom.add(roomlisthash);
                    }
                }catch(JSONException e){

                }
            }
        }
        LoadAvail loadAvail = new LoadAvail();
        loadAvail.execute();
    }

    private void mathAvail(final String invoiceid, final String roomid){
        class MathAvail extends AsyncTask<Void,Void,String>{
            @Override
            protected String doInBackground(Void... voids){
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("roomid",roomid);
                hashMap.put("availability",availroom.get(0).get("availability"));
                RequestHandler requestHandler = new RequestHandler();
                String s = requestHandler.sendPostRequest("http://supersensitive-dabs.000webhostapp.com/addition.php",hashMap);
                return s;
            }
            @Override
            protected void onPostExecute(String s){
                super.onPostExecute(s);
                if (s.equalsIgnoreCase("success")){
                    deleteBooked(invoiceid);
                }else{
                    Toast.makeText(HotelActivity.this, "Failed to delete current room!", Toast.LENGTH_SHORT).show();
                }
            }
        }
        MathAvail mathAvail = new MathAvail();
        mathAvail.execute();
    }

    private void deleteBooked(final String invoiceid){
        class DeleteBooked extends AsyncTask<Void,Void,String>{
            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("invoiceid",invoiceid);
                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest("http://supersensitive-dabs.000webhostapp.com/delete_booked.php",hashMap);
                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (s.equalsIgnoreCase("success")){
                    Toast.makeText(HotelActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(HotelActivity.this,MainActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("email",emailid);
                    bundle.putString("name",nameid);
                    bundle.putString("phone",phoneid);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }else{
                    Toast.makeText(HotelActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        }
        DeleteBooked deleteBooked = new DeleteBooked();
        deleteBooked.execute();
    }

    public void backToMain(){
        Intent intent = new Intent(this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("email",emailid);
        bundle.putString("name",nameid);
        bundle.putString("phone",phoneid);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
