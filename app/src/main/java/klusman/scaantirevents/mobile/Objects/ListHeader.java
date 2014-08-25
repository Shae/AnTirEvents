package klusman.scaantirevents.mobile.Objects;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Calendar;

import klusman.scaantirevents.R;

/**
 * Created by Narrook on 8/23/14.
 */
public class ListHeader {

    String HeaderDate;
    View v;

    public ListHeader(Context context, Calendar calDate){
        String Month = getMonthString(calDate.get(Calendar.MONTH));
        String Year = String.valueOf(calDate.get(Calendar.YEAR));
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.cell_date_header, null);
        v.setTag("Header");
        TextView tv = (TextView) v.findViewById(R.id.cell_header_text);
        tv.setText(Month + ", " + Year);

    }

    public String getMonthString(int monthInt){
        String month = "";
        switch (monthInt){
            case 0:
                month = "January";
                break;
            case 1:
                month = "February";
                break;
            case 2:
                month = "March";
                break;
            case 3:
                month = "April";
                break;
            case 4:
                month = "May";
                break;
            case 5:
                month = "June";
                break;
            case 6:
                month = "July";
                break;
            case 7:
                month = "August";
                break;
            case 8:
                month = "September";
                break;
            case 9:
                month = "October";
                break;
            case 10:
                month = "November";
                break;
            case 11:
                month = "December";
                break;

            default:
                month = "TBD";
                break;

        }
        return month;
    }

    public View getView(){
        return v;
    }

}
