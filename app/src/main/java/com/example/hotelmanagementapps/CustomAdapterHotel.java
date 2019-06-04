package com.example.hotelmanagementapps;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomAdapterHotel extends SimpleAdapter {

    private Context mContext;
    public LayoutInflater inflater=null;
    public CustomAdapterHotel(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        mContext = context;
        inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        try{
            if(convertView==null)
                vi = inflater.inflate(R.layout.list_hotel, null);
            HashMap<String, Object> data = (HashMap<String, Object>) getItem(position);
            TextView hotelname = vi.findViewById(R.id.textView28);
            TextView tvloc = vi.findViewById(R.id.textView29);
            TextView tvcity = vi.findViewById(R.id.textView30);
            TextView tvrate = vi.findViewById(R.id.textView31);
            CircleImageView imgrest =vi.findViewById(R.id.hotelimg);
            String hname = (String) data.get("hotelname");
            String hloc =(String) data.get("location");
            String hcity =(String) data.get("city");
            String hrate = (String) data.get("rate");
            String hid=(String) data.get("hotelid");
            hotelname.setText(hname);
            tvloc.setText(hloc);
            tvcity.setText(hcity);
            tvrate.setText(hrate);
            String image_url = "http://supersensitive-dabs.000webhostapp.com/hotelpicture/"+hid+".jpg";
            Picasso.with(mContext).load(image_url).memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE)
                    .fit().into(imgrest);

        }catch (IndexOutOfBoundsException e){

        }

        return vi;
    }
}
