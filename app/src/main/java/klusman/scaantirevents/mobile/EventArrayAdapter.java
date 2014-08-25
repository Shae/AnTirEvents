package klusman.scaantirevents.mobile;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import klusman.scaantirevents.R;

/**
 * Created by Narrook on 8/23/14.
 */
public class EventArrayAdapter extends ArrayAdapter<View>{

    Context context;

    List<View> vList = new ArrayList<View>();

    public EventArrayAdapter(Context context, int resource, List<View> objects) throws ParseException {
        super(context, resource, objects);

        this.context = context;
        this.vList = objects;

    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        View v = vList.get(position);

        if(v.getTag().equals("Event")){
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView id = (TextView) v.findViewById(R.id.cell_event_id);
                    Intent goToDetail = new Intent(context, EventDetail.class);
                    String s = id.getText().toString();
                    Log.i("TAG", "Cell id = " + s);
                    goToDetail.putExtra("ID", s);
                    context.startActivity(goToDetail);
                }
            });
        }

        return v;

    }



}
