package klusman.scaantirevents.mobile.Tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;
import klusman.scaantirevents.mobile.Dao.EventDao;
import klusman.scaantirevents.mobile.MyActivity;
import klusman.scaantirevents.mobile.MyApp;
import klusman.scaantirevents.mobile.Objects.Event;

public class PullAllEvents
{


    public interface AsyncListener
    {
        public void recieveAsyncData(List<JSONObject> jList);
    }

    Context context;
    List<JSONObject> jList = new ArrayList<JSONObject>();

    public PullAllEvents(Context context)
    {
        this.context = context;

    }

    public void pullAll()
    {

        MyAsyncTask mat = new MyAsyncTask();
        mat.execute();


    }



    class MyAsyncTask extends AsyncTask<String, String, String> {


        private ProgressDialog progressDialog = new ProgressDialog(context);
        InputStream inputStream = null;
        String result = "";

        protected void onPreExecute() {
            Log.i("TAG", "Pre pull");
            progressDialog.setMessage("Downloading your data...");
            progressDialog.show();
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                public void onCancel(DialogInterface arg0) {
                    MyAsyncTask.this.cancel(true);
                }
            });
        }

        @Override
        protected String doInBackground(String... params)
        {
            Log.i("TAG", "Start pull");

            StringBuilder builder = new StringBuilder();
            HttpClient client = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet("http://scalac.herokuapp.com");
            try {
                HttpResponse response = client.execute(httpGet);
                StatusLine statusLine = response.getStatusLine();
                int statusCode = statusLine.getStatusCode();
                if (statusCode == 200) {
                    HttpEntity entity = response.getEntity();
                    InputStream content = entity.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                    }

                } else {
                    Log.e("TAG", "Failed to download file");
                }
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }



            return builder.toString();
        } // protected Void doInBackground(String... params)

        protected void onPostExecute(String result) {

            parseJSON(result);
            progressDialog.dismiss();



        } // protected void onPostExecute(Void v)
    } //class MyAsyncTask extends AsyncTask<String, String, Void>



    public void parseJSON(String JSONstring){

        try
        {
            JSONArray jsonArray = new JSONArray(JSONstring);
            Log.i("TAG", "Number of entries " + jsonArray.length());
            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Log.i("TAG", jsonObject.getString(""));
                if(jsonObject.getString("type").compareToIgnoreCase("VEVENT") == 0  ){
                    jList.add(jsonObject);
                }
            }
            if(jList.size() > 0 ){

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

//        MyActivity ma = new MyActivity();
//        ma.recieveAsyncData(jList);

    }
}
