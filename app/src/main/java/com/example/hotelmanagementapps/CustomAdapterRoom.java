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

public class CustomAdapterRoom extends SimpleAdapter {

    private Context mContext;
    public LayoutInflater inflater=null;
    public CustomAdapterRoom(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        mContext = context;
        inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        try{
            if(convertView==null)
                vi = inflater.inflate(R.layout.cust_list_room, null);
            HashMap<String, Object> data = (HashMap<String, Object>) getItem(position);
            TextView roomname = vi.findViewById(R.id.textView70);
            TextView roomtype = vi.findViewById(R.id.textView71);
            TextView availability = vi.findViewById(R.id.textView72);
            CircleImageView imgroom =vi.findViewById(R.id.imageView10);
            String rname = (String) data.get("roomname");
            String rtype =(String) data.get("roomtype");
            String ravail =(String) data.get("availability");
            String rid=(String) data.get("roomid");
            roomname.setText(rname);
            roomtype.setText(rtype);
            availability.setText(ravail);
            String image_url = "http://supersensitive-dabs.000webhostapp.com/roompicture/"+rid+".jpg";
            Picasso.with(mContext).load(image_url).memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE)
                    .fit().into(imgroom);

        }catch (IndexOutOfBoundsException e){

        }

        return vi;
    }
}
