package com.example.hotelmanagementapps;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.NetworkPolicy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomAdapter extends SimpleAdapter {

    private Context mContext;
    public LayoutInflater inflater=null;
    public CustomAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        mContext = context;
        inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        try{
            if(convertView==null)
                vi = inflater.inflate(R.layout.cust_list_hotel, null);
            HashMap<String, Object> data = (HashMap<String, Object>) getItem(position);
            TextView hotelname = vi.findViewById(R.id.textView8);
            TextView roomtype = vi.findViewById(R.id.textView9);
            TextView tvloc = vi.findViewById(R.id.textView10);
            TextView tvdate = vi.findViewById(R.id.textView50);
            TextView tvtime = vi.findViewById(R.id.textView51);
            CircleImageView imghotel =vi.findViewById(R.id.hotelView);
            String hname = (String) data.get("hotelname");
            String rtype =(String) data.get("roomtype");
            String hloc =(String) data.get("location");
            String hdate =(String) data.get("startdate");
            String htime = (String) data.get("starttime");
            String hid=(String) data.get("hotelid");
            hotelname.setText(hname);
            roomtype.setText(rtype);
            tvloc.setText(hloc);
            tvdate.setText(hdate);
            tvtime.setText(htime);
            String image_url = "http://supersensitive-dabs.000webhostapp.com/hotelpicture/"+hid+".jpg";
            Picasso.with(mContext).load(image_url).memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE)
                    .fit().into(imghotel);

        }catch (IndexOutOfBoundsException e){

        }

        return vi;
    }
}
