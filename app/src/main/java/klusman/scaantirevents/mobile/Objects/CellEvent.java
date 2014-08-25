package klusman.scaantirevents.mobile.Objects;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import klusman.scaantirevents.R;

/**
 * Created by Narrook on 8/24/14.
 */
public class CellEvent {


    View v;
    Context c;
    Event e;
    String id;

    public CellEvent(Context context, Event event){
        c = context;
        e = event;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.main_event_list_cell, null);
        v.setTag("Event");
        id = event.getEventId();
        String sDate = null;

        TextView title = (TextView) v.findViewById(R.id.cell_title);
        title.setText(e.getEventName());

        TextView location = (TextView) v.findViewById(R.id.cell_Location);
        String loc = getCleanLocation(e.getEventLocation());
        location.setText(loc);

        TextView eId = (TextView) v.findViewById(R.id.cell_event_id);
        eId.setText(e.getEventId());


        TextView date = (TextView) v.findViewById(R.id.cell_date_range);
        try {
            sDate = dateConverter(e.getEventStart(), e.getEventEnd());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        date.setText(sDate);

    }

    private String getCleanLocation(String location){
        String finalField = "";
        String[] fields = location.split("->");
        finalField = fields[0];

        String[] fieldsAddress = fields[0].split("[0-9]", 2);
        finalField = fieldsAddress[0] ;


        return finalField;

    }

    private String dateConverter(String start, String end) throws ParseException
    {

        String eventRunDate = "";
        String eventStartMini = "";

        if (start != null)
        {
            Calendar cStart = calDateFromString(start);

            String Day = String.valueOf(cStart.get(Calendar.DAY_OF_MONTH));
            String Month = getMonthString(cStart.get(Calendar.MONTH));
           // String Year = String.valueOf(cStart.get(Calendar.YEAR));

            eventRunDate = Month + " " + Day;  //removed the year at this time
            eventStartMini = Month + " " + Day;

            if (end != null)
            {
                Calendar cEnd = calDateFromString(end);

                String eDay = String.valueOf(cEnd.get(Calendar.DAY_OF_MONTH));
                String eMonth = getMonthString(cEnd.get(Calendar.MONTH));
               // String eYear = String.valueOf(cEnd.get(Calendar.YEAR));

                if (cEnd.get(Calendar.DAY_OF_MONTH) > cStart.get(Calendar.DAY_OF_MONTH))
                {
                    eventRunDate = (eventStartMini) + "  to  " + eMonth + " " + eDay;  // removed the year at this time
                }

            }
        }
        return eventRunDate;
    }


    public int getIntMonth(String string) throws ParseException
    {
        String s = string;

        Calendar cal = stringToCalendar(s);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int eMonth = cal.get(Calendar.MONTH);
        return eMonth;
    }

    public static Calendar calDateFromString(String string) throws ParseException
    {
        String s = string;
        Calendar cal = stringToCalendar(s);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // System.err.println(sdf.format(cal.getTime()));
        return cal;
    }

    public static Calendar stringToCalendar(String strDate) throws ParseException
    {
        String FORMAT_DATETIME = "yyyy-MM-dd'T'HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_DATETIME);
        Date date = sdf.parse(strDate);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    public String getMonthString(int monthInt)
    {
        String month = "";
        switch (monthInt){
            case 0:
                month = "Jan";
                break;
            case 1:
                month = "Feb";
                break;
            case 2:
                month = "Mar";
                break;
            case 3:
                month = "Apr";
                break;
            case 4:
                month = "May";
                break;
            case 5:
                month = "Jun";
                break;
            case 6:
                month = "Jul";
                break;
            case 7:
                month = "Aug";
                break;
            case 8:
                month = "Sep";
                break;
            case 9:
                month = "Oct";
                break;
            case 10:
                month = "Nov";
                break;
            case 11:
                month = "Dec";
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

    public String getEventID(){
        return id;
    }

    public Event getEvent(){
        return e;
    }

}
